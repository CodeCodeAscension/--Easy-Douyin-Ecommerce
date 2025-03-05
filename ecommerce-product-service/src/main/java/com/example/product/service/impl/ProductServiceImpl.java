package com.example.product.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.api.domain.dto.product.*;
import com.example.api.enums.ProductStatusEnum;
import com.example.common.exception.*;
import com.example.product.convert.ProductInfoVoConvert;
import com.example.product.domain.dto.*;
import com.example.product.domain.po.Product;
import com.example.api.domain.vo.product.ProductInfoVo;
import com.example.product.index.ProductsIndex;
import com.example.product.mapper.ProductMapper;
import com.example.product.service.ICategoryService;
import com.example.product.service.IProCateRelService;
import com.example.product.service.IProductService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.beans.FeatureDescriptor;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private ICategoryService iCategoryService;

    @Resource
    private IProCateRelService iProCateRelService;

    @Resource
    private RestHighLevelClient elasticsearchClient;

    @Resource
    private ProductInfoVoConvert productInfoVoConvert;


    // ================================ 商品操作 ================================

    @Transactional
    @Override
    public void createProduct(CreateProductDto createProductDto) throws SystemException {
        // 1. 校验分类是否存在并获取分类ID
        List<Long> categoryIds = iCategoryService.validateAndGetCategoryIds(createProductDto.getCategories());

        // 2. 构建商品实体
        Product product = Product.builder()
                .name(createProductDto.getName())
                .description(createProductDto.getDescription())
                .price(createProductDto.getPrice())
                .sold(0)
                .stock(createProductDto.getStock() != null ? createProductDto.getStock() : 0) // 需要根据业务需求调整
                .merchantName(createProductDto.getMerchantName())
                .status(createProductDto.getStatus())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        // 3. 保存商品
        if (!save(product)) {
            log.error("商品创建失败: {}", createProductDto);
            throw new DatabaseException("商品创建失败");
        }

        // 4. 保存分类关系
        iProCateRelService.saveProductCategories(product.getId(), categoryIds);

        // 5. 同步到Elasticsearch
        syncFullProductToES(product.getId());
    }

    @Transactional
    @Override
    public void createProducts(List<CreateProductDto> createProductDtos) throws SystemException {
        createProductDtos.forEach(this::createProduct);
    }

    @Transactional
    @Override
    public void updateProduct(UpdateProductDto updateProductDto) {
        // 1. 查询现有商品
        Product product = getById(updateProductDto.getId());
        if (product == null) {
            throw new NotFoundException("商品不存在");
        }

        // 2. 更新商品字段（使用BeanUtils处理非空字段）
        BeanUtils.copyProperties(updateProductDto, product, getNullPropertyNames(updateProductDto));

        // 3. 更新分类关系（如果传了categories）
        if (updateProductDto.getCategories() != null) {
            List<Long> newCategoryIds = iCategoryService.validateAndGetCategoryIds(updateProductDto.getCategories());
            iProCateRelService.updateProductCategories(product.getId(), new HashSet<>(newCategoryIds));
        }

        product.setUpdateTime(LocalDateTime.now());

        // 4. 更新商品
        if (!updateById(product)) {
            log.error("商品更新失败: {}", updateProductDto);
            throw new DatabaseException("商品更新失败");
        }

        // 5. 同步到Elasticsearch
        syncFullProductToES(product.getId());
    }

    // ================================ 查询服务 ================================

    @Override
    public ProductInfoVo getProductInfoById(Long productId) throws UserException, SystemException {
        try {
            Product product = this.getById(productId);
            if(product == null) {
                throw new NotFoundException("商品不存在");
            }
            return productInfoVoConvert.convertToProductInfoVo(product);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("数据库异常：{}", ex.getMessage());
            // 数据库异常，尝试从ES中搜索数据
            GetRequest request = new GetRequest(ProductsIndex.name, productId.toString());
            try {
                GetResponse response = elasticsearchClient.get(request, RequestOptions.DEFAULT);
                if (!response.isExists()) {
                    throw new NotFoundException("商品不存在");
                }
                Map<String, Object> source = response.getSourceAsMap();
                return this.convertToVo(source);
            } catch (IOException e) {
                throw new SystemException("ES查询失败", e);
            }
        }
    }

    @Override
    public IPage<ProductInfoVo> getProductInfoByCategory(ListProductsDto listProductsDto) throws UserException, SystemException {
        SearchRequest searchRequest = new SearchRequest(ProductsIndex.name);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 构建查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (StringUtils.hasText(listProductsDto.getCategoryName())) {
            // 使用 wildcard 查询实现模糊匹配
            boolQuery.must(QueryBuilders.wildcardQuery(ProductsIndex.categoryName, "*" + listProductsDto.getCategoryName() + "*"));
        }

        // 分页设置
        int page = listProductsDto.getPage() != null ? listProductsDto.getPage() : 1;
        int size = listProductsDto.getPageSize() != null ? listProductsDto.getPageSize() : 20;
        sourceBuilder.query(boolQuery)
                .from((page - 1) * size)
                .size(size);

        searchRequest.source(sourceBuilder);

        try {
            SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
            List<ProductInfoVo> products = Arrays.stream(response.getHits().getHits())
                    .map(hit -> convertToVo(hit.getSourceAsMap()))
                    .collect(Collectors.toList());

            // 构建分页结果
            Page<ProductInfoVo> pageResult = new Page<>(page, size, Objects.requireNonNull(response.getHits().getTotalHits()).value);
            pageResult.setRecords(products);
            return pageResult;
        } catch (Exception e) {
            throw new DatabaseException("ES查询失败", e);
        }
    }

    @Override
    public IPage<ProductInfoVo> searchProductInfo(SearchProductsDto searchProductsDto) throws UserException, SystemException {
        SearchRequest searchRequest = new SearchRequest(ProductsIndex.name);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // 商品名称模糊查询
        if (StringUtils.hasText(searchProductsDto.getProductName())) {
            boolQuery.must(QueryBuilders.matchQuery(ProductsIndex.productName, searchProductsDto.getProductName()));
//            boolQuery.must(QueryBuilders.fuzzyQuery(ProductsIndex.productName, searchProductsDto.getProductName()).fuzziness(Fuzziness.AUTO));
        }

        // 价格范围查询
        if (searchProductsDto.getPriceLow() != null || searchProductsDto.getPriceHigh() != null) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(ProductsIndex.price);
            if (searchProductsDto.getPriceLow() != null) rangeQuery.gte(searchProductsDto.getPriceLow());
            if (searchProductsDto.getPriceHigh() != null) rangeQuery.lte(searchProductsDto.getPriceHigh());
            boolQuery.filter(rangeQuery);
        }

        // 分类查询（模糊匹配）
        if (StringUtils.hasText(searchProductsDto.getCategoryName())) {
            boolQuery.must(QueryBuilders.wildcardQuery(ProductsIndex.categoryName, "*" + searchProductsDto.getCategoryName() + "*"));
        }

        // 商家名称查询
        if (StringUtils.hasText(searchProductsDto.getMerchantName())) {
            boolQuery.must(QueryBuilders.wildcardQuery(ProductsIndex.merchantName, "*" + searchProductsDto.getMerchantName() + "*"));
        }

        // 销量查询
        if (searchProductsDto.getSold() != null) {
            boolQuery.filter(QueryBuilders.rangeQuery(ProductsIndex.sold).gte(searchProductsDto.getSold()));
        }

        // 库存查询
        if (searchProductsDto.getStock() != null) {
            boolQuery.filter(QueryBuilders.rangeQuery(ProductsIndex.stock).gte(searchProductsDto.getStock()));
        }

        // 分页设置
        int page = searchProductsDto.getPage() != null ? searchProductsDto.getPage() : 1;
        int size = searchProductsDto.getPageSize() != null ? searchProductsDto.getPageSize() : 20;
        sourceBuilder.query(boolQuery)
                .from((page - 1) * size)
                .size(size);

        searchRequest.source(sourceBuilder);

        try {
            SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
            List<ProductInfoVo> products = Arrays.stream(response.getHits().getHits())
                    .map(hit -> convertToVo(hit.getSourceAsMap()))
                    .collect(Collectors.toList());

            Page<ProductInfoVo> pageResult = new Page<>(page, size, Objects.requireNonNull(response.getHits().getTotalHits()).value);
            pageResult.setRecords(products);
            return pageResult;
        } catch (IOException e) {
            throw new DatabaseException("ES查询失败", e);
        }
    }

    // ================================ 库存操作 ================================

    @Override
    public void addProductStock(AddProductDto addProductDto) throws UserException, SystemException {
        // 根据商品ID查询商品信息
        Long productId = addProductDto.getProductId();
        Product product = productMapper.selectById(productId);

        if (product == null) {
            throw new NotFoundException("商品不存在");
        }

        // 增加库存
        Integer addStock = addProductDto.getAddStock();
        product.setStock(product.getStock() + addStock);

        // 同步到ES
        syncProductToES(product);
    }

    /**
     * 减少库存
     * @param decProductDto dto
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    @Override
    public void decProductStock(DecProductDto decProductDto) throws UserException, SystemException {
        // 根据商品ID查询商品信息
        Long productId = decProductDto.getProductId();
        Product product = productMapper.selectById(productId);

        if (product == null) {
            throw new NotFoundException("商品不存在");
        }

        // 商品已下架，无法卖出
        if (product.getStatus() != ProductStatusEnum.PUT_ON) {
            throw new BadRequestException("商品已下架，无法卖出");
        }

        // 减少库存
        Integer decStock = decProductDto.getDecStock();
        if (product.getStock() < decStock) {
            log.error("库存不足，productId: {}", productId);
            throw new BadRequestException("库存不足");
        }

        product.setStock(product.getStock() - decStock);

        // 同步到ES
        syncProductToES(product);
    }

    @Override
    public void addProductSales(AddProductSoldDto addProductSoldDto) throws UserException, SystemException {
        // 根据商品ID查询商品信息
        Long productId = addProductSoldDto.getProductId();
        Product product = productMapper.selectById(productId);

        if (product == null) {
            return;
        }

        // 增加销量
        Integer addSold = addProductSoldDto.getAddSold();
        product.setSold(product.getSold() + addSold);

        // 同步到ES
        syncProductToES(product);

    }


    // ================================ 数据同步 ================================
    /**
     * 更新商品信息（双写MySQL和ES）
     * @param product 商品对象
     * @throws DatabaseException 数据库异常
     */
    @Transactional
    protected void syncProductToES(Product product) throws DatabaseException {
        // 1. 更新MySQL
        int update = productMapper.updateById(product);
        if (update == 0) {
            log.error("更新库存失败，productId: {}", product.getId());
            throw new DatabaseException("更新库存失败");
        }

        // 2. 同步到Elasticsearch
        try {
            UpdateRequest request = new UpdateRequest(ProductsIndex.name, product.getId().toString())
                    .doc(convertToMap(product), XContentType.JSON)
                    .docAsUpsert(true); // 不存在时创建文档

            elasticsearchClient.update(request, RequestOptions.DEFAULT);
            log.info("商品同步到ES成功，ID: {}", product.getId());
        } catch (IOException e) {
            log.error("商品同步到ES失败，ID: {}", product.getId(), e);
            throw new DatabaseException("ES同步失败", e);
        }
    }

    /**
     * 将商品对象转化为Map数据
     * @param product 商品对象
     * @return Map对象
     */
    private Map<String, Object> convertToMap(Product product) {
        Map<String, Object> map = new HashMap<>();
        map.put(ProductsIndex.productId, product.getId());
        map.put(ProductsIndex.productName, product.getName());
        map.put(ProductsIndex.description, product.getDescription());
        map.put(ProductsIndex.price, product.getPrice());
        map.put(ProductsIndex.sold, product.getSold());
        map.put(ProductsIndex.stock, product.getStock());
        map.put(ProductsIndex.merchantName, product.getMerchantName());
        map.put(ProductsIndex.status, product.getStatus().getCode());
        map.put(ProductsIndex.categoryName, iProCateRelService.getProductCategoryNames(product.getId()));
        map.put(ProductsIndex.createTime, product.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        map.put(ProductsIndex.updateTime, product.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        return map;
    }

    /**
     * 将ES查询结果转化为ProductInfoVo对象
     * @param source ES查询结果
     * @return vo对象
     */
    private ProductInfoVo convertToVo(Map<String, Object> source) {
        // 处理 categories 字段
        Object categoriesValue = source.get(ProductsIndex.categoryName);
        List<String> categories = new ArrayList<>();

        if (categoriesValue instanceof List) {
            // 已经是列表类型
            for (Object item : (List<?>) categoriesValue) {
                categories.add(item.toString());
            }
        } else if (categoriesValue instanceof String) {
            // 如果是字符串，按业务规则解析（如逗号分隔）
            String[] parts = ((String) categoriesValue).split(",");
            categories = Arrays.stream(parts)
                    .map(String::trim)
                    .collect(Collectors.toList());
        }
        return ProductInfoVo.builder()
                .id(Long.parseLong(source.get("id").toString()))
                .name((String) source.get("name"))
                .description((String) source.get("description"))
                .price(((Number) source.get("price")).floatValue())
                .sold((Integer) source.get("sold"))
                .stock((Integer) source.get("stock"))
                .merchantName((String) source.get("merchantName"))
                .categories(categories)
                .status(ProductStatusEnum.fromCode((Integer) source.get("status")))
                .createTime(LocalDateTime.parse((String)source.get("createTime")))
                .updateTime(LocalDateTime.parse((String)source.get("createTime")))
                .build();
    }

    /**
     * 完整同步商品数据到ES（包含分类名称）
     * @param productId 商品ID
     */
    private void syncFullProductToES(Long productId) {
        Product product = this.getById(productId);
        // 构建ES文档
        Map<String, Object> doc = this.convertToMap(product);
        log.info(product.getCreateTime().toString());

        // 更新ES
        UpdateRequest request = new UpdateRequest(ProductsIndex.name, productId.toString())
                .doc(doc, XContentType.JSON)
                .docAsUpsert(true);

        try {
            elasticsearchClient.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("ES同步失败，productId: {}", productId, e);
            // 此处可以添加重试逻辑或消息队列处理
        }
    }

    /**
     * 获得对象中属性为null的字段名
     * @param source 对象
     * @return 字符串数组
     */
    private String[] getNullPropertyNames(Object source) {
        BeanWrapper src = new BeanWrapperImpl(source);
        return Arrays.stream(src.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(name -> src.getPropertyValue(name) == null)
                .toArray(String[]::new);
    }
}


