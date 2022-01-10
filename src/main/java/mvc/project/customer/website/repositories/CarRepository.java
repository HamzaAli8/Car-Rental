package mvc.project.customer.website.repositories;

import mvc.project.customer.website.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CarRepository extends JpaRepository<Car,Long> {

    Car getCarByUserUserId (Long id);

}
