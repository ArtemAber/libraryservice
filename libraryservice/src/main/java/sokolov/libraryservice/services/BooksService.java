package sokolov.libraryservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sokolov.libraryservice.models.*;
import sokolov.libraryservice.repositories.AccountingRepository;
import sokolov.libraryservice.repositories.BooksRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void freeBook(int bookId) {
        Book book = booksRepository.findById(bookId).orElse(null);
        book.setActivity(StatusOfBook.свободна);
        for (AccountingOfBooks accounting: book.getAccountingOfBooksList()) {
            if (accounting.getStatus() == StatusOfAccounting.на_руках) {
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
}
