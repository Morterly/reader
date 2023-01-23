package com.reader.entity;


public enum Genre {
    FANTASY("Фэнтези"),
    SCIENCE_FICTION("Научная фантастика"),
    ADVENTURE("Приключения"),
    MYSTERY("Мистика"),
    HORROR("Ужасы"),
    ROMANCE("Романтика");

    private String genre;

    Genre(String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

}
