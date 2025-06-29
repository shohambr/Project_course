import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { Store } from '../../types';
import apiService from '../../services/api';

interface StoreState {
  stores: Store[];
  currentStore: Store | null;
  isLoading: boolean;
  error: string | null;
  searchResults: Store[];
}

const initialState: StoreState = {
  stores: [],
  currentStore: null,
  isLoading: false,
  error: null,
  searchResults: [],
};

// Async thunks
export const fetchStores = createAsyncThunk(
  'stores/fetchStores',
  async (_, { rejectWithValue }) => {
    try {
      const stores = await apiService.getAllStores();
      return stores;
    } catch (error: any) {
      return rejectWithValue(error.message || 'Failed to fetch stores');
    }
  }
);

export const fetchStore = createAsyncThunk(
  'stores/fetchStore',
  async (storeId: string, { rejectWithValue }) => {
    try {
      const store = await apiService.getStore(storeId);
      return store;
    } catch (error: any) {
      return rejectWithValue(error.message || 'Failed to fetch store');
    }
  }
);

export const searchStores = createAsyncThunk(
  'stores/searchStores',
  async (query: string, { rejectWithValue }) => {
    try {
      const stores = await apiService.searchStores(query);
      return stores;
    } catch (error: any) {
      return rejectWithValue(error.message || 'Search failed');
    }
  }
);

export const rateStore = createAsyncThunk(
  'stores/rateStore',
  async ({ storeId, rating }: { storeId: string; rating: number }, { rejectWithValue }) => {
    try {
      const success = await apiService.rateStore(storeId, rating);
      if (success) {
        return { storeId, rating };
      }
      throw new Error('Failed to rate store');
    } catch (error: any) {
      return rejectWithValue(error.message || 'Failed to rate store');
    }
  }
);

const storeSlice = createSlice({
  name: 'stores',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    clearSearchResults: (state) => {
      state.searchResults = [];
    },
    setCurrentStore: (state, action: PayloadAction<Store | null>) => {
      state.currentStore = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      // Fetch Stores
      .addCase(fetchStores.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchStores.fulfilled, (state, action) => {
        state.isLoading = false;
        state.stores = action.payload;
        state.error = null;
      })
      .addCase(fetchStores.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      // Fetch Store
      .addCase(fetchStore.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchStore.fulfilled, (state, action) => {
        state.isLoading = false;
        state.currentStore = action.payload;
        state.error = null;
      })
      .addCase(fetchStore.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      // Search Stores
      .addCase(searchStores.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(searchStores.fulfilled, (state, action) => {
        state.isLoading = false;
        state.searchResults = action.payload;
        state.error = null;
      })
      .addCase(searchStores.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      // Rate Store
      .addCase(rateStore.fulfilled, (state, action) => {
        const { storeId, rating } = action.payload;
        const store = state.stores.find(s => s.id === storeId);
        if (store) {
          store.rating = rating;
        }
        if (state.currentStore && state.currentStore.id === storeId) {
          state.currentStore.rating = rating;
        }
      });
  },
});

export const { clearError, clearSearchResults, setCurrentStore } = storeSlice.actions;
export default storeSlice.reducer;