package com.bookstore.service;

import com.bookstore.dto.BookDto;
import com.bookstore.entity.Book;
import com.bookstore.exception.BadRequestException;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
    }

    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAllBooks();
        }
        return bookRepository.searchByTitleOrAuthor(keyword.trim());
    }

    public List<Book> findByCategory(String category) {
        return bookRepository.findByCategory(category);
    }

    @Transactional
    public Book createBook(BookDto bookDto) {
        if (bookRepository.existsByIsbn(bookDto.getIsbn())) {
            throw new BadRequestException("A book with this ISBN already exists");
        }

        Book book = Book.builder()
                .title(bookDto.getTitle())
                .author(bookDto.getAuthor())
                .category(bookDto.getCategory())
                .isbn(bookDto.getIsbn())
                .price(bookDto.getPrice())
                .stock(bookDto.getStock())
                .description(bookDto.getDescription())
                .imageUrl(bookDto.getImageUrl())
                .build();

        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBook(Long id, BookDto bookDto) {
        Book book = findById(id);

        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setCategory(bookDto.getCategory());

        if (!book.getIsbn().equals(bookDto.getIsbn()) && bookRepository.existsByIsbn(bookDto.getIsbn())) {
            throw new BadRequestException("A book with this ISBN already exists");
        }
        book.setIsbn(bookDto.getIsbn());

        book.setPrice(bookDto.getPrice());
        book.setStock(bookDto.getStock());
        book.setDescription(bookDto.getDescription());
        book.setImageUrl(bookDto.getImageUrl());

        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = findById(id);
        bookRepository.delete(book);
    }
}
