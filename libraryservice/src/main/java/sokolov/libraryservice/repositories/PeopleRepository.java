package sokolov.libraryservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sokolov.libraryservice.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {

    List<Person> findByFioContainingIgnoreCase(String fio);

    Optional<Person> findByLogin(String login);

    Optional<Person> findByPhoneNumber(long number);
}
