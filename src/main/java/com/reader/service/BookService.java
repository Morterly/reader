package com.reader.service;


import com.reader.dto.BookCreateEditDto;
import com.reader.dto.BookReadDto;
import com.reader.entity.Book;
import com.reader.entity.Genre;
import com.reader.mapper.BookCreateEditMapper;
import com.reader.mapper.BookReadMapper;
import com.reader.repository.AuthorRepository;
import com.reader.repository.BookRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BookCreateEditMapper bookCreateEditMapper;
    private final BookReadMapper bookReadMapper;

    public Page<BookReadDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookReadMapper::map);
    }

    public Page<BookReadDto> findAllByGenre(Genre genre, Pageable pageable) {
        return bookRepository.findAllByGenre(genre, pageable)
                .map(bookReadMapper::map);
    }

    public Page<BookReadDto> findAllByAuthor_id(Long id, Pageable pageable) {
        return bookRepository.findAllByAuthor_Id(id, pageable)
                .map(bookReadMapper::map);
    }

    public BookReadDto save(BookCreateEditDto bookDto) {
        Book book = bookCreateEditMapper.map(bookDto);

        return Optional.of(bookRepository.saveAndFlush(book))
                .map(bookReadMapper::map)
                .orElseThrow();
    }

    public BookReadDto findById(Long id) {
        return bookRepository.findById(id)
                .map(bookReadMapper::map)
                .orElseThrow();
    }

    public Page<BookReadDto> findByKeyword(String keyword, Pageable pageable) {
        return bookRepository.findAllByKeyword(keyword, pageable)
                .map(bookReadMapper::map);
    }

    public void delete(Long id) {
        Book book = bookRepository.findById(id).orElseThrow();

        bookRepository.delete(book);
        bookRepository.flush();

        if (book.getAuthor().getBooks().size() == 0) {
            authorRepository.delete(book.getAuthor());
            authorRepository.flush();
        }
    }
}
