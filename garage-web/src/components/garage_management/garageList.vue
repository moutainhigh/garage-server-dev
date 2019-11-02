<template>
  <div class="info-bg">
    <!-- 查询条件等部分 -->
    <div class="bg-class search-class">
      <h3 class="itemTitle">
        <span></span>车库管理
      </h3>
      <Form ref="searchForm" :model="searchForm" :label-width="120">
        <Row>
          <Col span="8">
            <FormItem label="车库编号">
              <Input type="text" v-model="searchForm.garageNum" placeholder="请输入..."></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="车库名称" prop="projType">
              <Input type="text" v-model="searchForm.garageName" placeholder="请输入..."></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="所在地区">
              <Col span="10">
                <Select v-model="searchForm.province" @on-change="getprovinceId" clearable placeholder="请选择">
                  <Option
                    v-for="item in provinceList"                    
                    :key="item.regionId"
                    :value="item.regionId"
                  >{{item.name}}</Option>
                </Select>
              </Col>
              <Col span="3">
                <span style="margin:0 10px;">一</span>
              </Col>
              <Col span="10">
                <Select v-model="searchForm.city" clearable placeholder="请选择">
                  <Option
                    v-for="item in cityList"
                    :key="item.regionId"
                    :value="item.regionId"
                  >{{item.name}}</Option>
                </Select>
              </Col>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="8">
            <FormItem label="车位状态">
              <Select v-model="searchForm.parkingStatus" placeholder="请选择">
                <Option
                  v-for="item in parkingStatusList"
                  :key="item.value"
                  :value="item.value"
                >{{item.name}}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="经营属性">
              <Select v-model="searchForm.operateAttr" placeholder="请选择">
                <Option
                  v-for="item in operateAttrList"
                  :key="item.value"
                  :value="item.value"
                >{{item.name}}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="车库状态">
              <Select v-model="searchForm.garageStatus" placeholder="请选择">
                <Option
                  v-for="item in garageStatusList"
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
      <div class="btnGroup" v-if="this.showzy">
        <Button @click="addGarage" type="primary">新建车库</Button>
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
  name: "paymentpool",
  data() {
    return {
      showzy:false,
      showEdit:false,
      changetitle: "新建",
      createCarModal: false, //车库模态窗
      provinceList: [], //省份
      cityList: [], //城市
      parkingStatusList:[],//车位状态
      garageStatusList: [], //车库状态
      operateAttrList:[],//经营属性
      searchForm: {
        //查询
        garageNum: "",
        garageName: "",
        province: "",
        city: "",
        parkingStatus: "",
        operateAttr: "",
        garageStatus: ""
      },
      total: 0,
      current: 1,
      rowCount: 10,
      tableCol: [
        {
          title: "车库编号",
          key: "garageNum",
          width: 180,
        },
        {
          title: "车库名称",
          key: "garageName"
        },
        {
          title: "所在地区",
          key: "cityStr"
        },
        {
          title: "具体地址",
          key: "garageAddresss"
        },
        {
          title: "已使用车位",
          key: "parkedNum"
        },
        {
          title: "经营属性",
          key: "operateAttrStr"
        },
        {
          title: "车库状态",
          key: "garageStatusStr"
        },
        {
          title: "负责人",
          key: "contact"
        },
        // {
        //   title: "联系方式",
        //   key: "contactTele",
        //   width: 120
        // },
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
                  display:!this.showzy ?"none":"inline-block",
              	},
                on: {
                    click: () => {
                          this.$router.push(
                            "/garageDetails?id=" +
                              params.row.id +
                              "&type=edit"
                          );
                      }
                    }
                }, '编辑'),
              h(
                "a",
                {
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.$router.push(
                        "/garageDetails?id=" +
                          params.row.id +
                          "&type=view"
                      );
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
        accountNo: "",
        bankName: "",
        currencyCode: "",
        currency: "",
        locCityCode: "",
        locCityName: "",
        locProvinceCode: "",
        locProvinceName: "",
        orgFullName: "",
        remark: "",
        status: ""
      },
      createFinaRule: {
        orgFullName: [
          {
            required: true,
            message: "请输入机构名称",
            pattern: /^[\u4e00-\u9fa5]+$/,
            trigger: "change"
          }
        ],
        locCityCode: [
          {
            required: true,
            message: "请选择所在市",
            trigger: "change"
          }
        ],
        locProvinceCode: [
          {
            required: true,
            message: "请选择所在省",
            trigger: "change"
          }
        ],
        bankName: [
          {
            required: true,
            message: "请输入开户行名称",
            pattern: /^[\u4e00-\u9fa5]+$/,
            trigger: "change"
          }
        ],
        accountNo: [
          {
            required: true,
            pattern: /^[0-9]*$/,
            message: "请输入卡号",
            trigger: "change"
          }
        ],
        currencyCode: [
          {
            required: true,
            message: "请选择币种",
            trigger: "change"
          }
        ]
      }
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
      //新建车库
      this.$router.push("/garageDetails?type=add");
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
        .post("/garage/sgGarageInfo/pageQuery", list)
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
    getprovinceListFn() {
      //城市
      this.$http
        .post("/garage/sgGarageInfo/getProvinces")
        .then(function(res) {
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
    getprovinceId(val){
      if(val){
        this.getcityListFn(val);
      }
      
    },
    getcityListFn(id) {
      //市
      this.$http.post("/garage/sgGarageInfo/getCities?provinceId="+id).then(function(res) {
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
    getAuthorize() {
      //根据权限展示按钮
      this.$http
        .post("/garage/hasRole/getAuthorize")
        .then(function(res) {
          if (res.data.success == true) {
            if(res.data.data == 'garageManageRole'){//库管
                this.showEdit = true;
                this.showzy = false;
            }else if(res.data.data == 'disposeCommissionerRoleId'){//处置专员
                this.showzy = true;
                this.showEdit = false;
            }else{
                this.showzy = false;
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
    this.getprovinceListFn();
    this.getoperateAttrList();
    this.getparkingStatus();
    this.getgarageStatusList();
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