package com.reader.dto;

import com.reader.entity.Genre;
import lombok.Value;

import java.util.List;

@Value
public class BookReadDto {
    Long id;
    String title;
    AuthorDto author;
    String image;
    List<String> text;
    Genre genre;
    String annotation;
    String pathToFile;
    List<BookmarkReadDto> bookmarks;
}
