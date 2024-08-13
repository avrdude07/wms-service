package id.co.app.controller;

import id.co.app.config.JasyptEncryptorConfig;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/demo")
@RequiredArgsConstructor
public class DemoController {

    private  final JasyptEncryptorConfig jasyptEncryptorConfig;
    @GetMapping
    public ResponseEntity<String> sayHello() {
        return  ResponseEntity.ok("Hello from secured endpoint");
    }

    @GetMapping("/encrypt")
    public ResponseEntity<String> encrypt(@RequestBody String input) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(jasyptEncryptorConfig.configEncryptDecrypt());
        return  ResponseEntity.ok(encryptor.encrypt(input));
    }

    @GetMapping("/decrypt")
    public ResponseEntity<String> decrypt(@RequestBody String input) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(jasyptEncryptorConfig.configEncryptDecrypt());
        return  ResponseEntity.ok(encryptor.decrypt(input));
    }
}
