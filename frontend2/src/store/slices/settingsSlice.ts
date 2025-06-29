import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { AppSettings } from '../../types';

interface SettingsState {
  theme: 'light' | 'dark';
  language: string;
  notifications: boolean;
  fontSize: 'small' | 'medium' | 'large';
  autoSync: boolean;
}

const initialState: SettingsState = {
  theme: 'light',
  language: 'en',
  notifications: true,
  fontSize: 'medium',
  autoSync: true,
};

const settingsSlice = createSlice({
  name: 'settings',
  initialState,
  reducers: {
    setTheme: (state, action: PayloadAction<'light' | 'dark'>) => {
      state.theme = action.payload;
    },
    setLanguage: (state, action: PayloadAction<string>) => {
      state.language = action.payload;
    },
    setNotifications: (state, action: PayloadAction<boolean>) => {
      state.notifications = action.payload;
    },
    setFontSize: (state, action: PayloadAction<'small' | 'medium' | 'large'>) => {
      state.fontSize = action.payload;
    },
    setAutoSync: (state, action: PayloadAction<boolean>) => {
      state.autoSync = action.payload;
    },
    resetSettings: (state) => {
      return initialState;
    },
    updateSettings: (state, action: PayloadAction<Partial<SettingsState>>) => {
      return { ...state, ...action.payload };
    },
  },
});

export const {
  setTheme,
  setLanguage,
  setNotifications,
  setFontSize,
  setAutoSync,
  resetSettings,
  updateSettings,
} = settingsSlice.actions;

export default settingsSlice.reducer;