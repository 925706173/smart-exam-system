import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, logout as logoutApi, getUserInfo as getUserInfoApi } from '@/api/auth'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const role = computed(() => userInfo.value.role || '')
  const realName = computed(() => userInfo.value.realName || '')

  // 登录
  async function login(loginData) {
    const res = await loginApi(loginData)
    token.value = res.data.token
    userInfo.value = {
      userId: res.data.userId,
      role: res.data.role,
      realName: res.data.realName,
    }

    // 持久化存储
    localStorage.setItem('token', token.value)
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))

    return res.data
  }

  // 退出登录
  async function logout() {
    try {
      await logoutApi()
    } catch (e) {
      // 即使接口失败也要清除本地状态
    }

    token.value = ''
    userInfo.value = {}
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')

    router.push('/login')
  }

  // 获取用户信息
  async function fetchUserInfo() {
    const res = await getUserInfoApi()
    userInfo.value = res.data
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
    return res.data
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    role,
    realName,
    login,
    logout,
    fetchUserInfo,
  }
})
