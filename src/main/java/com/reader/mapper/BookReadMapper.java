package com.reader.mapper;

import com.reader.dto.AuthorDto;
import com.reader.dto.BookReadDto;
import com.reader.dto.BookmarkReadDto;
import com.reader.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookReadMapper implements Mapper<Book, BookReadDto> {
    private final ChapterReadMapper chapterReadMapper;
    private final AuthorMapper authorMapper;
    private final BookmarkMapper bookmarkMapper;

    @Override
    public BookReadDto map(Book object) {
        AuthorDto author = Optional.ofNullable(object.getAuthor())
                .map(authorMapper::mapFrom)
                .orElse(null);

        List<BookmarkReadDto> bookmarks;
        if (object.getBookmarks() == null) {
            bookmarks = new ArrayList<>();
        } else {
            bookmarks = object.getBookmarks().stream()
                    .map(bookmarkMapper::mapFrom)
                    .collect(Collectors.toList());
        }

        List<String> text = null;
        if (object.getChapters() != null) {
            object.getChapters().sort((o1, o2) -> (int) (o1.getId() - o2.getId()));
            text = object.getChapters().stream()
                    .map(chapterReadMapper::map)
                    .map(c -> {
                        List<String> strings = c.getTitle().stream()
                                .map(t -> {
                                    StringBuilder sb = new StringBuilder(t);
                                    sb.insert(0, "{title}");
                                    return sb.toString();
                                })
                                .collect(Collectors.toCollection(ArrayList::new));
                        strings.addAll(c.getText());
                        return strings;
                    })
                    .flatMap(Collection::stream)
                    .toList();
        }

        return new BookReadDto(
                object.getId(),
                object.getTitle(),
                author,
                object.getImage(),
                text,
                object.getGenre(),
                object.getAnnotation(),
                object.getPathToFile(),
                bookmarks
        );
    }

}

