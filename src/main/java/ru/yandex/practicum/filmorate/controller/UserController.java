package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.ecxeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (validateUser(user) == null) {
            throw new ValidationException("Валидация не пройдена.");
        }
        user.setId(getNextId());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (user.getId() == null) {
            throw new ValidationException("Id должен быть указан!");
        }
        if (users.containsKey(user.getId())) {
            if (validateUser(user) == null) {
                throw new ValidationException("Валидация не пройдена.");
            }
            log.debug("Валидация пройдена.");
            user.setBirthday(user.getBirthday());
            user.setLogin(user.getLogin());
            user.setEmail(user.getEmail());
            if (user.getName() == null) {
                user.setName(user.getLogin());
                log.debug("Заменили имя на логин.");
            } else {
                user.setName(user.getName());
            }
            return user;
        } else throw new ValidationException("Такого пользователя нет в списке!");
    }

    private User validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains(String.valueOf('@'))) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @!");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(String.valueOf(' '))) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
        return user;
    }

    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
