package id.co.app.services;

import id.co.app.exception.GeneralException;
import id.co.app.model.dto.ContactRequestDTO;
import id.co.app.model.entities.Contact;
import id.co.app.repositories.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;

    public Page<Contact> getContacts(String phoneNumber, String email, String fromDate, String toDate, int offset, int limit, String sortBy, String orderBy) throws ParseException {
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
            log.info("Masuk Filter Contact Dengan Date");
            return contactRepository.getContactPageFilterWithDate(phoneNumber, email, startDate, endDate, pageable);
        } else {
            log.info("Masuk Filter Contact Tanpa Date");
            return contactRepository.getContactPageFilter(phoneNumber, email, pageable);
        }
    }

    @Transactional
    public void addNewContact(ContactRequestDTO contactRequestDTO) {
        Date newDate = new Date();
        Contact newContact = new Contact();
        newContact.setIdContact(contactRequestDTO.getId());
        newContact.setFirstName(contactRequestDTO.getFirstName());
        newContact.setLastName(contactRequestDTO.getLastName());
        newContact.setEmail(contactRequestDTO.getEmail());
        newContact.setPhoneNumber(contactRequestDTO.getPhoneNumber());
        newContact.setCreatedDate(newDate);

        Contact checkContact= contactRepository.findByEmail(newContact.getEmail());
        if (checkContact != null){
            log.error("An error occurred while updating contact");
            throw new GeneralException("Data contact dengan email " + newContact.getEmail() + " Sudah Ada");
        }
        contactRepository.save(newContact);
    }

    @Transactional
    public void deleteContact(Long contactId) {
        boolean exists = contactRepository.existsById(contactId);
        if(!exists){
            log.error("An error occurred while deleting  contact");
            throw new GeneralException("Contact with id " + contactId + " does not exists");
        }
        contactRepository.deleteById(contactId);
    }

    @Transactional
    public  void updateContact(Long contactId, ContactRequestDTO contactRequestDTO) {
        Optional<Contact> optionalContact = contactRepository.findById(contactId);

        if (optionalContact.isEmpty()) {
            log.error("An error occurred while updating contact");
            throw new GeneralException("Contact not found with id: " + contactId);
        }
        Contact existingContact = optionalContact.get();

        // Periksa apakah nama produk yang diberikan sudah digunakan oleh entitas lain
        if (!existingContact.getEmail().equals(contactRequestDTO.getEmail())) {
            if (contactRepository.existsByEmail(contactRequestDTO.getEmail())) {
                log.error("An error occurred while updating contact");
                throw new GeneralException("Email already exists: " + contactRequestDTO.getEmail());
            }
        } else {
            log.error("An error occurred while updating contact");
            throw new GeneralException("Email already exists: " + contactRequestDTO.getEmail());
        }

        // Update atribut lain jika diperlukan
        existingContact.setFirstName(contactRequestDTO.getFirstName());
        existingContact.setLastName(contactRequestDTO.getLastName());
        existingContact.setEmail(contactRequestDTO.getEmail());
        existingContact.setPhoneNumber(contactRequestDTO.getPhoneNumber());
        existingContact.setUpdatedDate(new Date());

        contactRepository.save(existingContact);
    }
}
