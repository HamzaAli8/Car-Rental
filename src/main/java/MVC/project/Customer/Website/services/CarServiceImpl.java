package MVC.project.Customer.Website.services;

import MVC.project.Customer.Website.models.Car;
import MVC.project.Customer.Website.models.Customer;
import MVC.project.Customer.Website.repositories.CarRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@NoArgsConstructor
@Transactional(readOnly = true)
public class CarServiceImpl implements CarService{

    @Autowired
    CarRepository carRepository;


    @Override
    public List<Car> getAllCars() {

        return carRepository.findAll();
    }

    @Override
    @Transactional
    public Car saveCar(Car car) {


        return carRepository.save(car);
    }

    @Override
    public Car getCar(Long id){

        return carRepository.findById(id)
                .orElse(null);
    }

    @Override
    @Transactional
    public void deleteCar(Long id) {

        carRepository.delete(getCar(id));
    }

    @Override
    @Transactional
    public List<Car> saveAllCars(List<Car> carList) {
        return carRepository.saveAll(carList);
    }
}
