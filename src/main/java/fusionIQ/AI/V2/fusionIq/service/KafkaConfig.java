//package fusionIQ.AI.V2.fusionIq.service;
//
//import fusionIQ.AI.V2.fusionIq.data.Message;
//import org.apache.kafka.clients.admin.NewTopic;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.support.serializer.JsonSerializer;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class KafkaConfig {
//
//    // ProducerFactory for Message type
//    @Bean
//    public ProducerFactory<String, Message> messageProducerFactory() {
//        Map<String, Object> config = new HashMap<>();
//        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        return new DefaultKafkaProducerFactory<>(config);
//    }
//
//    // KafkaTemplate for Message type
//    @Bean
//    public KafkaTemplate<String, Message> messageKafkaTemplate() {
//        return new KafkaTemplate<>(messageProducerFactory());
//    }
//
//    // ProducerFactory for Object[] type (for create-group requests)
//    @Bean
//    public ProducerFactory<String, Object[]> objectArrayProducerFactory() {
//        Map<String, Object> config = new HashMap<>();
//        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        return new DefaultKafkaProducerFactory<>(config);
//    }
//
//    // KafkaTemplate for Object[] type
//    @Bean
//    public KafkaTemplate<String, Object[]> objectArrayKafkaTemplate() {
//        return new KafkaTemplate<>(objectArrayProducerFactory());
//    }
//
//    // Define Kafka topics
//    @Bean
//    public NewTopic chatTopic() {
//        return new NewTopic("chat-messages", 3, (short) 1);
//    }
//
//    @Bean
//    public NewTopic createGroupTopic() {
//        return new NewTopic("create-group", 3, (short) 1);
//    }
//    @Bean
//    public NewTopic deleteMessageTopic() {
//        return new NewTopic("delete-message", 3, (short) 1);
//    }
//
//    @Bean
//    public NewTopic deleteChatTopic() {
//        return new NewTopic("delete-chat", 3, (short) 1);
//    }
//}