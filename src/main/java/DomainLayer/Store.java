package DomainLayer;

import ServiceLayer.EventLogger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Generated;
import jakarta.persistence.*;
import org.testng.annotations.Ignore;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "stores")
public class Store {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id = UUID.randomUUID().toString();

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "founder", nullable = false)
    private String founder;

    @Column(name = "open_now")
    private boolean openNow;

    @Column(name = "rating")
    private double rating;

    // New image and branding fields
    @Column(name = "banner_image_url", length = 500)
    private String bannerImageUrl;

    @Column(name = "logo_image_url", length = 500)
    private String logoImageUrl;

    @Column(name = "established_year")
    private Integer establishedYear;

    @Column(name = "total_products")
    private int totalProducts = 0;

    @Column(name = "total_sales")
    private int totalSales = 0;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Transient
    private PurchasePolicy purchasePolicy = new PurchasePolicy();

    @ElementCollection
    @CollectionTable(name = "store_discounts", joinColumns = @JoinColumn(name = "store_id"))
    @Column(name = "discount_id")
    private List<String> discounts = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "store_users", joinColumns = @JoinColumn(name = "store_id"))
    @Column(name = "user_id")
    private List<String> users = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "store_products", joinColumns = @JoinColumn(name = "store_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "products_quantity")
    private Map<String, Integer> products = new HashMap<>();

    //does reserved products should be in the database? todo
    @ElementCollection
    @CollectionTable(name = "reserved_products", joinColumns = @JoinColumn(name = "store_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<String, Integer> reservedProducts = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "store_questions", joinColumns = @JoinColumn(name = "store_id"))
    @MapKeyColumn(name = "query_asker_ID")
    @Column(name = "question")
    private Map<String, String> questions = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "store_ratings", joinColumns = @JoinColumn(name = "store_id"))
    @MapKeyColumn(name = "rater_id")
    @Column(name = "rating")
    private Map<String, Double> raterId = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "store_owners", joinColumns = @JoinColumn(name = "store_id"))
    @Column(name = "owner_id")
    private List<String> owners = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKeyColumn(name = "manager_id")
    @JoinColumn(name = "store_id")
    private Map<String, ManagerPermissions> managers = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "owners_to_superior", joinColumns = @JoinColumn(name = "store_id"))
    @MapKeyColumn(name = "owner_id")
    @Column(name = "superior_id")
    private Map<String, String> ownersToSuperior = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "managers_to_superior", joinColumns = @JoinColumn(name = "store_id"))
    @MapKeyColumn(name = "manager_id")
    @Column(name = "superior_id")
    private Map<String, String> managersToSuperior = new HashMap<>();

