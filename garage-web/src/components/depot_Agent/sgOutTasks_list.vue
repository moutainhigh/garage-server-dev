<template>
  <div class="info-bg">
    <!-- 查询条件等部分 -->
    <div class="bg-class search-class">
      <h3 class="itemTitle">
        <span></span>出库单
      </h3>
      <Form ref="searchForm" :model="searchForm" :label-width="120">
        <Row>
          <Col span="8">
            <FormItem label="任务单号">
              <Input type="text" v-model="searchForm.orderTaskNum" placeholder="请输入..."></Input>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="创建时间" prop="projType">
              <Col span="10">
                <DatePicker
                  v-model="searchForm.createTimeBegin"
                  @on-change="getCreateTimeBegin"
                  type="date"
                  placeholder="开始日期"
                ></DatePicker>
              </Col>
              <Col span="2">
                <span style="line-height:30px;margin-left:10px;">至</span>
              </Col>
              <Col span="10">
                <DatePicker
                  v-model="searchForm.createTimeEnd"
                  @on-change="getCreateTimeEnd"
                  type="date"
                  placeholder="结束日期"
                ></DatePicker>
              </Col>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="8">
            <FormItem label="alix申请编号">
              <Input type="text" v-model="searchForm.alixNum" placeholder="请输入..."></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="出库单号">
              <Input type="text" v-model="searchForm.taskNum" placeholder="请输入..."></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="车架号">
              <Input type="text" v-model="searchForm.vin" placeholder="请输入..."></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="审批状态">
              <Select v-model="searchForm.billStatus" placeholder="请选择">
                <Option
                  v-for="item in billStatusList"
                   :key="item.value"
                  :value="item.value"
                >{{item.name}}</Option>
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
      <Table
        border
        :columns="tableCol"
        :data="tableData"
        @on-selection-change="onSelection"
        :height="tableHeight"
        class="repaymentPlanT"
      ></Table>
      <Page
        :total="total"
        @on-page-size-change="onPageSizeChange"
        @on-change="onPageChange"
        :page-size-opts="pageSizeOpts"
        show-sizer
        show-elevator
        show-total
        class="marginT10"
      />
    </div>
  </div>
</template>
<script>
export default {
  name: "paymentpool",
  data() {
    return {
      showBtn:false,
      createCarModal: false, //车库模态窗
      billStatusList: [], //审批状态
      searchForm: {
        //查询
        orderTaskNum: "",
        createTimeBegin: "",
        createTimeEnd: "",
        alixNum: "",
        taskNum: "",
        vin: "",
        billStatus: "",
      },
      total: 0,
      current: 1,
      rowCount: 10,
      tableCol: [
        {
          title: "任务单号",
          key: "orderTaskNum"
        },
        {
          title: "出库单号",
          key: "taskNum"
        },
        {
          title: "车架号",
          key: "vin"
        },
        {
          title: "alix申请编号",
          key: "alixNum"
        },
        {
          title: "创建时间",
          key: "createTime"
        },
        {
          title: "实际出库日期",
          key: "actualStartTime"
        },
        {
          title: "审批状态",
          key: "billStatusStr"
        },
        {
          title: "操作",
          key: "action",
          fixed: "right",
          width: 180,
          render: (h, params) => {
            return h("div", [
              h(
                "a",
                {
                  style: {
                    marginRight: "5px",
                    display: params.row.billStatus!==0 && params.row.billStatus!==1||!this.showBtn? "none" : "inline-block"
                  },
                  on: {
                    click: () => {
                      this.$router.push(
                        "/sgOutTasks_Details?id=" + params.row.id + "&type=edit"
                      );
                    }
                  }
                },
                "处理"
              ),
              h(
                "a",
                {
                  style: {
                    marginRight: "5px",
                    display: params.row.billStatus!==5||!this.showBtn? "none" : "inline-block"
                  },
                  on: {
                    click: () => {
                      this.$router.push(
                        "/sgOutTasks_Details?id=" + params.row.id + "&type=approve"
                      );
                    }
                  }
                },
                "审批"
              ),
              h(
                "a",
                {
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.$router.push(
                        "/sgOutTasks_Details?id=" + params.row.id + "&type=view"
                      );
                    }
                  }
                },
                "查看"
              ),
              
            ]);
          }
        }
      ],
      tableData: [],
      pageSizeOpts: [10, 20, 50, 100],
      selectData: [],
      tableHeight: 0
    };
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
    getCreateTimeBegin(date){
      this.searchForm.createTimeBegin = date;
    },
    getCreateTimeEnd(date){
      this.searchForm.createTimeEnd = date;
    },
    lsckCancel(){
      this.lsckModal = false;
    },
    showLsckModal(){//临时出库弹窗
      this.lsckModal = true;
    },
    searchBtn() {
      this.current = 1;
      this.selectData = [];
      this.getData();
    },
    resetBtn() {
      for (let i in this.searchForm) {
        this.searchForm[i] = "";
      }
    },
    createCarOk() {
      //保存新建
    },
    createCarCancel() {
      //取消新建
    },
    onPageChange(page) {
      this.current = page;
      this.selectData = [];
      this.getData();
    },
    onPageSizeChange(size) {
      this.current = 1;
      this.rowCount = size;
      this.selectData = [];
      this.getData();
    },
    onSelection(selection) {
      this.selectData = selection;
    },
    getData() {
      //获取列表数据
      let list = Object.assign({}, list, this.searchForm);
      list.current = this.current;
      list.rowCount = this.rowCount;
      this.$http
        .post("/garage/sgGarageOrder/pageQuery", list)
        .then(function(res) {
          console.log(res.data);
          if (res.data.success == true) {
            this.tableData = res.data.data.records;
            this.total = res.data.data.total;
            this.current = res.data.data.current;
            this.rowCount = res.data.data.size;
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    getAuthorize() {
      //根据权限展示按钮
      this.$http
        .post("/garage/hasRole/getAuthorize")
        .then(function(res) {
          if (res.data.success == true) {
            if(res.data.data == 'garageManageRole'){
                this.showBtn = true;
            }else{
                this.showBtn = false;
            } 
            
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    getbillStatusList() {
      //审批状态
      this.$http
        .post("/garage/enum/getEnumDataList?enumName=garage.SgAllocateTaskStatusEnum")
        .then(function(res) {
          if (res.data.success == true) {
            this.billStatusList = res.data.data;
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
  },
  mounted() {
    this.searchBtn();
    this.getbillStatusList();//审批状态
    this.getAuthorize();
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