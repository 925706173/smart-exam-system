<template>
  <div class="paper-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>试卷管理</span>
          <el-button type="primary" @click="handleAdd">创建试卷</el-button>
        </div>
      </template>

      <el-table :data="paperList" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="试卷标题" min-width="200" />
        <el-table-column prop="totalScore" label="总分" width="80" />
        <el-table-column prop="duration" label="时长(分钟)" width="100" />
        <el-table-column label="考试时间" width="300">
          <template #default="{ row }">
            {{ formatDateTime(row.startTime) }} ~ {{ formatDateTime(row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="openQuestionDrawer(row)">管理题目</el-button>
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button
              v-if="row.status === 'DRAFT'"
              size="small"
              type="success"
              @click="handlePublish(row)"
            >
              发布
            </el-button>
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
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑试卷' : '创建试卷'" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="试卷标题" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="科目">
          <el-input v-model="form.subjectName" placeholder="请输入科目名称" />
        </el-form-item>
        <el-form-item label="考试时长" prop="duration">
          <el-input-number v-model="form.duration" :min="10" :max="300" />
          <span style="margin-left: 8px">分钟</span>
        </el-form-item>
        <el-form-item label="总分" prop="totalScore">
          <el-input-number v-model="form.totalScore" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="及格分数" prop="passScore">
          <el-input-number v-model="form.passScore" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="可考次数">
          <el-input-number v-model="form.maxAttempts" :min="1" :max="99" />
          <span style="margin-left: 8px">次</span>
        </el-form-item>
        <el-form-item label="考试时间">
          <el-date-picker
            v-model="form.timeRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
          />
        </el-form-item>
        <el-form-item label="乱序题目">
          <el-switch v-model="form.shuffleQuestion" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="乱序选项">
          <el-switch v-model="form.shuffleOption" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="指定班级">
          <el-select v-model="form.classIds" multiple placeholder="不指定则所有学生可见" style="width: 100%">
            <el-option v-for="cls in classOptions" :key="cls.id" :label="cls.name" :value="cls.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 试卷题目管理抽屉 -->
    <el-drawer v-model="drawerVisible" :title="'管理题目 - ' + currentPaper.title" size="70%" direction="rtl">
      <div class="drawer-content">
        <!-- 顶部操作栏 -->
        <div class="drawer-toolbar">
          <div class="toolbar-left">
            <span>共 {{ paperQuestions.length }} 题，总分 {{ currentPaper.totalScore }} 分</span>
          </div>
          <div class="toolbar-right">
            <el-button type="success" @click="openNewQuestionDialog">新增题目</el-button>
            <el-button type="primary" @click="showAddQuestionDialog">从题库添加题目</el-button>
          </div>
        </div>

        <!-- 题目列表 -->
        <el-table :data="paperQuestions" v-loading="drawerLoading" stripe>
          <el-table-column prop="sortOrder" label="序号" width="60" />
          <el-table-column prop="questionId" label="题ID" width="60" />
          <el-table-column prop="title" label="题干" min-width="150" show-overflow-tooltip />
          <el-table-column label="题型" width="80">
            <template #default="{ row }">
              <el-tag size="small">{{ formatQuestionType(row.type) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="参考答案" width="80" show-overflow-tooltip>
            <template #default="{ row }">
              {{ row.answer }}
            </template>
          </el-table-column>
          <el-table-column label="难度" width="100">
            <template #default="{ row }">
              <el-rate v-model="row.difficulty" disabled :max="5" size="small" class="rate-compact" />
            </template>
          </el-table-column>
          <el-table-column label="分值" width="130">
            <template #default="{ row }">
              <el-input-number
                v-model="row.score"
                :min="0"
                :precision="2"
                size="small"
                @change="handleScoreChange(row)"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="140" fixed="right">
            <template #default="{ row }">
              <el-button size="small" type="primary" @click="handleEditQuestion(row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleRemoveQuestion(row)">移除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-drawer>

    <!-- 从题库添加题目弹窗 -->
    <el-dialog v-model="addQuestionVisible" title="从题库添加题目" width="800px" append-to-body>
      <div class="add-question-dialog">
        <!-- 搜索栏 -->
        <el-form :inline="true" class="search-form">
          <el-form-item label="题型">
            <el-select v-model="searchQuery.type" placeholder="全部" clearable style="width: 120px">
              <el-option label="单选题" value="SINGLE" />
              <el-option label="多选题" value="MULTI" />
              <el-option label="判断题" value="JUDGE" />
              <el-option label="填空题" value="FILL" />
              <el-option label="主观题" value="SUBJECTIVE" />
            </el-select>
          </el-form-item>
          <el-form-item label="关键词">
            <el-input v-model="searchQuery.keyword" placeholder="搜索题干" clearable style="width: 200px" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchQuestions">搜索</el-button>
          </el-form-item>
        </el-form>

        <!-- 题目列表 -->
        <el-table
          :data="bankQuestions"
          v-loading="searchLoading"
          stripe
          height="350"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="45" />
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column prop="title" label="题干" min-width="250" show-overflow-tooltip />
          <el-table-column label="题型" width="80">
            <template #default="{ row }">
              <el-tag size="small">{{ formatQuestionType(row.type) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="难度" width="80">
            <template #default="{ row }">
              <el-rate v-model="row.difficulty" disabled :max="5" size="small" class="rate-compact" />
            </template>
          </el-table-column>
          <el-table-column prop="score" label="默认分值" width="80" />
        </el-table>

        <el-pagination
          v-model:current-page="searchQuery.pageNum"
          v-model:page-size="searchQuery.pageSize"
          :total="searchTotal"
          layout="total, prev, pager, next"
          style="margin-top: 10px; justify-content: flex-end"
          @current-change="searchQuestions"
        />

        <!-- 每题分值设置 -->
        <div class="add-question-footer">
          <span>每题分值：</span>
          <el-input-number v-model="addScore" :min="0.5" :precision="2" :step="1" />
          <el-button type="primary" style="margin-left: 12px" :disabled="selectedQuestions.length === 0" @click="batchAddQuestions">
            添加选中的 {{ selectedQuestions.length }} 道题
          </el-button>
        </div>
      </div>
    </el-dialog>

    <!-- 新增/编辑题目弹窗 -->
    <el-dialog v-model="newQuestionVisible" :title="isEditQuestion ? '编辑题目' : '新增题目'" width="700px" append-to-body>
      <el-form ref="newFormRef" :model="newForm" :rules="newFormRules" label-width="80px">
        <el-form-item label="题型" prop="type">
          <el-select v-model="newForm.type" style="width: 100%">
            <el-option label="单选题" value="SINGLE" />
            <el-option label="多选题" value="MULTI" />
            <el-option label="判断题" value="JUDGE" />
            <el-option label="填空题" value="FILL" />
            <el-option label="主观题" value="SUBJECTIVE" />
          </el-select>
        </el-form-item>
        <el-form-item label="题干" prop="title">
          <div style="width: 100%">
            <el-input v-model="newForm.title" type="textarea" :rows="3" placeholder="请输入题目内容，可粘贴包含A B C D选项的整段文字，然后点击一键识别" />
            <el-button
              v-if="newForm.type === 'SINGLE' || newForm.type === 'MULTI'"
              type="primary"
              link
              size="small"
              style="margin-top: 4px"
              @click="parseNewFormOptions"
            >
              一键识别选项
            </el-button>
          </div>
        </el-form-item>
        <el-form-item label="难度" prop="difficulty">
          <el-rate v-model="newForm.difficulty" :max="5" />
        </el-form-item>
        <el-form-item label="分值" prop="score">
          <el-input-number v-model="newForm.score" :min="0" :precision="2" />
        </el-form-item>

        <!-- 选项区域 -->
        <el-form-item v-if="newForm.type === 'SINGLE' || newForm.type === 'MULTI'" label="选项">
          <div class="options-list">
            <div v-for="(opt, idx) in newForm.options" :key="idx" class="option-item">
              <span class="option-label">{{ newOptionLabels[idx] }}.</span>
              <el-input v-model="newForm.options[idx]" :placeholder="'请输入选项' + newOptionLabels[idx]" />
            </div>
            <div class="option-actions">
              <el-button size="small" @click="addNewOption" :disabled="newForm.options.length >= 8">添加选项</el-button>
              <el-button size="small" @click="removeNewOption" :disabled="newForm.options.length <= 2">删除选项</el-button>
            </div>
          </div>
        </el-form-item>

        <el-form-item label="参考答案" prop="answer">
          <el-radio-group v-if="newForm.type === 'SINGLE'" v-model="newForm.answer">
            <el-radio v-for="(opt, idx) in newForm.options" :key="idx" :value="newOptionLabels[idx]">
              {{ newOptionLabels[idx] }}
            </el-radio>
          </el-radio-group>
          <el-checkbox-group v-else-if="newForm.type === 'MULTI'" v-model="newMultiAnswer">
            <el-checkbox v-for="(opt, idx) in newForm.options" :key="idx" :value="newOptionLabels[idx]">
              {{ newOptionLabels[idx] }}
            </el-checkbox>
          </el-checkbox-group>
          <el-radio-group v-else-if="newForm.type === 'JUDGE'" v-model="newForm.answer">
            <el-radio value="true">√ 正确</el-radio>
            <el-radio value="false">× 错误</el-radio>
          </el-radio-group>
          <el-input v-else v-model="newForm.answer" type="textarea" :rows="3" placeholder="请输入参考答案" />
        </el-form-item>

        <el-form-item label="解析">
          <el-input v-model="newForm.explanation" type="textarea" :rows="2" placeholder="请输入解析（选填）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="newQuestionVisible = false">取消</el-button>
        <el-button v-if="!isEditQuestion" type="primary" :loading="newQuestionLoading" @click="handleCreateAndAdd">创建并添加到试卷</el-button>
        <el-button v-else type="primary" :loading="newQuestionLoading" @click="handleUpdateQuestion">保存修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getPaperPage, createPaper, updatePaper, publishPaper, deletePaper,
  getPaperQuestions, addPaperQuestion, updatePaperQuestion, removePaperQuestion,
  assignClasses, getPaperClassIds,
} from '@/api/paper'
import { getQuestionPage, createQuestion, getQuestionDetail, updateQuestion } from '@/api/question'
import { getClassAll } from '@/api/class'
import { formatDateTime, formatQuestionType } from '@/utils/format'

const loading = ref(false)
const paperList = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  title: '',
  subjectName: '',
  duration: 120,
  totalScore: 100,
  passScore: 60,
  maxAttempts: 3,
  timeRange: [],
  shuffleQuestion: 1,
  shuffleOption: 1,
  classIds: [],
})

const classOptions = ref([])
const loadClassOptions = async () => {
  try {
    const res = await getClassAll()
    classOptions.value = res.data
  } catch (e) { console.error(e) }
}

const rules = {
  title: [{ required: true, message: '请输入试卷标题', trigger: 'blur' }],
  duration: [{ required: true, message: '请输入考试时长', trigger: 'blur' }],
  totalScore: [{ required: true, message: '请输入总分', trigger: 'blur' }],
  passScore: [{ required: true, message: '请输入及格分数', trigger: 'blur' }],
}

// 试卷题目管理
const drawerVisible = ref(false)
const drawerLoading = ref(false)
const currentPaper = ref({})
const paperQuestions = ref([])

// 添加题目弹窗
const addQuestionVisible = ref(false)
const searchLoading = ref(false)
const bankQuestions = ref([])
const searchTotal = ref(0)
const selectedQuestions = ref([])
const addScore = ref(5)
const searchQuery = reactive({ type: '', keyword: '', pageNum: 1, pageSize: 10 })

// 新增/编辑题目弹窗
const newQuestionVisible = ref(false)
const newQuestionLoading = ref(false)
const newFormRef = ref(null)
const newMultiAnswer = ref([])
const isEditQuestion = ref(false)
const editingQuestionId = ref(null)
const loadingDetail = ref(false)
const generateLabels = (count) => Array.from({ length: count }, (_, i) => String.fromCharCode(65 + i))
const newOptionLabels = computed(() => generateLabels(newForm.options.length))

const newForm = reactive({
  type: 'SINGLE',
  title: '',
  difficulty: 3,
  score: 5,
  answer: '',
  explanation: '',
  options: ['', '', '', ''],
})

const newFormRules = {
  type: [{ required: true, message: '请选择题型', trigger: 'change' }],
  title: [{ required: true, message: '请输入题干', trigger: 'blur' }],
  difficulty: [{ required: true, message: '请选择难度', trigger: 'change' }],
  score: [{ required: true, message: '请输入分值', trigger: 'blur' }],
  answer: [{ required: true, message: '请输入参考答案', trigger: 'blur' }],
}

// 多选答案数组 → 逗号分隔字符串
watch(newMultiAnswer, (val) => {
  if (loadingDetail.value) return
  newForm.answer = val.sort().join(',')
})

// 切换题型时重置答案和选项
watch(() => newForm.type, (newType) => {
  if (loadingDetail.value) return
  newForm.answer = ''
  newMultiAnswer.value = []
  if (newType === 'SINGLE' || newType === 'MULTI') {
    newForm.options = ['', '', '', '']
  }
})

const addNewOption = () => {
  if (newForm.options.length < 8) newForm.options.push('')
}

const removeNewOption = () => {
  if (newForm.options.length > 2) {
    const removed = newOptionLabels.value[newForm.options.length - 1]
    newForm.options.pop()
    if (newForm.answer === removed) newForm.answer = ''
    newMultiAnswer.value = newMultiAnswer.value.filter(a => a !== removed)
  }
}

// 一键识别选项
const parseNewFormOptions = () => {
  const text = newForm.title
  if (!text || !text.trim()) {
    ElMessage.warning('请先输入题干内容')
    return
  }
  const pattern = /([A-H])\s*[.、)：:]\s*/gi
  const matches = []
  let match
  while ((match = pattern.exec(text)) !== null) {
    matches.push({ letter: match[1].toUpperCase(), index: match.index, fullMatch: match[0] })
  }
  if (matches.length < 2) {
    ElMessage.warning('未识别到足够的选项（至少需要2个，如 A. B. C. D.）')
    return
  }
  const parsed = []
  for (let i = 0; i < matches.length; i++) {
    const start = matches[i].index + matches[i].fullMatch.length
    const end = i + 1 < matches.length ? matches[i + 1].index : text.length
    let content = text.substring(start, end).trim().replace(/[,;，；\s]+$/, '').trim()
    parsed.push({ letter: matches[i].letter, content })
  }
  const titleEnd = matches[0].index
  const titlePart = text.substring(0, titleEnd).trim()
  if (titlePart) newForm.title = titlePart
  newForm.options = parsed.map(p => p.content)
  newForm.answer = ''
  newMultiAnswer.value = []
  ElMessage.success(`已识别 ${parsed.length} 个选项`)
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getPaperPage({ pageNum: pageNum.value, pageSize: pageSize.value })
    paperList.value = res.data.records
    total.value = res.data.total
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
  loadClassOptions()
})

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null, title: '', subjectName: '', duration: 120, totalScore: 100, passScore: 60,
    maxAttempts: 3, timeRange: [], shuffleQuestion: 1, shuffleOption: 1, classIds: [],
  })
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  isEdit.value = true
  Object.assign(form, {
    ...row,
    timeRange: [row.startTime, row.endTime],
    classIds: [],
  })
  dialogVisible.value = true
  // Load existing class associations
  try {
    const res = await getPaperClassIds(row.id)
    form.classIds = res.data || []
  } catch (e) { console.error(e) }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    const data = {
      ...form,
      startTime: form.timeRange?.[0],
      endTime: form.timeRange?.[1],
    }
    const classIds = data.classIds
    delete data.timeRange
    delete data.classIds

    let paperId = form.id
    if (isEdit.value) {
      await updatePaper(form.id, data)
      ElMessage.success('更新成功')
    } else {
      const res = await createPaper(data)
      paperId = res.data
      ElMessage.success('创建成功')
    }
    // Save class associations (empty array clears all associations)
    await assignClasses(paperId, classIds || [])
    dialogVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handlePublish = async (row) => {
  await ElMessageBox.confirm('确定要发布该试卷吗？发布后学生可以开始考试。', '提示', { type: 'warning' })
  try {
    await publishPaper(row.id)
    ElMessage.success('发布成功')
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定要删除该试卷吗？', '提示', { type: 'warning' })
  try {
    await deletePaper(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    console.error(e)
  }
}

// 试卷题目管理
const openQuestionDrawer = async (row) => {
  currentPaper.value = row
  drawerVisible.value = true
  await loadPaperQuestions()
}

const loadPaperQuestions = async () => {
  drawerLoading.value = true
  try {
    const res = await getPaperQuestions(currentPaper.value.id)
    paperQuestions.value = res.data
  } catch (e) {
    console.error(e)
  } finally {
    drawerLoading.value = false
  }
}

const handleScoreChange = async (row) => {
  try {
    await updatePaperQuestion(currentPaper.value.id, row.id, row.score)
    // 刷新试卷信息（总分可能变了）
    const res = await getPaperPage({ pageNum: pageNum.value, pageSize: pageSize.value })
    const updated = res.data.records.find(p => p.id === currentPaper.value.id)
    if (updated) currentPaper.value = updated
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleRemoveQuestion = async (row) => {
  await ElMessageBox.confirm('确定要从试卷中移除该题目吗？', '提示', { type: 'warning' })
  try {
    await removePaperQuestion(currentPaper.value.id, row.id)
    ElMessage.success('已移除')
    await loadPaperQuestions()
    // 刷新试卷总分
    const res = await getPaperPage({ pageNum: pageNum.value, pageSize: pageSize.value })
    const updated = res.data.records.find(p => p.id === currentPaper.value.id)
    if (updated) currentPaper.value = updated
    loadData()
  } catch (e) {
    console.error(e)
  }
}

// 打开新增题目弹窗
const openNewQuestionDialog = () => {
  isEditQuestion.value = false
  editingQuestionId.value = null
  Object.assign(newForm, {
    type: 'SINGLE', title: '', difficulty: 3, score: 5, answer: '', explanation: '',
    options: ['', '', '', ''],
  })
  newMultiAnswer.value = []
  newQuestionVisible.value = true
}

// 打开编辑题目弹窗
const handleEditQuestion = async (row) => {
  loadingDetail.value = true
  try {
    const res = await getQuestionDetail(row.questionId)
    const q = res.data
    isEditQuestion.value = true
    editingQuestionId.value = q.id
    Object.assign(newForm, {
      type: q.type || 'SINGLE',
      title: q.title || '',
      difficulty: q.difficulty || 3,
      score: row.score || 5,
      answer: q.answer || '',
      explanation: q.explanation || '',
      options: (q.options && q.options.length > 0)
        ? q.options.map(o => o.optionContent || '')
        : ['', '', '', ''],
    })
    if (q.type === 'MULTI' && q.answer) {
      newMultiAnswer.value = q.answer.split(',')
    } else {
      newMultiAnswer.value = []
    }
    await nextTick()
    newForm.answer = q.answer || ''
    newQuestionVisible.value = true
  } catch (e) {
    console.error(e)
  } finally {
    loadingDetail.value = false
  }
}

// 保存编辑的题目
const handleUpdateQuestion = async () => {
  await newFormRef.value.validate()
  newQuestionLoading.value = true
  try {
    const submitData = {
      ...newForm,
      options: (newForm.type === 'SINGLE' || newForm.type === 'MULTI')
        ? newForm.options.map((content, idx) => ({
            optionLabel: newOptionLabels.value[idx],
            optionContent: content,
            sortOrder: idx,
          }))
        : null,
    }
    await updateQuestion(editingQuestionId.value, submitData)
    ElMessage.success('题目已更新')
    newQuestionVisible.value = false
    await loadPaperQuestions()
  } catch (e) {
    console.error(e)
  } finally {
    newQuestionLoading.value = false
  }
}

// 创建题目并添加到试卷
const handleCreateAndAdd = async () => {
  await newFormRef.value.validate()
  newQuestionLoading.value = true
  try {
    // 构造提交数据，将字符串选项转为QuestionOption对象
    const submitData = {
      ...newForm,
      options: (newForm.type === 'SINGLE' || newForm.type === 'MULTI')
        ? newForm.options.map((content, idx) => ({
            optionLabel: newOptionLabels.value[idx],
            optionContent: content,
            sortOrder: idx,
          }))
        : null,
    }
    // 1. 创建题目
    const res = await createQuestion(submitData)
    const questionId = res.data
    // 2. 添加到试卷
    await addPaperQuestion(currentPaper.value.id, questionId, newForm.score)
    ElMessage.success('题目已创建并添加到试卷')
    newQuestionVisible.value = false
    await loadPaperQuestions()
    // 刷新试卷信息
    const paperRes = await getPaperPage({ pageNum: pageNum.value, pageSize: pageSize.value })
    const updated = paperRes.data.records.find(p => p.id === currentPaper.value.id)
    if (updated) currentPaper.value = updated
    loadData()
  } catch (e) {
    console.error(e)
  } finally {
    newQuestionLoading.value = false
  }
}

// 从题库添加题目
const showAddQuestionDialog = () => {
  addQuestionVisible.value = true
  selectedQuestions.value = []
  searchQuestions()
}

const searchQuestions = async () => {
  searchLoading.value = true
  try {
    const res = await getQuestionPage({
      type: searchQuery.type,
      keyword: searchQuery.keyword,
      pageNum: searchQuery.pageNum,
      pageSize: searchQuery.pageSize,
    })
    bankQuestions.value = res.data.records
    searchTotal.value = res.data.total
  } catch (e) {
    console.error(e)
  } finally {
    searchLoading.value = false
  }
}

const handleSelectionChange = (val) => {
  selectedQuestions.value = val
}

const batchAddQuestions = async () => {
  try {
    for (const q of selectedQuestions.value) {
      // 使用题库中题目的原始分值，而不是默认的addScore
      const score = q.score || addScore.value
      await addPaperQuestion(currentPaper.value.id, q.id, score)
    }
    ElMessage.success(`成功添加 ${selectedQuestions.value.length} 道题`)
    addQuestionVisible.value = false
    await loadPaperQuestions()
    // 刷新试卷信息
    const res = await getPaperPage({ pageNum: pageNum.value, pageSize: pageSize.value })
    const updated = res.data.records.find(p => p.id === currentPaper.value.id)
    if (updated) currentPaper.value = updated
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const getStatusType = (status) => {
  const map = { DRAFT: 'info', PUBLISHED: 'success', CLOSED: 'warning' }
  return map[status] || 'info'
}

const getStatusLabel = (status) => {
  const map = { DRAFT: '草稿', PUBLISHED: '已发布', CLOSED: '已结束' }
  return map[status] || status
}
</script>

<style scoped>
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.drawer-content {
  padding: 0 10px;
}

.drawer-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
  padding: 8px 0;
  border-bottom: 1px solid #eee;
}

.toolbar-left {
  font-size: 14px;
  color: #666;
}

.toolbar-right {
  display: flex;
  gap: 8px;
}

.search-form {
  margin-bottom: 12px;
}

.add-question-footer {
  display: flex;
  align-items: center;
  margin-top: 16px;
  padding-top: 12px;
  border-top: 1px solid #eee;
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
