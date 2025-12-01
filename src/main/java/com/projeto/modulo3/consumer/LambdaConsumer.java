package com.projeto.modulo3.consumer;

import com.projeto.modulo3.entity.MessageEntity;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
public class LambdaConsumer {

    // Escuta o tópico Kafka e imprime mensagem no console
    @RetryableTopic(
            autoCreateTopics = "false",
            backoff = @Backoff(
                    delay = 15000,       // espera 15s antes de tentar de novo
                    multiplier = 2.0,    // dobra o tempo a cada falha
                    maxDelay = 54000     // máximo de espera 54s
            ),
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    @KafkaListener(
            topics = "${spring.kafka.topic.modulo3}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerFactory"
    )
    public void receive(@Payload ConsumerRecord<String, MessageEntity> record) {
        // Mostra a chave e valor da mensagem
        System.out.println("CHAVE: " + record.key());
        System.out.println("MENSAGEM: " + record.value());
    }
}
