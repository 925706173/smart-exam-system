import request from './request'

// 获取试卷成绩报告
export function getPaperReport(paperId) {
  return request({
    url: `/stats/paper/${paperId}/report`,
    method: 'get',
  })
}

// 获取成绩排名
export function getPaperRank(paperId) {
  return request({
    url: `/stats/paper/${paperId}/rank`,
    method: 'get',
  })
}

// 学生历史成绩
export function getStudentHistory() {
  return request({
    url: '/stats/student/history',
    method: 'get',
  })
}
