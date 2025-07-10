<template>
  <view class="page-container">
    <custom-navigation-bar :back="true" position="flex" title="订单详情" />
    
    <!-- 订单状态卡片 -->
    <view class="status-card">
      <view class="status-info">
        <text class="status-text">{{ getStatusText(orderDetail.tradeStatus) }}</text>
        <text class="status-desc">{{ getStatusDesc(orderDetail.tradeStatus) }}</text>
        <view v-if="orderDetail.tradeStatus === 10 && orderDetail.expiredAt" class="countdown-container">
          <text class="countdown-label">支付剩余时间：</text>
          <text class="countdown-time">{{ countdownText }}</text>
        </view>
      </view>
    </view>
    
    <!-- 订单信息 -->
    <view class="order-info-card">
      <view class="card-header">
        <text class="card-title">订单信息</text>
      </view>
      <view class="info-item">
        <text class="info-label">订单编号</text>
        <text class="info-value" @longpress="copyOrderSn(orderDetail.sn)">{{ orderDetail.sn }}</text>
      </view>
      <view class="info-item">
        <text class="info-label">下单时间</text>
        <text class="info-value">{{ formatDate(orderDetail.createdAt) }}</text>
      </view>
      <view class="info-item" v-if="orderDetail.paymentTime">
        <text class="info-label">支付时间</text>
        <text class="info-value">{{ formatDate(orderDetail.paymentTime) }}</text>
      </view>
    </view>
    
    <!-- 商品信息 -->
    <view class="goods-card">
      <view class="card-header">
        <text class="card-title">商品信息</text>
      </view>
      <view class="goods-list">
        <view v-for="(ticket, index) in orderDetail.ticket" :key="ticket.id + '_' + index" class="goods-item">
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
            <text class="goods-name">{{ ticket?.name || '未知商品' }}</text>
            <text class="goods-qty">数量：{{ getTicketQuantityFromMap(orderDetail, ticket.id) }}</text>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 实付金额 -->
    <view class="amount-card">
      <!-- 退款/售后状态下显示退款金额和实付金额 -->
      <template v-if="orderDetail.tradeStatus === 51">
        <view class="amount-item">
          <text class="amount-label">实付金额</text>
          <text class="amount-value paid-amount">￥{{ formatAmount(orderDetail.amountPaid) }}</text>
        </view>
        <view class="amount-item total">
          <text class="amount-label">退款金额</text>
          <text class="amount-value final refund-amount">￥{{ formatAmount(orderDetail.refundAmount) }}</text>
        </view>
      </template>
      <!-- 其他状态只显示对应金额 -->
      <template v-else>
        <view class="amount-item total">
          <text class="amount-label">{{ orderDetail.tradeStatus === 10 ? '应付金额' : '实付金额' }}</text>
          <text class="amount-value final paid-amount">￥{{ formatAmount(orderDetail.amountPaid) }}</text>
        </view>
      </template>
    </view>
    
    <!-- 操作按钮 -->
    <view class="action-buttons" v-if="showActionButtons">
      <view class="button-group">
        <view 
          v-if="orderDetail.tradeStatus === 10" 
          class="action-btn primary"
          @tap="payOrder"
        >
          <text class="btn-text">立即支付</text>
        </view>
        <view 
          v-if="orderDetail.tradeStatus === 10" 
          class="action-btn secondary"
          @tap="cancelOrder"
        >
          <text class="btn-text">取消订单</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { onLoad } from '@dcloudio/uni-app'
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { order as orderAPI } from '@/api'
import Decimal from 'decimal.js'

// 订单详情数据
const orderDetail = ref({})
const loading = ref(false)
const isPaying = ref(false)

// 倒计时相关
const countdownText = ref('')
let countdownTimer = null

// 计算属性：是否显示操作按钮
const showActionButtons = computed(() => {
  // 待付款状态且未过期才显示按钮
  if (orderDetail.value.tradeStatus === 10) {
    // 如果有过期时间，检查是否已过期
    if (orderDetail.value.expiredAt) {
      const now = new Date().getTime()
      const expiredTime = new Date(orderDetail.value.expiredAt).getTime()
      return now < expiredTime
    }
    return true // 如果没有过期时间，默认显示
  }
  return false
})

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



// 获取订单状态文本
function getStatusText(tradeStatus) {
  const statusMap = {
    10: '待付款',
    40: '已付款',
    51: '退款/售后'
  }
  return statusMap[tradeStatus] || '未知状态'
}

