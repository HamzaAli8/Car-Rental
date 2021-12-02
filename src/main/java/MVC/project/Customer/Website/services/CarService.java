package MVC.project.Customer.Website.services;

import MVC.project.Customer.Website.models.Car;
import MVC.project.Customer.Website.models.Customer;

import java.util.List;

public interface CarService {

    List<Car> getAllCars();

    Car saveCar(Car car);

    Car getCar(Long id);

    void deleteCar(Long id);

    List<Car> saveAllCars(List<Car> carList);

}
