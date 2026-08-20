package com.bookstore.config;

import com.bookstore.entity.Role;

import java.util.List;

/**
 * Single source of truth for the demo-account roster: one admin, one
 * student. There's no self-registration in this app on purpose — it's a
 * portfolio demo, so DataInitializer seeds these two accounts on every
 * startup, and the login page lists them so visitors can pick one instead
 * of registering their own. Both accounts can be used by multiple visitors
 * at the same time (no locking) — kept simple since this is just a demo.
 */
public final class DemoAccounts {

    private DemoAccounts() {}

    public record Account(String label, String email, String password, Role role) {}

    public static final List<Account> ALL = List.of(
            new Account("Admin", "admin@leaflore.com", "admin123", Role.ADMIN),
            new Account("Student", "student@leaflore.com", "student123", Role.CUSTOMER)
    );
}
