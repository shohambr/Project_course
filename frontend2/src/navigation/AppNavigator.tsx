import React, { useEffect } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { useSelector, useDispatch } from 'react-redux';
import { Ionicons } from '@expo/vector-icons';

import { RootState, AppDispatch } from '../store';
import { validateToken } from '../store/slices/authSlice';

// Auth Screens
import LoginScreen from '../screens/auth/LoginScreen';
import RegisterScreen from '../screens/auth/RegisterScreen';

// Main Screens
import HomeScreen from '../screens/main/HomeScreen';
import StoresScreen from '../screens/main/StoresScreen';
import ProductsScreen from '../screens/main/ProductsScreen';
import CartScreen from '../screens/main/CartScreen';
import SettingsScreen from '../screens/main/SettingsScreen';

// Detail Screens
import StoreDetailScreen from '../screens/detail/StoreDetailScreen';
import ProductDetailScreen from '../screens/detail/ProductDetailScreen';
import CheckoutScreen from '../screens/checkout/CheckoutScreen';
import OrderHistoryScreen from '../screens/orders/OrderHistoryScreen';
import ProfileScreen from '../screens/profile/ProfileScreen';

import { RootStackParamList, BottomTabParamList } from '../types';

const Stack = createStackNavigator<RootStackParamList>();
const Tab = createBottomTabNavigator<BottomTabParamList>();

const TabNavigator = () => {
  const theme = useSelector((state: RootState) => state.settings.theme);
  
  return (
    <Tab.Navigator
      screenOptions={({ route }) => ({
        tabBarIcon: ({ focused, color, size }) => {
          let iconName: keyof typeof Ionicons.glyphMap;

          switch (route.name) {
            case 'Home':
              iconName = focused ? 'home' : 'home-outline';
              break;
            case 'Stores':
              iconName = focused ? 'storefront' : 'storefront-outline';
              break;
            case 'Products':
              iconName = focused ? 'grid' : 'grid-outline';
              break;
            case 'Cart':
              iconName = focused ? 'bag' : 'bag-outline';
              break;
            case 'Settings':
              iconName = focused ? 'settings' : 'settings-outline';
              break;
            default:
              iconName = 'home-outline';
          }

          return <Ionicons name={iconName} size={size} color={color} />;
        },
        tabBarActiveTintColor: '#007AFF',
        tabBarInactiveTintColor: 'gray',
        tabBarStyle: {
          backgroundColor: theme === 'dark' ? '#1a1a1a' : '#ffffff',
        },
        headerStyle: {
          backgroundColor: theme === 'dark' ? '#1a1a1a' : '#ffffff',
        },
        headerTitleStyle: {
          color: theme === 'dark' ? '#ffffff' : '#000000',
        },
      })}
    >
      <Tab.Screen name="Home" component={HomeScreen} />
      <Tab.Screen name="Stores" component={StoresScreen} />
      <Tab.Screen name="Products" component={ProductsScreen} />
      <Tab.Screen name="Cart" component={CartScreen} />
      <Tab.Screen name="Settings" component={SettingsScreen} />
    </Tab.Navigator>
  );
};

const AuthNavigator = () => (
  <Stack.Navigator screenOptions={{ headerShown: false }}>
    <Stack.Screen name="Login" component={LoginScreen} />
    <Stack.Screen name="Register" component={RegisterScreen} />
  </Stack.Navigator>
);

const MainNavigator = () => (
  <Stack.Navigator>
    <Stack.Screen 
      name="Main" 
      component={TabNavigator} 
      options={{ headerShown: false }}
    />
    <Stack.Screen 
      name="StoreDetail" 
      component={StoreDetailScreen} 
      options={{ title: 'Store Details' }}
    />
    <Stack.Screen 
      name="ProductDetail" 
      component={ProductDetailScreen} 
      options={{ title: 'Product Details' }}
    />
    <Stack.Screen 
      name="Checkout" 
      component={CheckoutScreen} 
      options={{ title: 'Checkout' }}
    />
    <Stack.Screen 
      name="OrderHistory" 
      component={OrderHistoryScreen} 
      options={{ title: 'Order History' }}
    />
    <Stack.Screen 
      name="Profile" 
      component={ProfileScreen} 
      options={{ title: 'Profile' }}
    />
  </Stack.Navigator>
);

const AppNavigator = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { isAuthenticated, isLoading } = useSelector((state: RootState) => state.auth);

  useEffect(() => {
    // Check if user has a valid token on app start
    dispatch(validateToken());
  }, [dispatch]);

  if (isLoading) {
    // You can replace this with a proper loading screen component
    return null;
  }

  return (
    <NavigationContainer>
      {isAuthenticated ? <MainNavigator /> : <AuthNavigator />}
    </NavigationContainer>
  );
};

export default AppNavigator;