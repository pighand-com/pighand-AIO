<template>
	<view class="page">
		<custom-navigation-bar :back="false" position="flex" />

		<!-- 背景图片 -->
		<view class="bg-container">
			<image class="bg-image" src="https://pighand-dev-1253579824.cos.ap-beijing.myqcloud.com/banner/4_1750729268223_0_60.png" mode="widthFix"></image>
			<view class="bg-gradient"></view>
		</view>
		
		<!-- 内容容器 -->
		<view class="content-container">
			<!-- 用户信息区域 -->
			<view class="user-info">
				<!-- 头像 -->
				<user-check :item="['login', 'avatar']">
					<view class="avatar-container">
						<image class="avatar" :src="userInfo?.avatar || defaultAvatar" mode="aspectFill"></image>
					</view>
				</user-check>
				
				<!-- 手机号 -->
				<view class="phone-container">
					<user-check :item="['login', 'phone']">
						<text class="phone-text">{{ userInfo?.phone || '获取手机号' }}</text>
					</user-check>
				</view>
				
				<!-- 右侧图标区域 -->
				<view class="icon-group">
					<!-- 分销二维码图标 -->
					<view class="qr-icon-container" v-if="salespersonId" @click="showDistributionQR">
						<view class="qr-icon">
							<image src="../static/qr-code.svg" class="qr-icon-image" mode="aspectFit"></image>
						</view>
					</view>
					
					<!-- 设置图标 -->
					<view v-if="isLogin" class="settings-icon-container" @click="showSettings">
						<view class="settings-icon">
							<image src="../static/setting.svg" class="settings-icon-image" mode="aspectFit"></image>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 分销区域 -->
			<view class="distribution-section" v-if="salespersonId">
				<user-check :item="['login']">
					<view class="distribution-box" @click="goToDistribution">
						<text class="distribution-text">我的分销</text>
						<text class="distribution-arrow">{{">"}}</text>
					</view>
				</user-check>
			</view>
			
			<!-- 排队设置区域 -->
			<view class="queue-section" v-if="isStaff">
				<user-check :item="['login']">
					<view class="queue-box" @click="goToQueueSettings">
						<text class="queue-text">排队设置</text>
						<text class="queue-arrow">{{">"}}</text>
					</view>
				</user-check>
			</view>
			
			<!-- 核销区域 -->
			<view class="verification-section" v-if="isStaff">
				<user-check :item="['login']">
					<view class="verification-box" @click="goToVerification">
						<text class="verification-text">核销</text>
						<text class="verification-arrow">{{">"}}</text>
					</view>
				</user-check>
			</view>
			
			<!-- 打卡确认区域 -->
			<view class="checkin-section" v-if="isStaff">
				<user-check :item="['login']">
					<view class="checkin-box" @click="goToCheckIn">
						<text class="checkin-text">打卡确认</text>
						<text class="checkin-arrow">{{">"}}</text>
					</view>
				</user-check>
			</view>
			
			<!-- 订单区域 -->
			<view class="order-section">
				<user-check :item="['login']">
					<view class="order-box" @click="goToOrder">
						<text class="order-text">我的订单</text>
						<text class="order-arrow">{{">"}}</text>
					</view>
				</user-check>
			</view>
			
			<!-- 票务区域 -->
			<view class="ticket-section">
				<user-check :item="['login']">
					<view class="ticket-box" @click="goToTicket">
						<text class="ticket-text">我的票务</text>
						<text class="ticket-arrow">{{">"}}</text>
					</view>
				</user-check>
			</view>
		</view>
		
		<tab-bar />
		
		<!-- 联系我们悬浮按钮 -->
		<view class="contact-float-btn" @click="showContactModal">
			<image src="../static/phone-icon.svg" class="contact-phone-icon" mode="aspectFit"></image>
			<text class="contact-float-text">联系我们</text>
		</view>
		
		<!-- 分销二维码弹窗 -->
		<view class="qr-modal" v-if="isQrModalVisible" @click="hideQrModal">
			<view class="qr-modal-content" @click.stop>
				<view class="qr-image-container">
					<image :src="distributionQrCode" mode="aspectFit" class="qr-image" show-menu-by-longpress></image>
				</view>
				<text class="qr-tip">长按保存二维码</text>
			</view>
		</view>
		
		<!-- 设置弹窗 -->
		<view class="settings-modal" v-if="isSettingsModalVisible" @click="hideSettings">
			<view class="settings-modal-content" @click.stop>
				<view class="settings-header">
					<text class="settings-title">设置</text>
				</view>
				<view class="settings-body">
					<view class="settings-item" @click="logout">
						<text class="settings-item-text">退出登录</text>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 联系我们弹窗 -->
		<view class="contact-modal" v-if="isContactModalVisible" @click="hideContactModal">
			<view class="contact-modal-content" @click.stop>
				<view class="contact-header">
					<text class="contact-title">联系我们</text>
				</view>
				<view class="contact-body">
					<view class="contact-item" @click="makePhoneCall('投诉反馈')">
						<image src="../static/phone-icon.svg" class="contact-item-icon" mode="aspectFit"></image>
						<text class="contact-item-text">投诉反馈</text>
					</view>
					<view class="contact-item" @click="makePhoneCall('品牌合作')">
						<image src="../static/phone-icon.svg" class="contact-item-icon" mode="aspectFit"></image>
						<text class="contact-item-text">品牌合作</text>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 打卡地点选择弹窗 -->
		<view class="checkin-modal" v-if="isCheckInModalVisible" @click="hideCheckInModal">
			<view class="checkin-modal-content" @click.stop>
				<view class="checkin-header">
					<text class="checkin-title">选择打卡地点</text>
				</view>
				<view class="checkin-body">
					<view class="checkin-location-list">
						<view 
							v-for="location in checkInLocations" 
							:key="location.id" 
							class="checkin-location-item"
							:class="{ 'selected': selectedLocationId === location.id }"
							@click="selectCheckInLocation(location.id)"
						>
							<text class="checkin-location-name">{{ location.name }}</text>
							<view class="checkin-location-check" v-if="selectedLocationId === location.id">
								<text class="checkin-check-icon">✓</text>
							</view>
						</view>
					</view>
					<view class="checkin-actions">
						<view class="checkin-cancel-btn" @click="hideCheckInModal">
							<text class="checkin-cancel-text">取消</text>
						</view>
						<view class="checkin-confirm-btn" @click="confirmCheckIn">
							<text class="checkin-confirm-text">确认打卡</text>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
