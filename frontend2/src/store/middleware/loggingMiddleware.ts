import { Middleware } from '@reduxjs/toolkit';
import { debugLogger } from '../../services/debugLogger';

export const loggingMiddleware: Middleware = (store) => (next) => (action: any) => {
  const startTime = Date.now();
  const previousState = store.getState();
  
  // Log the action being dispatched
  debugLogger.reduxAction(action.type, action.payload);
  
  console.log('🔥 REDUX ACTION DISPATCHED:', {
    type: action.type,
    payload: action.payload,
    timestamp: new Date().toISOString(),
    previousState: JSON.stringify(previousState, null, 2),
  });

  // Execute the action
  const result = next(action);
  
  const newState = store.getState();
  const duration = Date.now() - startTime;
  
  // Log state changes
  const stateChanges = findStateChanges(previousState, newState);
  if (Object.keys(stateChanges).length > 0) {
    debugLogger.stateChange('REDUX_STORE', action.type, previousState, newState);
    
    console.log('🔄 STATE CHANGED:', {
      action: action.type,
      changes: stateChanges,
      duration: `${duration}ms`,
      timestamp: new Date().toISOString(),
    });
  }

  // Log performance if action took too long
  if (duration > 100) {
    debugLogger.performance('REDUX_ACTION', duration, {
      actionType: action.type,
      slow: true,
    });
    
    console.warn('⚠️ SLOW REDUX ACTION:', {
      type: action.type,
      duration: `${duration}ms`,
      threshold: '100ms',
    });
  }

  // Log errors if any
  if (action.type.endsWith('/rejected')) {
    debugLogger.error('REDUX', action.payload, {
      actionType: action.type,
      originalAction: action.meta?.requestId,
    });
    
    console.error('❌ REDUX ACTION REJECTED:', {
      type: action.type,
      error: action.payload,
      meta: action.meta,
    });
  }

  // Log successful async actions
  if (action.type.endsWith('/fulfilled')) {
    console.log('✅ REDUX ACTION FULFILLED:', {
      type: action.type,
      payload: action.payload,
      meta: action.meta,
    });
  }

  return result;
};

// Helper function to find state changes
function findStateChanges(previousState: any, newState: any, path: string = ''): any {
  const changes: any = {};
  
  // Handle different types
  if (previousState === newState) {
    return changes;
  }
  
  if (typeof previousState !== typeof newState) {
    changes[path || 'root'] = {
      from: previousState,
      to: newState,
      type: 'type_change',
    };
    return changes;
  }
  
  if (typeof previousState !== 'object' || previousState === null) {
    if (previousState !== newState) {
      changes[path || 'root'] = {
        from: previousState,
        to: newState,
        type: 'value_change',
      };
    }
    return changes;
  }
  
  // Handle arrays
  if (Array.isArray(previousState)) {
    if (!Array.isArray(newState)) {
      changes[path || 'root'] = {
        from: previousState,
        to: newState,
        type: 'array_to_non_array',
      };
      return changes;
    }
    
    if (previousState.length !== newState.length) {
      changes[`${path}.length`] = {
        from: previousState.length,
        to: newState.length,
        type: 'array_length_change',
      };
    }
    
    const maxLength = Math.max(previousState.length, newState.length);
    for (let i = 0; i < maxLength; i++) {
      const itemChanges = findStateChanges(
        previousState[i],
        newState[i],
        `${path}[${i}]`
      );
      Object.assign(changes, itemChanges);
    }
    
    return changes;
  }
  
  // Handle objects
  const allKeys = new Set([
    ...Object.keys(previousState || {}),
    ...Object.keys(newState || {}),
  ]);
  
  for (const key of allKeys) {
    const newPath = path ? `${path}.${key}` : key;
    const itemChanges = findStateChanges(
      previousState[key],
      newState[key],
      newPath
    );
    Object.assign(changes, itemChanges);
  }
  
  return changes;
}