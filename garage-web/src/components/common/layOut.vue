<!-- 根据公司UI的样式布局 -->
<!-- 没有搜索条件的页面，加上 v-if="noSearch" -->
<template>
	<div class="info-bg">
		<!-- 查询条件等部分 -->
		<div class="bg-class search-class" v-if="!noSearch">
			<slot name="header"></slot>
		</div>
		<!-- 正文等部分 -->
		<div class="bg-class main-class" v-if="!noSearch">
			<slot name="main"></slot>
		</div>
		<div class="bg-class no-search-class" v-if="noSearch">
			<slot name="main"></slot>
		</div>
		<!-- 模态窗部分 -->
		<slot name="modal"></slot>
	</div>
</template>

<script>
	export default {
		props:['no-search'],
		data() {
			return {

			}
		},
		methods: {}
	}
</script>

<style>

</style>