package id.co.app.services;

import id.co.app.model.dto.OrderRequestDTO;
import id.co.app.model.entities.Order;
import id.co.app.model.entities.Stock;
import id.co.app.repositories.OrderRepository;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final StockService stockService;

    public Page<Order> getOrders(String productName, String fromDate, String toDate, String customer, int offset, int limit, String sortBy, String orderBy) throws ParseException {
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
            log.info("Masuk Filter Order Dengan Date");
            return orderRepository.getOrderPageFilterWithDate(productName, customer, startDate, endDate, pageable);
        } else {
            log.info("Masuk Filter Order Tanpa Date");
            return orderRepository.getOrderPageFilter(productName, customer, pageable);
        }
    }

    public void placeOrder(OrderRequestDTO orderRequestDTO, Map<String, Object> map) {
        stockService.reduceStock(orderRequestDTO.getProductName(), orderRequestDTO.getSoldUnits(), map);

        Date newDate = new Date();
        Order newOrder = new Order();
        newOrder.setProductName(orderRequestDTO.getProductName());
        newOrder.setSoldUnits(orderRequestDTO.getSoldUnits());
        newOrder.setDate(newDate); // Set tanggal dan waktu saat ini
        newOrder.setCustomer(orderRequestDTO.getCustomer());
        orderRepository.save(newOrder);
    }
}
