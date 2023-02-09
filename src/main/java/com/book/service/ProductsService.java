package com.book.service;

import com.book.model.ProductsEntity;
import com.book.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductsService implements ProductsServiceInterface{
    private final ProductsRepository productsRepository;

    public Optional<Page<ProductsEntity>> findAllProducts(int page, int size) {
        return Optional.of(productsRepository.findAll(PageRequest.of(page, size)));
    }

    @Override
    public Optional<Page<ProductsEntity>> findAllProductsBySort(int page, int size, String sort) {
        Optional<Page<ProductsEntity>> result;
        switch (sort) {
            case "newest" -> result = findAllProductsSortedByIdDesc(page, size);
            case "price_asc" -> result = findAllProductsSortedByPriceAsc(page, size);
            case "price_desc" -> result = findAllProductsSortedByPriceDesc(page, size);
            case "best_seller" -> result = findAllProductsSortedByOrderItemsCount(page, size);
            default -> result = findAllProducts(page, size);
        }
        return result;
    }

    @Override
    public Optional<Page<ProductsEntity>> findAllProductsSortedByIdDesc(int page, int size) {
        return Optional.of(productsRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending())));
    }

    @Override
    public Optional<Page<ProductsEntity>> findAllProductsSortedByPriceAsc(int page, int size) {
        return Optional.of(productsRepository.findAll(PageRequest.of(page, size, Sort.by("productPrice").ascending())));
    }

    @Override
    public Optional<Page<ProductsEntity>> findAllProductsSortedByPriceDesc(int page, int size) {
        return Optional.of(productsRepository.findAll(PageRequest.of(page, size, Sort.by("productPrice").descending())));
    }

    @Override
    public Optional<Page<ProductsEntity>> findAllProductsSortedByOrderItemsCount(int page, int size) {
        return Optional.of(productsRepository.findAllByTotalSold(PageRequest.of(page, size)));
    }

    public Optional<ProductsEntity> findProductById(int id) {
        return productsRepository.findById(id);
    }
}
