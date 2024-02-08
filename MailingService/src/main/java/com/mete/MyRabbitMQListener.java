package com.mete;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MyRabbitMQListener {

    private final MailingService mailingService;
    private final MailTemplate mailTemplate;

    /*
    @RabbitListener(queues = "mailingQueue")
    public void handleMessage(Body body){
        System.out.println(body.toString());
        mailingService.sendSimpleEmail(body.email(),"cargo application mailing service","hey there this is cargo application");
    }*/
    public record Body(String routingKey, String email,String name, String pnrNo, Integer templateNumber){}

    @RabbitListener(queues = "mailingQueue")
    public void handleMessage(Body body){

        Map<Integer,String> templates = mailTemplate.getTemplates();
        String mail = null;

        if(body.templateNumber == 1) {
            System.out.println(templates.get(1).formatted(body.name));
            mail = templates.get(1).formatted(body.name);
        }else {
            System.out.println(templates.get(body.templateNumber).formatted(body.name,body.pnrNo));
            mail = templates.get(body.templateNumber).formatted(body.name,body.pnrNo);
        }
        System.out.println("debug mail= " + mail);
        mailingService.sendSimpleEmail(body.email(),"cargo application mailing service",mail);
    }

}
