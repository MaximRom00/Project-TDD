package by.rom.testdrivendevelopment.controller;

import by.rom.testdrivendevelopment.model.Book;
import by.rom.testdrivendevelopment.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<Book> getBook(@PathVariable String name) throws Exception {
        Book book = bookService.getBookByName(name);
        return new ResponseEntity<>(book ,HttpStatus.OK);
    }

    @GetMapping
    public List<Book> getAllBooks(){
        return bookService.getAllBooks();
    }

    @PostMapping
    public ResponseEntity<Book> saveCar(@RequestBody Book book) {
        return new ResponseEntity<>(bookService.saveBook(book),HttpStatus.CREATED);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Book> deleteBook(@PathVariable String name){
        Book book = bookService.getBookByName(name);

        bookService.deleteBook(book);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") long id, @RequestBody Book updatedBook) {
        Book book = bookService.findById(id);
        if (book != null) {
            return new ResponseEntity<>(bookService.saveBook(book), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}
