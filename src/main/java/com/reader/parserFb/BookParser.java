package com.reader.parserFb;


import com.reader.dto.AuthorDto;
import com.reader.dto.BookCreateEditDto;
import com.reader.dto.BookReadDto;
import com.reader.dto.ChapterCreateDto;
import com.reader.entity.Genre;
import com.reader.parserFb.parser.FictionBook;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class BookParser {
    private final FictionBook fb;
    private final DescriptionParser description;
    private final List<ChapterParser> chapters;

    public BookParser(String path) {
        try {
            this.fb = new FictionBook(new File(path));
            this.description = new DescriptionParser(fb);
            chapters = fb.getBody().getSections().stream()
                    .map(s -> {
                        List<ChapterParser> list = new ArrayList<>();
                        list.add(new ChapterParser(s));
                        if (!s.getSections().isEmpty()) {
                            s.getSections().stream()
                                    .map(ChapterParser::new)
                                    .forEach(list::add);
                        }
                        return list;
                    })
                    .flatMap(Collection::stream)
                    .toList();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthorDto parseAuthor() {
        return AuthorDto.builder()
                .fullName(description.getFullName())
                .build();
    }

    public BookCreateEditDto parseBook(AuthorDto author, Genre genre, String path) {
        return BookCreateEditDto.builder()
                .title(description.getBookTitle())
                .authorId(author.getId())
                .image(description.getImage())
                .annotation(description.getAnnotation())
                .genre(genre)
                .pathToFile(path)
                .build();
    }

    public List<ChapterCreateDto> parseChapter(BookReadDto bookDto) {
        return chapters.stream()
                .map(c -> ChapterCreateDto.builder()
                        .title(c.getTitle())
                        .text(c.getText())
                        .bookId(bookDto.getId())
                        .build())
                .collect(Collectors.toList());

    }
}
