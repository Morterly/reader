package com.reader.mapper;


import com.reader.dto.BookmarkReadDto;
import com.reader.entity.Book;
import com.reader.entity.Bookmark;
import com.reader.entity.User;
import com.reader.repository.BookRepository;
import com.reader.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookmarkMapper implements Mapper<BookmarkReadDto, Bookmark> {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public Bookmark map(BookmarkReadDto object) {
        Book book = bookRepository.findById(object.getBookId()).orElseThrow();
        User user = userRepository.findById(object.getUserId()).orElseThrow();

        return Bookmark.builder()
                .id(object.getId())
                .page(object.getPage())
                .book(book)
                .user(user)
                .build();
    }

    public BookmarkReadDto mapFrom(Bookmark object) {
        return new BookmarkReadDto(
                object.getId(),
                object.getPage(),
                object.getBook().getId(),
                object.getUser().getId()
        );
    }
}
