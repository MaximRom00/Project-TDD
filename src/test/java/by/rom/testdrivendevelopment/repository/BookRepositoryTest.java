package by.rom.testdrivendevelopment.repository;

import by.rom.testdrivendevelopment.model.Author;
import by.rom.testdrivendevelopment.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
@Sql(value = {"/book-init.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void shouldReturnBookSearchedByName(){
        Optional<Book> book = bookRepository.findByName("Idiot");
        assertTrue(book.isPresent());
    }

    @Test
    public void shouldReturnNotFoundBook(){
        assertFalse(bookRepository.findByName("The Brothers Karamazov").isPresent());
    }

    @Test
    public void shouldReturnBookList() {
        List<Book> bookList = bookRepository.findAll();

        assertEquals(5, bookList.size());
    }

    @Test
    public void shouldSaveBook(){
        Book book = bookRepository.save(new Book("Dead Soul"));
        Optional<Book> bookFromDB = bookRepository.findByName(book.getName());

        assertTrue(bookFromDB.isPresent());
        assertEquals(book, bookFromDB.get());

    }

    @Test
    public void shouldUpdateBookAuthor(){
        Optional<Book> bookFromDB = bookRepository.findByName("1984");

        assertTrue(bookFromDB.isPresent());

        Author author = new Author("G", "Orwell");
        bookFromDB.get().setAuthor(author);
        bookRepository.save(bookFromDB.get());

        assertEquals(bookFromDB.get().getAuthor(), author);
    }

    @Test
    public void shouldDeleteBookByName(){
        bookRepository.deleteByName("1984");

        Optional<Book> book = bookRepository.findByName("1984");

        assertFalse(book.isPresent());
    }

}
