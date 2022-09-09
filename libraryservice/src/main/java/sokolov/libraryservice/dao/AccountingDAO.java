//package sokolov.libraryservice.dao;
//
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.query.Query;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import sokolov.libraryservice.models.AccountingOfBooks;
//import sokolov.libraryservice.models.Person;
//
//import java.util.List;
//
//@Component
//public class AccountingDAO {
//
//    private final SessionFactory sessionFactory;
//
//    @Autowired
//    public AccountingDAO(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }
//
//    @Transactional
//    public List<AccountingOfBooks> showBooksByPerson(int personId) {
//        Session session = sessionFactory.getCurrentSession();
//        Person person = session.get(Person.class, personId);
//        Query query = session.createQuery("from AccountingOfBooks where person = :person");
//        query.setParameter("person", person);
//        return query.list();
//    }
//} проверка гита
