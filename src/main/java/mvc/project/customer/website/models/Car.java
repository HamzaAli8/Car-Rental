package mvc.project.customer.website.models;


import lombok.*;
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
    private String city;
    private Integer cost;
    private Transmission transmission;

    @OneToOne(
            cascade = CascadeType.MERGE,
            fetch = FetchType.LAZY
    )
    @JoinColumn(

            name = "user_id",
            referencedColumnName = "userId"

    )
    private User user;



    public enum Transmission{

        Manual,
        Automatic


    }


}
