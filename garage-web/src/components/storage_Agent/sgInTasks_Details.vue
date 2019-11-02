<template>
  <div class="info-bg">
    <div class="bg-class">
      <!-- 新建车库 -->
      <h3 class="itemTitle" style="margin:20px 0;">
        <span></span>任务信息
      </h3>
      <Form ref="createFinaForm" :model="createFinaForm" :rules="createFinaRule" :label-width="120">
        <Row>
          <Row>
            <Col span="7">
              <FormItem label="任务单号：">
                <Input
                  type="text"
                  v-model="createFinaForm.taskNum"
                  :title="createFinaForm.taskNum"
                  :disabled="isView||isEdit||isApprove"
                  placeholder=""
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="入库单号：">
                <Input
                  type="text"
                  v-model="createFinaForm.detailTaskNum"
                  :title="createFinaForm.detailTaskNum"
                  :disabled="isView||isEdit||isApprove"
                  placeholder=""
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="来源：">
                <Input
                  type="text"
                  v-model="createFinaForm.pushSourceStr"
                  :title="createFinaForm.pushSourceStr"
                  :disabled="isView||isEdit||isApprove"
                  placeholder=""
                ></Input>
              </FormItem>
            </Col>
          </Row>
          <Row>
            <h3 class="itemTitle" style="margin:20px 0;">
              <span></span>调配信息
            </h3>
            <Col span="7">
              <FormItem label="预计出库车库：">
                <Input
                  type="text"
                  v-model="createFinaForm.garageOutName"
                  :title="createFinaForm.garageOutName"
                  :disabled="isView||isEdit||isApprove"
                  placeholder=""
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="实际出库日期：">
                <DatePicker
                  v-model="createFinaForm.actualStartTime"
                  @on-change="getactualStartTime"
                  :disabled="isView||isEdit||isApprove"
                  type="date"
                  placeholder=""
                ></DatePicker>
              </FormItem>
            </Col>
            <!-- <Col span="7">
              <FormItem label="调配原因：">
                <Select
                  v-model="createFinaForm.locCityCode"
                  :disabled = "isView"
                  @on-change="getCityName"
                  :label-in-value="true"
                  placeholder="请选择"
                >
                  <Option
                    v-for="item in locCityList"
                    :key="item.regionId"
                    :value="item.regionId"
                  >{{item.name}}</Option>
                </Select>
              </FormItem>
            </Col>-->
          </Row>
          <Row>
            <Col span="7">
              <FormItem label="入库车库：">
                <Input
                  type="text"
                  v-model="createFinaForm.garageInName"
                  :disabled="isView||isEdit||isApprove"
                  :title="createFinaForm.garageInName"
                  placeholder=""
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="入库日期：">
                <DatePicker
                  v-model="createFinaForm.predictEndTime"
                  @on-change="getpredictEndTime"
                  :disabled="isView||isEdit||isApprove"
                  type="date"
                  placeholder=""
                ></DatePicker>
              </FormItem>
            </Col>
          </Row>
        </Row>
        <h3 class="itemTitle" style="margin:20px 0;">
          <span></span>车辆信息
        </h3>
        <div style="margin:20px;">
          <Table
            border
            :columns="carCol"
            :data="carData"
            :height="tableHeight"
            class="repaymentPlanT"
          ></Table>
        </div>
        <h3 class="itemTitle" style="margin:20px 0;">
          <span></span>物流信息
        </h3>
        <Row>
          <Row>
            <Col span="7">
              <FormItem label="运输方式：">
                <Select
                  v-model="createFinaForm.shippingType"
                  :label-in-value="true"
                  :disabled="isView||isApprove"
                  placeholder="请选择"
                >
                  <Option
                    v-for="item in shippingTypeList"
                    :key="item.value"
                    :value="item.value"
                  >{{item.name}}</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="物流公司：">
                <Input
                  type="text"
                  :disabled="isView||isApprove"
                  v-model="createFinaForm.logiCompany"
                  :title="createFinaForm.logiCompany"
                  placeholder=""
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="运输车辆车牌号：">
                <Input
                  type="text"
                  :disabled="isView||isApprove"
                  v-model="createFinaForm.logiLicNum"
                  :title="createFinaForm.logiLicNum"
                  placeholder=""
                ></Input>
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span="7">
              <FormItem label="托运费：">
                <Input
                  type="text"
                  :disabled="isView||isApprove"
                  v-model="createFinaForm.consignmentFee"
                  :title="createFinaForm.consignmentFee"
                  placeholder=""
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="司机姓名：">
                <Input
                  type="text"
                  :disabled="isView||isApprove"
                  v-model="createFinaForm.driverName"
                  :title="createFinaForm.driverName"
                  placeholder=""
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="司机证件类型：">
                <Select
                  v-model="createFinaForm.cerType"
                  :label-in-value="true"
                  :disabled="isView||isApprove"
                  placeholder="请选择"
                >
                  <Option
                    v-for="item in cerTypeList"
                    :key="item.value"
                    :value="item.value"
                  >{{item.name}}</Option>
                </Select>
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span="7">
              <FormItem label="司机证件号码：">
                <Input
                  type="text"
                  :disabled="isView||isApprove"
                  v-model="createFinaForm.cerNum"
                  :title="createFinaForm.cerNum"
                  placeholder=""
                ></Input>
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span="12">
              <FormItem label="备注：">
                <Input
                  type="textarea"
                  :rows="4"
                  :disabled="isView"
                  v-model="createFinaForm.remark"
                  :title="createFinaForm.remark"
                  placeholder
                ></Input>
              </FormItem>
            </Col>
          </Row>
        </Row>
        <Row>
          <Col span="24" class="textCenter marginB6">
            <FormItem :label-width="1">
             
            </FormItem>
          </Col>
        </Row>
      </Form>
      <h3 class="itemTitle" style="margin:20px 0;" v-if="$route.query.type=='view'">
        <span></span>审批记录
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
      <div class="textCenter marginB6" style="padding-bottom:20px;">
         <Button
          size="large"
          @click="applyBtn"
          type="primary"
          v-if="$route.query.type=='edit'"
        >保存</Button>
        <Button size="large" @click="approval" type="primary" v-if="$route.query.type=='approve'">申请入库</Button>
        <!-- <Button size="large" @click="refuse" type="primary" v-if="$route.query.type=='approve'">审批驳回</Button> -->
        <Button size="large" @click="$router.push('/sgInTasks_list')">取消</Button>
      </div>
      <Modal v-model="photoModal" title @on-ok="photoOk" @on-cancel="photocancel">
        <div style="padding:20px;">
          <Table border :columns="photoCol" :data="photoData" class="repaymentPlanT"></Table>
        </div>
      </Modal>
      <!-- 其他附件 -->
      <Modal v-model="otherModal" title @on-ok="otherOk" @on-cancel="otherCancel">
        <div style="padding:20px;">
          <Table border :columns="otherCol" :data="otherData" class="repaymentPlanT"></Table>
        </div>
      </Modal>
    </div>
  </div>
