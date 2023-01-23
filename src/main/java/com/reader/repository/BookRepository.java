package com.reader.repository;


import com.reader.entity.Book;
import com.reader.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAllByAuthor_Id(Long id, Pageable pageable);

    Page<Book> findAllByGenre(Genre genre, Pageable pageable);

    @Query(value = "select book from Book book "
            + "left join book.author author "
            + "where lower(book.title) like lower(concat('%', ?1, '%')) "
            + "or "
            + "lower(author.fullName) like lower(concat('%', ?1, '%'))")
    Page<Book> findAllByKeyword(String keyword, Pageable pageable);
}
