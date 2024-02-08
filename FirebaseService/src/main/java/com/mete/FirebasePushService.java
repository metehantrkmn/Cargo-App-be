package com.mete;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirebasePushService{

    private final FirebaseMessaging firebaseMessaging;

    public String sendNotification(Note note) throws FirebaseMessagingException {

        String recipientToken = "dk2KV8QuSZeVSa03RDfDJk:APA91bGkwehiLiO0gRf1e23_M5CRyIt79U7TIPjRKP42uNWTbIhHe8KXchPYMGWIAhxXlAoiajIHkbTB6f3vRXOg2wvWf2j2_kwv6gHM-ZyH0j39KVg-V5RR11er1WVcb23H3TzJlm3o";

        Notification notification = Notification
                .builder()
                .setTitle(note.getSubject())
                .setBody(note.getContent())
                .build();

        Message message = Message
                .builder()
                .setToken(recipientToken)
                .setNotification(notification)
                .putAllData(note.getData())
                .build();

        System.out.println("inside sendNotification DEBUG");
        return firebaseMessaging.send(message);
    }

}