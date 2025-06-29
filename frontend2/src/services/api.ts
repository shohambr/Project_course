import axios, { AxiosInstance, AxiosResponse } from 'axios';
import * as SecureStore from 'expo-secure-store';
import { 
  ApiResponse, 
  LoginRequest, 
  RegisterRequest, 
  AuthResponse, 
  Store, 
  Product, 
  CartItem,
  Order 
} from '../types';

// Configure base URL - update this to your backend URL
const BASE_URL = 'http://localhost:8080/api';

class ApiService {
  private axios: AxiosInstance;

  constructor() {
    this.axios = axios.create({
      baseURL: BASE_URL,
      timeout: 10000,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Add request interceptor to include auth token
    this.axios.interceptors.request.use(async (config) => {
      const token = await this.getAuthToken();
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    });

    // Add response interceptor for error handling
    this.axios.interceptors.response.use(
      (response) => response,
      (error) => {
        console.error('API Error:', error);
        return Promise.reject(error);
      }
    );
  }

  // Auth Token Management
  private async getAuthToken(): Promise<string | null> {
    try {
      return await SecureStore.getItemAsync('authToken');
    } catch (error) {
      console.error('Error getting auth token:', error);
      return null;
    }
  }

  async setAuthToken(token: string): Promise<void> {
    try {
      await SecureStore.setItemAsync('authToken', token);
    } catch (error) {
      console.error('Error setting auth token:', error);
    }
  }

  async removeAuthToken(): Promise<void> {
    try {
      await SecureStore.deleteItemAsync('authToken');
    } catch (error) {
      console.error('Error removing auth token:', error);
    }
  }

  // Auth API
  async login(credentials: LoginRequest): Promise<AuthResponse> {
    const response: AxiosResponse<ApiResponse<AuthResponse>> = await this.axios.post('/auth/login', credentials);
    if (response.data.success && response.data.payload) {
      const authData = response.data.payload[0];
      await this.setAuthToken(authData.token);
      return authData;
    }
    throw new Error(response.data.message || 'Login failed');
  }

  async register(userData: RegisterRequest): Promise<string> {
    const response: AxiosResponse<ApiResponse<string>> = await this.axios.post('/auth/register', userData);
    if (response.data.success) {
      return response.data.message || 'Registration successful';
    }
    throw new Error(response.data.message || 'Registration failed');
  }

  async logout(): Promise<void> {
    try {
      await this.axios.post('/auth/logout');
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      await this.removeAuthToken();
    }
  }

  async validateToken(): Promise<boolean> {
    try {
      const response = await this.axios.get('/auth/validate');
      return response.data.success;
    } catch (error) {
      return false;
    }
  }

  // Store API
  async getAllStores(): Promise<Store[]> {
    const response: AxiosResponse<ApiResponse<Store>> = await this.axios.get('/stores');
    if (response.data.success && response.data.payload) {
      return response.data.payload;
    }
    return [];
  }

  async getStore(storeId: string): Promise<Store | null> {
    try {
      const response: AxiosResponse<ApiResponse<Store>> = await this.axios.get(`/stores/${storeId}`);
      if (response.data.success && response.data.payload) {
        return response.data.payload[0];
      }
      return null;
    } catch (error) {
      console.error('Error getting store:', error);
      return null;
    }
  }

  async searchStores(query: string): Promise<Store[]> {
    const response: AxiosResponse<ApiResponse<Store>> = await this.axios.get(`/stores/search?query=${query}`);
    if (response.data.success && response.data.payload) {
      return response.data.payload;
    }
    return [];
  }

  async rateStore(storeId: string, rating: number): Promise<boolean> {
    try {
      const response = await this.axios.post(`/stores/${storeId}/rate`, { rating });
      return response.data.success;
    } catch (error) {
      console.error('Error rating store:', error);
      return false;
    }
  }

  // Product API
  async getAllProducts(): Promise<Product[]> {
    const response: AxiosResponse<ApiResponse<Product>> = await this.axios.get('/products');
    if (response.data.success && response.data.payload) {
      return response.data.payload;
    }
    return [];
  }

  async getProduct(productId: string): Promise<Product | null> {
    try {
      const response: AxiosResponse<ApiResponse<Product>> = await this.axios.get(`/products/${productId}`);
      if (response.data.success && response.data.payload) {
        return response.data.payload[0];
      }
      return null;
    } catch (error) {
      console.error('Error getting product:', error);
      return null;
    }
  }

  async searchProducts(query: string): Promise<string[]> {
    const response: AxiosResponse<ApiResponse<string>> = await this.axios.get(`/products/search?query=${query}`);
    if (response.data.success && response.data.payload) {
      return response.data.payload;
    }
    return [];
  }

  async getProductsByCategory(category: string): Promise<string[]> {
    const response: AxiosResponse<ApiResponse<string>> = await this.axios.get(`/products/category/${category}`);
    if (response.data.success && response.data.payload) {
      return response.data.payload;
    }
    return [];
  }

  async rateProduct(productId: string, rating: number): Promise<boolean> {
    try {
      const response = await this.axios.post(`/products/${productId}/rate`, { rating });
      return response.data.success;
    } catch (error) {
      console.error('Error rating product:', error);
      return false;
    }
  }

  // Cart API
  async addToCart(storeId: string, productId: string, quantity: number): Promise<boolean> {
    try {
      const response = await this.axios.post('/cart/add', { storeId, productId, quantity });
      return response.data.success;
    } catch (error) {
      console.error('Error adding to cart:', error);
      return false;
    }
  }

  async removeFromCart(storeId: string, productId: string, quantity: number): Promise<boolean> {
    try {
      const response = await this.axios.post('/cart/remove', { storeId, productId, quantity });
      return response.data.success;
    } catch (error) {
      console.error('Error removing from cart:', error);
      return false;
    }
  }

  async getCartTotal(): Promise<number> {
    try {
      const response: AxiosResponse<ApiResponse<number>> = await this.axios.get('/cart/total');
      if (response.data.success && response.data.payload) {
        return response.data.payload[0];
      }
      return 0;
    } catch (error) {
      console.error('Error getting cart total:', error);
      return 0;
    }
  }

  async checkout(checkoutData: {
    paymentService: string;
    creditCardNumber: string;
    expirationDate: string;
    backNumber: string;
    state: string;
    city: string;
    street: string;
    homeNumber: string;
  }): Promise<boolean> {
    try {
      const response = await this.axios.post('/cart/checkout', checkoutData);
      return response.data.success;
    } catch (error) {
      console.error('Error during checkout:', error);
      return false;
    }
  }

  // Order API
  async getOrderHistory(): Promise<string[]> {
    const response: AxiosResponse<ApiResponse<string>> = await this.axios.get('/orders/history');
    if (response.data.success && response.data.payload) {
      return response.data.payload;
    }
    return [];
  }

  // User API
  async getUserProfile(): Promise<{ username: string } | null> {
    try {
      const response: AxiosResponse<ApiResponse<{ username: string }>> = await this.axios.get('/user/profile');
      if (response.data.success && response.data.payload) {
        return response.data.payload[0];
      }
      return null;
    } catch (error) {
      console.error('Error getting user profile:', error);
      return null;
    }
  }
}

export const apiService = new ApiService();
export default apiService;