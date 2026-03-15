<template>
  <button :disabled="!jugadorSeleccionado" @click="$emit('devorar')">Devorar</button>
</template>

<script>
export default {
  props: ['jugadorSeleccionado'],
}
</script>

<style scoped>
button {
  background: #8b0000;
  color: white;

  padding: 12px 25px;
}
</style>
