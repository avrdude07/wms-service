package id.co.app.controller;

import static id.co.app.constant.Constants.*;

import id.co.app.model.dto.ErrorResponseDto;
import id.co.app.model.dto.OrderRequestDTO;
import id.co.app.model.dto.ResponseApiDto;
import id.co.app.model.dto.SuccessResponseDto;
import id.co.app.model.entities.Order;
import id.co.app.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ResponseApiDto> getOrders(@RequestParam(required = false) String productName,
                                            @RequestParam(required = false) String customer,
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
            Page<Order> page =  orderService.getOrders(productName, fromDate, toDate, customer, offset, limit, sortBy, orderBy);
            map.put(PAGE_SIZE, String.valueOf(page.getPageable().getPageSize()));
            map.put(CURRENT_PAGE, String.valueOf(page.getPageable().getOffset() + 1));
            map.put(TOTAL_PAGE, String.valueOf(page.getTotalPages()));
            map.put(TOTAL_DATA, String.valueOf(page.getTotalElements()));
            successResponseDto.setData(page.getContent());
            successResponseDto.setPaging(map);
            successResponseDto.setMessage("Success Get Data From Table Order");
            successResponseDto.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(successResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto();
            log.error("Error Get Data From Table Order ", e);
            errorResponseDto.setErrors("Error Get Data From Table Order " + e.getMessage());
            errorResponseDto.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<ResponseApiDto> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        SuccessResponseDto successResponseDto = new SuccessResponseDto();
        orderService.placeOrder(orderRequestDTO);
        successResponseDto.setStatus(HttpStatus.OK);
        successResponseDto.setMessage("Berhasil melakukan order");
        return new ResponseEntity<>(successResponseDto, HttpStatus.OK);
    }
}
