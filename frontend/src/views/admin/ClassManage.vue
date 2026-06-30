<template>
  <div class="class-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>班级管理</span>
          <el-button type="primary" @click="handleAdd">创建班级</el-button>
        </div>
      </template>

      <el-table :data="classList" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="班级名称" min-width="200" />
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="teacherCount" label="教师数" width="80" />
        <el-table-column prop="studentCount" label="学生数" width="80" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="openMemberDrawer(row)">管理成员</el-button>
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        style="margin-top: 20px; justify-content: flex-end"
        @current-change="loadData"
      />
    </el-card>

    <!-- 创建/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑班级' : '创建班级'" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="班级名称" prop="name">
          <el-input v-model="form.name" placeholder="如：2024级计算机1班" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="班级描述（选填）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 成员管理抽屉 -->
    <el-drawer v-model="drawerVisible" :title="'管理成员 - ' + currentClass.name" size="60%" direction="rtl">
      <div class="drawer-content">
        <!-- 顶部操作栏 -->
        <div class="drawer-toolbar">
          <div class="toolbar-left">
            <el-radio-group v-model="memberRoleFilter" @change="loadMembers">
              <el-radio-button value="">全部</el-radio-button>
              <el-radio-button value="TEACHER">教师</el-radio-button>
              <el-radio-button value="STUDENT">学生</el-radio-button>
            </el-radio-group>
          </div>
          <div class="toolbar-right">
            <el-button type="primary" @click="openAddMemberDialog">添加成员</el-button>
          </div>
        </div>

        <!-- 成员列表 -->
        <el-table :data="memberList" v-loading="memberLoading" stripe>
          <el-table-column prop="userId" label="用户ID" width="80" />
          <el-table-column prop="username" label="用户名" width="120" />
          <el-table-column prop="realName" label="姓名" width="120" />
          <el-table-column label="角色" width="100">
            <template #default="{ row }">
              <el-tag :type="row.role === 'TEACHER' ? 'warning' : 'success'" size="small">
                {{ row.role === 'TEACHER' ? '教师' : '学生' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template #default="{ row }">
              <el-button size="small" type="danger" @click="handleRemoveMember(row)">移除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-drawer>

    <!-- 添加成员弹窗 -->
    <el-dialog v-model="addMemberVisible" title="添加成员" width="600px" append-to-body>
      <div class="add-member-dialog">
        <el-form :inline="true" class="search-form">
          <el-form-item label="角色">
            <el-select v-model="addMemberRole" placeholder="全部" clearable style="width: 120px">
              <el-option label="教师" value="TEACHER" />
              <el-option label="学生" value="STUDENT" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="loadAvailableUsers">搜索</el-button>
          </el-form-item>
        </el-form>

        <el-table
          :data="availableUsers"
          v-loading="availableLoading"
          stripe
          height="350"
          @selection-change="handleUserSelectionChange"
        >
          <el-table-column type="selection" width="45" />
          <el-table-column prop="userId" label="ID" width="60" />
          <el-table-column prop="username" label="用户名" width="120" />
          <el-table-column prop="realName" label="姓名" width="120" />
          <el-table-column label="角色" width="100">
            <template #default="{ row }">
              <el-tag :type="row.role === 'TEACHER' ? 'warning' : 'success'" size="small">
                {{ row.role === 'TEACHER' ? '教师' : '学生' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>

        <div class="add-member-footer">
          <el-button type="primary" :disabled="selectedUsers.length === 0" @click="batchAddMembers">
            添加选中的 {{ selectedUsers.length }} 位成员
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getClassPage, createClass, updateClass, deleteClass,
  getClassUsers, addClassUser, removeClassUser, getAvailableUsers
} from '@/api/class'

const loading = ref(false)
const classList = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 创建/编辑
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const form = reactive({ id: null, name: '', description: '' })
const rules = { name: [{ required: true, message: '请输入班级名称', trigger: 'blur' }] }

// 成员管理
const drawerVisible = ref(false)
const currentClass = ref({})
const memberList = ref([])
const memberLoading = ref(false)
const memberRoleFilter = ref('')

// 添加成员
const addMemberVisible = ref(false)
const addMemberRole = ref('')
const availableUsers = ref([])
const availableLoading = ref(false)
const selectedUsers = ref([])

const loadData = async () => {
  loading.value = true
  try {
    const res = await getClassPage({ pageNum: pageNum.value, pageSize: pageSize.value })
    classList.value = res.data.records
    total.value = res.data.total
  } catch (e) { console.error(e) } finally { loading.value = false }
}

onMounted(loadData)

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, { id: null, name: '', description: '' })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, { id: row.id, name: row.name, description: row.description })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    if (isEdit.value) {
      await updateClass(form.id, { name: form.name, description: form.description })
      ElMessage.success('更新成功')
    } else {
      await createClass({ name: form.name, description: form.description })
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) { console.error(e) }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm(`确定要删除班级"${row.name}"吗？`, '提示', { type: 'warning' })
  try {
    await deleteClass(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) { console.error(e) }
}

// 成员管理
const openMemberDrawer = (row) => {
  currentClass.value = row
  drawerVisible.value = true
  memberRoleFilter.value = ''
  loadMembers()
}

const loadMembers = async () => {
  memberLoading.value = true
  try {
    const params = memberRoleFilter.value ? { role: memberRoleFilter.value } : {}
    const res = await getClassUsers(currentClass.value.id, params)
    memberList.value = res.data
  } catch (e) { console.error(e) } finally { memberLoading.value = false }
}

const handleRemoveMember = async (row) => {
  await ElMessageBox.confirm(`确定要将${row.realName || row.username}从班级中移除吗？`, '提示', { type: 'warning' })
  try {
    await removeClassUser(currentClass.value.id, row.userId)
    ElMessage.success('已移除')
    loadMembers()
    loadData()
  } catch (e) { console.error(e) }
}

// 添加成员
const openAddMemberDialog = () => {
  addMemberRole.value = ''
  availableUsers.value = []
  selectedUsers.value = []
  addMemberVisible.value = true
  loadAvailableUsers()
}

const loadAvailableUsers = async () => {
  availableLoading.value = true
  try {
    const params = addMemberRole.value ? { role: addMemberRole.value } : {}
    const res = await getAvailableUsers(params)
    availableUsers.value = res.data
  } catch (e) { console.error(e) } finally { availableLoading.value = false }
}

const handleUserSelectionChange = (val) => { selectedUsers.value = val }

const batchAddMembers = async () => {
  try {
    for (const user of selectedUsers.value) {
      await addClassUser(currentClass.value.id, { userId: user.userId })
    }
    ElMessage.success(`成功添加 ${selectedUsers.value.length} 位成员`)
    addMemberVisible.value = false
    loadMembers()
    loadData()
  } catch (e) { console.error(e) }
}
</script>

<style scoped>
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.drawer-content { padding: 0 10px; }
.drawer-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
  padding: 8px 0;
  border-bottom: 1px solid #eee;
}
.toolbar-left, .toolbar-right { display: flex; gap: 8px; }
.search-form { margin-bottom: 12px; }
.add-member-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
  padding-top: 12px;
  border-top: 1px solid #eee;
}
</style>
