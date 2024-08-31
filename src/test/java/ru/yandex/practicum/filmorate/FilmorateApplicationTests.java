package ru.yandex.practicum.filmorate;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.ecxeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;

import java.io.IOException;
import java.time.LocalDate;

@SpringBootTest
public class FilmorateApplicationTests {

	@Test(expected = ValidationException.class)
	public void testValidateEmailUser() throws IOException {

		User user = new User();
		user.setId(1L);
		user.setEmail("exampleexample.com");
		user.setLogin("exampleLogin");
		user.setName("Example");
		user.setBirthday(LocalDate.of(2000, 1, 1));
		UserController userController = new UserController();
		userController.createUser(user);
	}

	@Test(expected = ValidationException.class)
	public void testValidateLoginUser() throws IOException {

		User user = new User();
		user.setId(1L);
		user.setEmail("example@example.com");
		user.setLogin("");
		user.setName("Example");
		user.setBirthday(LocalDate.of(2000, 1, 1));
		UserController userController = new UserController();
		userController.createUser(user);
	}

	@Test(expected = ValidationException.class)
	public void testValidateDateUser() throws IOException {

		User user = new User();
		user.setId(1L);
		user.setEmail("example@example.com");
		user.setLogin("exampleLogin");
		user.setName("Example");
		user.setBirthday(LocalDate.of(2050, 1, 1));
		UserController userController = new UserController();
		userController.createUser(user);
	}

	@Test(expected = ValidationException.class)
	public void testValidateFilmName() throws IOException {
		Film film = new Film();
		film.setName("");
		film.setDescription("exampleFilm");
		film.setReleaseDate(LocalDate.of(2000, 1, 1));
		film.setDuration(100);
		FilmController filmController = new FilmController();
		filmController.createFilm(film);
	}

	@Test(expected = ValidationException.class)
	public void testValidateFilmDescription() throws IOException {
		Film film = new Film();
		film.setName("Example");
		film.setDescription("exampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmFilmexampleFilmFilmexampleFilmFilmexampleFilmFilmexampleFilmFilmexampleFilmFilmexampleFilm");
		film.setReleaseDate(LocalDate.of(2000, 1, 1));
		film.setDuration(100);
		FilmController filmController = new FilmController();
		filmController.createFilm(film);
	}

	@Test(expected = ValidationException.class)
	public void testValidateFilmRelease() throws IOException {
		Film film = new Film();
		film.setName("Example");
		film.setDescription("exampleFilm");
		film.setReleaseDate(LocalDate.of(1895, 12, 27));
		film.setDuration(100);
		FilmController filmController = new FilmController();
		filmController.createFilm(film);
	}

	@Test(expected = ValidationException.class)
	public void testValidateFilmDuration() throws IOException {
		Film film = new Film();
		film.setName("Example");
		film.setDescription("exampleFilm");
		film.setReleaseDate(LocalDate.of(2000, 1, 1));
		film.setDuration(0);
		FilmController filmController = new FilmController();
		filmController.createFilm(film);
	}

}
