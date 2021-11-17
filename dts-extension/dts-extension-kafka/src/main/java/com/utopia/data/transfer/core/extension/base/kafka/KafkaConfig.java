package com.utopia.data.transfer.core.extension.base.kafka;

import com.utopia.data.transfer.model.code.entity.kafka.KafkaProperty;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.utils.NetUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author owen.cai
 * @create_date 2021/11/1
 * @alter_author
 * @alter_date
 */
public class KafkaConfig {

    public static Map<String, Object> buildConsumerProperties(KafkaProperty kafka, Pipeline pipeline){
        Map<String, Object> properties = new HashMap(8);
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        properties.putAll(buildPropertiesSsl(kafka.getSsl()));
        properties.putAll(buildPropertiesSecurity(kafka.getSecurity()));
        if (!CollectionUtils.isEmpty(kafka.getProperties())) {
            properties.putAll(kafka.getProperties());
        }
        properties.putAll(buildPropertiesConsumer(kafka.getConsumer()));
        properties.put("group.id", String.valueOf(pipeline.getId()));
        properties.put("enable.auto.commit", "false");
        properties.put("client.id", String.format("dts_%d", pipeline.getId(), NetUtils.getLocalAddress()));
        return properties;
    }

    public static Map<String, Object> buildProducerProperties(KafkaProperty kafka, Pipeline pipeline) {
        Map<String, Object> properties = new HashMap(8);
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        properties.putAll(buildPropertiesSsl(kafka.getSsl()));
        properties.putAll(buildPropertiesSecurity(kafka.getSecurity()));
        if (!CollectionUtils.isEmpty(kafka.getProperties())) {
            properties.putAll(kafka.getProperties());
        }
        properties.putAll(buildPropertiesProducer(kafka.getProducer()));

        properties.put("client.id", String.format("dts_%d", pipeline.getId(), NetUtils.getLocalAddress()));
        return properties;
    }

    protected static Map<String, Object> buildPropertiesProducer(KafkaProperty.Producer kafka) {
        Map<String, Object> properties = new HashMap(8);
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        map.from(kafka::getBootstrapServers).to(value -> properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, value));
        map.from(kafka::getAcks).to(value -> properties.put(ProducerConfig.ACKS_CONFIG,value));
        map.from(kafka::getBatchSize).to(value -> properties.put(ProducerConfig.BATCH_SIZE_CONFIG,value));
        map.from(kafka::getBufferMemory).to(value -> properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG,value));
        map.from(kafka::getCompressionType).whenHasText().to(value -> properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,value));
        map.from(kafka::getRetries).to(value -> properties.put(ProducerConfig.RETRIES_CONFIG,value));

        properties.putAll(buildPropertiesSsl(kafka.getSsl()));
        properties.putAll(buildPropertiesSecurity(kafka.getSecurity()));
        if (!CollectionUtils.isEmpty(kafka.getProperties())) {
            properties.putAll(kafka.getProperties());
        }
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        return properties;
    }

    protected static Map<String, Object> buildPropertiesConsumer(KafkaProperty.Consumer property) {
        Map<String, Object> properties = new HashMap(8);
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        map.from(property::getBootstrapServers).to(value -> properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, value));
        map.from(property::getAutoOffsetReset).to(value -> properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,value));
        map.from(property::getFetchMaxWait).whenNot(item->item <= 0).to(value -> properties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG,value));
        map.from(property::getFetchMinSize).whenNot(item->item <= 0).to(value -> properties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG,value));
        map.from(property::getHeartbeatInterval).whenNot(item->item <= 0).to(value -> properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG,value));
        map.from(property::getMaxPollRecords).whenNot(item->item <= 0).to(value -> properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,value));

        properties.putAll(buildPropertiesSsl(property.getSsl()));
        properties.putAll(buildPropertiesSecurity(property.getSecurity()));
        if (!CollectionUtils.isEmpty(property.getProperties())) {
            properties.putAll(property.getProperties());
        }
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        return properties;
    }

    protected static Map<String, Object> buildPropertiesSsl(KafkaProperty.Ssl ssl) {
        Map<String, Object> properties = new HashMap(8);
        if(Objects.nonNull(ssl)) {
            PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
            map.from(ssl::getKeyPassword).whenHasText().to(value -> properties.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG,value));
            map.from(ssl::getKeyStoreLocation).whenHasText().to(value -> properties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, value));
            map.from(ssl::getKeyStorePassword).whenHasText().to(value -> properties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, value));
            map.from(ssl::getKeyStoreType).whenHasText().to(value -> properties.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, value));
            map.from(ssl::getTrustStoreLocation).whenHasText().to(value -> properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, value));
            map.from(ssl::getTrustStorePassword).whenHasText().to(value -> properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, value));
            map.from(ssl::getTrustStoreType).whenHasText().to(value -> properties.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, value));
            map.from(ssl::getProtocol).whenHasText().to(value -> properties.put(SslConfigs.SSL_PROTOCOL_CONFIG, value));
        }
        return properties;
    }
    public static Map<String, Object> buildPropertiesSecurity(KafkaProperty.Security security) {
        Map<String, Object> properties = new HashMap(8);
        if(Objects.nonNull(security)) {
            PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
            map.from(security::getProtocol).whenHasText().to(value -> properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,value));
        }
        return properties;
    }
}
