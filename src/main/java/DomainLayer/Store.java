package DomainLayer;


import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micrometer.observation.Observation.Event;
import ServiceLayer.EventLogger;
public class Store {
    private String id= UUID.randomUUID().toString();
    private PurchasePolicy purchasePolicy = new PurchasePolicy();
    private DiscountPolicy discountPolicy = new DiscountPolicy();
    private List<String> users = new ArrayList<>();
    private Map<String, Integer> products = new HashMap<>();
    private Map<String, Integer> reservedProducts = new HashMap<>();
    private Map<String, String> questions = new HashMap<>();
    private boolean openNow;
    private double rating = 0;
    private Map<String , Double> raterId = new HashMap<>();
    private String ownerId;
    private String name;


    public Store(String ownerId) {
        this.ownerId = ownerId;
        openNow = true;
    }

    public Store() {
        openNow = true;
    }

    /**
     * use this function to detect if the store is open now so the logic is not depended on the boolean itself.
     * for example a store that despite being open would like to automatically open and close in certain hours.
     * @return a boolean that says if the store is open right now
     */
    public boolean isOpenNow() {
        return openNow;
    }
    public void openTheStore() {
        openNow = true;
    }
    public void closeTheStore() {
        openNow = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;

    }

    public void setName(String name) {this.name = name;}

    public Double getRating(){
        return rating;
    }

