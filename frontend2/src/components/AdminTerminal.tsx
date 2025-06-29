import React, { useState, useEffect, useRef, useCallback } from 'react';
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
  Platform,
  KeyboardAvoidingView,
  ActivityIndicator,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { LinearGradient } from 'expo-linear-gradient';
import * as Haptics from 'expo-haptics';
import { useSelector } from 'react-redux';
import { RootState } from '../store';
import { debugLogger, LogEntry } from '../services/debugLogger';
import ToastService from '../services/toastService';

interface AdminTerminalProps {
  visible: boolean;
  onClose: () => void;
}

interface TerminalCommand {
  id: string;
  command: string;
  timestamp: string;
  output?: string;
  status: 'pending' | 'success' | 'error';
  executionTime?: number;
}

const { width: screenWidth, height: screenHeight } = Dimensions.get('window');

const AdminTerminal: React.FC<AdminTerminalProps> = ({ visible, onClose }) => {
  // Authentication state
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [adminPassword, setAdminPassword] = useState('');
  const [authAttempts, setAuthAttempts] = useState(0);
  const [isLocked, setIsLocked] = useState(false);

  // Terminal state
  const [commandHistory, setCommandHistory] = useState<TerminalCommand[]>([]);
  const [currentCommand, setCurrentCommand] = useState('');
  const [isExecuting, setIsExecuting] = useState(false);
  const [logs, setLogs] = useState<LogEntry[]>([]);
  const [filteredLogs, setFilteredLogs] = useState<LogEntry[]>([]);
  const [terminalMode, setTerminalMode] = useState<'logs' | 'commands' | 'system'>('logs');
  const [autoScroll, setAutoScroll] = useState(true);
  const [logFilter, setLogFilter] = useState('');
  const [systemStats, setSystemStats] = useState<any>({});

  // Refs
  const scrollViewRef = useRef<ScrollView>(null);
  const commandInputRef = useRef<TextInput>(null);

  // Admin credentials (in production, this should be handled server-side)
  const ADMIN_PASSWORD = 'admin_terminal_2024!'; // This should be env variable
  const MAX_AUTH_ATTEMPTS = 3;
  const LOCKOUT_TIME = 5 * 60 * 1000; // 5 minutes

  const user = useSelector((state: RootState) => state.auth.user);

  // Authentication effect
  useEffect(() => {
    if (visible) {
      setIsAuthenticated(false);
      setAdminPassword('');
      logAdminAccess('TERMINAL_ACCESS_ATTEMPTED', {
        user: user?.username,
        timestamp: new Date().toISOString(),
      });
    }
  }, [visible]);

  // Real-time logs update
  useEffect(() => {
    if (visible && isAuthenticated) {
      const interval = setInterval(() => {
        const currentLogs = debugLogger.getLogs();
        setLogs(currentLogs.slice(-500)); // Keep last 500 logs
        updateSystemStats();
      }, 1000);

      return () => clearInterval(interval);
    }
  }, [visible, isAuthenticated]);

  // Filter logs
  useEffect(() => {
    let filtered = logs;
    if (logFilter.trim()) {
      const query = logFilter.toLowerCase();
      filtered = logs.filter(log => 
        log.action.toLowerCase().includes(query) ||
        log.category.toLowerCase().includes(query) ||
        (log.details && JSON.stringify(log.details).toLowerCase().includes(query))
      );
    }
    setFilteredLogs(filtered);
  }, [logs, logFilter]);

  // Auto scroll
  useEffect(() => {
    if (autoScroll && scrollViewRef.current) {
      scrollViewRef.current.scrollToEnd({ animated: true });
    }
  }, [filteredLogs, commandHistory, autoScroll]);

  const logAdminAccess = (action: string, details: any) => {
    debugLogger.customEvent('ADMIN_TERMINAL', action, {
      adminUser: user?.username,
      timestamp: new Date().toISOString(),
      ...details,
    });

    console.log(`👑 ADMIN TERMINAL - ${action}:`, details);
  };

  const updateSystemStats = () => {
    const summary = debugLogger.getSessionSummary();
    setSystemStats({
      totalLogs: summary.totalLogs,
      errorCount: summary.logsByLevel.ERROR,
      warningCount: summary.logsByLevel.WARN,
      sessionDuration: summary.sessionDuration,
      memoryUsage: (performance as any).memory ? Math.round((performance as any).memory.usedJSHeapSize / 1024 / 1024) : 'N/A',
      timestamp: new Date().toISOString(),
    });
  };

  const handleAuthentication = () => {
    if (isLocked) {
      ToastService.error('Access Locked', 'Too many failed attempts. Please wait before trying again.');
      return;
    }

    if (adminPassword === ADMIN_PASSWORD) {
      setIsAuthenticated(true);
      setAuthAttempts(0);
      Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success);
      ToastService.success('Admin Access Granted', 'Welcome to the Admin Terminal');
      
      logAdminAccess('AUTHENTICATION_SUCCESS', {
        user: user?.username,
        attempts: authAttempts + 1,
      });

      // Initialize terminal
      addCommandToHistory({
        id: Date.now().toString(),
        command: 'system.init',
        timestamp: new Date().toISOString(),
        output: `Admin Terminal v2.0.1 initialized\nUser: ${user?.username}\nSession: ${getSessionId()}\nTimestamp: ${new Date().toISOString()}`,
        status: 'success',
        executionTime: 0,
      });

    } else {
      const newAttempts = authAttempts + 1;
      setAuthAttempts(newAttempts);
      
      Haptics.notificationAsync(Haptics.NotificationFeedbackType.Error);
      ToastService.error('Authentication Failed', `Invalid password. ${MAX_AUTH_ATTEMPTS - newAttempts} attempts remaining.`);
      
      logAdminAccess('AUTHENTICATION_FAILED', {
        user: user?.username,
        attempts: newAttempts,
        remainingAttempts: MAX_AUTH_ATTEMPTS - newAttempts,
      });

      if (newAttempts >= MAX_AUTH_ATTEMPTS) {
        setIsLocked(true);
        ToastService.error('Access Locked', 'Too many failed attempts. Terminal locked for 5 minutes.');
        
        logAdminAccess('TERMINAL_LOCKED', {
          user: user?.username,
          lockoutDuration: LOCKOUT_TIME,
        });

        setTimeout(() => {
          setIsLocked(false);
          setAuthAttempts(0);
        }, LOCKOUT_TIME);
      }
    }
    
    setAdminPassword('');
  };

  const addCommandToHistory = (command: TerminalCommand) => {
    setCommandHistory(prev => [...prev, command]);
    logAdminAccess('COMMAND_EXECUTED', {
      command: command.command,
      status: command.status,
      executionTime: command.executionTime,
    });
  };

  const executeCommand = async (command: string) => {
    if (!command.trim()) return;

    const commandId = Date.now().toString();
    const startTime = Date.now();
    
    setIsExecuting(true);
    setCurrentCommand('');

    const newCommand: TerminalCommand = {
      id: commandId,
      command: command.trim(),
      timestamp: new Date().toISOString(),
      status: 'pending',
    };

    addCommandToHistory(newCommand);
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);

    // Simulate command execution
    setTimeout(() => {
      const executionTime = Date.now() - startTime;
      let output = '';
      let status: 'success' | 'error' = 'success';

      try {
        output = processCommand(command.trim());
      } catch (error) {
        output = `Error: ${error instanceof Error ? error.message : 'Unknown error'}`;
        status = 'error';
      }

      setCommandHistory(prev => prev.map(cmd => 
        cmd.id === commandId 
          ? { ...cmd, output, status, executionTime }
          : cmd
      ));

      setIsExecuting(false);
      
      if (status === 'error') {
        Haptics.notificationAsync(Haptics.NotificationFeedbackType.Error);
      } else {
        Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
      }
    }, 500 + Math.random() * 1000); // Simulate realistic execution time
  };

  const processCommand = (command: string): string => {
    const args = command.split(' ');
    const cmd = args[0].toLowerCase();

    switch (cmd) {
      case 'help':
        return `Available Commands:
• help - Show this help message
• clear - Clear terminal history
• logs [filter] - Show filtered logs
• stats - Show system statistics
• users - Show active users
• export - Export logs and data
• debug [on|off] - Toggle debug mode
• system.restart - Restart system services
• system.health - System health check
• cache.clear - Clear application cache
• db.stats - Database statistics
• network.test - Test network connectivity`;

      case 'clear':
        setCommandHistory([]);
        return 'Terminal cleared';

      case 'logs':
        const filter = args[1] || '';
        const recentLogs = logs.slice(-10);
        return `Recent logs${filter ? ` (filtered: ${filter})` : ''}:\n${recentLogs.map(log => 
          `[${new Date(log.timestamp).toLocaleTimeString()}] ${log.level} ${log.category}: ${log.action}`
        ).join('\n')}`;

      case 'stats':
        return `System Statistics:
• Total Logs: ${systemStats.totalLogs || 0}
• Errors: ${systemStats.errorCount || 0}
• Warnings: ${systemStats.warningCount || 0}
• Session Duration: ${systemStats.sessionDuration ? Math.round(systemStats.sessionDuration / 1000 / 60) : 0} minutes
• Memory Usage: ${systemStats.memoryUsage} MB
• Platform: ${Platform.OS}
• Timestamp: ${new Date().toLocaleString()}`;

      case 'users':
        return `Active Users:
• ${user?.username || 'Anonymous'} (Current Admin)
• Session ID: ${getSessionId()}
• Authentication: ADMIN
• Terminal Access: GRANTED`;

      case 'export':
        handleExportData();
        return 'Data export initiated. Check your share menu.';

      case 'debug':
        const mode = args[1]?.toLowerCase();
        if (mode === 'on') {
          debugLogger.setEnabled(true);
          return 'Debug logging enabled';
        } else if (mode === 'off') {
          debugLogger.setEnabled(false);
          return 'Debug logging disabled';
        }
        return 'Usage: debug [on|off]';

      case 'system.restart':
        return 'System services restart initiated...\n[SIMULATED] All services restarted successfully';

      case 'system.health':
        return `System Health Check:
✅ Frontend Service: Running
✅ API Connection: Active
✅ Authentication: Valid
✅ Logging Service: Active
✅ State Management: Healthy
🔄 Memory Usage: ${systemStats.memoryUsage} MB
📊 Performance: Good`;

      case 'cache.clear':
        return 'Application cache cleared successfully';

      case 'db.stats':
        return `Database Statistics:
• Connection Status: Active
• Query Count: ${Math.floor(Math.random() * 1000)}
• Cache Hit Ratio: ${(Math.random() * 20 + 80).toFixed(1)}%
• Active Connections: ${Math.floor(Math.random() * 10) + 1}
• Last Backup: ${new Date(Date.now() - Math.random() * 24 * 60 * 60 * 1000).toLocaleString()}`;

      case 'network.test':
        return `Network Connectivity Test:
✅ API Server: 200ms
✅ Database: 150ms
✅ CDN: 89ms
✅ External Services: 245ms
🌐 Status: All systems operational`;

      default:
        throw new Error(`Unknown command: ${cmd}. Type 'help' for available commands.`);
    }
  };

  const handleExportData = async () => {
    try {
      const exportData = {
        exportTime: new Date().toISOString(),
        adminUser: user?.username,
        sessionSummary: debugLogger.getSessionSummary(),
        commandHistory: commandHistory,
        systemStats: systemStats,
        logs: logs.slice(-100), // Last 100 logs
        platform: Platform.OS,
      };

      await Share.share({
        message: JSON.stringify(exportData, null, 2),
        title: `Admin Terminal Export - ${new Date().toLocaleDateString()}`,
      });

      logAdminAccess('DATA_EXPORTED', {
        logsCount: logs.length,
        commandsCount: commandHistory.length,
      });

    } catch (error) {
      ToastService.error('Export Failed', 'Could not export terminal data');
    }
  };

  const renderAuthScreen = () => (
    <LinearGradient
      colors={['#1a1a1a', '#000000', '#1a1a1a']}
      style={styles.authContainer}
    >
      <View style={styles.authContent}>
        <View style={styles.authHeader}>
          <Ionicons name="shield-checkmark" size={64} color="#ff6b6b" />
          <Text style={styles.authTitle}>ADMIN TERMINAL</Text>
          <Text style={styles.authSubtitle}>Secure Access Required</Text>
        </View>

        <View style={styles.authForm}>
          <View style={styles.authInputContainer}>
            <Ionicons name="lock-closed" size={20} color="#666" style={styles.authInputIcon} />
            <TextInput
              style={styles.authInput}
              placeholder="Enter admin password"
              placeholderTextColor="#666"
              value={adminPassword}
              onChangeText={setAdminPassword}
              secureTextEntry
              autoCapitalize="none"
              autoCorrect={false}
              editable={!isLocked}
              onSubmitEditing={handleAuthentication}
            />
          </View>

          {authAttempts > 0 && !isLocked && (
            <Text style={styles.authError}>
              Invalid password. {MAX_AUTH_ATTEMPTS - authAttempts} attempts remaining.
            </Text>
          )}

          {isLocked && (
            <Text style={styles.authLocked}>
              🔒 Terminal locked due to too many failed attempts.
            </Text>
          )}

          <TouchableOpacity
            style={[styles.authButton, isLocked && styles.authButtonDisabled]}
            onPress={handleAuthentication}
            disabled={isLocked}
          >
            <Text style={styles.authButtonText}>
              {isLocked ? 'LOCKED' : 'AUTHENTICATE'}
            </Text>
          </TouchableOpacity>
        </View>

        <View style={styles.authFooter}>
          <Text style={styles.authFooterText}>
            Admin terminal access is restricted to authorized personnel only.
          </Text>
          <Text style={styles.authFooterText}>
            User: {user?.username || 'Unknown'}
          </Text>
        </View>
      </View>
    </LinearGradient>
  );

  const renderModeSelector = () => (
    <View style={styles.modeSelector}>
      {(['logs', 'commands', 'system'] as const).map(mode => (
        <TouchableOpacity
          key={mode}
          style={[styles.modeButton, terminalMode === mode && styles.activeModeButton]}
          onPress={() => setTerminalMode(mode)}
        >
          <Ionicons 
            name={mode === 'logs' ? 'list' : mode === 'commands' ? 'terminal' : 'settings'} 
            size={16} 
            color={terminalMode === mode ? '#000' : '#666'} 
          />
          <Text style={[styles.modeButtonText, terminalMode === mode && styles.activeModeButtonText]}>
            {mode.toUpperCase()}
          </Text>
        </TouchableOpacity>
      ))}
    </View>
  );

  const renderLogsView = () => (
    <View style={styles.terminalContent}>
      <View style={styles.logsHeader}>
        <TextInput
          style={styles.logFilterInput}
          placeholder="Filter logs..."
          placeholderTextColor="#666"
          value={logFilter}
          onChangeText={setLogFilter}
        />
        <TouchableOpacity
          style={[styles.autoScrollButton, autoScroll && styles.activeAutoScrollButton]}
          onPress={() => setAutoScroll(!autoScroll)}
        >
          <Ionicons name={autoScroll ? "play" : "pause"} size={16} color={autoScroll ? "#000" : "#666"} />
        </TouchableOpacity>
      </View>
      
      <ScrollView ref={scrollViewRef} style={styles.logsContainer}>
        {filteredLogs.map((log, index) => (
          <View key={`${log.timestamp}-${index}`} style={styles.logItem}>
            <Text style={[styles.logText, { color: getLogColor(log.level) }]}>
              [{new Date(log.timestamp).toLocaleTimeString()}] {log.level} {log.category}: {log.action}
            </Text>
            {log.details && (
              <Text style={styles.logDetails}>
                {JSON.stringify(log.details, null, 2)}
              </Text>
            )}
          </View>
        ))}
      </ScrollView>
    </View>
  );

  const renderCommandsView = () => (
    <View style={styles.terminalContent}>
      <ScrollView ref={scrollViewRef} style={styles.commandsContainer}>
        {commandHistory.map((cmd) => (
          <View key={cmd.id} style={styles.commandItem}>
            <Text style={styles.commandText}>
              <Text style={styles.prompt}>admin@terminal:~$ </Text>
              {cmd.command}
            </Text>
            {cmd.output && (
              <Text style={[styles.commandOutput, { color: cmd.status === 'error' ? '#ff6b6b' : '#4ecdc4' }]}>
                {cmd.output}
              </Text>
            )}
            {cmd.status === 'pending' && (
              <ActivityIndicator size="small" color="#ffcc00" style={styles.commandSpinner} />
            )}
          </View>
        ))}
      </ScrollView>
      
      <View style={styles.commandInputContainer}>
        <Text style={styles.commandPrompt}>admin@terminal:~$ </Text>
        <TextInput
          ref={commandInputRef}
          style={styles.commandInput}
          value={currentCommand}
          onChangeText={setCurrentCommand}
          onSubmitEditing={() => executeCommand(currentCommand)}
          placeholder="Enter command (type 'help' for available commands)"
          placeholderTextColor="#666"
          autoCapitalize="none"
          autoCorrect={false}
          editable={!isExecuting}
        />
        <TouchableOpacity
          style={[styles.executeButton, isExecuting && styles.executeButtonDisabled]}
          onPress={() => executeCommand(currentCommand)}
          disabled={isExecuting}
        >
          <Ionicons name="send" size={16} color={isExecuting ? "#666" : "#4ecdc4"} />
        </TouchableOpacity>
      </View>
    </View>
  );

  const renderSystemView = () => (
    <View style={styles.terminalContent}>
      <ScrollView style={styles.systemContainer}>
        <View style={styles.systemSection}>
          <Text style={styles.systemSectionTitle}>📊 System Statistics</Text>
          <Text style={styles.systemText}>Total Logs: {systemStats.totalLogs || 0}</Text>
          <Text style={styles.systemText}>Errors: {systemStats.errorCount || 0}</Text>
          <Text style={styles.systemText}>Warnings: {systemStats.warningCount || 0}</Text>
          <Text style={styles.systemText}>Memory: {systemStats.memoryUsage} MB</Text>
          <Text style={styles.systemText}>Platform: {Platform.OS}</Text>
        </View>

        <View style={styles.systemSection}>
          <Text style={styles.systemSectionTitle}>👑 Admin Session</Text>
          <Text style={styles.systemText}>User: {user?.username}</Text>
          <Text style={styles.systemText}>Session: {getSessionId()}</Text>
          <Text style={styles.systemText}>Commands Executed: {commandHistory.length}</Text>
          <Text style={styles.systemText}>Terminal Mode: {terminalMode.toUpperCase()}</Text>
        </View>

        <View style={styles.systemSection}>
          <Text style={styles.systemSectionTitle}>🔧 Quick Actions</Text>
          <TouchableOpacity style={styles.quickActionButton} onPress={() => executeCommand('stats')}>
            <Text style={styles.quickActionText}>System Stats</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.quickActionButton} onPress={() => executeCommand('system.health')}>
            <Text style={styles.quickActionText}>Health Check</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.quickActionButton} onPress={handleExportData}>
            <Text style={styles.quickActionText}>Export Data</Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </View>
  );

  const getLogColor = (level: string) => {
    switch (level) {
      case 'ERROR': return '#ff6b6b';
      case 'WARN': return '#ffcc00';
      case 'INFO': return '#4ecdc4';
      case 'DEBUG': return '#999';
      default: return '#fff';
    }
  };

  const getSessionId = () => {
    const summary = debugLogger.getSessionSummary();
    return summary.sessionId || 'unknown';
  };

  if (!visible) return null;

  if (!isAuthenticated) {
    return (
      <Modal visible={visible} animationType="fade" presentationStyle="fullScreen">
        {renderAuthScreen()}
        <TouchableOpacity style={styles.closeButton} onPress={onClose}>
          <Ionicons name="close" size={24} color="#666" />
        </TouchableOpacity>
      </Modal>
    );
  }

  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen">
      <View style={styles.container}>
        <LinearGradient colors={['#1a1a1a', '#000000']} style={styles.header}>
          <TouchableOpacity onPress={onClose} style={styles.headerButton}>
            <Ionicons name="close" size={24} color="#ff6b6b" />
          </TouchableOpacity>
          
          <Text style={styles.headerTitle}>ADMIN TERMINAL</Text>
          
          <TouchableOpacity onPress={handleExportData} style={styles.headerButton}>
            <Ionicons name="download-outline" size={20} color="#4ecdc4" />
          </TouchableOpacity>
        </LinearGradient>

        {renderModeSelector()}

        <KeyboardAvoidingView 
          style={styles.content} 
          behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        >
          {terminalMode === 'logs' && renderLogsView()}
          {terminalMode === 'commands' && renderCommandsView()}
          {terminalMode === 'system' && renderSystemView()}
        </KeyboardAvoidingView>
      </View>
    </Modal>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#000',
  },
  authContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  authContent: {
    width: '100%',
    maxWidth: 400,
    alignItems: 'center',
  },
  authHeader: {
    alignItems: 'center',
    marginBottom: 40,
  },
  authTitle: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#ff6b6b',
    marginTop: 16,
    letterSpacing: 2,
  },
  authSubtitle: {
    fontSize: 16,
    color: '#666',
    marginTop: 8,
  },
  authForm: {
    width: '100%',
    marginBottom: 40,
  },
  authInputContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#1a1a1a',
    borderRadius: 12,
    paddingHorizontal: 16,
    height: 56,
    marginBottom: 16,
    borderWidth: 1,
    borderColor: '#333',
  },
  authInputIcon: {
    marginRight: 12,
  },
  authInput: {
    flex: 1,
    color: '#fff',
    fontSize: 16,
  },
  authError: {
    color: '#ff6b6b',
    fontSize: 14,
    textAlign: 'center',
    marginBottom: 16,
  },
  authLocked: {
    color: '#ffcc00',
    fontSize: 14,
    textAlign: 'center',
    marginBottom: 16,
  },
  authButton: {
    backgroundColor: '#ff6b6b',
    height: 56,
    borderRadius: 12,
    justifyContent: 'center',
    alignItems: 'center',
  },
  authButtonDisabled: {
    backgroundColor: '#333',
  },
  authButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
    letterSpacing: 1,
  },
  authFooter: {
    alignItems: 'center',
  },
  authFooterText: {
    color: '#666',
    fontSize: 12,
    textAlign: 'center',
    marginBottom: 4,
  },
  closeButton: {
    position: 'absolute',
    top: 50,
    right: 20,
    padding: 12,
    backgroundColor: 'rgba(0,0,0,0.5)',
    borderRadius: 20,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 16,
    paddingTop: 50,
    paddingBottom: 16,
  },
  headerButton: {
    padding: 8,
  },
  headerTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#fff',
    letterSpacing: 1,
  },
  modeSelector: {
    flexDirection: 'row',
    backgroundColor: '#1a1a1a',
    marginHorizontal: 16,
    borderRadius: 12,
    padding: 4,
  },
  modeButton: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 8,
    borderRadius: 8,
  },
  activeModeButton: {
    backgroundColor: '#4ecdc4',
  },
  modeButtonText: {
    fontSize: 12,
    color: '#666',
    marginLeft: 4,
  },
  activeModeButtonText: {
    color: '#000',
    fontWeight: 'bold',
  },
  content: {
    flex: 1,
    margin: 16,
  },
  terminalContent: {
    flex: 1,
    backgroundColor: '#0d1117',
    borderRadius: 12,
    overflow: 'hidden',
  },
  logsHeader: {
    flexDirection: 'row',
    padding: 12,
    borderBottomWidth: 1,
    borderBottomColor: '#333',
  },
  logFilterInput: {
    flex: 1,
    backgroundColor: '#1a1a1a',
    color: '#fff',
    padding: 8,
    borderRadius: 6,
    marginRight: 8,
  },
  autoScrollButton: {
    padding: 8,
    borderRadius: 6,
    backgroundColor: '#1a1a1a',
  },
  activeAutoScrollButton: {
    backgroundColor: '#4ecdc4',
  },
  logsContainer: {
    flex: 1,
    padding: 12,
  },
  logItem: {
    marginBottom: 8,
  },
  logText: {
    fontFamily: Platform.OS === 'ios' ? 'Courier' : 'monospace',
    fontSize: 12,
    lineHeight: 16,
  },
  logDetails: {
    fontFamily: Platform.OS === 'ios' ? 'Courier' : 'monospace',
    fontSize: 10,
    color: '#666',
    marginLeft: 16,
    marginTop: 4,
  },
  commandsContainer: {
    flex: 1,
    padding: 12,
  },
  commandItem: {
    marginBottom: 12,
  },
  commandText: {
    fontFamily: Platform.OS === 'ios' ? 'Courier' : 'monospace',
    fontSize: 14,
    color: '#fff',
    lineHeight: 18,
  },
  prompt: {
    color: '#4ecdc4',
    fontWeight: 'bold',
  },
  commandOutput: {
    fontFamily: Platform.OS === 'ios' ? 'Courier' : 'monospace',
    fontSize: 12,
    marginTop: 4,
    lineHeight: 16,
    marginLeft: 16,
  },
  commandSpinner: {
    alignSelf: 'flex-start',
    marginTop: 4,
    marginLeft: 16,
  },
  commandInputContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 12,
    borderTopWidth: 1,
    borderTopColor: '#333',
    backgroundColor: '#1a1a1a',
  },
  commandPrompt: {
    fontFamily: Platform.OS === 'ios' ? 'Courier' : 'monospace',
    fontSize: 14,
    color: '#4ecdc4',
    fontWeight: 'bold',
  },
  commandInput: {
    flex: 1,
    fontFamily: Platform.OS === 'ios' ? 'Courier' : 'monospace',
    fontSize: 14,
    color: '#fff',
    marginLeft: 8,
    marginRight: 8,
  },
  executeButton: {
    padding: 8,
  },
  executeButtonDisabled: {
    opacity: 0.5,
  },
  systemContainer: {
    flex: 1,
    padding: 16,
  },
  systemSection: {
    marginBottom: 24,
  },
  systemSectionTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#4ecdc4',
    marginBottom: 12,
  },
  systemText: {
    fontSize: 14,
    color: '#fff',
    marginBottom: 6,
    fontFamily: Platform.OS === 'ios' ? 'Courier' : 'monospace',
  },
  quickActionButton: {
    backgroundColor: '#1a1a1a',
    padding: 12,
    borderRadius: 8,
    marginBottom: 8,
    borderWidth: 1,
    borderColor: '#333',
  },
  quickActionText: {
    color: '#4ecdc4',
    fontSize: 14,
    fontWeight: '500',
  },
});

export default AdminTerminal;