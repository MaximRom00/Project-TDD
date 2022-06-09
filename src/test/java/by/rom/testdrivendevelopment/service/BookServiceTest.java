package by.rom.testdrivendevelopment.service;

import by.rom.testdrivendevelopment.exception.NotFoundException;
import by.rom.testdrivendevelopment.model.Book;
import by.rom.testdrivendevelopment.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)

public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Test
    public void getBookByNameFromService(){
        given(bookRepository.findByName("Idiot")).willReturn(Optional.of(new Book("Idiot")));

        Book book = bookService.getBookByName("Idiot");

        assertNotNull(book);
        assertEquals("Idiot", book.getName());
    }

    @Test
    public void bookNotFound(){
        given(bookRepository.findByName("pulse")).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, ()-> bookService.getBookByName("Idiot"));
    }
}
