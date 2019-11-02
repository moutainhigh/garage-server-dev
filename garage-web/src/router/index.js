import Vue from 'vue';
import Router from 'vue-router';

import localIndex from '@/components/localIndex';/*开发环境登录*/


//车库管理
import garageList from '@/components/garage_management/garageList'
import garageDetails from '@/components/garage_management/garageDetails'
//车辆管理
import vehicleList from '@/components/vehicle_Information/vehicleList'
import vehicleDetails from '@/components/vehicle_Information/vehicleDetails'
//出库待办
import sgOutTasks_list from '@/components/depot_Agent/sgOutTasks_list'
import sgOutTasks_Details from '@/components/depot_Agent/sgOutTasks_Details'
//入库待办
import sgInTasks_list from '@/components/storage_Agent/sgInTasks_list'
import sgInTasks_Details from '@/components/storage_Agent/sgInTasks_Details'
//订单查询
import orderList from '@/components/order_query/orderList'
import orderDetails from '@/components/order_query/orderDetails'
//中台
import middlePushMessage from '@/components/middleground_push/middleground_push_message_List'//中台推送信息列表
import middlePushLog from '@/components/middleground_push/middleground_push_log'//中台推送日志列表
//盘点
import disk_warehouse_task from '@/components/disk_warehouse/disk_warehouse_task'//安全车库盘点任务管理
import disk_warehouse_list from '@/components/disk_warehouse/disk_warehouse_list'//安全车库盘点
import disk_warehouse_details from '@/components/disk_warehouse/disk_warehouse_details'//单车盘点清单
import disk_warehouse_approval from '@/components/disk_warehouse/disk_warehouse_approval'//安全车库盘点--审批
import disk_warehouse_approvalDetails from '@/components/disk_warehouse/disk_warehouse_approvalDetails'//审批详情
Vue.use(Router)

export default new Router({
  routes: [
  	{
      path: '/localIndex',
      name:'localIndex',
      component: localIndex
    },
    {
      path: '/garageList',
      name:'garageList',
      component: garageList
    },
    {
      path: '/garageDetails',
      name:'garageDetails',
      component: garageDetails
    },
    {
      path: '/vehicleList',
      name:'vehicleList',
      component: vehicleList
    },
    {
      path: '/vehicleDetails',
      name:'vehicleDetails',
      component:vehicleDetails
    },
    {
      path: '/sgOutTasks_list',
      name:'sgOutTasks_list',
      component: sgOutTasks_list
    },
    {
      path: '/sgOutTasks_Details',
      name:'sgOutTasks_Details',
      component:sgOutTasks_Details
    },
    {
      path: '/sgInTasks_list',
      name:'sgInTasks_list',
      component: sgInTasks_list
    },
    {
      path: '/sgInTasks_Details',
      name:'sgInTasks_Details',
      component:sgInTasks_Details
    },
    {
      path: '/orderList',
      name:'orderList',
      component: orderList
    },
    {
      path: '/orderDetails',
      name:'orderDetails',
      component:orderDetails
    },
    {
      path: '/middlePushMessage',
      name:'middlePushMessage',
      component: middlePushMessage
    },
    {
      path: '/middlePushLog',
      name:'middlePushLog',
      component:middlePushLog
    },
    {
      path: '/disk_warehouse_task',
      name:'disk_warehouse_task',
      component:disk_warehouse_task
    },
    {
      path: '/disk_warehouse_list',
      name:'disk_warehouse_list',
      component:disk_warehouse_list
    },
    {
      path: '/disk_warehouse_details',
      name:'disk_warehouse_details',
      component:disk_warehouse_details
    },
    {
      path: '/disk_warehouse_approval',
      name:'disk_warehouse_approval',
      component:disk_warehouse_approval
    },
    {
      path: '/disk_warehouse_approvalDetails',
      name:'disk_warehouse_approvalDetails',
      component:disk_warehouse_approvalDetails
    },
    

  ]
})
