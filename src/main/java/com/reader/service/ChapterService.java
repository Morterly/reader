package com.reader.service;

import com.reader.dto.ChapterCreateDto;
import com.reader.dto.ChapterReadDto;
import com.reader.entity.Chapter;
import com.reader.mapper.ChapterCreateMapper;
import com.reader.mapper.ChapterReadMapper;
import com.reader.repository.ChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChapterService {
    private final ChapterRepository chapterRepository;
    private final ChapterCreateMapper chapterCreateMapper;
    private final ChapterReadMapper chapterReadMapper;

    public ChapterReadDto save(ChapterCreateDto chapterDto) {
        Chapter chapter = chapterCreateMapper.map(chapterDto);
        return Optional.of(chapterRepository.saveAndFlush(chapter))
                .map(chapterReadMapper::map)
                .orElseThrow();
    }
}
