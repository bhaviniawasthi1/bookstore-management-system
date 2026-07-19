package com.bookstore.service;

import com.bookstore.dto.CartItemDto;
import com.bookstore.entity.*;
import com.bookstore.exception.BadRequestException;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().user(user).build();
                    return cartRepository.save(newCart);
                });
    }

    public List<CartItemDto> getCartItems(User user) {
        Cart cart = getOrCreateCart(user);
        return cart.getItems().stream().map(item -> CartItemDto.builder()
                .id(item.getId())
                .bookId(item.getBook().getId())
                .title(item.getBook().getTitle())
                .author(item.getBook().getAuthor())
                .price(item.getBook().getPrice())
                .quantity(item.getQuantity())
                .subtotal(item.getSubtotal())
                .build()).collect(Collectors.toList());
    }

    public BigDecimal getCartTotal(User user) {
        Cart cart = getOrCreateCart(user);
        return cart.getItems().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getCartItemCount(User user) {
        Cart cart = getOrCreateCart(user);
        return cart.getItems().stream().mapToInt(CartItem::getQuantity).sum();
    }

    @Transactional
    public void addToCart(User user, Long bookId, int quantity) {
        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be positive");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", bookId));

        if (book.getStock() < quantity) {
            throw new BadRequestException("Insufficient stock. Available: " + book.getStock());
        }

        Cart cart = getOrCreateCart(user);

        cartItemRepository.findByCartAndBookId(cart, bookId).ifPresentOrElse(
                existingItem -> {
                    int newQuantity = existingItem.getQuantity() + quantity;
                    if (book.getStock() < newQuantity) {
                        throw new BadRequestException("Insufficient stock. Available: " + book.getStock());
                    }
                    existingItem.setQuantity(newQuantity);
                    cartItemRepository.save(existingItem);
                },
                () -> {
                    CartItem newItem = CartItem.builder()
                            .cart(cart)
                            .book(book)
                            .quantity(quantity)
                            .build();
                    cartItemRepository.save(newItem);
                }
        );
    }

    @Transactional
    public void updateCartItemQuantity(User user, Long itemId, int quantity) {
        if (quantity <= 0) {
            removeFromCart(user, itemId);
            return;
        }

        Cart cart = getOrCreateCart(user);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item", itemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new BadRequestException("Cart item does not belong to this user");
        }

        if (item.getBook().getStock() < quantity) {
            throw new BadRequestException("Insufficient stock. Available: " + item.getBook().getStock());
        }

        item.setQuantity(quantity);
        cartItemRepository.save(item);
    }

    @Transactional
    public void removeFromCart(User user, Long itemId) {
        Cart cart = getOrCreateCart(user);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item", itemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new BadRequestException("Cart item does not belong to this user");
        }

        cartItemRepository.delete(item);
    }

    @Transactional
    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        cartItemRepository.deleteByCart(cart);
    }
}
