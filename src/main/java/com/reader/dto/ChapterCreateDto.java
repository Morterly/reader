package com.reader.dto;


import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ChapterCreateDto {
    Long id;
    String title;
    List<String> text;
    Long bookId;
}
