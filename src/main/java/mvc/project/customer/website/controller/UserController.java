package mvc.project.customer.website.controller;


import mvc.project.customer.website.models.Car;
import mvc.project.customer.website.models.Customer;
import mvc.project.customer.website.models.User;
import mvc.project.customer.website.services.CarService;
import mvc.project.customer.website.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {


    @Autowired
    UserService userService;

    @Autowired
    CarService carService;



    @GetMapping("/")
    public String viewHomePage(Model model){

        return "index";
    }

    @GetMapping(value = "/register")
    public String registerPage(Model model){
        User user = User.builder().customer(new Customer()).build();
        model.addAttribute("user",user);
        return "register";
    }

    @PostMapping(value = "/register")
    public String saveUser(@ModelAttribute("user") User user){

        userService.createNewUser(user);


        return "redirect: welcome";
    }




    @GetMapping(value ="/welcome")
    public String successfulLoginPage(Model model, Principal principal){

        User user = userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal",principal);
        model.addAttribute(user);

        return "welcome";
    }

    @GetMapping("/contact")
    public String contactPage(Model model, Principal principal){

        User user = userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal",principal);
        model.addAttribute(user);

        return "contact";
    }

    @GetMapping("/user-list")
    public String viewCustomerList ( Model model, Principal principal){

        final List<User> userList = userService.getAllUsers();
        User user = userService.loadUserByUsername(principal.getName());
        model.addAttribute(user);
        model.addAttribute("userList", userList);
        return "user-list";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable(name = "id") Long id,
                                 @ModelAttribute("user")User user,Model model){

        if (!id.equals(user.getUserId())) {
            model.addAttribute("message",
                    "Cannot update, customer id " + user.getUserId()
                            + " doesn't match id to be updated: " + id + ".");
            return "error";
        }
        userService.updateUser(user,id);
        return "redirect:/welcome";
    }

    @RequestMapping("/delete/{id}")
    public String deleteUser(@PathVariable(name = "id")Long id, Model model)
    {
        User user  = userService.getUserById(id);
        if (user == null) {
            model.addAttribute("msg",
                    "Cannot find customer with id = " + id);
            return "error";
        }else if(user.getCar()!= null){

            model.addAttribute("msg",
                    "Cannot delete customer cause car is assigned");
            return "error";
        }
        userService.deleteUser(id);
        return "redirect:/welcome";
    }



    @GetMapping("/assign/{id}")
    public String showCarAssignPage(Model model,@PathVariable(name = "id")Long id){

        User user = userService.getCustomer(id);
        model.addAttribute("userId", user.getUserId());


        if(user.getCar() == null){
            List<Car> car = new ArrayList<>();
            model.addAttribute("car", car);
            List<Car> cars = carService.getAllCars();
            model.addAttribute("cars", cars);
        } else
        {
            model.addAttribute("msg",
                    "Cannot assign another car to customer");
            return "error";
        }

        return "assign-car";
    }

    @PostMapping("/assign/save/{id}")
    public String assignCarToUser(Model model,@ModelAttribute("car") Car car,
                                      @PathVariable(name = "id") Long userId) {
        User user = userService.getCustomer(userId);
        Car userCar = carService.getCar(car.getCarId());

        if (userCar.getUser() == null && user.getCar() == null) {

            user.setCar(userCar);
            userCar.setUser(user);

            userService.updateUser(user,userId);
            carService.saveCar(userCar);
        } else {

            model.addAttribute("msg",
                    "Car has already been assigned to another customer");
            return "error";
        }

        return "success";
    }


    @GetMapping("/edit/{id}")
    public String showEditCustomerPage(@PathVariable(name = "id")Long id, Model model){


        User user = userService.getCustomer(id);

        Long userId = user.getUserId();

        model.addAttribute("user", user);
        model.addAttribute("userId", userId);
        return "edit-customer";}




    @GetMapping
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @GetMapping("/success")
    public String successPage(){


        return "success";
    }
}
