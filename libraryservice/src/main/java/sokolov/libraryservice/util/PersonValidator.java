package sokolov.libraryservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sokolov.libraryservice.models.Person;
import sokolov.libraryservice.repositories.PeopleRepository;
import sokolov.libraryservice.services.PeopleService;

@Component
public class PersonValidator implements Validator {

    private final PeopleRepository peopleRepository;
    private final PeopleService peopleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonValidator(PeopleRepository peopleRepository, PeopleService peopleService, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.peopleService = peopleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (!peopleRepository.findByLogin(person.getLogin()).isEmpty()) {
            if (peopleRepository.findByLogin(person.getLogin()).get().getPersonId() != person.getPersonId()) {
                errors.rejectValue("login", "", "Человек с таким логином уже существует");
            }
        }
        if (!peopleRepository.findByPhoneNumber(person.getPhoneNumber()).isEmpty()) {
            if (peopleRepository.findByPhoneNumber(person.getPhoneNumber()).get().getPersonId() != person.getPersonId()) {
                errors.rejectValue("phoneNumber", "", "Человек с таким телефоном уже существует");
            }
        }

        if(person.getDateOfBirth() == null) {
            errors.rejectValue("dateOfBirth", "", "Дата рождения не должна быть пустая");
        }
    }
}
