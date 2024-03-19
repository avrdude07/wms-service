package id.co.app.controller;

import id.co.app.exception.GeneralException;
import id.co.app.model.dto.OrderRequestDTO;
import id.co.app.model.entities.Order;
import id.co.app.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<Object> getOrders(@RequestParam(required = false) String productName,
                                            @RequestParam(required = false) String customer,
                                            @RequestParam(required = false) String fromDate,
                                            @RequestParam(required = false) String toDate,
                                            @RequestParam(defaultValue = "1") int offset,
                                            @RequestParam(defaultValue = "10") int limit,
                                            @RequestParam(defaultValue = "productName") String sortBy,
                                            @RequestParam(defaultValue = "DESC") String orderBy
    ){
        Map<String, Object> map = new HashMap<>();
        try {
            Page<Order> page =  orderService.getOrders(productName, fromDate, toDate, customer, offset, limit, sortBy, orderBy);
            map.put("data", page.getContent());
            map.put("limit", String.valueOf(page.getPageable().getPageSize()));
            map.put("offset", String.valueOf(page.getPageable().getOffset() + 1));
            map.put("total", String.valueOf(page.getTotalElements()));
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error Get Data From Table Order " + e.getMessage());
            map.put("data", "");
            map.put("message", "Error Get Data From Table Order " + e.getMessage());
            map.put("status", "400");
            map.put("total", "0");
            log.error(String.format(Arrays.toString(e.getStackTrace())));
            return ResponseEntity.badRequest().body(map);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        Map<String, Object> map = new HashMap<>();
        try {
            orderService.placeOrder(orderRequestDTO, map);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (GeneralException e) {
            log.error("An error occurred while processing order: {}", e.getErrorMap());
            return new ResponseEntity<>(e.getErrorMap(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            log.error("An unexpected error occurred while processing order: {}", e.getMessage());
            errorMap.put("response", "fail");
            errorMap.put("error", e.getMessage());
            return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
