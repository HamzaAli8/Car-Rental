package mvc.project.customer.website;


import mvc.project.customer.website.models.Customer;
import mvc.project.customer.website.models.Role;
import mvc.project.customer.website.models.User;
import mvc.project.customer.website.repositories.RoleRepository;
import mvc.project.customer.website.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;


@SpringBootApplication
public class CustomerWebsiteApplication {

	@Bean
	public PasswordEncoder passwordEncoder(){

		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(CustomerWebsiteApplication.class);
	}

	Role userRole = Role.builder().role(Role.Roles.ROLE_USER).build();
	Role adminRole = Role.builder().role(Role.Roles.ROLE_ADMIN).build();


	@Bean
	public CommandLineRunner loadInitialData(RoleRepository roleRepository, UserRepository userRepository) {
		return (args) -> {
			if(roleRepository.findAll().isEmpty()){

				roleRepository.save(userRole);
				roleRepository.save(adminRole);
			}

			Customer superCustomer = Customer.builder().firstName("Admin")
					.lastName("Admin").city("London").address("12 Oxford Street")
					.age(33).emailAddress("admin@gmail.com").postCode(122334L).build();

			if(userRepository.findAll().isEmpty()){

				User superUser = new User("Admin", passwordEncoder().encode("Admin")
						,passwordEncoder().encode("Admin"),Arrays.asList(adminRole,userRole) ,superCustomer);
				userRepository.save(superUser);
			}

		};
}

}
