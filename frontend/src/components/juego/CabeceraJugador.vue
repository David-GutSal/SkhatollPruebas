<template>
  <div class="cabecera">
    <div class="nombre">Jugador: {{ nombreJugador }}</div>

    <div class="fase" @click="mensajeFase">
      <div v-if="esDia" class="sol">
        <i class="fas fa-sun"></i>
      </div>

      <div v-else class="luna">
        <i class="fas fa-moon"></i>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ['nombreJugador', 'esDia'],

  methods: {
    mensajeFase() {
      if (this.esDia) {
        alert('¡Es de día, reúnete con tus vecinos en la plaza del pueblo!')
      } else {
        alert('¡Llegó la noche, cierra puertas y ventanas!')
      }
    },
  },
}
</script>

<style scoped>
.cabecera {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
}

.nombre {
  background: black;
  color: red;

  padding: 10px 20px;
}

.sol,
.luna {
  width: 50px;
  height: 50px;

  display: flex;
  align-items: center;
  justify-content: center;

  border-radius: 50%;
}

.sol {
  background: white;
  color: black;
}

.luna {
  background: black;
  color: white;
}
</style>
