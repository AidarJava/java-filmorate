package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.ecxeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        if (validateFilm(film) == null) {
            throw new ValidationException("Валидация не пройдена.");
        }
        film.setId(getNextId());
        log.debug("Валидация пройдена.");
        films.put(film.getId(), film);
        log.debug("Фильм добавлен в список.");
        return film;
    }

    @PutMapping
    public Film ubdateFilm(@Valid @RequestBody Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Id должен быть указан!");
        }
        if (films.containsKey(film.getId())) {
            if (validateFilm(film) == null) {
                throw new ValidationException("Валидация не пройдена.");
            }
            film.setDescription(film.getDescription());
            film.setName(film.getName());
            film.setReleaseDate(film.getReleaseDate());
            film.setDuration(film.getDuration());
            return film;
        } else throw new ValidationException("Такого фильма нет в списке!");
    }

    private Film validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым!");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов!");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года!");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом!");
        }
        return film;
    }

    private Long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}