package pl.coderslab;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@Table(name = "buyorders")
public class BuyOrders {
    @Override
    public String toString() {
        return "BuyOrders{" +
                "id=" + id +
                ", volumen=" + volumen +
                ", priceLimit=" + priceLimit +
                ", user=" + user.getId() +
                ", company=" + company.getId() +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(1)
    private int volumen;
    @Min(1)
    private double priceLimit;
    @ManyToOne
    private User user;
    @ManyToOne
    private Companies company;
}
