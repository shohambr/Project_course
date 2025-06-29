import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

const CheckoutScreen: React.FC = () => (
  <View style={styles.container}>
    <Text style={styles.text}>Checkout Screen - Coming Soon!</Text>
  </View>
);

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', alignItems: 'center' },
  text: { fontSize: 18, fontWeight: '500' },
});

export default CheckoutScreen;