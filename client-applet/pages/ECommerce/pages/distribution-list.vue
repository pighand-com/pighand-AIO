<template>
  <view class="distribution-list">
    <custom-navigation-bar :back="true" position="flex" title="分销记录" />
    
    <!-- 统计信息 -->
    <view class="statistics-section">
      <view class="statistics-grid">
        <view class="stat-item" v-for="(value, key) in statistics" :key="key">
          <view class="stat-value" :class="`value-${key}`">{{ formatAmount(value) }}</view>
          <view class="stat-label">{{ statusLabels[key] }}</view>
        </view>
      </view>
    </view>

    <!-- 记录列表 -->
    <view class="records-section">
      <view class="records-title">分销记录</view>
      <view class="records-list">
        <view class="record-item" :class="{ 'settlement-record': record.type === 20 }" v-for="record in recordsList" :key="record.id">
          <view class="record-main">
            <view class="record-header">
              <view class="order-sn">订单号：{{ record.order?.sn || '-' }}</view>
              <view class="record-time">{{ formatTime(record.createdAt) }}</view>
            </view>
            <view class="record-content">
              <view class="record-info">
                <view class="record-type" :class="getTypeClass(record.type)">{{ getTypeLabel(record.type) }}</view>
              </view>
              <view class="record-amount" :class="[getAmountClass(record.type), getRecordAmountClass(record)]">
                <text class="currency-symbol">￥</text>
                <text class="amount-number">{{ formatRecordAmount(record) }}</text>
              </view>
            </view>
            <view v-if="record.type !== 20" class="expand-toggle" @click="toggleDetail(record.id)">
              <text class="expand-text">{{ expandedRecords[record.id] ? '收起明细' : '查看明细' }}</text>
              <view class="expand-icon" :class="{ 'expanded': expandedRecords[record.id] }">
                <text>▼</text>
              </view>
            </view>
          </view>
          
          <!-- 明细列表 -->
          <view v-if="expandedRecords[record.id] && record.type !== 20" class="detail-section">
            <view v-if="loadingDetails[record.id]" class="detail-loading">
              <text>加载明细中...</text>
            </view>
            <view v-else-if="recordDetails[record.id] && recordDetails[record.id].length > 0" class="detail-list">
              <view class="detail-item" v-for="detail in recordDetails[record.id]" :key="detail.id">
                <view class="detail-content">
                  <view class="detail-info">
                    <view class="detail-amount">
                      <text class="currency-symbol">￥</text>
                      <text class="amount-number">{{ formatAmount(detail.amount) }}</text>
                    </view>
                    <view class="detail-status" :class="getDetailStatusClass(detail.status)">
                      {{ getDetailStatusLabel(detail.status) }}
                    </view>
                  </view>
                  <view v-if="detail.status === 10 && detail.settlementTime" class="settlement-time">
                    可结算时间：{{ formatTime(detail.settlementTime) }}
                  </view>
                </view>
              </view>
            </view>
            <view v-else class="detail-empty">
              <text>暂无明细数据</text>
            </view>
          </view>
        </view>
      </view>
      
      <!-- 空状态 -->
      <view v-if="recordsList.length === 0 && !loading" class="empty-state">
        <text>暂无分销记录</text>
      </view>
      
      <!-- 加载状态 -->
      <view v-if="loading" class="loading-state">
        <text>加载中...</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import Decimal from 'decimal.js'
import { onLoad, onShow } from '@dcloudio/uni-app'
import { distribution as distributionAPI } from '@/api'

// 响应式数据
const statistics = ref({})
const recordsList = ref([])
const loading = ref(false)
const expandedRecords = ref({}) // 展开状态管理
const recordDetails = ref({}) // 明细数据
const loadingDetails = ref({}) // 明细加载状态

// 状态标签映射
const statusLabels = {
  frozenAmount: '冻结中',
  incomingAmount: '入账中',
  settledAmount: '待结算',
  withdrawAmount: '已结算',
  refundAmount: '退款'
}

// 类型标签映射
const typeLabels = {
  10: '分销',
  20: '结算'
}

// 获取类型标签
const getTypeLabel = (type) => {
  return typeLabels[type] || '未知类型'
}

// 获取类型样式类
const getTypeClass = (type) => {
  const classMap = {
    10: 'type-sales',
    20: 'type-settlement'
  }
  return classMap[type] || ''
}

// 获取状态样式类
const getStatusClass = (status) => {
  const classMap = {
    0: 'status-frozen',
    10: 'status-pending',
    20: 'status-settled',
    90: 'status-refund'
  }
  return classMap[status] || ''
}

// 获取金额样式类
const getAmountClass = (type) => {
  if (type === 10) return 'amount-sales'
  if (type === 20) return 'amount-settlement'
  return ''
}

