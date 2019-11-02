<template>
  <div class="info-bg">
    <!-- 查询条件等部分 -->
    <div class="bg-class search-class">
      <h3 class="itemTitle">
        <span></span>入库单
      </h3>
      <Form ref="searchForm" :model="searchForm" :label-width="120">
        <Row>
          <Col span="8">
            <FormItem label="任务单号">
              <Input type="text" v-model="searchForm.orderTaskNum" placeholder="请输入..."></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="alix申请编号">
              <Input type="text" v-model="searchForm.alixNum" placeholder="请输入..."></Input>
            </FormItem>
          </Col>
          <Col span="8">
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
            <FormItem label="入库车库名称">
              <Select v-model="searchForm.sgGarageInfoId" placeholder="请选择">
                <Option
                  v-for="item in sgGarageNameList"
                  :key="item.id"
                  :value="item.id"
                >{{item.garageName}}</Option>
              </Select>
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
      <div class="btnGroup" v-if="this.showEdit">
        <Button @click="shortGarage" type="primary">临时出库</Button>
      </div>
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
     <!-- 新建车库 -->
    <Modal
      v-model="lsckModal"
      :title="changetitle"
      @on-ok="lsckOk"
      @on-cancel="lsckCancel"
      width="650"
      :mask-closable="false"
    >
      <Form ref="createFinaForm" :model="createFinaForm" :rules="createFinaRule" :label-width="120">
        <Row>
           <Col span="24"> 
            <FormItem label="申请编号">
              <Input type="text" v-model="alixNum" placeholder="请输入..."  style="width:175px!important;"></Input>
            </FormItem>
          </Col>
          <Col  span="24" class="marginB6">
            <Table
              border
              :columns="carCol"
              :data="carData"
              class="repaymentPlanT"
            ></Table>
            <Page
              :total="carTotal"
              @on-page-size-change="carOnPageSizeChange"
              @on-change="carOnPageChange"
              :page-size-opts="carPageSizeOpts"
              show-sizer
              show-elevator
              show-total
              class="marginT10"
            />
          </Col>         
          <Col span="24" class="textCenter" style="margin:14px 0;">
            <FormItem :label-width="1">
              <Button type="primary" @click="searchCarBtn">查询</Button>
              <Button @click="resetCarBtn">重置</Button>
            </FormItem>
          </Col>         
          <Col span="12"> 
            <FormItem label="车架号：">
              <Input type="text" v-model="createFinaForm.vin" disabled style="width:175px!important;"></Input>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="临时出库时间：" prop="tempActualTime">
              <DatePicker v-model="createFinaForm.tempActualTime" type="date" placeholder=""  style="width:175px!important;"></DatePicker>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="临时入库时间：" prop="tempPredictTime">
              <DatePicker v-model="createFinaForm.tempPredictTime" type="date" placeholder=""  style="width:175px!important;"></DatePicker>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="出库原因；" prop="outReason">
              <Select
                v-model="createFinaForm.outReason"
                :label-in-value="true"
                placeholder="请选择"
                style="width:175px!important;"
              >
                <Option
                  v-for="item in enumDataList"
                  :key="item.value"
                  :value="item.value"
                >{{item.name}}</Option>
              </Select>
            </FormItem>
          </Col>
        </Row>
      </Form>
      <div slot="footer" class="textCenter marginB6">
        <Button size="large" @click="lsckOk" type="primary">保存</Button>
        <Button size="large" @click="lsckCancel">取消</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
