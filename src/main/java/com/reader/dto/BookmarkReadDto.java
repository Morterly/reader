package com.reader.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BookmarkReadDto {
    Long id;
    int page;
    Long bookId;
    Long userId;
}
