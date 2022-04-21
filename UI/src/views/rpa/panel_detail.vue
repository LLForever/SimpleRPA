<template>
  <my-panel ref="myPanel"></my-panel>
</template>

<script>
import { getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt'
import MyPanel from './components/panel'

export default {
  name: "panel_detail",
  data() {
    return {
      panelDetailData: ""
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

    }
  }
};
</script>