// 获取订单状态描述
function getStatusDesc(tradeStatus) {
  const statusMap = {
    10: '请尽快完成支付',
    40: '订单已完成支付',
    51: '订单已退款'
  }
  return statusMap[tradeStatus] || ''
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

// 获取订单详情
function fetchOrderDetail(id) {
  if (!id) return
  
  loading.value = true
  return orderAPI.find(id).then(res => {
    orderDetail.value = res || {}
    // 如果是待支付状态，启动倒计时
    if (orderDetail.value.tradeStatus === 10) {
      startCountdown()
    }
    return res
  }).catch(err => {
    console.error('获取订单详情失败:', err)
    uni.showToast({
      title: '获取订单详情失败',
      icon: 'none',
      duration: 2000
    })
  }).finally(() => {
    loading.value = false
  })
}

// 支付订单
const payOrder = async () => {
  if (isPaying.value || loading.value) return // 防止重复点击
  
  isPaying.value = true
  loading.value = true // 显示全屏loading
  
  try {
    const result = await orderAPI.pay(orderDetail.value.id, 'wechat_applet');

    const { nonceStr, prepayId, signType, paySign, timeStamp } = result;
    uni.requestPayment({
      provider:'wxpay',
      timeStamp,
      nonceStr,
      package:`prepay_id=${prepayId}`,
      signType: signType,
      paySign: paySign,
      success(responseData) {
        isPaying.value = false
        loading.value = false
        
        if(responseData.errMsg === 'requestPayment:ok'){
          uni.showToast({
            title: '支付成功！',
            icon: 'success',
            duration: 2000
          })
          // 支付成功后重新查询订单详情
          setTimeout(() => {
            fetchOrderDetail(orderDetail.value.id)
          }, 2000)
        }else{
          uni.showToast({
            title: '支付取消',
            icon: 'none',
            duration: 2000
          })
        }
      },
      fail(error) {
        isPaying.value = false
        loading.value = false
        
        uni.showToast({
          title: '支付失败',
          icon: 'error',
          duration: 2000
        })
      }
    })
  } catch (error) {
    isPaying.value = false
    loading.value = false
    
    console.error('支付失败:', error)
    uni.showToast({
      title: '支付失败，请重试',
      icon: 'error',
      duration: 2000
    })
  }
}

// 取消订单
function cancelOrder() {
  uni.showModal({
    title: '确认取消',
    content: '确定要取消这个订单吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          loading.value = true
          await orderAPI.cancel(orderDetail.value.id)
          
          uni.showToast({
            title: '订单已取消',
            icon: 'success',
            duration: 2000
          })
          
          // 延迟返回列表页面
          setTimeout(() => {
            uni.navigateBack()
          }, 2000)
        } catch (error) {
          console.error('取消订单失败:', error)
          uni.showToast({
            title: '取消订单失败，请重试',
            icon: 'error',
            duration: 2000
          })
        } finally {
          loading.value = false
        }
      }
    }
  })
}

