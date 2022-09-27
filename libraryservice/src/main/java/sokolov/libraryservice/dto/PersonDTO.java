package sokolov.libraryservice.dto;

import org.springframework.format.annotation.DateTimeFormat;
import sokolov.libraryservice.models.AccountingOfBooks;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

public class PersonDTO {

    private int personId;

    @NotEmpty(message = "ФИО не должно быть пустым")
    @Size(min = 2, max = 100, message = "ФИО должно быть от 2 до 100 символов")
    @Pattern(regexp = "^[А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+$", message = "ФИО должно быть вида: Иванов Иван Иванович")
    private String fio;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    private long phoneNumber;

    private boolean activity;

//    private List<AccountingOfBooks> accountingOfBooksList;

    @NotEmpty(message = "Логин не должен быть пустым")
    private String login;

    @NotEmpty(message = "Пароль не должен быть пустым")
    private String password;

    private String role;

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

//    public List<AccountingOfBooks> getAccountingOfBooksList() {
//        return accountingOfBooksList;
//    }
//
//    public void setAccountingOfBooksList(List<AccountingOfBooks> accountingOfBooksList) {
//        this.accountingOfBooksList = accountingOfBooksList;
//    }

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
}
