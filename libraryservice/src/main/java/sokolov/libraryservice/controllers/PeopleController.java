package sokolov.libraryservice.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sokolov.libraryservice.dto.BookDTO;
import sokolov.libraryservice.dto.PersonDTO;
import sokolov.libraryservice.models.Book;
import sokolov.libraryservice.models.Person;
import sokolov.libraryservice.security.PersonDetails;
import sokolov.libraryservice.services.AccountingService;
import sokolov.libraryservice.services.PeopleService;
import sokolov.libraryservice.util.PersonValidator;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private final AccountingService accountingService;
    private final PersonValidator personValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public PeopleController(PeopleService peopleService, AccountingService accountingService,
                            PersonValidator personValidator, ModelMapper modelMapper) {

        this.peopleService = peopleService;
        this.accountingService = accountingService;
        this.personValidator = personValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public String showAllPeople(Model model) {

        if(adminBool()) {
            model.addAttribute("people", peopleService.findAll().stream()
                    .map(this::convertToPersonDTO).collect(Collectors.toList()));
            model.addAttribute("admin", adminBool());
        } else {
            return "redirect:/people/" + getUserId();
        }

        return "people/showAllPeople";
    }

    @GetMapping("/{personId}")
    public String showPerson(@PathVariable("personId") int personId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        model.addAttribute("whoLoggedIn", personDetails.getPerson());
        model.addAttribute("person", convertToPersonDTO(peopleService.findOne(personId)));
        model.addAttribute("books", accountingService.showBooksByPerson(personId).stream()
                .map(this::convertToBookDTO).collect(Collectors.toList()));
        model.addAttribute("bookedBooks", accountingService.showBookedBooksByPerson(personId)
                .stream().map(this::convertToBookDTO).collect(Collectors.toList()));
        model.addAttribute("admin", adminBool());

        try {
            peopleService.resetDates();
            System.out.println("успешно");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("провал");
        }

        return "people/showPerson";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") PersonDTO personDTO) {

        return "people/new";
    }

    @PostMapping()
    public String createPerson(@ModelAttribute("person") @Valid PersonDTO personDTO,
                               BindingResult bindingResult) {
        personValidator.validate(convertToPerson(personDTO), bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/new";
        }
        peopleService.savePerson(convertToPerson(personDTO));

        return "redirect:/people";
    }

    @GetMapping("/{personId}/edit")
    public String editPerson(Model model, @PathVariable("personId") int personId) {
        model.addAttribute("person", convertToPersonDTO(peopleService.findOne(personId)));

        return "people/edit";
    }

    @PatchMapping("/{personId}")
    public String updatePerson(@ModelAttribute("person") @Valid PersonDTO personDTO,
                               BindingResult bindingResult, @PathVariable("personId") int personId) {

        personValidator.validate(convertToPerson(personDTO), bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/edit";
        }
        peopleService.updatePerson(personId, convertToPerson(personDTO));

        return "redirect:/people";
    }

    @DeleteMapping("/{personId}")
    public String deletePerson(@PathVariable("personId") int personId) {

        peopleService.deletePerson(personId);

        return "redirect:/people";
    }

    @GetMapping("/search")
    public String searchPeople(Model model) {

        model.addAttribute("people", peopleService.findAll().stream()
                .map(this::convertToPersonDTO).collect(Collectors.toList()));

        return "people/search";
    }

    @PatchMapping("/search")
    public String findPeopleByTitleStartingWith(@RequestParam("startingWith") String startingWith,
                                                Model model) {

        model.addAttribute("people", peopleService.findByFioStartingWith(startingWith).stream()
                .map(this::convertToPersonDTO).collect(Collectors.toList()));

        return "people/search";
    }

    @PatchMapping("/{personId}/setRoleAdmin")
    public String setRoleAdmin(@PathVariable("personId") int personId) {
        peopleService.setRoleAdmin(personId);
        return "redirect:/people/" + personId;
    }

    @PatchMapping("/{personId}/setRoleUser")
    public String setRoleUser(@PathVariable("personId") int personId) {
        peopleService.setRoleUser(personId);
        return "redirect:/people/" + personId;
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    private Book convertToBook(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }

    private BookDTO convertToBookDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    private Boolean adminBool() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return personDetails.getAuthorities().stream().collect(Collectors.toList()).get(0)
                .toString().equals("ROLE_ADMIN");
    }

    private int getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return (int)personDetails.getPerson().getPersonId();
    }
}
