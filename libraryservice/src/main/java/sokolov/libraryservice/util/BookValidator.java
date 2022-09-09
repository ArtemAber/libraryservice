package sokolov.libraryservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sokolov.libraryservice.models.Book;
import sokolov.libraryservice.repositories.BooksRepository;

import java.util.List;

@Component
public class BookValidator implements Validator {

    private final BooksRepository booksRepository;

    @Autowired
    public BookValidator(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Book.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Book book = (Book) o;

        List<Book> list = booksRepository.findAll();
        for(Book book1: list) {
            if(book1.getSerialNumber() == book.getSerialNumber()) {
                if(book1.getBookId() == book.getBookId()) {
                    continue;
                }
                errors.rejectValue("serialNumber", "", "Серийный номер должен быть уникальный");
            }
        }
    }
}
