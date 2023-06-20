package com.rule.service;

import com.alibaba.fastjson.JSON;
import com.rule.dao.MessageRepository;
import com.rule.dao.OrderRepository;
import com.rule.entity.EventMessage;
import com.rule.entity.OrderDTO;
import com.rule.entity.PartOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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

    @PostMapping("/createOrder")
    @Transactional
    @ApiOperation("创建订单")
    public void createOrder(@RequestBody OrderDTO orderDTO) {

        PartOrder partOrder = new PartOrder();
        partOrder.setSkuNo(orderDTO.getSkuNo());
        partOrder.setQuantity(orderDTO.getQuantity());
        partOrder.setPrice(orderDTO.getPrice());
        partOrder.setEntryId(orderDTO.getEntryId());
        partOrder.setEntryDatetime(LocalDateTime.now());
        //
        PartOrder saveOrder = orderRepository.save(partOrder);

        // 发送扣款消息
        EventMessage eventMessage = new EventMessage();
        eventMessage.setMessageId(UUID.randomUUID().toString());
        eventMessage.setEventName("PAY_SERVICE_DEDUCE");
        eventMessage.setMessageStatus("PREPARE");
        eventMessage.setPayload(JSON.toJSONString(saveOrder));
        eventMessage.setEntryId(partOrder.getEntryId());
        eventMessage.setEntryDatetime(LocalDateTime.now());
        messageRepository.save(eventMessage);
    }

}
