//package sokolov.libraryservice.dao;
//
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.query.Query;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import sokolov.libraryservice.models.*;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//
//@Component
//public class BookDAO {
//    private final SessionFactory sessionFactory;
//
//    @Autowired
//    public BookDAO(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }
//
//    @Transactional(readOnly = true)
//    public List<Book> showAllBooks() {
//        Session session = sessionFactory.getCurrentSession();
//
//        return session.createQuery("select b from Book b", Book.class).getResultList();
//    }
//
//    @Transactional(readOnly = true)
//    public Book showBook(int bookId) {
//        Session session = sessionFactory.getCurrentSession();
//
//        return session.get(Book.class, bookId);
//    }
//
//    @Transactional
//    public void addPerson(Person person, int bookId) {
//        Session session = sessionFactory.getCurrentSession();
//        Date date = new Date();
//        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
//        Book book = session.get(Book.class, bookId);
//        book.setActivity(StatusOfBook.на_руках);
//
//        AccountingOfBooks accounting = new AccountingOfBooks(person, book, date, StatusOfAccounting.на_руках);
//        session.save(accounting);
//    }
//
//    @Transactional
//    public void freeBook(int bookId) {
//        Session session = sessionFactory.getCurrentSession();
//        Book book = session.get(Book.class, bookId);
//        book.setActivity(StatusOfBook.свободна);
//        for(AccountingOfBooks accounting: book.getAccountingOfBooksList()) {
//            if (accounting.getStatus() == StatusOfAccounting.на_руках) {
//                accounting.setStatus(StatusOfAccounting.возвращена);
//                accounting.setDateReturnBook(new Date());
//            }
//        }
//    }
//
//    @Transactional
//    public void updateBook(int bookId, Book updateBook) {
//        Session session = sessionFactory.getCurrentSession();
//
//        Book book = session.get(Book.class, bookId);
//
//        book.setAuthor(updateBook.getAuthor());
//        book.setTitle(updateBook.getTitle());
//        book.setYearOfPublication(updateBook.getYearOfPublication());
//    }
//
//    @Transactional
//    public void saveBook(Book book) {
//        Session session = sessionFactory.getCurrentSession();
//        book.setActivity(StatusOfBook.свободна);
//        session.save(book);
//    }
//
//    @Transactional
//    public void deleteBook(int bookId) {
//        Session session = sessionFactory.getCurrentSession();
//
//        Book book = session.get(Book.class, bookId);
//        book.setActivity(StatusOfBook.списана);
//    }
//
//    @Transactional
//    public List<Book> findByTitleStartingWith(String startingWith) {
//        Session session = sessionFactory.getCurrentSession();
//        String sql = String.format("select * from books where title ilike '%s' and activity!='%s'",
//                "%" + startingWith + "%", StatusOfBook.списана);
//        Query query = session.createSQLQuery(sql).addEntity(Book.class);
//        List<Book> bookList = new ArrayList<>();
//
//        for(Iterator<Book> it = query.list().iterator(); it.hasNext();) {
//            bookList.add((Book) it.next());
//        }
//
//        return bookList;
//    }
//}
