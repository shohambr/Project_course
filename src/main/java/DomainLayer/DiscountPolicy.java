package DomainLayer;
import java.util.*;
import java.util.UUID;



public class DiscountPolicy {

    //private List<Discount> discounts = new ArrayList<>();
    private float originalPrice = 0;


//     public float applyDiscounts(Map<Product , Integer> products){

//         List<Discount> dummyDiscounts = this.discounts;
//         Map<Product, Float> productMap = new HashMap<>();


//         for (Map.Entry<Product, Integer> entry : products.entrySet()) {                              
//             Product product = entry.getKey();
//             float value = entry.getValue();
//             productMap.put(product, value);         
//         }


//         List<Discount> removedDiscounts = new ArrayList<>();

//         while (!dummyDiscounts.isEmpty()) {
//             Discount current = dummyDiscounts.get(0);
//             this.removeNestedDiscounts(current, removedDiscounts);
//         }
//         float price = 0f;
//         for (Map.Entry<Product, Float> entry : productMap.entrySet()) {
//             Product product = entry.getKey();
//             float value = entry.getValue();
//             for (Discount d : removedDiscounts) {
//                 productMap = d.applyDiscount(productMap, this.originalPrice, products, removedDiscounts);
//             }
//             price += product.getPrice() * value;
//         }
//         return price;
//     }


//     // Recursively removes all discounts nested within `discount` from the list
//     private void removeNestedDiscounts(Discount discount, List<Discount> removedDiscounts) {
        
//         addDiscountIfNotExists(removedDiscounts, discount);


//         for(Discount d: discount.discounts){
//             boolean exists = removedDiscounts.stream().anyMatch(disc -> disc.getId().equals(d.getId()));
//         if (!exists) {
//             this.removeNestedDiscounts(d, removedDiscounts);
//         }

//         }
//     }



// public void addDiscountIfNotExists(List<Discount> removedDiscounts, Discount newDiscount) {
//     boolean exists = removedDiscounts.stream().anyMatch(disc -> disc.getId().equals(newDiscount.getId()));


//     if (!exists) {
//         removedDiscounts.add(newDiscount);
//     }
// }
// }


//  class Discount {
//     boolean alreadyUsed = false;      //In case of numericalComposition = 1
//     String Id = UUID.randomUUID().toString();
//     float level = -1;                     //1 = product 2 = category 3 = store
//     float logicComposition = -1;                  //1 = xor 2 = and 3 = or
//     float numericalComposition = -1;              //1 = Maximum 2 = Multiplication 
//     List<Discount> discounts = new ArrayList<>();  //To what apply composition of discounts
//     float percentDiscount = 0;                //Discount percentage
//     String discounted = "";            //Product, category, store
//     float conditional = -1;                  //-1 = no condition, 1 = minimum price, 2 = quantity
//     float limiter = -1;                  //Minimum price or quantity
//     String conditionalDiscounted = ""; //Product name or category


// public String getId() {
//     return this.Id;
// }



//     public Map<Product, Float> applyDiscount(Map<Product, Float> productDiscounts, float originalPrice, List<Product> products, List<Discount> nestedDiscounts){
      
//         if(alreadyUsed == true)
//             return productDiscounts;


//         if(logicComposition == 1){   //xor
//             float predict = 0;         //even false, odd true
//             if(this.checkConditinal(originalPrice, products)){
//                 predict = predict + 1;
//             }
//             for(Discount d : discounts){
//                 if(d.checkConditinal(originalPrice, products)){
//                     predict = predict + 1;
//                 }
//             }

//             if (predict % 2 == 1){
//                 return this.applyNewMultiplier(productDiscounts, nestedDiscounts, products);
                
//             }
//         }




//         else if(logicComposition == 2){  //and
//             boolean predict = true;
//             if(!this.checkConditinal(originalPrice, products)){
//                 predict = false;
//             }
//             for(Discount d : discounts){
//                 if(!d.checkConditinal(originalPrice, products)){
//                     predict = false;
//                 }
//             }
//             if (predict){
//                 return this.applyNewMultiplier(productDiscounts, nestedDiscounts, products);
//             }
//         } 




//         else if(logicComposition == 3){   //or
//             boolean predict = false;
//             if(this.checkConditinal(originalPrice, products)){
//                 predict = true;
//             }
//             for(Discount d : discounts){
//                 if(d.checkConditinal(originalPrice, products)){
//                     predict = true;
//                 }
//             }
          
//             if (predict){
//                 return this.applyNewMultiplier(productDiscounts, nestedDiscounts, products);
//             }
//         }


//         return productDiscounts;

//     }



//     public Map<Product, Float> applyNewMultiplier(Map<Product, Float> productDiscounts, List<Discount> nestedDiscounts, List<Product> products){
//         if (level == 1){       //product
//             if(numericalComposition == 1){        //1 = Maximum 
              
//                 boolean isMax = true;
//                 float maximum = this.calculateOriginalPriceP(productDiscounts, products);
//                 for (Discount d : discounts) {
//                     double nestedTotal = d.calculateOriginalPriceP(productDiscounts, products);
//                     if (nestedTotal > maximum) {
//                         isMax = false;
//                     }
//                     else{
//                         d.alreadyUsed = true;
//                     }
//                 }
//                 if(isMax){
//                     this.alreadyUsed = true;
//                     for (Map.Entry<Product, Float> entry : productDiscounts.entrySet()) {
//                         Product product = entry.getKey();
//                         float value = entry.getValue();
  
//                         if (product.getName().equals(this.conditionalDiscounted)) {
//                             float discountedValue =  value - percentDiscount;
//                             productDiscounts.put(product, discountedValue);
//                         }
//                     }
//                 }
//                 return productDiscounts;
//             }


//             else if(numericalComposition == 2){     //2 = Multiplication 
//                 for (Map.Entry<Product, Float> entry : productDiscounts.entrySet()) {
//                     Product product = entry.getKey();
//                     float value = entry.getValue();
  
//                     if (product.getName().equals(this.conditionalDiscounted)) {
//                         float discountedValue =  value * (1 - percentDiscount);
//                         productDiscounts.put(product, discountedValue);
//                     }
//                 }
//                 return productDiscounts;
//             }

//             else if(numericalComposition == -1){
              
//                 for (Map.Entry<Product, Float> entry : productDiscounts.entrySet()) {
//                     Product product = entry.getKey();
//                     float value = entry.getValue();
  
//                     if (product.getName().equals(this.conditionalDiscounted)) {
//                         float discountedValue =  value - percentDiscount;
//                         productDiscounts.put(product, discountedValue);
//                     }
//                 }
//                 return productDiscounts;
//             }
//         }













//         else if (level == 2){  //category
//             if(numericalComposition == 1){        //1 = Maximum 
              
//                 boolean isMax = true;
//                 float maximum = this.calculateOriginalPriceC(productDiscounts, products);
//                 for (Discount d : discounts) {
//                     double nestedTotal = d.calculateOriginalPriceC(productDiscounts, products);
//                     if (nestedTotal > maximum) {
//                         isMax = false;
//                     }
//                     else{
//                         d.alreadyUsed = true;
//                     }
//                 }
//                 if(isMax){
//                     this.alreadyUsed = true;
//                     for (Map.Entry<Product, Float> entry : productDiscounts.entrySet()) {
//                         Product product = entry.getKey();
//                         float value = entry.getValue();
  
//                         if (product.getCategory().equals(this.discounted)) {
//                             float discountedValue =  value - percentDiscount;
//                             productDiscounts.put(product, discountedValue);
//                         }
//                     }
//                 }
//                 return productDiscounts;
//             }













//             else if(numericalComposition == 2){     //2 = Multiplication 
//                 for (Map.Entry<Product, Float> entry : productDiscounts.entrySet()) {
//                     Product product = entry.getKey();
//                     float value = entry.getValue();
  
//                     if (product.getCategory().equals(this.discounted)) {
//                         float discountedValue =  value * (1 - percentDiscount);
//                         productDiscounts.put(product, discountedValue);
//                     }
//                 }
//                 return productDiscounts;
//             }









//             else if(numericalComposition == -1){
              
//                 for (Map.Entry<Product, Float> entry : productDiscounts.entrySet()) {
//                     Product product = entry.getKey();
//                     float value = entry.getValue();
  
//                     if (product.getName().equals(this.discounted)) {
//                         float discountedValue =  value - percentDiscount;
//                         productDiscounts.put(product, discountedValue);
//                     }
//                 }
//                 return productDiscounts;
//             }
//         }
















//         else if (level == 3){  //store
//             if(numericalComposition == 1){        //1 = Maximum 
              
//                 boolean isMax = true;
//                 float maximum = this.calculateOriginalPriceS(productDiscounts, products);
//                 for (Discount d : discounts) {
//                     double nestedTotal = d.calculateOriginalPriceS(productDiscounts, products);
//                     if (nestedTotal > maximum) {
//                         isMax = false;
//                     }
//                     else{
//                         d.alreadyUsed = true;
//                     }
//                 }
//                 if(isMax){
//                     this.alreadyUsed = true;
//                     for (Map.Entry<Product, Float> entry : productDiscounts.entrySet()) {
//                         Product product = entry.getKey();
//                         float value = entry.getValue();
  
//                         if (true) {
//                             float discountedValue =  value - percentDiscount;
//                             productDiscounts.put(product, discountedValue);
//                         }
//                     }
//                 }
//                 return productDiscounts;
//             }












//             else if(numericalComposition == 2){     //2 = Multiplication 
//                  for (Map.Entry<Product, Float> entry : productDiscounts.entrySet()) {
//                      Product product = entry.getKey();
//                      float value = entry.getValue();
  
//                      if (true) {
//                          float discountedValue =  value * (1 - percentDiscount);
//                          productDiscounts.put(product, discountedValue);
//                      }
//                  }
//                  return productDiscounts;
//              }












//              else if(numericalComposition == -1){
              
//                  for (Map.Entry<Product, Float> entry : productDiscounts.entrySet()) {
//                      Product product = entry.getKey();
//                      float value = entry.getValue();
  
//                      if (true) {
//                          float discountedValue =  value - percentDiscount;
//                          productDiscounts.put(product, discountedValue);
//                      }
//                  }
//                  return productDiscounts;
//              }

//          }
//         return productDiscounts;

//      }








//      private float calculateOriginalPriceP(Map<Product, Float> productDiscounts, List<Product> products) {
//          float total = 0;
//          for (Map.Entry<Product, Float> entry : productDiscounts.entrySet()) {
//              if(entry.getKey().getName().equals(this.discounted))
//                  total += entry.getKey().getPrice();
//          }
//          return total;
//      }


//      private float calculateOriginalPriceC(Map<Product, Float> productDiscounts, List<Product> products) {
//          float total = 0;
//          for (Map.Entry<Product, Float> entry : productDiscounts.entrySet()) {
//             if(entry.getKey().getCategory().equals(this.discounted))
//                  total += entry.getKey().getPrice();
//          }
//          return total;
//      }


//      private float calculateOriginalPriceS(Map<Product, Float> productDiscounts, List<Product> products) {
//          float total = 0;
//          for (Map.Entry<Product, Float> entry : productDiscounts.entrySet()) {
//              total += entry.getKey().getPrice();
//          }
//          return total;
//      }


//      boolean checkConditinal(float originalPrice, List<Product> products){
//          if(this.conditional == -1){
//              return true;
//          }
//          else if(this.conditional == 1){
//              return originalPrice >= limiter;
//          }
//          else if(this.conditional == 2){
//              float quantity = -1;
      
//              for (Product p : products) {
//                  if (p.getName().equals(this.conditionalDiscounted)) {
//                      quantity =  p.getQuantity();
//                  }
          
//              if(quantity == -1 || quantity >= limiter ){
//                  return true;
//              }
//              else{
//                  return false;
//              }
//              }
//          return false;     
//          }
//          else{
//             return false;
//          }
//      }
 }