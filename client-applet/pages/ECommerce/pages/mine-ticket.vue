<template>
  <custom-navigation-bar :back="true" position="flex" title="我的票务" />

  <view class="mine-ticket-page">
    <view class="ticket-list">
      <view 
        v-for="ticket in ticketList" 
        :key="ticket.id" 
        class="ticket-item"
      >
        <view class="ticket-image">
          <image 
            :src="ticket?.theme?.posterUrl" 
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
      </view>
    </view>
    
    <view v-if="ticketList.length === 0" class="empty-state">
      <text>暂无票券</text>
    </view>
  </view>
</template>

<script setup>
import { onLoad } from '@dcloudio/uni-app'
import { ref } from 'vue'
import { ticket as ticketAPI } from '@/api'

const ticketList = ref([])

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
  background-color: #fff;
  border-radius: 16rpx;
  padding: 20rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
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
</style>
