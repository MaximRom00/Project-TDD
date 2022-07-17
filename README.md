# Project-TDD
TDD in Java

## Technologies used
- Java 11;
- Spring Boot;
- Spring Security;
- Spring Test;
- Spring Data;
- Spring Web;
- MySQL;
- H2;
- JUnit 5;
- Mockito;
- Hamcrest;
- Lombok.

## What is TDD
Test Driven Development is a process that consists of turning the requirements of the software application into specific test cases (acceptance criteria) and then implement the source code.

<p align="center">
<img  src="https://user-images.githubusercontent.com/95149324/179388894-aeeeb64e-1c31-41d7-ae5f-882402df0980.png" width="450" height="275"> 
                                                                                                                   </p>

This process uses the **red/green/refactor** pattern and consists of the following steps:

1. Create Test
2. Run Tests (should fail - Red)
3. Write Code
4. Run Tests (should pass - Green)  
5. Refactor

## Development

Let's start from Controller. First we create BookControllerTest and we are going to create an end point for: /api/books/{name}.

```java
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class BookControllerTest {
    @Autowired  
    private MockMvc mockMvc;
     @Test
    public void shouldReturnBookWithAuthor() throws Exception {
      
        mockMvc.perform(get("/api/books/Idiot"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
```
Code will give us an error saying that BookController class is not available. Here we'll go to main folder and will create just BookController. 

```java
public class BookController {
}
```
Now the compilation error is resolved. When we run BookControllerTest class now, we end-up with the failed message as below:

![image](https://user-images.githubusercontent.com/95149324/179402691-45f1b20e-e905-4e71-b540-584bb1beb12e.png)

The reason for below error is that, because there is no rest endpoint with url /api/books/Idiot in BookController class. Let's create the endpoint in BookController class.

```java
@RestController
@RequestMapping("/api/books")
public class BookController {
   @GetMapping("/{name}")
    public ResponseEntity<Book> getBook(@PathVariable String name) throws Exception {
        return new ResponseEntity<>( HttpStatus.OK);
    }
}
```

Yes it is passed now.
![image](https://user-images.githubusercontent.com/95149324/179404114-a00030e3-4fe3-42ef-a58f-4610523883fb.png)


And now we create Book model.
```java
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private Author author;

    public Book(String name) {
        this.name = name;
    }
}
```
Now in BookController class lets add below codes:
```java
@RestController
@RequestMapping("/books")
public class BookController {
   @GetMapping("/{name}")
    public ResponseEntity<Book> getBook(@PathVariable String name) throws Exception {
        Book book = new Book();
        return new ResponseEntity<>(book,HttpStatus.OK);
    }
}
```
Now lets navigate to BookControllerTest class and add few more point to existing table as below:
```java
@Test
    public void shouldReturnBookWithAuthor() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/books/Idiot"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("name").value("Idiot"))
                .andDo(print());
    }
```
From above code, what are we expecting from test is that, then the response should contain Book object with 'Idiot" name. 
![image](https://user-images.githubusercontent.com/95149324/179404638-b308c897-e038-4749-83e6-5fd54d06a7c3.png)
The reason for this that we are passing book object with null values in controller. Create Book service class and inject in controller.
```java
@Service
public class BookService {

    public Book getBookByName(String name) {
       return null;
    }
}
```
Now in BookControllerTest, we are going to mock BookService class. And we are mocking BookService object with @MockBean annotation.

```java
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class BookControllerTest {
    @Autowired  
    private MockMvc mockMvc;
    
    @MockBean
    private BookService bookService;
    
    @Test
    public void shouldReturnBookWithAuthor() throws Exception {
    
    given(bookService.getBookByName("Idiot")).willReturn(
                         Book.builder().name("Idiot")
                        .author(Author.builder().firstName("Fyodor").lastName("Dostoyevsky").build())
                        .build());
      
        mockMvc.perform(get("/api/books/Idiot"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("name").value("Idiot"))
                .andExpect(jsonPath("author.firstName").value("Fyodor"))
                .andExpect(jsonPath("author.lastName").value("Dostoyevsky"))
                .andDo(print());
    }
}
```
With above code we are defining the behaviour in such a way that, if we pass any string as name it should return new Book. 

Lets assume if there isn't book with such name, what would happen. For this we need to create NotFoundException class which will be throwed when there isn't book with such name.

```java
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{

    public NotFoundException(String message) {
        super(message);
    }
}
```
Again we are going to have another test to validate this scenario. If you run this test method it will work like work. 

```java
   @Test
    public void shouldReturn404WhenBookNotFound() throws Exception {
        given(bookService.getBookByName(Mockito.anyString())).willThrow(new NotFoundException("Book didn't found"));

        mockMvc.perform(get("/api/books/Idiot"))
                .andExpect(status().isNotFound());
    }
```
Now create BookServiceTest class,
```java
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Test
    public void shouldReturnBook(){
        given(bookRepository.findByName("Idiot")).willReturn(Optional.of(new Book("Idiot")));

        Book book = bookService.getBookByName("Idiot");

        assertNotNull(book);
        assertEquals("Idiot", book.getName());
    }
    }
```
We need to ensure that the BookRepository's method findByName should give us proper data fetched from database and use H2Database. We create init.sql file:
<p align="center">
<img  src="https://user-images.githubusercontent.com/95149324/179408259-827e3865-c992-4eca-8ace-219e8b8e6fa5.png" width="650" height="300"> 
                                                                                                                   </p> 

With above line we are defining the behaviour. Before every test will call select method in database for out test.

```java
@Sql(value = {"/book-init.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
```

```java
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
@Sql(value = {"/book-init.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void shouldReturnBookSearchedByName(){
        Optional<Book> book = bookRepository.findByName("Idiot");
        assertTrue(book.isPresent());
    }
    }
```
And we see select from db and test is passed.
<p align="center">
<img  src="https://user-images.githubusercontent.com/95149324/179410789-c3b69c7e-01b1-4413-8037-8ec340ccf427.png" width="800" height="300"> 
                                                                                                                   </p> 

