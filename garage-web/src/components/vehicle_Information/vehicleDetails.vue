<template>
  <div class="info-bg">
    <div class="bg-class">
      <!-- 新建车库 -->
      <h3 class="itemTitle" style="margin:20px 0;">
        <span></span>车辆信息
      </h3>
      <Form ref="createFinaForm" :model="createFinaForm" :rules="createFinaRule" :label-width="120">
        <Row style="margin:20px;">
          <Row style="margin:5px 0;">
            <Col span="7">
              <FormItem label="车架号：" prop="vin">
                <Input
                  type="text"
                  v-model="createFinaForm.vin"
                  :disabled="isView"
                  :title="createFinaForm.vin"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="车牌号：" prop="licNum">
                <Input
                  type="text"
                  v-model="createFinaForm.licNum"
                  :disabled="isView"
                  :title="createFinaForm.licNum"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="车辆来源：" prop="vehicleSource">
                <Select
                  v-model="createFinaForm.vehicleSource"
                  :disabled="isView"
                  :label-in-value="true"
                  placeholder="请选择"
                >
                  <Option
                    v-for="item in vehicleSourceList"
                    :key="item.value"
                    :value="item.value"
                  >{{item.name}}</Option>
                </Select>
              </FormItem>
            </Col>
          </Row>
          <Row style="margin:5px 0;">
            <Col span="7">
              <FormItem label="车辆类型：" prop="propertyRightType">
                <Select
                  v-model="createFinaForm.propertyRightType"
                  :disabled="isView"
                  :label-in-value="true"
                  placeholder="请选择"
                >
                  <Option
                    v-for="item in propertyRightTypeList"
                    :key="item.value"
                    :value="item.value"
                  >{{item.name}}</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="品牌：" prop="brand">
                <Select
                  v-if="$route.query.type == 'add'"
                  v-model="createFinaForm.brand"
                  @on-change="getBrandModelId"
                  :disabled="isView"
                  :label-in-value="true"
                  placeholder="请选择"
                   filterable
                >
                  <Option v-for="item in brandList" :key="item.id" :value="item.id">{{item.name}}</Option>
                </Select>
                <Input
                  v-if="$route.query.type != 'add'"
                  type="text"
                  v-model="createFinaForm.brandStr"
                  disabled
                  :title="createFinaForm.brandStr"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="子品牌：" prop="brandModel">
                <Select
                  v-if="$route.query.type == 'add'"
                  v-model="createFinaForm.brandModel"
                  @on-change="getVehicleClassId"
                  :disabled="isView"
                  :label-in-value="true"
                  placeholder="请选择"
                  filterable
                >
                  <Option
                    v-for="item in brandModelList"
                    :key="item.id"
                    :value="item.id"
                  >{{item.name}}</Option>
                </Select>
                <Input
                  v-if="$route.query.type != 'add'"
                  type="text"
                  v-model="createFinaForm.brandModelStr"
                  disabled
                  :title="createFinaForm.brandModelStr"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
          </Row>
          <Row style="margin:5px 0;">
            <Col span="7">
              <FormItem label="车型：" prop="vehicleClass">
                <Select
                  v-if="$route.query.type == 'add'"
                  v-model="createFinaForm.vehicleClass"
                  @on-change="getModelId"
                  :disabled="isView"
                  :label-in-value="true"
                  placeholder="请选择"
                >
                  <Option
                    v-for="item in vehicleClassList"
                    :key="item.id"
                    :value="item.id"
                  >{{item.name}}</Option>
                </Select>
                <Input
                  v-if="$route.query.type != 'add'"
                  type="text"
                  v-model="createFinaForm.vehicleClassStr"
                  disabled
                  :title="createFinaForm.vehicleClassStr"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="车款：" prop="model">
                <Select
                  v-if="$route.query.type == 'add'"
                  v-model="createFinaForm.model"
                  @on-change="getModelName"
                  :disabled="isView"
                  :label-in-value="true"
                  placeholder="请选择"
                >
                  <Option v-for="item in modelList" :key="item.id" :value="item.id">{{item.name}}</Option>
                </Select>
                 <Input
                  v-if="$route.query.type != 'add'"
                  type="text"
                  v-model="createFinaForm.modelStr"
                  disabled
                  :title="createFinaForm.modelStr"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="颜色：" prop="color">
                <Input
                  type="text"
                  v-model="createFinaForm.color"
                  :disabled="isView"
                  :title="createFinaForm.color"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
          </Row>
          <Row style="margin:5px 0;">
            <Col span="7">
              <FormItem label="申请编号：" prop="alixNum">
                <Input
                  type="text"
                  v-model="createFinaForm.alixNum"
                  :disabled="isView"
                  :title="createFinaForm.alixNum"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="所在车库：" prop="sgGarageInfoId">
                <Select
                  v-model="createFinaForm.sgGarageInfoId"
                  :disabled="isView"
                  placeholder="请选择"
                >
                  <Option
                    v-for="item in sgGarageNameList"
                    :key="item.id"
                    :value="item.id"
                  >{{item.garageName}}</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="里程：" prop="mileage">
                <InputNumber
                  v-model="createFinaForm.mileage"
                  :disabled="isView"
                  :title="createFinaForm.mileage"
                  placeholder="请输入..."
                ></InputNumber>
              </FormItem>
            </Col>
          </Row>
          <Row style="margin:5px 0;">
            <Col span="7">
              <FormItem label="车辆状态：" prop="stat">
                <Select v-model="createFinaForm.stat" disabled placeholder="请选择">
                  <Option
                    v-for="item in statList"
                    :key="item.value"
                    :value="item.value"
                  >{{item.name}}</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="租赁属性：" prop="leaseProperty">
                <Select v-model="createFinaForm.leaseProperty" :disabled="isView" placeholder="请选择">
                  <Option
                    v-for="item in leasePropertyList"
                    :key="item.value"
                    :value="item.value"                   
                  >{{item.name}}</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="业务类型：" prop="businessType">
                <Select v-model="createFinaForm.businessType" :disabled="isView" placeholder="请选择">
                  <Option
                    v-for="item in businessTypeList"
                    :key="item.value"
                    :value="item.value"                   
                  >{{item.name}}</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="视频编号：">
                <Input
                  type="text"
                   :disabled="isView&&isEdit"
                  v-model="createFinaForm.videoNumber"
                  :title="createFinaForm.videoNumber"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
          </Row>
          <Row style="margin:5px 0;">
            <Col span="7">
              <FormItem label="是否有钥匙：">
                <RadioGroup v-model="createFinaForm.isKey">
                  <Radio label="0" :disabled="isView&&isEdit">否</Radio>
                  <Radio label="1" :disabled="isView&&isEdit">1把</Radio>
                  <Radio label="2" :disabled="isView&&isEdit">2把</Radio>
                  <Radio label="3" :disabled="isView&&isEdit">3把</Radio>
                  <Radio label="4" :disabled="isView&&isEdit">4把(含以上)</Radio>
                </RadioGroup>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="是否有行驶证：">
                <RadioGroup v-model="createFinaForm.isDrivingLicense">
                  <Radio label="0" :disabled="isView&&isEdit">否</Radio>
                  <Radio label="1" :disabled="isView&&isEdit">是</Radio>
                </RadioGroup>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="是否有配新钥匙：">
                <RadioGroup v-model="createFinaForm.isNewKey">
                  <Radio label="0" :disabled="isView&&isEdit">否</Radio>
                  <Radio label="1" :disabled="isView&&isEdit">是</Radio>
                </RadioGroup>
              </FormItem>
            </Col>
          </Row>
          <Row style="margin:5px 0;">
            <Col span="7">
              <FormItem label="车牌照是否在车上：">
                <RadioGroup v-model="createFinaForm.isLicnumCar">
                  <Radio label="0" :disabled="isView&&isEdit">否</Radio>
                  <Radio label="1" :disabled="isView&&isEdit">是</Radio>
                </RadioGroup>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="是否正常启动：">
                <RadioGroup v-model="createFinaForm.isNormalStart">
                  <Radio label="0" :disabled="isView&&isEdit">否</Radio>
                  <Radio label="1" :disabled="isView&&isEdit">是</Radio>
                </RadioGroup>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="电瓶是否正常工作：">
                <RadioGroup v-model="createFinaForm.isBatteryWork">
                  <Radio label="0" :disabled="isView&&isEdit">否</Radio>
                  <Radio label="1" :disabled="isView&&isEdit">是</Radio>
                </RadioGroup>
              </FormItem>
            </Col>
          </Row>
          <Row style="margin:5px 0;">
            <Col span="7">
              <FormItem label="是否拖车入库：">
                <RadioGroup v-model="createFinaForm.isDragGarage">
                  <Radio label="0" :disabled="isView&&isEdit">否</Radio>
                  <Radio label="1" :disabled="isView&&isEdit">是</Radio>
                </RadioGroup>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="是否有备胎：">
                <RadioGroup v-model="createFinaForm.isSpare">
                  <Radio label="0" :disabled="isView&&isEdit">否</Radio>
                  <Radio label="1" :disabled="isView&&isEdit">是</Radio>
                </RadioGroup>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="轮胎型号是否一致：">
                <RadioGroup v-model="createFinaForm.isTyreModel">
                  <Radio label="0" :disabled="isView&&isEdit">否</Radio>
                  <Radio label="1" :disabled="isView&&isEdit">是</Radio>
                </RadioGroup>
              </FormItem>
            </Col>
          </Row>
          <Row style="margin:5px 0;">
            <Col span="7">
              <FormItem label="左前轮胎编号：">
                <Input
                  type="text"
                  v-model="createFinaForm.leftFrontNum"
                  :title="createFinaForm.leftFrontNum"
                  :disabled="isView&&isEdit"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="左后轮胎编号：">
                <Input
                  type="text"
                  v-model="createFinaForm.leftRearNum"
                  :title="createFinaForm.leftRearNum"
                  :disabled="isView&&isEdit"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="右前轮胎编号：">
                <Input
                  type="text"
                  v-model="createFinaForm.rightFrontNum"
                  :title="createFinaForm.rightFrontNum"
                  :disabled="isView&&isEdit"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
          </Row>
          <Row style="margin:5px 0;">
            <Col span="7">
              <FormItem label="右后轮胎编号：">
                <Input
                  type="text"
                  v-model="createFinaForm.rightRearNum"
                  :title="createFinaForm.rightRearNum"
                  :disabled="isView&&isEdit"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="邮箱油量：">
                <Input
                  type="text"
                  v-model="createFinaForm.oilQuantity"
                  :title="createFinaForm.oilQuantity"
                  :disabled="isView&&isEdit"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="车内物品：">
                <Input
                  type="text"
                  v-model="createFinaForm.vehicleCross"
                  :title="createFinaForm.vehicleCross"
                  :disabled="isView&&isEdit"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
          </Row>
          <Row style="margin:5px 0;">
            <Col span="7">
              <FormItem label="损坏描述：">
                <Input
                  type="text"
                  v-model="createFinaForm.damageDesc"
                  :title="createFinaForm.damageDesc"
                  :disabled="isView&&isEdit"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="GPS是否拆除：">
                <RadioGroup v-model="createFinaForm.removeGps">
                  <Radio label="0" :disabled="isView&&isEdit">否</Radio>
                  <Radio label="1" :disabled="isView&&isEdit">是</Radio>
                </RadioGroup>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="承租人姓名：">
                <Input
                  type="text"
                  v-model="createFinaForm.lessee"
                  :title="createFinaForm.lessee"
                  :disabled="isView&&isEdit"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
          </Row>
          <Row style="margin:5px 0;">
            <Col span="7">
              <FormItem label="处置商交接人员：">
                <Input
                  type="text"
                  v-model="createFinaForm.disposeHandover"
                  :title="createFinaForm.disposeHandover"
                  :disabled="isView&&isEdit"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="处置商身份证号：" prop="disposeCerNum">
                <Input
                  type="text"
                  v-model="createFinaForm.disposeCerNum"
                  :title="createFinaForm.disposeCerNum"
                  :disabled="isView&&isEdit"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="备注：">
                <Input
                  type="text"
                  v-model="createFinaForm.remark"
                  :title="createFinaForm.remark"
                  :disabled="isView&&isEdit"
                  placeholder="请输入..."
                ></Input>
              </FormItem>
            </Col>
          </Row>
        </Row>
        <h3 class="itemTitle" style="margin:20px 0;">
          <span></span>附件信息
        </h3>
        <div style="margin:20px;">
          <Table
            border
            :columns="fjCol"
            :data="fjData"
            :height="tableHeight"
            class="repaymentPlanT"
          ></Table>
        </div>
      </Form>
      <h3 class="itemTitle" style="margin:20px 0;"  v-if="$route.query.type=='edit' || $route.query.type=='view'">
        <span></span>出入库记录
      </h3>
      <div style="margin:20px;" v-if="$route.query.type=='edit' || $route.query.type=='view'">
        <Table
          border
          :columns="logCol"
          :data="logData"
          :height="tableHeight"
          class="repaymentPlanT"
        ></Table>
        <!-- <Page
          :total="total"
          @on-page-size-change="onPageSizeChange"
          @on-change="onPageChange"
          :page-size-opts="pageSizeOpts"
          show-sizer
          show-elevator
          show-total
          class="marginT10"
        />-->
      </div>
      <Row>
        <Col span="24" class="textCenter marginB6" style="padding-bottom:10px;">
          <Button
            size="large"
            @click="createBtn"
            type="primary"
            v-if="$route.query.type=='add'"
          >保存</Button>
          <Button
            size="large"
            @click="editBtn"
            type="primary"
            v-if="$route.query.type=='edit'"
          >编辑</Button>
          <Button size="large" @click="$router.push('/vehicleList')">返回</Button>
        </Col>
      </Row>
    </div>
  </div>
