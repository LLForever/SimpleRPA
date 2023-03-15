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
            <el-form-item label="创建时间" prop="creationTime">
                <el-date-picker clearable size="small"
                                v-model="queryParams.creationTime"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="选择任务创建时间">
                </el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
                <el-button
                    type="danger"
                    plain
                    icon="el-icon-delete"
                    size="mini"
                    :disabled="multiple"
                    @click="handleDelete"
                >删除
                </el-button>
            </el-form-item>
        </el-form>

        <el-table v-loading="loading" :data="RpaTaskScheduleList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"/>
            <el-table-column label="任务ID" align="center" prop="taskId"/>
            <el-table-column label="任务名称" align="center" prop="taskName"/>
            <el-table-column label="创建任务的用户ID" align="center" prop="userId"/>
            <el-table-column label="任务创建时间" align="center" prop="creationTime" width="180">
                <template slot-scope="scope">
                    <span>{{ parseTime(scope.row.creationTime, '{y}-{m}-{d}') }}</span>
                </template>
            </el-table-column>
            <el-table-column label="计划含义(中文)" align="center" prop="scheduleMeaning"/>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button
                        size="mini"
                        type="text"
                        @click="handleShow(scope.row)"
                    >近一次执行反馈信息
                    </el-button>
                    <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-delete"
                        @click="handleDelete(scope.row)"
                    >删除
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

        <!-- 添加或修改rpa任务计划详情对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>

        <el-dialog title="历史执行结果" :visible.sync="processVisual" width="1200px" append-to-body>
            <el-divider>近一次系统执行日志信息</el-divider>
            <div style="width: 100%; text-align: center">
                <el-row>
                    <el-alert
                        :title="tipsTitleContent"
                        type='info'
                        description="Powered by Simple RPA"
                        :closable="false"
                        center
                    >
                    </el-alert>
                </el-row>
            </div>
            <el-divider>任务历史信息</el-divider>
            <div style="width: 100%; text-align: center">
                <el-row>
                    <div id="processBar" style="margin-top: 10px"></div>
                </el-row>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="processVisual = false">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {
    listRpaTaskSchedule,
    getRpaTaskSchedule,
    delRpaTaskSchedule,
    addRpaTaskSchedule,
    updateRpaTaskSchedule
} from "@/api/rpa/RpaTaskSchedule";
import {get_recently_exec_log, getSuccessRate, query_most_error} from "@/api/rpa/RpaAssist";
import * as echarts from "echarts";

export default {
    name: "RpaTaskSchedule",
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
            // rpa任务计划详情表格数据
            RpaTaskScheduleList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                taskId: null,
                taskName: null,
                userId: null,
                creationTime: null,
                scheduleMeaning: null
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                taskId: [
                    {required: true, message: "任务ID不能为空", trigger: "blur"}
                ],
                taskName: [
                    {required: true, message: "任务名称不能为空", trigger: "blur"}
                ],
            },
            processVisual: false,
            tipsTitleContent: ''
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询rpa任务计划详情列表 */
        getList() {
            this.loading = true;
            listRpaTaskSchedule(this.queryParams).then(response => {
                this.RpaTaskScheduleList = response.rows;
                this.total = response.total;
                this.loading = false;
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
                taskName: null,
                userId: null,
                creationTime: null,
                scheduleMeaning: null
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
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加rpa任务计划详情";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getRpaTaskSchedule(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改rpa任务计划详情";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateRpaTaskSchedule(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addRpaTaskSchedule(this.form).then(response => {
                            this.$modal.msgSuccess("新增成功");
                            this.open = false;
                            this.getList();
                        });
                    }
                }
            });
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const ids = row.id || this.ids;
            this.$modal.confirm('是否确认删除rpa任务计划详情编号为"' + ids + '"的数据项？').then(function () {
                return delRpaTaskSchedule(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => {
            });
        },
        handleShow(row){
            this.processVisual = true
            get_recently_exec_log(row.taskId).then((res)=>{
                if(res.data === undefined){
                    this.tipsTitleContent = '任务暂未存在执行记录信息'
                    return
                }
                if(res.data.taskMsg === 'exec success'){
                    this.tipsTitleContent = '任务执行成功！未存在执行错误'
                }else{
                    this.tipsTitleContent = '该任务执行节点ID【' + res.data.nodeId + '】执行出错，执行出错信息为：' + res.data.taskMsg
                }
            })
            getSuccessRate(row.taskId).then(res => {
                this.initSuccessRate(res.data)
            })
        },
        initSuccessRate(successRate) {
            const myChart = echarts.init(document.getElementById('processBar'))
            myChart.resize(
                {
                    width: 1200,
                    height: 400
                }
            )
            const normalExec = '正常运行(' +  successRate.successNum + '次)', errorExec = '运行异常(' + successRate.errorNum + '次)'
            // 配置项
            var option = {
                title: {
                    show: false,
                    text: '任务执行历史'
                },
                series: [
                    {
                        type: 'pie',
                        stillShowZeroSum: false,
                        data: [
                            {
                                value: successRate.successNum,
                                name: normalExec
                            },
                            {
                                value: successRate.errorNum,
                                name: errorExec
                            }
                        ],
                        radius: '50%'
                    }
                ]
            }
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option)
        }
    }
};
</script>
