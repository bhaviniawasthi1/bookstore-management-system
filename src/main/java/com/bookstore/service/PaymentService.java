package com.bookstore.service;

import com.bookstore.entity.*;
import com.bookstore.exception.BadRequestException;
import com.bookstore.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment processPayment(Order order, PaymentMethod paymentMethod) {
        if (paymentRepository.findByOrderId(order.getId()).isPresent()) {
            throw new BadRequestException("Payment already processed for this order");
        }

        boolean paymentSuccess = simulatePayment(paymentMethod);

        PaymentStatus status = paymentSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;

        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalAmount())
                .paymentMethod(paymentMethod)
                .status(status)
                .transactionId(generateTransactionId())
                .paymentDate(LocalDateTime.now())
                .build();

        payment = paymentRepository.save(payment);

        if (paymentSuccess) {
            order.setStatus(OrderStatus.CONFIRMED);
        }

        return payment;
    }

    private boolean simulatePayment(PaymentMethod method) {
        if (method == PaymentMethod.COD) {
            return true;
        }
        return Math.random() > 0.1;
    }

    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    public Payment getPaymentByOrder(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElse(null);
    }
}
