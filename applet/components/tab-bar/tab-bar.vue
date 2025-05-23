<template>
	<tui-tabbar :current="current"  backdropFilter backgroundColor="#f8f8f8" :tabBar="tabBar" color="#777" selectedColor="#AC9157" @click="tabbarSwitch"></tui-tabbar>

</template>

<script setup>
const props = defineProps({
	current: {
		type: Number,
		default: 0
	},
	tabBar: {
		type: Array,
		default: () => [
			{
				pagePath: '/pages/tabbar/index/index',
				text: 'code',
				iconPath: '/static/images/tabbar/code_gray.png',
				selectedIconPath: '/static/images/tabbar/code_active.png'
			},
		]
	}
})

const tabbarSwitch = (e) => {
	props.current = e.index;
}
</script>

<style lang="scss" scoped>
	.navigation-bar {
		width: 100%;
		
		.title {
			color: #242424ff;
			font-size: 32rpx;
			font-weight: 600;
			position: absolute;
			width: 100%;
			top: 0;
			text-align: center;
			pointer-events: none;
		}
		
		.back {
			height: 100%;
			width: 80rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			
			image {
				width: 40rpx;
				height: 40rpx;
			}
		}
	}
</style>
