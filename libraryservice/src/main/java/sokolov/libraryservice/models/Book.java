package sokolov.libraryservice.models;

import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;

    @Column(name = "title")
    @NotEmpty(message = "Название книги не должно быть пустым")
    @Size(min = 1, max = 100, message = "Название книги должно быть от 1 до 100 символов")
    private String title;

    @Column(name = "author")
    @NotEmpty(message = "ФИО автора не должно быть пустым")
    @Size(min = 1, max = 100, message = "ФИО автора должно быть от 1 до 100 символов")
    private String author;

    @Column(name = "year_of_publication")
    @NotNull(message = "Год публикации книги не должен быть пустым")
    @Max(value = 2022, message = "Год публикации книги должен быть меньше 2022")
    private int yearOfPublication;

    @Column(name = "serial_number")
    @NotNull(message = "Серийный номер не должен быть пустым")
    private int serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity")
    private StatusOfBook activity;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<AccountingOfBooks> accountingOfBooksList;

    public Book() {
    }

    public Book(String title, String author, int yearOfPublication,int serialNumber) {
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.serialNumber = serialNumber;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(int yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public StatusOfBook getActivity() {
        return activity;
    }

    public void setActivity(StatusOfBook activity) {
        this.activity = activity;
    }

    public List<AccountingOfBooks> getAccountingOfBooksList() {
        return accountingOfBooksList;
    }

    public void setAccountingOfBooksList(List<AccountingOfBooks> accountingOfBooksList) {
        this.accountingOfBooksList = accountingOfBooksList;
    }

    public Person getPerson() {
        Person person = new Person();
        for(AccountingOfBooks accounting: accountingOfBooksList) {
            if((accounting.getStatus() == StatusOfAccounting.на_руках) | (accounting.getStatus() == StatusOfAccounting.забронирована)) {
                Hibernate.initialize(accounting.getPerson());
                person = accounting.getPerson();
            }
        }
        return person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return yearOfPublication == book.yearOfPublication && Objects.equals(title, book.title) && Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, yearOfPublication);
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                '}';
    }
}
