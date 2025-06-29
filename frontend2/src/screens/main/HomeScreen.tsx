import React, { useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  RefreshControl,
  Dimensions,
} from 'react-native';
import { useSelector, useDispatch } from 'react-redux';
import { Ionicons } from '@expo/vector-icons';
import { StackNavigationProp } from '@react-navigation/stack';

import { RootState, AppDispatch } from '../../store';
import { fetchStores } from '../../store/slices/storeSlice';
import { fetchProducts } from '../../store/slices/productSlice';
import { fetchCartTotal } from '../../store/slices/cartSlice';
import { RootStackParamList } from '../../types';
import AdminAccessButton from '../../components/AdminAccessButton';
import { logScreenEnter, logScreenLeave, logUserInteraction } from '../../services/debugLogger';

const { width } = Dimensions.get('window');

type HomeScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Home'>;

interface Props {
  navigation: HomeScreenNavigationProp;
}

const HomeScreen: React.FC<Props> = ({ navigation }) => {
  const dispatch = useDispatch<AppDispatch>();
  const { user } = useSelector((state: RootState) => state.auth);
  const { stores, isLoading: storesLoading } = useSelector((state: RootState) => state.stores);
  const { products, isLoading: productsLoading } = useSelector((state: RootState) => state.products);
  const { total, items } = useSelector((state: RootState) => state.cart);
  const theme = useSelector((state: RootState) => state.settings.theme);

  const isDark = theme === 'dark';
  const isLoading = storesLoading || productsLoading;

  useEffect(() => {
    // Log screen enter
    logScreenEnter('HomeScreen', {
      user: user?.username,
      theme: theme,
      storesCount: stores.length,
      productsCount: products.length,
      cartItems: items.length,
      cartTotal: total,
    });

    loadData();

    // Log screen leave on cleanup
    return () => {
      logScreenLeave('HomeScreen');
    };
  }, []);

  const loadData = () => {
    logUserInteraction('DATA_REFRESH', 'HOME_SCREEN', {
      triggeredBy: 'user',
      timestamp: new Date().toISOString(),
    }, 'HomeScreen');

    dispatch(fetchStores());
    dispatch(fetchProducts());
    dispatch(fetchCartTotal());
  };

  const QuickActionCard = ({ 
    icon, 
    title, 
    subtitle, 
    color, 
    onPress 
  }: {
    icon: string;
    title: string;
    subtitle: string;
    color: string;
    onPress: () => void;
  }) => (
    <TouchableOpacity
      style={[styles.quickActionCard, { backgroundColor: isDark ? '#1a1a1a' : '#fff' }]}
      onPress={() => {
        logUserInteraction('QUICK_ACTION_PRESS', title.toUpperCase().replace(' ', '_'), {
          title,
          subtitle,
          color,
        }, 'HomeScreen');
        onPress();
      }}
    >
      <View style={[styles.quickActionIcon, { backgroundColor: color + '20' }]}>
        <Ionicons name={icon as any} size={24} color={color} />
      </View>
      <Text style={[styles.quickActionTitle, { color: isDark ? '#fff' : '#000' }]}>
        {title}
      </Text>
      <Text style={[styles.quickActionSubtitle, { color: isDark ? '#ccc' : '#666' }]}>
        {subtitle}
      </Text>
    </TouchableOpacity>
  );

  const StatCard = ({ 
    label, 
    value, 
    icon, 
    color 
  }: {
    label: string;
    value: string | number;
    icon: string;
    color: string;
  }) => (
    <View style={[styles.statCard, { backgroundColor: isDark ? '#1a1a1a' : '#fff' }]}>
      <View style={styles.statHeader}>
        <View style={[styles.statIcon, { backgroundColor: color + '20' }]}>
          <Ionicons name={icon as any} size={20} color={color} />
        </View>
        <Text style={[styles.statValue, { color: isDark ? '#fff' : '#000' }]}>
          {value}
        </Text>
      </View>
      <Text style={[styles.statLabel, { color: isDark ? '#ccc' : '#666' }]}>
        {label}
      </Text>
    </View>
  );

  return (
    <View style={[styles.container, { backgroundColor: isDark ? '#000' : '#f5f5f5' }]}>
      <ScrollView
        style={styles.scrollView}
        contentContainerStyle={styles.scrollContent}
        refreshControl={
          <RefreshControl
            refreshing={isLoading}
            onRefresh={loadData}
            colors={['#007AFF']}
            tintColor={isDark ? '#fff' : '#000'}
          />
        }
      >
        {/* Header */}
        <View style={styles.header}>
          <View>
            <Text style={[styles.welcomeText, { color: isDark ? '#ccc' : '#666' }]}>
              Welcome back,
            </Text>
            <Text style={[styles.userName, { color: isDark ? '#fff' : '#000' }]}>
              {user?.username || 'Guest'}!
            </Text>
          </View>
          <TouchableOpacity
            style={[styles.profileButton, { backgroundColor: isDark ? '#1a1a1a' : '#fff' }]}
            onPress={() => {
              logUserInteraction('PROFILE_BUTTON_PRESS', 'NAVIGATION', {
                destination: 'Profile',
              }, 'HomeScreen');
              navigation.navigate('Profile');
            }}
          >
            <Ionicons name="person" size={24} color="#007AFF" />
          </TouchableOpacity>
        </View>

        {/* Stats Overview */}
        <View style={styles.statsContainer}>
          <StatCard
            label="Available Stores"
            value={stores.length}
            icon="storefront"
            color="#007AFF"
          />
          <StatCard
            label="Products"
            value={products.length}
            icon="grid"
            color="#34C759"
          />
          <StatCard
            label="Cart Items"
            value={items.length}
            icon="bag"
            color="#FF9500"
          />
          <StatCard
            label="Cart Total"
            value={`$${total.toFixed(2)}`}
            icon="card"
            color="#FF3B30"
          />
        </View>

        {/* Quick Actions */}
        <View style={styles.sectionContainer}>
          <Text style={[styles.sectionTitle, { color: isDark ? '#fff' : '#000' }]}>
            Quick Actions
          </Text>
          <View style={styles.quickActionsGrid}>
            <QuickActionCard
              icon="storefront"
              title="Browse Stores"
              subtitle="Explore all stores"
              color="#007AFF"
              onPress={() => navigation.navigate('Stores')}
            />
            <QuickActionCard
              icon="grid"
              title="View Products"
              subtitle="See all products"
              color="#34C759"
              onPress={() => navigation.navigate('Products')}
            />
            <QuickActionCard
              icon="bag"
              title="Shopping Cart"
              subtitle={`${items.length} items`}
              color="#FF9500"
              onPress={() => navigation.navigate('Cart')}
            />
            <QuickActionCard
              icon="receipt"
              title="Order History"
              subtitle="View past orders"
              color="#AF52DE"
              onPress={() => navigation.navigate('OrderHistory')}
            />
          </View>
        </View>

        {/* Recent Activity */}
        <View style={styles.sectionContainer}>
          <Text style={[styles.sectionTitle, { color: isDark ? '#fff' : '#000' }]}>
            Recent Activity
          </Text>
          <View style={[styles.activityCard, { backgroundColor: isDark ? '#1a1a1a' : '#fff' }]}>
            <View style={styles.activityItem}>
              <View style={[styles.activityIcon, { backgroundColor: '#34C759' + '20' }]}>
                <Ionicons name="checkmark-circle" size={20} color="#34C759" />
              </View>
              <View style={styles.activityContent}>
                <Text style={[styles.activityTitle, { color: isDark ? '#fff' : '#000' }]}>
                  Welcome to Frontend2!
                </Text>
                <Text style={[styles.activitySubtitle, { color: isDark ? '#ccc' : '#666' }]}>
                  Start exploring stores and products
                </Text>
              </View>
            </View>
          </View>
        </View>

        {/* Featured Section */}
        <View style={styles.sectionContainer}>
          <Text style={[styles.sectionTitle, { color: isDark ? '#fff' : '#000' }]}>
            Featured
          </Text>
          <TouchableOpacity
            style={[styles.featuredCard, { backgroundColor: isDark ? '#1a1a1a' : '#fff' }]}
            onPress={() => {
              logUserInteraction('FEATURED_CARD_PRESS', 'DISCOVER_STORES', {
                destination: 'Stores',
              }, 'HomeScreen');
              navigation.navigate('Stores');
            }}
          >
            <View style={styles.featuredContent}>
              <View style={[styles.featuredIcon, { backgroundColor: '#007AFF' + '20' }]}>
                <Ionicons name="star" size={24} color="#007AFF" />
              </View>
              <View style={styles.featuredText}>
                <Text style={[styles.featuredTitle, { color: isDark ? '#fff' : '#000' }]}>
                  Discover New Stores
                </Text>
                <Text style={[styles.featuredSubtitle, { color: isDark ? '#ccc' : '#666' }]}>
                  Find amazing products from top-rated stores
                </Text>
              </View>
              <Ionicons name="chevron-forward" size={20} color={isDark ? '#ccc' : '#999'} />
            </View>
          </TouchableOpacity>
        </View>
      </ScrollView>

      {/* Admin Access Button - Floating button for admin terminal access */}
      <AdminAccessButton />
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
    paddingBottom: 20,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 20,
    paddingTop: 20,
    paddingBottom: 20,
  },
  welcomeText: {
    fontSize: 16,
  },
  userName: {
    fontSize: 24,
    fontWeight: 'bold',
    marginTop: 4,
  },
  profileButton: {
    width: 44,
    height: 44,
    borderRadius: 22,
    justifyContent: 'center',
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  statsContainer: {
    flexDirection: 'row',
    paddingHorizontal: 16,
    marginBottom: 20,
  },
  statCard: {
    flex: 1,
    marginHorizontal: 4,
    padding: 16,
    borderRadius: 12,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  statHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 8,
  },
  statIcon: {
    width: 32,
    height: 32,
    borderRadius: 16,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: 8,
  },
  statValue: {
    fontSize: 18,
    fontWeight: 'bold',
  },
  statLabel: {
    fontSize: 12,
    textAlign: 'center',
  },
  sectionContainer: {
    paddingHorizontal: 20,
    marginBottom: 24,
  },
  sectionTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 16,
  },
  quickActionsGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
  },
  quickActionCard: {
    width: (width - 60) / 2,
    padding: 20,
    borderRadius: 12,
    marginBottom: 12,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  quickActionIcon: {
    width: 48,
    height: 48,
    borderRadius: 24,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 12,
  },
  quickActionTitle: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 4,
    textAlign: 'center',
  },
  quickActionSubtitle: {
    fontSize: 12,
    textAlign: 'center',
  },
  activityCard: {
    borderRadius: 12,
    padding: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  activityItem: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  activityIcon: {
    width: 40,
    height: 40,
    borderRadius: 20,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: 12,
  },
  activityContent: {
    flex: 1,
  },
  activityTitle: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 2,
  },
  activitySubtitle: {
    fontSize: 14,
  },
  featuredCard: {
    borderRadius: 12,
    padding: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  featuredContent: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  featuredIcon: {
    width: 48,
    height: 48,
    borderRadius: 24,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: 16,
  },
  featuredText: {
    flex: 1,
  },
  featuredTitle: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 4,
  },
  featuredSubtitle: {
    fontSize: 14,
  },
});

export default HomeScreen;