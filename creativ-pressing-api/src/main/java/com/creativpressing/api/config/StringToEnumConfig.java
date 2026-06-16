package com.creativpressing.api.config;

import com.creativpressing.api.enums.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StringToEnumConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new Converter<String, OrderStatus>() {
            @Override public OrderStatus convert(String source) { return OrderStatus.fromValue(source); }
        });
        registry.addConverter(new Converter<String, PaymentStatus>() {
            @Override public PaymentStatus convert(String source) { return PaymentStatus.fromValue(source); }
        });
        registry.addConverter(new Converter<String, ExpenseCategory>() {
            @Override public ExpenseCategory convert(String source) { return ExpenseCategory.fromValue(source); }
        });
        registry.addConverter(new Converter<String, EmployeeRole>() {
            @Override public EmployeeRole convert(String source) { return EmployeeRole.fromValue(source); }
        });
        registry.addConverter(new Converter<String, PhotoType>() {
            @Override public PhotoType convert(String source) { return PhotoType.fromValue(source); }
        });
    }
}
