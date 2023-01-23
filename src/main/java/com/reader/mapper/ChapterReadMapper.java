package com.reader.mapper;


import com.reader.dto.ChapterReadDto;
import com.reader.entity.Chapter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ChapterReadMapper implements Mapper<Chapter, ChapterReadDto> {
    @Override
    public ChapterReadDto map(Chapter object) {
        return new ChapterReadDto(
                object.getId(),
                Arrays.stream(object.getTitle().split("\n")).toList(),
                Arrays.stream(object.getText().split("\n")).toList()
        );
    }
}
