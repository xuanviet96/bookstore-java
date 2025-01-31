package com.book.service.impl;

import com.book.dto.ProductReviewDTO;
import com.book.model.ProductReviewsEntity;
import com.book.model.UsersEntity;
import com.book.repository.ProductReviewsRepository;
import com.book.service.ProductReviewsServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductReviewsService implements ProductReviewsServiceInterface {
    private final ProductReviewsRepository productReviewsRepository;
    private final UserService userService;
    @Override
    public Optional<List<ProductReviewsEntity>> findAllByProductId(int productsEntityId) {
        return Optional.of(productReviewsRepository.findAllByProductId(productsEntityId));
    }

    @Override
    public Optional<Integer> getAverageRatingByProductId(int productsEntityId) {
        List<ProductReviewsEntity> productReviewsEntityList = productReviewsRepository.findAllByProductId(productsEntityId);
        if (!productReviewsEntityList.isEmpty()) {
            AtomicInteger result = new AtomicInteger();
            productReviewsEntityList.stream().forEach(e -> result.addAndGet(e.getRating()));
            result.getAndUpdate(v -> v / productReviewsEntityList.size());
            return Optional.of(result.get());
        } else {
            return Optional.of(0);
        }

    }

    @Override
    public Boolean saveProductReview(String authHeader, ProductReviewDTO productReviewDTO) {
        Optional<UsersEntity> usersEntity = userService.getUserByJwtToken(authHeader.substring(7));

        if (usersEntity.isPresent()) {
            ProductReviewsEntity productReviewsEntity = new ProductReviewsEntity();
            productReviewsEntity.setUsersEntity(usersEntity.get());
            productReviewsEntity.setProductId(productReviewDTO.getProductId());
            productReviewsEntity.setRating(productReviewDTO.getRating());
            productReviewsEntity.setComment(productReviewDTO.getComment());
            productReviewsEntity.setReviewDate(Date.valueOf(DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now())));

            try {
                productReviewsRepository.save(productReviewsEntity);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public Optional<List<ProductReviewsEntity>> findAll() {
        return Optional.of(productReviewsRepository.findAll());
    }
}
