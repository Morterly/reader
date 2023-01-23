package com.reader.dto;


import com.reader.entity.Genre;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder
public class BookCreateEditDto {
    Long id;
    @NotBlank(message = "Название не может быть пустым")
    String title;
    Long authorId;
    @NotBlank(message = "Аннотация не может быть пустой")
    String annotation;
    String image;
    Genre genre;
    String pathToFile;
}
