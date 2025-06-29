import { Platform } from 'react-native';

export interface LogEntry {
  timestamp: string;
  level: 'DEBUG' | 'INFO' | 'WARN' | 'ERROR';
  category: string;
  action: string;
  details?: any;
  userId?: string;
  screenName?: string;
  sessionId?: string;
}

class DebugLogger {
  private static instance: DebugLogger;
  private logs: LogEntry[] = [];
  private sessionId: string;
  private isEnabled: boolean = true;
  private maxLogs: number = 1000;

  private constructor() {
    this.sessionId = this.generateSessionId();
    this.logSystemInfo();
  }

  static getInstance(): DebugLogger {
    if (!DebugLogger.instance) {
      DebugLogger.instance = new DebugLogger();
    }
    return DebugLogger.instance;
  }

  private generateSessionId(): string {
    return `session_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
  }

  private logSystemInfo() {
    this.log('SYSTEM', 'SESSION_START', {
      platform: Platform.OS,
      version: Platform.Version,
      sessionId: this.sessionId,
      timestamp: new Date().toISOString(),
    });
  }

  private formatLog(entry: LogEntry): string {
    const timestamp = new Date(entry.timestamp).toLocaleTimeString();
    const details = entry.details ? JSON.stringify(entry.details, null, 2) : '';
    
    return `[${timestamp}] [${entry.level}] [${entry.category}] ${entry.action}${
      entry.screenName ? ` (Screen: ${entry.screenName})` : ''
    }${details ? `\nDetails: ${details}` : ''}`;
  }

  private log(category: string, action: string, details?: any, level: 'DEBUG' | 'INFO' | 'WARN' | 'ERROR' = 'DEBUG') {
    if (!this.isEnabled) return;

    const entry: LogEntry = {
      timestamp: new Date().toISOString(),
      level,
      category,
      action,
      details,
      sessionId: this.sessionId,
    };

    this.logs.push(entry);
    
    // Keep only the last maxLogs entries
    if (this.logs.length > this.maxLogs) {
      this.logs = this.logs.slice(-this.maxLogs);
    }

    // Console output with colors and formatting
    const logMessage = this.formatLog(entry);
    const consoleMethod = level === 'ERROR' ? 'error' : level === 'WARN' ? 'warn' : 'log';
    
    console[consoleMethod](`🔍 ${logMessage}`);
    
    // For critical errors, also alert
    if (level === 'ERROR') {
      console.error('🚨 CRITICAL ERROR LOGGED:', entry);
    }
  }

  // User Interaction Logging
  userInteraction(action: string, element: string, details?: any, screenName?: string) {
    this.log('USER_INTERACTION', `${action.toUpperCase()}_${element.toUpperCase()}`, {
      element,
      screenName,
      ...details,
    }, 'INFO');
  }

  buttonPress(buttonName: string, screenName?: string, additionalData?: any) {
    this.userInteraction('BUTTON_PRESS', buttonName, additionalData, screenName);
  }

  inputChange(fieldName: string, value: any, screenName?: string) {
    this.userInteraction('INPUT_CHANGE', fieldName, {
      value: typeof value === 'string' && value.includes('password') ? '[HIDDEN]' : value,
      valueLength: typeof value === 'string' ? value.length : undefined,
    }, screenName);
  }

  formSubmit(formName: string, data: any, screenName?: string) {
    this.userInteraction('FORM_SUBMIT', formName, {
      fieldCount: Object.keys(data).length,
      fields: Object.keys(data),
      // Hide sensitive data
      sanitizedData: Object.keys(data).reduce((acc, key) => {
        acc[key] = key.toLowerCase().includes('password') ? '[HIDDEN]' : data[key];
        return acc;
      }, {} as any),
    }, screenName);
  }

  // Navigation Logging
  screenEnter(screenName: string, params?: any) {
    this.log('NAVIGATION', 'SCREEN_ENTER', {
      screenName,
      params,
      timestamp: new Date().toISOString(),
    }, 'INFO');
  }

  screenLeave(screenName: string, timeSpent?: number) {
    this.log('NAVIGATION', 'SCREEN_LEAVE', {
      screenName,
      timeSpent,
      timestamp: new Date().toISOString(),
    }, 'INFO');
  }

  navigationAction(action: string, from: string, to: string, params?: any) {
    this.log('NAVIGATION', action.toUpperCase(), {
      from,
      to,
      params,
    }, 'INFO');
  }

  // API Call Logging
  apiCall(method: string, url: string, data?: any, headers?: any) {
    this.log('API', 'REQUEST', {
      method: method.toUpperCase(),
      url,
      data: data ? JSON.stringify(data) : undefined,
      headers: headers ? Object.keys(headers) : undefined,
      timestamp: new Date().toISOString(),
    }, 'INFO');
  }

  apiResponse(method: string, url: string, status: number, response?: any, duration?: number) {
    this.log('API', 'RESPONSE', {
      method: method.toUpperCase(),
      url,
      status,
      responseSize: response ? JSON.stringify(response).length : 0,
      duration: duration ? `${duration}ms` : undefined,
      success: status >= 200 && status < 300,
      timestamp: new Date().toISOString(),
    }, status >= 400 ? 'ERROR' : 'INFO');
  }

  apiError(method: string, url: string, error: any, duration?: number) {
    this.log('API', 'ERROR', {
      method: method.toUpperCase(),
      url,
      error: error.message || error.toString(),
      duration: duration ? `${duration}ms` : undefined,
      timestamp: new Date().toISOString(),
    }, 'ERROR');
  }

  // State Management Logging
  stateChange(storeName: string, action: string, previousState?: any, newState?: any) {
    this.log('STATE', 'CHANGE', {
      store: storeName,
      action,
      previousState: previousState ? JSON.stringify(previousState) : undefined,
      newState: newState ? JSON.stringify(newState) : undefined,
      timestamp: new Date().toISOString(),
    }, 'DEBUG');
  }

  reduxAction(actionType: string, payload?: any) {
    this.log('REDUX', 'ACTION', {
      type: actionType,
      payload: payload ? JSON.stringify(payload) : undefined,
      timestamp: new Date().toISOString(),
    }, 'DEBUG');
  }

  // Authentication Logging
  authEvent(event: string, details?: any) {
    this.log('AUTH', event.toUpperCase(), {
      ...details,
      timestamp: new Date().toISOString(),
    }, 'INFO');
  }

  // Error Logging
  error(category: string, error: any, context?: any) {
    this.log(category, 'ERROR', {
      error: error.message || error.toString(),
      stack: error.stack,
      context,
      timestamp: new Date().toISOString(),
    }, 'ERROR');
  }

  // Performance Logging
  performance(action: string, duration: number, details?: any) {
    this.log('PERFORMANCE', action.toUpperCase(), {
      duration: `${duration}ms`,
      ...details,
      timestamp: new Date().toISOString(),
    }, duration > 1000 ? 'WARN' : 'INFO');
  }

  // Custom Event Logging
  customEvent(category: string, event: string, details?: any) {
    this.log(category.toUpperCase(), event.toUpperCase(), {
      ...details,
      timestamp: new Date().toISOString(),
    }, 'INFO');
  }

  // Utility Methods
  getLogs(): LogEntry[] {
    return [...this.logs];
  }

  getLogsByCategory(category: string): LogEntry[] {
    return this.logs.filter(log => log.category === category);
  }

  getLogsByLevel(level: 'DEBUG' | 'INFO' | 'WARN' | 'ERROR'): LogEntry[] {
    return this.logs.filter(log => log.level === level);
  }

  exportLogs(): string {
    return JSON.stringify(this.logs, null, 2);
  }

  clearLogs() {
    this.logs = [];
    this.log('SYSTEM', 'LOGS_CLEARED', {
      timestamp: new Date().toISOString(),
    });
  }

  setEnabled(enabled: boolean) {
    this.isEnabled = enabled;
    this.log('SYSTEM', `LOGGING_${enabled ? 'ENABLED' : 'DISABLED'}`, {
      timestamp: new Date().toISOString(),
    });
  }

  // Screen Time Tracking
  private screenTimes: { [key: string]: number } = {};

  startScreenTimer(screenName: string) {
    this.screenTimes[screenName] = Date.now();
    this.log('SCREEN_TIME', 'START_TIMER', {
      screenName,
      timestamp: new Date().toISOString(),
    }, 'DEBUG');
  }

  endScreenTimer(screenName: string) {
    const startTime = this.screenTimes[screenName];
    if (startTime) {
      const duration = Date.now() - startTime;
      delete this.screenTimes[screenName];
      
      this.log('SCREEN_TIME', 'END_TIMER', {
        screenName,
        duration: `${duration}ms`,
        durationSeconds: `${(duration / 1000).toFixed(2)}s`,
        timestamp: new Date().toISOString(),
      }, 'INFO');
    }
  }

  // Session Summary
  getSessionSummary() {
    const summary = {
      sessionId: this.sessionId,
      totalLogs: this.logs.length,
      logsByLevel: {
        DEBUG: this.getLogsByLevel('DEBUG').length,
        INFO: this.getLogsByLevel('INFO').length,
        WARN: this.getLogsByLevel('WARN').length,
        ERROR: this.getLogsByLevel('ERROR').length,
      },
      logsByCategory: this.logs.reduce((acc, log) => {
        acc[log.category] = (acc[log.category] || 0) + 1;
        return acc;
      }, {} as { [key: string]: number }),
      sessionDuration: Date.now() - new Date(this.logs[0]?.timestamp || Date.now()).getTime(),
      errors: this.getLogsByLevel('ERROR'),
    };

    this.log('SYSTEM', 'SESSION_SUMMARY', summary, 'INFO');
    return summary;
  }
}

// Export singleton instance
export const debugLogger = DebugLogger.getInstance();
export default debugLogger;

// Helper functions for easy access
export const logUserInteraction = (action: string, element: string, details?: any, screenName?: string) => {
  debugLogger.userInteraction(action, element, details, screenName);
};

export const logButtonPress = (buttonName: string, screenName?: string, additionalData?: any) => {
  debugLogger.buttonPress(buttonName, screenName, additionalData);
};

export const logInputChange = (fieldName: string, value: any, screenName?: string) => {
  debugLogger.inputChange(fieldName, value, screenName);
};

export const logFormSubmit = (formName: string, data: any, screenName?: string) => {
  debugLogger.formSubmit(formName, data, screenName);
};

export const logScreenEnter = (screenName: string, params?: any) => {
  debugLogger.screenEnter(screenName, params);
};

export const logScreenLeave = (screenName: string, timeSpent?: number) => {
  debugLogger.screenLeave(screenName, timeSpent);
};

export const logApiCall = (method: string, url: string, data?: any, headers?: any) => {
  debugLogger.apiCall(method, url, data, headers);
};

export const logApiResponse = (method: string, url: string, status: number, response?: any, duration?: number) => {
  debugLogger.apiResponse(method, url, status, response, duration);
};

export const logError = (category: string, error: any, context?: any) => {
  debugLogger.error(category, error, context);
};

export const logCustomEvent = (category: string, event: string, details?: any) => {
  debugLogger.customEvent(category, event, details);
};