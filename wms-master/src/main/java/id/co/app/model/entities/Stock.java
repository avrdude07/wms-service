package id.co.app.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "STOCK")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Stock {
    @Id
    @SequenceGenerator(name = "stock_sequence", sequenceName = "stock_sequence", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_sequence")
    private Long idStock;

    @Column(length = 140)
    private String productName;

    private Double quantity;

    private Date createdDate;

    @Column(length = 100)
    private String courier;

    @Column(name = "satuan", length = 20)
    private String satuan;

}