</template>
<script>
export default {
  name: "paymentpool",
  data() {
    return {
      isView: false,
      isEdit:false,
      isApprove:false,
      photoModal: false,
      otherModal: false,
      exportLoad: false,
      changetitle: "新建",
      createCarModal: false, //车库模态窗
      shippingTypeList: [], //运输方式
      cerTypeList: [], //证件类型
      total: 0,
      current: 1,
      rowCount: 10,
      carCol: [
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
          title: "车款",
          key: "modelStr"
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
          className: 'REQUIRED',
          render: (h, params) => {
            let self = this;
            let aa = params.row.mileage;
            return h("InputNumber", {
              props: {
                value: params.row.mileage,
                disabled: this.$route.query.type == "view"||this.$route.query.type == 'approve',
                max: 999999999999999,
                formatter: value =>
                  /\d+(\.\d{1,2})?/g
                    .exec(`${value}`)[0]
                    .replace(/\B(?=(?:\d{3})+\b)/g, ","),
                parser: value => {
                  let tmp = value.replace(/$s?|(,*)/g, "");
                  if (tmp.indexOf(".") == -1) {
                    return tmp;
                  } else {
                    return tmp.substring(0, tmp.indexOf(".") + 3);
                  }
                }
              },
              class: {
                textR: true
              },
              on: {
                "on-change": e => {
                  aa = e;
                },
                "on-blur": () => {
                  this.carData[params.index].mileage = aa;
                }
              }
            });
          }
        },
        {
          title: "车辆出入库交接单附件",
          key: "attachSourceDTOList",
          width: 140,
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
                            _self.attIds.push(
                              res.data.id
                            );
                            _self.photoData.push(res.data);
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
                    display: this.$route.query.type == 'view'||this.$route.query.type == 'approve'? "none" : "inline-block",
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
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.photoModal = true;
                    }
                  }
                },
                "查看附件"
              )
            ]);
          }
        },
        {
          title: "其他附件",
          key: "otherAttDTOList",
          width: 140,
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
                            _self.otherAttIds.push(
                              res.data.id
                            );
                            _self.otherData.push(res.data);
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
                    display: this.$route.query.type == 'view'||this.$route.query.type == 'approve'? "none" : "inline-block",
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
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.otherModal = true;
                    }
                  }
                },
                "查看附件"
              )
            ]);
          }
        },
        // {
        //   title: "调配状态",
        //   key: "orderStatusStr"
        // }
      ],
      carData: [],
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
      otherCol: [
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
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.delotherPhoto(params.index);
                    }
                  }
                },
                "删除"
              )
            ]);
          }
        }
      ],
      otherData: [],
      pageSizeOpts: [10, 20, 50, 100],
      tableHeight: 0,
      createFinaForm: {
        id: "",
        detailTaskNum: "",
        taskNum: "",
        pushSourceStr: "",
        garageInName: "",
        actualStartTime: "",
        garageOutName: "",
        predictEndTime: "",
        shippingType: "",
        logiCompany: "",
        logiLicNum: "",
        consignmentFee: "",
        driverName: "",
        cerType: "",
        cerNum: "",
        remark: "",        
        vehicleInfoDTOList:[],
      },
      attIds: [],
      otherAttIds:[],
      delAttIds: [],
      createFinaRule: {
        // shippingType: [
        //   {
        //     required: true,
        //     message: "请选择运输方式",
        //     trigger: "change"
        //   }
        // ]
      }
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
    delPhoto(index) {//车辆出入库
      this.delAttIds.push(this.photoData[index].id);
      this.photoData.splice(index, 1);
    },
    delotherPhoto(index) {//其他附件
      this.delAttIds.push(this.otherData[index].id);
      this.otherData.splice(index, 1);
    },
    getactualStartTime(date){
      this.createFinaForm.actualStartTime = date;
    },
    getpredictEndTime(date) {
      this.createFinaForm.predictEndTime = date;
    },
    photocancel(){
      this.photoModal = false;
    },
    photoOk() {
      this.photoModal = false;
    },
    otherOk() {
      this.otherModal = false;
    },
    otherCancel() {
      this.otherModal = false;
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
    approval() {//审批通过
      let list = {
        id: this.$route.query.id,
        remark:this.createFinaForm.remark
      };
      this.$http
        .post("/garage/sgGarageDetail/approveIn", list)
        .then(function(res) {
          if (res.data.success == true) {
             this.$Modal.success({
              title: "提示",
              content: res.data.data,
              onOk: () => {
                 this.$router.push("/sgInTasks_List");
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
    refuse() {//审批拒绝
      let list = {
        id: this.createFinaForm.id,
        remark:this.createFinaForm.remark
      };
      if(!this.createFinaForm.remark){
          this.$Modal.warning({
            title: "提示",
            content:'请填写备注',
            onOk: () => {

            }
          });
           return;
      }
      this.$http.post("/garage/sgGarageDetail/inReject", list).then(function(res) {
        if (res.data.success == true) {
           this.$Modal.success({
              title: "提示",
              content: res.data.data,
              onOk: () => {
                 this.$router.push("/sgInTasks_List");
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
    applyBtn() {
      //处理
      this.$refs["createFinaForm"].validate(valid => {
        if (valid) {
            let flag=null;
            if(this.createFinaForm.vehicleInfoDTOList[0].mileage===''){
              flag=true;
            }
            if(flag){
              this.$Modal.warning({
              title:'提示',
                content: '请填写必填项'
              })
              return;
            }
            this.handle();            
         
        }
      });
    },
    handle(){
       let that = this;
          let list = Object.assign({}, list, this.createFinaForm);
          list.vehicleInfoDTOList[0].mileage = that.carData[0].mileage;
          list.delAttIds = that.delAttIds;
          list.attIds = that.attIds;
          list.otherAttIds = that.otherAttIds;
          this.$http
            .post("/garage/sgGarageOrder/garageOutBranch", list)
            .then(function(res) {
              if (res.data.success == true) {
                this.$Modal.success({
                    title: "提示",
                    content: res.data.data,
                    onOk: () => {
                       this.$router.push("/sgInTasks_List");
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
    getDetails() {
      //获取列表数据
      let id = this.$route.query.sgGaragOrderId;
      let list = {
        sgGaragOrderId: this.$route.query.sgGaragOrderId
      };
      this.$http
        .post("/garage/sgGarageDetail/getBill",list)
        .then(function(res) {
          if (res.data.success == true) {
            for (let i in res.data.data) {
              for (let j in this.createFinaForm) {
                if (i == j) {
                  this.createFinaForm[j] = res.data.data[i];
                }
              }
            }
            this.photoData = res.data.data.attachSourceDTOList;
            this.otherData = res.data.data.otherAttDTOList;
            this.carData=res.data.data.vehicleInfoDTOList;
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
      let list = {
        id: this.createFinaForm.id,
        current: this.current,
        rowCount: this.rowCount
      };
      this.$http
        .post("/garage/sgGarageDetail/pageQueryLog", list)
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
    getshippingTypeList() {
      //运输方式
      this.$http
        .post("/garage/enum/getEnumDataList?enumName=garage.ShippingTypeEnum")
        .then(function(res) {
          if (res.data.success == true) {
            this.shippingTypeList = res.data.data;
          } else {
            this.$Modal.warning({
              title: "提示",
              content: res.data.errorMessage,
              onOk: () => {}
            });
          }
        });
    },
    getCerTypeList() {
      //证件类型
      this.$http
        .post("/garage/enum/getEnumDataList?enumName=garage.CerTypeCodeEnum")
        .then(function(res) {
          if (res.data.success == true) {
            this.cerTypeList = res.data.data;
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
    }
    if(this.$route.query.type == 'edit'){
      this.isEdit = true;
    }
    if(this.$route.query.type == 'approve'){
      this.isApprove = true;
      this.isView = false;
    }
    this.getDetails();
    setTimeout(() => {
      this.getlog();
    }, 200);
    this.getshippingTypeList();
    this.getCerTypeList();
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
.ivu-table th.REQUIRED .ivu-table-cell span::before{
	    content: "*";
	    display: inline-block;
	    margin-right: 4px;
	    line-height: 1;
	    font-family: SimSun;
	    font-size: 12px;
	    color: #ed4014;
	}
</style>