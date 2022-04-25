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
            defaultOpeneds: ['1', '2'],
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
                            style: {}
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
                            style: {}
                        },
                        {
                            id: '22',
                            type: 'read_csv',
                            name: '读取csv',
                            ico: 'el-icon-document',
                            // 自定义覆盖样式
                            style: {}
                        },
                        {
                            id: '23',
                            type: 'read_txt',
                            name: '读取txt',
                            ico: 'el-icon-document',
                            // 自定义覆盖样式
                            style: {}
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
                            style: {}
                        },
                        {
                            id: '32',
                            type: 'add_text',
                            name: '追加文本',
                            ico: 'el-icon-document-add',
                            // 自定义覆盖样式
                            style: {}
                        },
                        {
                            id: '33',
                            type: 'text_length',
                            name: '获得文本长度',
                            ico: 'el-icon-c-scale-to-original',
                            // 自定义覆盖样式
                            style: {}
                        },
                        {
                            id: '34',
                            type: 'obj_to_text',
                            name: '转换对象为文本',
                            ico: 'el-icon-files',
                            // 自定义覆盖样式
                            style: {}
                        }
                    ]
                },
                {
                    id: '6',
                    type: 'group',
                    name: '其他类型',
                    ico: '',
                    open: true,
                    children: [
                        {
                            id: '61',
                            type: 'end',
                            name: '结束',
                            ico: 'el-icon-video-pause',
                            // 自定义覆盖样式
                            style: {}
                        },
                        {
                            id: '62',
                            type: 'sleep',
                            name: '休眠等待',
                            ico: 'el-icon-timer',
                            // 自定义覆盖样式
                            style: {}
                        },
                        {
                            id: '63',
                            type: 'email_send',
                            name: '邮件发送',
                            ico: 'el-icon-s-promotion',
                            // 自定义覆盖样式
                            style: {}
                        },
                        {
                            id: '64',
                            type: 'system_time',
                            name: '获得当前时间',
                            ico: 'el-icon-time',
                            // 自定义覆盖样式
                            style: {}
                        },
                        {
                            id: '65',
                            type: 'move_time',
                            name: '增加/减少时间',
                            ico: 'el-icon-time',
                            // 自定义覆盖样式
                            style: {}
                        },
                        {
                            id: '66',
                            type: 'date_to_timestamp',
                            name: '日期转为时间戳',
                            ico: 'el-icon-time',
                            // 自定义覆盖样式
                            style: {}
                        },
                        {
                            id: '67',
                            type: 'timestamp_to_date',
                            name: '时间戳转为日期',
                            ico: 'el-icon-time',
                            // 自定义覆盖样式
                            style: {}
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
