package com.example.product.index;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 检查es客户端是否有products索引，如果没有则创建
 */
@Component
@AllArgsConstructor
@Slf4j
public class ProductsIndexInit {

    private final RestHighLevelClient client;

    @EventListener(ApplicationReadyEvent.class)
    public void initProductsIndex() {
        try {
            GetIndexRequest request = new GetIndexRequest(ProductsIndex.name);
            boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
            if (exists) {
                log.info("{}索引已经存在", ProductsIndex.name);
                return;
            }
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(ProductsIndex.name);
            createIndexRequest.source(ProductsIndex.indexTemplate, XContentType.JSON);
            client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            log.info("成功创建索引{}", ProductsIndex.name);
        } catch (Exception e) {
            log.error("初始化ES客户端商品索引发生错误：{}", e.getMessage());
        }
    }
}
