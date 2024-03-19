package id.co.app.services;

import id.co.app.exception.GeneralException;
import id.co.app.model.dto.StockRequestDTO;
import id.co.app.model.entities.Stock;
import id.co.app.repositories.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {

    private static final String RESPONSE = "response";
    private static final String ERROR = "error";

    private final StockRepository stockRepository;

    public Page<Stock> getStocks(String productName, String fromDate, String toDate, String courier, int offset, int limit, String sortBy, String orderBy) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Pageable pageable = null;
        if (orderBy.equalsIgnoreCase("DESC")){
            pageable = PageRequest.of(offset - 1, limit).withSort(Sort.by(sortBy).descending());
        } else {
            pageable = PageRequest.of(offset - 1, limit).withSort(Sort.by(sortBy).ascending());
        }

        if (StringUtils.hasText(fromDate) && StringUtils.hasText(toDate)){
            Date startDate = StringUtils.hasText(fromDate) ? formatter.parse(fromDate + " 00:00:00") : null;
            Date endDate = StringUtils.hasText(toDate) ? formatter.parse(toDate + " 23:59:59") : null;
            log.info("Masuk Filter Stock Dengan Date");
            return stockRepository.getStockPageFilterWithDate(productName, courier, startDate, endDate, pageable);
        } else {
            log.info("Masuk Filter Stock Tanpa Date");
            return stockRepository.getStockPageFilter(productName, courier, pageable);
        }
    }

    public void addNewStock(StockRequestDTO stockRequestDTO, Map<String, Object> map) {
        Date newDate = new Date();
        Stock newStock = new Stock();
        newStock.setProductName(stockRequestDTO.getProductName());
        newStock.setQuantity(stockRequestDTO.getQuantity());
        newStock.setDate(newDate); // Set tanggal dan waktu saat ini
        newStock.setCourier(stockRequestDTO.getCourier());

        Stock checkStock= stockRepository.findByProductName(newStock.getProductName());
        if (checkStock != null){
            map.put(RESPONSE, "fail");
            map.put(ERROR, "Data Stock Sudah Ada");
            throw new GeneralException(map);
        }
        stockRepository.save(newStock);
        map.put(RESPONSE, "success");
    }

    public void deleteStock(Long studentId, Map<String, Object> map) {
        boolean exists = stockRepository.existsById(studentId);
        if(!exists){
            map.put(RESPONSE, "fail");
            map.put(ERROR, "student with id " + studentId + " does not exists");
            throw new GeneralException(map);
        }
        stockRepository.deleteById(studentId);
        map.put(RESPONSE, "success");
    }

    public  void updateStock(Long id, StockRequestDTO stockRequestDTO, Map<String, Object> map) {
        Optional<Stock> optionalStock = stockRepository.findById(id);

        if (optionalStock.isEmpty()) {
            map.put(RESPONSE, "fail");
            map.put(ERROR, "Stock not found with id: " + id);
            throw new GeneralException(map);
        }
        Stock existingStock = optionalStock.get();

        // Periksa apakah nama produk yang diberikan sudah digunakan oleh entitas lain
        if (!existingStock.getProductName().equals(stockRequestDTO.getProductName())) {
            if (stockRepository.existsByProductName(stockRequestDTO.getProductName())) {
                map.put(RESPONSE, "fail");
                map.put(ERROR, "Product name already exists: " + stockRequestDTO.getProductName());
                throw new GeneralException(map);
            }
        } else {
            map.put(RESPONSE, "fail");
            map.put(ERROR, "Product name already exists: " + stockRequestDTO.getProductName());
            throw new GeneralException(map);
        }

        // Update atribut lain jika diperlukan
        existingStock.setProductName(stockRequestDTO.getProductName());
        existingStock.setQuantity(stockRequestDTO.getQuantity());
        existingStock.setCourier(stockRequestDTO.getCourier());

        stockRepository.save(existingStock);
        map.put(RESPONSE, "success");
    }

}
