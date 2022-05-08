package com.rule.elk.repository;

import com.rule.elk.entity.ProductEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductReposiory extends ElasticsearchRepository<ProductEntity, Long> {
}