export default {
  name: "paymentpool",
  data() {
    return {
      showEdit:false,
      changetitle: "车辆信息",
      lsckModal: false,     
      createCarModal: false, //车库模态窗
      billStatusList: [], //审批状态
      sgGarageNameList:[],//入库
      enumDataList:[],
      alixNum:'',//申请编号
      searchForm: {
        //查询
        orderTaskNum: "",
        sgGarageInfoId:'',
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
          title: "入库单号",
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
          title: "实际入库时间",
          key: "actualEndTime"
        },
        {
          title: "入库车库名称",
          key: "garageName"
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
              // h(
              //   "a",
              //   {
              //     style: {
              //       marginRight: "5px",
              //       display: params.row.billStatus!==0 && params.row.billStatus!==3 ? "none" : "inline-block"
              //     },
              //     on: {
              //       click: () => {
              //         this.$router.push(
              //           "/sgInTasks_Details?id=" + params.row.id + "&type=edit"
              //         );
              //       }
              //     }
              //   },
              //   "处理"
              // ),
               h(
                "a",
                {
                  style: {
                    marginRight: "5px",
                     display: params.row.billStatus!==3||!this.showEdit? "none" : "inline-block"
                  },
                  on: {
                    click: () => {
                      this.$router.push(
                        "/sgInTasks_Details?id=" + params.row.id + "&type=approve&sgGaragOrderId="+params.row.sgGaragOrderId
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
                        "/sgInTasks_Details?id=" + params.row.id + "&type=view&sgGaragOrderId="+params.row.sgGaragOrderId
                      );
                    }
                  }
                },
                "查看"
              ),
              // h(
              //   "a",
              //   {
              //     style: {
              //       marginRight: "5px",
              //       display: (params.row.billStatus==4 && params.row.orderStatus==4) || (params.row.billStatus==4 &&params.row.orderStatus==7)? "inline-block" : "none"
              //     },
              //     on: {
              //       click: () => {
              //         this.lsckModal = true;
              //         this.createFinaForm.sgGaragOrderId = params.row.sgGaragOrderId;
              //         this.createFinaForm.vin = params.row.vin;
              //         // this.showLsckModal(params.row);
              //       }
              //     }
              //   },
              //   "临时出库"
              // )
            ]);
          }
        }
      ],
      tableData: [],
      createFinaForm:{
        sgVehicleId:'',
        vin:'',
        alixNum:'',
        tempActualTime:'',
        tempPredictTime:'',
        outReason:''
      },
      createFinaRule:{
        tempActualTime: [
          {
            required: true,
            message: "请选择临时出库时间",
            type:'date',
            trigger: "change"
          }
        ],
        tempPredictTime: [
          {
            required: true,
            message: "请选择临时入库时间",
            type:'date',
            trigger: "change"
          }
        ],
        outReason: [
          {
            required: true,
            message: "请选择出库原因",
            type:'number',
            trigger: "change"
          }
        ],
      },
      pageSizeOpts: [10, 20, 50, 100],
      selectData: [],
      tableHeight: 0,
      carCol:[ 
         {
              title: '选中',
              align:'center',
              key: 'checkBox',
              width: 60,
              render:(h,params)=>{
                  return h('div',[
                      h('Checkbox',{
                          props:{
                              value:params.row.checkBox
                          },
                          on:{
                              'on-change':(e)=>{
                                  // if(!this.alixNum){
                                  //   this.alixNum = params.row.alixNum;
                                  // }  
                                  this.createFinaForm.alixNum = params.row.alixNum;                              
                                  this.createFinaForm.sgVehicleId = params.row.id;
                                  this.createFinaForm.vin = params.row.vin;
                                  this.carData.forEach((items)=>{      //先取消所有对象的勾选，checkBox设置为false
                                      this.$set(items,'checkBox',false)
                                  });
                                  this.carData[params.index].checkBox = e;  //再将勾选的对象的checkBox设置为true
                              }
                          }
                      })
                  ])
              }
          },
         {
          title: "车架号",
          key: "vin",
          width: 170
        },
        {
          title: "申请编号",
          key: "alixNum",
          width: 150
        },
        {
          title: "车牌号",
          key: "licNum",
          width: 120
        },
        {
          title: "车型",
          key: "vehicleClassStr",
          width: 120
        },
        {
          title: "车款",
          key: "modelStr",
          width: 120
        },
        {
          title: "颜色",
          key: "color",
          width: 120
        },
        {
          title: "所在车库",
          key: "sgGarageName",
          width: 120
        },
        {
          title: "里程",
          key: "mileage",
          width: 120
        }],
      carData:[],
      carTotal: 0,
      carCurrent: 1,
      carRowCount: 10,
      carPageSizeOpts: [10, 20, 50, 100],
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
    dateToString(date){ 
      var year = date.getFullYear(); 
      var month =(date.getMonth() + 1).toString(); 
      var day = (date.getDate()).toString();  
      if (month.length == 1) { 
          month = "0" + month; 
      } 
      if (day.length == 1) { 
          day = "0" + day; 
      }
      var dateTime = year + "-" + month + "-" + day;
      return dateTime; 
    },
    shortGarage(){//临时出库
      this.lsckModal = true;
      this.alixNum = '';
      this.getCarData();
    },
    searchCarBtn() {//临时出库
      this.carCurrent = 1;      
      this.getCarData();
    },
    getCarData() {//获取列表数据----临时出库
      let list = {
        "sign":1,
        "alixNum":this.alixNum,
        "current":this.carCurrent,
        "rowCount":this.carRowCount
      }
      this.$http
        .post("/garage/sgVehicleInfo/pageQuery", list)
        .then(function(res) {
          if (res.data.success == true) {
            this.carData = res.data.data.records;
            this.carTotal = res.data.data.total;
            this.carCurrent = res.data.data.current;
            this.carRowCount = res.data.data.size;
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    resetCarBtn() {//临时出库
      this.alixNum = '';
    },
    carOnPageChange(page) {//临时出库
      this.carCurrent = page;
      this.getCarData();
    },
    carOnPageSizeChange(size) {//临时出库
      this.carCurrent = 1;
      this.carRowCount = size;
      this.getCarData();
    },
    onSelection(selection) {//选择车架号
      this.selectData = selection;
      console.log(selection,"selection")
      this.createFinaForm.alixNum = selection[0].alixNum;
      this.createFinaForm.sgVehicleId = selection[0].id;
      this.createFinaForm.vin = selection[0].vin;
    },
    lsckOk(){
       this.$refs["createFinaForm"].validate(valid => {
          if (valid) {
              let list = Object.assign({}, list, this.createFinaForm);
              list.tempActualTime= this.dateToString(this.createFinaForm.tempActualTime) 
              list.tempPredictTime= this.dateToString(this.createFinaForm.tempPredictTime) 
              if(!list.vin){
                  this.$Modal.warning({
                    title: "提示",
                    content:'请选择车架号',
                    onOk: () => {}
                  });  
              }else{
                this.$http
                  .post("/garage/sgGarageOrder/createTempOut", list)
                  .then(function(res) {
                    if (res.data.success == true) {
                      this.$Modal.success({
                        title: "提示",
                        content: res.data.data,
                        onOk: () => {
                          this.lsckModal = false;
                          this.getData();
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
              }              
          } 
       })    
    },
    lsckCancel(){
      this.lsckModal = false;
      for (let i in this.createFinaForm) {
        this.createFinaForm[i] = "";
      }
      this.$refs["createFinaForm"].resetFields();
    },
    searchBtn() {
      this.current = 1;
      this.getData();
    },
    resetBtn() {
      for (let i in this.searchForm) {
        this.searchForm[i] = "";
      }
      this.searchForm.sign = '0';
    },
    addCar() {
      //新增车辆--模态窗
      this.$router.push("/vehicleDetails?type=add");
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
    getData() {
      //获取列表数据
      let list = Object.assign({}, list, this.searchForm);
      list.sign=0;
      list.current = this.current;
      list.rowCount = this.rowCount;
      this.$http
        .post("/garage/sgGarageDetail/pageQueryRKOrder", list)
        .then(function(res) {
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
    getSgGarageNameListFn() {
      //所在车库下拉
      this.$http
        .post("/garage/sgGarageInfo/getGarageInfoList")
        .then(function(res) {
          if (res.data.success == true) {
            this.sgGarageNameList = res.data.data;
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    getEnumDataList() {
      //原因下拉
      this.$http
        .post("/garage/enum/getEnumDataList?enumName=garage.OutReasonEnum")
        .then(function(res) {
          if (res.data.success == true) {
            this.enumDataList = res.data.data;
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
                this.showEdit = true;
            }else{
                this.showEdit = false;
            } 
            
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
    this.searchBtn();
    this.getbillStatusList();
    this.getSgGarageNameListFn();
    this.getEnumDataList();
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