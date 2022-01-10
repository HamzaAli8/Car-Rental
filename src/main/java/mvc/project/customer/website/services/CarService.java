package mvc.project.customer.website.services;

import mvc.project.customer.website.models.Car;

import java.util.List;

public interface CarService {

    List<Car> getAllCars();

    Car saveCar(Car car);

    Car getCar(Long id);

    void deleteCar(Long id);

    Car updateCar(Car car, Long id);


    List<Car> saveAllCars(List<Car> carList);

}
