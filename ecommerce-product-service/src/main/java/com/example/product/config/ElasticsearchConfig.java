package com.example.product.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(RestHighLevelClient.class)
public class ElasticsearchConfig {

    @Value("${ecommerce.elasticsearch.host}")
    private String host;

    @Value("${ecommerce.elasticsearch.port}")
    private int port;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(host, port, "http"))
        );
    }
}