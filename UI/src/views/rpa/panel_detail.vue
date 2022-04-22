<template>
  <my-panel ref="myPanel"></my-panel>
</template>

<script>
import { getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt'
import MyPanel from './components/panel'
import { websocketInit } from '../../utils/websocket'

export default {
  name: "panel_detail",
  data() {
    return {
      panelDetailData: "",
      websocketLink: null,
      isWebsocketConnected: false,
      websocketTimer: null,
      websocketConnectNumber: 0
    };
  },
  mounted() {
    this.initPanelData();
    this.initWebsocket();
  },
  components:{
    MyPanel
  },
  watch: {
  },
  created() {
  },
  methods: {
    initPanelData(){
      this.panelDetailData = this.$route.params.panelDetailData;
      this.$refs.myPanel.setRowDetail(this.panelDetailData);
    },
    initWebsocket(){
      if(this.isWebsocketConnected){
        return;
      }
      this.wsDestroy();
      clearInterval(this.websocketTimer);
      const taskJsonInfo = {
        taskId: this.panelDetailData.taskId,
        userId: this.panelDetailData.userId
      }
      this.websocketLink = new WebSocket(websocketInit(JSON.stringify(taskJsonInfo).toString()));
      this.websocketLink.addEventListener('open', this.wsOpenHandler)
      this.websocketLink.addEventListener('message', this.wsMessageHandler)
      this.websocketLink.addEventListener('error', this.wsErrorHandler)
      this.websocketLink.addEventListener('close', this.wsCloseHandler)
      this.websocketTimer = setInterval(() => {
        if (this.websocketLink.readyState === 0) {
          if(this.websocketConnectNumber < 5){
            this.initWebsocket();
          }else{
            this.$message.error('创建协作任务失败！');
            clearInterval(this.websocketTimer);
          }
          this.websocketConnectNumber++;
          console.log('connect failed!');
        } else {
          clearInterval(this.websocketTimer)
          this.isWebsocketConnected = true;
        }
      }, 1000)
    },
    wsOpenHandler(event) {
      console.log('ws建立连接成功')
    },
    /**
     * ws收到信息
     * */
    wsMessageHandler(e) {
      console.log('wsMessageHandler', e, JSON.parse(e.data))
    },
    /**
     * ws通信发生错误
     * */
    wsErrorHandler(event) {
      console.log(event, '通信发生错误')
    },
    /**
     * ws关闭
     * */
    wsCloseHandler(event) {
      console.log(event, 'ws关闭')
    },
    /**
     * 销毁ws
     * */
    wsDestroy() {
      if (this.websocketLink) {
        this.websocketLink.removeEventListener('open', this.wsOpenHandler)
        this.websocketLink.removeEventListener('message', this.wsMessageHandler)
        this.websocketLink.removeEventListener('error', this.wsErrorHandler)
        this.websocketLink.removeEventListener('close', this.wsCloseHandler)
        this.websocketLink.close()
        this.websocketLink = null
        this.isWebsocketConnected = false
        this.websocketConnectNumber = 0
        clearInterval(this.websocketLink)
      }
    },
    sendDataToServer(data){
      if(this.websocketLink.readyState === 1){
        this.websocketLink.send('来自前端的数据')
      }else{
        throw Error('未连接服务器');
      }
    }
  }
};
</script>
