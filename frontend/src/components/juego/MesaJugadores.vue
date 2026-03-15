<template>
  <div class="mesa" :class="{ noche: !esDia }">
    <CartaJugador
      v-for="j in jugadores"
      :key="j.id"
      :jugador="j"
      :esDia="esDia"
      @seleccionar="$emit('seleccionarJugador', j)"
    />
  </div>
</template>

<script>
import CartaJugador from './CartaJugador.vue'

export default {
  components: { CartaJugador },

  props: ['jugadores', 'esDia'],
}
</script>

<style scoped>
.mesa {
  display: grid;

  grid-template-columns: repeat(5, 1fr);

  gap: 20px;

  padding: 20px;

  background: black;

  border: 5px solid white;

  justify-items: center;
}

.mesa.noche {
  background: red;
}

@media (max-width: 900px) {
  .mesa {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 500px) {
  .mesa {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
