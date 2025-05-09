package DomainLayer;

import java.util.List;
import java.util.Map;

/**
 * Repository interface for customer inquiries
 * This interface defines methods for storing and retrieving customer inquiries
 */
public interface ICustomerInquiryRepository {
    
    /**
     * Get all customer inquiries for a specific store
     * 
     * @param storeId ID of the store
     * @return List of inquiries as maps with inquiry details
     */
    List<Map<String, Object>> getInquiriesByStore(String storeId);
    
    /**
     * Add a new customer inquiry
     * 
     * @param storeId ID of the store
     * @param inquiry Map containing inquiry details
     */
    void addInquiry(String storeId, Map<String, Object> inquiry);
    
    /**
     * Update an existing inquiry
     * 
     * @param storeId ID of the store
     * @param inquiryId ID of the inquiry
     * @param updatedFields Map of fields to update and their new values
     * @return true if successful, false otherwise
     */
    boolean updateInquiry(String storeId, String inquiryId, Map<String, Object> updatedFields);
    
    /**
     * Get customer inquiries for a specific customer across all stores
     * 
     * @param customerId ID of the customer
     * @return List of inquiries as maps with inquiry details
     */
    List<Map<String, Object>> getInquiriesByCustomer(String customerId);
    
    /**
     * Get the next unique inquiry ID
     * 
     * @return Next inquiry ID
     */
    String getNextInquiryId();
}