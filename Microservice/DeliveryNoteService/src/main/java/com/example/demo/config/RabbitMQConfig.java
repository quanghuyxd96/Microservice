//package com.example.demo.config;
//
//
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//public class RabbitMQConfig {
//
//    @Value("${spring.rabbitmq.username}")
//    private String username;
//
//    @Value("${spring.rabbitmq.password}")
//    private String password;
//
//    @Value("${spring.rabbitmq.host}")
//    private String host;
//
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
//        cachingConnectionFactory.setUsername(username);
//        cachingConnectionFactory.setPassword(password);
//        return cachingConnectionFactory;
//    }
//
//
//    //cách cũ không dùng đến
////    @Bean
////    public Jackson2JsonMessageConverter jsonMessageConverter() {
////        return new Jackson2JsonMessageConverter();
////    }
////
////    @Bean
////    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
////        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
////        rabbitTemplate.setMessageConverter(jsonMessageConverter());
////        return rabbitTemplate;
////    }
//
////    @Bean
////    Queue order() {
////        return new Queue("order-delivery.queue", true);
////    }
////
////    @Bean
////    Exchange myExchange() {
////        return ExchangeBuilder.directExchange(exchange).durable(true).build();
////    }
////
////
////    @Bean
////    Binding bindingOrder() {
////        return BindingBuilder
////                .bind(order())
////                .to(myExchange())
////                .with("order.delivery.routingKey")
////                .noargs();
////    }
//}