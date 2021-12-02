package MVC.project.Customer.Website.repositories;

import MVC.project.Customer.Website.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car,Long> {


}
