import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { Product } from '../../types';
import apiService from '../../services/api';

interface ProductState {
  products: Product[];
  currentProduct: Product | null;
  isLoading: boolean;
  error: string | null;
  searchResults: string[];
  categoryResults: string[];
}

const initialState: ProductState = {
  products: [],
  currentProduct: null,
  isLoading: false,
  error: null,
  searchResults: [],
  categoryResults: [],
};

// Async thunks
export const fetchProducts = createAsyncThunk(
  'products/fetchProducts',
  async (_, { rejectWithValue }) => {
    try {
      const products = await apiService.getAllProducts();
      return products;
    } catch (error: any) {
      return rejectWithValue(error.message || 'Failed to fetch products');
    }
  }
);

export const fetchProduct = createAsyncThunk(
  'products/fetchProduct',
  async (productId: string, { rejectWithValue }) => {
    try {
      const product = await apiService.getProduct(productId);
      return product;
    } catch (error: any) {
      return rejectWithValue(error.message || 'Failed to fetch product');
    }
  }
);

export const searchProducts = createAsyncThunk(
  'products/searchProducts',
  async (query: string, { rejectWithValue }) => {
    try {
      const productIds = await apiService.searchProducts(query);
      return productIds;
    } catch (error: any) {
      return rejectWithValue(error.message || 'Search failed');
    }
  }
);

export const fetchProductsByCategory = createAsyncThunk(
  'products/fetchProductsByCategory',
  async (category: string, { rejectWithValue }) => {
    try {
      const productIds = await apiService.getProductsByCategory(category);
      return productIds;
    } catch (error: any) {
      return rejectWithValue(error.message || 'Failed to fetch products by category');
    }
  }
);

export const rateProduct = createAsyncThunk(
  'products/rateProduct',
  async ({ productId, rating }: { productId: string; rating: number }, { rejectWithValue }) => {
    try {
      const success = await apiService.rateProduct(productId, rating);
      if (success) {
        return { productId, rating };
      }
      throw new Error('Failed to rate product');
    } catch (error: any) {
      return rejectWithValue(error.message || 'Failed to rate product');
    }
  }
);

const productSlice = createSlice({
  name: 'products',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    clearSearchResults: (state) => {
      state.searchResults = [];
    },
    clearCategoryResults: (state) => {
      state.categoryResults = [];
    },
    setCurrentProduct: (state, action: PayloadAction<Product | null>) => {
      state.currentProduct = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      // Fetch Products
      .addCase(fetchProducts.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchProducts.fulfilled, (state, action) => {
        state.isLoading = false;
        state.products = action.payload;
        state.error = null;
      })
      .addCase(fetchProducts.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      // Fetch Product
      .addCase(fetchProduct.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchProduct.fulfilled, (state, action) => {
        state.isLoading = false;
        state.currentProduct = action.payload;
        state.error = null;
      })
      .addCase(fetchProduct.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      // Search Products
      .addCase(searchProducts.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(searchProducts.fulfilled, (state, action) => {
        state.isLoading = false;
        state.searchResults = action.payload;
        state.error = null;
      })
      .addCase(searchProducts.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      // Fetch Products by Category
      .addCase(fetchProductsByCategory.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchProductsByCategory.fulfilled, (state, action) => {
        state.isLoading = false;
        state.categoryResults = action.payload;
        state.error = null;
      })
      .addCase(fetchProductsByCategory.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      // Rate Product
      .addCase(rateProduct.fulfilled, (state, action) => {
        const { productId, rating } = action.payload;
        const product = state.products.find(p => p.id === productId);
        if (product) {
          product.rating = rating;
        }
        if (state.currentProduct && state.currentProduct.id === productId) {
          state.currentProduct.rating = rating;
        }
      });
  },
});

export const { clearError, clearSearchResults, clearCategoryResults, setCurrentProduct } = productSlice.actions;
export default productSlice.reducer;