package com.reader.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@ToString(exclude = "chapters")
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"title", "author_id"})})
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
    @Column(columnDefinition = "TEXT")
    private String image;
    @Column(columnDefinition = "TEXT")
    private String annotation;
    private Genre genre;
    @Column(unique = true)
    private String pathToFile;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL/*, orphanRemoval = true*/)
    private List<Chapter> chapters;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Bookmark> bookmarks;
}
