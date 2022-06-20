package by.rom.testdrivendevelopment.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "author")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = "books")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private final List<Book> books = new ArrayList<>();

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
