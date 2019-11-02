<template>
  <div class="info-bg">
    <div class="bg-class">
      <!-- 新建车库123 -->
      <h3 class="itemTitle" style="margin:20px 0;">
        <span></span>车库信息
      </h3>
      <Form ref="createFinaForm" :model="createFinaForm" :rules="createFinaRule" :label-width="120">
        <Row>
          <Row style="margin-bottom:5px;">
            <Col span="10">
              <FormItem label="车库名称：" prop="garageName">
                <Input
                  type="text"
                  v-model="createFinaForm.garageName"
                  :title="createFinaForm.garageName"
                  :disabled="isView"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
            <Col span="10">
              <FormItem label="所在地区" class="personMsg">
                <Col span="11">
                  <FormItem prop="province">
                    <Select v-model="createFinaForm.province" @on-change="getprovinceId"  :disabled="isView" :label-in-value="true" placeholder="请选择">
                      <Option
                        v-for="item in provinceList"
                        :key="item.regionId"
                        :value="item.regionId"
                      >{{item.name}}</Option>
                    </Select>
                  </FormItem>
                </Col>
                <Col span="2">
                  <span style="margin:0 10px;">一</span>
                </Col>
                <Col span="11">
                  <FormItem prop="city">
                    <Select v-model="createFinaForm.city" @on-change="getcityName" :disabled="isView" :label-in-value="true" placeholder="请选择">
                      <Option
                        v-for="item in cityList"
                        :key="item.regionId"
                        :value="item.regionId"
                      >{{item.name}}</Option>
                    </Select>
                  </FormItem>
                </Col>
              </FormItem>
            </Col>
          </Row>
          <Row style="margin-bottom:5px;">
            <Col span="10">
              <FormItem label="具体地址：" prop="garageAddresss">
                <Input
                  type="text"
                  :disabled="isView"
                  v-model="createFinaForm.garageAddresss"
                  :title="createFinaForm.garageAddresss"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
            <Col span="10">
              <FormItem label="总车位：" prop="parkingNum">
                <InputNumber
                  :disabled="isView"
                  v-model="createFinaForm.parkingNum"
                  :title="createFinaForm.parkingNum"
                  :maxlength="7"
                  placeholder="请输入..."
                ></InputNumber>
              </FormItem>
            </Col>
          </Row>
          <Row style="margin-bottom:5px;">
            <Col span="10">
              <FormItem label="经营属性：" prop="operateAttr">
                <Select v-model="createFinaForm.operateAttr" :disabled="isView" placeholder="请选择">
                  <Option
                    v-for="item in operateAttrList"
                    :key="item.value"
                    :value="item.value"
                  >{{item.name}}</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="10">
              <FormItem label="租期：" class="personMsg">
                <Col span="11">
                  <FormItem prop="tenancyBeginDate">
                    <DatePicker
                    :disabled="isView"
                      v-model="createFinaForm.tenancyBeginDate"
                      @on-change="beginDateChange"
                      type="date"
                      :options="beginOptions"
                    ></DatePicker>
                  </FormItem>
                </Col>
                <Col span="2">
                  <span style="line-height:30px;margin-left:10px;">至</span>
                </Col>
                <Col span="11">
                  <FormItem prop="tenancyEndDate">
                    <DatePicker
                    :disabled="isView"
                      v-model="createFinaForm.tenancyEndDate"
                      @on-change="endDateChange"
                      type="date"
                      :options="endOptions"
                    ></DatePicker>
                  </FormItem>
                </Col>
              </FormItem>
            </Col>
          </Row>
          <Row style="margin-bottom:5px;">
            <Col span="10">
              <FormItem label="车库状态：" prop="garageStatus">
                <Select v-model="createFinaForm.garageStatus" :disabled="isView" placeholder="请选择">
                  <Option
                    v-for="item in garageStatusList"
                    :key="item.value"
                    :value="item.value"
                  >{{item.name}}</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="10">
              <FormItem label="发票类型：" prop="invoiceType">
                <Select
                  v-model="createFinaForm.invoiceType"
                  :label-in-value="true"
                  placeholder="请选择"
                  :disabled="isView"
                >
                  <Option
                    v-for="item in invoiceTypeList"
                    :key="item.value"
                    :value="item.value"
                  >{{item.name}}</Option>
                </Select>
              </FormItem>
            </Col>
          </Row>
          <Row style="margin-bottom:5px;">
            <Col span="20">
              <FormItem label="停车费（元）：">
                <FormItem label="小时：" prop="parkingCostHour" class="stopCarMonLabel">
                  <InputNumber
                    v-model="createFinaForm.parkingCostHour"
                    :title="createFinaForm.parkingCostHour"
                    placeholder="请输入..."
                    style="width:200px;"
                    :disabled="isView"
                  ></InputNumber>
                </FormItem>
                <FormItem label="天：" prop="parkingCostDay" class="stopCarMonLabel">
                  <InputNumber
                    v-model="createFinaForm.parkingCostDay"
                    :title="createFinaForm.parkingCostDay"
                    placeholder="请输入..."
                    style="width:200px;"
                    :disabled="isView"
                  ></InputNumber>
                </FormItem>
                <FormItem label="月：" prop="parkingCostMonth" class="stopCarMonLabel">
                  <InputNumber
                    v-model="createFinaForm.parkingCostMonth"
                    :title="createFinaForm.parkingCostMonth"
                    placeholder="请输入..."
                    style="width:200px;"
                    :disabled="isView"
                  ></InputNumber>
                </FormItem>
              </FormItem>
            </Col>
          </Row>
          <Row style="margin-bottom:5px;">
            <Col span="5">
              <FormItem label="是否签协议：" prop="fillPaySettled" class="personMsg1">
                <RadioGroup v-model="createFinaForm.fjStatus" :disabled="isView">
                  <Radio label="1" :disabled="isView">是</Radio>
                  <Radio label="0" :disabled="isView">否</Radio>
                </RadioGroup>
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="附件：" v-if="createFinaForm.fjStatus == 1" class="personMsg">
                <Upload
                  action="/garage/attachSource/uploadFile"
                  name="file"
                  :disabled="isView"
                  :maxSize="20480"
                  :format='["jpg", "jpeg", "png"]'
                  :on-success="uploadSuccess"
                  :on-progress="uploadProgress"
                  :show-upload-list="false"
                  :on-format-error="handleFormatError"
                  style="display:inline-block;"
                >
                  <Button type="primary" :loading="exportLoad" v-if="$route.query.type=='add'||$route.query.type=='edit'" :disabled="this.photoData.length == 1">上传附件</Button>
                </Upload>
                 <Button type="primary" @click="photoModal = true">查看附件</Button>
              </FormItem>
            </Col>
          </Row>
          <Row>
            <FormItem label="负责人信息：" prop="personList" class="personMsg">
              <Button type="primary" size="large" @click="contactBtn" :disabled="isView" v-if="$route.query.type=='add'||$route.query.type=='edit'">添加</Button>
            </FormItem>
            <div v-for="(item, index) in personList" style="margin-left:50px;width:100%;clear:both;height:30px;">
              <Col span="8">
                <FormItem label="负责人名称：" class="personMsg">
                  <Input
                    type="text"
                    v-model="item.name"
                    :title="item.name"
                    :disabled="isView"
                    placeholder
                    style="width:176px!important;"
                  ></Input>
                </FormItem>
              </Col>
              <Col span="8">
                <FormItem label="负责人联系方式：" class="personMsg">
                  <Input
                    type="text"
                    v-model="item.phone"
                    :title="item.phone"
                    :disabled="isView"
                    placeholder
                    style="width:176px!important;"
                    :maxlength="11"
                    @on-blur="test(index)"
                  ></Input>
                </FormItem>
              </Col>
              <Col span="3">
                <Button type="error" @click="delPerson(index)"  :disabled="isView" v-if="$route.query.type=='add'||$route.query.type=='edit'">删除</Button>
              </Col>
            </div>
          </Row>
          <!-- <Row>
            <Col span="10">
              <FormItem label="负责人名称：">
                <Input
                  type="textarea"
                  :rows="4"
                  v-model="createFinaForm.remark"
                  :title="createFinaForm.projName"
                  placeholder='多个负责人请用","分隔。请不要用中英文逗号掺杂'
                  style="width:176px!important;"
                ></Input>
              </FormItem>
            </Col>
            <Col span="10">
              <FormItem label="负责人联系方式：">
                <Input
                  type="textarea"
                  :rows="4"
                  v-model="createFinaForm.remark"
                  :title="createFinaForm.projName"
                  placeholder='多个负责人请用","分隔。顺序与联系人对应，请不要用中英文逗号掺杂'
                  style="width:176px!important;"
                ></Input>
              </FormItem>
            </Col>
          </Row>-->
        </Row>
        <h3 class="itemTitle" style="margin:20px 0;">
          <span></span>备注信息
        </h3>
        <Row>
          <Col span="22">
            <FormItem label="备注：">
              <Input
                type="textarea"
                :rows="4"
                :maxlength="100"
                :disabled="isView"
                v-model="createFinaForm.remark"
                :title="createFinaForm.remark"
                placeholder="最多输入100个字符"
              ></Input>
            </FormItem>
          </Col>
        </Row>
      </Form>
      <!-- 日志 -->
      <h3 class="itemTitle" style="margin:20px 0;" v-if="$route.query.type=='view'">
        <span></span>日志信息
      </h3>
      <div style="margin:20px;" v-if="$route.query.type=='view'">
        <Table
          border
          :columns="logCol"
          :data="logData"
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
      <div class="textCenter marginB6" style="padding:20px 0;">
        <Button size="large" @click="createBtn" type="primary" v-if="$route.query.type=='add'">保存</Button>
        <Button size="large" @click="editBtn" type="primary" v-if="$route.query.type=='edit'">保存</Button>
        <Button size="large" @click="$router.push('/garageList')">取消</Button>
      </div>
    </div>
    <Modal v-model="photoModal" title @on-ok="photoOk" @on-cancel="photocancel">
      <div style="padding:20px;">
        <Table border :columns="photoCol" :data="photoData" class="repaymentPlanT"></Table>
      </div>
    </Modal>
  </div>
