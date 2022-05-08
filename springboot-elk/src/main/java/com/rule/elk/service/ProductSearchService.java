package com.rule.elk.service;

import com.rule.elk.entity.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.List;

public interface ProductSearchService {
    List<ProductEntity> search(String name, @PageableDefault(page = 0, value = 10) Pageable pageable);
}
