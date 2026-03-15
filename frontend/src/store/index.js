import { createStore } from 'vuex'
import auth from './modules/auth'
import sala from './modules/sala'

export default createStore({
  modules: {
    auth,
    sala,
  },
})
