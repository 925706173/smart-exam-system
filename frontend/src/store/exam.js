import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import {
  startExam as startExamApi,
  getExamQuestions,
  getExamQuestionIds,
  submitAnswer as submitAnswerApi,
  saveDraft as saveDraftApi,
  submitExam as submitExamApi,
} from '@/api/exam'

export const useExamStore = defineStore('exam', () => {
  // 考试状态
  const recordId = ref(null)
  const paperTitle = ref('')
  const duration = ref(0)
  const totalScore = ref(0)
  const startTime = ref(null)
  const endTime = ref(null)
  const questionCount = ref(0)

  // 题目状态
  const questions = ref([])          // 当前页题目（含选项）
  const questionIds = ref([])        // 全部题目ID（答题卡用）
  const answers = ref({})
  const currentIndex = ref(0)
  const totalPages = ref(0)
  const currentPage = ref(1)

  // 计时器
  const remaining = ref(0)
  let timer = null

  // 草稿定时器
  let draftTimer = null

  // 计算属性
  const isFinished = computed(() => remaining.value <= 0)
  const answeredCount = computed(() => {
    return Object.values(answers.value).filter((a) => a && a.trim() !== '').length
  })

  // 开始考试
  async function start(paperId) {
    const res = await startExamApi(paperId)
    const data = res.data

    recordId.value = data.recordId
    paperTitle.value = data.paperTitle
    duration.value = data.duration
    totalScore.value = data.totalScore
    startTime.value = data.startTime
    endTime.value = data.endTime
    questionCount.value = data.questionCount

    // 计算剩余时间（秒）
    const end = new Date(data.endTime).getTime()
    const now = Date.now()
    remaining.value = Math.max(0, Math.floor((end - now) / 1000))

    // 预加载前3题
    await loadQuestions(1)
    if (totalPages.value > 1) {
      // 预缓存第2页
      getExamQuestions(recordId.value, 2)
    }

    // 启动倒计时
    startTimer()

    // 启动草稿定时保存（30秒）
    startDraftTimer()

    return data
  }

  // 加载已有考试（ExamRoom进入时使用，store中需已有数据）
  async function loadExam(rid) {
    // 如果store中已有该考试数据（从ExamHall跳转过来）
    if (recordId.value && String(recordId.value) === String(rid)) {
      await Promise.all([loadQuestions(1), loadQuestionIds()])
      startTimer()
      startDraftTimer()
      return true
    }
    // 否则用recordId加载题目（页面刷新场景）
    recordId.value = Number(rid)
    await Promise.all([loadQuestions(1), loadQuestionIds()])
    // 使用默认时长，后续可从接口获取
    remaining.value = 120 * 60
    startTimer()
    startDraftTimer()
    return true
  }

  // 加载全部题目ID（答题卡用）
  async function loadQuestionIds() {
    try {
      const res = await getExamQuestionIds(recordId.value)
      if (res && res.data) {
        questionIds.value = res.data.questionIds || []
        // 合并已保存的答案
        if (res.data.answers) {
          Object.assign(answers.value, res.data.answers)
        }
        // 更新题目数量
        if (res.data.total) {
          questionCount.value = res.data.total
        }
      }
    } catch (e) {
      // 接口不存在时静默降级，用questionCount生成序号
      console.warn('题目ID接口不可用，使用降级方案')
      if (questionCount.value > 0 && questionIds.value.length === 0) {
        questionIds.value = Array.from({ length: questionCount.value }, (_, i) => i)
      }
    }
  }

  // 加载题目（无刷新切题）
  async function loadQuestions(page) {
    const res = await getExamQuestions(recordId.value, page)
    if (!res || !res.data) {
      throw new Error('获取题目数据失败：响应为空')
    }
    questions.value = res.data.questions || []
    totalPages.value = Math.ceil((res.data.total || 0) / (res.data.pageSize || 10))
    currentPage.value = page

    // 合并已保存的答案
    if (res.data.answers) {
      Object.assign(answers.value, res.data.answers)
    }
  }

  // 切换题目（异步加载，Vue响应式渲染）
  async function goToQuestion(index) {
    const page = Math.floor(index / 10) + 1
    if (page !== currentPage.value) {
      await loadQuestions(page)
    }
    currentIndex.value = index % 10
  }

  // 保存答案
  function setAnswer(questionId, answer) {
    answers.value[questionId] = answer
  }

  // 提交单题答案（Ajax）
  async function submitSingleAnswer(questionId, answer) {
    if (!recordId.value) return
    await submitAnswerApi(recordId.value, questionId, answer)
  }

  // 启动倒计时
  function startTimer() {
    if (timer) clearInterval(timer)
    timer = setInterval(() => {
      remaining.value--
      if (remaining.value <= 0) {
        clearInterval(timer)
        // 自动交卷
        submit()
      }
    }, 1000)
  }

  // 启动草稿定时保存
  function startDraftTimer() {
    if (draftTimer) clearInterval(draftTimer)
    draftTimer = setInterval(() => {
      saveDraft()
    }, 30000) // 每30秒
  }

  // 保存草稿到Redis（Ajax静默POST）
  async function saveDraft() {
    if (!recordId.value) return
    try {
      await saveDraftApi({
        recordId: recordId.value,
        answers: answers.value,
      })
    } catch (e) {
      // 断网时序列化到localStorage
      localStorage.setItem(
        `draft_${recordId.value}`,
        JSON.stringify(answers.value)
      )
    }
  }

  // 交卷
  async function submit() {
    // 停止定时器
    if (timer) clearInterval(timer)
    if (draftTimer) clearInterval(draftTimer)

    // 最后一次保存草稿
    await saveDraft()

    // 交卷
    const res = await submitExamApi(recordId.value)

    // 清理状态
    cleanup()

    return res
  }

  // 停止定时器（不清理状态，用于组件卸载）
  function stopTimers() {
    if (timer) {
      clearInterval(timer)
      timer = null
    }
    if (draftTimer) {
      clearInterval(draftTimer)
      draftTimer = null
    }
  }

  // 清理状态
  function cleanup() {
    // 先清理localStorage草稿（需要recordId）
    if (recordId.value) {
      localStorage.removeItem(`draft_${recordId.value}`)
    }

    recordId.value = null
    paperTitle.value = ''
    duration.value = 0
    totalScore.value = 0
    startTime.value = null
    endTime.value = null
    questionCount.value = 0
    questions.value = []
    questionIds.value = []
    answers.value = {}
    currentIndex.value = 0
    totalPages.value = 0
    currentPage.value = 1
    remaining.value = 0

    if (timer) clearInterval(timer)
    if (draftTimer) clearInterval(draftTimer)
    timer = null
    draftTimer = null
  }

  // 格式化剩余时间
  function formatTime() {
    const hours = Math.floor(remaining.value / 3600)
    const minutes = Math.floor((remaining.value % 3600) / 60)
    const seconds = remaining.value % 60
    return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`
  }

  return {
    recordId,
    paperTitle,
    duration,
    totalScore,
    startTime,
    endTime,
    questionCount,
    questions,
    questionIds,
    answers,
    currentIndex,
    totalPages,
    currentPage,
    remaining,
    isFinished,
    answeredCount,
    start,
    loadExam,
    loadQuestions,
    loadQuestionIds,
    goToQuestion,
    setAnswer,
    submitSingleAnswer,
    saveDraft,
    submit,
    cleanup,
    stopTimers,
    formatTime,
  }
})
