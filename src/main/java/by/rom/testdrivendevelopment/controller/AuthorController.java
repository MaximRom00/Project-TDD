package by.rom.testdrivendevelopment.controller;

import by.rom.testdrivendevelopment.model.Author;
import by.rom.testdrivendevelopment.model.Book;
import by.rom.testdrivendevelopment.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<Author> getAuthorByName(@PathVariable String name){
        Author author = authorService.getAuthorByName(name);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Author> deleteAuthor(@PathVariable String name){
        Author author = authorService.getAuthorByName(name);

        authorService.deleteAuthor(author);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<Author> getAllAuthors(){
        return authorService.getAllAuthors();
    }

    @PostMapping
    public ResponseEntity<Author> saveAuthor(@RequestBody Author author) {
        return new ResponseEntity<>(authorService.saveAuthor(author),HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Author> updateBook(@PathVariable("id") long id, @RequestBody Author author){
        return new ResponseEntity<>(authorService.updateAuthor(author, id), HttpStatus.OK);
    }
}
