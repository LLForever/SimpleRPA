<template>
  <my-panel></my-panel>
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
      codeUrl: ""
    };
  },
  components:{
    MyPanel
  },
  watch: {
  },
  created() {
  },
  methods: {

  }
};
</script>
