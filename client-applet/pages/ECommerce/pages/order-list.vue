<template>
  <view class="page-container">
    
    <!-- 优化后的标签栏 -->
    <view class="tabs-container">
      <custom-navigation-bar :back="true" position="flex" title="我的订单" />
      <view class="tabs">
        <view
          v-for="(tab, idx) in tabs"
          :key="tab.tradeStatus"
          :class="['tab', { active: idx === activeTab }]"
          @tap="changeTab(idx)"
        >
          <text class="tab-text">{{ tab.label }}</text>
          <view v-if="idx === activeTab" class="tab-indicator"></view>
        </view>
      </view>
    </view>
    
    <!-- 优化后的订单列表 -->
    <view class="order-list">
      <view v-for="order in orderList" :key="order.sn" class="order-item" @tap="goToOrderDetail(order.id)">
        <!-- 订单头部信息 -->
        <view class="order-header">
          <view class="order-info">
            <text class="order-label">订单编号</text>
            <text class="order-sn" @longpress="copyOrderSn(order.sn)">{{ order.sn }}</text>
          </view>
          <text class="order-date">{{ formatDate(order.createdAt) }}</text>
        </view>
        
        <!-- 商品信息 -->
        <view class="order-goods">
			
		<view v-for="(ticket, index) in order.ticket" :key="ticket.id + '_' + index" class="goods-item">
            <view class="goods-img-container">
              <image 
                v-if="ticket?.theme?.posterUrl" 
                :src="ticket?.theme?.posterUrl" 
                class="goods-img" 
                mode="aspectFill" 
              />
              <view v-else class="goods-img-placeholder">
                <text class="placeholder-text">暂无图片</text>
              </view>
            </view>
            <view class="goods-info">
              <view class="goods-main">
                <text class="goods-name">{{ ticket?.name || '未知商品' }}</text>
                <view class="goods-meta">
                  <text class="goods-qty">数量：{{ getTicketQuantityFromMap(order, ticket.id) }}</text>
                </view>
              </view>
            </view>
          </view>
        </view>
        
        <!-- 订单底部 -->
        <view class="order-footer">
          <view class="order-actions">
            <!-- 可以在这里添加操作按钮 -->
          </view>
          <view class="order-amount-container">
            <!-- 根据订单状态显示不同的金额信息 -->
            <template v-if="tabs[activeTab].tradeStatus === 51">
              <text class="amount-label">退款金额</text>
              <text class="order-amount refund-amount">￥{{ formatAmount(order.refundAmount) }}</text>
            </template>
            <template v-else>
              <text class="amount-label">实付金额</text>
              <text class="order-amount paid-amount">￥{{ formatAmount(order.amountPaid) }}</text>
            </template>
          </view>
        </view>
      </view>
      
      <!-- 空状态优化 -->
      <view v-if="orderList.length === 0" class="empty-state">
        <text class="empty-text">暂无订单</text>
        <text class="empty-desc">您还没有相关订单哦</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { onLoad } from '@dcloudio/uni-app'
import { ref } from 'vue'
import { order as orderAPI } from '@/api'
import Decimal from 'decimal.js'

const tabs = [
  { label: '待付款', tradeStatus: 10 },
  { label: '已付款', tradeStatus: 40 },
  { label: '退款/售后', tradeStatus: 51 }
]
const activeTab = ref(0)
const orderList = ref([])

