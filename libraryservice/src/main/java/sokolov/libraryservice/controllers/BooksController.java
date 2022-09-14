package sokolov.libraryservice.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sokolov.libraryservice.dto.BookDTO;
import sokolov.libraryservice.dto.PersonDTO;
import sokolov.libraryservice.models.Book;
import sokolov.libraryservice.models.Person;
import sokolov.libraryservice.security.PersonDetails;
import sokolov.libraryservice.services.BooksService;
import sokolov.libraryservice.services.PeopleService;
import sokolov.libraryservice.util.BookValidator;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;
    private final BookValidator bookValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService, BookValidator bookValidator, ModelMapper modelMapper) {
        this.booksService = booksService;
        this.peopleService = peopleService;
        this.bookValidator = bookValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public String showAllBooks(Model model) {
        model.addAttribute("books", booksService.showAllBooks().stream()
                .map(this::convertToBookDTO).collect(Collectors.toList()));
        model.addAttribute("admin", adminBool());

        return "books/showAllBooks";
    }

    @GetMapping("/{bookId}")
    public String showBook(@PathVariable("bookId") int bookId, Model model, @ModelAttribute("person") PersonDTO personDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        model.addAttribute("whoLoggedIn", personDetails.getPerson());
        model.addAttribute("book", convertToBookDTO(booksService.showBook(bookId)));
        model.addAttribute("bookPersonId", booksService.showBook(bookId).getPerson().getPersonId());
        model.addAttribute("people", peopleService.findAll().stream()
                .map(this::convertToPersonDTO).collect(Collectors.toList()));
        model.addAttribute("admin", adminBool());
        model.addAttribute("bookingTime", booksService.getBookingTime(bookId));
        model.addAttribute("dateOfCapture", booksService.getDateOfCapture(bookId));

        if (peopleService.showPersonByBookId(bookId) != null) {
            model.addAttribute("reader", convertToPersonDTO(peopleService.showPersonByBookId(bookId)));
        } else {
            model.addAttribute("reader", null);
        }
        return "books/showBook";
    }

    @PatchMapping("/{bookId}/addperson")
    public String addPerson(@ModelAttribute("person") PersonDTO personDTO, @PathVariable("bookId") int bookId) {
        booksService.addPerson(convertToPerson(personDTO), bookId);

        return "redirect:/books/" + bookId;
    }

    @PatchMapping("/{bookId}/free")
    public String freeBook(@PathVariable("bookId") int bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person person = personDetails.getPerson();

        if(person.getRole().equals("ROLE_ADMIN")) {
            booksService.freeBook(bookId);
        } else if(booksService.showBook(bookId).getPerson().getPersonId() == person.getPersonId()) {
            booksService.freeBook(bookId);
        }

        return "redirect:/books/" + bookId;
    }

    @GetMapping("/{bookId}/edit")
    public String editBook(Model model, @PathVariable("bookId") int bookId) {
        model.addAttribute("book", convertToBookDTO(booksService.showBook(bookId)));

        return "books/edit";
    }

    @PatchMapping("/{bookId}")
    public String updateBook(@ModelAttribute("book") @Valid BookDTO bookDTO, BindingResult bindingResult,
                             @PathVariable("bookId") int bookId) {
        bookValidator.validate(convertToBook(bookDTO), bindingResult);
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        booksService.updateBook(bookId, convertToBook(bookDTO));
        return "redirect:/books";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") BookDTO bookDTO) {
        return "books/new";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid BookDTO bookDTO, BindingResult bindingResult) {
        bookValidator.validate(convertToBook(bookDTO), bindingResult);
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        booksService.saveBook(convertToBook(bookDTO));

        return "redirect:/books";
    }

    @DeleteMapping("/{bookId}")
    public String deleteBook(@PathVariable("bookId") int bookId) {
        booksService.deleteBook(bookId);

        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchBook(Model model) {
        model.addAttribute("books", booksService.showAllBooks().stream()
                .map(this::convertToBookDTO).collect(Collectors.toList()));
        model.addAttribute("admin", adminBool());
        return "books/search";
    }

    @PatchMapping("/search")
    public String findBookByTitleStartingWith(@RequestParam("startingWith") String startingWith, Model model) {
        model.addAttribute("books", booksService.findByTitleStartingWith(startingWith).stream()
                .map(this::convertToBookDTO).collect(Collectors.toList()));
        return "books/search";
    }

    @PostMapping("/bookAbook/{bookId}")
    public String bookAbook(@PathVariable("bookId") int bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person person = personDetails.getPerson();
        booksService.bookABook(bookId, person);
        return "redirect:/books/" + bookId;
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
}
