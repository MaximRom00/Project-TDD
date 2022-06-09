package by.rom.testdrivendevelopment.controller;

import by.rom.testdrivendevelopment.exception.NotFoundException;
import by.rom.testdrivendevelopment.model.Book;
import by.rom.testdrivendevelopment.service.BookService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
//@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc
@SpringBootTest
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    public void getBookByNameFromController() throws Exception {
        given(bookService.getBookByName(Mockito.anyString())).willReturn(new Book("Idiot"));

        mockMvc.perform(MockMvcRequestBuilders.get("/books/Idiot"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("name").value("Idiot"))
                .andDo(print());
    }

    @Test
    public void bookNotFound() throws Exception {
        given(bookService.getBookByName(Mockito.anyString())).willThrow(new NotFoundException("Book didn't found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/books/Idiot"))
                .andExpect(status().isNotFound());
    }



}
