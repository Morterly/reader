package com.reader.utility;

import com.reader.entity.Genre;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;
import java.util.stream.Stream;

@Converter(autoApply = true)
@Component
public class GenreConverter implements AttributeConverter<Genre, String> {

    @Override
    public String convertToDatabaseColumn(Genre genre) {
        if (Objects.isNull(genre)) {
            return null;
        }
        return genre.getGenre();
    }

    @Override
    public Genre convertToEntityAttribute(String name) {
        if (Objects.isNull(name)) {
            return null;
        }

        return Stream.of(Genre.values())
                .filter(g -> g.getGenre().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
