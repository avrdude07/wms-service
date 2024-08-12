package id.co.app.controller;

import id.co.app.helper.GeneralHelper;
import id.co.app.model.dto.ContactRequestDTO;
import id.co.app.model.dto.ErrorResponseDto;
import id.co.app.model.dto.SuccessResponseDto;
import id.co.app.model.entities.Contact;
import id.co.app.services.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/contact")
@Slf4j
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<Object> getContacts(@RequestParam(required = false) String phoneNumber,
                                              @RequestParam(required = false) String email,
                                              @RequestParam(required = false) String fromDate,
                                              @RequestParam(required = false) String toDate,
                                              @RequestParam(defaultValue = "1") int offset,
                                              @RequestParam(defaultValue = "10") int limit,
                                              @RequestParam(defaultValue = "phoneNumber") String sortBy,
                                              @RequestParam(defaultValue = "DESC") String orderBy
    ){
        Map<String, Object> map = new HashMap<>();
        SuccessResponseDto successResponseDto = new SuccessResponseDto();
        try {
            Page<Contact> page =  contactService.getContacts(phoneNumber, email, fromDate, toDate, offset, limit, sortBy, orderBy);
            GeneralHelper.setMetaData(successResponseDto, map, page);
            successResponseDto.setMessage("Success Get Data From Table Contact");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e){
            ErrorResponseDto errorResponseDto = new ErrorResponseDto();
            log.error("Error Get Data From Table Contact ", e);
            errorResponseDto.setErrors("Error Get Data From Table Contact " + e.getMessage());
            errorResponseDto.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping
    public ResponseEntity<SuccessResponseDto> addContact(@RequestBody ContactRequestDTO contactRequestDTO) {
        contactService.addNewContact(contactRequestDTO);
        return new ResponseEntity<>(new SuccessResponseDto(), HttpStatus.OK);
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<SuccessResponseDto> deleteContact(@PathVariable("contactId") Long contactId) {
        contactService.deleteContact(contactId);
        return new ResponseEntity<>(new SuccessResponseDto(), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{contactId}")
    public ResponseEntity<SuccessResponseDto> updateContact(@PathVariable("contactId") Long contactId, @RequestBody ContactRequestDTO contactRequestDTO) {
        contactService.updateContact(contactId, contactRequestDTO);
        return new ResponseEntity<>(new SuccessResponseDto(), HttpStatus.OK);
    }
}
