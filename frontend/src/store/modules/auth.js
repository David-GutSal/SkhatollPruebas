export default {
  namespaced: true,

  state: () => ({
    token: null,
    nombre: null,
    uuid: null,
  }),

  mutations: {
    SET_AUTH(state, { token, nombre, uuid }) {
      state.token = token
      state.nombre = nombre
      state.uuid = uuid
    },
    CLEAR_AUTH(state) {
      state.token = null
      state.nombre = null
      state.uuid = null
    },
  },

  actions: {
    login({ commit }, payload) {
      commit('SET_AUTH', payload)
    },
    logout({ commit }) {
      commit('CLEAR_AUTH')
    },
  },

  getters: {
    estaAutenticado: (state) => !!state.token,
    token: (state) => state.token,
    nombre: (state) => state.nombre,
    uuid: (state) => state.uuid,
  },
}
