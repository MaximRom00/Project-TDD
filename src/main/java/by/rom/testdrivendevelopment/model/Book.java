package by.rom.testdrivendevelopment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "book")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    public Book(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    public Book(String name
            , Author author
    ) {
        this.name = name;
        this.author = author;
    }
}
