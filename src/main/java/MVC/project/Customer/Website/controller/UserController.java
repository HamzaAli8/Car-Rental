package MVC.project.Customer.Website.controller;


import MVC.project.Customer.Website.models.Customer;
import MVC.project.Customer.Website.models.User;
import MVC.project.Customer.Website.repositories.UserRepository;
import MVC.project.Customer.Website.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @GetMapping(value = "/register")
    public String registerPage(Model model){
        User user = User.builder().customer(new Customer()).build();
        model.addAttribute("user",user);
        return "register";
    }

    @PostMapping(value = "/register")
    public String saveUser(@ModelAttribute("user") User user){

        userService.createNewUser(user);


        return "redirect:/";
    }
}
