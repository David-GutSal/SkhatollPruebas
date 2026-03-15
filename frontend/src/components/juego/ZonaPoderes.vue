<template>
  <div class="zona">
    <PoderLobo
      v-if="miRol === 'Lobo' && esMiTurno"
      :jugadorSeleccionado="jugadorSeleccionado"
      @devorar="$emit('devorar')"
    />

    <PoderVidente
      v-if="miRol === 'Vidente' && esMiTurno"
      :jugadorSeleccionado="jugadorSeleccionado"
      @premonicion="$emit('premonicion')"
    />
  </div>
</template>

<script>
import PoderLobo from './poderes/PoderLobo.vue'
import PoderVidente from './poderes/PoderVidente.vue'

export default {
  props: ['miRol', 'jugadorSeleccionado', 'esMiTurno'],

  components: {
    PoderLobo,
    PoderVidente,
  },
}
</script>

<style scoped>
.zona {
  display: flex;
  justify-content: center;
}
</style>
