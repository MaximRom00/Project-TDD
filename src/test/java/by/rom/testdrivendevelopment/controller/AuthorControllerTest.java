package by.rom.testdrivendevelopment.controller;

import by.rom.testdrivendevelopment.exception.NotFoundException;
import by.rom.testdrivendevelopment.model.Author;
import by.rom.testdrivendevelopment.service.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser(username = "user")
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Test
    public void shouldReturnAuthor() throws Exception {
        given(authorService.getAuthorByName("Gogol")).willReturn(
                Author.builder()
                        .firstName("Nikolai")
                        .lastName("Gogol")
                        .build());

        mockMvc.perform(get("/api/authors/Gogol"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("firstName").value("Nikolai"))
                .andExpect(jsonPath("lastName").value("Gogol"))
                .andDo(print());
    }

    @Test
    public void shouldDeleteAuthorById() throws Exception {
        given(authorService.getAuthorByName("Gogol")).willReturn(new Author("Nikolai", "Gogol"));

        mockMvc.perform(delete("/api/authors/Gogol")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void shouldReturnAllAuthors() throws Exception {
        List<Author> authors = List.of(
                new Author("Nikolai", "Gogol"),
                new Author("George", "Orwell"),
                new Author("John", "Steinbeck"));

        given(authorService.getAllAuthors()).willReturn(authors);

        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andDo(print());
    }

    @Test
    public void shouldReturnSavedAuthor() throws Exception{
        Author author = Author.builder().firstName("George").lastName("Orwell").build();

        given(authorService.saveAuthor(Mockito.any())).willReturn(author);

        mockMvc.perform(
                post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(author))
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("firstName").value("George"))
                .andExpect(jsonPath("lastName").value("Orwell"))
                .andDo(print());
    }

    @Test
    public void shouldReturnUpdatedAuthor() throws Exception{
        long id = 1L;

        Author author = new Author("Fyodor", "Dostoevsky");
        Author updatedAuthor = new Author("F.", "Dostoevsky");

        given(authorService.findById(id)).willReturn(Optional.of(author));
        given(authorService.updateAuthor(Mockito.any(), Mockito.any())).willReturn(updatedAuthor);

        mockMvc.perform(put("/api/authors/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedAuthor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedAuthor.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedAuthor.getLastName())))
                .andDo(print());
    }

    @Test
    public void shouldReturn404WheAuthorNotFound() throws Exception {
        given(authorService.getAuthorByName(Mockito.anyString())).willThrow(new NotFoundException("Author didn't found"));

        mockMvc.perform(get("/api/authors/Gogol"))
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
