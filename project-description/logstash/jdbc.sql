SELECT 
    p.id,
    p.name,
    p.description,
    p.price,
    p.sold,
    p.stoke AS stock,  
    p.merchant_name AS merchantName,  
    CASE p.status 
        WHEN '0' THEN 0 
        ELSE 1 
    END AS status,  
    DATE_FORMAT(p.create_time, '%Y-%m-%d %H:%i:%s') AS createTime,  
    DATE_FORMAT(p.update_time, '%Y-%m-%d %H:%i:%s') AS updateTime,
    GROUP_CONCAT(c.category_name) AS categories  
FROM product p
LEFT JOIN product_category_relation pcr ON p.id = pcr.product_id
LEFT JOIN category c ON pcr.category_id = c.id
WHERE p.status IN ('0', '1') 
# 增量查询，过滤掉已经存在且未发生更改的数据
# and (p.update_time > :sql_last_value or c.update_time > :sql_last_value)
GROUP BY p.id  