// 开始倒计时
function startCountdown() {
  // 清除之前的定时器
  if (countdownTimer) {
    clearInterval(countdownTimer)
    countdownTimer = null
  }
  
  // 如果不是待支付状态或没有过期时间，不启动倒计时
  if (orderDetail.value.tradeStatus !== 10 || !orderDetail.value.expiredAt) {
    countdownText.value = ''
    return
  }
  
  // 计算倒计时
  const updateCountdown = () => {
    const now = new Date().getTime()
    const expiredTime = new Date(orderDetail.value.expiredAt).getTime()
    const diff = expiredTime - now
    
    if (diff <= 0) {
      // 已过期
      countdownText.value = '已过期'
      clearInterval(countdownTimer)
      countdownTimer = null
      // 刷新订单状态
      fetchOrderDetail(orderDetail.value.id)
      return
    }
    
    // 计算时分秒
    const hours = Math.floor(diff / (1000 * 60 * 60))
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60))
    const seconds = Math.floor((diff % (1000 * 60)) / 1000)
    
    // 格式化显示
    countdownText.value = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`
  }
  
  // 立即执行一次
  updateCountdown()
  
  // 设置定时器，每秒更新一次
  countdownTimer = setInterval(updateCountdown, 1000)
}

// 页面加载时获取订单详情
onLoad((options) => {
  if (options && options.id) {
    fetchOrderDetail(options.id).then(() => {
      // 获取订单详情后启动倒计时
      startCountdown()
    })
  } else {
    uni.showToast({
      title: '订单编号不能为空',
      icon: 'none',
      duration: 2000
    })
    setTimeout(() => {
      uni.navigateBack()
    }, 2000)
  }
})

// 将分转换为元
function formatAmount(amountInCents) {
  if (!amountInCents && amountInCents !== 0) return '0.00'
  return new Decimal(amountInCents).div(100).toFixed(2)
}

// 组件卸载时清除定时器
onUnmounted(() => {
  if (countdownTimer) {
    clearInterval(countdownTimer)
    countdownTimer = null
  }
})
</script>

<style scoped>
/* 页面容器 */
.page-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #f8f9fa 0%, #ffffff 100%);
  padding-bottom: 120rpx;
}

/* 状态卡片 */
.status-card {
  margin: 32rpx 24rpx;
  background: linear-gradient(135deg, #007aff 0%, #5ac8fa 100%);
  border-radius: 24rpx;
  padding: 40rpx 32rpx;
  display: flex;
  align-items: center;
  box-shadow: 0 8rpx 32rpx rgba(0, 122, 255, 0.3);
}

.status-icon {
  margin-right: 24rpx;
}

.status-emoji {
  font-size: 48rpx;
}

.status-info {
  flex: 1;
}

.status-text {
  display: block;
  font-size: 36rpx;
  font-weight: 600;
  color: #ffffff;
  margin-bottom: 8rpx;
}

.status-desc {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);
}

.countdown-container {
  margin-top: 16rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 12rpx;
  padding: 8rpx 16rpx;
  display: flex;
  align-items: center;
}

.countdown-label {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.9);
}

.countdown-time {
  font-size: 26rpx;
  color: #ffffff;
  font-weight: 600;
}

/* 卡片通用样式 */
.order-info-card,
.goods-card,
.amount-card {
  margin: 32rpx 24rpx;
  background: #ffffff;
  border-radius: 24rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.card-header {
  padding: 32rpx 32rpx 24rpx;
  border-bottom: 1rpx solid #f5f5f7;
}

.card-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #1d1d1f;
}

/* 订单信息 */
.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx 32rpx;
  border-bottom: 1rpx solid #f5f5f7;
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  font-size: 30rpx;
  color: #8a8a8a;
  font-weight: 400;
}

.info-value {
  font-size: 30rpx;
  color: #1d1d1f;
  font-weight: 500;
  max-width: 400rpx;
  text-align: right;
  word-break: break-all;
}

/* 商品信息 */
.goods-list {
  padding: 0 32rpx 32rpx;
}

.goods-item {
  display: flex;
  align-items: flex-start;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #f5f5f7;
}

.goods-item:last-child {
  border-bottom: none;
}

.goods-img-container {
  margin-right: 24rpx;
  display: flex;
}

.goods-img {
  width: 160rpx;
  height: 160rpx;
  border-radius: 16rpx;
  background: #f5f5f7;
  border: 1rpx solid #e5e5ea;
}

.goods-img-placeholder {
  width: 100%;
  height: 100%;
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
  justify-content: space-around;
  height: 160rpx;
}

.goods-name {
  font-size: 32rpx;
  color: #1d1d1f;
  font-weight: 500;
  line-height: 1.4;
}

.goods-qty {
  font-size: 26rpx;
  color: #8a8a8a;
  background: #f5f5f7;
  padding: 6rpx 12rpx;
  border-radius: 8rpx;
  align-self: flex-start;
}

/* 费用明细 */
.amount-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx 32rpx;
  border-bottom: 1rpx solid #f5f5f7;
}

.amount-item:last-child {
  border-bottom: none;
}

.amount-item.total {
  background: #f8f9fa;
  border-top: 2rpx solid #e5e5ea;
}

.amount-label {
  font-size: 30rpx;
  color: #8a8a8a;
  font-weight: 400;
}

.amount-item.total .amount-label {
  color: #1d1d1f;
  font-weight: 600;
}

.amount-value {
  font-size: 30rpx;
  color: #1d1d1f;
  font-weight: 500;
}

.amount-value.discount {
  color: #34c759;
}

.amount-value.final {
  font-size: 36rpx;
  color: #ff3b30;
  font-weight: 600;
}

/* 金额颜色统一规范 */
.refund-amount {
  color: #34c759 !important; /* 退款金额：绿色 */
}

.paid-amount {
  color: #ff3b30 !important; /* 实付金额：红色 */
}

/* 操作按钮 */
.action-buttons {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #ffffff;
  padding: 24rpx 24rpx env(safe-area-inset-bottom);
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.1);
  border-top: 1rpx solid #f5f5f7;
}

.button-group {
  display: flex;
  gap: 24rpx;
}

.action-btn {
  flex: 1;
  height: 88rpx;
  border-radius: 44rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.action-btn.primary {
  background: linear-gradient(135deg, #007aff 0%, #5ac8fa 100%);
  box-shadow: 0 8rpx 24rpx rgba(0, 122, 255, 0.3);
}

.action-btn.secondary {
  background: #f5f5f7;
  border: 1rpx solid #e5e5ea;
}

.action-btn:active {
  transform: scale(0.98);
}

.btn-text {
  font-size: 32rpx;
  font-weight: 600;
}

.action-btn.primary .btn-text {
  color: #ffffff;
}

.action-btn.secondary .btn-text {
  color: #1d1d1f;
}

/* 响应式适配 */
@media (max-width: 750rpx) {
  .goods-img,
  .goods-img-placeholder {
    width: 140rpx;
    height: 140rpx;
  }
  
  .goods-info {
    height: 140rpx;
  }
  
  .goods-name {
    font-size: 30rpx;
  }
  
  .status-card {
    padding: 32rpx 24rpx;
  }
}
</style>
