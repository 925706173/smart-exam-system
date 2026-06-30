<template>
  <div class="question-bank">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>题库管理</span>
          <div>
            <el-button type="primary" @click="handleAdd">新增题目</el-button>
            <el-upload
              :show-file-list="false"
              :before-upload="handleImport"
              accept=".xlsx,.xls"
              style="display: inline-block; margin-left: 12px"
            >
              <el-button>Excel导入</el-button>
            </el-upload>
          </div>
        </div>
      </template>

      <!-- 筛选表单（动态筛选，防抖搜索） -->
      <el-form :model="filterQuery" inline class="filter-form">
        <el-form-item label="题型">
          <el-select v-model="filterQuery.type" placeholder="全部" clearable style="width: 120px">
            <el-option label="单选题" value="SINGLE" />
            <el-option label="多选题" value="MULTI" />
            <el-option label="判断题" value="JUDGE" />
            <el-option label="填空题" value="FILL" />
            <el-option label="主观题" value="SUBJECTIVE" />
          </el-select>
        </el-form-item>
        <el-form-item label="难度">
          <el-select v-model="filterQuery.difficulty" placeholder="全部" clearable style="width: 100px">
            <el-option v-for="i in 5" :key="i" :label="formatDifficulty(i)" :value="i" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input
            v-model="filterQuery.keyword"
            placeholder="搜索题干/答案"
            clearable
            style="width: 200px"
          />
        </el-form-item>
      </el-form>

      <!-- 题目列表 -->
      <el-table :data="questionList" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="题干" min-width="300" show-overflow-tooltip />
        <el-table-column label="题型" width="100">
          <template #default="{ row }">
            <el-tag>{{ formatQuestionType(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="难度" width="100">
          <template #default="{ row }">
            <el-rate v-model="row.difficulty" disabled :max="5" class="rate-compact" />
          </template>
        </el-table-column>
        <el-table-column prop="score" label="分值" width="80" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'PUBLISHED' ? 'success' : 'info'">
              {{ row.status === 'PUBLISHED' ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" @click="handleView(row)">查看</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
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
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑题目' : '新增题目'"
      width="700px"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="题型" prop="type">
          <el-select v-model="form.type" style="width: 100%">
            <el-option label="单选题" value="SINGLE" />
            <el-option label="多选题" value="MULTI" />
            <el-option label="判断题" value="JUDGE" />
            <el-option label="填空题" value="FILL" />
            <el-option label="主观题" value="SUBJECTIVE" />
          </el-select>
        </el-form-item>
        <el-form-item label="题干" prop="title">
          <div style="width: 100%">
            <el-input v-model="form.title" type="textarea" :rows="3" placeholder="请输入题目内容，可粘贴包含A B C D选项的整段文字，然后点击一键识别" />
            <el-button
              v-if="form.type === 'SINGLE' || form.type === 'MULTI'"
              type="primary"
              link
              size="small"
              style="margin-top: 4px"
              @click="parseOptions"
            >
              一键识别选项
            </el-button>
          </div>
        </el-form-item>
        <el-form-item label="难度" prop="difficulty">
          <el-rate v-model="form.difficulty" :max="5" />
        </el-form-item>
        <el-form-item label="分值" prop="score">
          <el-input-number v-model="form.score" :min="0" :precision="2" />
        </el-form-item>

        <!-- 选项区域（单选题/多选题） -->
        <el-form-item v-if="form.type === 'SINGLE' || form.type === 'MULTI'" label="选项">
          <div class="options-list">
            <div v-for="(opt, idx) in form.options" :key="idx" class="option-item">
              <span class="option-label">{{ optionLabels[idx] }}.</span>
              <el-input v-model="form.options[idx]" :placeholder="'请输入选项' + optionLabels[idx]" />
            </div>
            <div class="option-actions">
              <el-button :icon="Plus" size="small" @click="addOption" :disabled="form.options.length >= 8">
                添加选项
              </el-button>
              <el-button :icon="Minus" size="small" @click="removeOption" :disabled="form.options.length <= 2">
                删除选项
              </el-button>
            </div>
          </div>
        </el-form-item>

        <el-form-item label="参考答案" prop="answer">
          <!-- 单选题 -->
          <el-radio-group v-if="form.type === 'SINGLE'" v-model="form.answer">
            <el-radio v-for="(opt, idx) in form.options" :key="idx" :value="optionLabels[idx]">
              {{ optionLabels[idx] }}
            </el-radio>
          </el-radio-group>
          <!-- 多选题 -->
          <el-checkbox-group v-else-if="form.type === 'MULTI'" v-model="multiAnswer">
            <el-checkbox v-for="(opt, idx) in form.options" :key="idx" :value="optionLabels[idx]">
              {{ optionLabels[idx] }}
            </el-checkbox>
          </el-checkbox-group>
          <!-- 判断题 -->
          <el-radio-group v-else-if="form.type === 'JUDGE'" v-model="form.answer">
            <el-radio value="true">√ 正确</el-radio>
            <el-radio value="false">× 错误</el-radio>
          </el-radio-group>
          <!-- 填空题、主观题 -->
          <el-input v-else v-model="form.answer" type="textarea" :rows="3" placeholder="请输入参考答案" />
        </el-form-item>

        <el-form-item label="解析">
          <el-input v-model="form.explanation" type="textarea" :rows="2" placeholder="请输入解析（选填）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看题目详情弹窗 -->
    <el-dialog v-model="viewVisible" title="题目详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="题ID">{{ viewData.id }}</el-descriptions-item>
        <el-descriptions-item label="题型">
          <el-tag>{{ formatQuestionType(viewData.type) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="难度">
          <el-rate v-model="viewData.difficulty" disabled :max="5" />
        </el-descriptions-item>
        <el-descriptions-item label="分值">{{ viewData.score }}</el-descriptions-item>
        <el-descriptions-item label="状态" :span="2">
          <el-tag :type="viewData.status === 'PUBLISHED' ? 'success' : 'info'">
            {{ viewData.status === 'PUBLISHED' ? '已发布' : '草稿' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="题干" :span="2">{{ viewData.title }}</el-descriptions-item>
        <el-descriptions-item v-if="viewData.options && viewData.options.length" label="选项" :span="2">
          <div v-for="opt in viewData.options" :key="opt.id" style="margin-bottom: 4px">
            <strong>{{ opt.optionLabel }}.</strong> {{ opt.optionContent }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="参考答案" :span="2">
          <span style="color: #409eff; font-weight: bold">{{ viewData.answer }}</span>
        </el-descriptions-item>
        <el-descriptions-item v-if="viewData.explanation" label="解析" :span="2">
          {{ viewData.explanation }}
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="viewVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Minus } from '@element-plus/icons-vue'
import { getQuestionPage, createQuestion, updateQuestion, deleteQuestion, importQuestions, getQuestionDetail } from '@/api/question'
import { formatQuestionType, formatDifficulty } from '@/utils/format'
import { debounce } from 'lodash-es'

// 生成选项标签 A B C ...
const generateLabels = (count) => {
  return Array.from({ length: count }, (_, i) => String.fromCharCode(65 + i))
}

const optionLabels = computed(() => generateLabels(form.options.length))

const addOption = () => {
  if (form.options.length < 8) {
    form.options.push('')
  }
}

const removeOption = () => {
  if (form.options.length > 2) {
    const removed = optionLabels.value[form.options.length - 1]
    form.options.pop()
    if (form.answer === removed) form.answer = ''
    multiAnswer.value = multiAnswer.value.filter(a => a !== removed)
  }
}

// 一键识别：从题干中提取选项
const parseOptions = () => {
  const text = form.title
  if (!text || !text.trim()) {
    ElMessage.warning('请先输入题干内容')
    return
  }

  // 匹配 A. / A、 / A) / A： / A: / A  后面跟内容，直到下一个选项字母或结尾
  // 支持 A-D 或 A-H，选项之间可以是空格、换行、分号等分隔
  const pattern = /([A-H])\s*[.、)：:]\s*/gi
  const matches = []
  let match

  while ((match = pattern.exec(text)) !== null) {
    matches.push({
      letter: match[1].toUpperCase(),
      index: match.index,
      fullMatch: match[0],
    })
  }

  if (matches.length < 2) {
    ElMessage.warning('未识别到足够的选项（至少需要2个，如 A. B. C. D.）')
    return
  }

  // 提取每个选项的内容
  const parsed = []
  for (let i = 0; i < matches.length; i++) {
    const start = matches[i].index + matches[i].fullMatch.length
    const end = i + 1 < matches.length ? matches[i + 1].index : text.length
    let content = text.substring(start, end).trim()
    // 去掉末尾可能残留的分隔符
    content = content.replace(/[,;，；\s]+$/, '').trim()
    parsed.push({ letter: matches[i].letter, content })
  }

  // 检查字母是否连续（A B C D ...）
  const letters = parsed.map(p => p.letter)
  const expected = generateLabels(parsed.length)
  if (letters.join('') !== expected.join('') && letters.join('') !== [...expected].reverse().join('')) {
    // 不连续也没关系，按出现顺序重新映射为 A B C D ...
    // 但先检查是否有重复
    const unique = [...new Set(letters)]
    if (unique.length !== letters.length) {
      ElMessage.warning('识别到重复的选项字母，请检查格式')
      return
    }
  }

  // 题干 = 去掉第一个选项之前的文本（保留题目部分）
  const titleEnd = matches[0].index
  const titlePart = text.substring(0, titleEnd).trim()
  if (titlePart) {
    form.title = titlePart
  }

  // 填入选项
  form.options = parsed.map(p => p.content)
  // 清空已选答案（选项内容变了）
  form.answer = ''
  multiAnswer.value = []

  ElMessage.success(`已识别 ${parsed.length} 个选项`)
}

const loading = ref(false)
const loadingDetail = ref(false)
const questionList = ref([])

// 查看详情
const viewVisible = ref(false)
const viewData = reactive({
  id: null, type: '', title: '', difficulty: 3, score: 0,
  answer: '', explanation: '', status: '', options: [],
})
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

// 多选题答案（数组形式，提交时转为逗号分隔字符串）
const multiAnswer = ref([])

// 筛选条件（防抖搜索）
const filterQuery = reactive({
  type: '',
  difficulty: null,
  keyword: '',
  pageNum: 1,
  pageSize: 10,
})

// 表单
const form = reactive({
  id: null,
  type: 'SINGLE',
  title: '',
  difficulty: 3,
  score: 0,
  answer: '',
  explanation: '',
  options: ['', '', '', ''],
})

const rules = {
  type: [{ required: true, message: '请选择题型', trigger: 'change' }],
  title: [{ required: true, message: '请输入题干', trigger: 'blur' }],
  difficulty: [{ required: true, message: '请选择难度', trigger: 'change' }],
  score: [{ required: true, message: '请输入分值', trigger: 'blur' }],
  answer: [{ required: true, message: '请输入参考答案', trigger: 'blur' }],
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await getQuestionPage(filterQuery)
    questionList.value = res.data.records
    total.value = res.data.total
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

// 防抖筛选（lodash.debounce）
const debouncedLoad = debounce(loadData, 300)
watch(
  () => [filterQuery.type, filterQuery.difficulty, filterQuery.keyword],
  () => {
    filterQuery.pageNum = 1
    debouncedLoad()
  }
)

// 多选答案数组 → 逗号分隔字符串
watch(multiAnswer, (val) => {
  if (loadingDetail.value) return
  form.answer = val.sort().join(',')
})

// 切换题型时重置答案和选项
watch(() => form.type, (newType) => {
  if (loadingDetail.value) return
  form.answer = ''
  multiAnswer.value = []
  if (newType === 'SINGLE' || newType === 'MULTI') {
    form.options = ['', '', '', '']
  }
})

onMounted(loadData)

// 新增
const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, { id: null, type: 'SINGLE', title: '', difficulty: 3, score: 0, answer: '', explanation: '', options: ['', '', '', ''] })
  multiAnswer.value = []
  dialogVisible.value = true
}

// 编辑
const handleEdit = async (row) => {
  isEdit.value = true
  loadingDetail.value = true
  try {
    const res = await getQuestionDetail(row.id)
    const q = res.data
    Object.assign(form, {
      id: q.id,
      type: q.type || 'SINGLE',
      title: q.title || '',
      difficulty: q.difficulty || 3,
      score: q.score || 0,
      answer: q.answer || '',
      explanation: q.explanation || '',
      options: (q.options && q.options.length > 0)
        ? q.options.map(o => o.optionContent || '')
        : ['', '', '', ''],
    })
    if (q.type === 'MULTI' && q.answer) {
      multiAnswer.value = q.answer.split(',')
    } else {
      multiAnswer.value = []
    }
    await nextTick()
    form.answer = q.answer || ''
  } catch (e) {
    console.error(e)
  } finally {
    loadingDetail.value = false
  }
  dialogVisible.value = true
}

// 查看
const handleView = async (row) => {
  try {
    const res = await getQuestionDetail(row.id)
    Object.assign(viewData, res.data)
    viewVisible.value = true
  } catch (e) {
    console.error(e)
  }
}

// 删除
const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定要删除该题目吗？', '提示', { type: 'warning' })
  try {
    await deleteQuestion(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    console.error(e)
  }
}

// 提交表单
const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    // 构造提交数据，将字符串选项转为QuestionOption对象
    const submitData = {
      ...form,
      options: (form.type === 'SINGLE' || form.type === 'MULTI')
        ? form.options.map((content, idx) => ({
            optionLabel: optionLabels.value[idx],
            optionContent: content,
            sortOrder: idx,
          }))
        : null,
    }
    if (isEdit.value) {
      await updateQuestion(form.id, submitData)
      ElMessage.success('更新成功')
    } else {
      await createQuestion(submitData)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

// Excel导入
const handleImport = async (file) => {
  try {
    const res = await importQuestions(file, 1) // TODO: 选择科目
    ElMessage.success(`导入完成：成功${res.data.successCount}条，失败${res.data.failCount}条`)
    loadData()
  } catch (e) {
    ElMessage.error('导入失败')
  }
  return false
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

.options-list {
  width: 100%;
}

.option-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
}

.option-label {
  font-weight: bold;
  min-width: 24px;
  text-align: right;
}

.option-actions {
  display: flex;
  gap: 8px;
  margin-top: 4px;
}

:deep(.rate-compact .el-rate__icon) {
  margin-right: 2px !important;
}

:deep(.rate-compact .el-rate__icon:last-child) {
  margin-right: 0 !important;
}
</style>
