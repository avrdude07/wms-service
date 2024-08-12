package id.co.app.repositories;

import id.co.app.model.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query(value = "SELECT c FROM Contact c WHERE " +
            "(?1 IS NULL OR CONCAT(c.phoneNumber, '') LIKE %?1%) AND " +
            "(?2 IS NULL OR CONCAT(c.email, '') LIKE %?2%)")
    Page<Contact> getContactPageFilter(String phoneNumber, String email, Pageable pageable);

    @Query(value = "SELECT c FROM Contact c WHERE " +
            "(?1 IS NULL OR CONCAT(c.phoneNumber, '') LIKE %?1%) AND " +
            "(?2 IS NULL OR CONCAT(c.email, '') LIKE %?2%) AND " +
            "c.createdDate BETWEEN ?3 AND ?4")
    Page<Contact> getContactPageFilterWithDate(String productName, String courier, Date startDate, Date endDate, Pageable pageable);

    Contact findByEmail(String email);

    boolean existsByEmail(String email);
}
