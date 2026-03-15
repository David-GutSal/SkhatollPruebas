import { reactive } from 'vue'

export const MODO_SIMULACION = true

export const gameState = reactive({
  fase: 'DIA',

  narrador: '',

  jugadores: [],
})
