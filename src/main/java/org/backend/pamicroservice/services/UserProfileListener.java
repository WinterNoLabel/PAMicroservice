package org.backend.pamicroservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.springframework.messaging.handler.annotation.Header;
import lombok.RequiredArgsConstructor;
import org.backend.pamicroservice.models.dto.UserRegisteredEvent;
import org.backend.pamicroservice.models.entity.Profile;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserProfileListener {

    private final ObjectMapper objectMapper;

    @Autowired
    private ProfileService profileService;


    @RabbitListener(queues = "user_registered")
    public void handleUserRegisteredEvent(byte[] message, @Header(AmqpHeaders.DELIVERY_TAG) long tag, Channel channel) throws IOException {
        try {
            UserRegisteredEvent event = objectMapper.readValue(message, UserRegisteredEvent.class);

            Profile profile = profileService.createUserProfile(event.getUser_id(), event.getUsername());
            channel.basicAck(tag, false);

        } catch (Exception e) {
            channel.basicNack(tag, false, true);
        }

    }


}
