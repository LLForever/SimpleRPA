<template>
    <my-panel ref="myPanel"></my-panel>
</template>

<script>
import { getCodeImg } from '@/api/login'
import Cookies from 'js-cookie'
import { encrypt, decrypt } from '@/utils/jsencrypt'
import MyPanel from './components/panel'
import { websocketInit } from '../../utils/websocket'

export default {
    name: 'panel_detail',
    data() {
        return {
            panelDetailData: null,
            websocketLink: null,
            isWebsocketConnected: false,
            websocketConnectNumber: 0
        }
    },
    mounted() {
        this.initPanelData()
        //this.initWebsocket();
    },
    components: {
        MyPanel
    },
    watch: {},
    created() {
    },
    beforeDestroy() {
        this.wsDestroy()
    },
    methods: {
        initPanelData() {
            if (this.panelDetailData) {
                if (this.panelDetailData.taskId !== this.$route.params.panelDetailData.taskId) {
                    this.isWebsocketConnected = false
                }
            }
            this.panelDetailData = this.$route.params.panelDetailData
            setTimeout(() => {
                this.$refs.myPanel.setRowDetail(this.panelDetailData)
            }, 50)
            this.initWebsocket()
        },
        initWebsocket() {
            if (this.isWebsocketConnected === true) {
                return
            }
            this.wsDestroy()
            const taskJsonInfo = {
                taskId: this.panelDetailData.taskId,
                userId: this.panelDetailData.userId
            }
            this.websocketLink = new WebSocket(websocketInit(JSON.stringify(taskJsonInfo).toString()))
            this.websocketLink.addEventListener('open', this.wsOpenHandler)
            this.websocketLink.addEventListener('message', this.wsMessageHandler)
            this.websocketLink.addEventListener('error', this.wsErrorHandler)
            this.websocketLink.addEventListener('close', this.wsCloseHandler)
            setTimeout(() => {
                if (this.websocketLink.readyState === 1) {
                    this.isWebsocketConnected = true
                } else {
                    console.log('this.websocketConnectNumber ', this.websocketConnectNumber)
                    if (this.websocketConnectNumber < 5) {
                        this.initWebsocket()
                    } else {
                        this.$message.error('创建协作任务失败！')
                    }
                    this.websocketConnectNumber++
                }
            }, 1000)
        },
        wsOpenHandler(event) {
            this.$message.success('成功连接至服务器！')
        },
        /**
         * ws收到信息
         * */
        wsMessageHandler(e) {
            console.log('wsMessageHandler', e, JSON.parse(e.data))
            const res = JSON.parse(e.data);
            if(res.actionType === 200){
                this.$refs.myPanel.setStatusForTwoNode(res.jsonObject.now, 'success');
                this.$refs.myPanel.setStatusForTwoNode(res.jsonObject.next, 'running');
            }else if(res.actionType === 102){
            }else{
                this.$message.info(res.message)
            }
        },
        /**
         * ws通信发生错误
         * */
        wsErrorHandler(event) {
            // console.log(event, '通信发生错误')
            if (this.isWebsocketConnected === true) {
                this.$message.warning('通讯异常！')
            }
        },
        /**
         * ws关闭
         * */
        wsCloseHandler(event) {
            if (this.isWebsocketConnected === true) {
                this.$message.error('与服务器断开连接！')
                this.isWebsocketConnected = false
            }
        },
        /**
         * 销毁ws
         * */
        wsDestroy() {
            if (this.websocketLink) {
                if (this.isWebsocketConnected === true) {
                    this.websocketConnectNumber = 0
                }
                this.websocketLink.removeEventListener('open', this.wsOpenHandler)
                this.websocketLink.removeEventListener('message', this.wsMessageHandler)
                this.websocketLink.removeEventListener('error', this.wsErrorHandler)
                this.websocketLink.removeEventListener('close', this.wsCloseHandler)
                this.websocketLink.close()
                this.websocketLink = null
                this.isWebsocketConnected = false
            }
        },
        sendDataToServer(data) {
            if (this.websocketLink.readyState === 1) {
                this.websocketLink.send('来自前端的数据')
            } else {
                throw Error('未连接服务器')
            }
        }
    }
}
</script>
