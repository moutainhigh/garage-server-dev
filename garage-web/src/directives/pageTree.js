
/*

treeSetting: {
	ztree: {
		data: {
			simpleData: {
				enable: true
			}
		},
		check: {
			enable: true,
			chkStyle: "checkbox",
			chkboxType: { "Y": "ps", "N": "ps" }
		}
	},
	temp: {
		name: '',
		value: ''
	},
	name: '',
	value: '',
	initUrl: '/nssp-web/sspOrganizationAccountManage/getDataPermisionDepartmentsRule.do'
}
 
*/

import 'assets/js/ztree';

export default {
	twoWay: true,
	deep:true,

	update: function (options,oldval) {
		//console.log(options);
		//console.log(oldval);
		//debugger
		//if(oldval==undefined||(oldval.initObj.system_id!=options.initObj.system_id)){
		var _this = this;
		var setting = options.ztree;
		var callAjax = function(url,o){
			_this.vm.$http.post(url,o)
						.then(function (response) {
							$.fn.zTree.init($(_this.el), setting, response.body.data);
							_this.data = response.body.data;
							if (setting.check) {
								setChecked();
							}
						});
		}
		var init = function(){
			if(!!options.initUrl){
				var o = {};
				if(!!options.initObj){
					if(!!options.initObj.systemId){
						if(options.initObj.systemId != ''){
							o.systemId = options.initObj.systemId;
							callAjax(options.initUrl,o);
						}
					}else if(!!options.initObj.system_id){
						if(options.initObj.system_id != ''){
							o.system_id = options.initObj.system_id;
							callAjax(options.initUrl,o);
						}								
					}
				}
			}
			return;
		};
		init();
		var onCheck = function(){
			var zTree = $.fn.zTree.getZTreeObj(_this.el.id);
			var nodes = zTree.getCheckedNodes(true);
			var opt = options;
			var isCheckbox = opt.ztree && opt.ztree.check && opt.ztree.check.chkStyle === 'checkbox';
			
			if (opt.temp) opt = opt.temp;
			
			if (isCheckbox) {
				opt.value = nodes.map(function (v) {
					return v.id;
				}).join();
				
				if (opt.name !== undefined) {
					opt.name = nodes.map(function (v) {
						return v.name;
					}).join();
				}
			} else {
				opt.value = nodes[nodes.length - 1].id;
				
				if (opt.name !== undefined) {
					opt.name = nodes[nodes.length - 1].name;
				}
			}
			
			_this.set(options);
		};
		
		var onClick = function (e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj(_this.el.id),
					nodes = zTree.getSelectedNodes(),
					v = "";
			var d = "";
			var le;
			nodes.sort(function compare(a,b){return a.id-b.id;});
			for (var i=0, l=nodes.length; i<l; i++) {
				v = nodes[i].name + ",";
				d = nodes[i].id + ",";
                le = nodes[i].level;
			}
			if (v.length > 0 ) v = v.substring(0, v.length-1);
			if (d.length > 0 ) d = d.substring(0, d.length-1);

            if (le.length > 0 ) le = le.substring(0, d.length-1);
			var opt = options;
			if (opt.name !== undefined) {
				opt.name = v;
			}
			opt.value = d;
			opt.level = le;
			_this.set(options);
		}
		
		var setChecked = function(){
			var data = _this.data;
			var zTree = $.fn.zTree.getZTreeObj(_this.el.id);
            var checkArr = options.value.split(',');
            for (var n = 0; n < data.length; n++) {//??????????????????
                var checkS = false;
                var tNode = data[n];//????????????

                for (var i = checkArr.length - 1; i >= 0; i--) {
                    if (checkArr[i] == tNode.id) {
                        var node = zTree.getNodeByParam("id", tNode.id);
		                zTree.selectNode(node);
		                zTree.checkNode(node, true, true);//?????????????????????
						break;
                    }
                }
            }
		};
		
		if (setting.check) {
			setting.callback = {
				onCheck: onCheck
			};
		} else {
			setting.callback = {
				onClick: onClick
			};
		}
        
		if (!_this.data) {
			init();
		} else {
			 _this.data = null;
			$.fn.zTree.init($(_this.el), setting, _this.data);
			if (setting.check) {
				setChecked();
			}
		}
		//}
	},
	
	unbind: function () {
		$.fn.zTree.destroy(this.el.id);
	}
}




