<template>
  <div class="lista-jugadores">
    <h2>Jugadores en sala</h2>

    <ul>
      <li v-for="jugador in jugadores" :key="jugador.idUsuario">
        <label v-if="modoCreador">
          <input type="radio" name="narrador" :checked="jugador.esNarrador" />
          {{ jugador.nombre }}
        </label>

        <span v-else>
          {{ jugador.nombre }}
        </span>
      </li>
    </ul>

    <p v-if="modoCreador" class="mensaje">
      Puedes seleccionar a otro jugador como narrador o serlo tú mismo
    </p>
  </div>
</template>

<script>
export default {
  name: 'ListaJugadores',

  props: {
    jugadores: Array,

    modoCreador: Boolean,
  },
}
</script>

<style scoped>
.lista-jugadores {
  background: #1f1f1f;
  padding: 20px;

  border-radius: 10px;

  color: white;
}

ul {
  list-style: none;
  padding: 0;
}

li {
  padding: 8px 0;
}

.mensaje {
  margin-top: 15px;
  font-size: 14px;
}
</style>
