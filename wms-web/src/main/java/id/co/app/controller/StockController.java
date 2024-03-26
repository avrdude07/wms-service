package id.co.app.controller;

import static id.co.app.constant.Constants.*;
import id.co.app.model.dto.StockRequestDTO;
import id.co.app.model.dto.SuccessResponseDto;
import id.co.app.model.entities.Stock;
import id.co.app.services.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<SuccessResponseDto> addStock(@RequestBody StockRequestDTO stockRequestDTO) {
        stockService.addNewStock(stockRequestDTO);
        return new ResponseEntity<>(new SuccessResponseDto("Insert stock successfully"), HttpStatus.OK);
    }

    @DeleteMapping("/{stockId}")
    public ResponseEntity<SuccessResponseDto> deleteStudent(@PathVariable("stockId") Long stockId) {
        stockService.deleteStock(stockId);
        return new ResponseEntity<>(new SuccessResponseDto("Delete stock successfully"), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{stockId}")
    public ResponseEntity<SuccessResponseDto> updateStock(@PathVariable("stockId") Long stockId, @RequestBody StockRequestDTO stockRequestDTO) {
        stockService.updateStock(stockId, stockRequestDTO);
        return new ResponseEntity<>(new SuccessResponseDto("Update stock successfully"), HttpStatus.OK);
    }
}
