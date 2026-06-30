<template>
  <div class="dict-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>字典管理</span>
          <el-button type="primary" @click="handleAdd">新增字典</el-button>
        </div>
      </template>

      <el-form :model="filterQuery" inline class="filter-form">
        <el-form-item label="类型">
          <el-select v-model="filterQuery.type" placeholder="全部" clearable style="width: 120px">
            <el-option label="班级" value="CLASS" />
            <el-option label="专业" value="MAJOR" />
            <el-option label="科目" value="SUBJECT" />
            <el-option label="学期" value="SEMESTER" />
          </el-select>
        </el-form-item>
      </el-form>

      <el-table :data="dictList" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag>{{ formatDictType(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="code" label="编码" width="150" />
        <el-table-column prop="name" label="名称" min-width="200" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="filterQuery.pageNum"
        v-model:page-size="filterQuery.pageSize"
        :total="total"
        layout="total, prev, pager, next"
        style="margin-top: 20px; justify-content: flex-end"
        @current-change="loadData"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑字典' : '新增字典'" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" style="width: 100%">
            <el-option label="班级" value="CLASS" />
            <el-option label="专业" value="MAJOR" />
            <el-option label="科目" value="SUBJECT" />
            <el-option label="学期" value="SEMESTER" />
          </el-select>
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input v-model="form.code" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" />
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
import { getDictPage, createDict, updateDict, deleteDict } from '@/api/dict'
import { debounce } from 'lodash-es'

const loading = ref(false)
const dictList = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const filterQuery = reactive({
  type: '',
  pageNum: 1,
  pageSize: 10,
})

const form = reactive({
  id: null,
  type: 'CLASS',
  code: '',
  name: '',
  sortOrder: 0,
  status: 1,
  remark: '',
})

const rules = {
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
  code: [{ required: true, message: '请输入编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getDictPage(filterQuery)
    dictList.value = res.data.records
    total.value = res.data.total
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const debouncedLoad = debounce(loadData, 300)
watch(() => filterQuery.type, () => {
  filterQuery.pageNum = 1
  debouncedLoad()
})

onMounted(loadData)

const formatDictType = (type) => {
  const map = { CLASS: '班级', MAJOR: '专业', SUBJECT: '科目', SEMESTER: '学期' }
  return map[type] || type
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, { id: null, type: 'CLASS', code: '', name: '', sortOrder: 0, status: 1, remark: '' })
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
      await updateDict(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await createDict(form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm(`确定要删除「${row.name}」吗？`, '提示', { type: 'warning' })
  try {
    await deleteDict(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    console.error(e)
  }
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
