# 📊 Analytics Chart Design Pattern

**Using MoodPie's stunning PieChart and KeyChart components for e-commerce data**

## 🎯 Chart Transformation

### From MoodPie's Emotion Tracking → E-Commerce Analytics

```javascript
// BEFORE: MoodPie emotion categories
const emotions = [
  { id: 'happy', name: 'Happy', color: '#FF6B6B', icon: '😊' },
  { id: 'calm', name: 'Calm', color: '#4ECDC4', icon: '😌' },
  // ...
];

// AFTER: E-commerce product categories  
const productCategories = [
  { id: 'electronics', name: 'Electronics', color: '#FF6B6B', icon: '📱', totalProducts: 2847 },
  { id: 'fashion', name: 'Fashion', color: '#4ECDC4', icon: '👗', totalProducts: 1923 },
  // ...
];
```

## 🎨 Dual Mode Analytics (Market vs Personal)

### Toggle Between Views
```javascript
// Market Analytics Mode
const marketData = productCategories.map(cat => ({
  emotion: {
    id: cat.id,
    name: cat.name,
    color: cat.color,
    icon: cat.icon,
  },
  percentage: (cat.totalProducts / totalMarketProducts) * 100,
  count: cat.totalProducts,
}));

// Personal Purchase Analytics Mode  
const personalData = productCategories.map(cat => ({
  emotion: {
    id: cat.id,
    name: cat.name,
    color: cat.color,
    icon: cat.icon,
  },
  percentage: (cat.userPurchases / totalUserPurchases) * 100,
  count: cat.userPurchases,
}));
```

### Beautiful Toggle Animation (MoodPie-style)
```javascript
const handleToggleView = () => {
  const safeToggle = () => {
    setViewMode(prev => prev === 'market' ? 'purchases' : 'market');
    setCategoryData(processedCategoryData);
  };

  // Toggle animation with spring effect (same as MoodPie)
  chartScale.value = withSequence(
    withSpring(0.8),
    withSpring(1.05),
    withSpring(1)
  );

  chartRotation.value = withSequence(
    withTiming(180, { duration: 300 }, (finished) => finished && runOnJS(safeToggle)()),
    withTiming(360, { duration: 300 }),
    withTiming(0, { duration: 200, easing: Easing.ease })
  );
};
```

## 📊 Chart Data Structure

### Category Analytics Interface
```typescript
interface ProductCategory {
  id: string;
  name: string;
  color: string;
  icon: string;
  totalProducts: number;    // For market analytics
  userPurchases: number;    // For purchase analytics
}

interface ChartDataPoint {
  emotion: {              // Reusing MoodPie's emotion interface
    id: string;
    name: string;
    color: string;
    icon: string;
  };
  percentage: number;
  count: number;
}
```

## 🎨 Visual Components

### Analytics Header (MoodPie Panel style)
```javascript
// Beautiful header with dynamic content
analyticsHeader: {
  alignItems: 'center',
  marginBottom: isMobile ? 16 : 24,
  paddingHorizontal: isMobile ? 16 : 0,
}

analyticsTitle: {
  fontSize: isMobile ? 18 : 22,
  fontWeight: 'bold',
  textAlign: 'center',
  marginBottom: 8,
  color: colors.text,
}

analyticsSubtitle: {
  fontSize: isMobile ? 14 : 16,
  textAlign: 'center',
  color: colors.textSecondary,
}
```

### Dynamic Title System
```javascript
const currentTitle = viewMode === 'market' 
  ? '📊 Market Analytics'
  : '🛒 My Purchase Analytics';

const description = viewMode === 'market'
  ? 'Product Distribution in Market'
  : 'Your Purchase History by Category';

const subtitle = viewMode === 'market'
  ? `Total Products: ${totalMarketProducts.toLocaleString()}`
  : `Total Purchases: ${totalUserPurchases.toLocaleString()}`;
```

## 📈 Realistic Data Distribution

### Market Analytics (Total: 13,940 products)
```javascript
const marketDistribution = [
  { category: 'Sports & Outdoors', products: 3102, percentage: 22.3, color: '#96CEB4' },
  { category: 'Electronics', products: 2847, percentage: 20.4, color: '#FF6B6B' },
  { category: 'Books & Media', products: 2145, percentage: 15.4, color: '#DDA0DD' },
  { category: 'Fashion', products: 1923, percentage: 13.8, color: '#4ECDC4' },
  { category: 'Automotive', products: 1834, percentage: 13.2, color: '#F7DC6F' },
  { category: 'Home & Garden', products: 1456, percentage: 10.4, color: '#45B7D1' },
  { category: 'Beauty & Health', products: 892, percentage: 6.4, color: '#FFEAA7' },
  { category: 'Toys & Games', products: 743, percentage: 5.3, color: '#98D8C8' },
];
```

### Personal Analytics (Total: 56 purchases)
```javascript
const personalDistribution = [
  { category: 'Fashion', purchases: 15, percentage: 26.8, color: '#4ECDC4' },
  { category: 'Sports & Outdoors', purchases: 12, percentage: 21.4, color: '#96CEB4' },
  { category: 'Electronics', purchases: 8, percentage: 14.3, color: '#FF6B6B' },
  { category: 'Books & Media', purchases: 7, percentage: 12.5, color: '#DDA0DD' },
  { category: 'Home & Garden', purchases: 5, percentage: 8.9, color: '#45B7D1' },
  { category: 'Toys & Games', purchases: 4, percentage: 7.1, color: '#98D8C8' },
  { category: 'Beauty & Health', purchases: 3, percentage: 5.4, color: '#FFEAA7' },
  { category: 'Automotive', purchases: 2, percentage: 3.6, color: '#F7DC6F' },
];
```

## 🎯 Data Insights

### Market vs Personal Comparison
```javascript
// Interesting insights from the data:
// - Fashion: 13.8% of market, but 26.8% of your purchases (you love fashion!)
// - Sports: 22.3% of market, 21.4% of your purchases (aligned with market)
// - Automotive: 13.2% of market, but only 3.6% of your purchases (opportunity!)
```

### Beautiful Storytelling
```javascript
const getInsight = (category, marketPercent, personalPercent) => {
  const difference = personalPercent - marketPercent;
  if (difference > 5) {
    return `You love ${category} more than average! ❤️`;
  } else if (difference < -5) {
    return `${category} might be worth exploring! 🔍`;
  } else {
    return `You're aligned with market trends in ${category} 📊`;
  }
};
```

## ⚡ Animation Patterns (MoodPie-inherited)

### Chart Entrance Animation
```javascript
// Same beautiful animations as MoodPie
<Animated.View entering={FadeInUp.duration(600).delay(200).springify()}>
  <PieChart
    emotions={processedCategoryData.map(item => item.emotion)}
    size={pieSize}
    onPercentageChange={handleDataChange}
  />
</Animated.View>
```

### Toggle Button Animation  
```javascript
// Reusing MoodPie's button animation system
<Button
  title="Toggle View"
  onPress={handleToggleView}
  style={styles.toggleButton}
  disabled={isAnimating}
/>
```

---

**🎯 This pattern transforms MoodPie's emotion tracking into powerful e-commerce analytics while maintaining the same beautiful UX!** 📊✨🥧