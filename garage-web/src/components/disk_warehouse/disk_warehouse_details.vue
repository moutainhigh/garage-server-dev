<template>
  <div class="info-bg">
    <!-- 查询条件等部分 -->
    <div class="bg-class search-class">
      <h3 class="itemTitle">
        <span></span>单库盘点清单
      </h3>
      <Form ref="searchForm" :model="searchForm" :label-width="120">
        <Row>
          <Col span="8">
            <FormItem label="车架号">
               <Input type="text" v-model="searchForm.vin" placeholder="请输入..."></Input>                 
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="是否盘点">
                <Select v-model="searchForm.inventory" clearable placeholder="请选择">
                  <Option
                    v-for="item in inventoryList"                    
                    :key="item.value"
                    :value="item.value"
                  >{{item.name}}</Option>
                </Select> 
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="审批状态">
                <Select v-model="searchForm.approvalStatus" clearable placeholder="请选择">
                  <Option
                    v-for="item in approvalStatusList"                    
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
      <div style="font-size:14px;">
        <span>清单数量：<a style="color:#000;font-weight: bold;">{{listNum}}</a></span>
        <span>实际盘点数量：<a style="color:#000;font-weight: bold;">{{actualNum}}</a></span>
      </div>
      <div>
        <Table
          border
          :columns="tableCol"
          :data="tableData"
          :height="tableHeight"
          class="repaymentPlanT"
          :row-class-name="rowClassName"
        ></Table>
      </div>
       <h3 class="itemTitle" style="padding-left:0;">
        <span></span>其他在库车辆
      </h3>       
      <div style="margin-top:20px;"> 
        <div class="btnGroup" v-if="this.$route.query.type =='edit'">
          <Button @click="addGarage" type="primary">新增行</Button>
        </div>
        <Table 
          border
          :columns="addTableCol"
          :data="addTableData"
          class="repaymentPlanT"
        ></Table>
        <div style="margin-top:20px;text-align:center;">
          <Button type="primary" @click="submitBtn" v-if="$route.query.type=='edit'">提交</Button>
          <Button @click="returnBtn">返回</Button>
        </div>
      </div> 
    </div>       
    <Modal
        v-model="createCarModal"
        :title="changetitle"
        width="660"
        >
        <div>
            <Form ref="createFinaForm" :model="createFinaForm" :label-width="120">
              <Row>
                <Col span="12">
                  <FormItem label="车架号：">
                    <span>{{createFinaForm.vin}}</span>
                  </FormItem>
                </Col>
                <Col span="12">
                  <FormItem label="申请编号：">
                    <span>{{createFinaForm.alixNum}}</span>
                  </FormItem>
                </Col>
              </Row>
              <Row>
                <Col span="12">
                  <FormItem label="品牌：">
                    <span>{{createFinaForm.brand}}</span>
                  </FormItem>
                </Col>
                <Col span="12">
                  <FormItem label="实际颜色：">
                    <span>{{createFinaForm.color}}</span>
                  </FormItem>
                </Col>
              </Row>
              <Row>
                <Col span="12">
                  <FormItem label="实际停放车库：">
                    <span>{{createFinaForm.actualGarageName}}</span>
                  </FormItem>
                </Col>                
              </Row>
              <Row>
                <Col span="24">
                  <FormItem label="实际具体停放地址：">
                    <span>{{createFinaForm.actualParkAddress}}</span>
                  </FormItem>
                </Col>
              </Row>
              <Row>
                <Col span="12">
                <FormItem label="实际入库日期：">
                  <span>{{createFinaForm.actualStorageTime}}</span>
                </FormItem>
                 </Col>
                <Col span="12">
                 <FormItem label="盘点人姓名：">
                  <span>{{createFinaForm.pandianName}}</span>
                </FormItem>
                </Col>
              </Row>
              <Row>
                <FormItem label="盘点日期：">
                  <span v-if="isHideDate">{{createFinaForm.pandianDate}}</span>
                </FormItem>
              </Row>
              <Row>
                 <FormItem label="外观是否损坏：" prop="isAppearanceDamage" class="personMsg">
                  <Select v-model="createFinaForm.isAppearanceDamage" style="width:300px;" clearable placeholder="" :disabled="changetitle =='查看盘点期数及时间'">
                    <Option
                      v-for="item in inventoryList"                    
                      :key="item.value"
                      :value="item.value"
                    >{{item.name}}</Option>
                  </Select> 
                </FormItem>
              </Row>
              <Row>
                 <FormItem label="具体情况说明：" prop="appearanceDamageDescription" :class="{personMsg:createFinaForm.isAppearanceDamage==1}" >
                  <Input type="text" v-model="createFinaForm.appearanceDamageDescription" :maxlength="50" placeholder="" :disabled="changetitle =='查看盘点期数及时间'" style="width:300px;"></Input>
                </FormItem>
              </Row>
              <Row>
                 <FormItem label="盘点结果类型：" prop="resultType" class="personMsg">
                  <Select v-model="createFinaForm.resultType" clearable :disabled="changetitle =='查看盘点期数及时间'" style="width:300px;" placeholder="">
                    <Option
                      v-for="item in resultTypeList"                    
                      :key="item.value"
                      :value="item.value"
                    >{{item.name}}</Option>
                  </Select> 
                </FormItem>
              </Row>
              <Row>
                 <FormItem label="具体情况说明：" prop="resultTypeDescription" :class="{personMsg:createFinaForm.resultType==3}" >
                  <Input type="text" v-model="createFinaForm.resultTypeDescription" :disabled="changetitle =='查看盘点期数及时间'"  style="width:300px;" :maxlength="50" placeholder=""></Input>
                </FormItem>
              </Row>
            </Form>
            <div style="padding:20px;">
              <Table border :columns="uploadCol" :data="uploadData" class="repaymentPlanT"></Table>
            </div>
        </div>
        <div style="text-align:right;margin-top:15px;">
            <Button  @click="createOk" type="primary" v-if="changetitle !='查看盘点期数及时间'">确定</Button>
            <Button  @click="createCancel">取消</Button>
        </div>
        <div slot="footer"></div>
    </Modal>
    <!-- 其他在库车辆 -->
    <Modal
        v-model="addOtherCarModal"
        title="新建"
        >
        <div>
            <Form ref="addOtherCar" :model="addOtherCar" :rules="addOtherCarRule" :label-width="120">
              <Row>
                <Col span="20">
                  <FormItem label="车架号" prop="vin">
                     <Input type="text" v-model="addOtherCar.vin" placeholder="请输入..."></Input>
                  </FormItem>
                </Col>
              </Row>
              <Row>
                <Col span="20">
                  <FormItem label="车牌号" prop="licNum">
                     <Input type="text" v-model="addOtherCar.licNum" placeholder="请输入..."></Input>
                  </FormItem>
                </Col>   
              </Row>       
              <Row>
                <Col span="20">
                  <FormItem label="品牌" prop="brandStr">
                     <Select v-model="addOtherCar.brandStr" clearable placeholder="请选择">
                        <Option v-for="item in brandList" :key="item.name" :value="item.name">{{item.name}}</Option>
                      </Select> 
                  </FormItem>
                </Col>
              </Row>       
              <Row>
                <Col span="20">
                  <FormItem label="颜色" prop="color">
                     <Input type="text" v-model="addOtherCar.color" placeholder="请输入..."></Input>
                  </FormItem>
                </Col>
              </Row>  
            </Form>
        </div>
        <div style="text-align:right;margin-top:15px;">
            <Button @click="otherOk" type="primary">确定</Button>
            <Button @click="otherCancel">取消</Button>
        </div>
        <div slot="footer"></div>
    </Modal>
  </div>
