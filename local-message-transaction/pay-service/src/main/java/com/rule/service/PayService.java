package com.rule.service;

import com.alibaba.fastjson.JSON;
import com.rule.config.MessageSender;
import com.rule.dao.MessageRepository;
import com.rule.dao.TransactionStatementRepository;
import com.rule.dao.UserBalanceRepository;
import com.rule.entity.EventMessage;
import com.rule.entity.PartOrderDTO;
import com.rule.entity.TransactionStatement;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

//@Service
@RestController("/order")
@Api(tags = "Pay", description = "Deduce User balance")
@Slf4j
public class PayService {

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    @Autowired
    private TransactionStatementRepository transactionStatementRepository;

    @Resource
    private MessageRepository messageRepository;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @PostMapping("/deduceUserBalance")
    @Transactional
    @ApiOperation("扣减用户余额")
    public String deduceUserBalance(@RequestBody EventMessage eventMessage) {

        String payload = eventMessage.getPayload();
        PartOrderDTO partOrderDTO = JSON.parseObject(payload, PartOrderDTO.class);
        Integer orderNo = partOrderDTO.getOid();
        List<TransactionStatement> transactionStatementList = transactionStatementRepository.findAllByOrderNo(orderNo);
        if (!CollectionUtils.isEmpty(transactionStatementList)) {
            throw new RuntimeException("该订单已经支付，请勿重复扣款！");
        }

        BigDecimal price = partOrderDTO.getPrice();
        Integer quantity = partOrderDTO.getQuantity();
        BigDecimal total = price.multiply(new BigDecimal(quantity.toString()));
        Integer entryId = partOrderDTO.getEntryId();
        int update = userBalanceRepository.deduceUserBalance(entryId, total);
        if (update == 0) {
            return "扣款不成功!";
        } else {
            TransactionStatement transactionStatement = new TransactionStatement();
            transactionStatement.setOrderNo(orderNo);
            transactionStatement.setEntryId(entryId);
            transactionStatement.setEntryDatetime(LocalDateTime.now());
            transactionStatementRepository.save(transactionStatement);
            return "扣款成功！";
        }
    }

    @KafkaListener(topics = "PAY_SERVICE_DEDUCE", groupId = "pay-service")
    public void deduceUserBalance(ConsumerRecord<String, String> consumerRecord) {
        String value = consumerRecord.value();
        EventMessage eventMessage = JSON.parseObject(value, EventMessage.class);
        log.info("接收到消息，{}", eventMessage.getPayload());
        String payload = eventMessage.getPayload();
        PartOrderDTO partOrderDTO = JSON.parseObject(payload, PartOrderDTO.class);
        Integer orderNo = partOrderDTO.getOid();
        List<TransactionStatement> transactionStatementList = transactionStatementRepository.findAllByOrderNo(orderNo);
        if (!CollectionUtils.isEmpty(transactionStatementList)) {
            log.warn("该订单已扣减，请勿重复扣减库存！");
            return;
        }

        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transaction = platformTransactionManager.getTransaction(defaultTransactionDefinition);
        // 手动开启事务
        boolean success = false;
        EventMessage updateStatusMsg = new EventMessage();
        try {
            BigDecimal price = partOrderDTO.getPrice();
            Integer quantity = partOrderDTO.getQuantity();
            BigDecimal total = price.multiply(new BigDecimal(quantity.toString()));
            Integer entryId = partOrderDTO.getEntryId();
            int update = userBalanceRepository.deduceUserBalance(entryId, total);
            TransactionStatement transactionStatement = new TransactionStatement();
            transactionStatement.setOrderNo(orderNo);
            transactionStatement.setEntryId(entryId);
            transactionStatement.setEntryDatetime(LocalDateTime.now());
            if (update != 0) {
                transactionStatement.setPayStatus("SUCCESS");
            } else {
                transactionStatement.setPayStatus("FAILED");
            }
            transactionStatement = transactionStatementRepository.save(transactionStatement);
            // 发送消息，修改订单状态
            updateStatusMsg.setPayload(JSON.toJSONString(transactionStatement));
            updateStatusMsg.setMessageId(UUID.randomUUID().toString());
            updateStatusMsg.setEventName("UPDATE_ORDER_STATUS");
            updateStatusMsg.setMessageStatus("待发送");
            updateStatusMsg.setEntryId(eventMessage.getEntryId());
            updateStatusMsg.setEntryDatetime(LocalDateTime.now());
            messageRepository.save(updateStatusMsg);
            // 手动提交事务
            platformTransactionManager.commit(transaction);
            success = true;
        } catch (Exception e) {
            platformTransactionManager.rollback(transaction);
            throw e;
        }
        if (success) {
            // 发送消息到 MQ
            messageSender.send(updateStatusMsg);
        }
    }

}
