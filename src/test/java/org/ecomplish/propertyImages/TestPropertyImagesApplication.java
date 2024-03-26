package org.ecomplish.propertyImages;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestPropertyImagesApplication {

    public static void main(String[] args) {
        SpringApplication.from(PropertyImagesApplication::main).with(TestPropertyImagesApplication.class).run(args);
    }

}
