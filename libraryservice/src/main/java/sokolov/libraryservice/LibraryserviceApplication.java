package sokolov.libraryservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.ParseException;

@SpringBootApplication
public class LibraryserviceApplication {

	public static void main(String[] args) throws ParseException {
		SpringApplication.run(LibraryserviceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
