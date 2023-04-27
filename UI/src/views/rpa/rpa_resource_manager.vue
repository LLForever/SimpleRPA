<template>
    <div class="app-container">

        <el-row :gutter="10" style="margin-top: 10px">
            <el-col v-for="item of RpaTaskCooperationList" :key="item.id" :span="5" style="margin-bottom: 20px">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span style="font-weight: bold">{{ item.taskName }}</span>
                        <el-button v-if="item.enable" style="float: right; padding: 3px 0" type="text" @click="handleDelete(item)">停用</el-button>
                        <el-button v-else style="float: right; padding: 3px 0" type="text" @click="handleStartMachine(item)">启用</el-button>
                    </div>
                    <el-row style="margin-bottom: 10px">
                        <el-col :span="6">
                            {{'CPU：' }}
                        </el-col>
                        <el-col :span="18">
                            <el-progress :percentage="item.cpu_percentage" :color="customColors"></el-progress>
                        </el-col>
                    </el-row>
                    <el-row style="margin-bottom: 10px">
                        <el-col :span="6">
                            {{'内存：' }}
                        </el-col>
                        <el-col :span="18">
                            <el-progress :percentage="item.mem_percentage" :color="customColors"></el-progress>
                        </el-col>
                    </el-row>
                    <el-row style="margin-bottom: 10px">
                        <el-col :span="6">
                            {{'网络：' }}
                        </el-col>
                        <el-col :span="18">
                            <el-progress :percentage="item.net_percentage" :color="customColors"></el-progress>
                        </el-col>
                    </el-row>
                    <el-divider/>
                    <el-row style="margin-bottom: 10px;">
                        <el-descriptions>
                            <el-descriptions-item label="任务">
                                <el-tag v-if="item.isRunning" type="warning" size="small">该节点正在执行任务</el-tag>
                                <el-tag v-else type="success" size="small">该节点无任务执行</el-tag>
                            </el-descriptions-item>
                        </el-descriptions>
                    </el-row>
                    <el-row style="margin-bottom: 10px">
                        <el-descriptions>
                            <el-descriptions-item label="等待任务数量">
                                <span style="color: #409EFF">
                                    {{ item.queue }}
                                </span>
                            </el-descriptions-item>
                        </el-descriptions>
                    </el-row>
                    <el-row style="margin-bottom: 10px">
                        <el-descriptions>
                            <el-descriptions-item label="机器状态">
                                <i v-if="item.enable" class="el-icon-s-platform" style="color: #67C23A;font-size: 18px"></i>
                                <i v-else class="el-icon-s-platform" style="color: #F56C6C;font-size: 18px"></i>
                            </el-descriptions-item>
                        </el-descriptions>
                    </el-row>
                </el-card>
            </el-col>
        </el-row>

        <el-divider></el-divider>

        <el-card class="box-card">
            <el-divider>集群资源情况</el-divider>
            <el-row>
                <el-col :span="8">
                    <iframe src="http://192.168.103.116:30300/d-solo/SnndmSBVk/kubernetes-cluster-monitoring-via-prometheus?orgId=1&refresh=10s&from=1679476849219&to=1679477149219&var-Node=All&panelId=4" width="450" height="200" frameborder="0"></iframe>
                </el-col>
                <el-col :span="8">
                    <iframe src="http://192.168.103.116:30300/d-solo/SnndmSBVk/kubernetes-cluster-monitoring-via-prometheus?orgId=1&refresh=10s&from=1679478887019&to=1679479187020&var-Node=All&panelId=6" width="450" height="200" frameborder="0"></iframe>
                </el-col>
                <el-col :span="8">
                    <iframe src="http://192.168.103.116:30300/d-solo/SnndmSBVk/kubernetes-cluster-monitoring-via-prometheus?orgId=1&refresh=10s&from=1679478912905&to=1679479212905&var-Node=All&panelId=7" width="450" height="200" frameborder="0"></iframe>
                </el-col>
            </el-row>
            <el-divider>各节点资源变化图</el-divider>
            <el-row>
                <el-col :span="8">
                    <iframe src="http://192.168.103.116:30300/d-solo/H62-FCS4z/prometheus-2-0-stats?orgId=1&from=1679480388464&to=1679483988464&panelId=3" width="450" height="200" frameborder="0"></iframe>
                </el-col>
                <el-col :span="8">
                    <iframe src="http://192.168.103.116:30300/d-solo/H62-FCS4z/prometheus-2-0-stats?orgId=1&from=1679480388464&to=1679483988464&panelId=16" width="450" height="200" frameborder="0"></iframe>
                </el-col>
                <el-col :span="8">
                    <iframe src="http://192.168.103.116:30300/d-solo/H62-FCS4z/prometheus-2-0-stats?orgId=1&from=1679480388464&to=1679483988464&panelId=29" width="450" height="200" frameborder="0"></iframe>
                </el-col>
            </el-row>
        </el-card>

        <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="任务ID" prop="taskId">
                    <el-input v-model="form.taskId" placeholder="请输入任务ID" />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {
    listRpaTaskCooperation,
    getRpaTaskCooperation,
    delRpaTaskCooperation,
    addRpaTaskCooperation,
    updateRpaTaskCooperation,
    changeRpaTaskCooperationStatus, enableListRpaTaskCooperation, getOneTaskDetail
} from "@/api/rpa/RpaTaskCooperation";

