package cc.wanshan.gis.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

@Configuration
public class ElasticSearchConfig {

    @Value("${spring.data.elasticsearch.hostName}")
    private String hostName;

    @Value("${spring.data.elasticsearch.transport}")
    private Integer transport;

    @Value("${spring.data.elasticsearch.cluster-name}")
    private String clusterName;

    @Bean
    public TransportClient getClient() throws Exception {

        //ES地址
        TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(hostName), Integer.valueOf(transport));

        //配置信息
        Settings settings = Settings.builder().put("cluster.name", clusterName).build();

        return new PreBuiltTransportClient(settings).addTransportAddress(transportAddress);
    }
}

