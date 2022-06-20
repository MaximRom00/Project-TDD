package by.rom.testdrivendevelopment.service;

import by.rom.testdrivendevelopment.exception.IsPresentException;
import by.rom.testdrivendevelopment.exception.NotFoundException;
import by.rom.testdrivendevelopment.model.Author;
import by.rom.testdrivendevelopment.model.Book;
import by.rom.testdrivendevelopment.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author getAuthorByName(String name) {
        return authorRepository
                .findByLastName(name)
                .orElseThrow(()-> new NotFoundException("Author didn't found"));
    }

    public void deleteAuthor(Author author) {
        authorRepository.delete(author);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author saveAuthor(Author author) {
        Optional<Author> authorByName = authorRepository.findByLastName(author.getLastName());

        if(authorByName.isPresent()){
            throw new IsPresentException("Author already exist with name:" + author.getLastName());
        }

        return authorRepository.save(author);
    }

    public Optional<Author> findById(long id) {
        return authorRepository.findById(id);
    }

    public Author updateAuthor(Author author, Long id) {
        authorRepository.findAll()
                .stream()
                .filter(aut -> aut.getLastName().equalsIgnoreCase(author.getLastName()))
                .findAny()
                .ifPresent(card -> {
                    throw new IsPresentException("Author with such name: " + author.getLastName() + " exists. Name should be unique.");
                });

        Author updateAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Author with such id: %d , didn't found", id)));

        updateAuthor.setFirstName(author.getFirstName());
        updateAuthor.setLastName(author.getLastName());

        authorRepository.save(updateAuthor);

        return updateAuthor;
    }
}
