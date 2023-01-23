package com.reader.service;


import com.reader.dto.AuthorDto;
import com.reader.entity.Author;
import com.reader.mapper.AuthorMapper;
import com.reader.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream()
                .map(authorMapper::mapFrom)
                .toList();
    }

    public Optional<AuthorDto> findById(Long id) {
        return authorRepository.findById(id)
                .map(authorMapper::mapFrom);
    }

    public AuthorDto save(AuthorDto authorDto) {
        Author author = authorMapper.map(authorDto);
        return Optional.of(authorRepository.saveAndFlush(author))
                .map(authorMapper::mapFrom)
                .orElseThrow();
    }
}
