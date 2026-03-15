<template>
  <div class="carta" :class="[estadoClase, esDia ? 'dia' : 'noche']" @click="$emit('seleccionar')">
    <img src="@/assets/imgs/bruja.jpg" />

    <div class="info">
      <p>Jugador: {{ jugador.nombre }}</p>
    </div>

    <div class="iconos">
      <span v-if="jugador.alcalde">👑</span>

      <span v-if="!jugador.estaVivo">💀</span>

      <span v-if="jugador.votos > 0">🗳 {{ jugador.votos }}</span>
    </div>
  </div>
</template>

<script>
export default {
  props: ['jugador', 'esDia'],

  computed: {
    estadoClase() {
      return {
        muerto: !this.jugador.estaVivo,
        alcalde: this.jugador.alcalde,
      }
    },
  },
}
</script>

<style scoped>
.carta {
  display: flex;
  gap: 15px;
  padding: 15px;

  position: relative;
  cursor: pointer;
}

.carta img {
  width: 60px;
}

.carta.dia {
  background: red;
  color: white;
}

.carta.noche {
  background: black;
  color: red;
}

.muerto {
  opacity: 0.4;
}

.alcalde {
  border: 3px solid gold;
}

.iconos {
  position: absolute;
  top: 5px;
  right: 5px;
}

@media (max-width: 600px) {
  .carta {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
}
</style>
