import React, { useState, useEffect, useCallback } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Modal,
  TextInput,
  Alert,
  Share,
  Dimensions,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { debugLogger, LogEntry } from '../services/debugLogger';

interface DebugPanelProps {
  visible: boolean;
  onClose: () => void;
}

const { width: screenWidth, height: screenHeight } = Dimensions.get('window');

const DebugPanel: React.FC<DebugPanelProps> = ({ visible, onClose }) => {
  const [logs, setLogs] = useState<LogEntry[]>([]);
  const [filteredLogs, setFilteredLogs] = useState<LogEntry[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<string>('ALL');
  const [selectedLevel, setSelectedLevel] = useState<string>('ALL');
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [autoScroll, setAutoScroll] = useState(true);
  const [maxLogs, setMaxLogs] = useState(100);

  // Refresh logs every second
  useEffect(() => {
    if (visible) {
      const interval = setInterval(() => {
        const currentLogs = debugLogger.getLogs();
        setLogs(currentLogs.slice(-maxLogs));
      }, 1000);

      return () => clearInterval(interval);
    }
  }, [visible, maxLogs]);

  // Filter logs based on category, level, and search query
  useEffect(() => {
    let filtered = logs;

    // Filter by category
    if (selectedCategory !== 'ALL') {
      filtered = filtered.filter(log => log.category === selectedCategory);
    }

    // Filter by level
    if (selectedLevel !== 'ALL') {
      filtered = filtered.filter(log => log.level === selectedLevel);
    }

    // Filter by search query
    if (searchQuery.trim()) {
      const query = searchQuery.toLowerCase();
      filtered = filtered.filter(log => 
        log.action.toLowerCase().includes(query) ||
        log.category.toLowerCase().includes(query) ||
        (log.details && JSON.stringify(log.details).toLowerCase().includes(query))
      );
    }

    setFilteredLogs(filtered);
  }, [logs, selectedCategory, selectedLevel, searchQuery]);

  const getLogIcon = (level: string) => {
    switch (level) {
      case 'ERROR': return '🚨';
      case 'WARN': return '⚠️';
      case 'INFO': return 'ℹ️';
      case 'DEBUG': return '🔍';
      default: return '📝';
    }
  };

  const getLogColor = (level: string) => {
    switch (level) {
      case 'ERROR': return '#ff4444';
      case 'WARN': return '#ffaa00';
      case 'INFO': return '#007AFF';
      case 'DEBUG': return '#888';
      default: return '#000';
    }
  };

  const getCategoryColor = (category: string) => {
    const colors: { [key: string]: string } = {
      'USER_INTERACTION': '#34C759',
      'API': '#007AFF',
      'REDUX': '#FF9500',
      'NAVIGATION': '#AF52DE',
      'AUTH': '#FF3B30',
      'SYSTEM': '#8E8E93',
      'PERFORMANCE': '#FFCC00',
      'SCREEN_TIME': '#30A46C',
      'ANIMATION': '#FF6B6B',
      'FORM_VALIDATION': '#4ECDC4',
    };
    return colors[category] || '#666';
  };

  const formatTimestamp = (timestamp: string) => {
    return new Date(timestamp).toLocaleTimeString();
  };

  const handleExportLogs = async () => {
    try {
      const logData = {
        exportTime: new Date().toISOString(),
        totalLogs: logs.length,
        filteredLogs: filteredLogs.length,
        sessionSummary: debugLogger.getSessionSummary(),
        logs: filteredLogs,
      };

      const content = JSON.stringify(logData, null, 2);
      
      await Share.share({
        message: content,
        title: `Debug Logs Export - ${new Date().toLocaleDateString()}`,
      });
    } catch (error) {
      Alert.alert('Export Error', 'Failed to export logs');
    }
  };

  const handleClearLogs = () => {
    Alert.alert(
      'Clear Logs',
      'Are you sure you want to clear all logs? This action cannot be undone.',
      [
        { text: 'Cancel', style: 'cancel' },
        { 
          text: 'Clear', 
          style: 'destructive',
          onPress: () => {
            debugLogger.clearLogs();
            setLogs([]);
          }
        },
      ]
    );
  };

  const getUniqueCategories = () => {
    const categories = new Set(logs.map(log => log.category));
    return ['ALL', ...Array.from(categories)];
  };

  const getSessionStats = () => {
    const summary = debugLogger.getSessionSummary();
    return {
      totalLogs: logs.length,
      errors: logs.filter(log => log.level === 'ERROR').length,
      warnings: logs.filter(log => log.level === 'WARN').length,
      userInteractions: logs.filter(log => log.category === 'USER_INTERACTION').length,
      apiCalls: logs.filter(log => log.category === 'API').length,
      sessionDuration: summary.sessionDuration ? `${(summary.sessionDuration / 1000 / 60).toFixed(1)}m` : '0m',
    };
  };

  const renderLogItem = (log: LogEntry, index: number) => (
    <View key={`${log.timestamp}-${index}`} style={styles.logItem}>
      <View style={styles.logHeader}>
        <Text style={styles.logIcon}>{getLogIcon(log.level)}</Text>
        <Text style={[styles.logLevel, { color: getLogColor(log.level) }]}>
          {log.level}
        </Text>
        <Text style={[styles.logCategory, { color: getCategoryColor(log.category) }]}>
          {log.category}
        </Text>
        <Text style={styles.logTime}>{formatTimestamp(log.timestamp)}</Text>
      </View>
      
      <Text style={styles.logAction}>{log.action}</Text>
      
      {log.screenName && (
        <Text style={styles.logScreen}>📱 {log.screenName}</Text>
      )}
      
      {log.details && (
        <View style={styles.logDetails}>
          <Text style={styles.logDetailsText}>
            {JSON.stringify(log.details, null, 2)}
          </Text>
        </View>
      )}
    </View>
  );

  const stats = getSessionStats();

  return (
    <Modal
      visible={visible}
      animationType="slide"
      presentationStyle="fullScreen"
    >
      <View style={styles.container}>
        {/* Header */}
        <View style={styles.header}>
          <TouchableOpacity onPress={onClose} style={styles.closeButton}>
            <Ionicons name="close" size={24} color="#007AFF" />
          </TouchableOpacity>
          
          <Text style={styles.title}>Debug Panel</Text>
          
          <TouchableOpacity onPress={handleExportLogs} style={styles.exportButton}>
            <Ionicons name="share-outline" size={20} color="#007AFF" />
          </TouchableOpacity>
        </View>

        {/* Stats Bar */}
        <View style={styles.statsContainer}>
          <View style={styles.stat}>
            <Text style={styles.statNumber}>{stats.totalLogs}</Text>
            <Text style={styles.statLabel}>Logs</Text>
          </View>
          <View style={styles.stat}>
            <Text style={[styles.statNumber, { color: '#ff4444' }]}>{stats.errors}</Text>
            <Text style={styles.statLabel}>Errors</Text>
          </View>
          <View style={styles.stat}>
            <Text style={[styles.statNumber, { color: '#ffaa00' }]}>{stats.warnings}</Text>
            <Text style={styles.statLabel}>Warnings</Text>
          </View>
          <View style={styles.stat}>
            <Text style={[styles.statNumber, { color: '#34C759' }]}>{stats.userInteractions}</Text>
            <Text style={styles.statLabel}>Actions</Text>
          </View>
          <View style={styles.stat}>
            <Text style={[styles.statNumber, { color: '#007AFF' }]}>{stats.apiCalls}</Text>
            <Text style={styles.statLabel}>API</Text>
          </View>
          <View style={styles.stat}>
            <Text style={styles.statNumber}>{stats.sessionDuration}</Text>
            <Text style={styles.statLabel}>Session</Text>
          </View>
        </View>

        {/* Controls */}
        <View style={styles.controls}>
          <TextInput
            style={styles.searchInput}
            placeholder="Search logs..."
            value={searchQuery}
            onChangeText={setSearchQuery}
            placeholderTextColor="#999"
          />
          
          <ScrollView horizontal showsHorizontalScrollIndicator={false} style={styles.filterContainer}>
            {getUniqueCategories().map(category => (
              <TouchableOpacity
                key={category}
                style={[
                  styles.filterButton,
                  selectedCategory === category && styles.activeFilterButton
                ]}
                onPress={() => setSelectedCategory(category)}
              >
                <Text style={[
                  styles.filterButtonText,
                  selectedCategory === category && styles.activeFilterButtonText
                ]}>
                  {category}
                </Text>
              </TouchableOpacity>
            ))}
          </ScrollView>

          <View style={styles.actionButtons}>
            <TouchableOpacity
              style={[styles.actionButton, autoScroll && styles.activeActionButton]}
              onPress={() => setAutoScroll(!autoScroll)}
            >
              <Ionicons 
                name={autoScroll ? "play" : "pause"} 
                size={16} 
                color={autoScroll ? "#fff" : "#007AFF"} 
              />
              <Text style={[
                styles.actionButtonText,
                autoScroll && styles.activeActionButtonText
              ]}>
                Auto
              </Text>
            </TouchableOpacity>
            
            <TouchableOpacity
              style={styles.actionButton}
              onPress={handleClearLogs}
            >
              <Ionicons name="trash-outline" size={16} color="#ff4444" />
              <Text style={[styles.actionButtonText, { color: '#ff4444' }]}>
                Clear
              </Text>
            </TouchableOpacity>
          </View>
        </View>

        {/* Logs List */}
        <ScrollView 
          style={styles.logsList}
          showsVerticalScrollIndicator={true}
          scrollToEnd={autoScroll}
        >
          {filteredLogs.length === 0 ? (
            <View style={styles.noLogsContainer}>
              <Text style={styles.noLogsText}>No logs found</Text>
              <Text style={styles.noLogsSubtext}>
                {searchQuery ? 'Try adjusting your search or filters' : 'Start using the app to see logs'}
              </Text>
            </View>
          ) : (
            filteredLogs.map((log, index) => renderLogItem(log, index))
          )}
        </ScrollView>
      </View>
    </Modal>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 16,
    paddingTop: 50,
    paddingBottom: 16,
    backgroundColor: '#fff',
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
  },
  closeButton: {
    padding: 8,
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#000',
  },
  exportButton: {
    padding: 8,
  },
  statsContainer: {
    flexDirection: 'row',
    backgroundColor: '#fff',
    paddingVertical: 12,
    paddingHorizontal: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
  },
  stat: {
    flex: 1,
    alignItems: 'center',
  },
  statNumber: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#000',
  },
  statLabel: {
    fontSize: 12,
    color: '#666',
    marginTop: 2,
  },
  controls: {
    backgroundColor: '#fff',
    paddingHorizontal: 16,
    paddingVertical: 12,
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
  },
  searchInput: {
    height: 40,
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 8,
    paddingHorizontal: 12,
    marginBottom: 12,
    backgroundColor: '#f9f9f9',
  },
  filterContainer: {
    marginBottom: 12,
  },
  filterButton: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    marginRight: 8,
    borderRadius: 16,
    backgroundColor: '#f0f0f0',
  },
  activeFilterButton: {
    backgroundColor: '#007AFF',
  },
  filterButtonText: {
    fontSize: 12,
    color: '#666',
  },
  activeFilterButtonText: {
    color: '#fff',
  },
  actionButtons: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  actionButton: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 12,
    paddingVertical: 8,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#007AFF',
  },
  activeActionButton: {
    backgroundColor: '#007AFF',
  },
  actionButtonText: {
    fontSize: 12,
    color: '#007AFF',
    marginLeft: 4,
  },
  activeActionButtonText: {
    color: '#fff',
  },
  logsList: {
    flex: 1,
    paddingHorizontal: 16,
  },
  logItem: {
    backgroundColor: '#fff',
    padding: 12,
    marginVertical: 4,
    borderRadius: 8,
    borderLeftWidth: 4,
    borderLeftColor: '#007AFF',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    elevation: 2,
  },
  logHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 8,
  },
  logIcon: {
    fontSize: 16,
    marginRight: 8,
  },
  logLevel: {
    fontSize: 12,
    fontWeight: 'bold',
    marginRight: 8,
    minWidth: 50,
  },
  logCategory: {
    fontSize: 12,
    fontWeight: '600',
    marginRight: 8,
    flex: 1,
  },
  logTime: {
    fontSize: 10,
    color: '#999',
  },
  logAction: {
    fontSize: 14,
    fontWeight: '600',
    color: '#000',
    marginBottom: 4,
  },
  logScreen: {
    fontSize: 12,
    color: '#666',
    marginBottom: 8,
  },
  logDetails: {
    backgroundColor: '#f5f5f5',
    padding: 8,
    borderRadius: 4,
    marginTop: 4,
  },
  logDetailsText: {
    fontSize: 10,
    color: '#333',
    fontFamily: Platform.OS === 'ios' ? 'Courier' : 'monospace',
  },
  noLogsContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingVertical: 40,
  },
  noLogsText: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#666',
  },
  noLogsSubtext: {
    fontSize: 14,
    color: '#999',
    textAlign: 'center',
    marginTop: 8,
  },
});

export default DebugPanel;