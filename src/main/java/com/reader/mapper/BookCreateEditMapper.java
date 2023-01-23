package com.reader.mapper;


import com.reader.dto.AuthorDto;
import com.reader.dto.BookCreateEditDto;
import com.reader.entity.Author;
import com.reader.entity.Book;
import com.reader.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookCreateEditMapper implements Mapper<BookCreateEditDto, Book> {
    private final AuthorMapper authorMapper;
    private final AuthorService authorService;

    @Override
    public Book map(BookCreateEditDto object) {
        Author author = authorService.findById(object.getAuthorId())
                .map(authorMapper::map)
                .orElseThrow();
        return Book.builder()
                .id(object.getId())
                .title(object.getTitle())
                .author(author)
                .annotation(object.getAnnotation())
                .image(object.getImage())
                .genre(object.getGenre())
                .pathToFile(object.getPathToFile())
                .build();
    }

    public BookCreateEditDto mapFrom(Book book) {
        AuthorDto author = authorMapper.mapFrom(book.getAuthor());
        return BookCreateEditDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .annotation(book.getAnnotation())
                .image(book.getImage())
                .authorId(author.getId())
                .genre(book.getGenre())
                .pathToFile(book.getPathToFile())
                .build();
    }

}