import { onShow, onLoad } from '@dcloudio/uni-app'
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { getUserInfo, getToken, getSalespersonId, setSalespersonId, clearAll } from '@/common/storage'
import { distribution as distributionAPI, ticket as ticketAPI, checkInLocation as checkInLocationAPI } from '@/api'

import defaultAvatar from '../../../static/default-avatar.jpg'

const isLogin = ref(!!getToken())
const userInfo = ref(getUserInfo())
const salespersonId = ref(getSalespersonId())
const isQrModalVisible = ref(false)
const distributionQrCode = ref('')
const isSettingsModalVisible = ref(false)
const isContactModalVisible = ref(false)
const isStaff = ref(false)

// 打卡相关变量
const isCheckInModalVisible = ref(false)
const checkInLocations = ref([])
const selectedLocationId = ref('')

// 检查用户是否为工作人员（roleId=9000）
const checkIsStaff = async () => {
    const user = userInfo.value

	isStaff.value = user?.roles
		&& Array.isArray(user.roles)
		&& user.roles.some(role => role.roleId == 9000)
}

// 检查并设置salespersonId
const checkAndSetSalespersonId = async () => {
    const token = getToken()
    if (!token) {
        salespersonId.value = null
        return // 未登录直接返回
    }
    
    const currentSalespersonId = getSalespersonId()
    if (currentSalespersonId) {
        salespersonId.value = currentSalespersonId
        return // 已设置直接返回
    }
    
    try {
        const response = await distributionAPI.salesperson()
        if (response) {
            setSalespersonId(response)
            salespersonId.value = response
        }
    } catch (error) {
        console.log('获取销售员ID失败:', error)
    }
}

// 监听storage变化
const handleStorageChange = () => {
    userInfo.value = getUserInfo()
	isLogin.value = !!getToken()

	checkIsStaff()

    checkAndSetSalespersonId()
}

// 页面显示时检查salespersonId
onShow(() => {
    handleStorageChange()
})

uni.$on('storage-changed', handleStorageChange)

// 组件卸载时移除监听器
onUnmounted(() => {
    uni.$off('storage-changed', handleStorageChange)
})


// 跳转到订单页面
function goToOrder() {
    uni.navigateTo({
        url: '/pages/ECommerce/pages/order-list'
    })
}

