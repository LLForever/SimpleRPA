<template>
    <div class="flow-menu" ref="tool">
        <div v-for="menu  in  menuList" :key="menu.id">
            <span class="ef-node-pmenu" @click="menu.open = !menu.open"><i
                :class="{'el-icon-caret-bottom': menu.open,'el-icon-caret-right': !menu.open}"
            ></i>&nbsp;{{ menu.name }}</span>
            <ul v-show="menu.open" class="ef-node-menu-ul">
                <draggable @end="end" @start="move" v-model="menu.children" :options="draggableOptions">
                    <li v-for="subMenu in menu.children" class="ef-node-menu-li" :key="subMenu.id" :type="subMenu.type">
                        <i :class="subMenu.ico"></i> {{ subMenu.name }}
                    </li>
                </draggable>
            </ul>
        </div>
    </div>
</template>
<script>
import draggable from 'vuedraggable'

var mousePosition = {
    left: -1,
    top: -1
}

export default {
    data() {
        return {
            activeNames: '1',
            // draggable配置参数参考 https://www.cnblogs.com/weixin186/p/10108679.html
            draggableOptions: {
                preventOnFilter: false,
                sort: false,
                disabled: false,
                ghostClass: 'tt',
                // 不使用H5原生的配置
                forceFallback: true
                // 拖拽的时候样式
                // fallbackClass: 'flow-node-draggable'
            },
            // 默认打开的左侧菜单的id
            defaultOpeneds: ['1'],
            menuList: [
                {
                    id: '1',
                    type: 'group',
                    name: '开始节点(必须存在)',
                    ico: '',
                    open: true,
                    children: [
                        {
                            id: '11',
                            type: 'start',
                            name: '开始',
                            ico: 'el-icon-video-play',
                            // 自定义覆盖样式
                            style: {},
                            params: {}
                        }
                    ]
                },
                {
                    id: '2',
                    type: 'group',
                    name: '数据读取',
                    ico: '',
                    open: true,
                    children: [
                        {
                            id: '21',
                            type: 'read_excel',
                            name: '读取Excel',
                            ico: 'el-icon-document',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                file: null,
                                fileName: null,
                                outputParamName: '',
                                sheetName: null,
                                colNamePos: 0
                            }
                        },
                        {
                            id: '22',
                            type: 'read_csv',
                            name: '读取csv',
                            ico: 'el-icon-document',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                file: null,
                                fileName: null,
                                outputParamName: ''
                            }
                        },
                        {
                            id: '23',
                            type: 'read_txt',
                            name: '读取txt',
                            ico: 'el-icon-document',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                file: null,
                                fileName: null,
                                outputParamName: ''
                            }
                        }
                    ]
                },
                {
                    id: '3',
                    type: 'group',
                    name: '数据处理',
                    ico: '',
                    open: true,
                    children: [
                        {
                            id: '31',
                            type: 'random',
                            name: '产生随机数',
                            ico: 'el-icon-magic-stick',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                outputParamName: ''
                            }
                        },
                        {
                            id: '39',
                            type: 'generate_text',
                            name: '生成一个字符串',
                            ico: 'el-icon-magic-stick',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                inputText: '',
                                outputParamName: ''
                            }
                        },
                        {
                            id: '32',
                            type: 'add_text',
                            name: '追加文本',
                            ico: 'el-icon-document-add',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                source: '',
                                inputText: ''
                            }
                        },
                        {
                            id: '33',
                            type: 'text_length',
                            name: '获得文本长度',
                            ico: 'el-icon-c-scale-to-original',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                source: '',
                                outputParamName: ''
                            }
                        },
                        {
                            id: '34',
                            type: 'obj_to_text',
                            name: '转换对象为文本',
                            ico: 'el-icon-files',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                source: '',
                                outputParamName: ''
                            }
                        },
                        {
                            id: '35',
                            type: 'replace_text',
                            name: '文本替换',
                            ico: 'el-icon-document',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                source: '',
                                inputText: '',
                                targetText: '',
                                allReplace: true
                            }
                        },
                        {
                            id: '36',
                            type: 'encode_decode',
                            name: '编码解码',
                            ico: 'el-icon-lock',
                            // 自定义覆盖样式
                            style: {},
                            params: {}
                        },
                        {
                            id: '37',
                            type: 'calculate',
                            name: '值计算',
                            ico: 'el-icon-cpu',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                source: '',
                                calculateType: '',
                                targetText: '',
                                outputParamName: ''
                            }
                        },
                        {
                            id: '38',
                            type: 'get_obj_row',
                            name: '获得数组数据的一行',
                            ico: 'el-icon-document-remove',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                source: '',
                                outputParamName: '',
                                rowNum: 0
                            }
                        }
                    ]
                },
                {
                    id: '4',
                    type: 'group',
                    name: '条件判断',
                    ico: '',
                    open: true,
                    children: [
                        {
                            id: '41',
                            type: 'single_condition',
                            name: 'IF单条件',
                            ico: 'el-icon-set-up',
                            // 自定义覆盖样式
                            style: {},
                            params: {}
                        },
                        {
                            id: '42',
                            type: 'multi_condition',
                            name: 'IF多条件',
                            ico: 'el-icon-set-up',
                            // 自定义覆盖样式
                            style: {},
                            params: {}
                        }
                    ]
                },
                {
                    id: '5',
                    type: 'group',
                    name: '循环组件',
                    ico: '',
                    open: true,
                    children: [
                        {
                            id: '51',
                            type: 'for_loop',
                            name: 'For循环',
                            ico: 'el-icon-refresh-right',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                startPos: 0,
                                endPos: 0,
                                outputParamName: ''
                            },
                        },
                        {
                            id: '52',
                            type: 'while_loop',
                            name: 'While循环',
                            ico: 'el-icon-refresh-right',
                            // 自定义覆盖样式
                            style: {},
                            params: {}
                        },
                        {
                            id: '53',
                            type: 'foreach_loop',
                            name: 'ForEach循环',
                            ico: 'el-icon-refresh-right',
                            // 自定义覆盖样式
                            style: {},
                            params: {}
                        },
                        {
                            id: '54',
                            type: 'loop_end',
                            name: '循环结束',
                            ico: 'el-icon-video-pause',
                            // 自定义覆盖样式
                            style: {},
                            params: {}
                        }
                    ]
                },
                {
                    id: '6',
                    type: 'group',
                    name: '网页自动化组件',
                    ico: '',
                    open: true,
                    children: [
                        {
                            id: '61',
                            type: 'open_page',
                            name: '打开网页',
                            ico: 'el-icon-s-platform',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                URL: '',
                                outputParamName: ''
                            }
                        },
                        {
                            id: '62',
                            type: 'single_click',
                            name: '单击网页元素',
                            ico: 'el-icon-mouse',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                inputSource: {
                                    parentSource: '',
                                    childSource: ''
                                },
                                xPath: ''
                            }
                        },
                        {
                            id: '63',
                            type: 'double_click',
                            name: '双击网页元素',
                            ico: 'el-icon-mouse',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                inputSource: {
                                    parentSource: '',
                                    childSource: ''
                                },
                                xPath: ''
                            }
                        },
                        {
                            id: '64',
                            type: 'mouse_hover',
                            name: '鼠标悬停',
                            ico: 'el-icon-mouse',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                inputSource: {
                                    parentSource: '',
                                    childSource: ''
                                },
                                xPath: ''
                            }
                        },
                        {
                            id: '65',
                            type: 'write_input',
                            name: '填写输入框',
                            ico: 'el-icon-mouse',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                inputSource: {
                                    parentSource: '',
                                    childSource: ''
                                },
                                xPath: '',
                                inputText: ''
                            }
                        },
                        {
                            id: '66',
                            type: 'drag_element',
                            name: '拖拽元素',
                            ico: 'el-icon-mouse',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                inputSource: {
                                    parentSource: '',
                                    childSource: ''
                                },
                                xPath: '',
                                targetPosition: ''
                            }
                        },
                        {
                            id: '67',
                            type: 'get_element',
                            name: '获得元素对象',
                            ico: 'el-icon-s-platform',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                inputSource: {
                                    parentSource: '',
                                    childSource: ''
                                },
                                xPath: '',
                                outputParamName: ''
                            }
                        },
                        {
                            id: '68',
                            type: 'jump_web',
                            name: '跳转至新网址',
                            ico: 'el-icon-s-platform',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                inputSource: {
                                    parentSource: '',
                                    childSource: ''
                                },
                                URL: ''
                            }
                        },
                        {
                            id: '69',
                            type: 'get_screenshot',
                            name: '获得网页截图',
                            ico: 'el-icon-s-platform',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                inputSource: {
                                    parentSource: '',
                                    childSource: ''
                                },
                                img64: ''
                            }
                        },
                        {
                            id: '610',
                            type: 'get_cookie',
                            name: '获得Cookie',
                            ico: 'el-icon-s-platform',
                            // 自定义覆盖样式
                            style: {},
                            params: {}
                        },
                        {
                            id: '611',
                            type: 'element_content_getter',
                            name: '获得网页组件内容',
                            ico: 'el-icon-s-platform',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                inputSource: {
                                    parentSource: '',
                                    childSource: ''
                                },
                                outputParamName: ''
                            }
                        }
                    ]
                },
                {
                    id: '7',
                    type: 'group',
                    name: '其他类型',
                    ico: '',
                    open: true,
                    children: [
                        {
                            id: '71',
                            type: 'end',
                            name: '结束',
                            ico: 'el-icon-circle-close',
                            // 自定义覆盖样式
                            style: {},
                            params: {}
                        },
                        {
                            id: '72',
                            type: 'sleep',
                            name: '休眠等待',
                            ico: 'el-icon-timer',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                sleepTime: 0,
                                unit: 0
                            }
                        },
                        {
                            id: '73',
                            type: 'email_send',
                            name: '邮件发送',
                            ico: 'el-icon-s-promotion',
                            // 自定义覆盖样式
                            style: {},
                            params: {}
                        },
                        {
                            id: '74',
                            type: 'system_time',
                            name: '获得当前时间',
                            ico: 'el-icon-time',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                              outputParamName: ''
                            }
                        },
                        {
                            id: '75',
                            type: 'move_time',
                            name: '增加/减少时间',
                            ico: 'el-icon-time',
                            // 自定义覆盖样式
                            style: {},
                            params: {}
                        },
                        {
                            id: '76',
                            type: 'date_to_timestamp',
                            name: '日期转为时间戳',
                            ico: 'el-icon-time',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                inputText: ''
                            }
                        },
                        {
                            id: '77',
                            type: 'timestamp_to_date',
                            name: '时间戳转为日期',
                            ico: 'el-icon-time',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                inputText: ''
                            }
                        }
                    ]
                },
                {
                    id: '8',
                    type: 'group',
                    name: 'AI增强',
                    ico: '',
                    open: true,
                    children: [
                        {
                            id: '81',
                            type: 'ai_ocr',
                            name: 'OCR',
                            ico: 'el-icon-cpu',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                inputSource: {
                                    parentSource: '',
                                    childSource: ''
                                },
                                outputParamName: ''
                            }
                        },
                        {
                            id: '82',
                            type: 'ai_table_ocr',
                            name: '表格识别',
                            ico: 'el-icon-cpu',
                            // 自定义覆盖样式
                            style: {},
                            params: {
                                inputSource: {
                                    parentSource: '',
                                    childSource: ''
                                },
                                outputParamName: '',
                                outputAttributeList: []
                            }
                        }
                    ]
                }
            ],
            nodeMenu: {}
        }
    },
    components: {
        draggable
    },
    created() {
        /**
         * 以下是为了解决在火狐浏览器上推拽时弹出tab页到搜索问题
         * @param event
         */
        if (this.isFirefox()) {
            document.body.ondrop = function(event) {
                // 解决火狐浏览器无法获取鼠标拖拽结束的坐标问题
                mousePosition.left = event.layerX
                mousePosition.top = event.clientY - 50
                event.preventDefault()
                event.stopPropagation()
            }
        }
    },
    methods: {
        // 根据类型获取左侧菜单对象
        getMenuByType(type) {
            for (let i = 0; i < this.menuList.length; i++) {
                let children = this.menuList[i].children
                for (let j = 0; j < children.length; j++) {
                    if (children[j].type === type) {
                        return children[j]
                    }
                }
            }
        },
        // 拖拽开始时触发
        move(evt, a, b, c) {
            var type = evt.item.attributes.type.nodeValue
            this.nodeMenu = this.getMenuByType(type)
        },
        // 拖拽结束时触发
        end(evt, e) {
            this.$emit('addNode', evt, this.nodeMenu, mousePosition)
        },
        // 是否是火狐浏览器
        isFirefox() {
            var userAgent = navigator.userAgent
            if (userAgent.indexOf('Firefox') > -1) {
                return true
            }
            return false
        }
    }
}
</script>
