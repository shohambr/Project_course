package DomainLayer;
// import java.util.*;
// import java.util.UUID;

public class DiscountPolicy {
//     private List<Discount> discounts = new ArrayList<>();
//     private int originalPrice;



//     boolean applyDiscounts(List<products> products){

//     List<Discount> dummyDiscounts = this.discounts;

//     Map<Product, Integer> productMap = new HashMap<>();

//     for (Product p : products) {
//         this.originalPrice = this.originalPrice + p.getPrice();
//         productMap.put(p, 1);           
//     }

//     List<Discount> removedDiscounts = new ArrayList<>();

//     while (!dummyDiscounts.isEmpty()) {
//         Discount current = dummyDiscounts.get(0);
//         removeNestedDiscounts(current, dummyDiscounts);
//         removedDiscounts.add(current);
//         current.applyDiscount(productMap, this.originalPrice, products, removedDiscounts);
//         dummyDiscounts.remove(current);
//     }

//         return true;
//     }

//     // Recursively removes all discounts nested within `discount` from the list
//     private void removeNestedDiscounts(Discount discount, List<Discount> fromList) {
//         for (Discount d : discount.getDiscounts()) {
//             removeNestedDiscounts(d, fromList);
//             removedDiscounts.add(d);
//             fromList.remove(d);

//         }
//     }
}








// public interface Discount {
//     boolean alreadyUsed = false;      //In case of numericalComposition = 1
//     String Id = UUID.randomUUID().toString();
//     int level = -1;                     //1 = product 2 = category 3 = store
//     int logicComposition = -1;                  //1 = xor 2 = and 3 = or
//     int numericalComposition = -1;              //1 = Maximum 2 = Multiplication 
//     List<Discount> Discounts = new ArrayList<>();  //To what apply composition of discounts

//     int percentDiscount = 0;                //Discount percentage
//     String discounted = "";            //Product, category, store

//     int conditional = -1;                  //-1 = no condition, 1 = minimum price, 2 = quantity
//     int limiter = -1;                  //Minimum price or quantity
//     String conditionalDiscounted = ""; //Product name or category







//     Map<Product, Integer> applyDiscount(Map<Product, Integer> productDiscounts, int originalPrice, List<products> products, List<Discount> nestedDiscounts){
        
//         if(alreadyUsed = true)
//             return true;


//         if(logicComposition == 1){   //xor
//             int predict = 0;         //even false, odd true


//             if(this.checkConditinal()){
//                 predict = predict + 1;
//             }
//             for(Discount d : discounts){
//                 if(d.checkConditinal()){
//                     predict = predict + 1;
//                 }
//             }

//             if (predict % 2 == 1){
//                 this.applyNewMultiplier(productDiscounts, nestedDiscounts);
//             }


//         }




//         else if(logicComposition == 2){  //and

//             boolean predict = true;
//             if(!this.checkConditinal()){
//                 predict = false;
//             }
//             for(Discount d : discounts){
//                 if(!d.checkConditinal()){
//                     predict = false;
//                 }
//             }

//             if (predict){
//                 this.applyNewMultiplier(productDiscounts, nestedDiscounts);
//             }


//         } 




//         else if(logicComposition == 3){   //or

//             boolean predict = false;
//             if(this.checkConditinal()){
//                 predict = true;
//             }
//             for(Discount d : discounts){
//                 if(d.checkConditinal()){
//                     predict = true;
//                 }
//             }
            

//             if (predict){
//                 this.applyNewMultiplier(productDiscounts, nestedDiscounts);

//             }






//         }


//     }


//     Map<Product, Integer> applyNewMultiplier(Map<Product, Integer> products){
//         if (level == 1){       //product


//             if(numericalComposition == 1){        //1 = Maximum 
                
//                 boolean isMax = true;
//                 int maximum = this.calculateOriginalPriceP(productDiscounts);
//                 for (Discount d : Discounts) {
//                     double nestedTotal = d.calculateOriginalPriceP(productDiscounts);
//                     if (nestedTotal > maxNestedTotal) {
//                         isMax = false;
//                     }
//                     else{
//                         d.alreadyUsed = true;
//                     }
//                 }



//                 if(isMax){
//                     this.alreadyUsed = true;

//                     for (Map.Entry<Product, Integer> entry : products.entrySet()) {
//                         Product product = entry.getKey();
//                         int value = entry.getValue();
    
//                         if (product.getName().equals(this.conditionalDiscounted)) {
//                             float discountedValue =  value - percentDiscount;
//                             products.put(product, discountedValue);
//                         }
//                     }
//                 }

//                 return products;
//             }






//             else if(numericalComposition == 2){     //2 = Multiplication 
//                 for (Map.Entry<Product, Integer> entry : products.entrySet()) {
//                     Product product = entry.getKey();
//                     int value = entry.getValue();
    
//                     if (product.getName().equals(this.conditionalDiscounted)) {
//                         float discountedValue =  value * (1 - percentDiscount);
//                         products.put(product, discountedValue);
//                     }
//                 }

//                 return products;
//             }








//             else if(numericalComposition == -1){
                
//                 for (Map.Entry<Product, Integer> entry : products.entrySet()) {
//                     Product product = entry.getKey();
//                     int value = entry.getValue();
    
