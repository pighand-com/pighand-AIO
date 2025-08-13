<template>
  <custom-navigation-bar :back="true" position="flex" title="我的票务" />

  <view class="mine-ticket-page">
    <view class="ticket-list">
      <view 
        v-for="ticket in ticketList" 
        :key="ticket.id" 
        class="ticket-item"
      >
        <view class="ticket-id-header">
          票号：{{ ticket.id }}
        </view>
        <view class="ticket-content">
          <view class="ticket-image">
            <image 
              :src="ticket?.ticket?.posterUrl || ticket?.theme?.posterUrl" 
              class="ticket-img"
              mode="aspectFill"
            />
          </view>
          <view class="ticket-info">
            <view class="ticket-name">{{ ticket?.ticket?.name }}</view>
            <view class="remaining-count">
              剩余核销次数：{{ ticket.remainingValidationCount }}
            </view>
          </view>
          <view class="qr-icon-container" @click="showQrModal(ticket)">
            <image src="../../base/static/qr-code.svg" class="qr-icon-image" mode="aspectFit"></image>
          </view>
        </view>
      </view>
    </view>
    
    <view v-if="ticketList.length === 0" class="empty-state">
      <text>暂无票券</text>
    </view>
    
    <!-- 核销二维码弹窗 -->
    <view class="qr-modal" v-if="isQrModalVisible" @click="hideQrModal">
      <view class="qr-modal-content" @click.stop>
        <!-- 横向滑动的二维码列表 -->
        <swiper 
          class="qr-swiper" 
          :current="currentTicketIndex" 
          @change="onSwiperChange"
          :indicator-dots="false"
          :autoplay="false"
          :circular="false"
          :previous-margin="ticketList.length > 1 ? '70rpx' : '0'"
          :next-margin="ticketList.length > 1 ? '70rpx' : '0'"
        >
          <swiper-item 
            v-for="(ticket, index) in ticketList" 
            :key="ticket.id"
            class="qr-swiper-item"
          >
            <view class="qr-card">
              <view class="qr-tip-container">
                <text class="qr-tip-title">{{ ticket?.ticket?.name }}</text>
              </view>
              <view class="qr-image-container">
                <canvas 
                  :canvas-id="`qrcode-${index}`" 
                  class="qr-canvas" 
                  :style="{width: qrSize + 'px', height: qrSize + 'px'}"
                  v-show="!ticket.isLoading"
                ></canvas>
                <view v-if="ticket.isLoading" class="qr-loading" :style="{width: qrSize + 'px', height: qrSize + 'px'}">
                  <uni-load-more status="loading" :content-text="{contentdown: '生成中...', contentrefresh: '生成中...', contentnomore: '生成中...'}"></uni-load-more>
                </view>
              </view>
              <view class="qr-ticket-info">
                <text class="qr-ticket-code">票号：{{ ticket.id }}</text>
                <text class="qr-usage-tip">使用时请出示二维码</text>
              </view>
            </view>
          </swiper-item>
        </swiper>
        
        <!-- 指示器 -->
        <view v-if="ticketList.length > 1" class="qr-indicator">
          <view 
            v-for="(ticket, index) in ticketList" 
            :key="index"
            class="indicator-dot"
            :class="{ active: index === currentTicketIndex }"
          ></view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { onLoad, onReady } from '@dcloudio/uni-app'
import { ref } from 'vue'
import { ticket as ticketAPI } from '@/api'
import UQRCode from 'uqrcodejs'

const ticketList = ref([])
const isQrModalVisible = ref(false)
const currentTicketIndex = ref(0)
// 二维码大小统一配置（px单位，避免rpx导致显示问题）
const qrSize = ref(250)

// 查询我的票列表
const queryMineTickets = async () => {
  const params = {
    usable: true
  }
  const response = await ticketAPI.queryMine(params)
  if (response && response.records) {
    ticketList.value = response.records
  }
}

// 生成某张票的二维码（根据索引）
const generateQrCodeByIndex = (index) => {
  const t = ticketList.value[index]
  if (!t) return
  try {
    const qr = new UQRCode()

    qr.data = String(t.id)
    // 设置二维码大小，必须与canvas设置的宽高一致
    qr.size = qrSize.value
    
    qr.make()
    const ctx = uni.createCanvasContext(`qrcode-${index}`)
    qr.canvasContext = ctx
    qr.drawCanvas()
    // 标记当前票已完成加载
    t.isLoading = false
  } catch (error) {
    console.error('二维码生成失败', error)
    if (t) t.isLoading = false
  }
}

