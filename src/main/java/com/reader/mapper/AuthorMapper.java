package com.reader.mapper;


import com.reader.dto.AuthorDto;
import com.reader.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper implements Mapper<AuthorDto, Author> {
    @Override
    public Author map(AuthorDto object) {
        return Author.builder()
                .id(object.getId())
                .fullName(object.getFullName())
                .build();
    }

    public AuthorDto mapFrom(Author author) {
        return AuthorDto.builder()
                .id(author.getId())
                .fullName(author.getFullName())
                .build();
    }
}
