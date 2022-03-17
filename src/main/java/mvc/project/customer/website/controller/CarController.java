package mvc.project.customer.website.controller;

import mvc.project.customer.website.models.Car;
import mvc.project.customer.website.models.User;
import mvc.project.customer.website.repositories.UserRepository;
import mvc.project.customer.website.services.CarService;
import mvc.project.customer.website.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class CarController {

    @Autowired
    CarService carService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;


    @GetMapping("/car")
    public String viewHomePage(Model model, Principal principal){

        final List<Car> carList = carService.getAllCars();
        model.addAttribute("carList", carList);
        User user = userService.loadUserByUsername(principal.getName());
        model.addAttribute(user);
        return "car-index";
    }

    @GetMapping("/car2")
    public String viewHomePage2(Model model, Principal principal){

        final List<Car> carList = carService.getAllCars();
        model.addAttribute("carList", carList);
        User user = userService.loadUserByUsername(principal.getName());
        model.addAttribute(user);
        return "car-info2";
    }


    @GetMapping("/car/add")
    public String showNewCarPage(Model model) {

        Car car = new Car();
        model.addAttribute("car", car);
        return "new-car";

    }

    @GetMapping("/car/{id}")
    public String getCar(@PathVariable(name = "id") Long id, Model model){

        Car car = carService.getCar(id);

        model.addAttribute("car", car);

        return "car-info";
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
                    "Cannot find car with id = " + id);
            return "error";
        }else if(car.getUser()!= null){

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

        if (!id.equals(car.getCarId())) {
            model.addAttribute("message",
                    "Cannot update, car id " + car.getCarId()
                            + " doesn't match id to be updated: " + id + ".");
            return "error";
        }

        carService.updateCar(car,id);


        return "redirect:/car";
    }

    @GetMapping("/car/view")
    public String carView(Model model, Principal principal){

        final List<Car> cars = carService.getAllCars();
        model.addAttribute("cars", cars);

        Long userId = userRepository.findByUsername(principal.getName()).getUserId();
        User user = userService.loadUserByUsername(principal.getName());
        model.addAttribute("userId", userId);
        model.addAttribute("user", user);

        return "car-view";
    }


}

