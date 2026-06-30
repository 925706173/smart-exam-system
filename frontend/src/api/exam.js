import request from './request'

// 考试大厅 - 分页查询
export function getExamHall(params) {
  return request({
    url: '/exam/hall',
    method: 'get',
    params,
  })
}

// 开始考试
export function startExam(paperId) {
  return request({
    url: `/exam/start/${paperId}`,
    method: 'post',
  })
}

// 获取试卷题目（分页，无刷新切题）
export function getExamQuestions(recordId, page = 1, size = 10) {
  return request({
    url: `/exam/records/${recordId}/questions`,
    method: 'get',
    params: { page, size },
  })
}

// 获取题目ID列表（答题卡用，静默失败）
export function getExamQuestionIds(recordId) {
  return request({
    url: `/exam/records/${recordId}/question-ids`,
    method: 'get',
    silenceError: true,
  })
}

// 提交单题答案
export function submitAnswer(recordId, questionId, answer) {
  return request({
    url: `/exam/records/${recordId}/answer`,
    method: 'post',
    params: { questionId, answer },
  })
}

// 保存草稿到Redis
export function saveDraft(data) {
  return request({
    url: '/exam/draft',
    method: 'post',
    data,
  })
}

// 获取草稿
export function getDraft(recordId) {
  return request({
    url: `/exam/draft/${recordId}`,
    method: 'get',
  })
}

// 交卷
export function submitExam(recordId) {
  return request({
    url: `/exam/records/${recordId}/submit`,
    method: 'post',
  })
}

// 练习模式 - 单题即时判分
export function checkAnswer(questionId, answer) {
  return request({
    url: '/practice/check',
    method: 'post',
    params: { questionId, answer },
  })
}

// 错题本列表
export function getErrorBook(params) {
  return request({
    url: '/practice/error-book',
    method: 'get',
    params,
  })
}

// 错题复习
export function reviewError(id) {
  return request({
    url: `/practice/review/${id}`,
    method: 'post',
  })
}

// 试卷复查 - 已批改试卷列表
export function getReviewList() {
  return request({
    url: '/exam/review/list',
    method: 'get',
  })
}

// 试卷复查 - 查看详情
export function getReviewDetail(recordId) {
  return request({
    url: `/exam/review/${recordId}`,
    method: 'get',
  })
}
