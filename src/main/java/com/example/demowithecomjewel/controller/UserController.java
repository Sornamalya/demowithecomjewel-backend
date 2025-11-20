package com.example.demowithecomjewel.controller;

import com.example.demowithecomjewel.model.*;
import com.example.demowithecomjewel.dao.*;
import com.example.demowithecomjewel.dto.*;
import com.example.demowithecomjewel.dto.WishlistItemDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private ProductRepo productRepo;
    
    @Autowired
    private OrderRepo orderRepository;
    
    @Autowired
    private AddressRepo addressRepository;


    // ---------------- Register / Login ----------------
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User saved = userRepository.save(user);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("{\"error\":\"Registration failed\"}");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        User user = userRepository.findByEmail(loginUser.getEmail());
        if (user != null && user.getPassword().equals(loginUser.getPassword())) {
            // If you want to exclude passwords:
            user.setPassword(null); 
            return ResponseEntity.ok(user);
        }
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid email or password");
        return ResponseEntity.status(401).body(error);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    // ---------------- CART ----------------
 // ---------------- CART ----------------
//    @PostMapping("/cart/toggle")
//    public ResponseEntity<?> toggleCart(@RequestBody CartRequest req) {
//        Optional<User> optionalUser = userRepository.findById(req.getUserId());
//        if (optionalUser.isEmpty()) return ResponseEntity.status(404).body("{\"error\":\"User not found\"}");
//        User user = optionalUser.get();
//        if (user.getCart() == null) user.setCart(new ArrayList<>());
//
//        Optional<Product> productOpt = productRepo.findById(req.getProductId());
//        if (productOpt.isEmpty()) return ResponseEntity.status(404).body("{\"error\":\"Product not found\"}");
//        Product product = productOpt.get();
//
//        boolean exists = user.getCart().stream()
//                .anyMatch(c -> c.getProduct().getId().equals(product.getId()));
//
//        if (exists) {
//            user.getCart().removeIf(c -> c.getProduct().getId().equals(product.getId()));
//        } else {
//            CartItem newItem = new CartItem();
//            newItem.setProduct(product);
//            newItem.setQuantity(req.getQuantity() == null ? 1 : req.getQuantity());
//            newItem.setUser(user);
//            user.getCart().add(newItem);
//        }
//
//        userRepository.save(user);
//        return ResponseEntity.ok(mapCartToDTO(user.getCart()));
//    }
    
    @PostMapping("/cart/toggle")
    public ResponseEntity<?> toggleCart(@RequestBody CartRequest req) {
        Optional<User> optionalUser = userRepository.findById(req.getUserId());
        if (optionalUser.isEmpty()) return ResponseEntity.status(404).body("{\"error\":\"User not found\"}");
        User user = optionalUser.get();
        if (user.getCart() == null) user.setCart(new ArrayList<>());

        Optional<Product> productOpt = productRepo.findById(req.getProductId());
        if (productOpt.isEmpty()) return ResponseEntity.status(404).body("{\"error\":\"Product not found\"}");
        Product product = productOpt.get();

        Optional<CartItem> existingItem = user.getCart().stream()
                .filter(c -> c.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Remove from cart
            user.getCart().remove(existingItem.get());
        } else {
            // Add to cart
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setProductName(product.getName());
            newItem.setPrice(product.getPrice());
            newItem.setQuantity(req.getQuantity() == null ? 1 : req.getQuantity());
            newItem.setImageUrl(product.getImageUrl());
            newItem.setUser(user);
            user.getCart().add(newItem);
        }

        userRepository.save(user);
        return ResponseEntity.ok(mapCartToDTO(user.getCart()));
    }


    @PostMapping("/cart/update")
    public ResponseEntity<?> updateCartQuantity(@RequestBody CartQuantityRequest req) {
        Optional<User> optionalUser = userRepository.findById(req.getUserId());
        if (optionalUser.isEmpty()) return ResponseEntity.status(404).body("{\"error\":\"User not found\"}");
        User user = optionalUser.get();

        if (user.getCart() != null) {
            for (CartItem item : user.getCart()) {
                if (item.getProductId().equals(req.getProductId())) {
                    item.setQuantity(req.getQuantity());
                    break;
                }
            }
        }

        userRepository.save(user);
        return ResponseEntity.ok(mapCartToDTO(user.getCart()));
    }

    @GetMapping("/{userId}/cart")
    public ResponseEntity<List<CartItemDTO>> getCart(@PathVariable Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return ResponseEntity.ok(List.of());
        User user = optionalUser.get();
        return ResponseEntity.ok(mapCartToDTO(user.getCart()));
    }

    // ---------------- WISHLIST ----------------
 // ---------------- WISHLIST ----------------