</template>
<script>
export default {
  name: "paymentpool",
  data() {
    return {
      photoModal:false,
      isView: false,
      exportLoad: false,
      changetitle: "新建",
      createCarModal: false, //车库模态窗
      cityList: [], //省份
      provinceList: [], //城市
      parkingStatusList: [], //车位状态
      garageStatusList: [], //车库状态
      operateAttrList: [], //经营属性
      personList: [{ name: "", phone: "" }], //联系人相关
      invoiceTypeList: [], //发票类型
      total: 0,
      current: 1,
      rowCount: 10,
      logCol: [
        {
          title: "操作人",
          key: "creatorName"
        },
        {
          title: "处理事项",
          key: "event"
        },
        {
          title: "处理时间",
          key: "createTime"
        },
        {
          title: "备注",
          key: "remark"
        }
      ],
      logData: [],
      photoCol: [
        {
          title: "附件",
          key: "sourceAttchName"
        },
        {
          title: "操作",
          key: "order",
          render: (h, params) => {
            let self_ = this;
            return h("div", [
              h(
                "a",
                {
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      window.open(params.row.viewUrl);
                    }
                  }
                },
                "查看"
              ),
               h(
                "a",
                {
                  style: {
                    marginRight: "5px",
                    display: this.$route.query.type=='view'? "none" : "inline-block"
                  },
                  on: {
                    click: () => {
                      this.delPhoto(params.index);
                    }
                  }
                },
                "删除"
              )
            ]);
          }
        }
      ],
      photoData: [],
      pageSizeOpts: [10, 20, 50, 100],
      tableHeight: 0,
      createFinaForm: {
        id:'',
        garageName: "",
        city: "",
        cityStr:'',
        province: "",
        provinceStr:'',
        garageAddresss: "",
        parkingNum: 0,
        operateAttr: "",
        tenancyBeginDate: '',
        tenancyEndDate: '',
        garageStatus: "",
        invoiceType: "",
        parkingCostHour: 0,
        parkingCostDay: 0,
        parkingCostMonth: 0,
        fjStatus: "",
        remark: ""
      },
      begindate:'',
      endDate:'',
      attachmentId: '',
      deleteArr: [],
      createFinaRule: {
        garageName: [
          {
            required: true,
            message: "请输入车库名称",
            trigger: "change"
          }
        ],
        city: [
          {
            required: true,
            message: "请选择所在市",
            trigger: "change"
          }
        ],
        province: [
          {
            required: true,
            message: "请选择所在省",
            trigger: "change"
          }
        ],
        garageAddresss: [
          {
            required: true,
            message: "请输入其他地址",
            trigger: "change"
          }
        ],
        parkingNum: [
          {
            required: true,
            message: "请输入总车位",
            type: "number",
            trigger: "change"
          }
        ],
        operateAttr: [
          {
            required: true,
            message: "请选择经营属性",
            type: "number",
            trigger: "change"
          }
        ],
        tenancyBeginDate: [
          {
            required: true,
            type: "date",
            message: "请选择开始时间",
            trigger: "change"
          }
        ],
        tenancyEndDate: [
          {
            required: true,
            type: "date",
            message: "请选择结束时间",
            trigger: "change"
          }
        ],
        garageStatus: [
          {
            required: true,
            message: "请选择车库状态",
            type: "number",
            trigger: "change"
          }
        ],
        invoiceType: [
          {
            required: true,
            message: "请选择发票类型",
            type: "number",
            trigger: "change"
          }
        ],
        parkingCostHour: [
          {
            required: true,
            type: "number",
            message: "请选择小时",
            trigger: "change"
          }
        ],
        parkingCostDay: [
          {
            required: true,
            type: "number",
            message: "请选择天",
            trigger: "change"
          }
        ],
        parkingCostMonth: [
          {
            required: true,
            type: "number",
            message: "请选择月",
            trigger: "change"
          }
        ]
      }
    };
  },
  computed: {
			beginOptions() {
				return {
					disabledDate: (date) => {
						let a = date && date.valueOf() > Date.parse(this.createFinaForm.tenancyEndDate) - 86400000;
						let b = date && date.valueOf() < Date.now()-86400000; //可以选择当天的日期
						return a||b;
					}
				};
      },
      endOptions() {
				return {
					disabledDate: (date) => {
						let a;
						let b;
						var userAgent = navigator.userAgent;
						if(userAgent.match(/Firefox/i) == 'Firefox') {
						    a = date && date.valueOf() < Date.parse(this.createFinaForm.tenancyBeginDate);
            }
            else{
              a= date && date.valueOf() < Date.parse(this.createFinaForm.tenancyBeginDate);
              let b = date && date.valueOf() < Date.now()+86400000; //可以选择当天的日期
						}
						return a||b;
					}
				};
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
     delPhoto(index) {//车辆出入库
      //this.delAttIds.push(this.photoData[index].id);
      this.photoData.splice(index, 1);
    },
    test(index){//验证手机号是否非空		     
      var reg=/^[1][3,4,5,7,8][0-9]{9}$/;
        if(!reg.test(this.personList[index].phone)){
        //输入手机号格式错误
          for(var j in this.personList){
            this.$Modal.warning({
              title: "提示",
              content: '手机号输入错误，请确认！'
            });
              //this.personList[index].phone = '';
          }
        }else{
        
        }
    },
    photoOk(){
      this.photoModal = false;
    },
    photocancel(){
      this.photoModal = false;
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
    beginDateChange(date) {
       //this.createFinaForm.tenancyBeginDate = this.dateToString(date) ;
    },
    endDateChange(date) {
      //this.createFinaForm.tenancyEndDate = this.dateToString(date);
    },
    uploadSuccess(resp, file, fileList) {
      //上传完成
      this.exportLoad = false;
      if (resp.success) {
        this.$Modal.success({
          title: "提示",
          content: "上传成功！",
          onOk: () => {
            this.photoData.push(resp.data);
            this.attachmentId=resp.data.id;
          }
        });
      } else {
        this.$Modal.warning({
          title: "提示",
          content: resp.errorMessage
        });
      }
    },
    uploadProgress() {
      //上传中
      this.exportLoad = true;
    },
    handleFormatError() {
      //格式不正确
      this.exportLoad = false;
      this.$Modal.warning({
        title: "提示",
        content: "请上传正确的格式"
      });
    },
    contactBtn() {
      //新建负责人
      //保存新建
      this.personList.push({ name: "", phone: "" });
    },
    delPerson(index) {
      //删除
      this.personList.splice(index, 1);
    },
    createBtn() {
      this.$refs["createFinaForm"].validate(valid => {
        if (valid) {
          let flag = null;
          let self = this;
          if (this.createFinaForm.fjStatus == 1) {
            //是否签署
            if (this.attachmentId == '') {
              flag = true;
            }
          }
          if(this.personList.length){
            for (let i in this.personList) {
              for (let n in this.personList[i]) {
                if (this.personList[i][n] == "") {
                  flag = true;
                }
              }
            }
          }else{
              flag = true;
          }
          
          if (flag) {
            this.$Modal.warning({
              title: "提示",
              content: "请填写必填项"
            });
            return;
          }
          this.saveCreate();
        } else {
        }
      });
    },
    saveCreate() {

      //新建
      let list = Object.assign({}, list, this.createFinaForm);
      list.tenancyBeginDate= this.dateToString(this.createFinaForm.tenancyBeginDate) 
      list.tenancyEndDate= this.dateToString(this.createFinaForm.tenancyEndDate) 
      if(this.createFinaForm.fjStatus=="0"){//是否签署协议
          list.attachmentId = null;
      }else if(this.createFinaForm.fjStatus=="1"){
         list.attachmentId = this.attachmentId;
      }
      list.personList = this.personList;
      this.$http
        .post("/garage/sgGarageInfo/createGarage", list)
        .then(function(res) {
          if (res.data.success == true) {
            this.$Modal.success({
              title: "提示",
              content: res.data.data,
              onOk: () => {
                this.$router.push("/garageList");
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
    editBtn() {
      //编辑
       this.$refs["createFinaForm"].validate(valid => {
        if (valid) {
          let flag = null;
          let self = this;
          if (this.createFinaForm.fjStatus == 1) {
            //是否签署
            if (!this.attachmentId||!this.photoData.length) {
              flag = true;
            }
          }
          if(this.personList.length){
            for (let i in this.personList) {
              for (let n in this.personList[i]) {
                if (this.personList[i][n] == "") {
                  flag = true;
                }
              }
            }
          }else{
              flag = true;
          }
          if (flag) {
            this.$Modal.warning({
              title: "提示",
              content: "请填写必填项"
            });
            return;
          }
          this.saveEdit();
        } else {
        }
      });
    },
    saveEdit() {
      //编辑
      let list = Object.assign({}, list, this.createFinaForm);
      list.tenancyBeginDate= this.dateToString(this.createFinaForm.tenancyBeginDate) 
      list.tenancyEndDate= this.dateToString(this.createFinaForm.tenancyEndDate)     
      list.personList = this.personList;
      if(this.createFinaForm.fjStatus=="0"){//是否签署协议
          list.attachmentId = null;
      }else if(this.createFinaForm.fjStatus=="1"){
         list.attachmentId = this.attachmentId;
      }
      this.$http
        .post("/garage/sgGarageInfo/updateGarage", list)
        .then(function(res) {
          if (res.data.success == true) {
            this.$Modal.success({
              title: "提示",
              content: res.data.data,
              onOk: () => {
                this.$router.push("/garageList");
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

    onPageChange(page) {
      this.current = page;
      this.getlog();
    },
    onPageSizeChange(size) {
      this.current = 1;
      this.rowCount = size;
      this.getlog();
    },
    getDetails() {
      //获取列表数据
      let list = {
        id: this.$route.query.id
      };
      this.$http
        .post("/garage/sgGarageInfo/garageDetail", list)
        .then(function(res) {
          if (res.data.success == true) {
            for (let i in res.data.data) {
              for (let j in this.createFinaForm) {
                if (i == j) {
                  this.createFinaForm[j] = res.data.data[i];                
                  // if(res.data.data.attachmentId){
                  //   this.createFinaForm.fjStatus = '1';                    
                  // }
                }
              }
            }
            if(res.data.data.garageAttList){
              this.createFinaForm.fjStatus = '1';                    
            }else{
              this.createFinaForm.fjStatus = '0'; 
            }
            if(res.data.data.garageAttList){
              this.photoData = res.data.data.garageAttList;
            }
            this.attachmentId = res.data.data.attachmentId;
            this.getprovinceListFn();
            this.personList = res.data.data.personList;
            this.getcityListFn(this.createFinaForm.province);
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    getlog() {
      //日志
      let id = this.$route.query.id;
      let list = {
        garageId: this.$route.query.id
      };
      this.$http
        .post("/garage/sgGarageInfoLog/pageQueryLog", list)
        .then(function(res) {
          if (res.data.success == true) {
            this.logData = res.data.data.records;
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
    getprovinceListFn() {
      //城市
      this.$http.post("/garage/sgGarageInfo/getProvinces").then(function(res) {
        if (res.data.success == true) {
          this.provinceList = res.data.data;
        } else {
          this.$Modal.warning({
            title: "提示",
            content: res.data.errorMessage,
            onOk: () => {}
          });
        }
      });
    },
    getprovinceId(val) {
      this.createFinaForm.provinceStr = val.label;
      this.createFinaForm.province = val.value;
      this.getcityListFn(val.value);
    },
    getcityListFn(id) {
      //市
      this.$http
        .post("/garage/sgGarageInfo/getCities?provinceId=" + id)
        .then(function(res) {
          if (res.data.success == true) {
            this.cityList = res.data.data;
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    getcityName(val) {
      this.createFinaForm.cityStr = val.label;
      this.createFinaForm.city = val.value;
    },
    getoperateAttrList() {
      //经营属性
      this.$http
        .post("/garage/enum/getEnumDataList?enumName=garage.SgOperateAttrEnum")
        .then(function(res) {
          if (res.data.success == true) {
            this.operateAttrList = res.data.data;
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    getparkingStatus() {
      //获取车位状态
      this.$http
        .post("/garage/enum/getEnumDataList?enumName=garage.ParkingStatusEnum")
        .then(function(res) {
          if (res.data.success == true) {
            this.parkingStatusList = res.data.data;
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    getgarageStatusList() {
      //获取车库状态
      this.$http
        .post("/garage/enum/getEnumDataList?enumName=garage.GarageStatusEnum")
        .then(function(res) {
          if (res.data.success == true) {
            this.garageStatusList = res.data.data;
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    getinvoiceTypeList() {
      //获取车库状态
      this.$http
        .post("/garage/enum/getEnumDataList?enumName=garage.InvoiceTypeEnum")
        .then(function(res) {
          if (res.data.success == true) {
            this.invoiceTypeList = res.data.data;
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
    if (this.$route.query.type == "view") {
      this.isView = true;
      this.getDetails();
      this.getlog();
    }
    if (this.$route.query.type == "edit") {
      this.getDetails();
    }
    this.getprovinceListFn();
    this.getoperateAttrList();
    this.getparkingStatus();
    this.getgarageStatusList();
    this.getinvoiceTypeList();
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
.stopCarMonLabel {
  display: inline-block;
  width: 30%;
}
.stopCarMonLabel .ivu-form-item-label {
  width: 62px !important;
}
.stopCarMonLabel .ivu-input-number{
  width: 75% !important;
}
.REQUIRED label::before {
  content: "*";
  display: inline-block;
  margin-right: 4px;
  line-height: 1;
  font-family: SimSun;
  font-size: 12px;
  color: #ed4014;
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
  .personMsg1 .ivu-form-item-label::before{
	    content: "*";
	    display: inline-block;
	    margin-right: 4px;
	    line-height: 1;
	    font-family: SimSun;
	    font-size: 12px;
	    color: #ed4014;
	}
</style>