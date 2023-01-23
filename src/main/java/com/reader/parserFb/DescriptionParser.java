package com.reader.parserFb;


import com.reader.parserFb.parser.Element;
import com.reader.parserFb.parser.FictionBook;
import lombok.Getter;

import java.util.stream.Collectors;

@Getter
public class DescriptionParser {
    private String fullName;
    private String bookTitle;
    private String annotation;
    private String image;
    private final FictionBook fb;

    public DescriptionParser(FictionBook fb) {
        this.fb = fb;
        this.fullName = parseFullName();
        this.bookTitle = parseTitle();
        this.annotation = parseAnnotation();
        this.image = parseImage();

    }

    private String parseFullName() {
        return fb.getDescription().getTitleInfo().getAuthors().get(0).getFullName();
    }

    private String parseTitle() {
        return fb.getDescription().getTitleInfo().getBookTitle();
    }

    private String parseAnnotation() {
        return fb.getDescription().getTitleInfo().getAnnotation().getElements()
                .stream()
                .map(Element::getText)
                .collect(Collectors.joining());
    }

    private String parseImage() {
        return fb.getBinaries().values().stream()
                .map(b -> {
                    StringBuilder sb = new StringBuilder(b.getBinary());
                    return sb.insert(0, "data:image/jpg;base64,").toString();
                })
                .findFirst()
                .orElseThrow();
    }
}
