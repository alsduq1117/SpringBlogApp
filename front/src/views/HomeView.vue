<script setup lang="ts">
import {ref} from "vue";

const count = ref(0)
</script>

<template>
  <p>안녕하세요</p> <button @click="count" += 1>버튼을 눌러주세요</button>
  <p>{{count}}</p>
</template>

<style>

</style>