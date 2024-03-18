package id.co.app.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "STOCK", schema = "APP")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Stock {
    @Id
    @SequenceGenerator(name = "stock_sequence", sequenceName = "stock_sequence", schema = "APP", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_sequence")
    private Long idStock;

    @Column(length = 140)
    private String productName;

    private Integer quantity;

    private Date date;

    @Column(length = 100)
    private String courier ;

}