//    /**
//     * Maps store owners to their subordinates using a more normalized approach
//     * with proper JPA collection handling.
//     */
//    @ElementCollection
//    @CollectionTable(
//            name = "owner_subordinates",
//            joinColumns = @JoinColumn(name = "store_id")
//    )
//    @MapKeyColumn(name = "owner_id")
//    @OrderColumn(name = "subordinate_index") // Preserves list order
//    @Column(name = "subordinate_id")
//    private Map<String, List<String>> ownerToSubordinates = new HashMap<>();

    /**
     * Maps store owners to their subordinates using a wrapper embeddable
     */
    @ElementCollection
    @CollectionTable(
            name = "owner_subordinates",
            joinColumns = @JoinColumn(name = "store_id")
    )
    @MapKeyColumn(name = "owner_id")
    private Map<String, SubordinatesList> ownerToSubordinates = new HashMap<>();

    // Helper methods for easier access
    public List<String> getSubordinatesForOwner(String ownerId) {
        SubordinatesList list = ownerToSubordinates.get(ownerId);
        return list != null ? list.getSubordinates() : new ArrayList<>();
    }

    public void setSubordinatesForOwner(String ownerId, List<String> subordinates) {
        ownerToSubordinates.put(ownerId, new SubordinatesList(subordinates));
    }

    public void addSubordinateToOwner(String ownerId, String subordinateId) {
        ownerToSubordinates.computeIfAbsent(ownerId, k -> new SubordinatesList())
                .getSubordinates()
                .add(subordinateId);
    }

    /**
     * Wrapper entity to handle the list of subordinates
     */
    @Embeddable
    class SubordinatesList {
        @ElementCollection
        @CollectionTable(name = "subordinate_entries")
        @OrderColumn(name = "subordinate_index")
        @Column(name = "subordinate_id")
        private List<String> subordinates = new ArrayList<>();

        // Constructors
        public SubordinatesList() {}

        public SubordinatesList(List<String> subordinates) {
            this.subordinates = subordinates != null ? subordinates : new ArrayList<>();
        }

        // Getters and setters
        public List<String> getSubordinates() {
            return subordinates;
        }

        public void setSubordinates(List<String> subordinates) {
            this.subordinates = subordinates != null ? subordinates : new ArrayList<>();
        }
        public void addSubordinate(String newSubordinate) {
            this.subordinates.add(newSubordinate);
        }
        public void removeSubordinate(String subordinateId) {
            this.subordinates.remove(subordinateId);
        }
    }


    public Store(String founderID , String name) {
        this.name = name;
        founder = founderID;
        openNow = true;
    }
    public Store() {
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
    public synchronized void closeTheStore() {
        openNow = false;
    }

    public String getId() {
        return id;
    }

    public synchronized void setId(String id) {
        this.id = id;

    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Double getRating(){
        return rating;
    }

    public synchronized void setRating(Double rating){
        this.rating = rating;
    }

    // New getters and setters for image and branding fields
    public String getBannerImageUrl() { return bannerImageUrl; }
    public synchronized void setBannerImageUrl(String bannerImageUrl) { this.bannerImageUrl = bannerImageUrl; }

    public String getLogoImageUrl() { return logoImageUrl; }
    public synchronized void setLogoImageUrl(String logoImageUrl) { this.logoImageUrl = logoImageUrl; }

    public Integer getEstablishedYear() { return establishedYear; }
    public synchronized void setEstablishedYear(Integer establishedYear) { this.establishedYear = establishedYear; }

    public int getTotalProducts() { return totalProducts; }
    public synchronized void setTotalProducts(int totalProducts) { this.totalProducts = totalProducts; }

    public int getTotalSales() { return totalSales; }
    public synchronized void setTotalSales(int totalSales) { this.totalSales = totalSales; }

    public String getDescription() { return description; }
    public synchronized void setDescription(String description) { this.description = description; }
    public String getFounder() {
        return founder;
    }
    public boolean isFounder(String founder) {
        return this.founder.equals(founder);
    }
    public List<String> getUsers() {
        return users;
    }
    public synchronized void setUsers(List<String> users) {
        this.users = users;
    }

    public Map<String, Integer> getProducts() {
        return products;
    }
    public synchronized void setProducts(Map<String, Integer> products) {
        this.products = products;
    }
    public Map<String, Integer> getReservedProducts() {
        return reservedProducts;
    }
    public synchronized void setReservedProducts(Map<String, Integer> reservedProducts) {
        this.reservedProducts = reservedProducts;
    }
    public boolean isOpen() {
        return openNow;
    }
    public synchronized void setOpen(boolean open) {
        this.openNow = open;
    }
    public synchronized void setId(UUID id) {
        this.id = id.toString();
    }
    public synchronized void setDiscountPolicy(List<String> discounts) {
        this.discounts = discounts;
    }
    @JsonIgnore
    public PurchasePolicy getPurchasePolicy() {
        return purchasePolicy;
    }
    @JsonIgnore
    public synchronized void setPurchasePolicy(PurchasePolicy purchasePolicy) {
        this.purchasePolicy = purchasePolicy;
    }

    public List<String> getDiscountPolicy() {
        return discounts;
    }


    public synchronized void setDiscouns(List<String> discounts) {
        this.discounts = discounts;
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

    public synchronized boolean reserveProduct(String productId, int quantity) {
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


    public synchronized boolean unreserveProduct(String productId, int quantity) {
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


    public boolean userHasPermissions(String userId, String permission ) {
        return (owners.contains(userId) || managers.get(userId).getPermission(permission));
    }

    public String addProduct(String productName, String description, double price, int quantity, String category) {
        throw new UnsupportedOperationException("Not supported yet. - store.addProduct");
    }

    public boolean updateProductDetails(String productId, String productName, String description, double price, String category) {
        throw new UnsupportedOperationException("Not supported yet. - store.updateProductDetails");
    }

    public boolean updateProductQuantity(String productId, int newQuantity) {
        throw new UnsupportedOperationException("Not supported yet. - store.updateProductQuantity");
    }

    public void addOwner(String appointerId, String userId) {
        owners.add(userId);
        ownersToSuperior.put(userId, appointerId);
        ownerToSubordinates.put(userId, new SubordinatesList());
        ownerToSubordinates.get(appointerId).addSubordinate(userId);
    }

    public boolean userIsOwner(String userId) {
        return owners.contains(userId);
    }

    public boolean userIsManager(String userId) {
        return managers.containsKey(userId);
    }

    /**
     * Determines if a user is a superior of another user within a management hierarchy.
     * This method checks if the user identified by the "superior" parameter directly or
     * indirectly manages the user identified by the "subordinate" parameter.
     * Works for both owner and manager hierarchies.
     *
     * @param superior the unique identifier of the potential superior user
     * @param subordinate the unique identifier of the potential subordinate user
     * @return true, if the "superior" user is directly or indirectly superior to the "subordinate" user,
     *         otherwise false
     */
    public boolean checkIfSuperior(String superior, String subordinate) {
        if (superior == null || subordinate == null) {
            return false;
        }
        if(!owners.contains(superior))return false;
        if (!userIsManager(subordinate)&&!userIsOwner(subordinate)) return false;
        // Prevent checking if someone is their own superior
        if (superior.equals(subordinate)) {
            return false;
        }
        // Check if subordinate is an owner
        if (userIsOwner(subordinate)) {
            // Check if the direct superior matches
            String directSuperior = ownersToSuperior.get(subordinate);
            if (superior.equals(directSuperior)) {
                return true;
            }

            // If there's a superior, recursively check up the chain
            if (directSuperior != null) {
                return checkIfSuperior(superior, directSuperior);
            }
        }
        // Check if subordinate is a manager
        if (userIsManager(subordinate)) {
            // Check if the direct superior matches
            String directSuperior = managersToSuperior.get(subordinate);
            if (superior.equals(directSuperior)) {
                return true;
            }

            // If there's a superior, recursively check up the chain
            if (directSuperior != null) {
                return checkIfSuperior(superior, directSuperior);
            }
        }
        return false;
    }

    /**
     * Retrieves a list of all subordinates associated with the specified owner.
     * This includes direct subordinates as well as their subordinates recursively
     * within the management hierarchy.
     *
     * @param ownerId the unique identifier of the owner whose subordinates are to be retrieved
     * @return a LinkedList containing the unique identifiers of all subordinates
     */
    public LinkedList<String> getAllSubordinates(String ownerId) {
        LinkedList<String> subordinates = new LinkedList<>();

        // Check if the owner exists and has subordinates
        List<String> directSubordinates = ownerToSubordinates.get(ownerId).getSubordinates();
        if (directSubordinates == null) {
            return subordinates;
        }

        // Add direct subordinates
        subordinates.addAll(directSubordinates);

        // Recursively add subordinates of subordinates
        for (String subordinate : directSubordinates) {
            subordinates.addAll(getAllSubordinates(subordinate));
        }

        return subordinates;
    }



    public boolean removeDiscount(String id) {
        if (id == null) {
            return false;
        }
        return discounts.remove(id);
    }



    public boolean addDiscount(String discountId) {
        if (discountId == null) {
            return false;
        }
        return discounts.add(discountId);
    }


    /**
     * Terminates the ownership of the specified owner by removing their associated
     * roles and subordinates within a store management hierarchy. This includes
     * removing all subordinates of the given owner, the owner’s own role as an
     * owner, and any manager roles they may have.
     *
     * @param ownerId the unique identifier of the owner whose ownership should be terminated
     */
    public void terminateOwnership(String ownerId) {
        // First, get all subordinates that will need to be removed
        LinkedList<String> subordinatesToRemove = getAllSubordinates(ownerId);

        // Remove all subordinates first
        for (String subordinateId : subordinatesToRemove) {
            owners.remove(subordinateId);
            ownerToSubordinates.remove(subordinateId);
            ownersToSuperior.remove(subordinateId);
            // Also remove any manager roles they might have
            if (userIsManager(subordinateId)) {
                managers.remove(subordinateId);
                managersToSuperior.remove(subordinateId);
            }
        }
        // Finally remove the owner himself
        owners.remove(ownerId);
        ownerToSubordinates.remove(ownerId);
        ownersToSuperior.remove(ownerId);
        // Also remove any manager role the owner might have
        if (userIsManager(ownerId)) {
            managers.remove(ownerId);
            managersToSuperior.remove(ownerId);
        }
    }

    public void addManager(String appointerId, String userId, boolean[] permissions) {
        ManagerPermissions mp = new ManagerPermissions(permissions);
        managers.put(userId, mp);
        managersToSuperior.put(userId, appointerId);
    }

    public void changeManagersPermissions(String managerId, boolean[] permissions) {
        managers.get(managerId).setPermissions(permissions);
    }

    public void terminateManagment(String managerId) {
        this.managers.remove(managerId);
        String appointingOwner = this.managersToSuperior.get(managerId);
        this.managersToSuperior.remove(managerId);
        if (appointingOwner != null) {
            this.ownerToSubordinates.get(appointingOwner).removeSubordinate(managerId);
        }
    }

    public Map<String, Boolean> getPremissions(String managerId) {
        return managers.get(managerId).getPermissions();
    }

    private void buildRoleTree(StringBuilder sb, String founderID, String prefix, boolean isLastChild) {
        // Add current node
        sb.append(prefix);
        sb.append(isLastChild ? "└── " : "├── ");
        sb.append(founderID);
        sb.append("\n");

        // Prepare prefix for children
        String childPrefix = prefix + (isLastChild ? "    " : "│   ");

        // Get all subordinates (both owners and managers)
        List<String> subordinates = new ArrayList<>();
        List<String> ownerSubordinates = ownerToSubordinates.get(founderID).getSubordinates();
        if (ownerSubordinates != null) {
            subordinates.addAll(ownerSubordinates);
        }

        // Process each subordinate
        for (int i = 0; i < subordinates.size(); i++) {
            String subordinate = subordinates.get(i);
            buildRoleTree(sb, subordinate, childPrefix, i == subordinates.size() - 1);
        }
    }
    @JsonIgnore
    public String getRoles() {
        StringBuilder sb = new StringBuilder();
        sb.append("Store Management Structure:\n");
        buildRoleTree(sb, founder, "", true);
        return sb.toString();
    }

    public boolean closeByAdmin() {
        //todo: implement
        return false;
    }
}