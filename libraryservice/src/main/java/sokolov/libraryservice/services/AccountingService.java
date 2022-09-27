package sokolov.libraryservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sokolov.libraryservice.models.AccountingOfBooks;
import sokolov.libraryservice.models.Book;
import sokolov.libraryservice.models.StatusOfAccounting;
import sokolov.libraryservice.repositories.AccountingRepository;
import sokolov.libraryservice.repositories.PeopleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AccountingService {

    private final AccountingRepository accountingRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public AccountingService(AccountingRepository accountingRepository, PeopleRepository peopleRepository) {
        this.accountingRepository = accountingRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> showBooksByPerson(int personId) {
        List<AccountingOfBooks> accountingOfBooksList = new ArrayList<>();
        for(AccountingOfBooks accounting: accountingRepository.findAll()) {
            if(accounting.getPerson().getPersonId() == personId) {
                accountingOfBooksList.add(accounting);
            }
        }
        List<Book> bookList = new ArrayList<>();
        for(AccountingOfBooks accounting: accountingOfBooksList) {
            if(accounting.getStatus() == StatusOfAccounting.на_руках) {
                bookList.add(accounting.getBook());
            }
        }
        return bookList;
    }

    public List<Book> showBookedBooksByPerson(int personId) {
        List<AccountingOfBooks> accountingOfBooksList1 = accountingRepository.findAll();
        List<AccountingOfBooks> accountingOfBooksList = new ArrayList<>();
        for(AccountingOfBooks accounting: accountingOfBooksList1) {
            if(accounting.getPerson().getPersonId() == personId) {
                accountingOfBooksList.add(accounting);
            }
        }
        List<Book> bookList = new ArrayList<>();
        for(AccountingOfBooks accounting: accountingOfBooksList) {
            if(accounting.getStatus() == StatusOfAccounting.забронирована) {
                bookList.add(accounting.getBook());
            }
        }
        return bookList;
    }

    @Transactional
    public void addDelay(AccountingOfBooks accountingOfBooks, String[] info) {
        accountingOfBooks.setCountDayDelay(accountingOfBooks.getCountDayDelay() + Integer.parseInt(info[0]));
        accountingOfBooks.setCountFine(accountingOfBooks.getCountFine() + Integer.parseInt(info[1]));
    }
}
