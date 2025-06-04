<script>
	import login from '@/common/login';
	
	export default {
		onLaunch: function() {
			uni.hideTabBar();
			// 默认登录
			// login();
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style>
	/*每个页面公共css */
</style>
@/common/login