import { configureStore } from '@reduxjs/toolkit';
import authSlice from './slices/authSlice';
import storeSlice from './slices/storeSlice';
import productSlice from './slices/productSlice';
import cartSlice from './slices/cartSlice';
import settingsSlice from './slices/settingsSlice';

export const store = configureStore({
  reducer: {
    auth: authSlice,
    stores: storeSlice,
    products: productSlice,
    cart: cartSlice,
    settings: settingsSlice,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: ['persist/PERSIST'],
      },
    }),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;