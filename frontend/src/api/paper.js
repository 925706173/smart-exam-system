import request from './request'

// 分页查询试卷
export function getPaperPage(params) {
  return request({
    url: '/papers',
    method: 'get',
    params,
  })
}

// 获取试卷详情
export function getPaperDetail(id) {
  return request({
    url: `/papers/${id}`,
    method: 'get',
  })
}

// 创建试卷
export function createPaper(data) {
  return request({
    url: '/papers',
    method: 'post',
    data,
  })
}

// 规则抽题组卷
export function generatePaper(data, ruleConfig) {
  return request({
    url: '/papers/generate',
    method: 'post',
    data,
    params: { ruleConfig },
  })
}

// 更新试卷
export function updatePaper(id, data) {
  return request({
    url: `/papers/${id}`,
    method: 'put',
    data,
  })
}

// 发布试卷
export function publishPaper(id) {
  return request({
    url: `/papers/${id}/publish`,
    method: 'put',
  })
}

// 删除试卷
export function deletePaper(id) {
  return request({
    url: `/papers/${id}`,
    method: 'delete',
  })
}

// 获取待阅卷试卷列表
export function getGradingPapers(params) {
  return request({
    url: '/grading/papers',
    method: 'get',
    params,
  })
}

// 获取试卷答题列表
export function getPaperAnswers(paperId, params) {
  return request({
    url: `/grading/papers/${paperId}/answers`,
    method: 'get',
    params,
  })
}

// 人工评分
export function gradeAnswer(answerId, data) {
  return request({
    url: `/grading/answers/${answerId}`,
    method: 'put',
    data,
  })
}

// 触发AI批量阅卷
export function triggerAiGrade(paperId) {
  return request({
    url: `/grading/papers/${paperId}/ai-grade`,
    method: 'post',
  })
}

// 获取试卷题目列表
export function getPaperQuestions(paperId) {
  return request({
    url: `/papers/${paperId}/questions`,
    method: 'get',
  })
}

// 向试卷添加题目
export function addPaperQuestion(paperId, questionId, score) {
  return request({
    url: `/papers/${paperId}/questions`,
    method: 'post',
    params: { questionId, score },
  })
}

// 更新试卷题目分值
export function updatePaperQuestion(paperId, pqId, score) {
  return request({
    url: `/papers/${paperId}/questions/${pqId}`,
    method: 'put',
    params: { score },
  })
}

// 从试卷移除题目
export function removePaperQuestion(paperId, pqId) {
  return request({
    url: `/papers/${paperId}/questions/${pqId}`,
    method: 'delete',
  })
}

// 设置试卷关联班级
export function assignClasses(paperId, classIds) {
  return request({
    url: `/papers/${paperId}/classes`,
    method: 'put',
    data: { classIds },
  })
}

// 获取试卷关联班级ID列表
export function getPaperClassIds(paperId) {
  return request({
    url: `/papers/${paperId}/classes`,
    method: 'get',
  })
}

// 获取试卷的学生提交列表
export function getPaperStudents(paperId) {
  return request({
    url: `/grading/papers/${paperId}/students`,
    method: 'get',
  })
}

// 获取答卷详情（题目+答案）
export function getRecordDetail(paperId, recordId) {
  return request({
    url: `/grading/papers/${paperId}/records/${recordId}/detail`,
    method: 'get',
  })
}

// 批量评分提交
export function batchGrade(recordId, data) {
  return request({
    url: `/grading/records/${recordId}/batch-grade`,
    method: 'post',
    data,
  })
}
