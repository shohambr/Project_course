package utils;

import com.fasterxml.jackson.databind.module.SimpleModule;
import DomainLayer.Product;

public class ProductKeyModule extends SimpleModule {
    public ProductKeyModule() {
        // same package → no explicit import needed
        addKeyDeserializer(Product.class, new ProductKeyDes());
    }
}