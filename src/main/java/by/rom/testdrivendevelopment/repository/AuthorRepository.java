package by.rom.testdrivendevelopment.repository;

import by.rom.testdrivendevelopment.model.Author;
import by.rom.testdrivendevelopment.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByLastName(String name);

    @Transactional
    void deleteByLastName(String name);
}
