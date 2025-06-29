import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Switch,
  Alert,
} from 'react-native';
import { useSelector, useDispatch } from 'react-redux';
import { Ionicons } from '@expo/vector-icons';
import { StackNavigationProp } from '@react-navigation/stack';

import { RootState, AppDispatch } from '../../store';
import { setTheme, setLanguage, setNotifications, setFontSize, setAutoSync, resetSettings } from '../../store/slices/settingsSlice';
import { logoutUser } from '../../store/slices/authSlice';
import { RootStackParamList } from '../../types';

type SettingsScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Settings'>;

interface Props {
  navigation: SettingsScreenNavigationProp;
}

const SettingsScreen: React.FC<Props> = ({ navigation }) => {
  const dispatch = useDispatch<AppDispatch>();
  const settings = useSelector((state: RootState) => state.settings);
  const { user } = useSelector((state: RootState) => state.auth);

  const isDark = settings.theme === 'dark';

  const handleLogout = () => {
    Alert.alert(
      'Logout',
      'Are you sure you want to logout?',
      [
        { text: 'Cancel', style: 'cancel' },
        { 
          text: 'Logout', 
          style: 'destructive',
          onPress: () => dispatch(logoutUser())
        },
      ]
    );
  };

  const handleResetSettings = () => {
    Alert.alert(
      'Reset Settings',
      'Are you sure you want to reset all settings to default?',
      [
        { text: 'Cancel', style: 'cancel' },
        { 
          text: 'Reset', 
          style: 'destructive',
          onPress: () => dispatch(resetSettings())
        },
      ]
    );
  };

  const SettingItem = ({ 
    icon, 
    title, 
    subtitle, 
    onPress, 
    rightComponent 
  }: {
    icon: string;
    title: string;
    subtitle?: string;
    onPress?: () => void;
    rightComponent?: React.ReactNode;
  }) => (
    <TouchableOpacity
      style={[styles.settingItem, { backgroundColor: isDark ? '#1a1a1a' : '#fff' }]}
      onPress={onPress}
      disabled={!onPress}
    >
      <View style={styles.settingLeft}>
        <Ionicons name={icon as any} size={24} color={isDark ? '#007AFF' : '#007AFF'} />
        <View style={styles.settingText}>
          <Text style={[styles.settingTitle, { color: isDark ? '#fff' : '#000' }]}>
            {title}
          </Text>
          {subtitle && (
            <Text style={[styles.settingSubtitle, { color: isDark ? '#ccc' : '#666' }]}>
              {subtitle}
            </Text>
          )}
        </View>
      </View>
      {rightComponent || (
        onPress && <Ionicons name="chevron-forward" size={20} color={isDark ? '#ccc' : '#999'} />
      )}
    </TouchableOpacity>
  );

  return (
    <View style={[styles.container, { backgroundColor: isDark ? '#000' : '#f5f5f5' }]}>
      <ScrollView style={styles.scrollView} contentContainerStyle={styles.scrollContent}>
        {/* User Profile Section */}
        <View style={[styles.section, { backgroundColor: isDark ? '#1a1a1a' : '#fff' }]}>
          <TouchableOpacity 
            style={styles.profileSection}
            onPress={() => navigation.navigate('Profile')}
          >
            <View style={styles.avatarContainer}>
              <Ionicons name="person" size={30} color="#007AFF" />
            </View>
            <View style={styles.profileInfo}>
              <Text style={[styles.profileName, { color: isDark ? '#fff' : '#000' }]}>
                {user?.username || 'Guest User'}
              </Text>
              <Text style={[styles.profileEmail, { color: isDark ? '#ccc' : '#666' }]}>
                Tap to view profile
              </Text>
            </View>
            <Ionicons name="chevron-forward" size={20} color={isDark ? '#ccc' : '#999'} />
          </TouchableOpacity>
        </View>

        {/* App Preferences */}
        <Text style={[styles.sectionHeader, { color: isDark ? '#ccc' : '#666' }]}>
          APP PREFERENCES
        </Text>
        <View style={[styles.section, { backgroundColor: isDark ? '#1a1a1a' : '#fff' }]}>
          <SettingItem
            icon="moon"
            title="Dark Mode"
            subtitle={settings.theme === 'dark' ? 'Enabled' : 'Disabled'}
            rightComponent={
              <Switch
                value={settings.theme === 'dark'}
                onValueChange={(value) => dispatch(setTheme(value ? 'dark' : 'light'))}
                trackColor={{ false: '#767577', true: '#007AFF' }}
                thumbColor={settings.theme === 'dark' ? '#007AFF' : '#f4f3f4'}
              />
            }
          />
          <SettingItem
            icon="language"
            title="Language"
            subtitle="English"
            onPress={() => {
              Alert.alert('Language Settings', 'Language selection coming soon!');
            }}
          />
          <SettingItem
            icon="text"
            title="Font Size"
            subtitle={settings.fontSize.charAt(0).toUpperCase() + settings.fontSize.slice(1)}
            onPress={() => {
              const sizes = ['small', 'medium', 'large'] as const;
              const currentIndex = sizes.indexOf(settings.fontSize);
              const nextIndex = (currentIndex + 1) % sizes.length;
              dispatch(setFontSize(sizes[nextIndex]));
            }}
          />
        </View>

        {/* Notifications */}
        <Text style={[styles.sectionHeader, { color: isDark ? '#ccc' : '#666' }]}>
          NOTIFICATIONS
        </Text>
        <View style={[styles.section, { backgroundColor: isDark ? '#1a1a1a' : '#fff' }]}>
          <SettingItem
            icon="notifications"
            title="Push Notifications"
            subtitle={settings.notifications ? 'Enabled' : 'Disabled'}
            rightComponent={
              <Switch
                value={settings.notifications}
                onValueChange={(value) => dispatch(setNotifications(value))}
                trackColor={{ false: '#767577', true: '#007AFF' }}
                thumbColor={settings.notifications ? '#007AFF' : '#f4f3f4'}
              />
            }
          />
          <SettingItem
            icon="sync"
            title="Auto Sync"
            subtitle={settings.autoSync ? 'Enabled' : 'Disabled'}
            rightComponent={
              <Switch
                value={settings.autoSync}
                onValueChange={(value) => dispatch(setAutoSync(value))}
                trackColor={{ false: '#767577', true: '#007AFF' }}
                thumbColor={settings.autoSync ? '#007AFF' : '#f4f3f4'}
              />
            }
          />
        </View>

        {/* Account */}
        <Text style={[styles.sectionHeader, { color: isDark ? '#ccc' : '#666' }]}>
          ACCOUNT
        </Text>
        <View style={[styles.section, { backgroundColor: isDark ? '#1a1a1a' : '#fff' }]}>
          <SettingItem
            icon="receipt"
            title="Order History"
            subtitle="View your past orders"
            onPress={() => navigation.navigate('OrderHistory')}
          />
          <SettingItem
            icon="shield-checkmark"
            title="Privacy & Security"
            subtitle="Manage your privacy settings"
            onPress={() => {
              Alert.alert('Privacy Settings', 'Privacy settings coming soon!');
            }}
          />
          <SettingItem
            icon="card"
            title="Payment Methods"
            subtitle="Manage payment options"
            onPress={() => {
              Alert.alert('Payment Methods', 'Payment method management coming soon!');
            }}
          />
        </View>

        {/* Support */}
        <Text style={[styles.sectionHeader, { color: isDark ? '#ccc' : '#666' }]}>
          SUPPORT
        </Text>
        <View style={[styles.section, { backgroundColor: isDark ? '#1a1a1a' : '#fff' }]}>
          <SettingItem
            icon="help-circle"
            title="Help & Support"
            subtitle="Get help and contact support"
            onPress={() => {
              Alert.alert('Help & Support', 'Support section coming soon!');
            }}
          />
          <SettingItem
            icon="information-circle"
            title="About"
            subtitle="Version 1.0.0"
            onPress={() => {
              Alert.alert(
                'About Frontend2',
                'Frontend2 v1.0.0\n\nA modern e-commerce mobile application built with React Native and Expo.\n\n© 2024 Frontend2 Team',
                [{ text: 'OK' }]
              );
            }}
          />
          <SettingItem
            icon="star"
            title="Rate the App"
            subtitle="Share your feedback"
            onPress={() => {
              Alert.alert('Rate the App', 'Thank you for your interest! App store integration coming soon.');
            }}
          />
        </View>

        {/* Dangerous Actions */}
        <Text style={[styles.sectionHeader, { color: isDark ? '#ccc' : '#666' }]}>
          ADVANCED
        </Text>
        <View style={[styles.section, { backgroundColor: isDark ? '#1a1a1a' : '#fff' }]}>
          <SettingItem
            icon="refresh"
            title="Reset Settings"
            subtitle="Reset all settings to default"
            onPress={handleResetSettings}
          />
          <TouchableOpacity
            style={[styles.settingItem, styles.logoutItem]}
            onPress={handleLogout}
          >
            <View style={styles.settingLeft}>
              <Ionicons name="log-out" size={24} color="#ff4444" />
              <Text style={[styles.settingTitle, { color: '#ff4444', marginLeft: 15 }]}>
                Logout
              </Text>
            </View>
          </TouchableOpacity>
        </View>

        <View style={styles.footer}>
          <Text style={[styles.footerText, { color: isDark ? '#666' : '#999' }]}>
            Frontend2 © 2024
          </Text>
        </View>
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  scrollView: {
    flex: 1,
  },
  scrollContent: {
    paddingBottom: 50,
  },
  section: {
    marginHorizontal: 16,
    marginBottom: 20,
    borderRadius: 12,
    overflow: 'hidden',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  sectionHeader: {
    fontSize: 13,
    fontWeight: '600',
    marginHorizontal: 16,
    marginTop: 20,
    marginBottom: 8,
    textTransform: 'uppercase',
  },
  profileSection: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 20,
  },
  avatarContainer: {
    width: 50,
    height: 50,
    borderRadius: 25,
    backgroundColor: '#e3f2fd',
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: 15,
  },
  profileInfo: {
    flex: 1,
  },
  profileName: {
    fontSize: 18,
    fontWeight: '600',
    marginBottom: 4,
  },
  profileEmail: {
    fontSize: 14,
  },
  settingItem: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 20,
    paddingVertical: 16,
    borderBottomWidth: StyleSheet.hairlineWidth,
    borderBottomColor: '#e0e0e0',
  },
  settingLeft: {
    flexDirection: 'row',
    alignItems: 'center',
    flex: 1,
  },
  settingText: {
    marginLeft: 15,
    flex: 1,
  },
  settingTitle: {
    fontSize: 16,
    fontWeight: '500',
    marginBottom: 2,
  },
  settingSubtitle: {
    fontSize: 14,
  },
  logoutItem: {
    borderBottomWidth: 0,
  },
  footer: {
    alignItems: 'center',
    paddingVertical: 20,
  },
  footerText: {
    fontSize: 12,
  },
});

export default SettingsScreen;