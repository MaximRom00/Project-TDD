package by.rom.testdrivendevelopment.repository;

import by.rom.testdrivendevelopment.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>{

    Optional<Book> findByName(String name);

    @Transactional
    void deleteByName(String name);
}
