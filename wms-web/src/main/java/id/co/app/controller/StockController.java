package id.co.app.controller;

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
            map.put("data", page.getContent());
            map.put("limit", String.valueOf(page.getPageable().getPageSize()));
            map.put("offset", String.valueOf(page.getPageable().getOffset() + 1));
            map.put("total", String.valueOf(page.getTotalElements()));
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e){
            log.error("Error Get Data From Table Stock " + e.getMessage());
            map.put("data", "");
            map.put("message", "Error Get Data From Table Stock " + e.getMessage());
            map.put("status", "400");
            map.put("total", "0");
            log.error(String.format(Arrays.toString(e.getStackTrace())));
            return ResponseEntity.badRequest().body(map);
        }

    }

    @PostMapping
    public ResponseEntity<Object> addStock(@RequestBody StockRequestDTO stockRequestDTO) {
        Map<String, Object> map = new HashMap<>();
        map = stockService.addNewStock(stockRequestDTO);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping(path = "{stockId}")
    public ResponseEntity<Object> deleteStudent(@PathVariable("stockId") Long stockId) {
        Map<String, Object> map = new HashMap<>();
        map = stockService.deleteStock(stockId);
        return new ResponseEntity<>(map, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateStock(@PathVariable Long id, @RequestBody StockRequestDTO stockRequestDTO) {
        Map<String, Object> map = new HashMap<>();
        map = stockService.updateStock(id, stockRequestDTO);
        if (map.get("response").equals("success")){
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }
}
