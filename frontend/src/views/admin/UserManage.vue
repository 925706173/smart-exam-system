<template>
  <div class="user-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <div>
            <el-button type="primary" @click="handleAdd">新增用户</el-button>
            <el-upload
              :show-file-list="false"
              :before-upload="handleImport"
              accept=".xlsx,.xls"
              style="display: inline-block; margin-left: 12px"
            >
              <el-button>批量导入</el-button>
            </el-upload>
          </div>
        </div>
      </template>

      <!-- 筛选 -->
      <el-form :model="filterQuery" inline class="filter-form">
        <el-form-item label="角色">
          <el-select v-model="filterQuery.role" placeholder="全部" clearable style="width: 120px">
            <el-option label="学生" value="STUDENT" />
            <el-option label="教师" value="TEACHER" />
            <el-option label="管理员" value="ADMIN" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="filterQuery.keyword" placeholder="用户名/姓名" clearable style="width: 200px" />
        </el-form-item>
      </el-form>

      <!-- 用户列表 -->
      <el-table :data="userList" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="姓名" width="100" />
        <el-table-column label="角色" width="100">
          <template #default="{ row }">
            <el-tag :type="getRoleType(row.role)">{{ formatRole(row.role) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '冻结' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastLoginTime" label="最后登录" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.lastLoginTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" @click="handleResetPwd(row)">重置密码</el-button>
            <el-button
              size="small"
              :type="row.status === 1 ? 'warning' : 'success'"
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 1 ? '冻结' : '解冻' }}
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="filterQuery.pageNum"
        v-model:page-size="filterQuery.pageSize"
        :total="total"
        layout="total, prev, pager, next, jumper"
        style="margin-top: 20px; justify-content: flex-end"
        @current-change="loadData"
      />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑用户' : '新增用户'" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" style="width: 100%">
            <el-option label="学生" value="STUDENT" />
            <el-option label="教师" value="TEACHER" />
            <el-option label="管理员" value="ADMIN" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="!isEdit" label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="默认123456" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserPage, createUser, updateUser, deleteUser, resetPassword, toggleUserStatus, importUsers } from '@/api/user'
import { formatDateTime, formatRole } from '@/utils/format'
import { debounce } from 'lodash-es'

const loading = ref(false)
const userList = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const filterQuery = reactive({
  role: '',
  keyword: '',
  pageNum: 1,
  pageSize: 10,
})

const form = reactive({
  id: null,
  username: '',
  realName: '',
  role: 'STUDENT',
  password: '',
  email: '',
  phone: '',
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }],
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getUserPage(filterQuery)
    userList.value = res.data.records
    total.value = res.data.total
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const debouncedLoad = debounce(loadData, 300)
watch(() => [filterQuery.role, filterQuery.keyword], () => {
  filterQuery.pageNum = 1
  debouncedLoad()
})

onMounted(loadData)

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, { id: null, username: '', realName: '', role: 'STUDENT', password: '', email: '', phone: '' })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    if (isEdit.value) {
      await updateUser(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await createUser(form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleResetPwd = async (row) => {
  await ElMessageBox.confirm(`确定要重置「${row.realName}」的密码为123456吗？`, '提示', { type: 'warning' })
  try {
    await resetPassword(row.id)
    ElMessage.success('密码已重置为123456')
  } catch (e) {
    console.error(e)
  }
}

const handleToggleStatus = async (row) => {
  const action = row.status === 1 ? '冻结' : '解冻'
  await ElMessageBox.confirm(`确定要${action}「${row.realName}」吗？`, '提示', { type: 'warning' })
  try {
    await toggleUserStatus(row.id)
    ElMessage.success(`${action}成功`)
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm(`确定要删除「${row.realName}」吗？`, '提示', { type: 'warning' })
  try {
    await deleteUser(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleImport = async (file) => {
  try {
    const res = await importUsers(file, 'STUDENT')
    ElMessage.success(`导入完成：成功${res.data.successCount}条，失败${res.data.failCount}条`)
    loadData()
  } catch (e) {
    ElMessage.error('导入失败')
  }
  return false
}

const getRoleType = (role) => {
  const map = { STUDENT: '', TEACHER: 'success', ADMIN: 'danger' }
  return map[role] || ''
}
</script>

<style scoped>
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.filter-form {
  margin-bottom: 16px;
}
</style>
