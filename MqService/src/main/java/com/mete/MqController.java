package com.mete;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MqController {


    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange;

    public record MailingBody(String routingKey, String email,String name, String pnrNo, Integer templateNumber){}
    @PostMapping("/api/MQ/mailing")
    public ResponseEntity mailing(@RequestBody MailingBody body){
        System.out.println("hey there console !!!!");
        rabbitTemplate.convertAndSend(directExchange.getName(), body.routingKey, body);
        return ResponseEntity.ok("message to mailing service has been sent succesfully...");
    }

    public record NotificationBody(
            String routingKey,
            String subject,
            String content,
            Map<String, String> data
    ){}
    @PostMapping("/api/MQ/notification")
    public ResponseEntity notification(@RequestBody NotificationBody body){
        System.out.println(body);
        rabbitTemplate.convertAndSend(directExchange.getName(), body.routingKey(), body);
        return ResponseEntity.ok("notification request has been send succesfully...");
    }



}
