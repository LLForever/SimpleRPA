<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="任务ID" prop="taskId">
                <el-input
                    v-model="queryParams.taskId"
                    placeholder="请输入任务ID"
                    clearable
                    size="small"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="任务名称" prop="taskName">
                <el-input
                    v-model="queryParams.taskName"
                    placeholder="请输入任务名称"
                    clearable
                    size="small"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
                <el-button
                    type="primary"
                    plain
                    icon="el-icon-plus"
                    size="mini"
                    @click="handleAdd"
                >新增</el-button>
            </el-form-item>
        </el-form>

        <el-divider>个人分享中心</el-divider>

        <el-row :gutter="10" style="margin-top: 10px">
            <el-col v-for="item of RpaTaskCooperationList" :key="item.id" :span="4" style="margin-bottom: 20px">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span style="font-weight: bold">{{ item.taskName }}</span>
                        <el-button style="float: right; padding: 3px 0" type="text" @click="handleDelete(item)">删除</el-button>
                    </div>
                    <div style="margin-bottom: 10px">
                        {{'任务ID：' + item.taskId }}
                    </div>
                    <div style="margin-bottom: 10px">
                        {{'任务分享时间：' + item.importTime }}
                    </div>
                    <div style="margin-bottom: 10px">
                        {{'状态：' }}
                        <el-tag v-if="item.coEnable" type="success">协同编辑开启</el-tag>
                        <el-tag v-else type="danger">协同编辑关闭</el-tag>
                    </div>
                    <el-row style="margin-bottom: 10px">
                        <el-button size="small" round type="info" icon="el-icon-refresh" @click="changeCoEnableStatus(item)">转换协同编辑状态</el-button>
                    </el-row>
                </el-card>
            </el-col>
        </el-row>

        <el-divider>云协同编辑空间</el-divider>

        <el-row :gutter="10" style="margin-top: 10px">
            <el-col v-for="item of CloudRpaTaskCooperationList" :key="item.id" :span="4" style="margin-bottom: 20px">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span style="font-weight: bold">{{ item.taskName }}</span>
                    </div>
                    <div style="margin-bottom: 10px">
                        {{'任务ID：' + item.taskId }}
                    </div>
                    <div style="margin-bottom: 10px">
                        {{'任务分享时间：' + item.importTime }}
                    </div>
                    <el-row style="margin-bottom: 10px">
                        <el-button size="small" round type="info" icon="el-icon-edit" @click="openTask(item)">编辑任务</el-button>
                    </el-row>
                </el-card>
            </el-col>
        </el-row>

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

        <pagination
            v-show="total>0"
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="getList"
        />
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
            RpaTaskCooperationList: [],
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
        this.getList();
    },
    methods: {
        /** 查询RPA任务协作列表 */
        getList() {
            this.loading = true;
            listRpaTaskCooperation(this.queryParams).then(response => {
                this.RpaTaskCooperationList = response.rows;
                this.total = response.total;
                this.loading = false;
                console.log(this.RpaTaskCooperationList);
            });
            enableListRpaTaskCooperation(this.queryParams).then(res => {
                this.CloudRpaTaskCooperationList = res.rows
            })
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
            const ids = row.id || this.ids;
            this.$modal.confirm('是否确认删除RPA任务协作编号为"' + ids + '"的数据项？').then(function() {
                return delRpaTaskCooperation(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => {});
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
