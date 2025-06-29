# Frontend2 - Mobile E-Commerce Application

A modern, cross-platform mobile application built with **Expo** and **React Native** that serves as a mobile companion to the existing Java Spring Boot backend. Frontend2 provides a beautiful, intuitive shopping experience for iOS, iPadOS, and Android platforms.

## 🚀 Features

### Core Functionality
- **Authentication System**: Secure login/register with JWT tokens
- **Store Management**: Browse, search, and explore stores
- **Product Catalog**: View products with search and category filtering
- **Shopping Cart**: Add/remove items with quantity management
- **Checkout Process**: Complete purchase flow with payment integration
- **Order History**: Track past orders and purchases
- **Rating System**: Rate stores and products
- **Real-time Updates**: Live data synchronization

### Settings & Customization
- **Beautiful Settings Screen**: Comprehensive settings with modern UI
- **Dark/Light Mode**: Toggle between themes
- **Font Size Options**: Small, medium, large text sizes
- **Notification Controls**: Enable/disable push notifications
- **Auto Sync**: Automatic data synchronization
- **Language Support**: Multi-language ready (English by default)
- **User Profile Management**: Account settings and privacy controls

### Design & UX
- **Modern UI**: Clean, intuitive interface following iOS/Android guidelines
- **Responsive Design**: Optimized for phones and tablets
- **Smooth Animations**: Fluid transitions and interactions
- **Accessibility**: Built-in accessibility support
- **Cross-Platform**: Consistent experience across iOS and Android

## 🛠 Technology Stack

- **Framework**: Expo SDK (latest stable)
- **Language**: React Native with TypeScript
- **State Management**: Redux Toolkit
- **Navigation**: React Navigation v6
- **HTTP Client**: Axios with interceptors
- **Storage**: Expo SecureStore for tokens
- **Icons**: Expo Vector Icons
- **Platforms**: iOS, iPadOS, Android

## 🏗 Architecture

### Project Structure
```
frontend2/
├── App.tsx                 # Main application entry point
├── src/
│   ├── components/         # Reusable UI components
│   │   ├── auth/          # Authentication screens
│   │   ├── main/          # Main tab screens
│   │   ├── detail/        # Detail screens
│   │   ├── checkout/      # Checkout flow
│   │   ├── orders/        # Order management
│   │   └── profile/       # User profile
│   ├── services/          # API services
│   │   └── api.ts         # Main API service
│   ├── store/             # Redux store
│   │   ├── index.ts       # Store configuration
│   │   └── slices/        # Redux slices
│   ├── navigation/        # Navigation configuration
│   ├── types/             # TypeScript type definitions
│   └── utils/             # Utility functions
├── assets/                # Images, icons, fonts
└── package.json           # Dependencies and scripts
```

### State Management
- **Auth Slice**: User authentication and token management
- **Store Slice**: Store data and search functionality
- **Product Slice**: Product catalog and filtering
- **Cart Slice**: Shopping cart state and operations
- **Settings Slice**: App preferences and configuration

## 🔌 Backend Integration

### API Endpoints
The app integrates with the following Spring Boot REST endpoints:

#### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `POST /api/auth/logout` - User logout
- `GET /api/auth/validate` - Token validation

#### Stores
- `GET /api/stores` - Get all stores
- `GET /api/stores/{id}` - Get store by ID
- `GET /api/stores/search` - Search stores
- `POST /api/stores/{id}/rate` - Rate a store

#### Products
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/search` - Search products
- `GET /api/products/category/{category}` - Get products by category
- `POST /api/products/{id}/rate` - Rate a product

#### Shopping Cart
- `POST /api/cart/add` - Add item to cart
- `POST /api/cart/remove` - Remove item from cart
- `GET /api/cart/total` - Get cart total
- `POST /api/cart/checkout` - Process checkout

#### Orders & User
- `GET /api/orders/history` - Get order history
- `GET /api/user/profile` - Get user profile

## 🚀 Getting Started

### Prerequisites
- Node.js (16 or later)
- npm or yarn
- Expo CLI
- iOS Simulator (for iOS development)
- Android Studio (for Android development)

### Installation

1. **Clone the repository**
   ```bash
   cd frontend2
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Configure the backend URL**
   Edit `src/services/api.ts` and update the `BASE_URL`:
   ```typescript
   const BASE_URL = 'http://your-backend-url:8080/api';
   ```

