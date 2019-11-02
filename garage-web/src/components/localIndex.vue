<template>
<Row>
<Col span="20">
        <div class="Ordertransfer">
		<div class="userNavigation">
			<span>安全车库数据管理  ></span>
			<span>用户登录</span>
		</div>
		<div class="content">
			<span class="font14  black block blod marginTop10">登录信息 </span>
       		<div class="marginTop20  allWidth">
			<Form  :model="login" :label-width="120" inline>
					<Form-item label="用户名" class="redre">
						<Input style="width: 250px;" :title="login.userName" v-model="login.userName" placeholder="请输入用户名"></Input>
					</Form-item>
					<Form-item v-if="login_flg" label="状态:已登录" class="redre">
							
					</Form-item>
					<Form-item v-if="logout_flg" label="状态:已退出" class="redre">
							
					</Form-item>
					<br />
					<Form-item label="密码" class="redre">
						<Input style="width: 250px;" :title="login.password" v-model="login.password" placeholder="请输入用户名"></Input>
					</Form-item>
			</Form>
			</div>
			<div class="allWidth marginTop50">
				<div class="textCenter">
					<span class="search block" @click="loginEvent" >登录</span>
					<span class="search block" @click="logOutEvent" style="margin-left: 20px;">退出登录</span>	
				</div>
			</div>
		</div>
	</div>
</Col>
</Row>
	
</template>

<script>
	export default{
		data(){
			return {
				login_flg:false,
				logout_flg:true,
				mask:false,
				isShowBtn:false,
				total:0,
				page:1,
				pagesize:10,
				Id:1517308164556,
				login:{/*入参大对象*/
					userName:"",
					password:"",
				},
				queryData:{},
				queryData1:{},
				getOrder:{},
				VenShow:false,
				data1:[/*弹窗列表*/
				 
				],
				detailList:[],
				idData:[],/*勾选储存*/
				list:[],/*勾选保存参数*/
				lengTh:false,
			}
		},
		methods:{
			loginEvent(){/*登陸按鈕*/
				let list ={
					"password":this.login.password,
					"userName":this.login.userName
				}
				this.$http.post('/garage/local/login',list,{
					params: "application"
				}).then(function(res) {
					this.login_flg=res.data.success;
					this.logout_flg=!this.login_flg;
					if(res.data.success==true){
						this.$Modal.error({
							content: res.data.data,
							onOk: () => {
							},
						});
					}else{
						this.$Modal.error({
							content: res.data.errorMessage,
							onOk: () => {},
						});
					};
				});
			},
            logOutEvent(){/*退出按鈕*/
				let list ={
					"password":this.login.password,
					"userName":this.login.userName
				}
				this.$http.post('/garage/local/loginOut',list,{
					params: "application"
				}).then(function(res) {
					this.logout_flg=res.data.success;
					this.login_flg=!this.logout_flg;
					if(res.data.success==true){
						this.$Modal.error({
							content: res.data.data,
							onOk: () => {
									
							},
						});
					}else{
						this.$Modal.error({
							content: res.data.errorMessage,
							onOk: () => {},
						});
					};
				});
			}
		
	},
	created(){
			
	}
}
</script>

<style lang="less" scoped>
	.marginTop50 {
		margin-top: 50px
	}
	.allWidth{
		width: 100%
	}
	.textCenter{
		text-align: center;
	}
	.block{
		display: inline-block;
	}
	.search{
		font-size: 14px;
		background-color: #1D86F4;
		color: #fff;
		height: 28px;
		line-height: 28px;
		cursor: pointer;
		border-radius: 2px;
		padding:0 16px;
	}
	.Ordertransfer{
		.content{
			padding:30px;
		.redre{
			>label:before{
				content: '*';
			    display: inline-block;
			    margin-right: 4px;
			    line-height: 1;
			    font-family: SimSun;
			    font-size: 12px;
			    color: #f30;
			}
		}
		.ivu-form {
			.ivu-select.ivu-select-single {
				width: 250px !important;
			}
			.w176.ivu-select.ivu-select-single{
				width: 176px !important;
			}
			.w196.ivu-select.ivu-select-single{
				width: 196px !important;
			}
			.w156.ivu-select.ivu-select-single,.w156.ivu-input-type,span.w156{
				width: 157px !important;
			}
			.w140.ivu-select.ivu-select-single{
				width: 140px !important;
			}
		}
			.ivu-btn-ghost{
			    font-size: 14px !important;
			    border: 1px solid #D8D8D8 !important;
			    color: #666666 !important;
			    padding: 0 16px !important;
			    height: 28px !important;
			    line-height: 28px !important;
			    cursor: pointer !important;
			    border-radius: 2px !important;
			}
			.ivu-btn-primary{
			    font-size: 14px !important;
			    background-color: #1D86F4 !important;
			    color: #fff !important;
			    height: 28px !important;
			    line-height: 28px !important;
			    cursor: pointer !important;
			    border-radius: 2px !important;
			    padding: 0 16px !important;
			}
			.ivu-btn-primary[disabled]{
				    color: #c3cbd6 !important;
		    background-color: #f7f7f7 !important;
		    border-color: #d7dde4 !important;
			}
			.ivu-btn-primary[disabled]:hover{
			    color: #c3cbd6 !important;
			    background-color: #f7f7f7 !important;
			    border-color: #d7dde4 !important;
			    cursor: not-allowed !important;
			}
			.atSpin{
		z-index: 9999 !important;
    	background-color: rgba(0,0,0,0.5)!important;
		width: 0 !important;
		height: auto !important;
		top: 400px !important;
		left: 47% !important;
		.ivu-spin-text{
			position: absolute;
			color: gainsboro;
			>demo-spin-icon-load{
				font-weight: bold;
			}
			>div{
				width: 100px;
				margin-top: 5px;
				font-weight: bold;
			}
		}
		}
		}
	}
	
</style>