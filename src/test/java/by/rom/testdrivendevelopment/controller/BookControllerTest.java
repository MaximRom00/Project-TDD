package by.rom.testdrivendevelopment.controller;

import by.rom.testdrivendevelopment.exception.NotFoundException;
import by.rom.testdrivendevelopment.model.Author;
import by.rom.testdrivendevelopment.model.Book;
import by.rom.testdrivendevelopment.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    public void shouldReturnBookWithAuthor() throws Exception {
        given(bookService.getBookByName("Idiot")).willReturn(
                Book.builder()
                        .name("Idiot")
                        .author(Author.builder().firstName("Fyodor").lastName("Dostoyevsky").build())
                        .build());

        mockMvc.perform(get("/api/books/Idiot"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("name").value("Idiot"))
                .andExpect(jsonPath("author.firstName").value("Fyodor"))
                .andExpect(jsonPath("author.lastName").value("Dostoyevsky"))
                .andDo(print());
    }

    @Test
    public void shouldDeleteBook() throws Exception {
        given(bookService.getBookByName("Idiot")).willReturn(new Book("Idiot"));

        mockMvc.perform(delete("/api/books/Idiot")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void shouldReturnAllBooks() throws Exception {
        List<Book> books = List.of(
                new Book("1984"),
                new Book("Idiot"),
                new Book("Dead Souls"));

        given(bookService.getAllBooks()).willReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andDo(print());
    }

    @Test
    public void shouldSaveBook() throws Exception{
        Book book = Book.builder()
                .name("1984")
                .author(Author.builder().firstName("George").lastName("Orwell").build())
                .build();

        given(bookService.saveBook(Mockito.any())).willReturn(book);

        mockMvc.perform(
                post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(book))
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("name").value("1984"))
                .andExpect(jsonPath("author.firstName").value("George"))
                .andExpect(jsonPath("author.lastName").value("Orwell"))
                .andDo(print());
    }

    @Test
    public void shouldReturnUpdatedBook() throws Exception{
        long id = 2L;

        Book book = Book.builder().id(id).name("Idiot").author(new Author("Fyodor", "Dostoevsky")).build();
        Book updatedBook = Book.builder().id(id).name("The Brothers Karamazov").author(new Author("Fyodor", "Dostoevsky")).build();

        given(bookService.findById(id)).willReturn(Optional.of(book));
        given(bookService.updateBook(Mockito.any())).willReturn(updatedBook);

         mockMvc.perform(put("/api/books/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedBook.getName())))
                .andExpect(jsonPath("$.author.firstName", is(updatedBook.getAuthor().getFirstName())))
                .andExpect(jsonPath("$.author.lastName", is(updatedBook.getAuthor().getLastName())))
                .andDo(print());
    }

    @Test
    void shouldReturnNotFoundUpdateBook() throws Exception {
        long id = 1L;

        Book updatedBook = Book.builder().id(id).name("Dead Soul").build();

        given(bookService.findById(id)).willReturn(Optional.empty());
        given(bookService.saveBook(Mockito.any())).willReturn(updatedBook);

        mockMvc.perform(put("/api/books/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedBook)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void shouldReturn404WhenBookNotFound() throws Exception {
        given(bookService.getBookByName(Mockito.anyString())).willThrow(new NotFoundException("Book didn't found"));

        mockMvc.perform(get("/api/books/Idiot"))
                .andExpect(status().isNotFound());
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