// swiper 切换事件
const onSwiperChange = (e) => {
  const newIndex = e?.detail?.current || 0
  currentTicketIndex.value = newIndex
  // 惰性生成当前页二维码
  const t = ticketList.value[newIndex]
  if (t && t.isLoading) {
    setTimeout(() => generateQrCodeByIndex(newIndex), 100)
  }
}

// 显示二维码弹窗
const showQrModal = (ticket) => {
  const list = ticketList.value || []
  // 设置默认索引为当前点击的票
  const idx = Math.max(0, list.findIndex((t) => t.id === ticket.id))
  currentTicketIndex.value = idx
  // 为每个条目添加加载状态
  list.forEach((t, i) => {
    // 初始全部设为 loading，稍后惰性绘制
    t.isLoading = true
  })
  isQrModalVisible.value = true
  // 需要等弹窗渲染完成再绘制当前索引的二维码
  setTimeout(() => {
    generateQrCodeByIndex(idx)
  }, 120)
}

// 隐藏二维码弹窗
const hideQrModal = () => {
  isQrModalVisible.value = false
  // 关闭后刷新票券列表（可能变更剩余次数）
  queryMineTickets()
}

onLoad(() => {
  queryMineTickets()
})

</script>

<style scoped>
.mine-ticket-page {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.ticket-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.ticket-item {
  display: flex;
  flex-direction: column;
  background-color: #fff;
  border-radius: 16rpx;
  padding: 20rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
}

.ticket-id-header {
  font-size: 24rpx;
  color: #888;
  margin-bottom: 16rpx;
  line-height: 1.3;
}

.ticket-content {
  display: flex;
  align-items: center;
}

.ticket-image {
  width: 120rpx;
  height: 120rpx;
  margin-right: 20rpx;
}

.ticket-img {
  width: 100%;
  height: 100%;
  border-radius: 12rpx;
}

.ticket-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.ticket-name {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 16rpx;
  line-height: 1.4;
}

.remaining-count {
  font-size: 28rpx;
  color: #666;
  line-height: 1.3;
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400rpx;
  color: #999;
  font-size: 28rpx;
}

.qr-icon-container {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin-right: 24rpx;
}

.qr-icon-image {
  width: 48rpx;
  height: 48rpx;
}

.qr-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}

.qr-modal-content {
  width: 100%;
  padding: 0 0 40rpx;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.qr-swiper {
  width: 100%;
  height: 900rpx;
  /* 让左右卡片露出白边，提示可滑动 */
  box-sizing: border-box;
  padding: 40rpx 0 20rpx;
}

.qr-swiper-item {
  display: flex;
  align-items: center;
  justify-content: center;
}

.qr-card {
  background: #fff;
  border-radius: 28rpx;
  padding: 30rpx 30rpx 36rpx;
  box-shadow: 0 12rpx 36rpx rgba(255, 255, 255, 0.2);
}

.qr-tip-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10rpx;
  padding: 10rpx 0 24rpx;
}

.qr-tip-title {
  font-size: 34rpx;
  color: #111;
  font-weight: 600;
  text-align: center;
}

.qr-tip-date {
  font-size: 26rpx;
  color: #777;
}

.qr-image-container {
  display: flex;
  justify-content: center;
  padding: 24rpx;
}

.qr-canvas {
  border-radius: 16rpx;
}

.qr-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 16rpx;
}

.qr-ticket-info {
  margin-top: 24rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10rpx;
}

.qr-ticket-code {
  font-size: 26rpx;
  color: #333;
}

.qr-usage-tip {
  font-size: 24rpx;
  color: #9aa0a6;
}

.qr-indicator {
  margin-top: 16rpx;
  display: flex;
  gap: 24rpx;
}

.indicator-dot {
  width: 16rpx;
  height: 16rpx;
  background: #d1d5db;
  border-radius: 50%;
}

.indicator-dot.active {
  background: #4c8bf5;
}
</style>
