package com.shipping.app.service;

import com.shipping.app.model.ShippingOrder;
import com.shipping.app.repository.ShippingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShippingOrderService {
    
    @Autowired
    private ShippingOrderRepository repository;
    
    public ShippingOrder createOrder(ShippingOrder order) {
        return repository.save(order);
    }
    
    public ShippingOrder getOrder(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    
    public List<ShippingOrder> getAllOrders() {
        return repository.findAll();
    }
    
    public ShippingOrder updateOrder(Long id, ShippingOrder order) {
        ShippingOrder existingOrder = getOrder(id);
        existingOrder.setStatus(order.getStatus());
        existingOrder.setCost(order.getCost());
        return repository.save(existingOrder);
    }
    
    public void deleteOrder(Long id) {
        repository.deleteById(id);
    }
    
    public ShippingOrder trackOrder(Long id) {
        return getOrder(id);
    }
    
    public ShippingOrder getOrderByNumber(String orderNumber) {
        return repository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}