/**
 * 封神榜 - 性能监控模块
 */

(function() {
  'use strict';

  // 性能指标收集
  const metrics = {
    // 页面性能
    paint: {},
    // JavaScript错误
    jsErrors: [],
    // API性能
    apiCalls: [],
    // 用户行为
    userActions: []
  };

  // 监听页面性能
  if (PerformanceObserver) {
    // First Contentful Paint
    new PerformanceObserver((list) => {
      for (const entry of list.getEntries()) {
        metrics.paint.fcp = entry.startTime;
      }
    }).observe({ type: 'paint', buffered: true });

    // Largest Contentful Paint
    new PerformanceObserver((list) => {
      const entries = list.getEntries();
      const lastEntry = entries[entries.length - 1];
      metrics.paint.lcp = lastEntry.startTime;
    }).observe({ type: 'largest-contentful-paint', buffered: true });

    // First Input Delay
    new PerformanceObserver((list) => {
      const entries = list.getEntries();
      metrics.paint.fid = entries[0].processingStart - entries[0].startTime;
    }).observe({ type: 'first-input', buffered: true });
  }

  // 监听JS错误
  window.addEventListener('error', (event) => {
    metrics.jsErrors.push({
      message: event.message,
      filename: event.filename,
      lineno: event.lineno,
      timestamp: Date.now()
    });
    console.error('[JS Error]', event.message);
  });

  window.addEventListener('unhandledrejection', (event) => {
    metrics.jsErrors.push({
      message: event.reason,
      type: 'unhandledrejection',
      timestamp: Date.now()
    });
    console.error('[Promise Error]', event.reason);
  });

  // 监听用户行为
  const actionTypes = ['click', 'scroll', 'touchstart'];
  actionTypes.forEach(type => {
    document.addEventListener(type, () => {
      metrics.userActions.push({ type, timestamp: Date.now() });
      // 保留最近100条
      if (metrics.userActions.length > 100) {
        metrics.userActions.shift();
      }
    }, { passive: true });
  });

  // 性能报告
  window.PerformanceMonitor = {
    getMetrics: () => metrics,
    
    getReport: () => {
      const memory = performance.memory ? {
        used: (performance.memory.usedJSHeapSize / 1048576).toFixed(2) + 'MB',
        total: (performance.memory.totalJSHeapSize / 1048576).toFixed(2) + 'MB'
      } : 'N/A';

      return {
        paint: metrics.paint,
        jsErrors: metrics.jsErrors.length,
        apiCalls: metrics.apiCalls.length,
        userActions: metrics.userActions.length,
        memory
      };
    },

    // 上报性能数据
    report: () => {
      const report = {
        url: window.location.href,
        timestamp: new Date().toISOString(),
        ...window.PerformanceMonitor.getReport()
      };
      console.log('[Performance Report]', report);
      return report;
    },

    // 清理数据
    clear: () => {
      metrics.jsErrors = [];
      metrics.apiCalls = [];
      metrics.userActions = [];
    }
  };

  // 自动上报（页面卸载时）
  window.addEventListener('beforeunload', () => {
    const report = window.PerformanceMonitor.getReport();
    // 可以发送到服务器
    console.log('[Final Report]', report);
  });

  console.log('[Monitor] Performance monitoring initialized');
})();
