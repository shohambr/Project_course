import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { useSelector } from 'react-redux';
import { RootState } from '../../store';

const StoresScreen: React.FC = () => {
  const theme = useSelector((state: RootState) => state.settings.theme);
  const isDark = theme === 'dark';

  return (
    <View style={[styles.container, { backgroundColor: isDark ? '#000' : '#f5f5f5' }]}>
      <Text style={[styles.text, { color: isDark ? '#fff' : '#000' }]}>
        Stores Screen - Coming Soon!
      </Text>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  text: {
    fontSize: 18,
    fontWeight: '500',
  },
});

export default StoresScreen;