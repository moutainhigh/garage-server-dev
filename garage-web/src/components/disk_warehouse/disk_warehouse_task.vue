<template>
  <div class="info-bg">
    <!-- 查询条件等部分 -->
    <div class="bg-class search-class">
      <h3 class="itemTitle">
        <span></span>盘点任务管理
      </h3>
      <Form ref="searchForm" :model="searchForm" :label-width="120">
        <Row>
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
      <div class="btnGroup">
        <Button @click="addGarage" type="primary">新建盘点任务</Button>
      </div>
      <Table
        border
        :columns="tableCol"
        :data="tableData"
        :height="tableHeight"
        :loading="repeatClick"
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
    <Modal
        v-model="createCarModal"
        :title="changetitle"       
        >
        <div>
            <Form ref="createFinaForm" :model="createFinaForm" :rules="createFinaRule" :label-width="120">
              <Row>
                 <FormItem label="创建时间" class="personMsg">
                  <Col span="10">
                   <FormItem prop="startTime">
                      <DatePicker
                        v-model="createFinaForm.startTime"                       
                        type="date"
                        placeholder="开始日期"
                        :disabled="type=='look'"  
                        :options="startOptions"                 
                      ></DatePicker>
                    </FormItem>
                  </Col>
                  <Col span="2">
                    <span style="line-height:30px;margin-left:10px;">至</span>
                  </Col>
                  <Col span="10">
                    <FormItem prop="endTime">
                      <DatePicker
                        v-model="createFinaForm.endTime"                       
                        type="date"
                        placeholder="结束日期"
                        :disabled="type=='look'"
                        :options="endOptions"
                       
                      ></DatePicker>
                    </FormItem>
                  </Col>
                </FormItem>
              </Row>
              <Row>
                 <FormItem label="盘点车库" prop="appoint" class="appoint">
                  <Col span="10">
                    <RadioGroup v-model="createFinaForm.appoint" @on-change="changePoint" style="height:50px;float:left;">
                      <Radio label="0" :disabled="type=='look'">所有使用中车库</Radio>
                      <Radio label="1" :disabled="type=='look'">指定车库</Radio>
                    </RadioGroup>
                  </Col>
                </FormItem>
              </Row>
              <Row style="height:138px;overflow-y:auto;" v-if="createFinaForm.garages">
                  <ul class="carBox">
                    <li v-for='(item,index) in createFinaForm.garages' :value="item.name">
                        <span>{{item.name}}</span>
                        <a v-if="type!='look'" @click="delItem(index)">X</a>
                    </li>
                  </ul>
              </Row>
            </Form>
        </div>
        <div style="text-align:right;margin-top:15px;">
            <Button  @click="createOk" type="primary" v-if="changetitle!='查看'">确定</Button>
            <Button  @click="createCancel">取消</Button>
        </div>
        <div slot="footer"></div>
    </Modal>
    <!-- 车库查询 -->
    <Modal
        v-model="searchCarModal"
        title="车库查询"
        @on-ok="searchCarOk"
        @on-cancel="searchCarCancel">
        <div>
            <Form ref="searchCarForm" :model="searchCarForm" :label-width="120">
             <Row>
                <Col span="18">
                  <FormItem label="车库名称">
                       <Input type="text" v-model="searchCarForm.garageName" placeholder="请输入..."></Input>
                  </FormItem>
                </Col>
              </Row>
              <!--查询、重置按钮-->
              <Row>
                <Col span="24" class="textCenter marginB6">
                  <FormItem :label-width="1">
                    <Button type="primary" @click="searchCarBtn">查询</Button>
                    <Button @click="resetCarBtn">重置</Button>
                  </FormItem>
                </Col>
              </Row>             
              <Row style="overflow-y: scroll;height: 200px;">
                  <Table
                    border
                    :columns="carTableCol"
                    :data="carTableData"
                    @on-selection-change="onSelection"
                  ></Table>
              </Row>
            </Form>
        </div>
    </Modal>
  </div>
