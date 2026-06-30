import { createRouter, createWebHistory } from 'vue-router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { useUserStore } from '@/store/user'

// 路由配置
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', requiresAuth: false },
  },
  // 学生端路由
  {
    path: '/student',
    component: () => import('@/views/student/Layout.vue'),
    redirect: '/student/dashboard',
    meta: { requiresAuth: true, role: 'STUDENT' },
    children: [
      {
        path: 'dashboard',
        name: 'StudentDashboard',
        component: () => import('@/views/student/Dashboard.vue'),
        meta: { title: '学生首页' },
      },
      {
        path: 'exam-hall',
        name: 'ExamHall',
        component: () => import('@/views/student/ExamHall.vue'),
        meta: { title: '考试大厅' },
      },
      {
        path: 'exam-room/:recordId',
        name: 'ExamRoom',
        component: () => import('@/views/student/ExamRoom.vue'),
        meta: { title: '在线答题', hideLayout: true },
      },
      {
        path: 'history',
        name: 'History',
        component: () => import('@/views/student/History.vue'),
        meta: { title: '历史成绩' },
      },
      {
        path: 'exam-review',
        name: 'ExamReview',
        component: () => import('@/views/student/ErrorBook.vue'),
        meta: { title: '试卷复查' },
      },
    ],
  },
  // 教师端路由
  {
    path: '/teacher',
    component: () => import('@/views/teacher/Layout.vue'),
    redirect: '/teacher/question-bank',
    meta: { requiresAuth: true, role: 'TEACHER' },
    children: [
      {
        path: 'question-bank',
        name: 'QuestionBank',
        component: () => import('@/views/teacher/QuestionBank.vue'),
        meta: { title: '题库管理' },
      },
      {
        path: 'paper-manage',
        name: 'PaperManage',
        component: () => import('@/views/teacher/PaperManage.vue'),
        meta: { title: '试卷管理' },
      },
      {
        path: 'grading',
        name: 'GradingCenter',
        component: () => import('@/views/teacher/GradingCenter.vue'),
        meta: { title: '阅卷中心' },
      },
      {
        path: 'data-board',
        name: 'DataBoard',
        component: () => import('@/views/teacher/DataBoard.vue'),
        meta: { title: '数据看板' },
      },
    ],
  },
  // 管理员端路由
  {
    path: '/admin',
    component: () => import('@/views/admin/Layout.vue'),
    redirect: '/admin/user-manage',
    meta: { requiresAuth: true, role: 'ADMIN' },
    children: [
      {
        path: 'user-manage',
        name: 'UserManage',
        component: () => import('@/views/admin/UserManage.vue'),
        meta: { title: '用户管理' },
      },
      {
        path: 'class-manage',
        name: 'ClassManage',
        component: () => import('@/views/admin/ClassManage.vue'),
        meta: { title: '班级管理' },
      },
    ],
  },
  // 默认重定向
  {
    path: '/',
    redirect: '/login',
  },
  // 404
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/login/index.vue'),
    redirect: '/login',
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 路由守卫
NProgress.configure({ showSpinner: false })

router.beforeEach((to, from, next) => {
  NProgress.start()

  const userStore = useUserStore()
  const requiresAuth = to.meta.requiresAuth !== false

  // 不需要登录的页面
  if (!requiresAuth) {
    if (userStore.isLoggedIn) {
      // 已登录，跳转到对应角色首页
      const role = userStore.role
      if (role === 'STUDENT') next('/student')
      else if (role === 'TEACHER') next('/teacher')
      else if (role === 'ADMIN') next('/admin')
      else next()
    } else {
      next()
    }
    return
  }

  // 需要登录但未登录
  if (!userStore.isLoggedIn) {
    next('/login')
    return
  }

  // 权限校验
  const requiredRole = to.meta.role
  if (requiredRole && userStore.role !== requiredRole) {
    // 角色不匹配，跳转到对应角色首页
    const role = userStore.role
    if (role === 'STUDENT') next('/student')
    else if (role === 'TEACHER') next('/teacher')
    else if (role === 'ADMIN') next('/admin')
    else next('/login')
    return
  }

  next()
})

router.afterEach(() => {
  NProgress.done()
})

export default router
