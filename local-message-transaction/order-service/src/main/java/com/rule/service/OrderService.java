package com.rule.service;

import com.alibaba.fastjson.JSON;
import com.rule.config.MessageSender;
import com.rule.dao.MessageRepository;
import com.rule.dao.OrderRepository;
import com.rule.entity.EventMessage;
import com.rule.entity.OrderDTO;
import com.rule.entity.PartOrder;
import com.rule.entity.TransactionStatementDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

//@Service
@RestController("/order")
@Api(tags = "Order", description = "Create Order")
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private MessageSender messageSender;

    @PostMapping("/createOrder")
    @Transactional
    @ApiOperation("创建订单")
    public void createOrder(@RequestBody OrderDTO orderDTO) {

        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transaction = platformTransactionManager.getTransaction(defaultTransactionDefinition);

        PartOrder partOrder = new PartOrder();
        partOrder.setSkuNo(orderDTO.getSkuNo());
        partOrder.setQuantity(orderDTO.getQuantity());
        partOrder.setPrice(orderDTO.getPrice());
        partOrder.setEntryId(orderDTO.getEntryId());
        partOrder.setEntryDatetime(LocalDateTime.now());
        partOrder.setOrderStatus("待扣减");

        EventMessage eventMessage = new EventMessage();
        eventMessage.setMessageId(UUID.randomUUID().toString());
        eventMessage.setEventName("PAY_SERVICE_DEDUCE");
        eventMessage.setMessageStatus("待发送");
        eventMessage.setEntryId(partOrder.getEntryId());
        eventMessage.setEntryDatetime(LocalDateTime.now());
        boolean canSend = false;
        // 手动开启事务
        try {
            PartOrder saveOrder = orderRepository.save(partOrder);

            // 发送扣款消息
            eventMessage.setPayload(JSON.toJSONString(saveOrder));
            messageRepository.save(eventMessage);
            // 手动提交事务
            platformTransactionManager.commit(transaction);
            canSend = true;
        } catch (Exception e) {
            // 手动回滚事务
            platformTransactionManager.rollback(transaction);
        }
        if (canSend) {
            // 发送消息
            messageSender.send(eventMessage);
        }
    }

    @KafkaListener(topics = "UPDATE_ORDER_STATUS", groupId = "order-service")
    @Transactional
    public void updateOrderStatus(ConsumerRecord<String, String> consumerRecord) {
        String value = consumerRecord.value();
        EventMessage eventMessage = JSON.parseObject(value, EventMessage.class);
        String payload = eventMessage.getPayload();
        if (StringUtils.isEmpty(payload) || "null".equals(payload)) {
            return;
        }
        String status = "已履约";
        TransactionStatementDTO statementDTO = JSON.parseObject(payload, TransactionStatementDTO.class);
        String payStatus = statementDTO.getPayStatus();
        Integer orderNo = statementDTO.getOrderNo();
        // 修改订单状态
        orderRepository.updateOrderStatus(orderNo, "SUCCESS".equals(payStatus) ? status : "扣款失败，余额不足");
    }

}
