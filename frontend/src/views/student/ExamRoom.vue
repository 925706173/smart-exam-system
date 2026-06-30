<template>
  <div class="exam-room">
    <!-- 顶部工具栏 -->
    <div class="exam-header">
      <div class="header-left">
        <h3>{{ examStore.paperTitle }}</h3>
      </div>
      <div class="header-center">
        <el-icon><Clock /></el-icon>
        <span class="timer" :class="{ warning: examStore.remaining < 300 }">
          {{ examStore.formatTime() }}
        </span>
      </div>
      <div class="header-right">
        <span>已答 {{ examStore.answeredCount }} / {{ examStore.questionCount }}</span>
        <el-button type="danger" @click="handleSubmit">交卷</el-button>
      </div>
    </div>

    <div class="exam-body">
      <!-- 答题卡（右侧） -->
      <div class="answer-card">
        <h4>答题卡</h4>
        <div class="card-grid">
          <div
            v-for="(item, index) in answerCardItems"
            :key="index"
            class="card-item"
            :class="{
              active: index === currentGlobalIndex,
              answered: isAnswered(index),
            }"
            @click="handleNavigate(index)"
          >
            {{ index + 1 }}
          </div>
        </div>
      </div>

      <!-- 题目区域（左侧） -->
      <div class="question-area">
        <div class="question-scroll" v-if="currentQuestion">
          <div class="question-header">
            <el-tag>{{ formatQuestionType(currentQuestion.type) }}</el-tag>
            <span class="score">分值：{{ currentQuestion.score }}分</span>
            <span class="difficulty">难度：{{ formatDifficulty(currentQuestion.difficulty) }}</span>
          </div>

          <div class="question-title" v-html="currentQuestion.title" />

          <!-- 单选题 -->
          <el-radio-group
            v-if="currentQuestion.type === 'SINGLE'"
            :model-value="answers[currentQuestion.id]"
            @change="(val) => handleAnswer(currentQuestion.id, val)"
            class="option-group"
          >
            <el-radio
              v-for="opt in currentQuestion.options"
              :key="opt.id"
              :value="opt.optionLabel"
              class="option-item"
            >
              {{ opt.optionLabel }}. {{ opt.optionContent }}
            </el-radio>
          </el-radio-group>

          <!-- 多选题 -->
          <el-checkbox-group
            v-else-if="currentQuestion.type === 'MULTI'"
            :model-value="answers[currentQuestion.id] ? answers[currentQuestion.id].split('') : []"
            @change="(val) => handleAnswer(currentQuestion.id, val.join(''))"
            class="option-group"
          >
            <el-checkbox
              v-for="opt in currentQuestion.options"
              :key="opt.id"
              :value="opt.optionLabel"
              class="option-item"
            >
              {{ opt.optionLabel }}. {{ opt.optionContent }}
            </el-checkbox>
          </el-checkbox-group>

          <!-- 判断题 -->
          <el-radio-group
            v-else-if="currentQuestion.type === 'JUDGE'"
            :model-value="answers[currentQuestion.id]"
            @change="(val) => handleAnswer(currentQuestion.id, val)"
            class="option-group"
          >
            <el-radio value="T" class="option-item">正确</el-radio>
            <el-radio value="F" class="option-item">错误</el-radio>
          </el-radio-group>

          <!-- 填空题/主观题 -->
          <el-input
            v-else
            type="textarea"
            :rows="6"
            :model-value="answers[currentQuestion.id]"
            @input="(val) => handleAnswer(currentQuestion.id, val)"
            placeholder="请输入答案..."
          />
        </div>

        <!-- 切题按钮（固定在底部） -->
        <div class="navigation">
          <el-button :disabled="currentGlobalIndex === 0" @click="goPrev">
            <el-icon><ArrowLeft /></el-icon> 上一题
          </el-button>
          <el-button type="primary" :disabled="currentGlobalIndex >= totalQuestions - 1" @click="goNext">
            下一题 <el-icon><ArrowRight /></el-icon>
          </el-button>
          <el-button type="danger" @click="handleSubmit" style="margin-left: 40px">
            交卷
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useExamStore } from '@/store/exam'
import { formatQuestionType, formatDifficulty } from '@/utils/format'

const route = useRoute()
const router = useRouter()
const examStore = useExamStore()

const recordId = route.params.recordId
const currentGlobalIndex = ref(0)