// 跳转到票务页面
function goToTicket() {
    uni.navigateTo({
        url: '/pages/ECommerce/pages/mine-ticket'
    })
}

// 跳转到分销页面
function goToDistribution() {
    uni.navigateTo({
        url: '/pages/ECommerce/pages/distribution-list'
    })
}

// 跳转到排队设置页面
function goToQueueSettings() {
    uni.navigateTo({
        url: '/pages/ECommerce/pages/queue-setting'
    })
}

// 核销功能 - 打开扫码
function goToVerification() {
    uni.scanCode({
        success: async function (res) {
            try {
                // 解析扫码结果
                const scanResult = JSON.parse(res.result)
                
                // 检查结果格式是否正确
                if (!Array.isArray(scanResult) || scanResult.length === 0) {
                    uni.showToast({
                        title: '查询失败',
                        icon: 'none'
                    })
                    return
                }
                
                // 跳转到核销页面，传递扫码结果
                uni.navigateTo({
                    url: `/pages/ECommerce/pages/ticket-verification?data=${encodeURIComponent(res.result)}`
                })
            } catch (error) {
                console.error('解析扫码结果失败:', error)
                uni.showToast({
                    title: '查询失败',
                    icon: 'none'
                })
            }
        },
        fail: function (err) {
            uni.showToast({
                title: '扫码失败',
                icon: 'none'
            })
        }
    })
}

// 打卡确认功能 - 打开扫码
function goToCheckIn() {
    uni.scanCode({
        success: async function (res) {
            try {
                // 解析扫码结果
                const scanResult = JSON.parse(res.result)
                
                // 检查结果格式是否正确
                if (!scanResult.userId || scanResult.type !== 'checkIn') {
                    uni.showToast({
                        title: '二维码错误',
                        icon: 'none'
                    })
                    return
                }
                
                // 获取打卡地点列表并显示选择弹窗
                await loadCheckInLocations()
                showCheckInModal()
                
            } catch (error) {
                console.error('解析扫码结果失败:', error)
                uni.showToast({
                    title: '二维码错误',
                    icon: 'none'
                })
            }
        },
        fail: function (err) {
            uni.showToast({
                title: '扫码失败',
                icon: 'none'
            })
        }
    })
}

// 加载打卡地点列表
const loadCheckInLocations = async () => {
    try {
        const response = await checkInLocationAPI.getLocations()
		checkInLocations.value = response.records
		
		// 从localStorage获取上次选择的地点
		const savedLocationId = uni.getStorageSync('selectedCheckInLocationId')
		if (savedLocationId && response.records.find(loc => loc.id === savedLocationId)) {
			selectedLocationId.value = savedLocationId
		} else if (response.length > 0) {
			selectedLocationId.value = response[0].id
		}
    } catch (error) {
        console.error('获取打卡地点失败:', error)
        uni.showToast({
            title: '获取打卡地点失败',
            icon: 'none'
        })
    }
}

// 显示打卡地点选择弹窗
const showCheckInModal = () => {
    isCheckInModalVisible.value = true
}

// 隐藏打卡地点选择弹窗
const hideCheckInModal = () => {
    isCheckInModalVisible.value = false
}

// 选择打卡地点
const selectCheckInLocation = (locationId) => {
    selectedLocationId.value = locationId
}

// 确认打卡
const confirmCheckIn = async () => {
    if (!selectedLocationId.value) {
        uni.showToast({
            title: '请选择打卡地点',
            icon: 'none'
        })
        return
    }
    
    try {
        const response = await checkInLocationAPI.checkIn(selectedLocationId.value)
        
        // 保存选择的地点到localStorage
        uni.setStorageSync('selectedCheckInLocationId', selectedLocationId.value)
        
        hideCheckInModal()
        
        uni.showToast({
            title: '打卡成功',
            icon: 'success'
        })
    } catch (error) {
        console.error('打卡失败:', error)
        uni.showToast({
            title: '打卡失败',
            icon: 'none'
        })
    }
}

// 显示分销二维码
const showDistributionQR = async () => {
    try {
        const response = await distributionAPI.QRCode()
        if (response) {
            distributionQrCode.value = `data:image/png;base64,${response}`
            isQrModalVisible.value = true
        } else {
            uni.showToast({
                title: '获取二维码失败',
                icon: 'none'
            })
        }
    } catch (error) {
        console.log('获取分销二维码失败:', error)
        uni.showToast({
            title: '获取二维码失败',
            icon: 'none'
        })
    }
}

