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
            </el-form-item>
        </el-form>

        <el-table v-loading="loading" :data="TaskDetailList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"/>
            <el-table-column label="任务ID" align="center" prop="taskId"/>
            <el-table-column label="任务名称" align="center" prop="taskName"/>
            <el-table-column label="任务版本号" align="center" prop="taskVersion"/>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-edit"
                        @click="execute_rec(scope.row)"
                    >执行恢复
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

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
import * as echarts from 'echarts'
import {get_rec_list, getSuccessRate, query_most_error} from "@/api/rpa/RpaAssist";
import {addRpaTaskSchedule} from "@/api/rpa/RpaTaskSchedule";
import {getOneTaskDetail} from "@/api/rpa/RpaTaskCooperation";
import {sendRunTaskSig} from "@/api/rpa/task";

export default {
    name: "TaskDetail",
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
            // rpa面板任务详情表格数据
            TaskDetailList: [],
            // 弹出层标题
            title: "设定执行计划",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                taskId: null,
                taskName: null
            },
            // 表单参数
            form: {}
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询rpa面板任务详情列表 */
        getList() {
            this.loading = true;
            get_rec_list().then(response => {
                this.TaskDetailList = response.rows;
                this.total = response.total;
                this.loading = false;
            });
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.resetDialogForm()
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
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        execute_rec(row){
            getOneTaskDetail(row.taskId).then(res => {
                sendRunTaskSig(res).then(a => {
                    this.$message.success("恢复执行成功！");
                });
            })
        }
    }
};
</script>