import axios from 'axios'
import {floor} from "lodash";
import {add_server} from "@/api/rpa/RpaTaskSchedule";

export default {
    name: "RpaTaskCooperation",
    data() {
        return {
            // 遮罩层
            loading: true,
            // 选中数组
            ids: [],
            // 非单个禁用
            single: true,
            // 非多个禁用
            multiple: true,
            // 显示搜索条件
            showSearch: true,
            // 总条数
            total: 0,
            // RPA任务协作表格数据
            RpaTaskCooperationList: [
                {
                    id: 1,
                    taskName: '虚拟机(1号机)',
                    enable: true,
                    cpu_percentage: 0,
                    mem_percentage: 0,
                    net_percentage: 0,
                    isRunning: false,
                    queue: 0
                },
                {
                    id: 2,
                    taskName: '虚拟机(2号机)',
                    enable: true,
                    cpu_percentage: 0,
                    mem_percentage: 0,
                    net_percentage: 0,
                    isRunning: false,
                    queue: 0
                },
                {
                    id: 3,
                    taskName: '虚拟机(3号机)',
                    enable: true,
                    cpu_percentage: 0,
                    mem_percentage: 0,
                    net_percentage: 0,
                    isRunning: false,
                    queue: 0
                },
                {
                    id: 4,
                    taskName: '虚拟机(4号机)',
                    enable: true,
                    cpu_percentage: 0,
                    mem_percentage: 0,
                    net_percentage: 0,
                    isRunning: false,
                    queue: 0
                }
            ],
            customColors: [
                {color: '#5cb87a', percentage: 20},
                {color: '#1989fa', percentage: 40},
                {color: '#6f7ad3', percentage: 60},
                {color: '#e6a23c', percentage: 80},
                {color: '#f56c6c', percentage: 100}
            ],
            CloudRpaTaskCooperationList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 50,
                taskId: null,
                userId: null,
                taskName: null,
                importTime: null
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                taskId: [
                    { required: true, message: "任务ID不能为空", trigger: "blur" }
                ],
                userId: [
                    { required: true, message: "导入此任务作为协作者的用户ID不能为空", trigger: "blur" }
                ],
            }
        };
    },
    created() {
    },
    mounted() {
        this.$nextTick(()=>{
            this.initData();
        })
    },
    methods: {
        /** 查询RPA任务协作列表 */
        initData() {
            this.getMachine116Info()
            this.getMachine117Info()
            this.getMachine118Info()
            this.getMachine99Info()
            this.getMachinePerform()
        },
        getMachine116Info(){
            axios({
                method: 'get',
                url: '/machine116/panel-task/getrm',
                withCredentials: false
            }).then((response) => {
                console.log(this.RpaTaskCooperationList[0]);
                this.RpaTaskCooperationList[0].isRunning = response.data.isRunning
                for(const key in response.data){
                    if(key.indexOf('queue') !== -1){
                        this.RpaTaskCooperationList[0].queue = parseInt(response.data[key])
                    }
                }
            });
        },
        getMachine117Info(){
            axios({
                method: 'get',
                url: '/machine117/panel-task/getrm',
                withCredentials: false
            }).then((response) => {
                this.RpaTaskCooperationList[1].isRunning = response.data.isRunning
                for(const key in response.data){
                    if(key.indexOf('queue') !== -1){
                        this.RpaTaskCooperationList[1].queue = parseInt(response.data[key])
                    }
                }
            });
        },
        getMachine118Info(){
            axios({
                method: 'get',
                url: '/machine118/panel-task/getrm',
                withCredentials: false
            }).then((response) => {
                this.RpaTaskCooperationList[2].isRunning = response.data.isRunning
                for(const key in response.data){
                    if(key.indexOf('queue') !== -1){
                        this.RpaTaskCooperationList[2].queue = parseInt(response.data[key])
                    }
                }
            });
        },
        getMachine99Info(){
            axios({
                method: 'get',
                url: '/machine99/panel-task/getrm',
                withCredentials: false
            }).then((response) => {
                this.RpaTaskCooperationList[3].isRunning = response.data.isRunning
                for(const key in response.data){
                    if(key.indexOf('queue') !== -1){
                        this.RpaTaskCooperationList[3].queue = parseInt(response.data[key])
                    }
                }
            });
        },
        getMachinePerform(){
            axios({
                method: 'get',
                url: '/get_machine_per/schedule/get_perform',
                withCredentials: false
            }).then((response) => {
                this.RpaTaskCooperationList[0].cpu_percentage = parseFloat(response.data.master.cpu).toFixed(1)
                this.RpaTaskCooperationList[0].mem_percentage = parseFloat(response.data.master.mem).toFixed(1)
                this.RpaTaskCooperationList[0].net_percentage = parseFloat(response.data.master.net).toFixed(1)

                this.RpaTaskCooperationList[1].cpu_percentage = parseFloat(response.data.node1.cpu).toFixed(1)
                this.RpaTaskCooperationList[1].mem_percentage = parseFloat(response.data.node1.mem).toFixed(1)
                this.RpaTaskCooperationList[1].net_percentage = parseFloat(response.data.node1.net).toFixed(1)

                this.RpaTaskCooperationList[2].cpu_percentage = parseFloat(response.data.node2.cpu).toFixed(1)
                this.RpaTaskCooperationList[2].mem_percentage = parseFloat(response.data.node2.mem).toFixed(1)
                this.RpaTaskCooperationList[2].net_percentage = parseFloat(response.data.node2.net).toFixed(1)

                this.RpaTaskCooperationList[3].cpu_percentage = parseFloat(response.data.node3.cpu).toFixed(1)
                this.RpaTaskCooperationList[3].mem_percentage = parseFloat(response.data.node3.mem).toFixed(1)
                this.RpaTaskCooperationList[3].net_percentage = parseFloat(response.data.node3.net).toFixed(1)
            });
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
                id: null,
                taskId: null,
                userId: null,
                taskName: null,
                importTime: null
            };
            this.resetForm("form");
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.pageNum = 1;
            this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.resetForm("queryForm");
            this.handleQuery();
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.ids = selection.map(item => item.id)
            this.single = selection.length!==1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加RPA任务协作";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getRpaTaskCooperation(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改RPA任务协作";
            });
        },
        /** 提交按钮 */
        submitForm() {
            addRpaTaskCooperation(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
            });
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            row.enable = false
            remove_server(row.id).then(res => {
                this.$message.success("停用机器成功！")
            })
        },
        handleStartMachine(row) {
            row.enable = true
            add_server(row.id).then(res => {
                this.$message.success("启用机器成功！")
            })
        },
        changeCoEnableStatus(row){
            changeRpaTaskCooperationStatus(row).then(res => {
                if(res.code === 200){
                    this.$message.success("转换成功");
                    this.resetQuery()
                }
            })
        },
        openTask(row){
            getOneTaskDetail(row.taskId).then(res => {
                this.$router.push({
                    name: 'panel_detail',
                    params: {
                        panelDetailData: res
                    }
                });
            })
        }
    }
};
</script>

<style>
</style>
