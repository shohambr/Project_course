import React, { useState, useEffect, useCallback } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Alert,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  Animated,
  Easing,
} from 'react-native';
import { useDispatch, useSelector } from 'react-redux';
import { StackNavigationProp } from '@react-navigation/stack';
import { Ionicons } from '@expo/vector-icons';
import { LinearGradient } from 'expo-linear-gradient';
import * as Haptics from 'expo-haptics';
import { useFocusEffect } from '@react-navigation/native';

import { RootState, AppDispatch } from '../../store';
import { loginUser, clearError } from '../../store/slices/authSlice';
import { RootStackParamList } from '../../types';
import ToastService, { ErrorMessages, SuccessMessages } from '../../services/toastService';
import EnhancedButton from '../../components/EnhancedButton';
import { 
  debugLogger, 
  logScreenEnter, 
  logScreenLeave, 
  logButtonPress, 
  logInputChange, 
  logFormSubmit,
  logUserInteraction,
  logError,
  logCustomEvent
} from '../../services/debugLogger';

type LoginScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Login'>;

interface Props {
  navigation: LoginScreenNavigationProp;
}

const LoginScreen: React.FC<Props> = ({ navigation }) => {
  const dispatch = useDispatch<AppDispatch>();
  const { isLoading, error } = useSelector((state: RootState) => state.auth);
  const theme = useSelector((state: RootState) => state.settings.theme);

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [isFormValid, setIsFormValid] = useState(false);
  const [validationErrors, setValidationErrors] = useState<{[key: string]: string}>({});
  const [screenStartTime, setScreenStartTime] = useState<number>(0);

  // Animation values
  const fadeAnim = useState(new Animated.Value(0))[0];
  const slideAnim = useState(new Animated.Value(50))[0];
  const pulseAnim = useState(new Animated.Value(1))[0];

  const isDark = theme === 'dark';
  const SCREEN_NAME = 'LoginScreen';

  // Screen focus/blur logging
  useFocusEffect(
    useCallback(() => {
      const startTime = Date.now();
      setScreenStartTime(startTime);
      
      logScreenEnter(SCREEN_NAME, { 
        timestamp: new Date().toISOString(),
        theme: theme,
        platform: Platform.OS 
      });
      
      debugLogger.startScreenTimer(SCREEN_NAME);
      logUserInteraction('SCREEN_FOCUS', 'LOGIN_SCREEN', { timestamp: new Date() });

      // Screen enter animation
      animateScreenEntry();

      return () => {
        const endTime = Date.now();
        const timeSpent = endTime - startTime;
        
        logScreenLeave(SCREEN_NAME, timeSpent);
        debugLogger.endScreenTimer(SCREEN_NAME);
        
        logCustomEvent('SCREEN_ANALYTICS', 'LOGIN_SCREEN_TIME', {
          timeSpent: `${timeSpent}ms`,
          timeSpentSeconds: `${(timeSpent / 1000).toFixed(2)}s`,
          formCompleted: username.length > 0 && password.length > 0,
          validationErrors: Object.keys(validationErrors).length,
        });
      };
    }, [theme])
  );

  const animateScreenEntry = () => {
    logCustomEvent('ANIMATION', 'SCREEN_ENTRY_START');
    
    Animated.parallel([
      Animated.timing(fadeAnim, {
        toValue: 1,
        duration: 500,
        easing: Easing.out(Easing.quad),
        useNativeDriver: true,
      }),
      Animated.timing(slideAnim, {
        toValue: 0,
        duration: 500,
        easing: Easing.out(Easing.back(1.1)),
        useNativeDriver: true,
      }),
    ]).start(() => {
      logCustomEvent('ANIMATION', 'SCREEN_ENTRY_COMPLETE');
    });
  };

  const validateForm = useCallback(() => {
    const errors: {[key: string]: string} = {};
    let isValid = true;

    logUserInteraction('VALIDATION', 'FORM_VALIDATE', {
      usernameLength: username.length,
      passwordLength: password.length,
    }, SCREEN_NAME);

    if (!username.trim()) {
      errors.username = ErrorMessages.REQUIRED_FIELD;
      isValid = false;
    } else if (username.trim().length < 3) {
      errors.username = ErrorMessages.INVALID_USERNAME;
      isValid = false;
    }

    if (!password.trim()) {
      errors.password = ErrorMessages.REQUIRED_FIELD;
      isValid = false;
    } else if (password.length < 3) { // Simplified for demo
      errors.password = 'Password must be at least 3 characters';
      isValid = false;
    }

    setValidationErrors(errors);
    setIsFormValid(isValid);

    logCustomEvent('FORM_VALIDATION', isValid ? 'VALIDATION_PASSED' : 'VALIDATION_FAILED', {
      errors: Object.keys(errors),
      errorCount: Object.keys(errors).length,
      isValid,
    });

    return isValid;
  }, [username, password]);

  useEffect(() => {
    validateForm();
  }, [username, password, validateForm]);

  // Show error toast when Redux error occurs
  useEffect(() => {
    if (error) {
      logError('LOGIN_ERROR', error, {
        username: username.trim(),
        timestamp: new Date().toISOString(),
      });

      let errorMessage = ErrorMessages.AUTHENTICATION_ERROR;
      
      if (error.includes('network') || error.includes('fetch')) {
        errorMessage = ErrorMessages.NETWORK_ERROR;
      } else if (error.includes('timeout')) {
        errorMessage = ErrorMessages.TIMEOUT_ERROR;
      } else if (error.includes('server')) {
        errorMessage = ErrorMessages.SERVER_ERROR;
      }

      ToastService.error('Login Failed', errorMessage);
      
      // Pulse animation on error
      Animated.sequence([
        Animated.timing(pulseAnim, { toValue: 1.1, duration: 100, useNativeDriver: true }),
        Animated.timing(pulseAnim, { toValue: 1, duration: 100, useNativeDriver: true }),
      ]).start();
    }
  }, [error, username]);

  const handleUsernameChange = (value: string) => {
    logInputChange('username', value, SCREEN_NAME);
    
    logUserInteraction('INPUT_CHANGE', 'USERNAME_FIELD', {
      newLength: value.length,
      oldLength: username.length,
      hasSpecialChars: /[^a-zA-Z0-9]/.test(value),
      timestamp: new Date().toISOString(),
    }, SCREEN_NAME);

    setUsername(value);
    
    if (value.length > 0) {
      Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
    }
  };

  const handlePasswordChange = (value: string) => {
    logInputChange('password', '[HIDDEN]', SCREEN_NAME);
    
    logUserInteraction('INPUT_CHANGE', 'PASSWORD_FIELD', {
      newLength: value.length,
      oldLength: password.length,
      hasNumbers: /\d/.test(value),
      hasUppercase: /[A-Z]/.test(value),
      hasSpecialChars: /[^a-zA-Z0-9]/.test(value),
      timestamp: new Date().toISOString(),
    }, SCREEN_NAME);

    setPassword(value);
    
    if (value.length > 0) {
      Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
    }
  };

  const handleShowPasswordToggle = () => {
    logButtonPress('SHOW_PASSWORD_TOGGLE', SCREEN_NAME, {
      currentState: showPassword,
      newState: !showPassword,
    });

    logUserInteraction('TOGGLE', 'PASSWORD_VISIBILITY', {
      fromState: showPassword ? 'hidden' : 'visible',
      toState: showPassword ? 'visible' : 'hidden',
    }, SCREEN_NAME);

    setShowPassword(!showPassword);
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
  };

  const handleLogin = async () => {
    const startTime = Date.now();
    
    logButtonPress('LOGIN_BUTTON', SCREEN_NAME, {
      username: username.trim(),
      passwordLength: password.length,
      formValid: isFormValid,
      timestamp: new Date().toISOString(),
    });

    logFormSubmit('LOGIN_FORM', {
      username: username.trim(),
      password: '[HIDDEN]',
    }, SCREEN_NAME);

    if (!validateForm()) {
      logCustomEvent('FORM_SUBMISSION', 'VALIDATION_FAILED', {
        errors: validationErrors,
        attemptedSubmission: true,
      });
      
      ToastService.error('Validation Error', ErrorMessages.VALIDATION_ERROR);
      Haptics.notificationAsync(Haptics.NotificationFeedbackType.Error);
      return;
    }

    try {
      logCustomEvent('AUTH', 'LOGIN_ATTEMPT_START', {
        username: username.trim(),
        timestamp: new Date().toISOString(),
      });

      const result = await dispatch(loginUser({ 
        username: username.trim(), 
        password 
      })).unwrap();

      const duration = Date.now() - startTime;

      logCustomEvent('AUTH', 'LOGIN_ATTEMPT_SUCCESS', {
        username: username.trim(),
        duration: `${duration}ms`,
        timestamp: new Date().toISOString(),
      });

      ToastService.success(SuccessMessages.LOGIN_SUCCESS, `Welcome back, ${username.trim()}!`);
      Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success);

    } catch (error: any) {
      const duration = Date.now() - startTime;
      
      logError('LOGIN_ATTEMPT', error, {
        username: username.trim(),
        duration: `${duration}ms`,
        timestamp: new Date().toISOString(),
      });

      logCustomEvent('AUTH', 'LOGIN_ATTEMPT_FAILED', {
        username: username.trim(),
        error: error.message || error.toString(),
        duration: `${duration}ms`,
      });

      // Error handling is done in useEffect for Redux errors
    }
  };

  const navigateToRegister = () => {
    logButtonPress('NAVIGATE_TO_REGISTER', SCREEN_NAME, {
      currentFormState: {
        usernameLength: username.length,
        passwordLength: password.length,
        formValid: isFormValid,
      },
    });

    logUserInteraction('NAVIGATION', 'TO_REGISTER', {
      fromScreen: SCREEN_NAME,
      formCompleted: username.length > 0 && password.length > 0,
    });

    dispatch(clearError());
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
    navigation.navigate('Register');
  };

  const handleInputFocus = (fieldName: string) => {
    logUserInteraction('INPUT_FOCUS', fieldName.toUpperCase(), {
      timestamp: new Date().toISOString(),
    }, SCREEN_NAME);
    
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
  };

  const handleInputBlur = (fieldName: string) => {
    logUserInteraction('INPUT_BLUR', fieldName.toUpperCase(), {
      timestamp: new Date().toISOString(),
    }, SCREEN_NAME);
  };

  return (
    <LinearGradient
      colors={isDark ? ['#000', '#1a1a1a', '#000'] : ['#f0f9ff', '#ffffff', '#f0f9ff']}
      style={styles.container}
    >
      <KeyboardAvoidingView 
        style={styles.container}
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
      >
        <ScrollView 
          contentContainerStyle={styles.scrollContent} 
          keyboardShouldPersistTaps="handled"
          showsVerticalScrollIndicator={false}
        >
          <Animated.View 
            style={[
              styles.contentContainer,
              {
                opacity: fadeAnim,
                transform: [
                  { translateY: slideAnim },
                  { scale: pulseAnim }
                ]
              }
            ]}
          >
            <View style={styles.header}>
              <Text style={[styles.title, { color: isDark ? '#fff' : '#000' }]}>
                Welcome to Frontend2
              </Text>
              <Text style={[styles.subtitle, { color: isDark ? '#ccc' : '#666' }]}>
                Sign in to your account
              </Text>
            </View>

            <View style={styles.form}>
              <View style={[
                styles.inputContainer, 
                { 
                  borderColor: validationErrors.username 
                    ? '#ff4444' 
                    : isDark ? '#333' : '#ddd',
                  backgroundColor: isDark ? '#1a1a1a' : '#f9f9f9',
                  shadowColor: validationErrors.username ? '#ff4444' : '#000',
                }
              ]}>
                <Ionicons 
                  name="person-outline" 
                  size={20} 
                  color={validationErrors.username ? '#ff4444' : (isDark ? '#ccc' : '#666')} 
                  style={styles.inputIcon} 
                />
                <TextInput
                  style={[styles.input, { color: isDark ? '#fff' : '#000' }]}
                  placeholder="Username"
                  placeholderTextColor={isDark ? '#888' : '#999'}
                  value={username}
                  onChangeText={handleUsernameChange}
                  onFocus={() => handleInputFocus('username')}
                  onBlur={() => handleInputBlur('username')}
                  autoCapitalize="none"
                  autoCorrect={false}
                  autoComplete="username"
                />
                {username.length > 0 && (
                  <Ionicons 
                    name={validationErrors.username ? "close-circle" : "checkmark-circle"} 
                    size={20} 
                    color={validationErrors.username ? '#ff4444' : '#34C759'} 
                  />
                )}
              </View>
              
              {validationErrors.username && (
                <Text style={styles.errorText}>{validationErrors.username}</Text>
              )}

              <View style={[
                styles.inputContainer, 
                { 
                  borderColor: validationErrors.password 
                    ? '#ff4444' 
                    : isDark ? '#333' : '#ddd',
                  backgroundColor: isDark ? '#1a1a1a' : '#f9f9f9',
                  shadowColor: validationErrors.password ? '#ff4444' : '#000',
                }
              ]}>
                <Ionicons 
                  name="lock-closed-outline" 
                  size={20} 
                  color={validationErrors.password ? '#ff4444' : (isDark ? '#ccc' : '#666')} 
                  style={styles.inputIcon} 
                />
                <TextInput
                  style={[styles.input, { color: isDark ? '#fff' : '#000' }]}
                  placeholder="Password"
                  placeholderTextColor={isDark ? '#888' : '#999'}
                  value={password}
                  onChangeText={handlePasswordChange}
                  onFocus={() => handleInputFocus('password')}
                  onBlur={() => handleInputBlur('password')}
                  secureTextEntry={!showPassword}
                  autoCapitalize="none"
                  autoCorrect={false}
                  autoComplete="password"
                />
                <TouchableOpacity
                  onPress={handleShowPasswordToggle}
                  style={styles.eyeIcon}
                >
                  <Ionicons 
                    name={showPassword ? "eye-off-outline" : "eye-outline"} 
                    size={20} 
                    color={isDark ? '#ccc' : '#666'} 
                  />
                </TouchableOpacity>
              </View>
              
              {validationErrors.password && (
                <Text style={styles.errorText}>{validationErrors.password}</Text>
              )}

              <EnhancedButton
                title={isLoading ? 'Signing In...' : 'Sign In'}
                onPress={handleLogin}
                variant="primary"
                loading={isLoading}
                disabled={!isFormValid || isLoading}
                style={{ marginTop: 20 }}
                hapticFeedback={true}
                gradient={true}
                icon={!isLoading && <Ionicons name="log-in-outline" size={20} color="#fff" />}
              />

              <View style={styles.registerContainer}>
                <Text style={[styles.registerText, { color: isDark ? '#ccc' : '#666' }]}>
                  Don't have an account?{' '}
                </Text>
                <TouchableOpacity onPress={navigateToRegister}>
                  <Text style={styles.registerLink}>Sign Up</Text>
                </TouchableOpacity>
              </View>
            </View>
          </Animated.View>
        </ScrollView>
      </KeyboardAvoidingView>
    </LinearGradient>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  scrollContent: {
    flexGrow: 1,
    justifyContent: 'center',
    padding: 20,
  },
  contentContainer: {
    flex: 1,
    justifyContent: 'center',
  },
  header: {
    alignItems: 'center',
    marginBottom: 40,
  },
  title: {
    fontSize: 32,
    fontWeight: 'bold',
    marginBottom: 8,
    textAlign: 'center',
  },
  subtitle: {
    fontSize: 18,
    textAlign: 'center',
  },
  form: {
    width: '100%',
  },
  inputContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 2,
    borderRadius: 12,
    marginBottom: 8,
    paddingHorizontal: 15,
    height: 54,
    shadowOffset: {
      width: 0,
      height: 1,
    },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    elevation: 2,
  },
  inputIcon: {
    marginRight: 12,
  },
  input: {
    flex: 1,
    fontSize: 16,
    paddingVertical: 0,
  },
  eyeIcon: {
    padding: 8,
  },
  errorText: {
    color: '#ff4444',
    fontSize: 14,
    marginBottom: 12,
    marginLeft: 8,
    fontWeight: '500',
  },
  registerContainer: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 30,
  },
  registerText: {
    fontSize: 16,
  },
  registerLink: {
    fontSize: 16,
    color: '#007AFF',
    fontWeight: '600',
  },
});

export default LoginScreen;