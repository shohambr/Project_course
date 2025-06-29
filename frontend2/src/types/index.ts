// API Response Types
export interface ApiResponse<T = any> {
  message: string | null;
  success: boolean;
  payload: T[] | null;
}

// Auth Types
export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  username: string;
}

export interface User {
  username: string;
  token: string;
}

// Store Types
export interface Store {
  id: string;
  name: string;
  founder: string;
  openNow: boolean;
  rating: number;
  products: { [productId: string]: number };
  owners: string[];
  managers: { [managerId: string]: ManagerPermissions };
}

export interface ManagerPermissions {
  manageInventory: boolean;
  manageStaff: boolean;
  viewStore: boolean;
  updatePolicy: boolean;
  addProduct: boolean;
  removeProduct: boolean;
  updateProduct: boolean;
}

// Product Types
export interface Product {
  id: string;
  name: string;
  description: string;
  price: number;
  category: string;
  rating: number;
  storeId: string;
}

// Cart Types
export interface CartItem {
  productId: string;
  storeId: string;
  quantity: number;
  product?: Product;
}

export interface ShoppingCart {
  items: CartItem[];
  total: number;
}

// Order Types
export interface Order {
  id: string;
  userId: string;
  storeId: string;
  items: CartItem[];
  total: number;
  status: 'pending' | 'confirmed' | 'shipped' | 'delivered';
  createdAt: string;
}

// Navigation Types
export type RootStackParamList = {
  Auth: undefined;
  Main: undefined;
  Login: undefined;
  Register: undefined;
  Home: undefined;
  Stores: undefined;
  Products: undefined;
  Cart: undefined;
  Settings: undefined;
  Profile: undefined;
  StoreDetail: { storeId: string };
  ProductDetail: { productId: string };
  Checkout: undefined;
  OrderHistory: undefined;
};

export type BottomTabParamList = {
  Home: undefined;
  Stores: undefined;
  Products: undefined;
  Cart: undefined;
  Settings: undefined;
};

// Settings Types
export interface AppSettings {
  theme: 'light' | 'dark';
  language: string;
  notifications: boolean;
}

// Error Types
export interface ApiError {
  message: string;
  code?: number;
}