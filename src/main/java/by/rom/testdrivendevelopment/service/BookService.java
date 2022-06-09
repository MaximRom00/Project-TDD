package by.rom.testdrivendevelopment.service;

import by.rom.testdrivendevelopment.exception.NotFoundException;
import by.rom.testdrivendevelopment.model.Book;
import by.rom.testdrivendevelopment.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book getBookByName(String name){
        return bookRepository
                .findByName(name)
                .orElseThrow(()-> new NotFoundException("Book didn't found"));
    }
}
