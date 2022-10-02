<template>
    <el-row>
        <el-col>
            <el-input v-model="value" v-if="vis"></el-input>
            <el-button @click="openDialog" type="primary" v-if="!vis">选择参数</el-button>
        </el-col>
        <el-col>
            <el-checkbox v-model="vis">自定义</el-checkbox>
        </el-col>
        <el-dialog
            title="选择参数"
            :visible.sync="dialogVisible"
            width="30%">
            <el-row :gutter="10">
                <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="4">父节点名称</el-col>
                <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="16">
                    <el-select v-model="parentParam" placeholder="请选择" :popper-append-to-body="false" @change="changeValue">
                        <el-option
                            v-for="item in list"
                            :key="item"
                            :label="item"
                            :value="item">
                        </el-option>
                    </el-select>
                </el-col>
            </el-row>
            <el-divider/>
            <el-row :gutter="10">
                <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="4">子节点名称</el-col>
                <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="9">
                    <el-input v-model="childParam" placeholder="可为空"></el-input>
                </el-col>
            </el-row>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="closeDialog">确 定</el-button>
            </span>
        </el-dialog>
    </el-row>
</template>

<script>

export default {
    name: 'SpecialInput',
    model: {
        prop: "value",
        event: "change"
    },
    props: {
        "value": {type:String, default: ''},
        "list" : {type:Array, default: []},
        "isWeb": {type: Boolean, default: false}
    },
    data() {
        return {
            vis: true,
            dialogVisible: false,
            childParam: null,
            parentParam: ''
        }
    },
    mounted() {
    },
    watch: {
        value(){
            this.$emit("change", this.value);
        }
    },
    created() {
        this.vis = true
    },
    methods: {
        openDialog(){
            this.dialogVisible = true
            this.value = ''
        },
        closeDialog(){
            this.dialogVisible = false
            const json = {
                'parentSource': this.value,
                'childSource': this.childParam
            }
            if(!this.isWeb){
                this.value = JSON.stringify(json)
            }else{
                const json = {
                    childParam: this.childParam
                }
                this.$emit("flush_child", json);
            }
            this.parentParam = ''
            this.childParam = ''
        },
        changeValue(){
            this.value = this.parentParam
        }
    }
}
</script>
