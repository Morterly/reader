package com.reader.controller;

import com.reader.dto.AuthorDto;
import com.reader.dto.BookCreateEditDto;
import com.reader.dto.BookReadDto;
import com.reader.dto.UserReadDto;
import com.reader.entity.Genre;
import com.reader.parserFb.BookParser;
import com.reader.service.AuthorService;
import com.reader.service.BookService;
import com.reader.service.ChapterService;
import com.reader.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class FileController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final ChapterService chapterService;
    private final UserService userService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String fileUpload(@RequestParam("file") MultipartFile file,
                             @RequestParam("genre") Genre genre,
                             @RequestParam("authorId") Long authorId)
            throws IOException {

        String path = "C://books//" + file.getOriginalFilename();
        File convertFile = new File(path);
        convertFile.createNewFile();
        FileOutputStream fout = new FileOutputStream(convertFile);
        fout.write(file.getBytes());
        fout.close();

        BookParser parser = new BookParser(path);

        AuthorDto author;
        if (authorId == -1) {
            author = parser.parseAuthor();
            author = authorService.save(author);
        } else {
            author = authorService.findById(authorId).orElseThrow();
        }

        BookCreateEditDto book = parser.parseBook(author, genre, path);
        BookReadDto bookReadDto = bookService.save(book);

        parser.parseChapter(bookReadDto)
                .forEach(chapterService::save);

        return "redirect:/";
    }

    @GetMapping("/upload")
    public String upload(Principal principal,
                         Model model) {
        UserReadDto user = userService.findByUsername(principal.getName());
        List<AuthorDto> authors = authorService.findAll();
        model.addAttribute("genres", Genre.values());
        model.addAttribute("authors", authors);
        model.addAttribute("user", user);
        return "upload";
    }
}
