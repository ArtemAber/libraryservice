package sokolov.libraryservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sokolov.libraryservice.models.*;
import sokolov.libraryservice.repositories.AccountingRepository;
import sokolov.libraryservice.repositories.BooksRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;
    private final AccountingRepository accountingRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, AccountingRepository accountingRepository) {
        this.booksRepository = booksRepository;
        this.accountingRepository = accountingRepository;
    }

    public List<Book> showAllBooks() {
        List<Book> list = new ArrayList<>();
        for(Book book: booksRepository.findAll()) {
            if (!(book.getActivity() == StatusOfBook.списана)) {
                list.add(book);
            }
        }
        return list;
    }

    public List<Book> showFreeBooks() {
        List<Book> list = new ArrayList<>();
        for (Book book: booksRepository.findAll()) {
            if (book.getActivity() == StatusOfBook.свободна) {
                list.add(book);
            }
        }
        return list;
    }

    public Book showBook(int bookId) {
        return booksRepository.findById(bookId).orElse(null);
    }

    @Transactional
    public void addPerson(Person person, int bookId) {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Book book = booksRepository.findById(bookId).orElse(null);
        book.setActivity(StatusOfBook.на_руках);

        AccountingOfBooks accounting = new AccountingOfBooks(person, book, date, StatusOfAccounting.на_руках);
        accountingRepository.save(accounting);
    }

    @Transactional
    public void freeBook(int bookId) {
        Book book = booksRepository.findById(bookId).orElse(null);
        book.setActivity(StatusOfBook.свободна);
        for (AccountingOfBooks accounting: book.getAccountingOfBooksList()) {
            if ((accounting.getStatus() == StatusOfAccounting.на_руках) | (accounting.getStatus() == StatusOfAccounting.забронирована)) {
                accounting.setStatus(StatusOfAccounting.возвращена);
                accounting.setDateReturnBook(new Date());
            }
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateBook(int bookId, Book updateBook) {
        updateBook.setBookId(bookId);
        updateBook.setActivity(booksRepository.findById(bookId).get().getActivity());
        booksRepository.save(updateBook);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void saveBook(Book book) {
        book.setActivity(StatusOfBook.свободна);
        booksRepository.save(book);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteBook(int bookId) {
        Book book = booksRepository.findById(bookId).orElse(null);
        book.setActivity(StatusOfBook.списана);
        for (AccountingOfBooks accountingOfBooks : book.getAccountingOfBooksList()) {
            if ((accountingOfBooks.getStatus() == StatusOfAccounting.на_руках) || (accountingOfBooks.getStatus() == StatusOfAccounting.забронирована)) {
                accountingOfBooks.setStatus(StatusOfAccounting.возвращена);
                accountingOfBooks.setDateReturnBook(new Date());
            }
        }
    }

    public List<Book> findByTitleStartingWith(String startingWith) {
        List<Book> books = new ArrayList<>();
        for(Book book: booksRepository.findByTitleContainingIgnoreCase(startingWith)) {
            if (book.getActivity() != StatusOfBook.списана) {
                books.add(book);
            }
        }
        return books;
    }

    @Transactional
    public void bookABook(int bookId, Person person) {
        Book book = booksRepository.findById(bookId).orElse(null);
        book.setActivity(StatusOfBook.забронирована);
        AccountingOfBooks accounting = new AccountingOfBooks(person, book, new Date(), StatusOfAccounting.забронирована);
        accountingRepository.save(accounting);
    }

    public String getBookingTime(int bookId) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(showBook(bookId).getAccountingOfBooksList()
                    .stream().filter(accountingOfBooks -> accountingOfBooks.getStatus() == StatusOfAccounting.забронирована)
                    .findAny().orElse(null).getDateWasTaken());
        } catch (Exception e) {
            return null;
        }
        calendar.add(Calendar.DATE, 3);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        System.out.println(dateFormat.format(calendar.getTime()));
        return dateFormat.format(calendar.getTime());
    }

    public String getDateOfCapture(int bookId) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(showBook(bookId).getAccountingOfBooksList()
                    .stream().filter(accountingOfBooks -> accountingOfBooks.getStatus() == StatusOfAccounting.на_руках)
                    .findAny().orElse(null).getDateWasTaken());
        } catch (Exception e) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(calendar.getTime());
    }

    @Transactional
    public void issueABookedBook(int bookId) {
        for (AccountingOfBooks accountingOfBooks : showBook(bookId).getAccountingOfBooksList()) {
            if (accountingOfBooks.getStatus() == StatusOfAccounting.забронирована) {
                accountingOfBooks.setStatus(StatusOfAccounting.на_руках);
                accountingOfBooks.setDateWasTaken(new Date());
                showBook(bookId).setActivity(StatusOfBook.на_руках);
            }
        }
    }

//    public String getReturnDate(int bookId) {
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        try {
//            calendar.setTime(dateFormat.parse(getDateOfCapture(bookId)));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    return null;
//    }
}
