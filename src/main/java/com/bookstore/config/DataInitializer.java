package com.bookstore.config;

import com.bookstore.entity.*;
import com.bookstore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * Seeds the fixed demo-account roster and starter catalog on every startup.
 * There is no open self-registration in this app (see DemoAccounts /
 * AuthController) — visitors pick one of these accounts from the login page.
 * Combined with the in-memory H2 database, every restart wipes everything
 * and reseeds this exact same roster, so the live demo never accumulates
 * real user data.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() == 0) {
            createDemoAccounts();
        }
        if (bookRepository.count() == 0) {
            createSampleBooks();
        }
    }

    private void createDemoAccounts() {
        for (DemoAccounts.Account account : DemoAccounts.ALL) {
            User user = User.builder()
                    .name(account.label())
                    .email(account.email())
                    .password(passwordEncoder.encode(account.password()))
                    .role(account.role())
                    .address(account.role() == Role.ADMIN ? "LeafLore HQ, Main Street" : "Demo Address, LeafLore Campus")
                    .phone("1234567890")
                    .build();
            user = userRepository.save(user);

            if (account.role() == Role.CUSTOMER) {
                Cart cart = Cart.builder().user(user).build();
                cartRepository.save(cart);
            }
        }
    }

    private void createSampleBooks() {
        Book[] books = {
            Book.builder().title("The Great Gatsby").author("F. Scott Fitzgerald").category("Fiction")
                .isbn("9780743273565").price(new BigDecimal("12.99")).stock(50)
                .description("A story of the mysteriously wealthy Jay Gatsby and his love for the beautiful Daisy Buchanan.").build(),
            Book.builder().title("To Kill a Mockingbird").author("Harper Lee").category("Fiction")
                .isbn("9780061120084").price(new BigDecimal("14.99")).stock(40)
                .description("The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience that rocked it.").build(),
            Book.builder().title("1984").author("George Orwell").category("Fiction")
                .isbn("9780451524935").price(new BigDecimal("11.99")).stock(35)
                .description("A dystopian social science fiction novel and cautionary tale about the future of totalitarianism.").build(),
            Book.builder().title("Clean Code").author("Robert C. Martin").category("Technology")
                .isbn("9780132350884").price(new BigDecimal("39.99")).stock(25)
                .description("A handbook of agile software craftsmanship that teaches how to write clean, maintainable code.").build(),
            Book.builder().title("Spring Boot in Action").author("Craig Walls").category("Technology")
                .isbn("9781617292545").price(new BigDecimal("44.99")).stock(20)
                .description("A practical guide for building applications with Spring Boot, covering auto-configuration and production-ready features.").build(),
            Book.builder().title("Sapiens").author("Yuval Noah Harari").category("History")
                .isbn("9780062316097").price(new BigDecimal("18.99")).stock(30)
                .description("A brief history of humankind, exploring how biology and history have defined us and enhanced our understanding of what it means to be human.").build(),
            Book.builder().title("The Alchemist").author("Paulo Coelho").category("Fiction")
                .isbn("9780062315007").price(new BigDecimal("13.99")).stock(45)
                .description("A magical fable about following your dream, about the importance of listening to your heart and reading the omens along life's path.").build(),
            Book.builder().title("Introduction to Algorithms").author("Thomas H. Cormen").category("Technology")
                .isbn("9780262033848").price(new BigDecimal("79.99")).stock(15)
                .description("A comprehensive textbook covering a broad range of algorithms in depth, with a focus on implementation and design.").build(),
            Book.builder().title("The Art of War").author("Sun Tzu").category("Non-Fiction")
                .isbn("9781590302255").price(new BigDecimal("9.99")).stock(60)
                .description("An ancient Chinese military treatise that has become a classic of strategy and philosophy for leaders in all fields.").build(),
            Book.builder().title("Dune").author("Frank Herbert").category("Fantasy")
                .isbn("9780441013593").price(new BigDecimal("16.99")).stock(30)
                .description("Set on the desert planet Arrakis, Dune is the story of the boy Paul Atreides, heir to a noble family tasked with ruling an inhospitable world.").build()
        };

        for (Book book : books) {
            bookRepository.save(book);
        }
    }
}
