package com.reader.dto;


import com.reader.entity.Role;
import lombok.Value;

import java.util.List;

@Value
public class UserReadDto {
    Long id;
    String name;
    Role role;
    List<BookmarkReadDto> bookmarks;
}
