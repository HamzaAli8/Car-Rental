package MVC.project.Customer.Website.controller;


import MVC.project.Customer.Website.models.Car;
import MVC.project.Customer.Website.models.Customer;
import MVC.project.Customer.Website.services.CarService;
import MVC.project.Customer.Website.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    CarService carService;

    @GetMapping("/")
    public String viewHomePage(Model model){

        return "index";
    }

    @GetMapping("/customer-list")
    public String viewCustomerList ( Model model){

        final List<Customer> customerList = customerService.getAllCustomers();
        model.addAttribute("customerList", customerList);
        return "customer-list";
    }

    @GetMapping("/new")
    public String showNewCustomerPage(Model model){

        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "new-customer";
    }

    @PostMapping(value = "/save")
    public String saveCustomer(@ModelAttribute("customer") Customer customer){

        customerService.saveCustomer(customer);
        return "redirect:/";
    }

  @GetMapping("/edit/{id}")
  public String showEditCustomerPage(@PathVariable(name = "id")Long id, Model model){

        Customer customer = customerService.getCustomer(id);

        model.addAttribute("customer", customer);
        return "edit-customer";
  }
  @PostMapping("/update/{id}")
  public String updateCustomer(@PathVariable(name = "id") Long id,
                               @ModelAttribute("customer")Customer customer,Model model){

      if (!id.equals(customer.getCustomerId())) {
          model.addAttribute("message",
                  "Cannot update, customer id " + customer.getCustomerId()
                          + " doesn't match id to be updated: " + id + ".");
          return "error";
      }
      customerService.saveCustomer(customer);
      return "redirect:/";
    }

    @RequestMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable(name = "id")Long id, Model model)
    {
        Customer customer = customerService.getCustomer(id);
        if (customer == null) {
            model.addAttribute("msg",
                    "Cannot find customer with id = " + id);
            return "error";
        }else if(customer.getCar()!= null){

            model.addAttribute("msg",
                    "Cannot delete customer cause car is assigned");
            return "error";
        }
        customerService.deleteCustomer(id);
        return "redirect:/";
    }


    @GetMapping("/assign/{id}")
    public String showCarAssignPage(Model model,@PathVariable(name = "id")Long id){

        Customer customer = customerService.getCustomer(id);
        model.addAttribute("customerId", customer.getCustomerId());


        if(customer.getCar() == null){
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
    public String assignCarToCustomer(Model model,@ModelAttribute("car") Car car,
                                      @PathVariable(name = "id") Long customerId)
    {
         Customer customer = customerService.getCustomer(customerId);
         Car customerCar = carService.getCar(car.getCarId());

         if (customerCar.getCustomer() == null && customer.getCar() == null){

             customer.setCar(customerCar);
             customerCar.setCustomer(customer);

             customerService.saveCustomer(customer);
             carService.saveCar(customerCar);
         }else{

             model.addAttribute("msg",
                     "Car has already been assigned to another customer");
             return "error";
         }

        return "redirect:/";

    }




}