package com.rule.service;

import com.alibaba.fastjson.JSON;
import com.rule.dao.TransactionStatementRepository;
import com.rule.dao.UserBalanceRepository;
import com.rule.entity.EventMessage;
import com.rule.entity.PartOrderDTO;
import com.rule.entity.TransactionStatement;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

//@Service
@RestController("/order")
@Api(tags = "Pay", description = "Deduce User balance")
@Slf4j
public class PayService {

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    @Autowired
    private TransactionStatementRepository transactionStatementRepository;

    @PostMapping("/deduceUserBalance")
    @Transactional
    @ApiOperation("扣减用户余额")
    public void deduceUserBalance(@RequestBody EventMessage eventMessage) {

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
            log.error("扣款不成功!");
        } else {
            TransactionStatement transactionStatement = new TransactionStatement();
            transactionStatement.setOrderNo(orderNo);
            transactionStatement.setEntryId(entryId);
            transactionStatement.setEntryDatetime(LocalDateTime.now());
            transactionStatementRepository.save(transactionStatement);
        }
    }

}
