<template>
  <div class="info-bg">
    <!-- 查询条件等部分 -->
    <div class="bg-class search-class">
      <h3 class="itemTitle">
        <span></span>车库盘点
      </h3>
      <Form ref="searchForm" :model="searchForm" :label-width="120">
        <Row>
          <Col span="8">
            <FormItem label="盘点状态">
              <Select v-model="searchForm.approvalState" placeholder="请选择">
                <Option
                  v-for="item in billStateList"
                  :key="item.value"
                  :value="item.value"
                >{{item.name}}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="盘点期数">
                <Select v-model="searchForm.billNum" clearable placeholder="请选择">
                  <Option
                    v-for="item in billNumList"                    
                    :key="item"
                    :value="item"
                  >{{item}}</Option>
                </Select> 
            </FormItem>
          </Col>
        </Row>
        <!--查询、重置按钮-->
        <Row>
          <Col span="24" class="textCenter marginB6">
            <FormItem :label-width="1">
              <Button type="primary" @click="searchBtn">查询</Button>
              <Button @click="resetBtn">重置</Button>
            </FormItem>
          </Col>
        </Row>
      </Form>
    </div>
    <!-- 正文等部分 -->
    <div class="bg-class main-class" style="position: relative;">
      <div class="btnGroup" v-if="this.showEdit">
        <Button @click="addGarage" type="primary">新建车库</Button>
      </div>
      <Table
        border
        :columns="tableCol"
        :data="tableData"
        :height="tableHeight"
        class="repaymentPlanT"
      ></Table>
    </div>
  </div>
</template>
<script>
export default {
  name: "diskWareHoseList",
  data() {
    return {
      nowDate:'',
      showEdit:false,
      billNumList:[],
      billStateList:[],
      searchForm: {
        //查询
        approvalState: "",
        billNum: "",
      },
      tableCol: [
        {
          title: "盘点期数",
          key: "billNum",
          width: 180,
        },
        {
          title: "车库名称",
          key: "garageInfoName"
        },
        {
          title: "车库地址",
          key: "garageAddresss"
        },
        {
          title: "盘点状态",
          key: "approvalStateStr"
        },
        {
          title: "库管姓名",
          key: "contact"
        },
        {
          title: "最终完成时间",
          key: "finalizedTime"
        },
        {
          title: "操作",
          key: "action",
          fixed: "right",
          width: 100,
          render: (h, params) => {
            return h("div", [
              h('a', {
              	style: {
              		marginRight: '5px',
                  display:params.row.approvalState ==0||params.row.approvalState ==3?"none":"inline-block",
              	},
                on: {
                    click: () => {
                          if(params.row.endTime<=this.nowDate){
                             this.$Modal.warning({
                              title: "超时警告",
                              content: '您当前操作的盘库任务【'+params.row.billNum+'】已超时，无法操作提交',
                              onOk: () => {}
                            });
                          }else{
                            this.$router.push(
                              "/disk_warehouse_details?sgBillId=" +
                                params.row.id +
                                "&type=edit"
                            );
                          }
                          
                      }
                    }
                }, '盘点'),
              h(
                "a",
                {
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.$router.push(
                        "/disk_warehouse_details?sgBillId=" +
                          params.row.id +
                          "&type=view"
                      );
                    }
                  }
                },
                "查看"
              )
            ]);
          }
        }
      ],
      tableData: [],
      pageSizeOpts: [10, 20, 50, 100],
      selectData: [],
      tableHeight: 0,
    }
  },
  components: {},
  beforeMount() {},
  filters: {
    NumFormat: function(value) {
      var intPartFormat;
      if (!value) return 0;
      if (String(value).indexOf(".") == -1) {
        intPartFormat = value.toString().replace(/(\d)(?=(?:\d{3})+$)/g, "$1,"); // 将整数部分逢三一断
      } else {
        var a = value.toString();
        var b = a.split(".");
        var x = b[0];
        var y = b[1];
        var z = x.toString().replace(/(\d)(?=(?:\d{3})+$)/g, "$1,"); // 将整数部分逢三一断
        intPartFormat = z + "." + y;
      }
      return intPartFormat;
    }
  },
  methods: {
    searchBtn() {
      this.getData();
    },
    resetBtn() {
      for (let i in this.searchForm) {       
          this.searchForm[i] = '';        
      }
      this.searchForm.billNum = this.billNumList[0];
    },
    getData() {
      //获取列表数据
      let list = Object.assign({}, list, this.searchForm);
      console.log(list,"list")
      this.$http
        .post("/garage/sgInventoryBill/pageQueryBill", list)
        .then(function(res) {
          console.log(res.data);
          if (res.data.success == true) {
            this.tableData = res.data.data;
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    getBillState() {
      //盘点状态
      this.$http
        .post("/garage/enum/getEnumDataList?enumName=garage.inventory.InventoryBillStateEnum")
        .then(function(res) {
          if (res.data.success == true) {
            this.billStateList = res.data.data;
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    getInventoryList() {
      this.$http
        .post("/garage/sgInventoryHome/getInventoryList")
        .then(function(res) {
          if (res.data.success == true) {
            this.billNumList = res.data.data;
            this.searchForm.billNum = this.billNumList[0];
            
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    }
  },
  mounted() {    
    this.getBillState();
    this.getInventoryList();
    var date=new Date();
    var year=date.getFullYear();
    var month=date.getMonth()+1;
    var day = date.getDate();
    month=month>9?month.toString():'0' + month;
    this.nowDate=year+'-'+month+'-'+day;
    setTimeout(() => {
      this.searchBtn();
    },400)
  }
};
</script>
<style scoped lang="less">
.sumUl {
  background: white;
  height: 30px;
  li {
    display: inline-block;
    line-height: 40px;
  }
}
.contentTitle {
  height: 40px;
  background: #f7f7f8;
  position: relative;
  span {
    position: absolute;
    text-align: center;
    font-weight: bold;
    left: 10px;
    line-height: 40px;
  }
  h2 {
    text-align: center;
  }
}
.titleBorder {
  border-top: 1px solid #dcdee2;
  border-left: 1px solid #dcdee2;
  border-right: 1px solid #dcdee2;
}
.positionRe {
  position: relative;
}
.btnGroup {
  text-align: right;
  margin-bottom: 10px;
  button {
    margin-left: 5px;
  }
}
.spanText {
  top: 37px;
  font-weight: bold;
}
.ivu-date-picker {
  width: 100%;
}
</style>
<style>
.ivu-table td.textRightt {
  text-align: right !important;
}
.redre label::before {
  content: "*";
  display: inline-block;
  margin-right: 4px;
  line-height: 1;
  font-family: SimSun;
  font-size: 12px;
  color: #f30;
}
.stopCarMon {
  display: inline-block;
  width: 25%;
}
.stopCarMon .ivu-form-item-label {
  width: 50px !important;
}
</style>