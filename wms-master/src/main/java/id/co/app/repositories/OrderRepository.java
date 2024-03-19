package id.co.app.repositories;

import id.co.app.model.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT o FROM Order o WHERE " +
            "(?1 IS NULL OR CONCAT(o.productName, '') LIKE %?1%) AND " +
            "(?2 IS NULL OR CONCAT(o.customer, '') LIKE %?2%)")
    Page<Order> getOrderPageFilter(String productName, String customer, Pageable pageable);

    @Query(value = "SELECT o FROM Order o WHERE " +
            "(?1 IS NULL OR CONCAT(o.productName, '') LIKE %?1%) AND " +
            "(?2 IS NULL OR CONCAT(o.customer, '') LIKE %?2%) AND " +
            "o.date BETWEEN ?3 AND ?4")
    Page<Order> getOrderPageFilterWithDate(String productName, String customer, Date startDate, Date endDate, Pageable pageable);
}
