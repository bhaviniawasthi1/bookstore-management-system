package com.bookstore.controller;

import com.bookstore.dto.BookDto;
import com.bookstore.entity.Book;
import com.bookstore.entity.Order;
import com.bookstore.entity.OrderStatus;
import com.bookstore.entity.User;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.BookService;
import com.bookstore.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BookService bookService;
    private final OrderService orderService;
    private final UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalBooks", bookService.findAllBooks().size());
        model.addAttribute("totalOrders", orderService.getAllOrders().size());
        model.addAttribute("totalCustomers", userRepository.findAll().stream()
                .filter(u -> u.getRole().name().equals("CUSTOMER")).count());
        return "admin/dashboard";
    }

    @GetMapping("/books")
    public String manageBooks(Model model) {
        model.addAttribute("books", bookService.findAllBooks());
        return "admin/books";
    }

    @GetMapping("/books/add")
    public String addBookForm(Model model) {
        model.addAttribute("bookDto", new BookDto());
        return "admin/book-form";
    }

    @PostMapping("/books/save")
    public String saveBook(@Valid @ModelAttribute("bookDto") BookDto bookDto,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/book-form";
        }
        try {
            bookService.createBook(bookDto);
            redirectAttributes.addFlashAttribute("success", "Book added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/books/add";
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/books/edit/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        BookDto bookDto = BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .category(book.getCategory())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .stock(book.getStock())
                .description(book.getDescription())
                .imageUrl(book.getImageUrl())
                .build();
        model.addAttribute("bookDto", bookDto);
        return "admin/book-form";
    }

    @PostMapping("/books/update")
    public String updateBook(@Valid @ModelAttribute("bookDto") BookDto bookDto,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/book-form";
        }
        try {
            bookService.updateBook(bookDto.getId(), bookDto);
            redirectAttributes.addFlashAttribute("success", "Book updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteBook(id);
            redirectAttributes.addFlashAttribute("success", "Book deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/customers")
    public String viewCustomers(Model model) {
        List<User> customers = userRepository.findAll().stream()
                .filter(u -> u.getRole().name().equals("CUSTOMER"))
                .toList();
        model.addAttribute("customers", customers);
        return "admin/customers";
    }

    @GetMapping("/orders")
    public String viewOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }

    @PostMapping("/orders/update-status")
    public String updateOrderStatus(@RequestParam Long orderId,
                                     @RequestParam OrderStatus status,
                                     RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(orderId, status);
            redirectAttributes.addFlashAttribute("success", "Order status updated!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/orders";
    }
}
