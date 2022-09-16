package sokolov.libraryservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sokolov.libraryservice.models.*;
import sokolov.libraryservice.repositories.PeopleRepository;
import sokolov.libraryservice.repositories.BooksRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BooksRepository booksRepository, PasswordEncoder passwordEncoder) {

        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Person> findAll() {
        List<Person> people = new ArrayList<>();
        for (Person person: peopleRepository.findAll()) {
            if(person.isActivity()) {
                people.add(person);
            }
        }
        return people;
    }

    public Person findOne(int personId) {
        return peopleRepository.findById(personId).orElse(null);
    }

    @Transactional
    public void savePerson(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setActivity(true);
        person.setRole("ROLE_USER");
        peopleRepository.save(person);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updatePerson(int personId, Person updatePerson) {
        Person person = peopleRepository.findById(personId).orElse(null);
        if (!updatePerson.getFio().isEmpty()) {
            person.setFio(updatePerson.getFio());
        }
        if(updatePerson.getDateOfBirth() != null) {
            person.setDateOfBirth(updatePerson.getDateOfBirth());
        }
        person.setPhoneNumber(updatePerson.getPhoneNumber());
        if (!updatePerson.getLogin().isEmpty()) {
            person.setLogin(updatePerson.getLogin());
        }
        if (!updatePerson.getPassword().isEmpty()) {
            person.setPassword(passwordEncoder.encode(updatePerson.getPassword()));
        }
        peopleRepository.save(person);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deletePerson(int personId) {
        Person person = peopleRepository.findById(personId).orElse(null);
        person.setActivity(false);
    }

    public Person showPersonByBookId(int bookId) {
        Book book = booksRepository.findById(bookId).orElse(null);

        for(AccountingOfBooks accounting: book.getAccountingOfBooksList()) {
            if((accounting.getStatus() == StatusOfAccounting.на_руках) | (accounting.getStatus() == StatusOfAccounting.забронирована)) {
                return peopleRepository.findById(accounting.getPerson().getPersonId()).orElse(null);
            }
        }
        return null;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Person> findByFioStartingWith(String startingWith) {
        List<Person> people = new ArrayList<>();
        for(Person person: peopleRepository.findByFioContainingIgnoreCase(startingWith)) {
            if (person.isActivity()) {
                people.add(person);
            }
        }
        return people;
    }

    @Transactional
    public void resetDates() throws ParseException {
        List<Book> bookList = booksRepository.findAll();
        List<Book> bookResetList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendarOld = Calendar.getInstance();
        Calendar calendarNew = Calendar.getInstance();
        calendarNew.setTime(new Date());
        for (Book book: bookList) {
            if (book.getActivity() == StatusOfBook.забронирована) {
                try {
                    calendarOld.setTime(book.getAccountingOfBooksList().stream()
                            .filter(accountingOfBooks -> accountingOfBooks.getStatus() == StatusOfAccounting.забронирована)
                            .findAny().get().getDateWasTaken());
                } catch (Exception e) {
                    continue;
                }
                calendarOld.add(Calendar.DATE, 3);
                if (calendarNew.after(calendarOld)) {
                    book.setActivity(StatusOfBook.свободна);
                    bookResetList.add(book);
                    AccountingOfBooks accounting = book.getAccountingOfBooksList().stream()
                            .filter(accountingOfBooks -> accountingOfBooks.getStatus() == StatusOfAccounting.забронирована)
                            .findAny().orElse(null);
                    accounting.setDateReturnBook(new Date());
                    accounting.setStatus(StatusOfAccounting.возвращена);
                }
            }
        }
    }

    @Transactional
    public void setRoleAdmin(int personId) {
        peopleRepository.findById(personId).orElse(null).setRole("ROLE_ADMIN");
    }

    @Transactional
    public void setRoleUser(int personId) {
        peopleRepository.findById(personId).orElse(null).setRole("ROLE_USER");
    }
}
