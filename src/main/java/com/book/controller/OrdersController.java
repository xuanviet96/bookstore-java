package com.book.controller;

import com.book.dto.OrdersDTO;
import com.book.service.impl.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrdersController {
    private final OrdersService ordersService;

    @PostMapping("/createByCart")
    public ResponseEntity<String> createOrderByCart(@RequestHeader(name = "Authorization") String authHeader) {
        if (Boolean.TRUE.equals(ordersService.createOrderFromCart(authHeader))) {
            return new ResponseEntity<>("Created order successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to create a new order", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/createById")
    public ResponseEntity<String> createOrderById(@RequestHeader(name = "Authorization") String authHeader, @RequestBody OrdersDTO ordersDTO) {
        if (Boolean.TRUE.equals(ordersService.createOrderFromProductId(authHeader, ordersDTO))) {
            return new ResponseEntity<>("Created order successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to create a new order", HttpStatus.BAD_REQUEST);
    }
}
