package com.rule.elk.controller;

import com.rule.elk.entity.ProductEntity;
import com.rule.elk.service.ProductSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("elk")
public class ProductController {

    @Autowired
    private ProductSearchService productSearchService;

    @GetMapping("/search")
    public List<ProductEntity> search(@RequestParam("name") String name) {
        Pageable pageable = PageRequest.of(0, 10);
        return productSearchService.search(name, pageable);
    }
}

