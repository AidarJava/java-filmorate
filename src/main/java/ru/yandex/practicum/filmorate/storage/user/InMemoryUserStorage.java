package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.ecxeption.NotFoundException;
import ru.yandex.practicum.filmorate.ecxeption.OccurredException;
import ru.yandex.practicum.filmorate.ecxeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public User createUser(User user) {
        validateUser(user);
        user.setId(getNextId());
        log.debug("Валидация пройдена.");
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Id должен быть указан!");
        }
        validateUser(user);
        if (users.containsKey(user.getId())) {
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
        } else throw new NotFoundException("Такого пользователя нет в списке!");
    }

    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains(String.valueOf('@'))) {
            throw new OccurredException("Электронная почта не может быть пустой и должна содержать символ @!");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(String.valueOf(' '))) {
            throw new OccurredException("Логин не может быть пустым и содержать пробелы!");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new OccurredException("Дата рождения не может быть в будущем!");
        }
    }
}
