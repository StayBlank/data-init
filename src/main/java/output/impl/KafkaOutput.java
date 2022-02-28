package output.impl;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import output.Output;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class KafkaOutput implements Output {
    @Override
    public void write(List<Map<String, String>> datas, String name) {
        List<String> datasValueStr = datas.stream().map(data -> data.values().stream().map(v -> v + "").collect(Collectors.joining(","))).collect(Collectors.toList());
        Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", "broker1:port1, broker2:port2");
        kafkaProps.put("key.serializer", "org.apache.kafka.common.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.StringSerializer");
        KafkaProducer producer = new KafkaProducer<String, String>(kafkaProps);
        for (String s : datasValueStr) {
            ProducerRecord<String, String> record = new ProducerRecord<>("data-init", "Precision Products", "France");//Topic Key Value
            try {
                Future future = producer.send(record);
//                future.get();//不关心是否发送成功，则不需要这行。
            } catch (Exception e) {
                e.printStackTrace();//连接错误、No Leader错误都可以通过重试解决；消息太大这类错误kafkaProducer不会进行任何重试，直接抛出异常
            }
        }

    }
}
