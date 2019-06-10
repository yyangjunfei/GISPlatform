package cc.wanshan.gis.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class ElasticSearchConfig {

    private Client esClient;

    @Value("${spring.data.elasticsearch.hostName}")
    private String hostName;

    @Value("${spring.data.elasticsearch.transport}")
    private Integer transport;

    @Value("${spring.data.elasticsearch.cluster-name}")
    private String clusterName;

    @Bean
    public TransportClient transportClient() {

        TransportClient transportClient = null;

        try {
            TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(hostName), Integer.valueOf(transport));

            //配置信息
            Settings es = Settings.builder().put("cluster.name", clusterName).build();

            //配置信息Settings自定义
            transportClient = new PreBuiltTransportClient(es);

            transportClient.addTransportAddress(transportAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return transportClient;
    }

    /**
     * 避免TransportClient每次使用创建和释放
     */
    public Client esTemplate() {
        if (StringUtils.isEmpty(esClient) || StringUtils.isEmpty(esClient.admin())) {
            esClient = transportClient();
            return esClient;
        }
        return esClient;
    }


}

