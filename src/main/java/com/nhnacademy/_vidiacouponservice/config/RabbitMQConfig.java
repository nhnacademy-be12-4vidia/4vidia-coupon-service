package com.nhnacademy._vidiacouponservice.config;



import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "coupon.issue.queue";
    public static final String EXCHANGE = "coupon.issue.exchange";
    public static final String ROUTING_KEY = "coupon.issue";


    /**
     *
     * @return MQ재시작해도 큐가 유지되게 true
     */
    @Bean
    public Queue couponQueue() {
        return new Queue(QUEUE, true);
    }

    /**
     * 익스체인지 타입이 4개정도있는데 얘가 라우팅키를 패텅 매칭함
     * 나중에 welcome이랑 birthday도 구현해야해서 얘를 쓰는게 유연함
     */
//    @Bean
//    public TopicExchange couponExchange() {
//        return new TopicExchange(EXCHANGE);
//    }
    @Bean
    public DirectExchange couponExchange() {
        return new DirectExchange(EXCHANGE);
    }
    /**
     * exchange->라우팅키(issue)를 써서 들어온 메시지->queue로 슛
     */
    @Bean
    public Binding couponBinding() {
        return BindingBuilder.bind(couponQueue())
                .to(couponExchange())
                .with(ROUTING_KEY);
    }
}
