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

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Test
    public void shouldReturnBook(){
        given(bookRepository.findByName("Idiot")).willReturn(Optional.of(new Book("Idiot")));

        Book book = bookService.getBookByName("Idiot");

        assertNotNull(book);
        assertEquals("Idiot", book.getName());
    }

    @Test
    public void shouldReturnAllBooks(){
        List<Book> books = List.of(
                new Book("1984"),
                new Book("Idiot"),
                new Book("Dead Souls"));

        given(bookRepository.findAll()).willReturn(books);

        List<Book> bookListFromDB = bookService.getAllBooks();

        assertEquals(books, bookListFromDB);
    }

    @Test
    void shouldSaveAndReturnTheSameBook() {
        Book book = new Book("1984");

        given(bookRepository.save(book)).willReturn(book);

        Book savedBook = bookService.saveBook(book);

        assertEquals("1984", savedBook.getName());

        verify(bookRepository).save(book);
    }

    @Test
    public void shouldDeleteBook(){
        Book book = new Book("1984");

        given(bookRepository.findByName(book.getName())).willReturn(Optional.of(book));

        bookService.deleteBook(book);

        verify(bookRepository).delete(book);
    }

    @Test
    public void shouldReturnUpdatedBook(){
        Book book = Book.builder().id(1L).name("Idiot").build();

        given(bookRepository.save(book)).willReturn(book);
        book.setName("The Brothers Karamazov");

        Book updateBook = bookService.updateBook(book);

        assertEquals("The Brothers Karamazov", updateBook.getName());

        verify(bookRepository).save(book);
    }

    @Test
    public void bookNotFound(){
        given(bookRepository.findByName("pulse")).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, ()-> bookService.getBookByName("Idiot"));
    }
}
