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

                    <el-form-item label="上传文件" v-if="node.id && node.params.file !== undefined">
                        <el-card shadow="never">
                            <el-upload
                                action="#"
                                :limit="1"
                                :on-exceed="handleExceed"
                                :http-request="uploadFile"
                                :file-list="node.fileList"
                                :accept="getFileType(node)">
                                <el-button size="small" type="primary">点击上传</el-button>
                            </el-upload>
                        </el-card>
                    </el-form-item>

                    <el-form-item label="输入源" v-if="node.id && node.params.source !== undefined">
                        <s-input v-model="node.params.source" :list="inputParamList"></s-input>
                    </el-form-item>

                    <el-form-item label="网页源" v-if="node.id && node.params.inputSource !== undefined">
<!--                        <el-input v-model="node.params.inputSource.parentSource"></el-input>-->
                        <s-input v-model="node.params.inputSource.parentSource"
                                 :list="inputParamList"
                                 :is-web="true"
                                 @flush_child="flushWebInputSource"
                        ></s-input>
                    </el-form-item>

                    <el-form-item label="查看截图" v-if="node.id && node.type === 'get_screenshot'">
                        <el-button @click="openScreenshotDialog">点击查看</el-button>
                    </el-form-item>

                    <el-form-item label="网络链接" v-if="node.id && node.params.URL !== undefined">
                        <s-input v-model="node.params.URL" :list="inputParamList"></s-input>
                    </el-form-item>

                    <el-form-item label="xPath" v-if="node.id && node.params.xPath !== undefined">
<!--                        <el-input v-model="node.params.xPath"></el-input>-->
                        <s-input v-model="node.params.xPath" :list="inputParamList"></s-input>
                    </el-form-item>

                    <el-form-item label="目标位置" v-if="node.id && node.params.targetPosition !== undefined">
                        <el-input v-model="node.params.targetPosition"></el-input>
                    </el-form-item>

                    <el-form-item label="输入内容" v-if="node.id && node.params.inputText !== undefined">
<!--                        <el-input v-model="node.params.inputText"></el-input>-->
                        <s-input v-model="node.params.inputText" :list="inputParamList"></s-input>
                    </el-form-item>

                    <el-form-item label="判断类型" v-if="node.id && node.params.judgeType !== undefined">
                        <el-select v-model="node.params.judgeType" placeholder="请选择">
                            <el-option
                                v-for="i in judgeTypeItems"
                                :key="i.value"
                                :label="i.label"
                                :value="i.value">
                            </el-option>
                        </el-select>
                    </el-form-item>

                    <el-form-item label="休眠时间" v-if="node.id && node.params.sleepTime !== undefined">
                        <el-input v-model="node.params.sleepTime"></el-input>
                    </el-form-item>

                    <el-form-item label="起始位置" v-if="node.id && node.params.startPos !== undefined">
                        <s-input v-model="node.params.startPos" :list="inputParamList"></s-input>
                    </el-form-item>

                    <el-form-item label="终止位置" v-if="node.id && node.params.endPos !== undefined">
                        <s-input v-model="node.params.endPos" :list="inputParamList"></s-input>
                    </el-form-item>

                    <el-form-item label="指定行" v-if="node.id && node.params.rowNum !== undefined">
                        <s-input v-model="node.params.rowNum" :list="inputParamList"></s-input>
                    </el-form-item>

                    <el-form-item label="计算方式" v-if="node.id && node.params.calculateType !== undefined">
                        <el-select v-model="node.params.calculateType" placeholder="请选择">
                            <el-option
                                v-for="item in calculateType"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>

                    <el-form-item label="目标对象" v-if="node.id && node.params.targetText !== undefined">
                        <s-input v-model="node.params.targetText" :list="inputParamList"></s-input>
                    </el-form-item>


                    <el-form-item label="替换状态" v-if="node.id && node.params.allReplace !== undefined">
                        <el-checkbox v-model="node.params.allReplace">替换所有输入源对应文本</el-checkbox>
                    </el-form-item>

                    <el-form-item label="对方邮箱" v-if="node.id && node.params.to !== undefined">
                        <s-input v-model="node.params.to" :list="inputParamList"></s-input>
                    </el-form-item>
                    <el-form-item label="主题" v-if="node.id && node.params.subject !== undefined">
                        <s-input v-model="node.params.subject" :list="inputParamList"></s-input>
                    </el-form-item>
                    <el-form-item label="内容" v-if="node.id && node.params.content !== undefined">
                        <s-input v-model="node.params.content" :list="inputParamList"></s-input>
                    </el-form-item>

                    <el-form-item label="输出参数" v-if="node.id && node.params.outputParamName !== undefined">
                        <el-input v-model="node.params.outputParamName"></el-input>
                    </el-form-item>

                    <el-form-item label="识别属性" v-if="node.id && node.params.outputAttributeList !== undefined">
                        <el-tag
                            v-for="tag in node.params.outputAttributeList"
                            :key="tag"
                            closable
                            :disable-transitions="false"
                            @close="outputAttributeListTagClose(tag)">
                            {{tag}}
                        </el-tag>
                        <el-input
                            class="input-new-tag"
                            v-if="addAttrInputVisible"
                            v-model="attrInputValue"
                            ref="addTagInput"
                            size="small"
                            @keyup.enter.native="handleNewAttrInputConfirm"
                            @blur="handleNewAttrInputConfirm"
                        >
                        </el-input>
                        <el-button v-else class="button-new-tag" size="small" @click="showAddNewAttrButton">+ 新属性</el-button>
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
            <el-dialog
                title="查看图片"
                :visible.sync="screenshot_show_dialog"
                width="70%"
            >
                <el-image :src="screenshot_img64" fit="contain" style="width: 100%"></el-image>
                <span slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="screenshot_show_dialog = false">确 定</el-button>
                </span>
            </el-dialog>
        </div>
    </div>

