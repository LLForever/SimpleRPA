<template>
  <my-panel></my-panel>
</template>

<script>
import { getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt'
import MyPanel from './components/panel'
import { rpa_fun_test } from '../../api/rpa/task'

export default {
  name: "rpaIndex",
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
  mounted(){
    rpa_fun_test().then((res) => {
      console.log(res);
    }).finally(()=>{
      console.log("request end");
    })
  },
  created() {
  },
  methods: {

  }
};
</script>
