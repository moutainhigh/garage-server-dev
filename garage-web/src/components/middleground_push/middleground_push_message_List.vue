<template>
  <div class="info-bg">
    <!-- 查询条件等部分 -->
    <div class="bg-class search-class">
      <h3 class="itemTitle">
        <span></span>中台推送信息列表
      </h3>
      <Form ref="searchForm" :model="searchForm" :label-width="120">
        <Row>
          <Col span="8">
            <FormItem label="单号">
              <Input type="text" v-model="searchForm.billNum" placeholder="请输入..."></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="单子类型">
              <Select v-model="searchForm.type" placeholder="请选择">
                <Option
                  v-for="item in typeList"
                  :key="item.value"
                  :value="item.value"
                >{{item.name}}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="接口名称">
              <Select v-model="searchForm.interfaceName" placeholder="请选择">
                <Option
                  v-for="item in interfaceNameList"
                  :key="item.value"
                  :value="item.value"
                >{{item.name}}</Option>
              </Select>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="8">
            <FormItem label="发送状态">
              <Select v-model="searchForm.stat" placeholder="请选择">
                <Option
                  v-for="item in statList"
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
              <!-- <Button type="primary" @click="resetBtn">发送主体金额信息</Button> -->
              <Button @click="resetBtn">重置</Button>
             
            </FormItem>
          </Col>
        </Row>
      </Form>
    </div>
    <!-- 正文等部分 -->
    <div class="bg-class main-class" style="position: relative;">
      <!-- <div class="btnGroup">
        <Button @click="addCar" type="primary">新增车辆</Button>
      </div> -->
      <Table
        border
        :columns="tableCol"
        :data="tableData"
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
  name: "middlepushmsg",
  data() {
    return {
      changetitle: "新建",
      createCarModal: false, //车库模态窗
      typeList:[],//类型
      interfaceNameList:[],//接口名称
      statList:[{
        name:'未发送',
        value:0
      },
      {
        name:'已发送',
        value:1
      },
      {
        name:'发送失败',
        value:2
      }],//发送状态
      searchForm: {//查询
        billNum: "",
        type: "",
        interfaceName: "",
        stat: "",
      },
      total: 0,
      current: 1,
      rowCount: 10,
      tableCol: [
        {
          title: "单号",
          key: "billNum"
        },
        {
          title: "单子类型",
          key: "typeStr"
        },
        {
          title: "接口名称",
          key: "interfaceNameStr"
        },
        {
          title: "接口码值",
          key: "interfaceName"
        },
        {
          title: "发送次数",
          key: "sendCount"
        },
        {
          title: "发送状态",
          key: "stat",
          render:(h,params)=>{
							let text = '';
							if(params.row.stat==0){
								text='未发送';
							}
							else if(params.row.stat==1){
								text='已发送';
							}
							else if(params.row.stat==2){
								text = '发送失败';
							}
              return h('span',text)
          }
        },
        {
          title: "操作",
          key: "action",
          fixed: "right",
          width: 100,
          render: (h, params) => {
            return h("div", [              
               h(
                "a",
                {
                  style: {
                    marginRight: "5px",
                    //display:params.row.statStr !== '在库'?"none":"inline-block",
                  },
                  on: {
                    click: () => {
                        this.sendInfoTo(params.row);
                    }
                  }
                },
                "发送"
              ),
            ]);
          }
        }
      ],
      tableData: [],
      pageSizeOpts: [10, 20, 50, 100],
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
    searchBtn() {//查询
      this.current = 1;
      this.getData();
    },
    resetBtn() {//重置
      for (let i in this.searchForm) {      
          this.searchForm[i] = "";       
      }
    },
    onPageChange(page) {
      this.current = page;
      this.getData();
    },
    onPageSizeChange(size) {
      this.current = 1;
      this.rowCount = size;
      this.getData();
    },
    sendInfoTo(row){
        let self = this;
        let list={
          "billNum":row.billNum,
          "id":row.id,
          "type":row.type
        }
        this.$http
        .post("/garage/sgSendInfoToZhongtai/sendInfoToZT",list)
        .then(function(res) {
          if (res.data.success == true) {
            this.$Modal.success({
              title: "提示",
              content: res.data.data,
              onOk: () => {
                self.getData();
              }
            });
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    getData() {
      //获取列表数据
      let list = Object.assign({}, list, this.searchForm);
      list.current = this.current;
      list.rowCount = this.rowCount;
      this.$http
        .post("/garage/sgSendInfoToZhongtai/pageQuery", list)
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
    gettypeList() {
      //单子类型
      this.$http
        .post("/garage/enum/getEnumDataList?enumName=garage.zhongtai.BillStatusEnum")
        .then(function(res) {
          if (res.data.success == true) {
            this.typeList = res.data.data;
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    getinterfaceNameList() {
      //接口名
      this.$http
        .post("/garage/enum/getEnumDataList?enumName=garage.zhongtai.SendTypeEnum")
        .then(function(res) {
          if (res.data.success == true) {
            this.interfaceNameList = res.data.data;
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
    this.gettypeList();
    this.getinterfaceNameList();
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