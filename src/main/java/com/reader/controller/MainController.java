package com.reader.controller;


import com.reader.dto.BookReadDto;
import com.reader.dto.UserCreateEditDto;
import com.reader.dto.UserReadDto;
import com.reader.entity.Genre;
import com.reader.service.BookService;
import com.reader.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final BookService bookService;
    private final UserService userService;

    @GetMapping
    public String indexPage(Model model,
                            Principal principal,
                            @RequestParam("page") Optional<Integer> page,
                            @RequestParam(value = "keyword", required = false) String keyword) {
        if (principal != null) {
            UserReadDto user = userService.findByUsername(principal.getName());
            model.addAttribute("user", user);
        }

        int p = page.orElse(1);
        Pageable pageable = PageRequest.of(p - 1, 5, Sort.by(Sort.Direction.DESC, "id"));
        Page<BookReadDto> books = bookService.findAll(pageable);

        model.addAttribute("genres", Genre.values());
        model.addAttribute("books", books);
        return "index";
    }

    @GetMapping("/search")
    public String search(String keyword,
                         Principal principal,
                         Optional<Integer> page,
                         Model model) {
        int p = page.orElse(1);
        Pageable pageable = PageRequest.of(p - 1, 5);
        Page<BookReadDto> books = bookService.findByKeyword(keyword, pageable);

        if (principal != null) {
            UserReadDto user = userService.findByUsername(principal.getName());
            model.addAttribute("user", user);
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("books", books);
        model.addAttribute("genres", Genre.values());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        return "registration";
    }

    @PostMapping("/registration")
    public String userRegistration(@Validated @ModelAttribute UserCreateEditDto userDto,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", userDto);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/registration";
        }
        try {
            userService.save(userDto);
        } catch (DataAccessException e) {
            bindingResult.addError(new ObjectError("Duplicate problem", "Пользователь уже существует"));
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/registration";
        }
        return "redirect:/login";
    }

}