</template>
<script>
export default {
  name: "task",
  data() {
    return {
      type:'add',
      showEdit:false,
      changetitle: "新建盘点期数及时间",
      approvalStatus:[],
      approvalStatusList:[],
      brandList:[],
      resultTypeList:[],
      isHideDate:true,
      billState:"",
      showEditBtn:false,
      inventoryList:[
      {
        'name':'否',
        'value':'0',
      },
      {
        'name':'是',
        'value':'1',
      }],//是否盘点
      createCarModal: false, //任务管理模态窗
      searchCarModal:false,//点击指定车库弹窗
      addOtherCarModal:false,
      listNum:0,
      actualNum:0,
      searchForm: {
        //查询
        vin: "",
        inventory: "",
        approvalStatus: "",
      },
      total: 0,
      current: 1,
      rowCount: 10,
      tableCol: [
        {
            title: "序号",
            type: 'index',
            width: 60,
            align: 'center'
        },
        {
          title: "车架号",
          key: "vin"
        },
        {
          title: "申请编号",
          key: "alixNum"
        },
        {
          title: "车牌号",
          key: "licNum"
        },
        {
          title: "车型",
          key: "vehicleClassStr"
        },
        {
          title: "颜色",
          key: "color"
        },
        // {
        //   title: "审批状态",
        //   key: "approvalStatusStr"
        // },
        // {
        //   title: "不合格原因",
        //   key: "unQualifiedReasons",
         
        // },
        {
          title: "是否盘点",
          key: "inventoryStr"
        },
        {
          title: "操作",
          key: "action",
          width: 140,
          render: (h, params) => {//未盘点不显示编辑按钮，
            return h("div", [
              h('a', {
                style: {
                  marginRight: '5px',
                  display:this.$route.query.type !='view'&&(this.billState == 2?(params.row.approvalStatus == 2?this.showEditBtn=true:this.showEditBtn=false):this.billState == 3?this.showEditBtn=false:params.row.inventoryStr == '已盘点'?this.showEditBtn = true:this.showEditBtn = false)?"inline-block":"none",
                },
                on: {
                    click: () => {

                          //三目逻辑
                          // if(this.billState == 2){
                          //     if(params.row.approvalStatus == 2){
                          //       this.showEditBtn=true;
                          //     }else{
                          //       this.showEditBtn=false;
                          //     }
                          // }
                          // else if(this.billState == 3){
                          //    this.showEditBtn=false;                              
                          // }
                          // else{
                          //   if(params.row.inventoryStr == '已盘点'){
                          //     this.showEditBtn = true;
                          //   }
                          // }

                          this.type = 'edit';
                          this.changetitle = '编辑盘点期数及时间';
                          if(params.row.inventoryStr=="已盘点"){
                            this.isHideDate = true;
                          }
                          this.createCarModal = !this.createCarModal;
                          this.createFinaForm.vinPhotoAttId = '';
                          this.uploadData[0].sourceAttchName ='';
                          this.uploadData[0].url = '';
                          this.createFinaForm.carHeadPhotoAttId = '';
                          this.uploadData[1].sourceAttchName = '';
                          this.uploadData[1].url = "";
                          this.viewFn(params.row.id);
                      }
                    }
                }, '编辑'),
               h('a', {
                style: {
                  marginRight: '5px',
                  display:params.row.inventoryStr != '未盘点' || this.$route.query.type =='view'?"none":"inline-block",
                },
                on: {
                    click: () => {
                          this.type = 'edit';
                          this.changetitle = '盘点期数及时间'; 
                          this.isHideDate = true;                      
                          this.createCarModal = !this.createCarModal;
                          this.createFinaForm.vinPhotoAttId = '';
                          this.uploadData[0].sourceAttchName ='';
                          this.uploadData[0].url = '';
                          this.createFinaForm.carHeadPhotoAttId = '';
                          this.uploadData[1].sourceAttchName = '';
                          this.uploadData[1].url = "";
                          this.viewFn(params.row.id);
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
                      this.type = 'look';
                      this.changetitle = '查看盘点期数及时间';
                      this.createCarModal = !this.createCarModal;
                      if(params.row.inventoryStr=="未盘点"){
                        this.isHideDate = false;
                      }
                      this.viewFn(params.row.id);
                    }
                  }
                },
                "详情"
              )
            ]);
          }
        }
      ],
      tableData: [],
      pageSizeOpts: [10, 20, 50, 100],
      selectData: [],
      tableHeight: 0,
      createFinaForm: {
        id:'',
        vin: "",
        alixNum: "",
        brand: "",
        color: "",
        actualGarageName: "",
        actualParkAddress: "",
        actualStorageTime: "",
        pandianName: "",
        pandianDate:'',
        appearanceDamageDescription: "",
        isAppearanceDamage: "",
        appearanceDamageDescription: "",
        resultType:'',
        resultTypeDescription:'',
        vinPhotoAttId:'',
        carHeadPhotoAttId:'',
      },
      addOtherCar:{
        vin:'',
        licNum:'',
        brandStr:'',
        color:'',
      },
      addTableCol:[
          {
            title: "序号",
            type: 'index',
            width: 60,
            align: 'center'
          },
          {
            title: '车架号',
            key: 'vin'
          },
          {
            title: '车牌号',
            key: 'licNum'
          },
          {
            title: '品牌',
            key: 'brandStr'
          },
          {
            title: '颜色',
            key: 'color'
          },
          {
            title: '操作',
            width:120,
            render:(h,params)=>{
              let self = this;
              return h('div', [
                h('a', {
                  style: {
                    display:this.$route.query.type =='view'?"none":"inline-block",
                  },
                  on: {
                    click: () => {
                      if(this.addTableData[params.index].id){
                        this.delIMG(this.addTableData[params.index].id)
                      }
                     // this.addTableData.splice(params.index,1)
                    }
                  }
                }, '删除'),
              ]);
            }
         },
      ],
      addTableData:[],
      uploadCol: [
        {
            title: "序号",
            type: 'index',
            width: 60,
            align: 'center'
        },
        {
          title:'文件名称',
          key:'name',
          width: 120,
        },
        {
          title: "附件",
          key: "sourceAttchName",
          render: (h, params) => {
            let self_ = this;
            return h("div", [             
              h(
                "a",
                {
                  style: {
                    marginRight: "5px",
                  },
                  on: {
                    click: () => {
                      window.open(params.row.url);
                    }
                  }
                },
                params.row.sourceAttchName
              )
            ]);
          }
        },
        {
          title: "操作",
          key: "order",
          render: (h, params) => {
            let _self = this;
            return h("div", [ 
              h(
                "Upload",
                {
                  props: {
                    action: "/garage/attachSource/uploadFile",
                    data: {},
                    name: "file",
                    maxSize: 20480,
                    showUploadList: false,
                    format: ["jpg", "jpeg", "png"],
                    onSuccess: function(res, file) {
                      if (res.success == true) {
                        this.$Modal.error({
                          content: "上传成功",
                          onOk: () => {
                            if(params.index == 0){//车架号照片
                              _self.createFinaForm.vinPhotoAttId = res.data.id;
                              _self.uploadData[0].sourceAttchName = res.data.sourceAttchName;
                              _self.uploadData[0].url = res.data.viewUrl;
                            }else{
                              _self.createFinaForm.carHeadPhotoAttId = res.data.id;
                              _self.uploadData[1].sourceAttchName = res.data.sourceAttchName;
                              _self.uploadData[1].url = res.data.viewUrl;
                            }
                          }
                        });
                      } else {
                        this.$Modal.error({
                          content: res.data.errorMessage,
                          onOk: () => {}
                        });
                      }
                    },
                    onFormatError: function(file) {
                      this.$Modal.error({
                        content: "文件格式不正确!",
                        onOk: () => {}
                      });
                    },
                    onExceededSize: function(file) {
                      this.$Modal.error({
                        content: "文件大小超过20M!",
                        onOk: () => {}
                      });
                    }
                  },
                  style: {
                    display: this.type == 'look'? "none" : "inline-block",
                    marginRight: "5px",
                    color: "#2d8cf0",
                    cursor: "pointer"
                  }
                },
                "上传"
              ),             
              h(
                "a",
                {
                  style: {
                    marginRight: "5px",
                    display: this.type=='look'? "none" : "inline-block"
                  },
                  on: {
                    click: () => {
                      if(params.index == 0){//车架号照片
                        this.createFinaForm.vinPhotoAttId = '';
                        this.uploadData[0].sourceAttchName ='';
                        this.uploadData[0].url = '';
                      }else{
                        this.createFinaForm.carHeadPhotoAttId = '';
                        this.uploadData[1].sourceAttchName ='';
                        this.uploadData[1].url = '';
                      }
                    }
                  }
                },
                "删除"
              )
            ]);
          }
        }
      ],
      uploadData:[
      {
        "name":'车架号照片',
        'sourceAttchName':'',
        "url":'',
      },
      {
        "name":'车头正面照片',
        'sourceAttchName':'',
        "url":'',
      }],
      addOtherCarRule:{
        vin: [
          {
            required: true,           
            message: "请填写车架号",
            trigger: "change"
          }
        ],
        licNum: [
          {
            required: true,
            message: "请填写车牌号",
            trigger: "change"
          }
        ],
        brandStr: [
          {
            required: true,
            message: "请选择品牌",
            trigger: "change"
          }
        ],
        color: [
          {
            required: true,
            message: "请填写颜色",
            trigger: "change"
          }
        ],
      },
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
    rowClassName (row, index) {//未在库时标红
        if (row.resultTypeStr === '未在库') {
            return 'demo-table-red-row';
        }
        return '';
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
    addGarage() {
      this.type = 'add';
      for (let i in this.addOtherCar) {       
          this.addOtherCar[i] = "";        
      }
      this.addOtherCarModal = !this.addOtherCarModal;
    },
    createOk(){
      //盘点
      let list = Object.assign({}, list, this.createFinaForm);
      if(this.createFinaForm.isAppearanceDamage == null||this.createFinaForm.resultType == null){
         this.$Modal.warning({
          title: "提示",
          content: '请填写必填项',
          onOk: () => {}
        });
         return;
      }
      if(this.createFinaForm.isAppearanceDamage == 1&&!this.createFinaForm.appearanceDamageDescription){       
          this.$Modal.warning({
            title: "提示",
            content: '请填写具体情况说明',
            onOk: () => {}
          });               
         return;
      }      
      if(this.createFinaForm.resultType == 3 && !this.createFinaForm.resultTypeDescription){
        this.$Modal.warning({
            title: "提示",
            content: '请填写具体情况说明',
            onOk: () => {}
        });
        
        return;    
      }
      if(this.createFinaForm.resultType != 3 && !this.createFinaForm.vinPhotoAttId || this.createFinaForm.resultType != 3 && !this.createFinaForm.carHeadPhotoAttId){//未在库不用上传附件        
          this.$Modal.warning({
            title: "提示",
            content: '请上传图片',
            onOk: () => {}
          });
           
         return;
      }
      this.$http
        .post("/garage/sgInventoryDetail/saveDetail", list)
        .then(function(res) {    
          if (res.data.success == true) {
            this.$Modal.success({
              title: "提示",
              content: res.data.data,
              onOk: () => {
                this.getData();
                this.createCarModal = false;
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
    viewFn(id){//编辑
      let list = {
        "id":id
      }
      this.$http
        .post("/garage/sgInventoryDetail/getDetailView", list)
        .then(function(res) {    
          if (res.data.success == true) {  
                  this.createFinaForm.id = id;            
                  this.createFinaForm.vin = res.data.data.vin;
                  this.createFinaForm.alixNum = res.data.data.alixNum;
                  this.createFinaForm.brand = res.data.data.brandStr;
                  this.createFinaForm.color = res.data.data.color;
                  this.createFinaForm.actualGarageName = res.data.data.actualGarageName;
                  this.createFinaForm.actualParkAddress = res.data.data.actualParkAddress;
                  this.createFinaForm.actualStorageTime = res.data.data.actualStorageTime;
                  this.createFinaForm.pandianName = res.data.data.pandianName;
                  this.createFinaForm.pandianDate = res.data.data.pandianDate;
                  this.createFinaForm.appearanceDamageDescription = res.data.data.appearanceDamageDescription;
                  this.createFinaForm.isAppearanceDamage = res.data.data.isAppearanceDamage;
                  this.createFinaForm.appearanceDamageDescription = res.data.data.appearanceDamageDescription;
                  this.createFinaForm.resultType = res.data.data.resultType;
                  this.createFinaForm.resultTypeDescription = res.data.data.resultTypeDescription;
                  this.createFinaForm.vinPhotoAttId = res.data.data.vinAttId;
                  this.createFinaForm.carHeadPhotoAttId = res.data.data.carHeadAttId;
                  this.uploadData[0].vinPhotoView = res.data.data.vinPhotoView;
                  this.uploadData[1].carHeadPhotoView = res.data.data.carHeadPhotoView;
                  this.uploadData[0].url = res.data.data.vinPhotoView;
                  this.uploadData[1].url = res.data.data.carHeadPhotoView;
                  this.uploadData[0].sourceAttchName = res.data.data.vinPhotoName;
                  this.uploadData[1].sourceAttchName = res.data.data.carHeadPhotoName;
              } else {
                this.$Modal.warning({
                  title: "提示",
                  content: res.data.errorMessage,
                  onOk: () => {}
                });
            }
        });
    },
    otherOk(){
      //新增
      let self = this;
       this.$refs["addOtherCar"].validate(valid => {
        if (valid) {
          let list = Object.assign({}, list, this.addOtherCar);
          list.sgBillId = this.$route.query.sgBillId;
          this.$http
            .post("/garage/sgInventoryDetail/otherAdd", list)
            .then(function(res) {    
              if (res.data.success == true) {
                this.$Modal.success({
                  title: "提示",
                  content: '新增成功',
                  onOk: () => {
                    self.getData();
                    self.addOtherCarModal = !this.addOtherCarModal;
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
      })
    },
    delIMG(id){
          let list={
            "sgBillId":id
          }
          this.$http
            .post("/garage/sgInventoryDetail/otherDelete", list)
            .then(function(res) {    
              if (res.data.success == true) {
                this.$Modal.success({
                  title: "提示",
                  content: '删除成功',
                  onOk: () => {
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
    },
    otherCancel(){
      this.addOtherCarModal = false;
    },
    submitBtn(){
      let list = {
        'id': this.$route.query.sgBillId
      }
      this.$http
        .post("/garage/sgInventoryBill/submitBill", list)
        .then(function(res) {    
          if (res.data.success == true) {
            this.$Modal.success({
              title: "提示",
              content: res.data.data,
              onOk: () => {
                this.$router.push(
                  "/disk_warehouse_list"
                );
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
    returnBtn(){
       this.$router.push("/disk_warehouse_list");
    },
    getData() {
      //获取列表数据
      let list = Object.assign({}, list, this.searchForm);
      if(this.searchForm.inventory == '0'){
         list.inventory = false;
      }else if(this.searchForm.inventory == '1'){
         list.inventory = true;
      }
      list.sgBillId = this.$route.query.sgBillId;
      this.$http
        .post("/garage/sgInventoryBill/getBillView", list)
        .then(function(res) {    
          if (res.data.success == true) {
            this.tableData = res.data.data.inGarageVehicles;
            this.addTableData = res.data.data.othersInVehicles;
            this.listNum = res.data.data.inventoryNum.listNum;
            this.actualNum = res.data.data.inventoryNum.actualNum;
            this.billState = res.data.data.billState;
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
     getBrandListFn() {
      //品牌下拉
      this.$http.get("/garage/carDict/listMasterBrand").then(function(res) {
        if (res.data.success == true) {
          this.brandList = res.data.data;
        } else {
          this.$Modal.warning({
            title: "提示",
            content: res.data.errorMessage,
            onOk: () => {}
          });
        }
      });
    },
    getApprovalState() {
      //审批状态
      this.$http.post("/garage/enum/getEnumDataList?enumName=garage.inventory.InventoryApprovalStateEnum").then(function(res) {
        if (res.data.success == true) {
          this.approvalStatusList = res.data.data;
        } else {
          this.$Modal.warning({
            title: "提示",
            content: res.data.errorMessage,
            onOk: () => {}
          });
        }
      });
    },
    getResultType() {
      //盘点结果
      this.$http.post("/garage/enum/getEnumDataList?enumName=garage.inventory.InventoryResultTypeEnum").then(function(res) {
        if (res.data.success == true) {
          this.resultTypeList = res.data.data;
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
    this.getApprovalState();
    this.getBrandListFn();
    this.getResultType();
    this.searchBtn();
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
 .ivu-table .demo-table-red-row td{
    background-color: #ed4014;
    color: #fff;
    
}
.ivu-table .demo-table-red-row td div a{
    color:#fff;
}
</style>
