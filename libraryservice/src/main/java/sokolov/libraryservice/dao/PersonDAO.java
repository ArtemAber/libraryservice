//package sokolov.libraryservice.dao;
//
//import org.hibernate.SessionFactory;
//import org.hibernate.query.Query;
//import org.hibernate.Session;
//import org.hibernate.validator.constraints.pl.REGON;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//import sokolov.libraryservice.models.AccountingOfBooks;
//import sokolov.libraryservice.models.Book;
//import sokolov.libraryservice.models.Person;
//import sokolov.libraryservice.models.StatusOfAccounting;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.PersistenceContext;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
////@Repository
////public class PersonDAO {
////    @PersistenceContext
////    private final SessionFactory sessionFactory;
////
////    @Autowired
////    public PersonDAO(SessionFactory sessionFactory) {
////        this.sessionFactory = sessionFactory;
////    }
////
////    public List<Person> showAllPeople() {
////        Session session = sessionFactory.getCurrentSession();
////
////        return session.createQuery("select p from Person p", Person.class).getResultList();
////    }
////
////    @Transactional(readOnly = true)
////    public Person showPerson(int personId) {
////        Session session = sessionFactory.getCurrentSession();
////
////        Person person = session.get(Person.class, personId);
////        return person;
////    }
////
////    @Transactional
////    public void savePerson(Person person) {
////        Session session = sessionFactory.getCurrentSession();
////
////        session.save(person);
////    }
////
////    @Transactional
////    public void updatePerson(int personId, Person updatePerson) {
////        Session session = sessionFactory.getCurrentSession();
////
////        Person person = session.get(Person.class, personId);
////        person.setFio(updatePerson.getFio());
////        person.setDateOfBirth(updatePerson.getDateOfBirth());
////    }
////
////    @Transactional
////    public void deletePerson(int personId) {
////        Session session = sessionFactory.getCurrentSession();
////
////        Person person = session.get(Person.class, personId);
////        person.setActivity(false);
////    }
////
////    @Transactional
////    public Person showPersonByBookId(int bookId) {
////        Session session = sessionFactory.getCurrentSession();
////        Book book = session.get(Book.class, bookId);
////
////        for(AccountingOfBooks accounting: book.getAccountingOfBooksList()) {
////            if (accounting.getStatus() == StatusOfAccounting.на_руках) {
////                return session.get(Person.class, accounting.getPerson().getPersonId());
////            }
////        }
////        return null;
////    }
////
////    @Transactional
////    public List<Person> findByFioStartingWith(String startingWith) {
////        Session session = sessionFactory.getCurrentSession();
////        String sql = String.format("select * from person where fio ilike '%s' and activity='%s'",
////                "%" + startingWith + "%", "true");
////        Query query = session.createSQLQuery(sql).addEntity(Person.class);
////        List<Person> peopleList = new ArrayList<>();
////
////        for(Iterator<Person> it = query.list().iterator(); it.hasNext();) {
////            peopleList.add((Person) it.next());
////        }
////
////        return peopleList;
////    }
//}
