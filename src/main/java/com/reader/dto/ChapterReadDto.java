package com.reader.dto;

import lombok.Value;

import java.util.List;

@Value
public class ChapterReadDto {
    Long id;
    List<String> title;
    List<String> text;
}