//    @PostMapping("/wishlist/toggle")
//    public ResponseEntity<?> toggleWishlist(@RequestBody WishlistRequest req) {
//        Optional<User> optionalUser = userRepository.findById(req.getUserId());
//        if (optionalUser.isEmpty()) return ResponseEntity.status(404).body("{\"error\":\"User not found\"}");
//        User user = optionalUser.get();
//        if (user.getWishlist() == null) user.setWishlist(new ArrayList<>());
//
//        Optional<Product> productOpt = productRepo.findById(req.getProductId());
//        if (productOpt.isEmpty()) return ResponseEntity.status(404).body("{\"error\":\"Product not found\"}");
//        Product product = productOpt.get();
//
//        boolean exists = user.getWishlist().stream()
//                .anyMatch(w -> w.getProduct().getId().equals(product.getId()));
//
//        if (exists) {
//            user.getWishlist().removeIf(w -> w.getProduct().getId().equals(product.getId()));
//        } else {
//            WishlistItem newItem = new WishlistItem();
//            newItem.setProduct(product);
//            newItem.setUser(user);
//            user.getWishlist().add(newItem);
//        }
//
//        userRepository.save(user);
//        return ResponseEntity.ok(mapWishlistToDTO(user.getWishlist()));
//    }
    
    @PostMapping("/wishlist/toggle")
    public ResponseEntity<?> toggleWishlist(@RequestBody WishlistRequest req) {
        Optional<User> optionalUser = userRepository.findById(req.getUserId());
        if (optionalUser.isEmpty()) return ResponseEntity.status(404).body("{\"error\":\"User not found\"}");
        User user = optionalUser.get();
        if (user.getWishlist() == null) user.setWishlist(new ArrayList<>());

        Optional<Product> productOpt = productRepo.findById(req.getProductId());
        if (productOpt.isEmpty()) return ResponseEntity.status(404).body("{\"error\":\"Product not found\"}");
        Product product = productOpt.get();

        Optional<WishlistItem> existingItem = user.getWishlist().stream()
                .filter(w -> w.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Remove from wishlist
            user.getWishlist().remove(existingItem.get());
        } else {
            // Add to wishlist
            WishlistItem newItem = new WishlistItem();
            newItem.setProduct(product);
            newItem.setProductName(product.getName());
            newItem.setPrice(product.getPrice());
            newItem.setImageUrl(product.getImageUrl());
            newItem.setUser(user);
            user.getWishlist().add(newItem);
        }

        userRepository.save(user);
        return ResponseEntity.ok(mapWishlistToDTO(user.getWishlist()));
    }



    @GetMapping("/{userId}/wishlist")
    public ResponseEntity<List<WishlistItemDTO>> getWishlist(@PathVariable Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return ResponseEntity.ok(List.of());
        User user = optionalUser.get();
        return ResponseEntity.ok(mapWishlistToDTO(user.getWishlist()));
    }

    // ---------------- ADDRESS ----------------
    @PostMapping("/address/add")
    public Address addAddress(@RequestBody AddressDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                     .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Address addr = new Address();
        addr.setAddressLine(dto.getAddressLine());
        addr.setCity(dto.getCity());
        addr.setState(dto.getState());
        addr.setZipCode(dto.getZipCode());
        addr.setContactNumber(dto.getContactNumber());
        addr.setUser(user);

        return addressRepository.save(addr);
    }

    
 // ---------------- ADDRESS (fetch / update / delete) ----------------

    @GetMapping("/{userId}/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses(@PathVariable Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.ok(List.of()); // keep behaviour consistent with other endpoints
        }
        User user = optionalUser.get();
        if (user.getAddresses() == null) return ResponseEntity.ok(List.of());

        List<AddressDTO> dtos = user.getAddresses().stream()
                .map(this::addressToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<?> updateAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId,
            @RequestBody AddressDTO dto) {

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));

        User user = optionalUser.get();
        if (user.getAddresses() == null)
            return ResponseEntity.status(404).body(Map.of("error", "Address not found"));

        Optional<Address> addrOpt = user.getAddresses().stream()
                .filter(a -> a.getId() != null && a.getId().equals(addressId))
                .findFirst();

        if (addrOpt.isEmpty())
            return ResponseEntity.status(404).body(Map.of("error", "Address not found"));

        Address addr = addrOpt.get();
        // update fields from DTO
        addr.setAddressLine(dto.getAddressLine());
        addr.setCity(dto.getCity());
        addr.setState(dto.getState());
        addr.setZipCode(dto.getZipCode());
        addr.setContactNumber(dto.getContactNumber());

        userRepository.save(user); // persist changes
        return ResponseEntity.ok(addressToDTO(addr));
    }

    @DeleteMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long userId, @PathVariable Long addressId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));

        User user = optionalUser.get();
        if (user.getAddresses() == null)
            return ResponseEntity.status(404).body(Map.of("error", "Address not found"));

        boolean removed = user.getAddresses().removeIf(a -> a.getId() != null && a.getId().equals(addressId));
        if (!removed) return ResponseEntity.status(404).body(Map.of("error", "Address not found"));

        userRepository.save(user); // persist removal
        return ResponseEntity.ok(Map.of("message", "Address deleted"));
    }


    // ---------------- FAKE PAYMENT ----------------
    @PostMapping("/payment/fake")
    public ResponseEntity<?> fakePayment(@RequestBody PaymentRequest req) {
        Optional<Order> optionalOrder = orderRepository.findById(req.getOrderId());
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Order not found"));
        }

        Order order = optionalOrder.get();
        order.setStatus("PAID"); // Mark as paid
        orderRepository.save(order);

        // Clear user's cart
        User user = order.getUser();
        user.getCart().clear();
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Payment successful", "orderId", order.getId()));
    }
 // DTO for payment request
    public static class PaymentRequest {
        private Long orderId;

        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
    }

    // =================== Mappers ===================
    private List<CartItemDTO> mapCartToDTO(List<CartItem> cart) {
        if (cart == null) return List.of();
        return cart.stream().map(c -> new CartItemDTO(
                c.getProductId(),
                c.getProductName(),
                c.getPrice(),
                c.getQuantity(),
                c.getImageUrl()
        )).collect(Collectors.toList());
    }

    private List<WishlistItemDTO> mapWishlistToDTO(List<WishlistItem> wishlist) {
        if (wishlist == null) return List.of();
        return wishlist.stream().map(w -> new WishlistItemDTO(
                w.getProductId(),
                w.getProductName(),
                w.getPrice(),
                w.getImageUrl()
        )).collect(Collectors.toList());
    }

    // ------------------- Request DTOs -------------------
    
 // ----------------- simple mapper helpers (add near bottom of controller) -----------------
    private AddressDTO addressToDTO(Address a) {
        AddressDTO dto = new AddressDTO();
        dto.setId(a.getId());
        dto.setAddressLine(a.getAddressLine());
        dto.setCity(a.getCity());
        dto.setState(a.getState());
        dto.setZipCode(a.getZipCode());
        dto.setContactNumber(a.getContactNumber());
        dto.setUserId(a.getUser() != null ? a.getUser().getId() : null); // add this line
        return dto;
    }

    
    
    public static class CartRequest {
        private Long userId;
        private Long productId;
        private Integer quantity;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    public static class WishlistRequest {
        private Long userId;
        private Long productId;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
    }

    public static class CartQuantityRequest {
        private Long userId;
        private Long productId;
        private int quantity;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    public static class AddressRequest {
        private Long userId;
//        private Address address;
        private AddressDTO address;
        

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public AddressDTO getAddress() { return address; }
        public void setAddress(AddressDTO address) { this.address = address; }
    }

    public static class IdRequest {
        private Long userId;
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }
}