// 隐藏二维码弹窗
const hideQrModal = () => {
    isQrModalVisible.value = false
}

// 显示设置弹窗
const showSettings = () => {
    isSettingsModalVisible.value = true
}

// 隐藏设置弹窗
const hideSettings = () => {
    isSettingsModalVisible.value = false
}

// 退出登录
const logout = () => {
    uni.showModal({
        title: '确认退出',
        content: '确定要退出登录吗？',
        success: (res) => {
            if (res.confirm) {
                clearAll()
                hideSettings()
                uni.showToast({
                    title: '已退出登录',
                    icon: 'success'
                })
            }
        }
    })
}

// 显示联系我们弹窗
const showContactModal = () => {
    isContactModalVisible.value = true
}

// 隐藏联系我们弹窗
const hideContactModal = () => {
    isContactModalVisible.value = false
}

// 拨打电话
const makePhoneCall = (type) => {
    hideContactModal()
    uni.makePhoneCall({
        phoneNumber: '18182400663',
        success: () => {
            console.log(`${type}电话拨打成功`)
        },
        fail: (err) => {
            console.log(`${type}电话拨打失败:`, err)
            uni.showToast({
                title: '拨打失败',
                icon: 'none'
            })
        }
    })
}
</script>

<style scoped>
.page {
	position: relative;
	min-height: 100vh;
}

.bg-container {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	z-index: 0;
}

.bg-image {
	width: 100%;
	height: auto;
	display: block;
}

.bg-gradient {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: linear-gradient(to bottom, transparent 0%,#fff 60%, #fff 100%);
}

.content-container {
	margin: 60rpx 30rpx;
	background: rgba(255, 255, 255, 0.15);
	border-radius: 32rpx;
	box-shadow: 0 16rpx 48rpx rgba(0, 0, 0, 0.2);
	backdrop-filter: blur(20rpx);
	border: 2rpx solid rgba(255, 255, 255, 0.2);
	position: relative;
	z-index: 1;
	padding: 48rpx;
}

.user-info {
	display: flex;
    align-items: center;
    margin-bottom: 48rpx;
    padding-bottom: 32rpx;
    border-bottom: 2rpx solid rgba(255, 255, 255, 0.3);
    gap: 20rpx; /* 添加元素间距 */
}

.avatar-container {
	flex-shrink: 0; /* 防止头像被压缩 */
}

.avatar {
	width: 140rpx;
	height: 140rpx;
	border-radius: 70rpx;
	border: 6rpx solid rgba(255, 255, 255, 0.4);
	box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.2);
}

.phone-container {
	flex: 1;
	min-width: 0; /* 允许文本截断 */
	margin-right: 20rpx; /* 给右侧图标留出空间 */
}

.phone-text {
	font-size: 36rpx;
	color: #fff;
	font-weight: 600;
	text-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.3);
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap; /* 防止长手机号换行 */
}

.order-section {
	margin-top: 32rpx;
}

.ticket-section {
	margin-top: 32rpx;
}

.distribution-section {
	margin-top: 32rpx;
}

.queue-section {
	margin-top: 32rpx;
}

.verification-section {
	margin-top: 32rpx;
}

.order-box {
	background: linear-gradient(135deg, rgba(255, 255, 255, 0.4), rgba(255, 255, 255, 0.25));
	border-radius: 24rpx;
	padding: 48rpx 36rpx;
	display: flex;
	justify-content: space-between;
	align-items: center;
	box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.25), 0 0 0 2rpx rgba(255, 255, 255, 0.4);
	border: 3rpx solid rgba(255, 255, 255, 0.4);
	backdrop-filter: blur(15rpx);
	transition: all 0.3s ease;
	position: relative;
}

.order-text {
	font-size: 36rpx;
	color: #333;
	font-weight: 600;
	text-shadow: 0 1rpx 2rpx rgba(255, 255, 255, 0.8);
}

.order-arrow {
	font-size: 36rpx;
	color: #666;
	font-weight: bold;
	text-shadow: 0 1rpx 2rpx rgba(255, 255, 255, 0.8);
}

