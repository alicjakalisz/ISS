package com.alicja.ISS.configuration;


import com.alicja.ISS.utils.UrlReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConvertedConfig {


        @Bean
        public UrlReader util(){
            return new UrlReader();
        }
}
