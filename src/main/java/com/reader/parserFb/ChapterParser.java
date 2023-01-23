package com.reader.parserFb;


import com.reader.parserFb.parser.Element;
import com.reader.parserFb.parser.Section;
import com.reader.parserFb.parser.TextAuthor;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ChapterParser {
    private String title;
    private List<String> text;

    public ChapterParser(Section section) {
        this.title = parseTitle(section);
        this.text = parseText(section);
    }

    private List<String> parseText(Section section) {
        if (section.getEpigraphs().isEmpty()) {
            return section.getElements().stream()
                    .map(Element::getText)
                    .toList();
        } else {
            return section.getEpigraphs().stream()
                    .map(e -> {
                        List<String> strings = e.getElements().stream()
                                .map(Element::getText)
                                .collect(Collectors.toList());
                        String collect = e.getTextAuthor().stream()
                                .map(TextAuthor::getText)
                                .collect(Collectors.joining("\n"));
                        strings.add(collect);
                        return strings;
                    }).flatMap(Collection::stream)
                    .toList();
        }
    }

    private String parseTitle(Section section) {
        if (section.getTitle() == null) {
            return "";
        } else {
            return section.getTitle().getParagraphs().stream()
                    .map(Element::getText)
                    .collect(Collectors.joining("\n"));
        }
    }
}
