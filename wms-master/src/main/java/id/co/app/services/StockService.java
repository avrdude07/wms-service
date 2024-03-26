package id.co.app.services;

import static id.co.app.constant.Constants.*;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {

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

    @Transactional
    public void addNewStock(StockRequestDTO stockRequestDTO) {
        Date newDate = new Date();
        Stock newStock = new Stock();
        newStock.setIdStock(stockRequestDTO.getIdStock());
        newStock.setProductName(stockRequestDTO.getProductName());
        newStock.setQuantity(stockRequestDTO.getQuantity());
        newStock.setDate(newDate); // Set tanggal dan waktu saat ini
        newStock.setCourier(stockRequestDTO.getCourier());

        Stock checkStock= stockRepository.findByProductName(newStock.getProductName());
        if (checkStock != null){
            log.error("An error occurred while updating stock");
            throw new GeneralException("Data Stock Sudah Ada");
        }
        stockRepository.save(newStock);
    }

    @Transactional
    public void deleteStock(Long studentId) {
        boolean exists = stockRepository.existsById(studentId);
        if(!exists){
            log.error("An error occurred while deleting  stock");
            throw new GeneralException("student with id " + studentId + " does not exists");
        }
        stockRepository.deleteById(studentId);
    }

    @Transactional
    public  void updateStock(Long id, StockRequestDTO stockRequestDTO) {
        Optional<Stock> optionalStock = stockRepository.findById(id);

        if (optionalStock.isEmpty()) {
            log.error("An error occurred while updating stock");
            throw new GeneralException("Stock not found with id: " + id);
        }
        Stock existingStock = optionalStock.get();

        // Periksa apakah nama produk yang diberikan sudah digunakan oleh entitas lain
        if (!existingStock.getProductName().equals(stockRequestDTO.getProductName())) {
            if (stockRepository.existsByProductName(stockRequestDTO.getProductName())) {
                log.error("An error occurred while updating stock");
                throw new GeneralException("Product name already exists: " + stockRequestDTO.getProductName());
            }
        } else {
            log.error("An error occurred while updating stock");
            throw new GeneralException("Product name already exists: " + stockRequestDTO.getProductName());
        }

        // Update atribut lain jika diperlukan
        existingStock.setProductName(stockRequestDTO.getProductName());
        existingStock.setQuantity(stockRequestDTO.getQuantity());
        existingStock.setCourier(stockRequestDTO.getCourier());

        stockRepository.save(existingStock);
    }
    @Transactional
    public void reduceStock(String productName, int quantity) {
        Stock stock = stockRepository.findByProductName(productName);
        if (stock != null) {
            int currentQuantity = stock.getQuantity();
            if (currentQuantity >= quantity) {
                stock.setQuantity(currentQuantity - quantity);
                stockRepository.save(stock);
            } else {
                log.error("An error occurred while processing order");
                throw new GeneralException("Insufficient stock available");
            }
        } else {
            log.error("An error occurred while processing order");
            throw new GeneralException("Product not found in stock");
        }
    }

}
