package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.ecxeption.NotFoundException;
import ru.yandex.practicum.filmorate.ecxeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film getFilmById(Long id) {
        return films.get(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года!");
        }
        film.setId(getNextId());
        log.debug("Валидация пройдена.");
        films.put(film.getId(), film);
        log.debug("Фильм добавлен в список.");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
       // validateFilm(film);
        if (film.getId() == null) {
            throw new ValidationException("Id должен быть указан!");
        }
        if (films.containsKey(film.getId())) {
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года!");
            }
            films.put(film.getId(), film);
            return film;
        } else throw new NotFoundException("Такого фильма нет в списке!");
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
