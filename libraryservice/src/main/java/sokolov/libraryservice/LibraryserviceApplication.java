package sokolov.libraryservice;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sokolov.libraryservice.models.AccountingOfBooks;
import sokolov.libraryservice.models.Book;
import sokolov.libraryservice.models.StatusOfAccounting;
import sokolov.libraryservice.models.StatusOfBook;
import sokolov.libraryservice.services.BooksService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class LibraryserviceApplication {

	public static void main(String[] args) throws ParseException {
		SpringApplication.run(LibraryserviceApplication.class, args);

//		ResetBookingDates resetBookingDates = null;
//		resetBookingDates.resetDates();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}

//class ResetBookingDates {
//	@Autowired
//	private static BooksService booksService;
//
//	public static void resetDates() throws ParseException {
//		List<Book> bookList = booksService.showAllBooks();
//		List<Book> bookResetList = new ArrayList<>();
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		Calendar calendarOld = Calendar.getInstance();
//		Calendar calendarNew = Calendar.getInstance();
//		calendarNew.setTime(new Date());
//		for (Book book: bookList) {
//			if (book.getActivity() == StatusOfBook.забронирована) {
//				try {
//					calendarOld.setTime(book.getAccountingOfBooksList().stream()
//							.filter(accountingOfBooks -> accountingOfBooks.getStatus() == StatusOfAccounting.забронирована)
//							.findAny().get().getDateWasTaken());
//				} catch (Exception e) {
//					continue;
//				}
//				if (calendarNew.after(calendarOld)) {
//					book.setActivity(StatusOfBook.свободна);
//					bookResetList.add(book);
//					AccountingOfBooks accounting = book.getAccountingOfBooksList().stream()
//							.filter(accountingOfBooks -> accountingOfBooks.getStatus() == StatusOfAccounting.забронирована)
//							.findAny().orElse(null);
//					accounting.setDateReturnBook(new Date());
//				}
//			}
//		}
//	}
//}