</template>
<script>
export default {
  name: "vehicleDetails",
  data() {
    return {
      isEdit:true,//可编辑区域
      isView: false, //是否是查看
      exportLoad: false,
      changetitle: "新建",
      createCarModal: false, //车库模态窗
      leasePropertyList:[],
      vehicleSourceList: [],
      propertyRightTypeList: [],
      sgGarageNameList: [],
      statList: [],
      businessTypeList: [],
      brandList: [],
      brandModelList: [],
      vehicleClassList: [],
      modelList: [],
      createFinaForm: {
        id:'',
        vin: "",
        licNum: "",
        vehicleSource: "",
        propertyRightType: "",
        brand: "",
        brandStr: "",
        brandModel: "",
        brandModelStr: "",
        vehicleClass: "",
        vehicleClassStr:'',
        model: "",
        modelStr:'',
        color: "",
        alixNum: "",
        sgGarageInfoId: "",
        mileage: 0,
        stat: 0,
        leaseProperty:'',
        businessType: "",
        videoNumber: "",
        isKey: "",
        isDrivingLicense: "",
        isNewKey: "",
        isLicnumCar: "",
        isNormalStart: "",
        isBatteryWork: "",
        isDragGarage: "",
        isSpare: "",
        isTyreModel: "",
        leftFrontNum: "",
        leftRearNum: "",
        rightFrontNum: "",
        rightRearNum: "",
        oilQuantity: "",
        vehicleCross: "",
        damageDesc: "",
        removeGps: "",
        lessee: "",
        disposeHandover: "",
        disposeCerNum: "",
        remark: "",
        vehcleAttList: [],
        attIds:[],
        delAttIds:[],
      },
      total: 0,
      current: 1,
      rowCount: 10,
      deleteArr:[],
      fjCol: [
        {
          title: "序号",
          type: "index",
          width: 80
        },
        {
          title: "文件名称",
          key: "sourceAttchName"
        },
        {
          title: "是否必填",
          key: "required",
          width: 100,
          render: (h, params) => {
            return h(
              "a",
              {
                props: {
                  value: params.row.sourceAttchName
                },
                style: {
                  display: "inline-block",
                  color: "red",
                }
              },
              "是"
            );
          }
        },
        {
          title: "附件",
          key: "vehcleAttList",
          className: "REQUIRED",
          render: (h,params) =>{
            let _self = this;
            return params.row.vehcleAttList.map((item,index) =>{
              return h('div', [
                  h('a', {
                    style: {
                        display: "inline-block",
                        color: "#2d8cf0",
                        cursor: "pointer"
                      },
                    on:{
                      click:()=>{
                        this.seeIphone(item.viewUrl);
                      }
                    }
                  },item.sourceAttchName),
                  h('span', {
                    style: {
                        display: "inline-block",
                        color: "red",
                        marginLeft: "5px",
                        cursor: "pointer",
                        display:this.$route.query.type=='view'||this.$route.query.type=='edit'?"none":"inline-block",
                      },
                    on:{
                      click:()=>{
                        _self.createFinaForm.delAttIds.push(this.fjData[0].vehcleAttList[index].id);
                        _self.fjData[0].vehcleAttList.splice(index,1)
                      }
                    }
                  },'删除'),
              ]);              
            })
          }
        },
        {
          title: "操作",
          key: "action",
          width: 150,
          align: "center",
          fixed: "right",
          render: (h, params) => {
            var that = this;
            var sh = h(
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
                          for (let n in that.fjData) {
                            that.fjData[n].vehcleAttList.push(res.data)
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
                  display: "inline-block",
                  color: "#2d8cf0",
                  cursor: "pointer"
                }
              },
              "上传"
            );
            if(this.$route.query.type == 'view'){
              return  h("span", '')
            }else{
              return h("span", [sh]);
            }
            
          }
        }
      ],
      fjData: [
        {
          sourceAttchName: "入库单",
          required: "是",
          vehcleAttList: []
        }
      ],
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
      pageSizeOpts: [10, 20, 50, 100],
      tableHeight: 0,
      createFinaRule: {
        vin: [
          {
            required: true,
            message: "请输入车架号",
            // pattern: /^[\u4e00-\u9fa5]+$/,
            trigger: "change"
          }
        ],
        licNum: [
          {
            required: true,
            message: "请输入车牌号",
            trigger: "change"
          }
        ],
        vehicleSource: [
          {
            required: true,
            message: "请选择车辆来源",
            trigger: "change"
          }
        ],
        propertyRightType: [
          {
            required: true,
            message: "请选择车辆类型",
            trigger: "change"
          }
        ],
        brand: [
          {
            required: true,
            message: "请选择品牌",
            trigger: "change"
          }
        ],
        brandModel: [
          {
            required: true,
            message: "请选择子品牌",
            trigger: "change"
          }
        ],
        vehicleClass: [
          {
            required: true,
            message: "请选择车型",
            trigger: "change"
          }
        ],
        model: [
          {
            required: true,
            message: "请选择车款",
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
        alixNum: [
          {
            required: true,
            message: "请填写申请编号",
            trigger: "change"
          }
        ],
        sgGarageInfoId: [
          {
            required: true,
            message: "请选择所在车库",
            trigger: "change"
          }
        ],
        mileage: [
          {
            required: true,
            type:'number',
            message: "请填写里程",          
            trigger: "change"
          },
          // {message: '请填写数字',pattern: /^\d+(\.\d+)?$/,trigger: 'change' }
        ],
        stat: [
          {
            required: true,
            type: 'number',
            message: "请选择车辆状态",
            trigger: "change"
          }
        ],
        leaseProperty: [
          {
            required: true,
            message: "请选择租赁属性",
            trigger: "change"
          }
        ],
        businessType: [
          {
            required: true,
            message: "请选择业务类型",
            trigger: "change"
          }
        ],
        // videoNumber: [
        //   {
        //     required: true,
        //     message: "请填写视频编号",
        //     trigger: "change"
        //   }
        // ],
        // lessee: [
        //   {
        //     required: true,
        //     message: "请填写承租人姓名",
        //     trigger: "change"
        //   }
        // ],
        // disposeHandover: [
        //   {
        //     required: true,
        //     message: "请填写处置商交接人员",
        //     trigger: "change"
        //   }
        // ],
        disposeCerNum: [
          {
            required: false,
            message: "请填写正确身份证号格式",
            pattern: /^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/,
            trigger: "blur"
          }
        ]
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
    seeIphone(attchPath) {
      //查看图片
       window.open(attchPath);
    },
    createBtn() {
      //保存新建
      this.$refs["createFinaForm"].validate(valid => {
        if (valid) {
          let flag = null;
          let self = this;
          if (!self.fjData[0].vehcleAttList.length) {
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
      for(let i=0;i<this.fjData[0].vehcleAttList.length;i++){
        list.attIds.push(this.fjData[0].vehcleAttList[i].id);
      }
      this.$http.post("/garage/sgVehicleInfo/create", list).then(function(res) {
        if (res.data.success == true) {
          this.$Modal.success({
            title: "提示",
            content: res.data.data,
            onOk: () => {
              this.$router.push("/vehicleList");
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
      // this.$refs["createFinaForm"].validate(valid => {
      //   if (valid) {
          let flag = null;
          let self = this;
          if (!self.fjData[0].vehcleAttList.length) {
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
      //   } else {
      //   }
      // });
    },
    saveEdit() {
      //编辑
      let list = Object.assign({}, list, this.createFinaForm);    
      for(let i=0;i<this.fjData[0].vehcleAttList.length;i++){
        list.attIds.push(this.fjData[0].vehcleAttList[i].id);
      }
      list.id = this.$route.query.id;
      this.$http.post("/garage/sgVehicleInfo/update", list).then(function(res) {
        if (res.data.success == true) {
          this.$Modal.success({
            title: "提示",
            content: res.data.data,
            onOk: () => {
              this.$router.push("/vehicleList");
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
    // onPageChange(page) {
    //   this.current = page;
    //   this.getDetails();
    // },
    // onPageSizeChange(size) {
    //   this.current = 1;
    //   this.rowCount = size;
    //   this.getDetails();
    // },
    getDetails() {
      //获取列表数据
      let id = this.$route.query.id;
      let list = {
        vehicleId: this.$route.query.id
      };
      this.$http
        .post("/garage/sgVehicleInfo/getBill?vehicleId=" + id)
        .then(function(res) {
          if (res.data.success == true) {
            this.$refs['createFinaForm'].resetFields();
            for (let i in res.data.data) {
              for (let j in this.createFinaForm) {
                if (i == j) {
                  this.createFinaForm[j] = res.data.data[i];
                  this.fjData[0].vehcleAttList = res.data.data.vehcleAttList;                  
                }
              }
            }
            
            if(!this.createFinaForm.attIds){
              this.createFinaForm.attIds = []; 
            }   
            if(this.createFinaForm.isKey>= '4'){
              this.createFinaForm.isKey = '4';
            }        
            //this.getBrandModelListFn(this.createFinaForm.brand);
            //this.getVehicleClassListFn(this.createFinaForm.brandModel);
            //this.getModelListFn(this.createFinaForm.vehicleClass);
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
        vehicleId: this.$route.query.id
      };
      this.$http
        .post("/garage/sgVehicleLog/pageQueryLog", list)
        .then(function(res) {
          if (res.data.success == true) {
            this.logData = res.data.data.records;
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
        .post(
          "/garage/enum/getEnumDataList?enumName=garage.SgVehicleSourceEnum"
        )
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
        .post(
          "/garage/enum/getEnumDataList?enumName=garage.SgVehicleStatusEnum"
        )
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
    getleasePropertyFn() {
      //租赁属性
      this.$http
        .post("/garage/enum/getEnumDataList?enumName=garage.LeasePropertyEnum")
        .then(function(res) {
          if (res.data.success == true) {
            this.leasePropertyList = res.data.data;
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
    getBrandModelId(val) {
      if (val) {
        this.createFinaForm.brandModel = '';
        this.createFinaForm.vehicleClass = '';
        this.createFinaForm.model = ''; 
        this.createFinaForm.brandStr = val.label.slice(1);
        this.getBrandModelListFn(val.value);
      }
    },
    getVehicleClassId(val) {
      if (val) {
        this.createFinaForm.vehicleClass = '';
        this.createFinaForm.model = ''; 
        this.createFinaForm.brandModelStr = val.label;
        this.getVehicleClassListFn(val.value);
      }
    },
    getModelId(val) {
      if (val) {
        this.createFinaForm.model = ''; 
        this.createFinaForm.vehicleClassStr = val.label;
        this.getModelListFn(val.value);
      }
    },
    getModelName(val) {
      if (val) {
        this.createFinaForm.modelStr = val.label;
      }
    },
    getBrandModelListFn(id) {
      //子品牌下拉
      this.$http.get("/garage/carDict/listBrand/" + id).then(function(res) {
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
        .get("/garage/carDict/listModel/" + id)
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
        .get("/garage/carDict/listStyle/" + id)
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
    }
  },
  mounted() {
    if (this.$route.query.type == "view") {
      this.isView = true;
      this.getDetails();
      this.getlog();
    }
    if(this.$route.query.type=="edit"){
      this.isView = true;
      this.isEdit = false;
      this.getDetails();
      this.getlog();
    }
    this.getVehicleSourceListFn();
    this.getPropertyRightTypeListFn();
    this.getSgGarageNameListFn();
    this.getStatListFn();
    this.getBusinessTypeListFn();
    this.getleasePropertyFn();
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
.ivu-table th.REQUIRED .ivu-table-cell span::before {
  content: "*";
  display: inline-block;
  margin-right: 4px;
  line-height: 1;
  font-family: SimSun;
  font-size: 12px;
  color: #ed4014;
}
</style>
