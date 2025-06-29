import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { CartItem, ShoppingCart } from '../../types';
import apiService from '../../services/api';

interface CartState {
  items: CartItem[];
  total: number;
  isLoading: boolean;
  error: string | null;
  checkoutLoading: boolean;
}

const initialState: CartState = {
  items: [],
  total: 0,
  isLoading: false,
  error: null,
  checkoutLoading: false,
};

// Async thunks
export const addToCart = createAsyncThunk(
  'cart/addToCart',
  async ({ storeId, productId, quantity }: { storeId: string; productId: string; quantity: number }, { rejectWithValue }) => {
    try {
      const success = await apiService.addToCart(storeId, productId, quantity);
      if (success) {
        return { storeId, productId, quantity };
      }
      throw new Error('Failed to add to cart');
    } catch (error: any) {
      return rejectWithValue(error.message || 'Failed to add to cart');
    }
  }
);

export const removeFromCart = createAsyncThunk(
  'cart/removeFromCart',
  async ({ storeId, productId, quantity }: { storeId: string; productId: string; quantity: number }, { rejectWithValue }) => {
    try {
      const success = await apiService.removeFromCart(storeId, productId, quantity);
      if (success) {
        return { storeId, productId, quantity };
      }
      throw new Error('Failed to remove from cart');
    } catch (error: any) {
      return rejectWithValue(error.message || 'Failed to remove from cart');
    }
  }
);

export const fetchCartTotal = createAsyncThunk(
  'cart/fetchCartTotal',
  async (_, { rejectWithValue }) => {
    try {
      const total = await apiService.getCartTotal();
      return total;
    } catch (error: any) {
      return rejectWithValue(error.message || 'Failed to fetch cart total');
    }
  }
);

export const checkout = createAsyncThunk(
  'cart/checkout',
  async (checkoutData: {
    paymentService: string;
    creditCardNumber: string;
    expirationDate: string;
    backNumber: string;
    state: string;
    city: string;
    street: string;
    homeNumber: string;
  }, { rejectWithValue }) => {
    try {
      const success = await apiService.checkout(checkoutData);
      if (success) {
        return true;
      }
      throw new Error('Checkout failed');
    } catch (error: any) {
      return rejectWithValue(error.message || 'Checkout failed');
    }
  }
);

const cartSlice = createSlice({
  name: 'cart',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    clearCart: (state) => {
      state.items = [];
      state.total = 0;
    },
    updateCartItem: (state, action: PayloadAction<CartItem>) => {
      const existingItem = state.items.find(
        item => item.productId === action.payload.productId && item.storeId === action.payload.storeId
      );
      
      if (existingItem) {
        existingItem.quantity = action.payload.quantity;
      } else {
        state.items.push(action.payload);
      }
    },
    removeCartItem: (state, action: PayloadAction<{ productId: string; storeId: string }>) => {
      state.items = state.items.filter(
        item => !(item.productId === action.payload.productId && item.storeId === action.payload.storeId)
      );
    },
  },
  extraReducers: (builder) => {
    builder
      // Add to Cart
      .addCase(addToCart.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(addToCart.fulfilled, (state, action) => {
        state.isLoading = false;
        const { storeId, productId, quantity } = action.payload;
        const existingItem = state.items.find(
          item => item.productId === productId && item.storeId === storeId
        );
        
        if (existingItem) {
          existingItem.quantity += quantity;
        } else {
          state.items.push({ productId, storeId, quantity });
        }
        state.error = null;
      })
      .addCase(addToCart.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      // Remove from Cart
      .addCase(removeFromCart.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(removeFromCart.fulfilled, (state, action) => {
        state.isLoading = false;
        const { storeId, productId, quantity } = action.payload;
        const existingItem = state.items.find(
          item => item.productId === productId && item.storeId === storeId
        );
        
        if (existingItem) {
          existingItem.quantity -= quantity;
          if (existingItem.quantity <= 0) {
            state.items = state.items.filter(
              item => !(item.productId === productId && item.storeId === storeId)
            );
          }
        }
        state.error = null;
      })
      .addCase(removeFromCart.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      // Fetch Cart Total
      .addCase(fetchCartTotal.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchCartTotal.fulfilled, (state, action) => {
        state.isLoading = false;
        state.total = action.payload;
        state.error = null;
      })
      .addCase(fetchCartTotal.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      // Checkout
      .addCase(checkout.pending, (state) => {
        state.checkoutLoading = true;
        state.error = null;
      })
      .addCase(checkout.fulfilled, (state) => {
        state.checkoutLoading = false;
        state.items = [];
        state.total = 0;
        state.error = null;
      })
      .addCase(checkout.rejected, (state, action) => {
        state.checkoutLoading = false;
        state.error = action.payload as string;
      });
  },
});

export const { clearError, clearCart, updateCartItem, removeCartItem } = cartSlice.actions;
export default cartSlice.reducer;