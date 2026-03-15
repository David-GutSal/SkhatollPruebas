<template>
  <div class="panel">
    <template v-if="esDia">
      <button @click="$emit('votarAlcalde')" :disabled="hayAlcalde">Votar alcalde</button>

      <button @click="$emit('votarLinchamiento')">Votar linchamiento</button>

      <button @click="$emit('finalizarVotacion')">Finalizar votación</button>
    </template>

    <template v-else>
      <button class="noche" @click="$emit('eventos')">Iniciar eventos</button>
    </template>
  </div>
</template>

<script>
export default {
  props: ['esDia', 'hayAlcalde'],
}
</script>

<style scoped>
.panel {
  display: flex;
  justify-content: center;
  gap: 20px;

  margin-bottom: 20px;

  flex-wrap: wrap;
}

button {
  background: black;
  color: white;

  padding: 10px 20px;

  border: none;
}

button:disabled {
  opacity: 0.5;
}

.noche {
  background: red;
}
</style>
