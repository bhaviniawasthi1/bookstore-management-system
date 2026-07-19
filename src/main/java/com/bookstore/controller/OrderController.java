package com.bookstore.controller;

import com.bookstore.entity.Order;
import com.bookstore.entity.User;
import com.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public String orderHistory(@AuthenticationPrincipal User user, Model model) {
        List<Order> orders = orderService.getUserOrders(user);
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/{id}")
    public String orderDetail(@AuthenticationPrincipal User user, @PathVariable Long id, Model model) {
        Order order = orderService.findById(id);

        if (!order.getUser().getId().equals(user.getId()) && user.getRole().name().equals("CUSTOMER")) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        return "order-detail";
    }
}
