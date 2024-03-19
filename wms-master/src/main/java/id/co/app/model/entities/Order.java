package id.co.app.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "ORDER", schema = "APP")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {
    @Id
    @SequenceGenerator(name = "order_sequence", sequenceName = "order_sequence", schema = "APP", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequence")
    private int idOrder;

    @Column(length = 140)
    private String productName;

    private Integer soldUnits;

    private Date date;

    @Column(length = 100)
    private String customer;
}