    public void setRating(Double rating){
        this.rating = rating;
    }
    public String getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    public List<String> getUsers() {
        return users;
    }
    public void setUsers(List<String> users) {
        this.users = users;
    }
    public Map<String, Integer> getProducts() {
        return products;
    }
    public void setProducts(Map<String, Integer> products) {
        this.products = products;
    }
    public Map<String, Integer> getReservedProducts() {
        return reservedProducts;
    }
    public void setReservedProducts(Map<String, Integer> reservedProducts) {
        this.reservedProducts = reservedProducts;
    }
    public boolean isOpen() {
        return openNow;
    }
    public void setOpen(boolean open) {
        this.openNow = open;
    }
    public void setId(UUID id) {
        this.id = id.toString();
    }
    @JsonIgnore
    public PurchasePolicy getPurchasePolicy() {
        return purchasePolicy;
    }
    @JsonIgnore
    public void setPurchasePolicy(PurchasePolicy purchasePolicy) {
        this.purchasePolicy = purchasePolicy;
    }
    @JsonIgnore
    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }
    @JsonIgnore
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }


    


    public Boolean registerUser(String userId) {
        if(users.contains(userId)) {
            return false;
        }
        users.add(userId);
        return true;
    }

    public boolean increaseProduct(String productId, int quantity) {
        if (quantity <= 0) {
            return false;
        }

        if (!products.containsKey(productId)) {
            return false;
        }

        int currentQuantity = products.get(productId);
        products.put(productId, Integer.valueOf(currentQuantity + quantity));
        return true;
    }


    public boolean decreaseProduct(String idProduct, int quantity) {
        if (quantity <= 0) {
            return false;
        }

        if (!products.containsKey(idProduct)) {
            return false;
        }

        int currentQuantity = products.get(idProduct);
        if (quantity > currentQuantity) {
            return false;
        }

        int updatedQuantity = currentQuantity - quantity;

        products.put(idProduct, Integer.valueOf(updatedQuantity));

        return true;
    }

    public Map<String, Integer> getProducts() {
        return products;
    }

    public boolean changeProductQuantity(String productId, int newQuantity) {
        if (newQuantity < 0) {
            return false;
        }

        if (!products.containsKey(productId)) {
            return false;
        }

        if (newQuantity == 0) {
            products.remove(productId);
        } else {
            products.put(productId, Integer.valueOf(newQuantity));
        }

        return true;
    }


    public boolean removeProduct(String productId) {
        if (!products.containsKey(productId)) {
            return false;
        }

        products.remove(productId);
        return true;
    }


    public boolean addNewProduct(String productId, int quantity) {
        if (quantity <= 0) {
            return false;
        }

        if (!products.containsKey(productId) || products.get(productId) == 0) {
            products.put(productId, Integer.valueOf(quantity));
            return true;
        }else if (products.get(productId) > 0) {
            products.put(productId, Integer.valueOf(products.get(productId) + quantity));
            return true;
        }

        return false; // Product already exists with quantity > 0
    }


    public Integer getProductQuantity(String productId) {
        if (!products.containsKey(productId)) {
            return null;
        }
        return products.get(productId);
    }


    public void sellProduct(String productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        if (!reservedProducts.containsKey(productId)) {
            throw new IllegalArgumentException("Product not reserved");
        }
        if (reservedProducts.get(productId) < quantity) {
            throw new IllegalArgumentException("Not enough reserved quantity");
        }
        int currentQuantity = reservedProducts.get(productId);
        if (currentQuantity == quantity) {
            reservedProducts.remove(productId);
        } else {
            reservedProducts.put(productId, Integer.valueOf(currentQuantity - quantity));
        }
    }

    public boolean rate(int rate) {
        if (rate < 1 || rate > 5) {
            return false;
        }
        if (raterId.containsKey(id)) {
            double lastRate = raterId.get(id);
            rating = (rating * raterId.size() - lastRate + rate) / raterId.size();
        }
        else {
            rating = (rating * raterId.size() + rate) / (raterId.size() + 1);
        }
        raterId.put(id, Double.valueOf(rate));
        return true;
    }
    // public boolean changeProductQuantity(String productId, int newQuantity) {
    //     if (newQuantity < 0) {
    //         return false;
    //     }

    //     Product product = findProductById(productId);
    //     if (product == null) {
    //         return false;
    //     }

    //     if (newQuantity == 0) {
    //         products.remove(product);
    //     } else {
    //         products.put(product, Integer.valueOf(newQuantity));
    //     }

    //     return true;
    // }

    // public boolean removeProduct(String productId) {
    //     Product product = findProductById(productId);
    //     if (product == null) {
    //         return false;
    //     }

    //     products.remove(product);
    //     return true;
    // }


    // public int calculateProduct(Product product, int quantity) {
    //     if (quantity <= 0) {
    //         return -1;
    //     }
    //     if (!products.containsKey(product)) {
    //         return -1;
    //     }
    //     if (quantity > products.get(product)) {
    //         return -1;
    //     }

    //     return product.getPrice() * quantity;            //got to decide how price works
    // }


    // public int sellProduct(String productId, int quantity) {
    //     if (quantity <= 0) {
    //         return -1;
    //     }
    //     if (!products.containsKey(productId)) {
    //         return -1;
    //     }
    //     if (quantity > products.get(productId)) {
    //         return -1;
    //     }

    //     int updatedQuantity = products.get(productId) - quantity;
    //     if (updatedQuantity == 0) {
    //         products.remove(productId);
    //     } else {
    //         products.put(productId, Integer.valueOf(updatedQuantity));
    //     }
    //     productService.decreaseQuantity(productId, quantity);       //Changed according productService implementation
    //     Product product = productRepository.getProductById(productId);
    //     return product.getPrice() * quantity;            //got to decide how price works
    // }

    public boolean availableProduct(String productId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        return products.containsKey(productId) && products.get(productId) >= quantity;
    }

    public boolean reserveProduct(String productId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        if (!products.containsKey(productId)) {
            EventLogger.logEvent("ReserveProduct", "Product not found");
            return false;
        }
        if (products.get(productId) < quantity) {
            EventLogger.logEvent("ReserveProduct", "Not enough quantity available");
            return false;
        }
        int currentQuantity = products.get(productId);
        if (currentQuantity == quantity) {
            products.remove(productId);
            reservedProducts.put(productId, Integer.valueOf(quantity));
        } else if(currentQuantity > quantity) {
            products.put(productId, Integer.valueOf(currentQuantity - quantity));
            reservedProducts.put(productId, Integer.valueOf(quantity));
        } else {
            return false;
        }
        return true;
    }


    public boolean unreserveProduct(String productId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        if (!reservedProducts.containsKey(productId)) {
            return false;
        }
        int currentQuantity = reservedProducts.get(productId);
        if (currentQuantity < quantity) {
            return false;
        }
        if (currentQuantity == quantity) {
            reservedProducts.remove(productId);
            products.put(productId, Integer.valueOf(quantity));
        } else {
            reservedProducts.put(productId, Integer.valueOf(currentQuantity - quantity));
            products.put(productId, Integer.valueOf(products.get(productId) + quantity));
        }
        return true;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\nUsers:\n");
        for (String userId : users) {
            sb.append(userId).append("\n");
        }

        sb.append("\nAll Products in Store:\n");
        for (String productId : products.keySet()) {
            sb.append(productId).append("\n");
        }

        return sb.toString();
    }

    @JsonIgnore
    public String getOrderHistory() {
        //returns an order history
        return "";
    }

    public String getName() {return name;}
}
