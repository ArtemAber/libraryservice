package sokolov.libraryservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sokolov.libraryservice.models.AccountingOfBooks;


@Repository
public interface AccountingRepository extends JpaRepository<AccountingOfBooks, Integer> {
}
