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

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button
                    type="primary"
                    plain
                    icon="el-icon-plus"
                    size="mini"
                    @click="handleAdd"
                >新增
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    type="success"
                    plain
                    icon="el-icon-edit"
                    size="mini"
                    :disabled="single"
                    @click="handleUpdate"
                >修改
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    type="danger"
                    plain
                    icon="el-icon-delete"
                    size="mini"
                    :disabled="multiple"
                    @click="handleDelete"
                >删除
                </el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="TaskDetailList" @selection-change="handleSelectionChange"
                  :row-class-name="tableRowClassName">
            <el-table-column type="selection" width="55" align="center"/>
            <el-table-column label="任务ID" align="center" prop="taskId"/>
            <el-table-column label="任务名称" align="center" prop="taskName"/>
            <el-table-column label="任务执行状态" align="center" prop="taskStatus"/>
            <el-table-column label="任务执行进度" align="center" prop="taskProgress"/>
            <el-table-column label="任务版本号" align="center" prop="taskVersion"/>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-edit"
                        @click="openPanelDetail(scope.row)"
                    >打开面板
                    </el-button>
                    <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-s-platform"
                        @click="openVisualDetail(scope.row)"
                    >可视化
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

        <!-- 添加或修改rpa面板任务详情对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="任务名称" prop="taskName">
                    <el-input v-model="form.taskName" placeholder="请输入任务名称"/>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>

        <el-dialog title="可视化" :visible.sync="processVisual" width="1200px" append-to-body>
            <el-divider>任务当前执行信息</el-divider>
            <div style="width: 100%; text-align: center">
                <el-row>
                    <el-progress type="circle" :percentage="processNumber" :status="processStatusName"></el-progress>
                </el-row>
                <el-row>
                    <el-alert
                        :title="tipsTitleContent"
                        :type="tipsStatusName"
                        description="Powered by Simple RPA"
                        show-icon
                        center
                        :closable="false"
                    >
                    </el-alert>
                </el-row>
            </div>
            <el-divider>任务历史信息</el-divider>
            <el-row>
                <el-col :span="12">
                    <div id="processBar" style="margin-top: 10px"></div>
                </el-col>
                <el-col :span="12">
                    <div id="errorInfo" style="margin-top: 10px"></div>
                </el-col>
            </el-row>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="processVisual = false">确 定</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
import {listTaskDetail, getTaskDetail, delTaskDetail, addTaskDetail, updateTaskDetail} from "@/api/rpa/TaskDetail";

import * as echarts from 'echarts'
import {getSuccessRate, query_most_error} from "@/api/rpa/RpaAssist";

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
            title: "",
            // 是否显示弹出层
            open: false,
            processVisual: false,
            processStatusName: '',
            processNumber: null,
            tipsStatusName: '',
            tipsTitleContent: '',
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                taskId: null,
                taskStatus: null,
                taskName: null,
                taskProgress: null,
                taskVersion: null
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
                ]
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询rpa面板任务详情列表 */
        getList() {
            this.loading = true;
            listTaskDetail(this.queryParams).then(response => {
                this.TaskDetailList = response.rows;
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
                taskId: null,
                taskProgress: null,
                taskVersion: null,
                taskName: null
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
            this.title = "添加rpa面板任务详情";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getTaskDetail(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改rpa面板任务详情";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateTaskDetail(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addTaskDetail(this.form).then(response => {
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
            this.$modal.confirm('是否确认删除rpa面板任务详情编号为"' + ids + '"的数据项？').then(function () {
                return delTaskDetail(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => {
            });
        },
        tableRowClassName({row, rowIndex}) {
            if (row.taskStatus === 'warning') {
                return 'warning-row';
            } else if (row.taskStatus === 'completed') {
                return 'success-row';
            } else if (row.taskStatus === 'error') {
                return 'error-row';
            }
            return '';
        },
        openPanelDetail(row) {
            this.$router.push({
                name: 'panel_detail',
                params: {
                    panelDetailData: row
                }
            });
        },
        openVisualDetail(row) {
            this.processVisual = true
            this.$nextTick(() => {
                if (row.taskStatus === 'completed') {
                    this.processNumber = 100
                    this.processStatusName = 'success'
                    this.tipsStatusName = 'success'
                    this.tipsTitleContent = '任务成功执行完毕！'
                } else if (row.taskStatus === 'running') {
                    const pro = Math.floor(Math.random() * 80 + 19)
                    this.processNumber = pro
                    this.processStatusName = ''
                    this.tipsStatusName = 'info'
                    this.tipsTitleContent = '任务正在执行中！进度为：' + pro + '%'
                } else if (row.taskStatus === 'error') {
                    this.processNumber = Math.floor(Math.random() * 80 + 19)
                    this.processStatusName = 'exception'
                    this.tipsStatusName = 'error'
                    this.tipsTitleContent = '任务此次执行出错，请查看任务执行日志！'
                } else if (row.taskStatus === 'created') {
                    this.processNumber = 0
                    this.processStatusName = ''
                    this.tipsStatusName = 'info'
                    this.tipsTitleContent = '任务暂未执行！'
                }
                query_most_error(row.taskId).then(error_res => {
                    this.init_query_most_error(error_res.data, row.nodeList)
                })
                getSuccessRate(row.taskId).then(res => {
                    this.initSuccessRate(res.data)
                })
            })
        },
        init_query_most_error(data, nodeList) {
            const myChart = echarts.init(document.getElementById('errorInfo'))
            myChart.resize(
                {
                    width: 400,
                    height: 400
                }
            )
            var x_data = []
            var s_data = []
            for (const item of data) {
                for (const node of nodeList) {
                    if (node.id == item.nodeId) {
                        x_data.push(node.name)
                        break;
                    }
                }
                s_data.push(item.errorNum)
                if (x_data.length !== s_data.length) {
                    x_data.push(item.nodeId)
                }
            }

            var option = {
                title: {
                    show: true,
                    text: '异常组件排名(异常次数)'
                },
                xAxis: {
                    data: x_data,
                    axisLabel: {
                        interval: 0,
                        formatter: function (params) {
                            var newParamsName = ''
                            const paramsNameNumber = params.length
                            const provideNumber = 6 // 单行显示文字个数
                            const rowNumber = Math.ceil(paramsNameNumber / provideNumber)
                            if (paramsNameNumber > provideNumber) {
                                for (let p = 0; p < rowNumber; p++) {
                                    var tempStr = ''
                                    var start = p * provideNumber
                                    var end = start + provideNumber
                                    if (p === rowNumber - 1) {
                                        tempStr = params.substring(start, paramsNameNumber)
                                    } else {
                                        tempStr = params.substring(start, end) + '\n'
                                    }
                                    newParamsName += tempStr
                                }
                            } else {
                                newParamsName = params
                            }
                            return newParamsName
                        }
                    }
                },
                yAxis: {
                },
                series: [
                    {
                        type: 'bar',
                        data: s_data
                    }
                ]
            }
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option)
        },
        initSuccessRate(successRate) {
            const myChart = echarts.init(document.getElementById('processBar'))
            myChart.resize(
                {
                    width: 500,
                    height: 400
                }
            )
            const normalExec = '正常运行(' +  successRate.successNum + '次)', errorExec = '运行异常(' + successRate.errorNum + '次)'
            // 配置项
            var option = {
                title: {
                    show: true,
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

<style>
.el-table .warning-row {
    background: oldlace;
}

.el-table .error-row {
    background: #ffe0e0;
}

.el-table .success-row {
    background: #f0f9eb;
}
</style>
