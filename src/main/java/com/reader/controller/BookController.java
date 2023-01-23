package com.reader.controller;

import com.reader.dto.*;
import com.reader.entity.Genre;
import com.reader.service.AuthorService;
import com.reader.service.BookService;
import com.reader.service.BookmarkService;
import com.reader.service.UserService;
import com.reader.utility.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final UserService userService;
    private final BookmarkService bookmarkService;

    @GetMapping("/info/{bookId}")
    public String bookInfo(@PathVariable("bookId") Long bookId,
                           Principal principal,
                           Model model) {
        BookReadDto book = bookService.findById(bookId);
        model.addAttribute("book", book);

        if (principal != null) {
            UserReadDto user = userService.findByUsername(principal.getName());
            model.addAttribute("user", user);

            book.getBookmarks().stream()
                    .filter(b -> b.getUserId().equals(user.getId()))
                    .findFirst()
                    .ifPresent(c -> model.addAttribute("bookmark", c));
        }

        return "bookInfo";
    }

    @GetMapping("/author/{id}")
    public String booksAuthor(@PathVariable Long id,
                              Optional<Integer> page,
                              Principal principal,
                              Model model) {
        if (principal != null) {
            UserReadDto user = userService.findByUsername(principal.getName());
            model.addAttribute("user", user);
        }

        int p = page.orElse(1);
        Pageable pageable = PageRequest.of(p - 1, 5);
        Page<BookReadDto> books = bookService.findAllByAuthor_id(id, pageable);
        AuthorDto author = authorService.findById(id).orElseThrow();

        model.addAttribute("genres", Genre.values());
        model.addAttribute("books", books);
        model.addAttribute("message", "Книги автора " + author.getFullName());
        return "index";
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/read/{bookId}/{page}")
    public String readBookPageable(@PathVariable(name = "bookId") Long bookId,
                                   @PathVariable("page") Integer page,
                                   Principal principal,
                                   Model model) {
        UserReadDto user = userService.findByUsername(principal.getName());
        BookReadDto book = bookService.findById(bookId);

        BookmarkReadDto bookmark;
        if (bookmarkService.findByBookAndUser(book, user).isPresent()) {
            bookmark = bookmarkService.findByBookAndUser(book, user).orElseThrow();
            bookmark.setPage(page);
        } else {
            bookmark = BookmarkReadDto.builder()
                    .bookId(book.getId())
                    .userId(user.getId())
                    .page(page)
                    .build();
        }
        bookmarkService.save(bookmark);

        Pageable pageable = PageRequest.of(page - 1, 20);
        Page<String> pages = Utility.toPage(book.getText(), pageable);

        model.addAttribute("pages", pages);
        model.addAttribute("book", book);
        model.addAttribute("user", user);
        return "book";
    }

    @GetMapping("/genres/{genre}")
    public String genres(@PathVariable("genre") String genre,
                         Principal principal,
                         @RequestParam("page") Optional<Integer> page,
                         Model model) {
        if (principal != null) {
            UserReadDto user = userService.findByUsername(principal.getName());
            model.addAttribute("user", user);
        }

        int p = page.orElse(1);
        Pageable pageable = PageRequest.of(p - 1, 5);
        Genre g = Genre.valueOf(genre.toUpperCase());
        Page<BookReadDto> books = bookService.findAllByGenre(g, pageable);

        model.addAttribute("genres", Genre.values());
        model.addAttribute("books", books);
        model.addAttribute("message", "Книги жанра " + g.getGenre());
        return "index";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/remove/{id}")
    public String removeBook(@PathVariable Long id) {
        bookService.delete(id);
        return "redirect:/";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable Long id,
                           Principal principal,
                           Model model) {
        if (principal != null) {
            UserReadDto user = userService.findByUsername(principal.getName());
            model.addAttribute("user", user);
        }

        BookReadDto book = bookService.findById(id);
        List<AuthorDto> authors = authorService.findAll();

        model.addAttribute("authors", authors);
        model.addAttribute("genres", Genre.values());
        model.addAttribute("book", book);
        return "bookEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/edit/{id}")
    public String editBookPost(@Validated @ModelAttribute BookCreateEditDto bookDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("book", bookDto);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/books/edit/{id}";
        }
        try {
            bookService.save(bookDto);
        } catch (DataAccessException e) {
            AuthorDto author = authorService.findById(bookDto.getAuthorId()).orElseThrow();
            bindingResult.addError(new ObjectError("Duplicate problem",
                    String.format("Книга %s автора %s уже существует", bookDto.getTitle(), author.getFullName())));
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/books/edit/{id}";
        }
        return "redirect:/";
    }

}
