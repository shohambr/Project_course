package InfrastructureLayer;
import DomainLayer.Discount;
import DomainLayer.Product;
import org.atmosphere.config.service.Get;
import org.hibernate.sql.Update;

import java.util.HashMap;
import java.util.Map;

public class DiscountRepository {

    private final Map<String, Discount> discounts = new HashMap<>();

    public Discount find(String discountId) {
        Discount dis = discounts.get(discountId);
        return dis;
    }
}
