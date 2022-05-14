<template>
    <div>
        <div class="ef-node-form">
            <div class="ef-node-form-header">
                编辑
            </div>
            <div class="ef-node-form-body">
                <el-form :model="node" ref="dataForm" label-width="80px" v-show="type === 'node'">
                    <el-form-item label="节点id">
                        <el-input v-model="node.id" :disabled="true"></el-input>
                    </el-form-item>
                    <el-form-item label="名称">
                        <el-input v-model="node.name"></el-input>
                    </el-form-item>
                    <el-form-item label="上传文件" v-if="node.id && node.type === 'read_txt'">
                        <el-upload
                            action="#"
                            :limit="1"
                            :on-exceed="handleExceed"
                            :http-request="uploadFile"
                            :file-list="node.fileList"
                            accept=".txt">
                            <el-button size="small" type="primary">点击上传</el-button>
                        </el-upload>
                    </el-form-item>
                    <el-form-item label="上传文件" v-if="node.id && node.type === 'read_excel'">
                        <el-upload
                            action="#"
                            :limit="1"
                            :on-exceed="handleExceed"
                            :http-request="uploadFile"
                            :file-list="node.fileList"
                            accept=".xlsx,.xls">
                            <el-button size="small" type="primary">点击上传</el-button>
                        </el-upload>
                    </el-form-item>
                    <el-form-item label="输出参数" v-if="node.id && node.params.outputParamName !== undefined">
                        <el-input v-model="node.params.outputParamName"></el-input>
                    </el-form-item>
                    <el-divider></el-divider>
                    <el-form-item label="状态">
                        <span>{{ getStatus(node.state) }}</span>
                    </el-form-item>
                    <el-form-item>
<!--                        <el-button icon="el-icon-refresh">重置</el-button>-->
                        <el-button type="primary" icon="el-icon-check" :disabled="!node.id" @click="save">保存</el-button>
                        <el-button type="danger" icon="el-icon-close" :disabled="!node.id" @click="deleteElement">删除</el-button>
                    </el-form-item>
                </el-form>

                <el-form :model="line" ref="dataForm" label-width="80px" v-show="type === 'line'">
                    <el-form-item label="标注">
                        <el-input v-model="line.label"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button icon="el-icon-refresh">重置</el-button>
                        <el-button type="primary" icon="el-icon-check" @click="saveLine">保存</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="danger" icon="el-icon-close" @click="deleteElement">删除</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <!--            <div class="el-node-form-tag"></div>-->
        </div>
    </div>

</template>

<script>
    import { cloneDeep } from 'lodash'
    import { blobToBase64, initNodeParams } from '../../views/rpa/utils/NodeParamsHandler'

    export default {
        data() {
            return {
                visible: true,
                // node 或 line
                type: 'node',
                node: {},
                line: {},
                data: {},
                stateList: [{
                  state: 'ready',
                  label: '待开始'
                }, {
                    state: 'success',
                    label: '成功'
                }, {
                    state: 'warning',
                    label: '警告'
                }, {
                    state: 'error',
                    label: '错误'
                }, {
                    state: 'running',
                    label: '运行中'
                }]
            }
        },
        methods: {
            /**
             * 表单修改，这里可以根据传入的ID进行业务信息获取
             * @param data
             * @param id
             */
            nodeInit(data, id) {
                this.type = 'node'
                this.data = data
                data.nodeList.filter((node) => {
                    if (node.id === id) {
                        const tmp = cloneDeep(node);
                        this.node = initNodeParams(tmp);
                    }
                })
            },
            lineInit(line) {
                this.type = 'line'
                this.line = line
            },
            // 修改连线
            saveLine() {
                this.$emit('setLineLabel', this.line.from, this.line.to, this.line.label)
            },
            deleteElement(){
                this.$emit('deleteElement')
            },
            save() {
                this.data.nodeList.filter((node) => {
                    if (node.id === this.node.id) {
                        node.name = this.node.name
                        node.left = this.node.left
                        node.top = this.node.top
                        node.ico = this.node.ico
                        node.state = this.node.state
                        node.params = this.node.params
                        console.log(this.data)
                        this.$emit('repaintEverything')
                    }
                })
            },
            clearData(){
              this.node = {}
              this.line = {}
            },
            handleExceed(files, fileList) {
                this.$message.warning('当前限制选择 1 个文件。');
            },
            uploadFile(file){
                const File = new Blob([file.file]);
                blobToBase64(File).then(res => {
                    this.node.params.file = res;
                    this.node.params.fileName = file.file.name
                    this.node.params.outputParamName = Date.now()
                    console.log(res);
                    console.log('uploadFile', this.node, file);
                })
            },
            getStatus(st){
                const list = this.stateList;
                for (const item of list){
                    if(item.state === st){
                        return item.label;
                    }
                }
                return '';
            }
        }
    }
</script>

<style>
    .el-node-form-tag {
        position: absolute;
        top: 50%;
        margin-left: -15px;
        height: 40px;
        width: 15px;
        background-color: #fbfbfb;
        border: 1px solid rgb(220, 227, 232);
        border-right: none;
        z-index: 0;
    }
</style>
