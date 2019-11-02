<template>
  <div class="info-bg">
    <!-- 查询条件等部分 -->
    <div class="bg-class search-class">
      <h3 class="itemTitle">
        <span></span>车辆管理
      </h3>
      <Form ref="searchForm" :model="searchForm" :label-width="120">
        <Row>
          <Col span="8">
            <FormItem label="车架号">
              <Input type="text" v-model="searchForm.vin" placeholder="请输入..."></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="车牌号" prop="projType">
              <Input type="text" v-model="searchForm.licNum" placeholder="请输入..."></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="车辆来源">
              <Select v-model="searchForm.vehicleSource" placeholder="请选择">
                <Option
                  v-for="item in vehicleSourceList"
                  :key="item.value"
                  :value="item.value"
                >{{item.name}}</Option>
              </Select>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="8">
            <FormItem label="车辆类型">
              <Select v-model="searchForm.propertyRightType" placeholder="请选择">
                <Option
                  v-for="item in propertyRightTypeList"
                  :key="item.value"
                  :value="item.value"
                >{{item.name}}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="所在车库">
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
            <FormItem label="车辆状态">
              <Select v-model="searchForm.stat" placeholder="请选择">
                <Option
                  v-for="item in statList"
                  :key="item.value"
                  :value="item.value"
                >{{item.name}}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="业务类型">
              <Select v-model="searchForm.businessType" placeholder="请选择">
                <Option
                  v-for="item in businessTypeList"
                  :key="item.value"
                  :value="item.value"
                >{{item.name}}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem label="车型">
              <Col span="5" style="margin-right:10px;">
                <Select v-model="searchForm.brand" @on-change="getBrandModelId" placeholder="请选择品牌">
                  <Option
                    v-for="item in brandList"
                    :key="item.id"
                    :value="item.id"
                  >{{item.name}}</Option>
                </Select>
              </Col>
              <Col span="5" style="margin-right:10px;">
                <Select v-model="searchForm.brandModel" @on-change="getVehicleClassId" placeholder="请选择子品牌">
                  <Option
                    v-for="item in brandModelList"
                    :key="item.id"
                    :value="item.id"
                  >{{item.name}}</Option>
                </Select>
              </Col>
              <Col span="5" style="margin-right:10px;">
                <Select v-model="searchForm.vehicleClass" @on-change="getModelId" placeholder="请选择车型">
                  <Option
                    v-for="item in vehicleClassList"
                    :key="item.id"
                    :value="item.id"
                  >{{item.name}}</Option>
                </Select>
              </Col>
              <Col span="5">
                <Select v-model="searchForm.model" placeholder="请选择车款">
                  <Option
                    v-for="item in modelList"
                    :key="item.id"
                    :value="item.id"
                  >{{item.name}}</Option>
                </Select>
              </Col>
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
        <!-- <Button @click="addCar" type="primary">新增车辆</Button> -->
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
  </div>
