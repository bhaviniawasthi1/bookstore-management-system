package com.bookstore.controller;

import com.bookstore.dto.CartItemDto;
import com.bookstore.entity.User;
import com.bookstore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String viewCart(@AuthenticationPrincipal User user, Model model) {
        List<CartItemDto> items = cartService.getCartItems(user);
        BigDecimal total = cartService.getCartTotal(user);
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("itemCount", cartService.getCartItemCount(user));
        return "cart";
    }

    @PostMapping("/add/{bookId}")
    public String addToCart(@AuthenticationPrincipal User user,
                            @PathVariable Long bookId,
                            @RequestParam(defaultValue = "1") int quantity,
                            RedirectAttributes redirectAttributes) {
        try {
            cartService.addToCart(user, bookId, quantity);
            redirectAttributes.addFlashAttribute("success", "Book added to cart!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/books/" + bookId;
    }

    @PostMapping("/update/{itemId}")
    public String updateCartItem(@AuthenticationPrincipal User user,
                                  @PathVariable Long itemId,
                                  @RequestParam int quantity,
                                  RedirectAttributes redirectAttributes) {
        try {
            cartService.updateCartItemQuantity(user, itemId, quantity);
            redirectAttributes.addFlashAttribute("success", "Cart updated!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove/{itemId}")
    public String removeFromCart(@AuthenticationPrincipal User user,
                                  @PathVariable Long itemId,
                                  RedirectAttributes redirectAttributes) {
        cartService.removeFromCart(user, itemId);
        redirectAttributes.addFlashAttribute("success", "Item removed from cart!");
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(@AuthenticationPrincipal User user,
                             RedirectAttributes redirectAttributes) {
        cartService.clearCart(user);
        redirectAttributes.addFlashAttribute("success", "Cart cleared!");
        return "redirect:/cart";
    }
}
