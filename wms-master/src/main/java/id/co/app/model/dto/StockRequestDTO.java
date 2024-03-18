package id.co.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockRequestDTO {
    private Long idStock;
    private String productName;
    private Integer quantity;
    private String courier;
}