.order-box::before {
	content: '';
	position: absolute;
	top: -2rpx;
	left: -2rpx;
	right: -2rpx;
	bottom: -2rpx;
	background: linear-gradient(135deg, rgba(255, 255, 255, 0.6), rgba(255, 255, 255, 0.3));
	border-radius: 26rpx;
	z-index: -1;
	opacity: 0.8;
}

.order-box:active {
	transform: scale(0.98);
	box-shadow: 0 6rpx 24rpx rgba(0, 0, 0, 0.3);
}

.ticket-box {
	background: linear-gradient(135deg, rgba(255, 255, 255, 0.4), rgba(255, 255, 255, 0.25));
	border-radius: 24rpx;
	padding: 48rpx 36rpx;
	display: flex;
	justify-content: space-between;
	align-items: center;
	box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.25), 0 0 0 2rpx rgba(255, 255, 255, 0.4);
	border: 3rpx solid rgba(255, 255, 255, 0.4);
	backdrop-filter: blur(15rpx);
	transition: all 0.3s ease;
	position: relative;
}

.ticket-text {
	font-size: 36rpx;
	color: #333;
	font-weight: 600;
	text-shadow: 0 1rpx 2rpx rgba(255, 255, 255, 0.8);
}

.ticket-arrow {
	font-size: 36rpx;
	color: #666;
	font-weight: bold;
	text-shadow: 0 1rpx 2rpx rgba(255, 255, 255, 0.8);
}

.ticket-box::before {
	content: '';
	position: absolute;
	top: -2rpx;
	left: -2rpx;
	right: -2rpx;
	bottom: -2rpx;
	background: linear-gradient(135deg, rgba(255, 255, 255, 0.6), rgba(255, 255, 255, 0.3));
	border-radius: 26rpx;
	z-index: -1;
	opacity: 0.8;
}

.ticket-box:active {
	transform: scale(0.98);
	box-shadow: 0 6rpx 24rpx rgba(0, 0, 0, 0.3);
}

.distribution-box {
	background: linear-gradient(135deg, rgba(255, 255, 255, 0.4), rgba(255, 255, 255, 0.25));
	border-radius: 24rpx;
	padding: 48rpx 36rpx;
	display: flex;
	justify-content: space-between;
	align-items: center;
	box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.25), 0 0 0 2rpx rgba(255, 255, 255, 0.4);
	border: 3rpx solid rgba(255, 255, 255, 0.4);
	backdrop-filter: blur(15rpx);
	transition: all 0.3s ease;
	position: relative;
}

.distribution-text {
	font-size: 36rpx;
	color: #333;
	font-weight: 600;
	text-shadow: 0 1rpx 2rpx rgba(255, 255, 255, 0.8);
}

.distribution-arrow {
	font-size: 36rpx;
	color: #666;
	font-weight: bold;
	text-shadow: 0 1rpx 2rpx rgba(255, 255, 255, 0.8);
}

.distribution-box::before {
	content: '';
	position: absolute;
	top: -2rpx;
	left: -2rpx;
	right: -2rpx;
	bottom: -2rpx;
	background: linear-gradient(135deg, rgba(255, 255, 255, 0.6), rgba(255, 255, 255, 0.3));
	border-radius: 26rpx;
	z-index: -1;
	opacity: 0.8;
}

.distribution-box:active {
	transform: scale(0.98);
	box-shadow: 0 6rpx 24rpx rgba(0, 0, 0, 0.3);
}

.queue-box {
	background: linear-gradient(135deg, rgba(255, 255, 255, 0.4), rgba(255, 255, 255, 0.25));
	border-radius: 24rpx;
	padding: 48rpx 36rpx;
	display: flex;
	justify-content: space-between;
	align-items: center;
	box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.25), 0 0 0 2rpx rgba(255, 255, 255, 0.4);
	border: 3rpx solid rgba(255, 255, 255, 0.4);
	backdrop-filter: blur(15rpx);
	transition: all 0.3s ease;
	position: relative;
}

.queue-text {
	font-size: 36rpx;
	color: #333;
	font-weight: 600;
	text-shadow: 0 1rpx 2rpx rgba(255, 255, 255, 0.8);
}

.queue-arrow {
	font-size: 36rpx;
	color: #666;
	font-weight: bold;
	text-shadow: 0 1rpx 2rpx rgba(255, 255, 255, 0.8);
}

