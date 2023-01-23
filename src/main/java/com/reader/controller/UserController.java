package com.reader.controller;

import com.reader.dto.*;
import com.reader.entity.Genre;
import com.reader.service.BookService;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
public class UserController {
    private final UserService userService;
    private final BookService bookService;

    @GetMapping("/info/{id}")
    public String userInfo(@PathVariable("id") Long id,
                           Principal principal,
                           Model model) {
        UserReadDto currentUser = userService.findByUsername(principal.getName());
        if (currentUser.getId().equals(id)) {
            UserCreateEditDto user = userService.findById(id);
            model.addAttribute("user", user);
            return "userInfo";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/info/{id}")
    public String userEdit(@PathVariable("id") Long id,
                           @Validated @ModelAttribute UserCreateEditDto userDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", userDto);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/users/info/{id}";
        }

        try {
            userService.save(userDto);
        } catch (DataAccessException e) {
            bindingResult.addError(new ObjectError("Duplicate problem", "Пользователь уже существует"));
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/users/info/{id}";
        }
        redirectAttributes.addFlashAttribute("message", "Данные успешно изменены");
        return "redirect:/users/info/{id}";
    }

    @GetMapping("/bookmarks/{id}")
    public String bookmarks(@PathVariable("id") Long id,
                            Principal principal,
                            Optional<Integer> page,
                            Model model) {
        if (principal != null) {
            UserReadDto user = userService.findByUsername(principal.getName());
            List<BookReadDto> listBooks = user.getBookmarks().stream()
                    .map(BookmarkReadDto::getBookId)
                    .map(bookService::findById)
                    .collect(Collectors.toList());

            int p = page.orElse(1);
            Pageable pageable = PageRequest.of(p - 1, 5);
            Page<BookReadDto> books = Utility.toPage(listBooks, pageable);

            model.addAttribute("genres", Genre.values());
            model.addAttribute("user", user);
            model.addAttribute("books", books);
        }
        return "index";
    }
}
