package MVC.project.Customer.Website.models;


import lombok.*;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter

@Setter
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long carId;
    private String name;
    private String model;
    private String regNumber;
    private Integer year;

    @OneToOne(
            cascade = CascadeType.MERGE,
            fetch = FetchType.LAZY
    )
    @JoinColumn(

            name = "customer_id",
            referencedColumnName = "customerId"

    )
    private Customer customer;
}
