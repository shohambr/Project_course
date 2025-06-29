import React from 'react';
import {
  TouchableOpacity,
  Text,
  StyleSheet,
  ViewStyle,
  TextStyle,
  ActivityIndicator,
  View,
} from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import * as Haptics from 'expo-haptics';
import * as Animatable from 'react-native-animatable';

interface EnhancedButtonProps {
  title: string;
  onPress: () => void;
  variant?: 'primary' | 'secondary' | 'danger' | 'success';
  size?: 'small' | 'medium' | 'large';
  loading?: boolean;
  disabled?: boolean;
  style?: ViewStyle;
  textStyle?: TextStyle;
  hapticFeedback?: boolean;
  gradient?: boolean;
  icon?: React.ReactNode;
}

const EnhancedButton: React.FC<EnhancedButtonProps> = ({
  title,
  onPress,
  variant = 'primary',
  size = 'medium',
  loading = false,
  disabled = false,
  style,
  textStyle,
  hapticFeedback = true,
  gradient = true,
  icon,
}) => {
  const handlePress = () => {
    if (disabled || loading) return;
    
    if (hapticFeedback) {
      Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
    }
    
    onPress();
  };

  const getGradientColors = () => {
    switch (variant) {
      case 'primary':
        return ['#007AFF', '#0051D5'];
      case 'secondary':
        return ['#8E8E93', '#636366'];
      case 'danger':
        return ['#FF3B30', '#D70015'];
      case 'success':
        return ['#34C759', '#30A46C'];
      default:
        return ['#007AFF', '#0051D5'];
    }
  };

  const getBackgroundColor = () => {
    switch (variant) {
      case 'primary':
        return '#007AFF';
      case 'secondary':
        return '#8E8E93';
      case 'danger':
        return '#FF3B30';
      case 'success':
        return '#34C759';
      default:
        return '#007AFF';
    }
  };

  const buttonStyle = [
    styles.button,
    styles[size],
    disabled && styles.disabled,
    !gradient && { backgroundColor: getBackgroundColor() },
    style,
  ];

  const content = (
    <View style={styles.content}>
      {loading ? (
        <ActivityIndicator color="#fff" size="small" />
      ) : (
        <>
          {icon && <View style={styles.icon}>{icon}</View>}
          <Text style={[styles.text, styles[`${size}Text`], textStyle]}>
            {title}
          </Text>
        </>
      )}
    </View>
  );

  if (gradient && !disabled) {
    return (
      <Animatable.View animation="pulse" iterationCount={1} duration={100}>
        <TouchableOpacity onPress={handlePress} disabled={disabled || loading}>
          <LinearGradient
            colors={getGradientColors()}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 1 }}
            style={buttonStyle}
          >
            {content}
          </LinearGradient>
        </TouchableOpacity>
      </Animatable.View>
    );
  }

  return (
    <Animatable.View animation="pulse" iterationCount={1} duration={100}>
      <TouchableOpacity
        style={buttonStyle}
        onPress={handlePress}
        disabled={disabled || loading}
      >
        {content}
      </TouchableOpacity>
    </Animatable.View>
  );
};

const styles = StyleSheet.create({
  button: {
    borderRadius: 12,
    justifyContent: 'center',
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  small: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    minHeight: 36,
  },
  medium: {
    paddingHorizontal: 20,
    paddingVertical: 12,
    minHeight: 48,
  },
  large: {
    paddingHorizontal: 24,
    paddingVertical: 16,
    minHeight: 56,
  },
  disabled: {
    opacity: 0.6,
    shadowOpacity: 0,
    elevation: 0,
  },
  content: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
  },
  icon: {
    marginRight: 8,
  },
  text: {
    color: '#fff',
    fontWeight: '600',
    textAlign: 'center',
  },
  smallText: {
    fontSize: 14,
  },
  mediumText: {
    fontSize: 16,
  },
  largeText: {
    fontSize: 18,
  },
});

export default EnhancedButton;