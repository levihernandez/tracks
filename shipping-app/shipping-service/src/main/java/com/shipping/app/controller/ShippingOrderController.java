package com.shipping.app.controller;

import com.shipping.app.model.ShippingOrder;
import com.shipping.app.service.ShippingOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class ShippingOrderController {
    private final ShippingOrderService shippingOrderService;

    @PostMapping
    public ResponseEntity<ShippingOrder> createOrder(@RequestBody ShippingOrder order) {
        return ResponseEntity.ok(shippingOrderService.createOrder(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShippingOrder> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(shippingOrderService.getOrder(id));
    }

    @GetMapping
    public ResponseEntity<List<ShippingOrder>> getAllOrders() {
        return ResponseEntity.ok(shippingOrderService.getAllOrders());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShippingOrder> updateOrder(@PathVariable Long id, @RequestBody ShippingOrder order) {
        return ResponseEntity.ok(shippingOrderService.updateOrder(id, order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        shippingOrderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/track")
    public ResponseEntity<ShippingOrder> trackOrder(@PathVariable Long id) {
        return ResponseEntity.ok(shippingOrderService.trackOrder(id));
    }

    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<ShippingOrder> getOrderByNumber(@PathVariable String orderNumber) {
        return ResponseEntity.ok(shippingOrderService.getOrderByNumber(orderNumber));
    }
}