</template>
<script>
export default {
  name: "vehicleList",
  data() {
    return {
      changetitle: "新建",
      createCarModal: false, //车库模态窗
      vehicleSourceList:[],
      propertyRightTypeList:[],
      sgGarageNameList:[],
      statList:[],
      businessTypeList:[],
      brandList:[],
      brandModelList:[],
      vehicleClassList:[],
      modelList:[],
      searchForm: {//查询
        sign:0,
        vin: "",
        licNum: "",
        vehicleSource: "",
        propertyRightType: "",
        sgGarageInfoId: "",
        stat: "",
        businessType: "",
        brand: "",
        brandModel: "",
        vehicleClass: "",
        model: ""
      },
      total: 0,
      current: 1,
      rowCount: 10,
      tableCol: [
        {
          title: "车架号",
          key: "vin",
          width: 170
        },
        {
          title: "申请编号",
          key: "alixNum",
          width: 120
        },
        {
          title: "车牌号",
          key: "licNum",
          width: 150
        },
        {
          title: "车辆来源",
          key: "vehicleSourceStr",
          width: 120
        },
        {
          title: "车辆类型",
          key: "propertyRightTypeStr",
          width: 120
        },
        {
          title: "品牌",
          key: "brandStr",
          width: 120
        },
        {
          title: "子品牌",
          key: "brandModelStr",
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
          title: "车库所在地址",
          key: "sgGarageAddresss",
          width: 120
        },
        {
          title: "车辆状态",
          key: "statStr",
          width: 120
        },
        {
          title: "业务状态",
          key: "businessTypeStr",
          width: 120
        },
        {
          title: "首次入库日期",
          key: "actualStorageTime",
          width: 120
        },
        {
          title: "累计在库天数",
          key: "actualStorageTimeInt",
          width: 120
        },
        {
          title: "备注",
          key: "remark",
          width: 120
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
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.$router.push(
                        "/vehicleDetails?id=" + params.row.id + "&type=view"
                      );
                    }
                  }
                },
                "详情"
              ),
               h(
                "a",
                {
                  style: {
                    marginRight: "5px",
                    display:params.row.statStr !== '在库'?"none":"inline-block",
                  },
                  on: {
                    click: () => {
                      this.$router.push(
                        "/vehicleDetails?id=" + params.row.id + "&type=edit"
                      );
                    }
                  }
                },
                "编辑"
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
      this.searchForm.sign = '0';
    },
    getImportUrl() {},
    addCar() {
      //新增车辆--模态窗
      this.$router.push("/vehicleDetails?type=add");
      //this.createCarModal = true;
    },
    createCarOk() {
      //保存新建
    },
    createCarCancel() {
      //取消新建
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
        .post("/garage/sgVehicleInfo/pageQuery", list)
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
    getVehicleSourceListFn() {
      //车辆来源下拉
      this.$http
        .post("/garage/enum/getEnumDataList?enumName=garage.SgVehicleSourceEnum")
        .then(function(res) {
          if (res.data.success == true) {
            this.vehicleSourceList = res.data.data;
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    getPropertyRightTypeListFn() {
      //车辆类型下拉
      this.$http
        .post("/garage/enum/getEnumDataList?enumName=garage.VehicleTypeEnum")
        .then(function(res) {
          if (res.data.success == true) {
            this.propertyRightTypeList = res.data.data;
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
    getStatListFn() {
      //车辆状态下拉
      this.$http
        .post("/garage/enum/getEnumDataList?enumName=garage.SgVehicleStatusEnum")
        .then(function(res) {
          if (res.data.success == true) {
            this.statList = res.data.data;
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    getBusinessTypeListFn() {
      //业务状态下拉
      this.$http
        .post("/garage/enum/getEnumDataList?enumName=garage.BusinessTypeEnum")
        .then(function(res) {
          if (res.data.success == true) {
            this.businessTypeList = res.data.data;
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
      this.$http
        .get("/garage/carDict/listMasterBrand")
        .then(function(res) {
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
    getBrandModelId(val){
      if(val){
         this.searchForm.brandModel = '';
         this.searchForm.vehicleClass = '';
         this.searchForm.model = '';         
         this.getBrandModelListFn(val);
      }
       
    },
    getVehicleClassId(val) {
      if (val) {
         this.searchForm.vehicleClass = '';
         this.searchForm.model = ''; 
        this.getVehicleClassListFn(val);
      }
    },
    getModelId(val){
      if(val){
         this.searchForm.model = ''; 
        this.getModelListFn(val);
      }
    },
    getBrandModelListFn(id) {
      //子品牌下拉
      this.$http
        .get("/garage/carDict/listBrand/"+id)
        .then(function(res) {
          if (res.data.success == true) {
            this.brandModelList = res.data.data;
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    getVehicleClassListFn(id) {
      //车型下拉
      this.$http
        .get("/garage/carDict/listModel/"+id)
        .then(function(res) {
          if (res.data.success == true) {
            this.vehicleClassList = res.data.data;
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    getModelListFn(id) {
      //车款下拉
      this.$http
        .get("/garage/carDict/listStyle/"+id)
        .then(function(res) {
          if (res.data.success == true) {
            this.modelList = res.data.data;
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
    this.getVehicleSourceListFn();
    this.getPropertyRightTypeListFn();
    this.getSgGarageNameListFn();
    this.getStatListFn();
    this.getBusinessTypeListFn();
    this.getBrandListFn();
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