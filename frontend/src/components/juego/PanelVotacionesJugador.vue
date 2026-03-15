<template>
  <div class="panel" v-if="esDia">
    <button :disabled="!votacionActiva || !jugadorSeleccionado" @click="$emit('votarAlcalde')">
      Votar alcalde
    </button>

    <button :disabled="!votacionActiva || !jugadorSeleccionado" @click="$emit('votarCulpable')">
      Votar culpable
    </button>
  </div>
</template>

<script>
export default {
  props: ['esDia', 'votacionActiva', 'jugadorSeleccionado'],
}
</script>

<style scoped>
.panel {
  display: flex;
  gap: 20px;
  justify-content: center;
  flex-wrap: wrap;
}

button {
  background: black;
  color: white;

  padding: 10px 20px;
}
</style>
