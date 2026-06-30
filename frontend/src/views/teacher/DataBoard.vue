<template>
  <div class="data-board">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>数据看板</span>
          <el-select v-model="selectedPaperId" placeholder="选择试卷" style="width: 300px">
            <el-option
              v-for="paper in paperOptions"
              :key="paper.id"
              :label="paper.title"
              :value="paper.id"
            />
          </el-select>
        </div>
      </template>

      <div v-if="report" class="stats-cards">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card shadow="hover">
              <div class="stat-item">
                <div class="stat-value">{{ report.totalCount }}</div>
                <div class="stat-label">参考人数</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <div class="stat-item">
                <div class="stat-value success">{{ report.passCount }}</div>
                <div class="stat-label">及格人数</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <div class="stat-item">
                <div class="stat-value warning">{{ report.failCount }}</div>
                <div class="stat-label">不及格人数</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <div class="stat-item">
                <div class="stat-value primary">{{ report.passRate }}%</div>
                <div class="stat-label">及格率</div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="20" style="margin-top: 20px">
          <el-col :span="6">
            <el-card shadow="hover">
              <div class="stat-item">
                <div class="stat-value">{{ report.avgScore }}</div>
                <div class="stat-label">平均分</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <div class="stat-item">
                <div class="stat-value success">{{ report.maxScore }}</div>
                <div class="stat-label">最高分</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <div class="stat-item">
                <div class="stat-value warning">{{ report.minScore }}</div>
                <div class="stat-label">最低分</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- ECharts图表 -->
      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="12">
          <el-card>
            <template #header><span>及格/不及格比例</span></template>
            <div ref="pieChartRef" style="height: 400px"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <template #header><span>分数段分布</span></template>
            <div ref="barChartRef" style="height: 400px"></div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getPaperPage } from '@/api/paper'
import { getPaperReport } from '@/api/stats'

const selectedPaperId = ref(null)
const paperOptions = ref([])
const report = ref(null)

const pieChartRef = ref(null)
const barChartRef = ref(null)
let pieChart = null
let barChart = null

// 加载试卷列表
onMounted(async () => {
  try {
    const res = await getPaperPage({ pageNum: 1, pageSize: 100 })
    paperOptions.value = res.data.records
  } catch (e) {
    console.error(e)
  }

  // 监听窗口resize
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  pieChart?.dispose()
  barChart?.dispose()
})

const handleResize = () => {
  pieChart?.resize()
  barChart?.resize()
}

// 选择试卷后加载报告
watch(selectedPaperId, async (id) => {
  if (!id) return
  try {
    const res = await getPaperReport(id)
    report.value = res.data
    await nextTick()
    renderCharts()
  } catch (e) {
    console.error(e)
  }
})

// 渲染ECharts图表
const renderCharts = () => {
  if (!report.value) return

  // 饼图 - 及格/不及格比例
  if (pieChartRef.value) {
    pieChart = echarts.init(pieChartRef.value)
    pieChart.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: {c}人 ({d}%)' },
      legend: { bottom: 0 },
      series: [
        {
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
          label: { show: true, formatter: '{b}\n{d}%' },
          data: [
            { value: report.value.passCount, name: '及格', itemStyle: { color: '#67c23a' } },
            { value: report.value.failCount, name: '不及格', itemStyle: { color: '#f56c6c' } },
          ],
        },
      ],
    })
  }

  // 柱状图 - 分数段分布
  if (barChartRef.value && report.value.scoreDistribution) {
    barChart = echarts.init(barChartRef.value)
    const distData = report.value.scoreDistribution
    barChart.setOption({
      tooltip: { trigger: 'axis' },
      xAxis: {
        type: 'category',
        data: distData.map((item) => item.label),
        axisLabel: { interval: 0 },
      },
      yAxis: { type: 'value', name: '人数' },
      series: [
        {
          type: 'bar',
          data: distData.map((item) => item.count),
          barWidth: '40%',
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#409eff' },
              { offset: 1, color: '#79bbff' },
            ]),
            borderRadius: [4, 4, 0, 0],
          },
          label: { show: true, position: 'top' },
        },
      ],
    })
  }
}
</script>

<style scoped>
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.stat-item {
  text-align: center;
  padding: 20px 0;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #333;
}

.stat-value.success {
  color: #67c23a;
}

.stat-value.warning {
  color: #e6a23c;
}

.stat-value.primary {
  color: #409eff;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 8px;
}
</style>
