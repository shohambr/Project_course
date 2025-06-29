package DomainLayer;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "storeID", referencedColumnName = "id")
    private String storeId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "rating")
    private double rating;

    @Column(name = "category")
    private String category;

    // New image and product enhancement fields
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "gallery_images", columnDefinition = "TEXT")
    private String galleryImages; // JSON array of image URLs

    @Column(name = "discount_percentage")
    private float discountPercentage = 0.0f;

    @Column(name = "review_count")
    private int reviewCount = 0;

    @Column(name = "is_featured")
    private boolean isFeatured = false;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "sku", length = 50)
    private String sku;

    // Constructors
    public Product(String id, String storeId, String name, String description, float price , int quantity, double rating, String category) {
        if(quantity > 0) {
            this.id = id;
            this.storeId = storeId;
            this.name = name;
            this.description = description;
            this.price = price;
            this.quantity = quantity;
            this.rating = rating;
            this.category = category;
        } else {
            throw new IllegalArgumentException("Product must have at least 1 quantity");
        }
    }

    public Product() {
        // Needed for JPA / Jackson
    }

    // Getters
    public String getId() { return id; }
    public String getStoreId() { return storeId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public float getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public double getRating() { return rating; }
    public String getCategory() { return category; }

    // New getters for image and enhancement fields
    public String getImageUrl() { return imageUrl; }
    public String getGalleryImages() { return galleryImages; }
    public float getDiscountPercentage() { return discountPercentage; }
    public int getReviewCount() { return reviewCount; }
    public boolean isFeatured() { return isFeatured; }
    public String getBrand() { return brand; }
    public String getSku() { return sku; }

    // Setters
    public synchronized void setId(String id) { this.id = id; }
    public synchronized void setStoreId(String storeId) { this.storeId = storeId; }
    public synchronized void setName(String name) { this.name = name; }
    public synchronized void setDescription(String description) { this.description = description; }
    public synchronized void setPrice(int price) { this.price = price; }
    public synchronized void setQuantity(int quantity) { this.quantity = quantity; }
    public synchronized void setRating(double rating) { this.rating = rating; }
    public synchronized void setCategory(String category) { this.category = category; }

    // New setters for image and enhancement fields
    public synchronized void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public synchronized void setGalleryImages(String galleryImages) { this.galleryImages = galleryImages; }
    public synchronized void setDiscountPercentage(float discountPercentage) { this.discountPercentage = discountPercentage; }
    public synchronized void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }
    public synchronized void setFeatured(boolean featured) { this.isFeatured = featured; }
    public synchronized void setBrand(String brand) { this.brand = brand; }
    public synchronized void setSku(String sku) { this.sku = sku; }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", storeId=" + storeId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", rating=" + rating +
                ", category=" + category +
                '}';
    }

    public boolean addRating(String username, double rate) {
        return false;
    }
}
