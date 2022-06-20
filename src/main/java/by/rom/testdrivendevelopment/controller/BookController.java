package by.rom.testdrivendevelopment.controller;

import by.rom.testdrivendevelopment.model.Book;
import by.rom.testdrivendevelopment.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Book> saveBook(@RequestBody Book book) {
        return new ResponseEntity<>(bookService.saveBook(book),HttpStatus.CREATED);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Book> deleteBook(@PathVariable String name){
        Book book = bookService.getBookByName(name);

        bookService.deleteBook(book);

        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") long id, @RequestBody Book book){
        return bookService.findById(id)
                .map(savedBook -> {

                    savedBook.setName(book.getName());
                    savedBook.setAuthor(book.getAuthor());

                    Book updatedBook = bookService.updateBook(savedBook);
                    return new ResponseEntity<>(updatedBook, HttpStatus.OK);

                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
