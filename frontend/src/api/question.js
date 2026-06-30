import request from './request'

// 分页查询题目
export function getQuestionPage(params) {
  return request({
    url: '/questions',
    method: 'get',
    params,
  })
}

// 获取题目详情
export function getQuestionDetail(id) {
  return request({
    url: `/questions/${id}`,
    method: 'get',
  })
}

// 创建题目
export function createQuestion(data) {
  return request({
    url: '/questions',
    method: 'post',
    data,
  })
}

// 更新题目
export function updateQuestion(id, data) {
  return request({
    url: `/questions/${id}`,
    method: 'put',
    data,
  })
}

// 删除题目
export function deleteQuestion(id) {
  return request({
    url: `/questions/${id}`,
    method: 'delete',
  })
}

// Excel批量导入题库
export function importQuestions(file, subjectId) {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('subjectId', subjectId)
  return request({
    url: '/questions/import',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}