// 获取记录金额颜色类
const getRecordAmountClass = (record) => {
  // 根据金额类型确定颜色
  if (record.frozenAmount && record.frozenAmount > 0) {
    return 'amount-frozenAmount'
  }
  if (record.incomingAmount && record.incomingAmount > 0) {
    return 'amount-incomingAmount'
  }
  if (record.settledAmount && record.settledAmount > 0) {
    return 'amount-settledAmount'
  }
  if (record.withdrawAmount && record.withdrawAmount > 0) {
    return 'amount-withdrawAmount'
  }
  if (record.refundAmount && record.refundAmount > 0) {
    return 'amount-refundAmount'
  }
  return ''
}

// 格式化时间
const formatTime = (timeStr) => {
  if (!timeStr) return '-'
  const date = new Date(timeStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

// 格式化金额（分转元）
 const formatAmount = (amount) => {
  if (!amount && amount !== 0) return '0'
  const result = new Decimal(amount).dividedBy(100)
  return result.modulo(1).equals(0) ? result.toFixed(0) : result.toFixed(2)
}

// 格式化记录金额
const formatRecordAmount = (record) => {
  const totalAmount = new Decimal(record.frozenAmount || 0)
    .plus(record.settledAmount || 0)
    .plus(record.refundAmount || 0)
  const result = totalAmount.dividedBy(100)
  return result.modulo(1).equals(0) ? result.toFixed(0) : result.toFixed(2)
}

// 获取明细状态标签
const getDetailStatusLabel = (status) => {
  const statusMap = {
    0: '冻结中',
    10: '待结算',
    20: '已结算',
    90: '订单退款'
  }
  return statusMap[status] || '未知状态'
}

// 获取明细状态样式类
const getDetailStatusClass = (status) => {
  const classMap = {
    0: 'detail-status-frozen',
    10: 'detail-status-pending',
    20: 'detail-status-settled',
    90: 'detail-status-refund'
  }
  return classMap[status] || ''
}

// 切换明细展开状态
const toggleDetail = async (recordId) => {
  // 查找对应的记录
  const record = recordsList.value.find(r => r.id === recordId)
  
  // 如果是结算单（type=20），不显示明细
  if (record && record.type === 20) {
    return
  }
  
  const isExpanded = expandedRecords.value[recordId]
  
  if (isExpanded) {
    // 收起明细
    expandedRecords.value[recordId] = false
  } else {
    // 展开明细
    expandedRecords.value[recordId] = true
    
    // 如果还没有加载过明细数据，则加载
    if (!recordDetails.value[recordId]) {
      await loadRecordDetail(recordId)
    }
  }
}

// 加载记录明细
const loadRecordDetail = async (recordId) => {
  try {
    loadingDetails.value[recordId] = true
    const res = await distributionAPI.querySalesDetail(recordId)
    recordDetails.value[recordId] = res || []
  } catch (error) {
    console.error('加载明细失败:', error)
    recordDetails.value[recordId] = []
  } finally {
    loadingDetails.value[recordId] = false
  }
}

// 加载统计数据
const loadStatistics = async () => {
  const res = await distributionAPI.statistics()
  
  // 基于statusLabels动态创建默认值对象
  const defaultStatistics = Object.keys(statusLabels).reduce((acc, key) => {
    acc[key] = 0
    return acc
  }, {})

  const processedRes = res ? { ...res } : {}
  
  // 将已结算金额的负数转为正数
  if (processedRes.withdrawAmount && processedRes.withdrawAmount < 0) {
    processedRes.withdrawAmount = Math.abs(processedRes.withdrawAmount)
  }

  statistics.value = {
    ...defaultStatistics,
    frozenAmount: processedRes.frozenAmount,
    incomingAmount: processedRes.incomingAmount,
    settledAmount: processedRes.settledAmount,
    withdrawAmount: processedRes.withdrawAmount,
    refundAmount: processedRes.refundAmount,
  }
}

// 加载记录列表
const loadRecords = async () => {
    loading.value = true
    const res = await distributionAPI.querySales()

    recordsList.value = res?.records || []
    loading.value = false
}

// 初始化数据
const initData = async () => {
  await Promise.all([
    loadStatistics(),
    loadRecords()
  ])
}

// 页面生命周期
onLoad(() => {
  initData()
})

onShow(() => {
  initData()
})
</script>

<style scoped>
.distribution-list {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

/* 统计信息样式 */
.statistics-section {
  background: white;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.statistics-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.stat-item {
  flex: 1;
  min-width: 0;
  text-align: center;
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
}

.stat-value {
  font-size: 36rpx;
  font-weight: bold;
  color: #007aff;
  margin-bottom: 10rpx;
}

/* 统计金额颜色 */
.stat-value.value-frozenAmount {
  color: #909399;
}

.stat-value.value-incomingAmount {
  color: #17a2b8;
}

.stat-value.value-settledAmount {
  color: #e6a23c;
}

.stat-value.value-withdrawAmount {
  color: #67c23a;
}

.stat-value.value-refundAmount {
  color: #f56c6c;
}

.stat-label {
  font-size: 24rpx;
  color: #666;
  font-weight: normal;
}

/* 记录列表样式 */
.records-section {
  background: transparent;
  border-radius: 0;
  padding: 0;
}

.records-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 30rpx;
  padding: 0 20rpx;
}

.records-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.record-item {
  background: white;
  border-radius: 16rpx;
  padding: 30rpx 30rpx 0;
  margin: 0 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
  border: 1rpx solid #f0f0f0;
}

/* 结算单样式 */
.settlement-record {
  padding-bottom: 30rpx;
}

.record-header {
  margin-bottom: 20rpx;
}

.order-sn {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
}

.record-time {
  font-size: 24rpx;
  color: #999;
  margin-top: 10rpx;
}

.record-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.record-info {
  display: flex;
  gap: 20rpx;
}

.record-type {
  font-size: 24rpx;
  color: #666;
  padding: 8rpx 16rpx;
  background: #f0f0f0;
  border-radius: 8rpx;
}

.type-sales {
  color: #007aff;
  background: #e3f2fd;
}

.type-settlement {
  color: #ff6b35;
  background: #fff3e0;
}

.record-status {
  font-size: 24rpx;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
}

.status-frozen {
  color: #ff9500;
  background: #fff3e0;
}

.status-pending {
  color: #007aff;
  background: #e3f2fd;
}

.status-settled {
  color: #34c759;
  background: #e8f5e8;
}

.status-refund {
  color: #ff3b30;
  background: #ffebee;
}

.record-amount {
  display: flex;
  align-items: baseline;
  color: #333;
}

.currency-symbol {
  font-size: 24rpx;
  font-weight: normal;
  margin-right: 4rpx;
}

.amount-number {
  font-size: 36rpx;
  font-weight: bold;
}

.record-amount.amount-sales .amount-number {
  color: #ff3b30;
}

.record-amount.amount-settlement .amount-number {
  color: #34c759;
}

/* 记录金额颜色 */
.record-amount.amount-frozenAmount .amount-number {
  color: black;
}

.record-amount.amount-incomingAmount .amount-number {
  color: #17a2b8;
}

.record-amount.amount-settledAmount .amount-number {
  color: #e6a23c;
}

.record-amount.amount-withdrawAmount .amount-number {
  color: #67c23a;
}

.record-amount.amount-refundAmount .amount-number {
  color: #f56c6c;
}

/* 空状态和加载状态 */
.empty-state,
.loading-state {
  text-align: center;
  padding: 80rpx 20rpx;
  color: #999;
  font-size: 28rpx;
  background: white;
  border-radius: 16rpx;
  margin: 0 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
  border: 1rpx solid #f0f0f0;
}

/* 记录主体样式 */
.record-main {
  /* 移除cursor: pointer，因为点击事件现在在expand-toggle上 */
}

/* 展开切换区域样式 */
.expand-toggle {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin-top: 20rpx;
  padding: 16rpx 0;
  cursor: pointer;
  border-top: 1rpx solid #f0f0f0;
}

/* 展开图标样式 */
.expand-icon {
  margin-left: 12rpx;
  transition: transform 0.3s ease;
}

.expand-icon.expanded {
  transform: rotate(180deg);
}

.expand-icon text {
  font-size: 24rpx;
  color: #999;
}

/* 展开文字样式 */
.expand-text {
  font-size: 26rpx;
  color: #999;
  font-weight: 400;
}

/* 明细区域样式 */
.detail-section {
  margin-top: 20rpx;
  padding: 0 20rpx;
  margin-bottom: 30rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
  border-left: 4rpx solid #007aff;
}

.detail-loading {
  text-align: center;
  padding: 40rpx 0;
  color: #999;
  font-size: 26rpx;
}

.detail-empty {
  text-align: center;
  padding: 40rpx 0;
  color: #999;
  font-size: 26rpx;
}

/* 明细列表样式 */
.detail-list {
  display: flex;
  flex-direction: column;
}

.detail-item {
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.detail-item:last-child {
  border-bottom: none;
}

.detail-content {
  display: flex;
  flex-direction: column;
  gap: 12rpx;
}

.detail-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.detail-amount {
  display: flex;
  align-items: baseline;
}

.detail-amount .currency-symbol {
  font-size: 22rpx;
  color: #333;
  margin-right: 4rpx;
}

.detail-amount .amount-number {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.detail-status {
  font-size: 24rpx;
  padding: 6rpx 12rpx;
  border-radius: 6rpx;
  font-weight: 500;
}

.detail-status-frozen {
  color: #909399;
  background: #f4f4f5;
}

.detail-status-pending {
  color: #e6a23c;
  background: #fdf6ec;
}

.detail-status-settled {
  color: #67c23a;
  background: #f0f9ff;
}

.detail-status-refund {
  color: #f56c6c;
  background: #fef0f0;
}

.settlement-time {
  font-size: 24rpx;
  color: #666;
  padding: 8rpx 12rpx;
  background: #fff7e6;
  border-radius: 6rpx;
  border-left: 3rpx solid #e6a23c;
}
</style>
