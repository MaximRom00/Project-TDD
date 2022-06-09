package by.rom.testdrivendevelopment.repository;

import by.rom.testdrivendevelopment.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>{

    public Optional<Book> findByName(String name);
}
