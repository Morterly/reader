package com.reader.service;

import com.reader.dto.BookReadDto;
import com.reader.dto.BookmarkReadDto;
import com.reader.dto.UserReadDto;
import com.reader.entity.Book;
import com.reader.entity.Bookmark;
import com.reader.entity.User;
import com.reader.mapper.BookmarkMapper;
import com.reader.repository.BookRepository;
import com.reader.repository.BookmarkRepository;
import com.reader.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookmarkMapper bookmarkMapper;

    public Optional<BookmarkReadDto> findByBookAndUser(BookReadDto bookDto, UserReadDto userDto) {
        Book book = bookRepository.findById(bookDto.getId()).orElseThrow();
        User user = userRepository.findById(userDto.getId()).orElseThrow();
        return bookmarkRepository.findByBookAndUser(book, user)
                .map(bookmarkMapper::mapFrom);
    }

    public List<BookmarkReadDto> findAllByBook_id(Long id) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByBook_Id(id).orElseThrow();
        return bookmarks.stream()
                .map(bookmarkMapper::mapFrom)
                .collect(Collectors.toList());

    }

    public List<BookmarkReadDto> findAllByUser_id(Long id) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUser_Id(id).orElseThrow();
        return bookmarks.stream()
                .map(bookmarkMapper::mapFrom)
                .collect(Collectors.toList());
    }

    public BookmarkReadDto save(BookmarkReadDto bookmarkDto) {
        Bookmark bookmark = bookmarkMapper.map(bookmarkDto);
        return Optional.of(bookmarkRepository.saveAndFlush(bookmark))
                .map(bookmarkMapper::mapFrom)
                .orElseThrow();
    }
}
