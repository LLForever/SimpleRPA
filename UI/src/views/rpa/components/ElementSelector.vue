<template>
    <div>
        <el-checkbox v-model="checked">是否输入文本</el-checkbox>
        <el-input v-model="test" v-if="checked"></el-input>
        <el-button v-if="!checked"></el-button>
    </div>
</template>
<script>
    export default {
        data() {
            return {
                checked: true,
                test: ''
            };
        }
    };
</script>
