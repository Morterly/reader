package com.reader.mapper;


import com.reader.dto.BookmarkReadDto;
import com.reader.dto.UserReadDto;
import com.reader.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {
    private final BookmarkMapper bookmarkMapper;

    @Override
    public UserReadDto map(User object) {
        List<BookmarkReadDto> bookmarks;
        if (object.getBookmarks() == null) {
            bookmarks = new ArrayList<>();
        } else {
            bookmarks = object.getBookmarks().stream().map(bookmarkMapper::mapFrom).toList();
        }

        return new UserReadDto(
                object.getId(),
                object.getUsername(),
                object.getRole(),
                bookmarks
        );
    }
}
