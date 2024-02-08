package com.mete;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RabbitListenerService {

    private final FirebasePushService firebasePushService;

    public record NotificationBody(
            String routingKey,
            String subject,
            String content,
            Map<String, String> data
    ){}
    @RabbitListener(queues = "notificationQueue")
    public void notificationHandler(NotificationBody body){

        System.out.println("********debug***********");
        System.out.println(body);

        Note note = new Note();
        note.setContent(body.content);
        note.setSubject(body.subject);
        note.setData(body.data);

        try{
            firebasePushService.sendNotification(note);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

}
