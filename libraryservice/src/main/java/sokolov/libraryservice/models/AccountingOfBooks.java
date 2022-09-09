package sokolov.libraryservice.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "accounting_of_books")
public class AccountingOfBooks {

    @Id
    @Column(name = "record_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int recordId;

    @ManyToOne
    @JoinColumn(name = "personId", referencedColumnName = "person_id")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "bookId", referencedColumnName = "book_id")
    private Book book;

    @Column(name = "date_was_taken")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateWasTaken;

    @Column(name = "date_return_book")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateReturnBook;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusOfAccounting status;

    public AccountingOfBooks() {
    }

    public AccountingOfBooks(Person person, Book book, Date dateWasTaken, StatusOfAccounting status) {
        this.person = person;
        this.book = book;
        this.dateWasTaken = dateWasTaken;
        this.status = status;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getDateWasTaken() {
        return dateWasTaken;
    }

    public void setDateWasTaken(Date dateWasTaken) {
        this.dateWasTaken = dateWasTaken;
    }

    public Date getDateReturnBook() {
        return dateReturnBook;
    }

    public void setDateReturnBook(Date dateReturnBook) {
        this.dateReturnBook = dateReturnBook;
    }

    public StatusOfAccounting getStatus() {
        return status;
    }

    public void setStatus(StatusOfAccounting status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AccountingOfBooks{" +
                "person=" + person +
                ", book=" + book +
                ", dateWasTaken=" + dateWasTaken +
                ", dateReturnBook=" + dateReturnBook +
                ", status=" + status +
                '}';
    }
}
