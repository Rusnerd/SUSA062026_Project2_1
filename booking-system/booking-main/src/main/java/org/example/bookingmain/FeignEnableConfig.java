package org.example.bookingmain;
 
import org.springframework.cloud.openfeign.EnableFeignClients;
 
@EnableFeignClients(basePackages = "org.example.bookingmain.web")
public class FeignEnableConfig {}
