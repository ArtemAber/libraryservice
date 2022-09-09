package sokolov.libraryservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sokolov.libraryservice.models.Person;
import sokolov.libraryservice.repositories.PeopleRepository;

@Component
public class PersonValidator implements Validator {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonValidator(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
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
    }
}
