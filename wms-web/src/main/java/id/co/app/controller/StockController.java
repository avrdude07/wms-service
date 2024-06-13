package id.co.app.controller;

import static id.co.app.constant.Constants.*;

import id.co.app.model.dto.ErrorResponseDto;
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
        SuccessResponseDto successResponseDto = new SuccessResponseDto();
        try {
            Page<Stock> page =  stockService.getStocks(productName, fromDate, toDate, courier, offset, limit, sortBy, orderBy);
            map.put(PAGE_SIZE, String.valueOf(page.getPageable().getPageSize()));
            map.put(CURRENT_PAGE, String.valueOf(page.getPageable().getOffset() + 1));
            map.put(TOTAL_PAGE, String.valueOf(page.getTotalPages()));
            map.put(TOTAL_DATA, String.valueOf(page.getTotalElements()));
            successResponseDto.setData(page.getContent());
            successResponseDto.setPaging(map);
            successResponseDto.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e){
            ErrorResponseDto errorResponseDto = new ErrorResponseDto();
            log.error("Error Get Data From Table Stock ", e);
            errorResponseDto.setErrors("Error Get Data From Table Stock " + e.getMessage());
            errorResponseDto.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping
    public ResponseEntity<SuccessResponseDto> addStock(@RequestBody StockRequestDTO stockRequestDTO) {
        stockService.addNewStock(stockRequestDTO);
        return new ResponseEntity<>(new SuccessResponseDto(), HttpStatus.OK);
    }

    @DeleteMapping("/{stockId}")
    public ResponseEntity<SuccessResponseDto> deleteStock(@PathVariable("stockId") Long stockId) {
        stockService.deleteStock(stockId);
        return new ResponseEntity<>(new SuccessResponseDto(), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{stockId}")
    public ResponseEntity<SuccessResponseDto> updateStock(@PathVariable("stockId") Long stockId, @RequestBody StockRequestDTO stockRequestDTO) {
        stockService.updateStock(stockId, stockRequestDTO);
        return new ResponseEntity<>(new SuccessResponseDto(), HttpStatus.OK);
    }
}
