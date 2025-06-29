import React, { useState, useEffect } from 'react';
import {
  View,
  TouchableOpacity,
  Text,
  StyleSheet,
  Animated,
  Alert,
  Vibration,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useSelector } from 'react-redux';
import { RootState } from '../store';
import AdminTerminal from './AdminTerminal';
import DebugPanel from './DebugPanel';
import * as Haptics from 'expo-haptics';
import { debugLogger, logCustomEvent, logUserInteraction } from '../services/debugLogger';

interface AdminAccessButtonProps {
  style?: any;
}

const AdminAccessButton: React.FC<AdminAccessButtonProps> = ({ style }) => {
  const [showAdminTerminal, setShowAdminTerminal] = useState(false);
  const [showDebugPanel, setShowDebugPanel] = useState(false);
  const [isExpanded, setIsExpanded] = useState(false);
  const [tapCount, setTapCount] = useState(0);
  const [lastTapTime, setLastTapTime] = useState(0);
  const [isAdminMode, setIsAdminMode] = useState(false);

  // Animation values
  const scaleAnim = useState(new Animated.Value(1))[0];
  const rotateAnim = useState(new Animated.Value(0))[0];
  const expandAnim = useState(new Animated.Value(0))[0];

  const user = useSelector((state: RootState) => state.auth.user);
  const isAuthenticated = useSelector((state: RootState) => state.auth.isAuthenticated);

  // Admin users - in production, this should come from user roles/permissions
  const ADMIN_USERS = ['admin', 'founder', 'owner', 'superuser', 'root'];
  const isUserAdmin = user?.username && ADMIN_USERS.includes(user.username.toLowerCase());

  // Secret tap sequence to activate admin mode (7 taps in 3 seconds)
  const ADMIN_TAP_SEQUENCE = 7;
  const TAP_TIME_WINDOW = 3000; // 3 seconds

  useEffect(() => {
    // Reset tap count after time window
    const timer = setTimeout(() => {
      if (tapCount > 0) {
        setTapCount(0);
        logCustomEvent('ADMIN_ACCESS', 'TAP_SEQUENCE_TIMEOUT', {
          tapCount,
          timeWindow: TAP_TIME_WINDOW,
        });
      }
    }, TAP_TIME_WINDOW);

    return () => clearTimeout(timer);
  }, [lastTapTime, tapCount]);

  const handleSecretTap = () => {
    const currentTime = Date.now();
    const timeDiff = currentTime - lastTapTime;

    if (timeDiff > TAP_TIME_WINDOW) {
      setTapCount(1);
    } else {
      setTapCount(prev => prev + 1);
    }

    setLastTapTime(currentTime);

    logUserInteraction('SECRET_TAP', 'ADMIN_BUTTON', {
      tapCount: tapCount + 1,
      timeDiff,
      isAuthenticated,
      user: user?.username,
    });

    // Haptic feedback for each tap
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);

    // Animate button on tap
    Animated.sequence([
      Animated.timing(scaleAnim, {
        toValue: 0.9,
        duration: 100,
        useNativeDriver: true,
      }),
      Animated.timing(scaleAnim, {
        toValue: 1,
        duration: 100,
        useNativeDriver: true,
      }),
    ]).start();

    // Check if admin sequence completed
    if (tapCount + 1 >= ADMIN_TAP_SEQUENCE) {
      activateAdminMode();
    }
  };

  const activateAdminMode = () => {
    if (!isAuthenticated) {
      Alert.alert(
        'Authentication Required',
        'You must be logged in to access admin features.',
        [{ text: 'OK' }]
      );
      return;
    }

    setIsAdminMode(true);
    setTapCount(0);
    
    // Strong haptic feedback for admin activation
    Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success);
    Vibration.vibrate([100, 50, 100]);

    logCustomEvent('ADMIN_ACCESS', 'ADMIN_MODE_ACTIVATED', {
      user: user?.username,
      tapSequenceCompleted: true,
      isUserAdmin,
      timestamp: new Date().toISOString(),
    });

    console.log('👑 ADMIN MODE ACTIVATED', {
      user: user?.username,
      isUserAdmin,
      timestamp: new Date().toISOString(),
    });

    // Animate admin mode activation
    Animated.parallel([
      Animated.timing(rotateAnim, {
        toValue: 1,
        duration: 500,
        useNativeDriver: true,
      }),
      Animated.timing(expandAnim, {
        toValue: 1,
        duration: 300,
        useNativeDriver: true,
      }),
    ]).start();

    setIsExpanded(true);

    Alert.alert(
      '👑 Admin Mode Activated',
      `Welcome, ${user?.username}!\n\nAdmin features are now available:\n• 🖥️ Admin Terminal\n• 🔍 Debug Panel\n• 📊 System Monitoring\n\nUse responsibly.`,
      [
        { 
          text: 'Open Terminal', 
          onPress: () => setShowAdminTerminal(true),
          style: 'default'
        },
        { 
          text: 'Debug Panel', 
          onPress: () => setShowDebugPanel(true),
          style: 'default'
        },
        { 
          text: 'OK', 
          style: 'default'
        },
      ]
    );
  };

  const handleToggleExpand = () => {
    const newExpanded = !isExpanded;
    setIsExpanded(newExpanded);

    logUserInteraction('ADMIN_PANEL', newExpanded ? 'EXPAND' : 'COLLAPSE', {
      isAdminMode,
      user: user?.username,
    });

    Animated.timing(expandAnim, {
      toValue: newExpanded ? 1 : 0,
      duration: 300,
      useNativeDriver: true,
    }).start();

    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
  };

  const handleOpenAdminTerminal = () => {
    logCustomEvent('ADMIN_ACCESS', 'TERMINAL_OPENED', {
      user: user?.username,
      source: 'admin_button',
    });

    setShowAdminTerminal(true);
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Heavy);
  };

  const handleOpenDebugPanel = () => {
    logCustomEvent('ADMIN_ACCESS', 'DEBUG_PANEL_OPENED', {
      user: user?.username,
      source: 'admin_button',
    });

    setShowDebugPanel(true);
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
  };

  const handleDeactivateAdmin = () => {
    Alert.alert(
      'Deactivate Admin Mode',
      'Are you sure you want to exit admin mode?',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Deactivate',
          style: 'destructive',
          onPress: () => {
            setIsAdminMode(false);
            setIsExpanded(false);
            setTapCount(0);

            logCustomEvent('ADMIN_ACCESS', 'ADMIN_MODE_DEACTIVATED', {
              user: user?.username,
              timestamp: new Date().toISOString(),
            });

            Animated.parallel([
              Animated.timing(rotateAnim, {
                toValue: 0,
                duration: 300,
                useNativeDriver: true,
              }),
              Animated.timing(expandAnim, {
                toValue: 0,
                duration: 300,
                useNativeDriver: true,
              }),
            ]).start();

            Haptics.notificationAsync(Haptics.NotificationFeedbackType.Warning);
          }
        }
      ]
    );
  };

  const getRotation = rotateAnim.interpolate({
    inputRange: [0, 1],
    outputRange: ['0deg', '360deg'],
  });

  const getExpandScale = expandAnim.interpolate({
    inputRange: [0, 1],
    outputRange: [0, 1],
  });

  // Don't show if user is not authenticated
  if (!isAuthenticated) {
    return null;
  }

  return (
    <View style={[styles.container, style]}>
      {/* Admin Terminal Modal */}
      <AdminTerminal
        visible={showAdminTerminal}
        onClose={() => setShowAdminTerminal(false)}
      />

      {/* Debug Panel Modal */}
      <DebugPanel
        visible={showDebugPanel}
        onClose={() => setShowDebugPanel(false)}
      />

      {/* Expanded Admin Panel */}
      {isAdminMode && (
        <Animated.View 
          style={[
            styles.expandedPanel,
            {
              transform: [{ scale: getExpandScale }],
              opacity: expandAnim,
            }
          ]}
        >
          <TouchableOpacity
            style={styles.adminButton}
            onPress={handleOpenAdminTerminal}
          >
            <Ionicons name="terminal" size={20} color="#fff" />
            <Text style={styles.adminButtonText}>Terminal</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={styles.adminButton}
            onPress={handleOpenDebugPanel}
          >
            <Ionicons name="bug" size={20} color="#fff" />
            <Text style={styles.adminButtonText}>Debug</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={[styles.adminButton, styles.dangerButton]}
            onPress={handleDeactivateAdmin}
          >
            <Ionicons name="power" size={20} color="#fff" />
            <Text style={styles.adminButtonText}>Exit</Text>
          </TouchableOpacity>
        </Animated.View>
      )}

      {/* Main Admin Access Button */}
      <TouchableOpacity
        style={[
          styles.mainButton,
          isAdminMode && styles.adminActiveButton,
        ]}
        onPress={isAdminMode ? handleToggleExpand : handleSecretTap}
        onLongPress={isUserAdmin ? activateAdminMode : undefined}
      >
        <Animated.View
          style={{
            transform: [
              { scale: scaleAnim },
              { rotate: getRotation },
            ],
          }}
        >
          <Ionicons 
            name={isAdminMode ? "settings" : "shield-checkmark"} 
            size={24} 
            color={isAdminMode ? "#ff6b6b" : "#666"} 
          />
        </Animated.View>
        
        {tapCount > 0 && tapCount < ADMIN_TAP_SEQUENCE && (
          <View style={styles.tapIndicator}>
            <Text style={styles.tapCount}>{tapCount}</Text>
          </View>
        )}

        {isAdminMode && (
          <View style={styles.adminIndicator}>
            <Text style={styles.adminText}>ADMIN</Text>
          </View>
        )}
      </TouchableOpacity>

      {/* Instructions for non-admin users */}
      {!isAdminMode && isUserAdmin && (
        <View style={styles.instructionsBubble}>
          <Text style={styles.instructionsText}>
            Long press for admin access
          </Text>
        </View>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    position: 'absolute',
    bottom: 100,
    right: 20,
    alignItems: 'center',
  },
  mainButton: {
    width: 56,
    height: 56,
    borderRadius: 28,
    backgroundColor: 'rgba(0, 0, 0, 0.8)',
    justifyContent: 'center',
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 4,
    },
    shadowOpacity: 0.3,
    shadowRadius: 6,
    elevation: 8,
    borderWidth: 2,
    borderColor: 'rgba(255, 255, 255, 0.1)',
  },
  adminActiveButton: {
    backgroundColor: '#1a1a1a',
    borderColor: '#ff6b6b',
    shadowColor: '#ff6b6b',
  },
  expandedPanel: {
    position: 'absolute',
    bottom: 70,
    right: 0,
    flexDirection: 'column',
    alignItems: 'flex-end',
  },
  adminButton: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.9)',
    paddingHorizontal: 16,
    paddingVertical: 12,
    borderRadius: 25,
    marginBottom: 8,
    borderWidth: 1,
    borderColor: '#4ecdc4',
    minWidth: 120,
  },
  dangerButton: {
    borderColor: '#ff6b6b',
  },
  adminButtonText: {
    color: '#fff',
    fontSize: 14,
    fontWeight: '600',
    marginLeft: 8,
  },
  tapIndicator: {
    position: 'absolute',
    top: -8,
    right: -8,
    width: 20,
    height: 20,
    borderRadius: 10,
    backgroundColor: '#ffcc00',
    justifyContent: 'center',
    alignItems: 'center',
  },
  tapCount: {
    color: '#000',
    fontSize: 12,
    fontWeight: 'bold',
  },
  adminIndicator: {
    position: 'absolute',
    bottom: -12,
    left: 0,
    right: 0,
    backgroundColor: '#ff6b6b',
    borderRadius: 8,
    paddingVertical: 2,
    paddingHorizontal: 4,
  },
  adminText: {
    color: '#fff',
    fontSize: 8,
    fontWeight: 'bold',
    textAlign: 'center',
    letterSpacing: 1,
  },
  instructionsBubble: {
    position: 'absolute',
    bottom: 70,
    right: 0,
    backgroundColor: 'rgba(0, 0, 0, 0.8)',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 12,
    maxWidth: 120,
  },
  instructionsText: {
    color: '#fff',
    fontSize: 10,
    textAlign: 'center',
  },
});

export default AdminAccessButton;