.queue-box::before {
	content: '';
	position: absolute;
	top: -2rpx;
	left: -2rpx;
	right: -2rpx;
	bottom: -2rpx;
	background: linear-gradient(135deg, rgba(255, 255, 255, 0.6), rgba(255, 255, 255, 0.3));
	border-radius: 26rpx;
	z-index: -1;
	opacity: 0.8;
}

.queue-box:active {
	transform: scale(0.98);
	box-shadow: 0 6rpx 24rpx rgba(0, 0, 0, 0.3);
}

.verification-box {
	background: linear-gradient(135deg, rgba(255, 255, 255, 0.4), rgba(255, 255, 255, 0.25));
	border-radius: 24rpx;
	padding: 48rpx 36rpx;
	display: flex;
	justify-content: space-between;
	align-items: center;
	box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.25), 0 0 0 2rpx rgba(255, 255, 255, 0.4);
	border: 3rpx solid rgba(255, 255, 255, 0.4);
	backdrop-filter: blur(15rpx);
	transition: all 0.3s ease;
	position: relative;
}

.verification-text {
	font-size: 36rpx;
	color: #333;
	font-weight: 600;
	text-shadow: 0 1rpx 2rpx rgba(255, 255, 255, 0.8);
}

.verification-arrow {
	font-size: 36rpx;
	color: #666;
	font-weight: bold;
	text-shadow: 0 1rpx 2rpx rgba(255, 255, 255, 0.8);
}

.verification-box::before {
	content: '';
	position: absolute;
	top: -2rpx;
	left: -2rpx;
	right: -2rpx;
	bottom: -2rpx;
	background: linear-gradient(135deg, rgba(255, 255, 255, 0.6), rgba(255, 255, 255, 0.3));
	border-radius: 26rpx;
	z-index: -1;
	opacity: 0.8;
}

.verification-box:active {
	transform: scale(0.98);
	box-shadow: 0 6rpx 24rpx rgba(0, 0, 0, 0.3);
}

.checkin-box {
	background: linear-gradient(135deg, rgba(255, 255, 255, 0.4), rgba(255, 255, 255, 0.25));
	border-radius: 24rpx;
	padding: 48rpx 36rpx;
	display: flex;
	justify-content: space-between;
	align-items: center;
	box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.25), 0 0 0 2rpx rgba(255, 255, 255, 0.4);
	border: 3rpx solid rgba(255, 255, 255, 0.4);
	backdrop-filter: blur(15rpx);
	transition: all 0.3s ease;
	position: relative;
}

.checkin-text {
	font-size: 36rpx;
	color: #333;
	font-weight: 600;
	text-shadow: 0 1rpx 2rpx rgba(255, 255, 255, 0.8);
}

.checkin-arrow {
	font-size: 36rpx;
	color: #666;
	font-weight: bold;
	text-shadow: 0 1rpx 2rpx rgba(255, 255, 255, 0.8);
}

.checkin-box::before {
	content: '';
	position: absolute;
	top: -2rpx;
	left: -2rpx;
	right: -2rpx;
	bottom: -2rpx;
	background: linear-gradient(135deg, rgba(255, 255, 255, 0.6), rgba(255, 255, 255, 0.3));
	border-radius: 26rpx;
	z-index: -1;
	opacity: 0.8;
}

.checkin-box:active {
	transform: scale(0.98);
	box-shadow: 0 6rpx 24rpx rgba(0, 0, 0, 0.3);
}

.checkin-section {
	margin-top: 32rpx;
}

/* 联系我们悬浮按钮 */
.contact-float-btn {
	position: fixed;
	bottom: 200rpx;
	left: 50%;
	transform: translateX(-50%);
	width: 200rpx;
	height: 80rpx;
	background: linear-gradient(135deg, #F5F5F5, #E8E8E8);
	border-radius: 40rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	gap: 12rpx;
	box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.15);
	z-index: 1000;
	transition: all 0.3s ease;
	border: 2rpx solid rgba(0, 0, 0, 0.1);
}

.contact-float-btn:active {
	transform: translateX(-50%) scale(0.95);
	box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.25);
}

.contact-phone-icon {
	width: 32rpx;
	height: 32rpx;
	filter: brightness(0);
}

.contact-float-text {
	font-size: 28rpx;
	color: #333333;
	font-weight: 600;
}

/* 二维码图标样式 */
.icon-group {
	display: flex;
	align-items: center;
	gap: 32rpx; /* 增加图标间距从20rpx到32rpx */
	flex-shrink: 0; /* 防止图标被压缩 */
}

