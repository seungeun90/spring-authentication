package com.spring.auth.pass.config;

import com.dreamsecurity.mobileOK.MobileOKException;
import com.dreamsecurity.mobileOK.mobileOKKeyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class PassConfiguration {

    private final PassProperties passProperties;

    @Bean
    public mobileOKKeyManager mobileOKKeyManager() throws MobileOKException, IOException {
        mobileOKKeyManager mobileOK = new mobileOKKeyManager();
        InputStream is = new ClassPathResource(passProperties.getKeyFilePath()).getInputStream();
        File keyFile = File.createTempFile("keyInfo", ".dat");
        //log.info("resource1 {}", keyFile.getPath());

        try (FileOutputStream fos = new FileOutputStream(keyFile)) {
            int read;
            byte[] bytes = new byte[1024];

            while ((read = is.read(bytes)) != -1) {
                fos.write(bytes, 0, read);
            }
        }

        mobileOK.keyInit(keyFile.getPath(), passProperties.getKeyFileCode());
        keyFile.deleteOnExit();
        return mobileOK;
    }

}
