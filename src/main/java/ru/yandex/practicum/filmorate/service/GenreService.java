package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
public class GenreService {
    private final GenreStorage storage;

    @Autowired
    public GenreService(GenreStorage storage) {
        this.storage = storage;
    }

    public List<Genre> findAll() {
        return storage.findAll();
    }

    public Genre findById(int id) {
        return storage.findById(id);
    }
}
