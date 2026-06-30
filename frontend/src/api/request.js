import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/store/user'
import router from '@/router'

// 创建axios实例
const service = axios.create({
  baseURL: '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 请求拦截器 - 自动携带Token
service.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers['Authorization'] = `Bearer ${userStore.token}`
    }
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器 - 统一处理响应
service.interceptors.response.use(
  (response) => {
    const res = response.data

    // 统一响应格式: {code, message, data, timestamp}
    if (res.code === 200) {
      return res
    }

    // 业务错误（静默模式不弹提示）
    if (!response.config.silenceError) {
      ElMessage.error(res.message || '请求失败')
    }
    return Promise.reject(new Error(res.message || '请求失败'))
  },
  (error) => {
    const silent = error.config?.silenceError

    if (!silent) {
      console.error('响应错误:', error)
    }

    if (error.response) {
      const { status, data } = error.response

      if (!silent) {
        switch (status) {
          case 401:
            // Token过期或未登录
            ElMessageBox.confirm('登录已过期，请重新登录', '提示', {
              confirmButtonText: '重新登录',
              cancelButtonText: '取消',
              type: 'warning',
            }).then(() => {
              const userStore = useUserStore()
              userStore.logout()
              router.push('/login')
            })
            break
          case 403:
            ElMessage.error('无权限访问')
            break
          case 404:
            ElMessage.error('请求资源不存在')
            break
          case 500:
            ElMessage.error(data?.message || '服务器内部错误')
            break
          default:
            ElMessage.error(data?.message || `请求失败(${status})`)
        }
      }
    } else if (!silent) {
      if (error.message.includes('timeout')) {
        ElMessage.error('请求超时，请重试')
      } else if (error.message.includes('Network Error')) {
        // 断网处理 - 将草稿序列化到localStorage
        handleOffline(error)
      } else {
        ElMessage.error('网络异常，请检查网络连接')
      }
    }

    return Promise.reject(error)
  }
)

// 断网处理
let offlineQueue = []
let isOnline = true

function handleOffline(error) {
  isOnline = false
  ElMessage.warning('网络已断开，草稿将暂存本地')

  // 监听网络恢复
  window.addEventListener('online', () => {
    isOnline = true
    ElMessage.success('网络已恢复，正在同步草稿...')
    retryOfflineQueue()
  })
}

// 重传离线队列
async function retryOfflineQueue() {
  while (offlineQueue.length > 0) {
    const { config, resolve, reject } = offlineQueue.shift()
    try {
      const response = await service(config)
      resolve(response)
    } catch (err) {
      reject(err)
    }
  }
}

// 检查网络状态
export function getOnlineStatus() {
  return isOnline
}

// 添加到离线队列
export function addToOfflineQueue(config, resolve, reject) {
  offlineQueue.push({ config, resolve, reject })
}

export default service
