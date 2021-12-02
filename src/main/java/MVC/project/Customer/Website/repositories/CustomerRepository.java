package MVC.project.Customer.Website.repositories;

import MVC.project.Customer.Website.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {


}
