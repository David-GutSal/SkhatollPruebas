export default {
  namespaced: true,

  state: () => ({
    codigoSala: null,
    esCreador: false,
    jugadores: [],
    miRol: null,
    miRolDescripcion: null,
    miBando: null,
    bandoGanador: null,
    mensajeFin: null,
  }),

  mutations: {
    SET_SALA(state, { codigoSala, esCreador }) {
      state.codigoSala = codigoSala
      state.esCreador = esCreador
    },
    SET_JUGADORES(state, jugadores) {
      state.jugadores = jugadores
    },
    SET_ROL(state, { nombreRol, descripcionRol, bando }) {
      state.miRol = nombreRol
      state.miRolDescripcion = descripcionRol
      state.miBando = bando
    },
    UPDATE_FASE(state, fase) {
      state.jugadores = state.jugadores.map((j) => ({ ...j }))
      state.fase = fase
    },
    MARCAR_MUERTO(state, nombreJugador) {
      const jugador = state.jugadores.find((j) => j.nombre === nombreJugador)
      if (jugador) jugador.estaVivo = false
    },
    ACTUALIZAR_VOTOS(state, votos) {
      // Resetear votos
      state.jugadores.forEach((j) => (j.votos = 0))
      // Contar votos por objetivo
      votos.forEach((v) => {
        const objetivo = state.jugadores.find((j) => j.nombre === v.nombreObjetivo)
        if (objetivo) objetivo.votos = (objetivo.votos || 0) + 1
      })
    },
    SET_RESULTADO(state, { bandoGanador, mensaje }) {
      state.bandoGanador = bandoGanador
      state.mensajeFin = mensaje
    },
    CLEAR_SALA(state) {
      state.codigoSala = null
      state.esCreador = false
      state.jugadores = []
      state.miRol = null
      state.miRolDescripcion = null
      state.miBando = null
    },
  },

  actions: {
    crearSala({ commit }, codigoSala) {
      commit('SET_SALA', { codigoSala, esCreador: true })
    },
    unirse({ commit }, codigoSala) {
      commit('SET_SALA', { codigoSala, esCreador: false })
    },
    setResultado({ commit }, resultado) {
      commit('SET_RESULTADO', resultado)
    },
    setJugadores({ commit }, jugadores) {
      commit('SET_JUGADORES', jugadores)
    },
    setRol({ commit }, rol) {
      commit('SET_ROL', rol)
    },
    marcarMuerto({ commit }, nombreJugador) {
      commit('MARCAR_MUERTO', nombreJugador)
    },
    actualizarVotos({ commit }, votos) {
      commit('ACTUALIZAR_VOTOS', votos)
    },
    salir({ commit }) {
      commit('CLEAR_SALA')
    },
  },

  getters: {
    codigoSala: (state) => state.codigoSala,
    esCreador: (state) => state.esCreador,
    jugadores: (state) => state.jugadores,
    miRol: (state) => state.miRol,
    miRolDescripcion: (state) => state.miRolDescripcion,
    miBando: (state) => state.miBando,
    bandoGanador: (state) => state.bandoGanador,
    mensajeFin: (state) => state.mensajeFin,
  },
}