.qr-icon-container {
	display: flex;
	align-items: center;
	justify-content: center;
}

.qr-icon {
	width: 72rpx; /* 稍微减小图标尺寸从80rpx到72rpx */
	height: 72rpx;
	border-radius: 50%;
	background: linear-gradient(135deg, rgba(255, 255, 255, 0.4), rgba(255, 255, 255, 0.25));
	border: 3rpx solid rgba(255, 255, 255, 0.4);
	box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.2);
	backdrop-filter: blur(15rpx);
	display: flex;
	align-items: center;
	justify-content: center;
	transition: all 0.3s ease;
}

.qr-icon:active {
	transform: scale(0.95);
	box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.3);
}

.qr-icon-image {
	width: 36rpx; /* 相应调整图标内部图片尺寸 */
	height: 36rpx;
}

/* 设置图标样式 */
.settings-icon-container {
	display: flex;
	align-items: center;
	justify-content: center;
}

.settings-icon {
	width: 72rpx; /* 稍微减小图标尺寸从80rpx到72rpx */
	height: 72rpx;
	border-radius: 50%;
	background: linear-gradient(135deg, rgba(255, 255, 255, 0.4), rgba(255, 255, 255, 0.25));
	border: 3rpx solid rgba(255, 255, 255, 0.4);
	box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.2);
	backdrop-filter: blur(15rpx);
	display: flex;
	align-items: center;
	justify-content: center;
	transition: all 0.3s ease;
}

.settings-icon:active {
	transform: scale(0.95);
	box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.3);
}

.settings-icon-image {
	width: 36rpx; /* 相应调整图标内部图片尺寸 */
	height: 36rpx;
}

/* 设置弹窗样式 */
.settings-modal {
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

.settings-modal-content {
	width: 600rpx;
	background: linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(255, 255, 255, 0.9));
	border-radius: 32rpx;
	box-shadow: 0 16rpx 48rpx rgba(0, 0, 0, 0.3);
	backdrop-filter: blur(20rpx);
	border: 2rpx solid rgba(255, 255, 255, 0.3);
	overflow: hidden;
}

.settings-header {
	padding: 48rpx 48rpx 32rpx;
	border-bottom: 2rpx solid rgba(0, 0, 0, 0.1);
	text-align: center;
}

.settings-title {
	font-size: 40rpx;
	font-weight: 600;
	color: #333;
}

.settings-body {
	padding: 32rpx 48rpx 48rpx;
}

.settings-item {
	padding: 32rpx 0;
	border-bottom: 2rpx solid rgba(0, 0, 0, 0.05);
	transition: all 0.3s ease;
}

.settings-item:last-child {
	border-bottom: none;
}

.settings-item:active {
	background-color: rgba(0, 0, 0, 0.05);
	transform: scale(0.98);
}

.settings-item-text {
	font-size: 36rpx;
	color: #e74c3c;
	font-weight: 500;
	display: block;
	text-align: center;
}

/* 分销二维码弹窗样式 */
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
	width: 550rpx;
	padding: 40rpx;
	position: relative;
	display: flex;
	flex-direction: column;
	align-items: center;
}

.qr-image-container {
	display: flex;
	justify-content: center;
	margin-bottom: 30rpx;
	background-color: #ffffff;
	padding: 30rpx;
	border-radius: 20rpx;
	box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
}

.qr-image {
	width: 450rpx;
	height: 450rpx;
	border-radius: 20rpx;
}

.qr-tip {
	font-size: 28rpx;
	color: #ffffff;
	font-weight: bold;
	text-align: center;
	display: block;
}

/* 联系我们弹窗样式 */
.contact-modal {
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

.contact-modal-content {
	width: 600rpx;
	background: linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(255, 255, 255, 0.9));
	border-radius: 32rpx;
	box-shadow: 0 16rpx 48rpx rgba(0, 0, 0, 0.3);
	backdrop-filter: blur(20rpx);
	border: 2rpx solid rgba(255, 255, 255, 0.3);
	overflow: hidden;
}

.contact-header {
	padding: 48rpx 48rpx 32rpx;
	border-bottom: 2rpx solid rgba(0, 0, 0, 0.1);
	text-align: center;
}

.contact-title {
	font-size: 40rpx;
	font-weight: 600;
	color: #333;
}

.contact-body {
	padding: 32rpx 48rpx 48rpx;
}

