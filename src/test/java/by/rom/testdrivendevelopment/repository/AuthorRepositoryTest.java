package by.rom.testdrivendevelopment.repository;

import by.rom.testdrivendevelopment.model.Author;
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
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
@Sql(value = {"/book-init.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void shouldReturnAuthorSearchedByName(){
        Optional<Author> author = authorRepository.findByLastName("Orwell");
        assertTrue(author.isPresent());
    }

    @Test
    public void shouldReturnNotFoundAuthor(){
        assertFalse(authorRepository.findByLastName("New LastName").isPresent());
    }

    @Test
    public void shouldReturnAuthorList() {
        List<Author> authorList = authorRepository.findAll();

        assertEquals(5, authorList.size());
    }

    @Test
    public void shouldSaveAuthor(){
        Author author = authorRepository.save(new Author("Nikolai", "Gogol"));
        Optional<Author> authorFromDB = authorRepository.findByLastName(author.getLastName());

        assertTrue(authorFromDB.isPresent());
        assertEquals(author, authorFromDB.get());

    }

    @Test
    public void shouldUpdateAuthorFirstName(){
        Optional<Author> authorFromDB = authorRepository.findByLastName("Orwell");

        assertTrue(authorFromDB.isPresent());

        authorFromDB.get().setFirstName("G.");
        authorRepository.save(authorFromDB.get());

        assertEquals(authorFromDB.get().getFirstName(), "G.");
    }

    @Test
    public void shouldDeleteAuthorByName(){
        authorRepository.deleteByLastName("Orwell");

        Optional<Author> author = authorRepository.findByLastName("Orwell");

        assertFalse(author.isPresent());
    }
}
