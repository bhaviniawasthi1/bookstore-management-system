package com.bookstore.controller;

import com.bookstore.dto.CartItemDto;
import com.bookstore.entity.*;
import com.bookstore.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;
    private final PaymentService paymentService;

    @GetMapping("/checkout")
    public String checkout(@AuthenticationPrincipal User user, Model model) {
        List<CartItemDto> items = cartService.getCartItems(user);
        BigDecimal total = cartService.getCartTotal(user);

        if (items.isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("paymentMethods", PaymentMethod.values());
        model.addAttribute("user", user);
        return "checkout";
    }

    @PostMapping("/checkout/place")
    public String placeOrder(@AuthenticationPrincipal User user,
                              @RequestParam String shippingAddress,
                              @RequestParam PaymentMethod paymentMethod,
                              RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.createOrder(user, shippingAddress);
            Payment payment = paymentService.processPayment(order, paymentMethod);

            if (payment.getStatus() == PaymentStatus.SUCCESS) {
                redirectAttributes.addFlashAttribute("success", "Order placed successfully! Payment completed.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Order placed but payment failed. Please try again.");
            }

            return "redirect:/orders/" + order.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
    }
}
