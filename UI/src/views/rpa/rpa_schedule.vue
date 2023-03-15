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
                        @click="setSchedule(scope.row)"
                    >设定执行计划
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
            <el-form ref="form" :model="form" label-width="80px">
                <el-form-item label="星期">
                    <el-cascader
                        v-model="week_value"
                        :options="week_options"
                        @change="week_change"></el-cascader>
                </el-form-item>

                <el-form-item label="月份">
                    <el-cascader
                        v-model="mon_value"
                        :options="mon_options"></el-cascader>
                </el-form-item>

                <el-form-item label="日">
                    <el-row :gutter="5">
                        <el-col :span="15">
                            <el-select v-model="day_value" placeholder="请选择" @change="day_change">
                                <el-option
                                    v-for="item in day_options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="1">
                            <span v-if="day_value === 'Day'">第</span>
                        </el-col>
                        <el-col :span="3">
                            <el-input v-model="day_input" v-if="day_value === 'Day'"></el-input>
                        </el-col>
                        <el-col :span="1">
                            <span v-if="day_value === 'Day'">天</span>
                        </el-col>
                    </el-row>
                </el-form-item>

                <el-form-item label="小时">
                    <el-row :gutter="5">
                        <el-col :span="15">
                            <el-select v-model="hour_value" placeholder="请选择">
                                <el-option
                                    v-for="item in hour_options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="1">
                            <span v-if="hour_value === 'Fixed'">第</span>
                        </el-col>
                        <el-col :span="3">
                            <el-input v-model="hour_input" v-if="hour_value === 'Fixed'"></el-input>
                        </el-col>
                        <el-col :span="3">
                            <span v-if="hour_value === 'Fixed'">小时</span>
                        </el-col>
                    </el-row>
                </el-form-item>

                <el-form-item label="分钟">
                    <el-row :gutter="5">
                        <el-col :span="15">
                            <el-select v-model="minute_value" placeholder="请选择">
                                <el-option
                                    v-for="item in minute_options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="1">
                            <span v-if="minute_value === 'Fixed'">第</span>
                        </el-col>
                        <el-col :span="3">
                            <el-input v-model="minute_input" v-if="minute_value === 'Fixed'"></el-input>
                        </el-col>
                        <el-col :span="3">
                            <span v-if="minute_value === 'Fixed'">分钟</span>
                        </el-col>
                    </el-row>
                </el-form-item>

                <el-form-item label="秒">
                    <el-row :gutter="5">
                        <el-col :span="15">
                            <el-select v-model="second_value" placeholder="请选择">
                                <el-option
                                    v-for="item in second_options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="1">
                            <span v-if="second_value === 'Fixed'">第</span>
                        </el-col>
                        <el-col :span="3">
                            <el-input v-model="second_input" v-if="second_value === 'Fixed'"></el-input>
                        </el-col>
                        <el-col :span="3">
                            <span v-if="second_value === 'Fixed'">秒</span>
                        </el-col>
                    </el-row>
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
import {listTaskDetail, getTaskDetail, delTaskDetail, addTaskDetail, updateTaskDetail} from "@/api/rpa/TaskDetail";

