package utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import DomainLayer.Product;

import java.io.IOException;

/**
 * Lets Jackson use Product IDs as map keys.
 */
public final class ProductKeyModule extends SimpleModule {

    public ProductKeyModule() {

        /* --- Product ▶︎ String when serialising --- */
        addKeySerializer(Product.class, new StdKeySerializer() {
            @Override
            public void serialize(Object value, JsonGenerator gen,
                                  SerializerProvider serializers) throws IOException {
                Product p = (Product) value;
                gen.writeFieldName(p.getId().toString());
            }
        });

        /* --- String ▶︎ Product when deserialising --- */
        addKeyDeserializer(Product.class, new KeyDeserializer() { // ← from com.fasterxml.jackson.databind
            @Override
            public Object deserializeKey(String key,
                                         DeserializationContext ctxt) {
                Product p = new Product();
                p.setId(key);  // adjust if setId expects UUID
                return p;
            }
        });
    }
}