// 全部题目ID（答题卡用）和答案状态
const answers = computed(() => examStore.answers)
const totalQuestions = computed(() => examStore.questionCount)

// 答题卡数据：优先用questionIds，fallback用questionCount生成序号
const answerCardItems = computed(() => {
  if (examStore.questionIds && examStore.questionIds.length > 0) {
    return examStore.questionIds
  }
  // fallback：用题目数量生成序号数组
  return Array.from({ length: examStore.questionCount || 0 }, (_, i) => i)
})

// 当前题目（从当前页的题目列表中取）
const currentQuestion = computed(() => {
  const localIndex = currentGlobalIndex.value % 10
  return examStore.questions[localIndex] || null
})

// 判断某题是否已作答
const isAnswered = (index) => {
  const qids = examStore.questionIds
  if (qids && qids.length > 0) {
    const qid = qids[index]
    return answers.value[qid] && answers.value[qid].trim() !== ''
  }
  // fallback：检查当前页的题目
  const localIndex = index % 10
  const q = examStore.questions[localIndex]
  if (q && answers.value[q.id]) {
    return answers.value[q.id].trim() !== ''
  }
  return false
}

// 初始化
onMounted(async () => {
  try {
    const rid = recordId.replace(':', '')
    await examStore.loadExam(rid)
  } catch (e) {
    console.error('加载考试失败:', e)
    ElMessage.error('加载考试失败')
    router.push('/student/exam-hall')
  }
})

// 组件卸载时只清理定时器，不清理store状态
onUnmounted(() => {
  examStore.stopTimers()
})

// 处理答案变化（Ajax提交单题）
const handleAnswer = async (questionId, answer) => {
  examStore.setAnswer(questionId, answer)
  await examStore.submitSingleAnswer(questionId, answer)
}

// 切题
const handleNavigate = async (index) => {
  currentGlobalIndex.value = index
  await examStore.goToQuestion(index)
}

const goPrev = () => {
  if (currentGlobalIndex.value > 0) {
    handleNavigate(currentGlobalIndex.value - 1)
  }
}

const goNext = () => {
  if (currentGlobalIndex.value < totalQuestions.value - 1) {
    handleNavigate(currentGlobalIndex.value + 1)
  }
}

// 交卷
const handleSubmit = async () => {
  const unanswered = totalQuestions.value - examStore.answeredCount
  if (unanswered > 0) {
    await ElMessageBox.confirm(
      `还有 ${unanswered} 题未作答，确定要交卷吗？`,
      '交卷确认',
      { type: 'warning' }
    )
  }

  try {
    await examStore.submit()
    ElMessage.success('交卷成功')
    router.push('/student/history')
  } catch (e) {
    ElMessage.error('交卷失败')
  }
}
</script>

<style scoped>
.exam-room {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f5f5;
}

.exam-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 24px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 10;
}

.header-center {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  font-weight: bold;
}

.timer.warning {
  color: #f56c6c;
  animation: blink 1s infinite;
}

@keyframes blink {
  50% { opacity: 0.5; }
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.exam-body {
  flex: 1;
  display: flex;
  padding: 20px;
  gap: 20px;
  overflow: hidden;
}

.answer-card {
  width: 240px;
  background: #fff;
  border-radius: 8px;
  padding: 16px;
  overflow-y: auto;
}

.answer-card h4 {
  margin-bottom: 16px;
  font-size: 16px;
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 8px;
}

.card-item {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.card-item:hover {
  border-color: #409eff;
}

.card-item.active {
  background: #409eff;
  color: #fff;
  border-color: #409eff;
}

.card-item.answered {
  background: #67c23a;
  color: #fff;
  border-color: #67c23a;
}

.question-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: #fff;
  border-radius: 8px;
  padding: 24px;
}

.question-scroll {
  flex: 1;
  overflow-y: auto;
  padding-right: 8px;
}

.question-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
}

.question-title {
  font-size: 16px;
  line-height: 1.8;
  margin-bottom: 24px;
}

.option-group {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 16px;
}

.option-group :deep(.el-radio),
.option-group :deep(.el-checkbox) {
  margin-right: 0;
  height: auto;
}

.option-item {
  padding: 12px 16px;
  border: 1px solid #eee;
  border-radius: 8px;
  transition: all 0.2s;
}

.option-item:hover {
  background: #f5f7fa;
}

.navigation {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  padding-top: 16px;
  border-top: 1px solid #eee;
  flex-shrink: 0;
}
</style>
