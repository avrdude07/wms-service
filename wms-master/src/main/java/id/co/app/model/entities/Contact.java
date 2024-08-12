package id.co.app.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "CONTACT")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Contact {

    @Id
    @SequenceGenerator(name = "contact_sequence", sequenceName = "contact_sequence", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_sequence")
    private Long idContact;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "created_at")
    private Date createdDate;

    @Column(name = "updated_at")
    private Date updatedDate;
}
