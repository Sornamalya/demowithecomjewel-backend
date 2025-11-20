//package com.example.demowithecomjewel.service;
//
//import com.example.demowithecomjewel.dao.ProductRepo;
//import com.example.demowithecomjewel.dao.UserRepo;
//import com.example.demowithecomjewel.dto.*;
////import com.example.demowithecomjewel.model.Product;
//import com.example.demowithecomjewel.model.User;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class UserService {
//
//    private final UserRepo userRepository;
//    private final ProductRepo productRepository;
//
//    // Store user-specific data
//    private final Map<Long, CartDto> carts = new HashMap<>();
//    private final Map<Long, WishlistDto> wishlists = new HashMap<>();
//    private final Map<Long, List<AddressDto>> addresses = new HashMap<>();
//    private final Map<Long, List<PaymentDto>> payments = new HashMap<>();
//
//    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    public UserService(UserRepo userRepository, ProductRepo productRepository) {
//        this.userRepository = userRepository;
//        this.productRepository = productRepository;
//    }
//
//    // 🔹 Register
//    public User register(RegisterRequest request) {
//        String hashedPassword = passwordEncoder.encode(request.getPassword()); // hash password
//        User user = new User(request.getName(), request.getEmail(), hashedPassword);
//        return userRepository.save(user);
//    }
//
//    // 🔹 Login
////    public Optional<User> login(LoginRequest request) {
////        return userRepository.findByEmail(request.getEmail())
////                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()));
////        // compare hashed
////    }
//    public Optional<User> login(LoginRequest request) {
//        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
//        System.out.println("Trying login for: " + request.getEmail());
//        if(userOpt.isPresent()) {
//            System.out.println("User found, comparing passwords...");
//            boolean match = passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword());
//            System.out.println("Password match: " + match);
//            if(match) return userOpt;
//        }
//        System.out.println("Login failed");
//        return Optional.empty();
//    }
//
//
//    // 🔹 Toggle Cart
//    public void toggleCart(Long userId, Long productId) {
//        carts.putIfAbsent(userId, new CartDto());
//        CartDto cart = carts.get(userId);
//        CartItemDto existing = cart.findItemByProductId(productId);
//        if (existing != null) {
//            cart.getItems().remove(existing);
//        } else {
//            cart.getItems().add(new CartItemDto(productId, 1));
//        }
//    }
//
//    // 🔹 Toggle Wishlist
//    public void toggleWishlist(Long userId, Long productId) {
//        wishlists.putIfAbsent(userId, new WishlistDto());
//        WishlistDto wishlist = wishlists.get(userId);
//        if (wishlist.getProductIds().contains(productId)) {
//            wishlist.getProductIds().remove(productId);
//        } else {
//            wishlist.getProductIds().add(productId);
//        }
//    }
//
//    // 🔹 Add Address
//    public void addAddress(Long userId, AddressDto address) {
//        addresses.computeIfAbsent(userId, k -> new ArrayList<>()).add(address);
//    }
//
//    // 🔹 Add Payment
//    public void addPayment(Long userId, PaymentDto payment) {
//        payments.computeIfAbsent(userId, k -> new ArrayList<>()).add(payment);
//    }
//
//    // 🔹 Get User Response
//    public UserResponse getUserResponse(Long userId) {
//        User user = userRepository.findById(userId).orElseThrow();
//
//        List<ProductDto> cartProducts = carts.getOrDefault(userId, new CartDto()).getItems()
//                .stream()
//                .map(item -> productRepository.findById(item.getProductId())
//                        .map(p -> new ProductDto(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getImageUrl()))
//                        .orElse(null))
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//
//        List<ProductDto> wishlistProducts = wishlists.getOrDefault(userId, new WishlistDto()).getProductIds()
//                .stream()
//                .map(pid -> productRepository.findById(pid)
//                        .map(p -> new ProductDto(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getImageUrl()))
//                        .orElse(null))
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//
//        return new UserResponse(
//                user.getId(),
//                user.getName(),
//                user.getEmail(),
//                cartProducts,
//                wishlistProducts,
//                addresses.getOrDefault(userId, new ArrayList<>()),
//                payments.getOrDefault(userId, new ArrayList<>())
//        );
//    }
//}