import * as echarts from 'echarts'
import {getSuccessRate, query_most_error} from "@/api/rpa/RpaAssist";
import {addRpaTaskSchedule} from "@/api/rpa/RpaTaskSchedule";

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
            form: {},
            mon_value: [],
            mon_options: [{
                value: 'None',
                label: '不指定'
            },{
                    value: 'month',
                    label: '指定月',
                    children: [{
                        value: 'Jan',
                        label: '一月'
                    },
                        {
                            value: 'Feb',
                            label: '二月'
                        },
                        {
                            value: 'Mar',
                            label: '三月'
                        },
                        {
                            value: 'Apr',
                            label: '四月'
                        },
                        {
                            value: 'May',
                            label: '五月'
                        },
                        {
                            value: 'June',
                            label: '六月'
                        },
                        {
                            value: 'July',
                            label: '七月'
                        },
                        {
                            value: 'Aug',
                            label: '八月'
                        },
                        {
                            value: 'Sep',
                            label: '九月'
                        },
                        {
                            value: 'Oct',
                            label: '十月'
                        },
                        {
                            value: 'Nov',
                            label: '十一月'
                        },
                        {
                            value: 'Dec',
                            label: '十二月'
                        }]
                }],
            week_value: [],
            week_options: [{
                value: 'None',
                label: '不指定'
            }, {
                    value: 'week',
                    label: '指定周',
                    children: [{
                        value: 'Mon',
                        label: '周一'
                    },
                        {
                            value: 'Tue',
                            label: '周二'
                        },
                        {
                            value: 'Wed',
                            label: '周三'
                        },
                        {
                            value: 'Thur',
                            label: '周四'
                        },
                        {
                            value: 'Fri',
                            label: '周五'
                        },
                        {
                            value: 'Sat',
                            label: '周六'
                        },
                        {
                            value: 'Sun',
                            label: '周天'
                        }]
                }],
            day_options: [{
                value: 'None',
                label: '不指定'
            }, {
                value: 'Day',
                label: '指定天'
            }],
            day_value: '',
            day_input: '',
            hour_input: '',
            hour_value: '',
            hour_options: [{
                value: 'None',
                label: '不指定'
            }, {
                value: 'Fixed',
                label: '指定小时'
            }],
            minute_input: '',
            minute_value: '',
            minute_options: [{
                value: 'None',
                label: '不指定'
            }, {
                value: 'Fixed',
                label: '指定分钟'
            }],
            second_input: '',
            second_value: '',
            second_options: [{
                value: 'None',
                label: '不指定'
            }, {
                value: 'Fixed',
                label: '指定秒'
            }],
            schedule_row: null
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
        setSchedule(row){
            this.open = true
            this.schedule_row = row
        },
        submitForm(){
            if(this.judgeDialogFormDataAvailable()){
                const Json = this.collectDialogFormData()
                var meaning = ''
                if(Json.mon.length < 2){
                    meaning += '每月'
                }else{
                    const m = '每年' + this.getMonthName(Json.mon[1]);
                    meaning += m
                }
                if(Json.week.length > 1){
                    const w = '每' + this.getWeekName(Json.week[1])
                    meaning += w
                }
                if(Json.day !== ''){
                    meaning += (Json.day + '日')
                }
                if(Json.day === '' && Json.week.length < 2){
                    meaning += '每天'
                }
                if(Json.hour === ''){
                    meaning += '每小时'
                }else{
                    meaning += (Json.hour + '点')
                }
                if(Json.minute === ''){
                    meaning += '每分钟'
                }else{
                    meaning += ('第' + Json.minute + '分钟')
                }
                if(Json.second === ''){
                    meaning += '每秒'
                }else{
                    meaning += ('第' + Json.hour + '秒')
                }
                meaning += ' 执行此任务'
                const schedule_data = {
                    taskId: this.schedule_row.taskId,
                    taskName: this.schedule_row.taskName,
                    userId: this.schedule_row.userId,
                    scheduleMeaning: meaning
                }
                addRpaTaskSchedule(schedule_data).then(res => {
                    this.$message.success("设定成功！")
                    this.open = false
                    this.resetDialogForm()
                })
            }else{
                this.$message.error("没有有效信息！设定失败")
            }
        },
        getMonthName(data){
            if(data === 'Jan'){
                return '一月';
            }else if(data === 'Feb'){
                return '二月';
            }else if(data === 'Mar'){
                return '三月';
            }else if(data === 'Apr'){
                return '四月';
            }else if(data === 'May'){
                return '五月';
            }else if(data === 'June'){
                return '六月';
            }else if(data === 'July'){
                return '七月';
            }else if(data === 'Aug'){
                return '八月';
            }else if(data === 'Sep'){
                return '九月';
            }else if(data === 'Oct'){
                return '十月';
            }else if(data === 'Nov'){
                return '十一月';
            }else if(data === 'Dec'){
                return '十二月';
            }
        },
        getWeekName(data){
            if(data === 'Mon'){
                return '周一';
            }else if(data === 'Tue'){
                return '周二';
            }else if(data === 'Wed'){
                return '周三';
            }else if(data === 'Thur'){
                return '周四';
            }else if(data === 'Fri'){
                return '周五';
            }else if(data === 'Sat'){
                return '周六';
            }else if(data === 'Sun'){
                return '周天';
            }
        },
        day_change(){
            if(this.day_value === 'Day'){
                this.week_value = ['None']
            }
        },
        week_change(){
            if(this.week_value[0] !== 'None'){
                this.day_value = 'None'
                this.day_input = ''
            }
        },
        collectDialogFormData(){
            const json = {
                "week": this.week_value,
                "mon": this.mon_value,
                "day": this.day_input,
                "hour": this.hour_input,
                "minute": this.minute_input,
                "second": this.second_input
            }
            return json;
        },
        judgeDialogFormDataAvailable(){
          if(this.week_value.length < 2){
              if(this.mon_value.length < 2){
                  if(this.day_input === ''){
                      if(this.hour_input === ''){
                          if(this.minute_input === ''){
                              if(this.second_input === ''){
                                  return false;
                              }
                          }
                      }
                  }
              }
          }
          return true
        },
        resetDialogForm(){
            this.week_value = []
            this.mon_value = []
            this.day_value = ''
            this.day_input = ''
            this.second_input = ''
            this.second_value = ''
            this.minute_input = ''
            this.minute_value = ''
            this.hour_input = ''
            this.hour_value = ''
        }
    }
};
</script>
