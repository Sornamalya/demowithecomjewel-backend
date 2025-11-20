package com.example.demowithecomjewel.controller;

import com.example.demowithecomjewel.dao.*;
import com.example.demowithecomjewel.dao.UserRepo;
import com.example.demowithecomjewel.dto.AddressDTO;
import com.example.demowithecomjewel.dto.*;
import com.example.demowithecomjewel.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderRepo orderRepo;
    
    @Autowired
    private AddressRepo addressRepo;

    // ✅ Place Order
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody PlaceOrderRequest req) {
        Optional<User> optionalUser = userRepo.findById(req.getUserId());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }
        User user = optionalUser.get();

        if (user.getCart() == null || user.getCart().isEmpty()) {
            return ResponseEntity.status(400).body(Map.of("error", "Cart is empty"));
        }

        // ✅ Fetch existing address (do NOT create a new Address)
        Optional<Address> optionalAddr = addressRepo.findById(req.getAddressId());
        if (optionalAddr.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Address not found"));
        }
        Address addr = optionalAddr.get();

        // Create new order and link existing address
        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setShippingAddress(addr); // reuse existing address

        // Copy cart items
        List<OrderItem> items = new ArrayList<>();
        double subtotal = 0.0;
        for (CartItem c : user.getCart()) {
            OrderItem oi = new OrderItem();
            oi.setProductName(c.getProductName());
            oi.setQuantity(c.getQuantity());
            oi.setPrice(c.getPrice());
            oi.setOrder(order);
            items.add(oi);
            subtotal += c.getPrice() * c.getQuantity();
        }
        order.setItems(items);

        // Calculate total (shipping fixed)
        double totalAmount = subtotal + 60;
        order.setTotalAmount(totalAmount);

        // Save order (this will create order + items; address is already existing)
        orderRepo.save(order);

        // Clear cart and save user
        user.getCart().clear();
        userRepo.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "Order placed successfully",
                "orderId", order.getId(),
                "total", totalAmount
        ));
    }


    // ✅ Fetch Order History
    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getOrderHistory(@PathVariable Long userId) {
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        List<Order> orders = orderRepo.findByUserId(userId);

        List<OrderResponseDTO> dtoList = orders.stream().map(order -> {
            OrderResponseDTO dto = new OrderResponseDTO();
            dto.setOrderId(order.getId());
            dto.setStatus(order.getStatus());
            dto.setTotalAmount(order.getTotalAmount());

            if (order.getShippingAddress() != null) {
                dto.setAddressLine(order.getShippingAddress().getAddressLine());
                dto.setCity(order.getShippingAddress().getCity());
                dto.setState(order.getShippingAddress().getState());
                dto.setZipCode(order.getShippingAddress().getZipCode());
                dto.setContactNumber(order.getShippingAddress().getContactNumber());
            }

            List<OrderResponseDTO.OrderItemDTO> itemDTOs = order.getItems().stream().map(oi -> {
                OrderResponseDTO.OrderItemDTO i = new OrderResponseDTO.OrderItemDTO();
                i.setProductName(oi.getProductName());
                i.setQuantity(oi.getQuantity());
                i.setPrice(oi.getPrice());
                return i;
            }).toList();

            dto.setItems(itemDTOs);
            return dto;
        }).toList();

        return ResponseEntity.ok(dtoList);
    }


 // ✅ DTO for placing order
    public static class PlaceOrderRequest {
        private Long userId;
        private Long addressId; // instead of AddressDTO

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public Long getAddressId() { return addressId; }
        public void setAddressId(Long addressId) { this.addressId = addressId; }
    }


}