.contact-item {
	padding: 32rpx 0;
	border-bottom: 2rpx solid rgba(0, 0, 0, 0.05);
	transition: all 0.3s ease;
	display: flex;
	align-items: center;
	justify-content: center;
	gap: 16rpx;
}

.contact-item:last-child {
	border-bottom: none;
}

.contact-item:active {
	background-color: rgba(0, 0, 0, 0.05);
	transform: scale(0.98);
}

.contact-item-icon {
	width: 32rpx;
	height: 32rpx;
	filter: brightness(0);
}

.contact-item-text {
	font-size: 36rpx;
	color: #333;
	font-weight: 500;
}

/* 打卡地点选择弹窗样式 */
.checkin-modal {
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

.checkin-modal-content {
	width: 600rpx;
	max-height: 80vh;
	background: linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(255, 255, 255, 0.9));
	border-radius: 32rpx;
	box-shadow: 0 16rpx 48rpx rgba(0, 0, 0, 0.3);
	backdrop-filter: blur(20rpx);
	border: 2rpx solid rgba(255, 255, 255, 0.3);
	overflow: hidden;
}

.checkin-header {
	padding: 48rpx 48rpx 32rpx;
	border-bottom: 2rpx solid rgba(0, 0, 0, 0.1);
	text-align: center;
}

.checkin-title {
	font-size: 40rpx;
	font-weight: 600;
	color: #333;
}

.checkin-body {
	padding: 32rpx 48rpx 48rpx;
}

.checkin-location-list {
	max-height: 400rpx;
	overflow-y: auto;
	margin-bottom: 32rpx;
}

/* 隐藏打卡地点列表滚动条 */
.checkin-location-list::-webkit-scrollbar {
	display: none;
}

.checkin-location-list {
	-ms-overflow-style: none;
	scrollbar-width: none;
}

.checkin-location-item {
	padding: 32rpx 24rpx;
	border-radius: 16rpx;
	margin-bottom: 16rpx;
	background: rgba(255, 255, 255, 0.6);
	border: 2rpx solid rgba(0, 0, 0, 0.1);
	display: flex;
	justify-content: space-between;
	align-items: center;
	transition: all 0.3s ease;
}

.checkin-location-item.selected {
	background: linear-gradient(135deg, #ff8c00, #ff6b00);
	border-color: #ff8c00;
	box-shadow: 0 4rpx 12rpx rgba(255, 140, 0, 0.3);
}

.checkin-location-item:active {
	transform: scale(0.98);
}

.checkin-location-name {
	font-size: 32rpx;
	color: #333;
	font-weight: 500;
}

.checkin-location-item.selected .checkin-location-name {
	color: #fff;
}

.checkin-location-check {
	width: 48rpx;
	height: 48rpx;
	border-radius: 50%;
	background: rgba(255, 255, 255, 0.9);
	display: flex;
	align-items: center;
	justify-content: center;
}

.checkin-check-icon {
	font-size: 28rpx;
	color: #ff8c00;
	font-weight: bold;
}

.checkin-actions {
	display: flex;
	gap: 24rpx;
}

.checkin-cancel-btn {
	flex: 1;
	padding: 32rpx 0;
	border-radius: 16rpx;
	background: rgba(0, 0, 0, 0.1);
	border: 2rpx solid rgba(0, 0, 0, 0.2);
	display: flex;
	align-items: center;
	justify-content: center;
	transition: all 0.3s ease;
}

.checkin-cancel-btn:active {
	transform: scale(0.98);
	background: rgba(0, 0, 0, 0.15);
}

.checkin-cancel-text {
	font-size: 32rpx;
	color: #666;
	font-weight: 500;
}

.checkin-confirm-btn {
	flex: 1;
	padding: 32rpx 0;
	border-radius: 16rpx;
	background: linear-gradient(135deg, #ff8c00, #ff6b00);
	border: 2rpx solid #ff8c00;
	display: flex;
	align-items: center;
	justify-content: center;
	transition: all 0.3s ease;
	box-shadow: 0 4rpx 12rpx rgba(255, 140, 0, 0.3);
}

.checkin-confirm-btn:active {
	transform: scale(0.98);
	box-shadow: 0 2rpx 6rpx rgba(255, 140, 0, 0.4);
}

.checkin-confirm-text {
	font-size: 32rpx;
	color: #fff;
	font-weight: 600;
}
</style>