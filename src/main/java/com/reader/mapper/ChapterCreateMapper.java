package com.reader.mapper;


import com.reader.dto.ChapterCreateDto;
import com.reader.entity.Book;
import com.reader.entity.Chapter;
import com.reader.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChapterCreateMapper implements Mapper<ChapterCreateDto, Chapter> {
    private final BookRepository bookRepository;

    @Override
    public Chapter map(ChapterCreateDto object) {
        Book book = bookRepository.findById(object.getBookId()).orElseThrow();

        return Chapter.builder()
                .title(object.getTitle())
                .text(String.join("\n", object.getText()))
                .book(book)
                .build();
    }
}