</template>
<script>
export default {
  name: "task",
  data() {
    return {
      type:'add',
      repeatClick:false,
      showEdit:false,
      billNumList:[],
      selectData:[],
      changetitle: "新建盘点期数及时间",
      createCarModal: false, //任务管理模态窗
      searchCarModal:false,//点击指定车库弹窗
      provinceList: [], //省份
      cityList: [], //城市
      parkingStatusList:[],//车位状态
      garageStatusList: [], //车库状态
      operateAttrList:[],//经营属性
      searchForm: {
        //查询
        billNum: "",
      },
      total: 0,
      current: 1,
      rowCount: 10,
      tableCol: [
        {
          title: "盘点期数",
          key: "billNum",
          width: 180,
        },
        {
          title: "起始时间",
          key: "startTime"
        },
        {
          title: "截止时间",
          key: "endTime"
        },
        {
          title: "发布状态",
          key: "stateStr"
        },
        {
          title: "操作",
          key: "action",
          fixed: "right",
          width: 130,
          render: (h, params) => {
            return h("div", [
              h('a', {
              	style: {
              		marginRight: '5px',
                  display:params.row.state !==0?"none":"inline-block",
              	},
                on: {
                    click: () => {
                          this.type = 'edit';
                          this.changetitle = '编辑盘点任务及时间';
                          // this.createFinaForm.startTime =  params.row.startTime;
                          // this.createFinaForm.endTime =  params.row.endTime;
                          // this.createFinaForm.appoint =  params.row.appoint;
                          // this.createFinaForm.garages = params.row.garages;
                          // this.searchCarModal = false;
                          // this.createCarModal = !this.createCarModal;
                          this.$refs["createFinaForm"].resetFields();
                          this.viewForm(params.row.id);
                          this.createFinaForm.id = params.row.id;
                          this.createCarModal = !this.createCarModal

                      }
                    }
                }, '编辑'),
               h('a', {
                style: {
                  marginRight: '5px',
                  border:'none',
                  background:'none',
                  display:params.row.state !==0?"none":"inline-block",
                },
                on: {
                    click: (e) => {
                          this.type = 'fabu';
                          this.repeatClick = true;                        
                          this.checkData(params.row.id);
                      }
                    }
                }, '发布'),
              h(
                "a",
                {
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.type = 'look';
                      this.changetitle='查看';
                      this.$refs["createFinaForm"].resetFields();
                      this.viewForm(params.row.id);
                     
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
      tableHeight: 0,
      createFinaForm: {
        id:'',
        startTime:'',
        endTime:'',
        appoint:'',
        garages:[],
      },
      searchCarForm:{
        garageName:''
      },
      createFinaRule:{
        startTime: [
          {
            required: true,
            type: "date",
            message: "请选择开始日期",
            trigger: "change"
          }
        ],
        endTime: [
          {
            required: true,
            type: "date",
            message: "请选择结束日期",
            trigger: "change"
          }
        ],
        appoint: [
          {
            required: true,
            message: "请选择盘点车库",
            trigger: "change"
          }
        ],
      },
      carTableCol:[
        {
            type: 'selection',
            width: 60,
            align: 'center'
        },
        {
            title: "序号",
            type: 'index',
            align: 'center',
            width: 90,
        },
        {
          title: "车库名称",
          key: "garageName"
        },
        {
          title: "车库地址",
          key: "garageAddresss"
        }
      ],
      carTableData:[],
    }
  },
  components: {},
  beforeMount() {},
  computed: {
     startOptions(){
        return {
          disabledDate: (date) => {
            let a = date && date.valueOf() < Date.now()-86400000; //可以选择当天的日期
            return a;
          }
        };
      },
     endOptions() {
        return {
          disabledDate: (date) => {
            let a;
            let b;
            var userAgent = navigator.userAgent;
            a= date && date.valueOf() < Date.parse(this.createFinaForm.startTime)+ 86400000;
            return a;
          }
        };
      },
    },
  watch:{
    // ['createFinaForm.appoint'](val){
    //   if(this.createFinaForm.appoint == '1'&& this.type != 'look'){//指定车库
    //       this.searchCarModal = true;
    //       // this.createCarModal = false;
    //   }
    // }
  },
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
    // getStartTime(date){
    //   this.createFinaForm.startTime = date;
    // },
    // getEndTime(date){
    //   this.createFinaForm.endTime = date;
    // },
    changePoint(){
      if(this.createFinaForm.appoint == '1'&& this.type != 'look'){//指定车库          
          this.searchCarModal = !this.searchCarModal;
          // this.createCarModal = false;
      }else{
        this.selection = [];
        this.createFinaForm.garages = [];
        this.searchCarForm.garageName = '';
        this.getcarTableData();
      }
    },
    checkData(id) {
      //发布前检查
      let list={
        'id':id
      }
      let self = this;      
      this.$http
        .post("/garage/sgInventoryHome/checkData", list)
        .then(function(res) {
          if (res.data.success == true) {
            // this.$Modal.success({
            //   title: "提示",
            //   content: res.data.data,
            //   onOk: () => {
                
            //   }
            // });
            self.issue(id);
          } else {
            this.repeatClick = false;
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    issue(id){
        //发布前检查
      let list={
        'id':id
      }
      this.$http
        .post("/garage/sgInventoryHome/issue", list)
        .then(function(res) {
          if (res.data.success == true) {
            this.repeatClick = false;
            this.$Modal.success({
              title: "提示",
              content: res.data.data,
              onOk: () => {
                this.getData();
              }
            });
          } else {
            this.repeatClick = false;
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
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
    searchBtn() {
      this.current = 1;
      this.selectData = [];
      this.getData();
    },
    resetBtn() {
      for (let i in this.searchForm) {       
          this.searchForm[i] = [];        
      }
    },
    searchCarBtn(){
      this.getcarTableData();
    },
    resetCarBtn() {
      for (let i in this.searchCarForm) {       
          this.searchCarForm[i] = "";        
      }
    },
    searchCarOk(){
      this.searchCarModal = !this.searchCarCancel;
      this.createFinaForm.garages = [];
      //this.createCarModal = !this.createCarModal; 
      console.log(this.selectData,"data")
      for(let i in this.selectData){
        let list = {
          name:'',
          id:'',
        }
        list.name = this.selectData[i].garageName;
        list.id = this.selectData[i].id;
        this.createFinaForm.garages.push(list);
      }
      
    },
    searchCarCancel(){
      this.searchCarModal = !this.searchCarCancel;
    },
    addGarage() {
      this.type = 'add';
      this.changetitle='新建盘点任务';
      this.createFinaForm.startTime = ''; 
      this.createFinaForm.endTime = ''; 
      this.createFinaForm.appoint = '';
      this.createFinaForm.garages = []; 
      this.getcarTableData();
      this.createCarModal = !this.createCarModal;
      this.$refs["createFinaForm"].resetFields();
    },
    createOk(){//创建
      this.$refs["createFinaForm"].validate(valid => {
        if (valid) {
            let flag=null;           
            if(this.createFinaForm.appoint == 1 && this.createFinaForm.garages.length<1){
              flag=true;                  
            }   
            if(this.createFinaForm.startTime>this.createFinaForm.endTime){
                this.$Modal.warning({
                  title:'提示',
                  content: '起始时间应小于等于截止时间'
                })
                  return;
            }           
            if(flag){
              this.$Modal.warning({
              title:'提示',
              content: '请选择车库'
            })
              return;
            }
            if(this.type == 'add'){
              this.createForm();
            }else{
              this.updateForm();
            }
            
          }
      })
    },
    viewForm(id){  
        let list = {
          'id':id
        }  
        this.$Spin.show({
            render: (h) => {
                return h('div', [
                    h('Icon', {
                        'class': 'demo-spin-icon-load',
                        props: {
                            type: 'ios-loading',
                            size: 18
                        }
                    }),
                    h('div', 'Loading')
                ])
            }
        }); 
       // this.createFinaForm.garages = [];
        this.$http
          .post("/garage/sgInventoryHome/getHomeView", list)
          .then(function(res) {
            this.$Spin.hide();
            if (res.data.success == true) {              
              this.createFinaForm.startTime =  res.data.data.startTime;
              this.createFinaForm.endTime =  res.data.data.endTime;
              this.createFinaForm.appoint =  res.data.data.appoint;
              this.createFinaForm.garages = res.data.data.garages;
              this.createCarModal = true;
             
          } else {
            this.$Spin.hide();
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    createForm(){
      let list = Object.assign({}, list, this.createFinaForm);  
      list.startTime = this.dateToString(this.createFinaForm.startTime);
      list.endTime = this.dateToString(this.createFinaForm.endTime);         
        this.$http
          .post("/garage/sgInventoryHome/createInventory", list)
          .then(function(res) {
            if (res.data.success == true) {
            this.$Modal.success({
              title: "提示",
              content: '创建成功！',
              onOk: () => {
                this.getData();
                this.createCarModal = !this.createCarModal;
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
    updateForm(){
      let list = Object.assign({}, list, this.createFinaForm); 
      list.startTime = this.dateToString(this.createFinaForm.startTime);
      list.endTime = this.dateToString(this.createFinaForm.endTime);           
        this.$http
          .post("/garage/sgInventoryHome/updateInventory", list)
          .then(function(res) {
            if (res.data.success == true) {
            this.$Modal.success({
              title: "提示",
              content: '修改成功！',
              onOk: () => {
                this.getData();
                this.createCarModal = !this.createCarModal;
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
    createCancel(){
      this.createCarModal = false;
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
      list.current = this.current;
      list.rowCount = this.rowCount;
      this.$http
        .post("/garage/sgInventoryHome/pageQuery", list)
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
    onSelection(selection){
      this.selectData = selection;
    },
    delItem(index){
      this.createFinaForm.garages.splice(index,1);
    },
    getcarTableData(){
      //获取列表数据
      let list = Object.assign({}, list, this.searchCarForm);
      this.$http
        .post("/garage/sgInventoryHome/getGarageInfoList", list)
        .then(function(res) {
          if (res.data.success == true) {
            this.carTableData = res.data.data;
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
      //根据权限展示按钮
      this.$http
        .post("/garage/sgInventoryHome/getInventoryList")
        .then(function(res) {
          if (res.data.success == true) {
            this.billNumList = res.data.data;
            
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
    this.getcarTableData();
    this.getInventoryList();
  }
};
</script>
<style scoped lang="less">
.carBox{
  width:100%;
  clear: both;
  // overflow-y:scroll;
  li{
    border: 1px solid #ccc;
    width: 31%;
    padding: 8px;
    float:left;
    margin:4px;
    a{
      color:red;
      display: inline-block;
      font-size:14px;
    }
  }
}
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
.personMsg label::before{
    content: "*";
    display: inline-block;
    margin-right: 4px;
    line-height: 1;
    font-family: SimSun;
    font-size: 12px;
    color: #ed4014;
}
.appoint .ivu-form-item-error-tip{
  margin-top: 60px !important;
}
</style>