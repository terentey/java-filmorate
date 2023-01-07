package ru.yandex.practicum.filmorate.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class JdbcTest {
    private static final JdbcTemplate jdbc = new JdbcTemplate(myDataSource());

    public static DataSource myDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:file:./db/filmorate");
        dataSource.setUsername("sa");
        dataSource.setPassword("password");
        return dataSource;
    }

    public static void restartDb() {
        jdbc.update("DELETE FROM SCHEMA.GENRE_FILM; " +
                "DELETE FROM SCHEMA.LIKES; " +
                "DELETE FROM SCHEMA.USER_FRIEND; " +
                "DELETE FROM SCHEMA.FILM; " +
                "DELETE FROM SCHEMA.USERS; " +
                "ALTER TABLE SCHEMA.FILM ALTER COLUMN ID RESTART; " +
                "ALTER TABLE SCHEMA.USERS ALTER COLUMN ID RESTART;");
    }
}
