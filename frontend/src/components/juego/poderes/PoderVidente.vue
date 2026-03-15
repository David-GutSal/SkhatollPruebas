<template>
  <button :disabled="!jugadorSeleccionado" @click="$emit('premonicion')">Premonición</button>
</template>

<script>
export default {
  props: ['jugadorSeleccionado'],
}
</script>

<style scoped>
button {
  background: black;
  color: white;

  padding: 12px 25px;
}
</style>