// 格式化时间戳为日期
function formatDate(timestamp) {
  if (!timestamp) return ''
  const date = new Date(timestamp)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${year}.${month}.${day} ${hours}:${minutes}`
}

// 获取票券数量
function getTicketQuantityFromMap(order, ticketId) {
  // 创建ticketId到quantity的映射
  if (!order._ticketQuantityMap) {
    order._ticketQuantityMap = new Map()
    order.orderSku?.forEach(sku => {
      if (sku.ticketId) {
        order._ticketQuantityMap.set(sku.ticketId, sku.quantity || 0)
      }
    })
  }
  return order._ticketQuantityMap.get(ticketId) || 0
}

// 获取订单列表
function fetchOrders() {
  const tradeStatus = tabs[activeTab.value].tradeStatus
  orderAPI.query({ tradeStatus }).then(res => {
    orderList.value = res?.records || []
  })
}

// 切换tab并刷新订单列表
function changeTab(idx) {
  activeTab.value = idx
  fetchOrders()
}

// 复制订单编号
function copyOrderSn(orderSn) {
  uni.setClipboardData({
    data: orderSn,
    success: () => {
      uni.showToast({
        title: '订单编号已复制',
        icon: 'success',
        duration: 2000
      })
    },
    fail: () => {
      uni.showToast({
        title: '复制失败',
        icon: 'none',
        duration: 2000
      })
    }
  })
}

// 跳转到订单详情页
function goToOrderDetail(id) {
  uni.navigateTo({
    url: `/pages/ECommerce/pages/order-detail?id=${id}`
  })
}

// 将分转换为元
function formatAmount(amountInCents) {
  if (!amountInCents && amountInCents !== 0) return '0.00'
  return new Decimal(amountInCents).div(100).toFixed(2)
}

// 页面加载时处理跳转参数并拉取订单
onLoad((options) => {
  if (options && options.tradeStatus) {
    const idx = tabs.findIndex(tab => String(tab.tradeStatus) === String(options.tradeStatus))
    if (idx !== -1) {
      activeTab.value = idx
    }
  }
  fetchOrders()
})
</script>

<style scoped>
/* 页面容器 */
.page-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #f8f9fa 0%, #ffffff 100%);
}

/* 标签栏容器 */
.tabs-container {
  background: #ffffff;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 100;
}

.tabs {
  display: flex;
  padding: 0 32rpx;
}

.tab {
  flex: 1;
  position: relative;
  padding: 32rpx 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  transition: all 0.3s ease;
}

.tab-text {
  font-size: 32rpx;
  font-weight: 500;
  color: #8a8a8a;
  transition: all 0.3s ease;
}

.tab.active .tab-text {
  color: #007aff;
  font-weight: 600;
  transform: scale(1.05);
}

.tab-indicator {
  position: absolute;
  bottom: 0;
  width: 60rpx;
  height: 6rpx;
  background: linear-gradient(90deg, #007aff 0%, #5ac8fa 100%);
  border-radius: 3rpx;
  animation: slideIn 0.3s ease;
}

@keyframes slideIn {
  from {
    width: 0;
    opacity: 0;
  }
  to {
    width: 60rpx;
    opacity: 1;
  }
}

/* 订单列表 */
.order-list {
  padding: 32rpx 24rpx;
  padding-top: 32rpx;
}

.order-item {
  position: relative;
  background: #ffffff;
  margin-bottom: 32rpx;
  border-radius: 24rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.06);
  padding: 32rpx;
  border: 1rpx solid #f0f0f0;
  transition: all 0.3s ease;
  overflow: hidden;
}

.order-item:hover {
  transform: translateY(-4rpx);
  box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.12);
}



/* 订单头部 */
.order-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24rpx;
  padding-top: 16rpx;
}

.order-info {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.order-label {
  font-size: 24rpx;
  color: #8a8a8a;
  font-weight: 400;
}

.order-sn {
  font-size: 28rpx;
  color: #007aff;
  font-weight: 500;
  font-family: 'SF Mono', Monaco, monospace;
  cursor: pointer;
}

.order-date {
  font-size: 26rpx;
  color: #8a8a8a;
  font-weight: 400;
}

/* 商品信息 */
.order-goods {
  margin-bottom: 24rpx;
}

.goods-item {
  display: flex;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f5f5f7;
  height: 140rpx;
}

.goods-item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.goods-img-container {
  margin-right: 24rpx;
  width: 140rpx;
  height: 140rpx;
}

.goods-img {
  width: 100%;
  height: 100%;
  border-radius: 16rpx;
  background: #f5f5f7;
  border: 1rpx solid #e5e5ea;
}

.goods-img-placeholder {
  border-radius: 16rpx;
  background: linear-gradient(135deg, #f5f5f7 0%, #e5e5ea 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1rpx dashed #d1d1d6;
}

.placeholder-text {
  font-size: 22rpx;
  color: #8a8a8a;
}

.goods-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 140rpx;
}

.goods-main {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  height: 100%;
}

.goods-name {
  font-size: 30rpx;
  color: #1d1d1f;
  font-weight: 500;
  line-height: 1.4;
  max-width: 400rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.goods-meta {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.goods-qty {
  font-size: 26rpx;
  color: #8a8a8a;
  background: #f5f5f7;
  padding: 6rpx 12rpx;
  border-radius: 8rpx;
}

/* 订单底部 */
.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 24rpx;
  border-top: 1rpx solid #f5f5f7;
}

.order-actions {
  display: flex;
  gap: 16rpx;
}

.order-amount-container {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 4rpx;
}

.amount-label {
  font-size: 24rpx;
  color: #8a8a8a;
}

.order-amount {
  font-size: 36rpx;
  font-weight: 600;
  font-family: 'SF Pro Display', -apple-system, sans-serif;
}

/* 金额颜色统一规范 */
.refund-amount {
  color: #34c759 !important; /* 退款金额：绿色 */
}

.paid-amount {
  color: #ff3b30 !important; /* 实付金额：红色 */
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 40rpx;
  text-align: center;
}

.empty-text {
  font-size: 32rpx;
  color: #1d1d1f;
  font-weight: 500;
  margin-bottom: 16rpx;
}

.empty-desc {
  font-size: 28rpx;
  color: #8a8a8a;
  line-height: 1.5;
}

</style>
