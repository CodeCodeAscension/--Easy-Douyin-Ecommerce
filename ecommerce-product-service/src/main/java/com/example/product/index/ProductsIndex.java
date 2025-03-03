package com.example.product.index;

/**
 * 商品索引
 */
public class ProductsIndex {

    /**
     * 索引名称
     */
    public static final String name = "products";

    /**
     * 索引类型
     */
    public static final String type = "_doc";

    /**
     * 商品ID
     */
    public static final String productId = "id";

    /**
     * 商品名称
     */
    public static final String productName = "name";

    /**
     * 描述
     */
    public static final String description = "description";

    /**
     * 价格
     */
    public static final String price = "price";

    /**
     * 销量
     */
    public static final String sold = "sold";

    /**
     * 库存
     */
    public static final String stock = "stock";

    /**
     * 商家名称
     */
    public static final String merchantName = "merchantName";

    /**
     * 分类名称
     */
    public static final String categoryName = "categories";

    /**
     * 状态
     */
    public static final String status = "status";

    /**
     * 创建时间
     */
    public static final String createTime = "createTime";

    /**
     * 更新时间
     */
    public static final String updateTime = "updateTime";

    /**
     * 创建索引库
     */
    public static final String indexTemplate = """
            {
              "mappings": {
                "properties": {
                  "id": { "type": "keyword" },
                  "name": { "type": "text", "analyzer": "ik_max_word" },
                  "description": { "type": "text", "analyzer": "ik_max_word" },
                  "price": { "type": "float" },
                  "sold": { "type": "integer" },
                  "stock": { "type": "integer" },
                  "merchantName": { "type": "keyword" },
                  "categories": { "type": "keyword" },
                  "status": { "type": "integer" },
                  "createTime": {
                    "type": "date",
                    "format": "yyyy-MM-dd'T'HH:mm:ss"
                  },
                  "updateTime": {
                    "type": "date",
                    "format": "yyyy-MM-dd'T'HH:mm:ss"
                  }
                }
              }
            }
            """;
}
