import { createRouter, createWebHistory } from 'vue-router'

import InicioView from '../views/InicioView.vue'
import SalaView from '../views/SalaView.vue'
import ReglasPersonajesView from '../views/ReglasPersonajesView.vue'
import ContactoView from '../views/ContactoView.vue' /* Esta es provisional. Quizás hay algo mejor para poner aquí*/

import LobbyView from '../views/LobbyView.vue'
import CargaRolView from '../views/CargaRolView.vue'
import NarradorView from '../views/NarradorView.vue'
import JugadorView from '../views/JugadorView.vue'
import ResultadosView from '../views/ResultadosView.vue'

const routes = [
  {
    path: '/',
    name: 'inicio',
    component: InicioView,
  },
  {
    path: '/sala',
    name: 'sala',
    component: SalaView,
  },
  {
    path: '/reglas',
    name: 'reglas',
    component: ReglasPersonajesView,
  },
  {
    path: '/contacto',
    name: 'contacto',
    component: ContactoView,
  },

  {
    path: '/lobby',
    name: 'lobby',
    component: LobbyView,
  },

  {
    path: '/carga-rol',
    name: 'cargaRol',
    component: CargaRolView,
  },
  {
    path: '/narrador',
    name: 'narrador',
    component: NarradorView,
  },
  {
    path: '/jugador',
    name: 'jugador',
    component: JugadorView,
  },
  {
    path: '/resultados',
    name: 'resultados',
    component: ResultadosView,
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
