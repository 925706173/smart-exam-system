/**
 * localStorage封装
 */

const PREFIX = 'smart_exam_'

export function setStorage(key, value) {
  try {
    localStorage.setItem(PREFIX + key, JSON.stringify(value))
  } catch (e) {
    console.error('存储失败:', e)
  }
}

export function getStorage(key) {
  try {
    const value = localStorage.getItem(PREFIX + key)
    return value ? JSON.parse(value) : null
  } catch (e) {
    console.error('读取失败:', e)
    return null
  }
}

export function removeStorage(key) {
  localStorage.removeItem(PREFIX + key)
}

export function clearStorage() {
  localStorage.clear()
}
