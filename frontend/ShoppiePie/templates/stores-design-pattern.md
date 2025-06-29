# 🏪 Stores Screen Design Pattern

**Inspired by MoodPie's beautiful Panel and Card components**

## 🎨 Design Pattern

### Layout Structure
```
📱 Screen
├── 📄 Header Panel (rounded, padded)
│   ├── 🎯 Title: "Discover Amazing Stores"
│   └── 📝 Subtitle: "Find perfect products..."
└── 📋 Store List
    ├── 🏪 Store Card 1 (rounded, shadowed)
    │   ├── 🖼️ Store Image (rounded corners)
    │   ├── 📊 Store Info
    │   │   ├── 🏷️ Store Name (bold)
    │   │   ├── 📝 Description (secondary text)
    │   │   ├── ⭐ Rating + Product Count
    │   │   └── 🏷️ Category Badge
    │   └── 👆 Touch Handler
    ├── 🏪 Store Card 2...
    └── 🏪 Store Card 3...
```

## 🎯 MoodPie Pattern Usage

### Panel Component Pattern
```javascript
// Using MoodPie's Panel styling approach
headerPanel: {
  marginBottom: 24,
  padding: 20,
  backgroundColor: colors.surface, // #F2F2F7
  borderRadius: 12,
  shadowColor: colors.text,
  shadowOffset: { width: 0, height: 2 },
  shadowOpacity: 0.1,
  shadowRadius: 8,
  elevation: 4,
}
```

### Card Component Pattern
```javascript
// Store cards using MoodPie's card styling
storeCard: {
  marginBottom: 16,
  backgroundColor: colors.surface,
  borderRadius: 12,
  shadowColor: colors.text,
  shadowOffset: { width: 0, height: 2 },
  shadowOpacity: 0.1,
  shadowRadius: 8,
  elevation: 4,
}
```

### Image Container Pattern
```javascript
// Rounded image containers like MoodPie
storeImageContainer: {
  width: 80,
  height: 80,
  borderRadius: 12,
  overflow: 'hidden',
  marginRight: rtl.isRTL ? 0 : 16,
  marginLeft: rtl.isRTL ? 16 : 0,
}
```

### Typography Pattern
```javascript
// MoodPie's typography hierarchy
storeName: {
  fontSize: 18,
  fontWeight: 'bold',
  color: colors.text,
  marginBottom: 8,
  textAlign: rtl.isRTL ? 'right' : 'left',
}

storeDescription: {
  fontSize: 14,
  color: colors.textSecondary,
  marginBottom: 12,
  lineHeight: 20,
}
```

### Badge Pattern
```javascript
// Category badges using MoodPie's pill styling
categoryBadge: {
  alignSelf: rtl.isRTL ? 'flex-end' : 'flex-start',
  paddingHorizontal: 8,
  paddingVertical: 4,
  borderRadius: 8,
  backgroundColor: colors.primary + '20',
}
```

## 📊 Data Structure

### Store Interface
```typescript
interface Store {
  id: string;
  name: string;
  description: string;
  rating: number;
  image: string;          // Unsplash professional images
  category: string;
  productsCount: number;
  bannerImage?: string;   // Optional store banner
  logoImage?: string;     // Optional store logo
}
```

### Sample Data
```javascript
const stores = [
  {
    id: '1',
    name: 'TechHub Pro',
    description: 'Premium electronics and cutting-edge technology',
    rating: 4.8,
    image: 'https://images.unsplash.com/photo-1531482615713-2afd69097998?w=200',
    category: 'Electronics',
    productsCount: 156,
  },
  // ... more stores
];
```

## 🎨 Color Palette (MoodPie-inspired)

```javascript
const colors = {
  primary: '#007AFF',      // iOS blue
  background: '#FFFFFF',   // Clean white
  surface: '#F2F2F7',     // Light gray panels
  text: '#000000',        // Primary text
  textSecondary: '#8E8E93', // Secondary text
  border: '#C6C6C8',      // Subtle borders
  success: '#34C759',     // Success green
  error: '#FF3B30',       // Error red
};
```

## ⚡ Animations (MoodPie-style)

### Loading Animation
```javascript
// Fade in animation for store cards
entering={FadeInUp.duration(600).delay(index * 100).springify()}
```

### Touch Feedback
```javascript
// Smooth touch feedback
<TouchableOpacity activeOpacity={0.8}>
```

## 📱 Responsive Design

### Mobile vs Tablet
```javascript
const isMobile = Platform.OS !== 'web' && screenWidth < 768;

// Adjust spacing based on device
padding: isMobile ? 16 : 24,
fontSize: isMobile ? 18 : 20,
```

## 🌍 RTL Support (MoodPie pattern)

```javascript
// RTL-aware spacing
marginRight: rtl.isRTL ? 0 : 16,
marginLeft: rtl.isRTL ? 16 : 0,
textAlign: rtl.isRTL ? 'right' : 'left',
```

---

**🎯 This pattern creates store screens that feel like natural extensions of MoodPie's design system!**