</template>

<script>
    import { cloneDeep } from 'lodash'
    import { blobToBase64, initNodeParams } from '../../views/rpa/utils/NodeParamsHandler'
    import sInput from '../../views/rpa/components/SpecialInput.vue'
    import {get_screenshot} from "@/api/rpa/RpaAssist";

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
                }],
                inputParamList: [],
                screenshot_show_dialog: false,
                screenshot_img64: '',
                addAttrInputVisible: false,
                attrInputValue: '',
                calculateType:[
                    {
                        label: '加',
                        value: 0
                    },
                    {
                        label: '减',
                        value: 1
                    },
                    {
                        label: '乘',
                        value: 2
                    },
                    {
                        label: '除',
                        value: 3
                    }
                ],
                judgeTypeItems:[{
                    label: '等于',
                    value: 'equal'
                }, {
                    label: '不等于',
                    value: 'notequal'
                }, {
                    label: '包含',
                    value: 'contain'
                }, {
                    label: '不包含',
                    value: 'notcontain'
                }, {
                    label: '元素出现',
                    value: 'appear'
                }, {
                    label: '元素消失',
                    value: 'disappear'
                }]
            }
        },
        components: {
            sInput
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
                        if(node.params.outputParamName){
                            if(this.inputParamList.indexOf(node.params.outputParamName)==-1){
                                this.inputParamList.push(node.params.outputParamName)
                            }
                        }
                        // console.log(this.data)
                        // console.log(this.node)
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
                    this.node.params.outputParamName = (Date.now()).toString()
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
            },
            getFileType(node){
                if(node.type === 'read_txt'){
                    return '.txt';
                }else if(node.type === 'read_csv'){
                    return '.csv'
                }else if(node.type === 'read_excel'){
                    return '.xlsx,.xls'
                }
            },
            setInputParamList(list) {
                this.inputParamList = list;
            },
            flushWebInputSource(json){
                this.node.params.inputSource.childSource = json.childParam
            },
            openScreenshotDialog(){
                const screen_param = {
                    taskid: this.data.taskId,
                    nodeid: this.node.id
                }
                get_screenshot(screen_param).then(res => {
                    this.screenshot_show_dialog = true
                    this.screenshot_img64 = 'data:image/png;base64,' + res
                })
            },
            outputAttributeListTagClose(tag) {
                this.node.params.outputAttributeList.splice(this.node.params.outputAttributeList.indexOf(tag), 1);
            },
            showAddNewAttrButton() {
                this.addAttrInputVisible = true;
                this.$nextTick(_ => {
                    this.$refs.addTagInput.$refs.input.focus();
                });
            },
            handleNewAttrInputConfirm() {
                let inputValue = this.attrInputValue;
                if (inputValue) {
                    this.node.params.outputAttributeList.push(inputValue);
                }
                this.addAttrInputVisible = false;
                this.attrInputValue = '';
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
