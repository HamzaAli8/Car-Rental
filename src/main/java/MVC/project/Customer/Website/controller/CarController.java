package MVC.project.Customer.Website.controller;

import MVC.project.Customer.Website.models.Car;
import MVC.project.Customer.Website.services.CarService;
import MVC.project.Customer.Website.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CarController {

    @Autowired
    CarService carService;

    @Autowired
    CustomerService customerService;


    @GetMapping("/car")
    public String viewHomePage(Model model){

        final List<Car> carList = carService.getAllCars();
        model.addAttribute("carList", carList);
        return "car-index";
    }

    @GetMapping("/car/new")
    public String showNewCarPage(Model model) {

        Car car = new Car();
        model.addAttribute("car", car);
        return "new-car";

    }

    @PostMapping(value = "/car/save")
    public String saveCar(@ModelAttribute("car") Car car) {

        carService.saveCar(car);
        return "redirect:/car";
    }

    @RequestMapping("/car/delete/{id}")
    public String deleteCar(@PathVariable(name = "id")Long id, Model model)
    {
        Car car = carService.getCar(id);
        if (car == null) {
            model.addAttribute("msg",
                    "Cannot find customer with id = " + id);
            return "error";
        }else if(car.getCustomer()!= null){

            model.addAttribute("msg",
                    "Cannot delete car cause customer is assigned");
            return "error";
        }
        carService.deleteCar(id);
        return "redirect:/car";
    }

    @GetMapping("/car/edit/{id}")
    public String showEditCustomerPage(@PathVariable(name = "id")Long id, Model model){

        Car car = carService.getCar(id);

        model.addAttribute("car", car);
        return "edit-car";
    }

    @PostMapping("/car/update/{id}")
    public String updateCar(@PathVariable(name = "id") Long id,
                            @ModelAttribute("car")Car car,Model model){

        Car car2 = carService.getCar(id);

        if (!id.equals(car.getCarId())) {
            model.addAttribute("message",
                    "Cannot update, car id " + car.getCarId()
                            + " doesn't match id to be updated: " + id + ".");
            return "error";
        }
        car2.setModel(car.getModel());
        car2.setName(car.getName());
        car2.setRegNumber(car.getRegNumber());
        car2.setYear(car.getYear());

        carService.saveCar(car2);
        return "redirect:/car";
    }


}

