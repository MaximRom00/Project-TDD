package by.rom.testdrivendevelopment.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
@Sql(value = {"/book-init.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void findByName(){
        assertTrue(bookRepository.findByName("Idiot").isPresent());
    }

    @Test
    public void bookNotFound(){
            assertFalse(bookRepository.findByName("The Brothers Karamazov").isPresent());
    }
}
