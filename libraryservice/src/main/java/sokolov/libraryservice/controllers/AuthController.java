package sokolov.libraryservice.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sokolov.libraryservice.dto.PersonDTO;
import sokolov.libraryservice.models.Person;
import sokolov.libraryservice.services.PeopleService;
import sokolov.libraryservice.services.PersonDetailsService;
import sokolov.libraryservice.util.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final PersonValidator personValidator;
    private final PeopleService peopleService;
    private final PersonDetailsService personDetailsService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthController(PersonValidator personValidator, PeopleService peopleService, PersonDetailsService personDetailsService, ModelMapper modelMapper) {
        this.personValidator = personValidator;
        this.peopleService = peopleService;
        this.personDetailsService = personDetailsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") PersonDTO personDTO) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid PersonDTO personDTO,
                                      BindingResult bindingResult) {
        personValidator.validate(convertToPerson(personDTO), bindingResult);

        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }
        peopleService.savePerson(convertToPerson(personDTO));

        return "redirect:/auth/login";
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }
}
