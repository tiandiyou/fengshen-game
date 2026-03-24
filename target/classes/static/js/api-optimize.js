/**
 * 封神榜 - API请求优化模块
 * 添加缓存、错误重试、性能监控
 */

// API缓存
const apiCache = new Map();
const CACHE_EXPIRE = 30000; // 30秒缓存

// 请求队列（避免重复请求）
const pendingRequests = new Map();

// 性能监控
const performanceMetrics = {
  requests: 0,
  errors: 0,
  totalTime: 0
};

// 优化的API请求函数
async function optimizedFetch(url, options = {}) {
  const { 
    useCache = false, 
    retry = 2, 
    cacheTime = CACHE_EXPIRE,
    ...fetchOptions 
  } = options;

  const cacheKey = url + JSON.stringify(fetchOptions.body || {});
  
  // 检查缓存
  if (useCache && apiCache.has(cacheKey)) {
    const cached = apiCache.get(cacheKey);
    if (Date.now() - cached.time < cacheTime) {
      console.log('[Cache] Using cached:', url);
      return cached.data;
    }
  }

  // 检查pending请求
  if (pendingRequests.has(cacheKey)) {
    console.log('[Queue] Waiting for pending request:', url);
    return pendingRequests.get(cacheKey);
  }

  // 创建请求
  const requestPromise = (async () => {
    for (let i = 0; i <= retry; i++) {
      try {
        const startTime = performance.now();
        
        const response = await fetch(url, {
          ...fetchOptions,
          headers: {
            'Content-Type': 'application/json',
            ...fetchOptions.headers
          }
        });

        const data = await response.json();
        const time = performance.now() - startTime;

        // 记录性能
        performanceMetrics.requests++;
        performanceMetrics.totalTime += time;
        console.log(`[API] ${url} - ${time.toFixed(0)}ms`);

        // 缓存结果
        if (useCache && response.ok) {
          apiCache.set(cacheKey, { data, time: Date.now() });
        }

        return data;
      } catch (error) {
        console.error(`[API Error] ${url}:`, error);
        performanceMetrics.errors++;
        
        if (i < retry) {
          console.log(`[Retry] ${url} - Attempt ${i + 1}/${retry}`);
          await new Promise(r => setTimeout(r, 1000 * (i + 1))); // 指数退避
        }
      }
    }
    return { success: false, message: '请求失败' };
  })();

  pendingRequests.set(cacheKey, requestPromise);
  
  try {
    return await requestPromise;
  } finally {
    pendingRequests.delete(cacheKey);
  }
}

// 清理过期缓存
function clearExpiredCache() {
  const now = Date.now();
  for (const [key, value] of apiCache.entries()) {
    if (now - value.time > CACHE_EXPIRE) {
      apiCache.delete(key);
    }
  }
}

// 定期清理缓存
setInterval(clearExpiredCache, 60000);

// 导出优化后的API函数
window.API = {
  get: (url, options) => optimizedFetch(url, { ...options, method: 'GET' }),
  post: (url, body, options) => optimizedFetch(url, { ...options, method: 'POST', body: JSON.stringify(body) }),
  put: (url, body, options) => optimizedFetch(url, { ...options, method: 'PUT', body: JSON.stringify(body) }),
  delete: (url, options) => optimizedFetch(url, { ...options, method: 'DELETE' }),
  
  // 缓存管理
  clearCache: () => apiCache.clear(),
  getCacheStats: () => ({
    size: apiCache.size,
    metrics: performanceMetrics
  })
};

console.log('[Optimize] API optimization module loaded');
