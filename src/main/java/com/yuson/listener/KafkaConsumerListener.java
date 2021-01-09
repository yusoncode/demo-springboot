//package com.yuson.listener;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
///**
// * 描述
// *
// * @author yuson
// * @date 2020-12-01
// */
////@Component
//public class KafkaConsumerListener {
//    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerListener.class);
//
//    //    @KafkaListener(topics = "test")
////    public void onMessage(String message){
////        log.info(String.format("topic = %s, partition = %s, offset = %d, customer = %s",record.topic(), record.partition(), record.offset(),record.key() ));
////        String content =record.value().toString();
////        System.out.println(message);
////    }
//    @KafkaListener(topics = "test")
//    public void infoTagConsumer(ConsumerRecord record, Acknowledgment acknowledgment) throws IOException {
//        logger.info(String.format("topic = %s, partition = %s, offset = %d, customer = %s",record.topic(), record.partition(), record.offset(),record.key() ));
//        String content =record.value().toString();
//        logger.info(content);
//
//        //手动提交位移
//        if(acknowledgment != null ){
//            acknowledgment.acknowledge();
//        }
//    }
//}
