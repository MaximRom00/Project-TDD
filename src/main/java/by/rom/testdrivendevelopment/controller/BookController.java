package by.rom.testdrivendevelopment.controller;

import by.rom.testdrivendevelopment.model.Book;
import by.rom.testdrivendevelopment.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<Book> getCarDetails(@PathVariable String name) throws Exception {
        Book book = bookService.getBookByName(name);
        return new ResponseEntity<>(book ,HttpStatus.OK);
    }
}
