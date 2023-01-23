package com.reader.repository;


import com.reader.entity.Book;
import com.reader.entity.Bookmark;
import com.reader.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByBookAndUser(Book book, User user);

    Optional<List<Bookmark>> findAllByBook_Id(Long id);

    Optional<List<Bookmark>> findAllByUser_Id(Long id);
}
