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
import 'ztree';
export default {
	twoWay: true,
	deep:true,
	update: function (options) {
		var _this = this;
		var setting = options.ztree;
		var init = function(){
			_this.vm.$http.post(options.initUrl,options.initObj)
				.then(function (response) {
					$.fn.zTree.init($(_this.el), setting, response.body.data);
					_this.data = response.body.data;
                    _this.vm.$emit('getAllCheckData', _this.data);
					if (setting.check) {
						setChecked();
					}
				});
		};
		
		var onCheck = function(){
			
			var zTree = $.fn.zTree.getZTreeObj(_this.el.id);
			var nodes = zTree.getCheckedNodes(true,true);

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
			var zTree = $.fn.zTree.getZTreeObj(_this.el.id);
			var nodes = zTree.getSelectedNodes();
			var v = "";
			var d = "";
			nodes.sort(function compare(a,b){return a.id-b.id;});
			for (var i=0, l=nodes.length; i<l; i++) {
				v = nodes[i].name + ",";
				d = nodes[i].id + ",";
			}
			if (v.length > 0 ) v = v.substring(0, v.length-1);
			if (d.length > 0 ) d = d.substring(0, d.length-1);
			
			var opt = options;
			
			if (opt.name !== undefined) {
				opt.name = v;
			}
			opt.value = d;
			
			_this.set(options);
		}
		
		var setChecked = function(){
			var data = _this.data;
			var zTree = $.fn.zTree.getZTreeObj(_this.el.id);
            var checkArr = options.value.split(',');
            if(data != null){
            	for (var n = 0; n < data.length; n++) {//循环所有节点
                var checkS = false;
                var tNode = data[n];//当前节点

                for (var i = checkArr.length - 1; i >= 0; i--) {
                    if (checkArr[i] == tNode.id) {
                        var node = zTree.getNodeByParam("id", tNode.id);
		                zTree.selectNode(node);
		                zTree.checkNode(node, true, false);//默认给选中状态
						break;
                    }
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
			this.unbind();
			$.fn.zTree.init($(_this.el), setting, _this.data);
			if (setting.check) {
				setChecked();
			}
		}
	},
	
	unbind: function () {
		$.fn.zTree.destroy(this.el.id);
	}
}




