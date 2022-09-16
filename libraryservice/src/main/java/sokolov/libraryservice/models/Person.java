package sokolov.libraryservice.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;

    @NotEmpty(message = "ФИО не должно быть пустым")
    @Size(min = 2, max = 100, message = "ФИО должно быть от 2 до 100 символов")
    @Column(name = "fio")
    @Pattern(regexp = "^[А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+$", message = "ФИО должно быть вида: Иванов Иван Иванович")
    private String fio;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @Column(name = "phone_number")
    private long phoneNumber;

    @Column(name = "activity")
    private boolean activity;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<AccountingOfBooks> accountingOfBooksList;

    @Column(name = "login")
    @NotEmpty(message = "Логин не должен быть пустым")
    private String login;

    @Column(name = "password")
    @NotEmpty(message = "Пароль не должен быть пустым")
    private String password;

    @Column(name = "role")
    private String role;

    public Person() {
    }

    public Person(String fio, Date dateOfBirth, long phoneNumber) {
        this.fio = fio;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActivity() {
        return activity;
    }

    public void setActivity(boolean activity) {
        this.activity = activity;
    }

    public List<AccountingOfBooks> getAccountingOfBooksList() {
        return accountingOfBooksList;
    }

    public void setAccountingOfBooksList(List<AccountingOfBooks> accountingOfBooksList) {
        this.accountingOfBooksList = accountingOfBooksList;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return phoneNumber == person.phoneNumber && Objects.equals(fio, person.fio) && Objects.equals(dateOfBirth, person.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fio, dateOfBirth, phoneNumber);
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId=" + personId +
                ", fio='" + fio + '\'' +
                '}';
    }
}