4. **Start the development server**
   ```bash
   npm start
   ```

### Development Commands

```bash
# Start Expo development server
npm start

# Run on iOS simulator
npm run ios

# Run on Android emulator
npm run android

# Run on web
npm run web

# TypeScript type checking
npx tsc --noEmit

# Clear cache
npm start -- --clear
```

## 📱 Screens Overview

### Authentication Flow
- **Login Screen**: Secure user authentication with validation
- **Register Screen**: New user account creation

### Main Navigation (Bottom Tabs)
- **Home**: Dashboard with quick actions and statistics
- **Stores**: Browse and search all available stores
- **Products**: Product catalog with filtering and search
- **Cart**: Shopping cart management and checkout
- **Settings**: Comprehensive app settings and preferences

### Detail Screens
- **Store Detail**: Detailed store information and products
- **Product Detail**: Product information with add to cart
- **Checkout**: Payment and shipping information
- **Order History**: Past orders and tracking
- **Profile**: User account management

## 🎨 Design System

### Color Palette
- **Primary**: #007AFF (iOS Blue)
- **Success**: #34C759 (Green)
- **Warning**: #FF9500 (Orange)
- **Error**: #FF3B30 (Red)
- **Purple**: #AF52DE

### Typography
- **Title**: 24-28px, Bold
- **Heading**: 18-20px, Semi-bold
- **Body**: 16px, Regular
- **Caption**: 12-14px, Regular

### Spacing
- **Small**: 8px
- **Medium**: 16px
- **Large**: 24px
- **XLarge**: 32px

## 🔧 Configuration

### App Configuration (`app.json`)
- **Name**: Frontend2
- **Bundle ID**: com.frontend2.app
- **Icons**: Adaptive icons for all platforms
- **Splash Screen**: Custom splash screen
- **Permissions**: Camera, notifications, etc.

### Environment Variables
Create a `.env` file for environment-specific configuration:
```
API_BASE_URL=http://localhost:8080/api
APP_VERSION=1.0.0
```

## 📦 Build & Deployment

### Development Build
```bash
npx expo build:ios
npx expo build:android
```

### Production Build
```bash
# Configure EAS Build
npm install -g @expo/cli
expo build:configure

# Build for app stores
expo build:ios --type app-store
expo build:android --type app-bundle
```

### TestFlight Integration
```bash
# Upload to TestFlight
npx testflight
```

## 🧪 Testing

### Unit Tests
```bash
npm test
```

### E2E Testing
```bash
# Setup Detox or similar
npm run e2e:ios
npm run e2e:android
```

## 📈 Performance Optimization

### Bundle Optimization
- Tree shaking enabled
- Code splitting for screens
- Image optimization
- Lazy loading for heavy components

### Memory Management
- Proper cleanup of subscriptions
- Image caching and cleanup
- Redux state normalization

## 🔒 Security

### Token Management
- Secure token storage with Expo SecureStore
- Automatic token refresh
- Proper logout and cleanup

### API Security
- Request/response interceptors
- Error handling and sanitization
- Input validation

## 🌐 Internationalization

The app is prepared for multiple languages:
- English (default)
- Extensible for additional languages
- RTL support ready

## 🚀 Future Enhancements

### Planned Features
- [ ] Push notifications
- [ ] Offline support
- [ ] Biometric authentication
- [ ] Advanced search filters
- [ ] Social sharing
- [ ] Wishlist functionality
- [ ] Store recommendations
- [ ] Product reviews and comments
- [ ] Live chat support
- [ ] Barcode scanning

### Performance Improvements
- [ ] Infinite scrolling for product lists
- [ ] Image lazy loading and caching
- [ ] Background sync
- [ ] App state persistence

## 📞 Support

For support and questions:
- Check the documentation
- Create an issue in the repository
- Contact the development team

## 📄 License

This project is proprietary software. All rights reserved.

---

**Frontend2** - A modern mobile e-commerce experience 📱✨