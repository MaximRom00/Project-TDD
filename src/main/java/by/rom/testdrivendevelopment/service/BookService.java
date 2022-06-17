package by.rom.testdrivendevelopment.service;

import by.rom.testdrivendevelopment.exception.IsPresentException;
import by.rom.testdrivendevelopment.exception.NotFoundException;
import by.rom.testdrivendevelopment.model.Book;
import by.rom.testdrivendevelopment.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public void deleteBook(Book book) {
        bookRepository.delete(book);
    }

    public List<Book> getAllBooks() {
       return bookRepository.findAll();
    }

    public Book saveBook(Book book) {
        Optional<Book> savedBook = bookRepository.findByName(book.getName());

        if(savedBook.isPresent()){
            throw new IsPresentException("Book already exist with name:" + book.getName());
        }

        return bookRepository.save(book);
    }

    public Book updateBook( Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }
}