//                     if (product.getName().equals(this.conditionalDiscounted)) {
//                         float discountedValue =  value - percentDiscount;
//                         products.put(product, discountedValue);
//                     }
//                 }

//                 return products;
//             }



//         }

//         else if (level == 2){  //category


//             if(numericalComposition == 1){        //1 = Maximum 
                
//                 boolean isMax = true;
//                 int maximum = this.calculateOriginalPriceC(productDiscounts);
//                 for (Discount d : Discounts) {
//                     double nestedTotal = d.calculateOriginalPriceC(productDiscounts);
//                     if (nestedTotal > maxNestedTotal) {
//                         isMax = false;
//                     }
//                     else{
//                         d.alreadyUsed = true;
//                     }
//                 }



//                 if(isMax){
//                     this.alreadyUsed = true;

//                     for (Map.Entry<Product, Integer> entry : products.entrySet()) {
//                         Product product = entry.getKey();
//                         int value = entry.getValue();
    
//                         if (product.getCategory().equals(this.discounted)) {
//                             float discountedValue =  value - percentDiscount;
//                             products.put(product, discountedValue);
//                         }
//                     }
//                 }

//                 return products;
//             }






//             else if(numericalComposition == 2){     //2 = Multiplication 
//                 for (Map.Entry<Product, Integer> entry : products.entrySet()) {
//                     Product product = entry.getKey();
//                     int value = entry.getValue();
    
//                     if (product.getCategory().equals(this.discounted)) {
//                         float discountedValue =  value * (1 - percentDiscount);
//                         products.put(product, discountedValue);
//                     }
//                 }

//                 return products;
//             }








//             else if(numericalComposition == -1){
                
//                 for (Map.Entry<Product, Integer> entry : products.entrySet()) {
//                     Product product = entry.getKey();
//                     int value = entry.getValue();
    
//                     if (product.getName().equals(this.discounted)) {
//                         float discountedValue =  value - percentDiscount;
//                         products.put(product, discountedValue);
//                     }
//                 }

//                 return products;
//             }
//         }











//         else if (level == 3){  //store




//             if(numericalComposition == 1){        //1 = Maximum 
                
//                 boolean isMax = true;
//                 int maximum = this.calculateOriginalPriceS(productDiscounts);
//                 for (Discount d : Discounts) {
//                     double nestedTotal = d.calculateOriginalPriceS(productDiscounts);
//                     if (nestedTotal > maxNestedTotal) {
//                         isMax = false;
//                     }
//                     else{
//                         d.alreadyUsed = true;
//                     }
//                 }



//                 if(isMax){
//                     this.alreadyUsed = true;

//                     for (Map.Entry<Product, Integer> entry : products.entrySet()) {
//                         Product product = entry.getKey();
//                         int value = entry.getValue();
    
//                         if (true) {
//                             float discountedValue =  value - percentDiscount;
//                             products.put(product, discountedValue);
//                         }
//                     }
//                 }

//                 return products;
//             }






//             else if(numericalComposition == 2){     //2 = Multiplication 
//                 for (Map.Entry<Product, Integer> entry : products.entrySet()) {
//                     Product product = entry.getKey();
//                     int value = entry.getValue();
    
//                     if (true) {
//                         float discountedValue =  value * (1 - percentDiscount);
//                         products.put(product, discountedValue);
//                     }
//                 }

//                 return products;
//             }








//             else if(numericalComposition == -1){
                
//                 for (Map.Entry<Product, Integer> entry : products.entrySet()) {
//                     Product product = entry.getKey();
//                     int value = entry.getValue();
    
//                     if (true) {
//                         float discountedValue =  value - percentDiscount;
//                         products.put(product, discountedValue);
//                     }
//                 }

//                 return products;
//             }
//         }
//     }




//     private double calculateOriginalPriceP(Map<Product, Integer> products) {
//         double total = 0;
//         for (Map.Entry<Product, Integer> entry : products.entrySet()) {
//             if(entry.getKey().getName == discounted)
//                 total += entry.getKey().getPrice();
//         }
//         return total;
//     }


//     private double calculateOriginalPriceC(Map<Product, Integer> products) {
//         double total = 0;
//         for (Map.Entry<Product, Integer> entry : products.entrySet()) {
//             if(entry.getKey().geCategory == discounted)
//                 total += entry.getKey().getPrice();
//         }
//         return total;
//     }


//     private double calculateOriginalPriceS(Map<Product, Integer> products) {
//         double total = 0;
//         for (Map.Entry<Product, Integer> entry : products.entrySet()) {
//             total += entry.getKey().getPrice();
//         }
//         return total;
//     }






//     boolean checkConditinal(int originalPrice, List<products> products){
//         if(this.conditional = -1){
//             return true;
//         }
//         else if(this.conditional = 1){
//             return originalPrice >= limiter;
//         }
//         else if(this.conditional = 2){
//             int quantity = -1;
        
//             for (Product p : products) {
//                 if (p.getName().equals(this.conditionalDiscounted)) {
//                     quantity =  p.getQuantity();
//                 }
            

//             if(quantity == -1 || quantity >= limiter ){
//                 return true;
//             }
//             else{
//                 return false;
//             }
//             }

//         return false;     

//         }
//     }
// }