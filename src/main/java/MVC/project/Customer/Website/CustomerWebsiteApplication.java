package MVC.project.Customer.Website;

import MVC.project.Customer.Website.models.Car;
import MVC.project.Customer.Website.models.Customer;
import MVC.project.Customer.Website.services.CarService;
import MVC.project.Customer.Website.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.util.Arrays;

@SpringBootApplication
public class CustomerWebsiteApplication implements CommandLineRunner {

	@Autowired
	CustomerService customerService;

	@Autowired
	CarService carService;

	public static void main(String[] args) {
		SpringApplication.run(CustomerWebsiteApplication.class);

	}


	@Override
	public void run(String...args) throws Exception{
		if (customerService.getAllCustomers().isEmpty()) {
			customerService.saveAllCustomer(Arrays.asList(

					Customer.builder()
							.fullName("Hamza Ali")
							.age(34)
							.emailAddress("hamzaali@gmail.com")
							.address("123 Old Street London")
							.build(),
					Customer.builder().fullName("Saeed Ali")
							.age(33)
							.emailAddress("SaeedAli@gmail.com")
							.address("125 Old Street Manchester")
							.build()
			));
		}

		if(carService.getAllCars().isEmpty()) {
			carService.saveAllCars(Arrays.asList(
					Car.builder()
							.name("Mercedes")
							.model("Benz")
							.regNumber("ABC123")
							.year(2021)
							.build(),
					Car.builder()
							.name("BMW").model("3 Series").regNumber("XYZ789").year(2022).build()
					)
			);
		}
	}




}
