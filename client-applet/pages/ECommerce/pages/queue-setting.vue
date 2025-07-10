<template>
  <custom-navigation-bar :back="true" position="flex" title="排队设置" />

  <view class="queue-setting-page">
    <view class="theme-list">
      <view 
        v-for="theme in themeList" 
        :key="theme.id" 
        class="theme-item"
        @click="openQueueSetting(theme)"
      >
        <view class="theme-image">
          <image 
            :src="theme.posterUrl" 
            class="theme-img"
            mode="aspectFill"
          />
        </view>
        <view class="theme-info">
          <view class="theme-name">{{ theme.themeName }}</view>
          <view class="queue-duration">
            排队时间：{{ theme.queueDuration || 0 }}分钟
          </view>
        </view>
        <view class="setting-icon">
          <text class="icon">></text>
        </view>
      </view>
    </view>
    
    <view v-if="themeList.length === 0" class="empty-state">
      <text>暂无主题</text>
    </view>
  </view>

  <!-- 排队时间设置弹窗 -->
  <uni-popup ref="queuePopup" type="center">
    <view class="popup-content">
      <view class="popup-title">设置排队时间</view>
      <view class="popup-subtitle">{{ currentTheme?.themeName }}</view>
      <view class="input-section">
        <view class="input-label">排队时间（分钟）</view>
        <input 
          v-model="inputQueueDuration" 
          type="number" 
          placeholder="请输入排队时间"
          class="duration-input"
        />
      </view>
      <view class="popup-buttons">
        <button class="cancel-btn" @click="closePopup">取消</button>
        <button class="confirm-btn" @click="submitQueueDuration">提交</button>
      </view>
    </view>
  </uni-popup>
</template>

<script setup>
import { onLoad } from '@dcloudio/uni-app'
import { ref } from 'vue'
import { theme as themeAPI } from '@/api'

const themeList = ref([])
const currentTheme = ref(null)
const inputQueueDuration = ref('')
const queuePopup = ref(null)

// 查询主题列表
const queryThemes = async () => {
  try {
    const response = await themeAPI.query()
    if (response && response.records) {
      themeList.value = response.records
    } else if (response && Array.isArray(response)) {
      themeList.value = response
    }
  } catch (error) {
    console.error('查询主题列表失败:', error)
    uni.showToast({
      title: '查询失败',
      icon: 'none'
    })
  }
}

// 打开排队设置弹窗
const openQueueSetting = (theme) => {
  currentTheme.value = theme
  inputQueueDuration.value = theme.queueDuration || ''
  queuePopup.value.open()
}

// 关闭弹窗
const closePopup = () => {
  queuePopup.value.close()
  currentTheme.value = null
  inputQueueDuration.value = ''
}

// 提交排队时间
const submitQueueDuration = async () => {
  if (!inputQueueDuration.value || inputQueueDuration.value < 0) {
    uni.showToast({
      title: '请输入有效的排队时间',
      icon: 'none'
    })
    return
  }

  try {
    uni.showLoading({
      title: '设置中...'
    })

    await themeAPI.updateQueueDuration(currentTheme.value.id, Number(inputQueueDuration.value))

    // 更新本地数据
    const themeIndex = themeList.value.findIndex(item => item.id === currentTheme.value.id)
    if (themeIndex !== -1) {
      themeList.value[themeIndex].queueDuration = Number(inputQueueDuration.value)
    }

    uni.hideLoading()
    uni.showToast({
      title: '设置成功',
      icon: 'success'
    })
    
    closePopup()
  } catch (error) {
    uni.hideLoading()
    console.error('设置排队时间失败:', error)
    uni.showToast({
      title: '设置失败',
      icon: 'none'
    })
  }
}

onLoad(() => {
  queryThemes()
})
</script>

<style scoped>
.queue-setting-page {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.theme-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.theme-item {
  display: flex;
  align-items: center;
  background-color: #fff;
  border-radius: 16rpx;
  padding: 20rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
}

.theme-image {
  width: 120rpx;
  height: 120rpx;
  margin-right: 20rpx;
}

.theme-img {
  width: 100%;
  height: 100%;
  border-radius: 12rpx;
}

.theme-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.theme-name {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 16rpx;
  line-height: 1.4;
}

.queue-duration {
  font-size: 28rpx;
  color: #666;
  line-height: 1.3;
}

.setting-icon {
  width: 40rpx;
  display: flex;
  justify-content: center;
  align-items: center;
}

.icon {
  font-size: 32rpx;
  color: #999;
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400rpx;
  color: #999;
  font-size: 28rpx;
}

/* 弹窗样式 */
.popup-content {
  width: 600rpx;
  background-color: #fff;
  border-radius: 20rpx;
  padding: 40rpx;
}

.popup-title {
  font-size: 36rpx;
  font-weight: 600;
  color: #333;
  text-align: center;
  margin-bottom: 16rpx;
}

.popup-subtitle {
  font-size: 28rpx;
  color: #666;
  text-align: center;
  margin-bottom: 40rpx;
}

.input-section {
  margin-bottom: 40rpx;
}

.input-label {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 16rpx;
}

.duration-input {
  width: 100%;
  height: 80rpx;
  border: 2rpx solid #e5e5e5;
  border-radius: 12rpx;
  padding: 0 20rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.duration-input:focus {
  border-color: #007aff;
}

.popup-buttons {
  display: flex;
  gap: 20rpx;
}

.cancel-btn,
.confirm-btn {
  flex: 1;
  height: 80rpx;
  border-radius: 12rpx;
  font-size: 28rpx;
  border: none;
}

.cancel-btn {
  background-color: #f5f5f5;
  color: #666;
}

.confirm-btn {
  background-color: #007aff;
  color: #fff;
}
</style>