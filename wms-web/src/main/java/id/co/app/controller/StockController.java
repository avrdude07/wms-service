package id.co.app.controller;

import static id.co.app.constant.Constants.*;
import id.co.app.exception.GeneralException;
import id.co.app.model.dto.StockRequestDTO;
import id.co.app.model.entities.Stock;
import id.co.app.services.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("api/v1/stock")
@Slf4j
public class StockController {

    @Autowired
    StockService stockService;

    @GetMapping
    public ResponseEntity<Object> getStocks(@RequestParam(required = false) String productName,
                                            @RequestParam(required = false) String courier,
                                            @RequestParam(required = false) String fromDate,
                                            @RequestParam(required = false) String toDate,
                                            @RequestParam(defaultValue = "1") int offset,
                                            @RequestParam(defaultValue = "10") int limit,
                                            @RequestParam(defaultValue = "productName") String sortBy,
                                            @RequestParam(defaultValue = "DESC") String orderBy
                                            ){
        Map<String, Object> map = new HashMap<>();
        try {
            Page<Stock> page =  stockService.getStocks(productName, fromDate, toDate, courier, offset, limit, sortBy, orderBy);
            map.put(DATA, page.getContent());
            map.put(LIMIT, String.valueOf(page.getPageable().getPageSize()));
            map.put(OFFSET, String.valueOf(page.getPageable().getOffset() + 1));
            map.put(TOTAL, String.valueOf(page.getTotalElements()));
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e){
            log.error("Error Get Data From Table Stock " + e.getMessage());
            map.put(DATA, "");
            map.put(MESSAGE, "Error Get Data From Table Stock " + e.getMessage());
            map.put(STATUS, "400");
            map.put(TOTAL, "0");
            log.error(String.format(Arrays.toString(e.getStackTrace())));
            return ResponseEntity.badRequest().body(map);
        }

    }

    @PostMapping
    public ResponseEntity<Object> addStock(@RequestBody StockRequestDTO stockRequestDTO) {
        Map<String, Object> map = new HashMap<>();
        try {
            stockService.addNewStock(stockRequestDTO, map);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (GeneralException e) {
            log.error("An error occurred while updating stock: {}", e.getErrorMap());
            return new ResponseEntity<>(e.getErrorMap(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            log.error("An unexpected error occurred while updating   stock: {}", e.getMessage());
            errorMap.put(RESPONSE, FAIL);
            errorMap.put(ERROR, e.getMessage());
            return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping(path = "{stockId}")
    public ResponseEntity<Object> deleteStudent(@PathVariable("stockId") Long stockId) {
        Map<String, Object> map = new HashMap<>();
        try {
            stockService.deleteStock(stockId, map);
            return new ResponseEntity<>(map, HttpStatus.NO_CONTENT);
        } catch (GeneralException e) {
            log.error("An error occurred while deleting  stock: {}", e.getErrorMap());
            return new ResponseEntity<>(e.getErrorMap(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            log.error("An unexpected error occurred while deleting  stock: {}", e.getMessage());
            errorMap.put(RESPONSE, FAIL);
            errorMap.put(ERROR, e.getMessage());
            return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateStock(@PathVariable Long id, @RequestBody StockRequestDTO stockRequestDTO) {
        Map<String, Object> map = new HashMap<>();
        try {
            stockService.updateStock(id, stockRequestDTO, map);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (GeneralException e) {
            log.error("An error occurred while updating stock: {}", e.getErrorMap());
            return new ResponseEntity<>(e.getErrorMap(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            log.error("An unexpected error occurred while updating stock: {}", e.getMessage());
            errorMap.put(RESPONSE, FAIL);
            errorMap.put(ERROR, e.getMessage());
            return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
