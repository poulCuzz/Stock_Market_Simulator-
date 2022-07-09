package pl.coderslab;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter

@Table(name = "sharesheld")
public class SharesHeld {

    @Override
    public String toString() {
        return "SharesHeld{" +
                "id=" + id +
                ", dateAndTime='" + dateAndTime + '\'' +
                ", volume=" + volume +
                ", purchasePrice=" + purchasePrice +
                ", valueAll=" + valueAll +
                ", purchasePriceAll=" + purchasePriceAll +
                ", profitOrLoss=" + profitOrLoss +
                ", company=" + company.getName() +
                ", user=" + user.getId() +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public void setValueAll(double valueAll) {
        this.valueAll = valueAll;
    }

    public void setCompany(Companies company) {
        this.company = company;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dateAndTime;
    private int volume;

    public void setPurchasePriceAll(double purchasePriceAll) {
        this.purchasePriceAll = volume * purchasePrice ;
    }

    public void setProfitOrLoss(double profitOrLoss) {
        this.profitOrLoss = valueAll - purchasePriceAll;
    }

    private double purchasePrice;
    private double valueAll;
    private double purchasePriceAll;
    private double profitOrLoss;
    @OneToOne
    private Companies company;
    @ManyToOne
    private User user;
}
