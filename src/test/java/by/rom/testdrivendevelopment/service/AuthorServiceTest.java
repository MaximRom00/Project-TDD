package by.rom.testdrivendevelopment.service;

import by.rom.testdrivendevelopment.exception.NotFoundException;
import by.rom.testdrivendevelopment.model.Author;
import by.rom.testdrivendevelopment.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthorServiceTest {

    @InjectMocks
    private AuthorService authorService;

    @Mock
    private AuthorRepository authorRepository;

    private Author author;

    @BeforeEach
    public void setup(){
        author = new Author("Nikolai", "Gogol");
    }

    @Test
    public void shouldReturnAuthor(){
        given(authorRepository.findByLastName("Gogol")).willReturn(Optional.of(author));

        Author author = authorService.getAuthorByName("Gogol");

        assertNotNull(author);
        assertEquals("Gogol", author.getLastName());
    }

    @Test
    public void shouldReturnAllAuthors(){
        List<Author> authors = List.of(
                new Author("Nikolai", "Gogol"),
                new Author("George", "Orwell"),
                new Author("John", "Steinbeck"));

        given(authorRepository.findAll()).willReturn(authors);

        List<Author> authorListFromDB = authorService.getAllAuthors();

        assertEquals(authors, authorListFromDB);
    }

    @Test
    void shouldSaveAndReturnTheSameAuthor() {
        given(authorRepository.save(author)).willReturn(author);

        Author savedAuthor = authorService.saveAuthor(author);

        assertEquals("Gogol", savedAuthor.getLastName());

        verify(authorRepository).save(author);
    }

    @Test
    public void shouldDeleteAuthor(){
        given(authorRepository.findByLastName(author.getLastName())).willReturn(Optional.of(author));

        authorService.deleteAuthor(author);

        verify(authorRepository).delete(author);
    }

    @Test
    public void shouldReturnUpdatedAuthor(){
        Author author = new Author(1L,"George", "Orwell");

        given(authorRepository.save(author)).willReturn(author);
        author.setFirstName("G.");

        Author updateAuthor = authorService.saveAuthor(author);

        assertEquals("G.", updateAuthor.getFirstName());

        verify(authorRepository).save(author);
    }

    @Test
    public void authorNotFound(){
        given(authorRepository.findByLastName("New Author")).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, ()-> authorService.getAuthorByName("Gogol"));
    }
}
