package id.co.app.repositories;

import id.co.app.model.entities.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query(value = "SELECT s FROM Stock s WHERE " +
            "(?1 IS NULL OR CONCAT(s.productName, '') LIKE %?1%) AND " +
            "(?2 IS NULL OR CONCAT(s.courier, '') LIKE %?2%)")
    Page<Stock> getStockPageFilter(String productName, String courier, Pageable pageable);

    @Query(value = "SELECT s FROM Stock s WHERE " +
            "(?1 IS NULL OR CONCAT(s.productName, '') LIKE %?1%) AND " +
            "(?2 IS NULL OR CONCAT(s.courier, '') LIKE %?2%) AND " +
            "s.date BETWEEN ?3 AND ?4")
    Page<Stock> getStockPageFilterWithDate(String productName, String courier, Date startDate, Date endDate, Pageable pageable);

    Stock findByProductName(String productName);

    boolean existsByProductName(String productName);
}
