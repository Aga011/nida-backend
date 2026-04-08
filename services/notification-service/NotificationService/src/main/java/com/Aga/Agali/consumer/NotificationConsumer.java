package com.Aga.Agali.consumer;

import com.Aga.Agali.config.RabbitMQConfig;
import com.Aga.Agali.dto.NotificationDto;
import com.Aga.Agali.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void consume(NotificationDto notificationDto) {
        notificationService.processNotification(notificationDto);
    }
}