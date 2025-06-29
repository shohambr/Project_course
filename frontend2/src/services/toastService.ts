import Toast from 'react-native-toast-message';
import * as Haptics from 'expo-haptics';

export interface ToastConfig {
  type: 'success' | 'error' | 'info' | 'warning';
  title: string;
  message?: string;
  duration?: number;
  haptic?: boolean;
}

class ToastService {
  static show({
    type,
    title,
    message,
    duration = 4000,
    haptic = true
  }: ToastConfig) {
    // Trigger haptic feedback based on toast type
    if (haptic) {
      switch (type) {
        case 'success':
          Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success);
          break;
        case 'error':
          Haptics.notificationAsync(Haptics.NotificationFeedbackType.Error);
          break;
        case 'warning':
          Haptics.notificationAsync(Haptics.NotificationFeedbackType.Warning);
          break;
        case 'info':
          Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
          break;
      }
    }

    Toast.show({
      type,
      text1: title,
      text2: message,
      visibilityTime: duration,
      autoHide: true,
      topOffset: 60,
      bottomOffset: 40,
    });
  }

  static success(title: string, message?: string, duration?: number) {
    this.show({ type: 'success', title, message, duration });
  }

  static error(title: string, message?: string, duration?: number) {
    this.show({ type: 'error', title, message, duration });
  }

  static info(title: string, message?: string, duration?: number) {
    this.show({ type: 'info', title, message, duration });
  }

  static warning(title: string, message?: string, duration?: number) {
    this.show({ type: 'warning', title, message, duration });
  }

  static hide() {
    Toast.hide();
  }
}

export default ToastService;

// Predefined error messages for common scenarios
export const ErrorMessages = {
  NETWORK_ERROR: 'Network connection failed. Please check your internet connection.',
  VALIDATION_ERROR: 'Please check your input and try again.',
  AUTHENTICATION_ERROR: 'Invalid credentials. Please try again.',
  SERVER_ERROR: 'Server error occurred. Please try again later.',
  TIMEOUT_ERROR: 'Request timed out. Please try again.',
  UNKNOWN_ERROR: 'Something went wrong. Please try again.',
  
  // Auth specific
  EMPTY_FIELDS: 'Please fill in all required fields',
  PASSWORD_MISMATCH: 'Passwords do not match',
  WEAK_PASSWORD: 'Password must be at least 8 characters long',
  INVALID_USERNAME: 'Username must be at least 3 characters long',
  USERNAME_TAKEN: 'Username is already taken',
  
  // Form validation
  INVALID_EMAIL: 'Please enter a valid email address',
  INVALID_PHONE: 'Please enter a valid phone number',
  REQUIRED_FIELD: 'This field is required',
};

// Success messages
export const SuccessMessages = {
  LOGIN_SUCCESS: 'Welcome back!',
  REGISTER_SUCCESS: 'Account created successfully!',
  LOGOUT_SUCCESS: 'Logged out successfully',
  PROFILE_UPDATED: 'Profile updated successfully',
  PASSWORD_CHANGED: 'Password changed successfully',
  ORDER_PLACED: 'Order placed successfully!',
  ITEM_ADDED_TO_CART: 'Item added to cart',
  ITEM_REMOVED_FROM_CART: 'Item removed from cart',
};