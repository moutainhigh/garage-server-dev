// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import App from './App';
import router from './router';
import VueResource from 'vue-resource';
import iView from 'iview';
import echarts from 'echarts'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

import 'iview/dist/styles/iview.css';
import './assets/css/style.css';
import './assets/css/ztree.css';
import './assets/css/zTreeStyle.css'
import './assets/css/public.less';
import './assets/css/el.css';

Vue.prototype.$echarts = echarts;
Vue.prototype.$VVue = Vue;
//全局的请求方法
import globalFunction from "./assets/js/globalFunction.js"
Vue.prototype.$globalFunction = globalFunction;

Vue.config.productionTip = false;
Vue.use(iView);
Vue.use(ElementUI);
Vue.use(VueResource);

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
