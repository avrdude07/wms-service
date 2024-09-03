package id.co.app.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptEncryptorConfig {

    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor getPasswordEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(configEncryptDecrypt());
        return encryptor;
    }

    public SimpleStringPBEConfig configEncryptDecrypt() {
        final SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("Secret");  //Private Key
        config.setPoolSize("10");

        config.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setKeyObtentionIterations("1000");
        config.setProviderName("SunJCE");

        return config;
    }
}
