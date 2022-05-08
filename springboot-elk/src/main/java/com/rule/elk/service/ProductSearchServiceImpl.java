package com.rule.elk.service;

import com.rule.elk.entity.ProductEntity;
import com.rule.elk.repository.ProductReposiory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    @Autowired
    private ProductReposiory productReposiory;

    @Override
    public List<ProductEntity> search(String name, @PageableDefault(page = 0, value = 10) Pageable pageable) {
//        String user = null;
//        System.out.println(user.getBytes());
        // 1.拼接查询条件
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        // 2.模糊查询 name、 subtitle、detail含有 搜索关键字
        builder.must(QueryBuilders.multiMatchQuery(name, "name", "subtitle", "detail"));
        // 3.调用ES接口查询
        Page<ProductEntity> page = productReposiory.search(builder, pageable);
        // 4.获取集合数据
//        int a = 1/0;
        return page.getContent();
    }

}