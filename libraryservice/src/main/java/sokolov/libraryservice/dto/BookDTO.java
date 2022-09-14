package sokolov.libraryservice.dto;

import org.hibernate.Hibernate;
import sokolov.libraryservice.models.AccountingOfBooks;
import sokolov.libraryservice.models.Person;
import sokolov.libraryservice.models.StatusOfAccounting;
import sokolov.libraryservice.models.StatusOfBook;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class BookDTO {

    private int bookId;

    @NotEmpty(message = "Название книги не должно быть пустым")
    @Size(min = 1, max = 100, message = "Название книги должно быть от 1 до 100 символов")
    private String title;

    @NotEmpty(message = "ФИО автора не должно быть пустым")
    @Size(min = 1, max = 100, message = "ФИО автора должно быть от 1 до 100 символов")
    private String author;

    @NotNull(message = "Год публикации книги не должен быть пустым")
    @Max(value = 2022, message = "Год публикации книги должен быть меньше 2022")
    private int yearOfPublication;

    @NotNull(message = "Серийный номер не должен быть пустым")
    private int serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity")
    private StatusOfBook activity;

    private List<AccountingOfBooks> accountingOfBooksList;

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
}
