<template>
  <div class="contenedor-carga">
    <div class="tarjeta-rol" v-if="nombreRol">
      <img class="imagen-personaje" :src="imagenUrl" alt="rol" />
      <div class="info-rol">
        <h2>Tu rol es: {{ nombreRol }}</h2>
        <p class="descripcion">{{ descripcionRol }}</p>
        <p class="bando">Bando: {{ bando }}</p>
      </div>
    </div>

    <div v-else class="esperando">
      <p>Asignando rol...</p>
    </div>

    <p class="mensaje-espera">Espera a que el narrador inicie la partida...</p>

    <div class="slider-frases">
      <p class="frase" v-for="(frase, index) in frases" :key="index" v-show="index === fraseActual">
        "{{ frase }}"
      </p>
    </div>
  </div>
</template>

<script>
// Para ver esta página antes de prepararla para backend hayq ue usar este enlace: http://localhost:5173/carga-rol

// =============================
// IMPORTS PARA BACKEND (WEBSOCKET)
// =============================
// DESCOMENTAR CUANDO SE CONECTE CON SPRING BOOT

import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'

import BrujaImg from '@/assets/imgs/bruja.jpg'
import { mapActions } from 'vuex'
export default {
  name: 'CargaRolView',
  data() {
    return {
      nombreRol: '',
      descripcionRol: '',
      bando: '',
      imagenUrl: null,
      frases: [
        'Cuando las barbas de tu vecino veas cortar pon las tuyas a remojar ',
        'El lobo ya no come la carne que quiere, sino la que puede',
        'Cuidado con el lobo con piel de cordero',
        'Las apariencias engañan, la criatura más dulce puede ser la más diabólica',
      ],
      fraseActual: 0,
      stompClient: null,
    }
  },

  created() {
    const esCreador = this.$store.getters['sala/esCreador']
    if (esCreador) {
      this.$router.push({ name: 'narrador' })
      return
    }
    setInterval(() => {
      this.fraseActual = (this.fraseActual + 1) % this.frases.length
    }, 8000)

    const nombreRol = this.$store.getters['sala/miRol']
    if (nombreRol) {
      this.nombreRol = nombreRol
      this.descripcionRol = this.$store.getters['sala/miRolDescripcion']
      this.bando = this.$store.getters['sala/miBando']
      this.imagenUrl = this.getImagenPorRol(nombreRol)
      setTimeout(() => this.$router.push({ name: 'jugador' }), 5000)
    } else {
      const unwatch = this.$watch(
        () => this.$store.getters['sala/miRol'],
        (nuevoRol) => {
          if (nuevoRol) {
            this.nombreRol = nuevoRol
            this.descripcionRol = this.$store.getters['sala/miRolDescripcion']
            this.bando = this.$store.getters['sala/miBando']
            this.imagenUrl = this.getImagenPorRol(nuevoRol)
            setTimeout(() => this.$router.push({ name: 'jugador' }), 5000)
            unwatch()
          }
        },
      )
    }
  },
  methods: {
    ...mapActions('sala', ['setRol']),
    conectarWebSocket() {
      const token = this.$store.getters['auth/token']
      const codigoSala = this.$store.getters['sala/codigoSala']
      const socket = new SockJS('http://localhost:8080/ws')
      this.stompClient = new Client({
        webSocketFactory: () => socket,
        connectHeaders: { Authorization: `Bearer ${token}` },
        reconnectDelay: 5000,
      })
      this.stompClient.onConnect = () => {
        this.stompClient.subscribe('/user/queue/rol', (message) => {
          const payload = JSON.parse(message.body)
          if (payload.tipo === 'ROL_ASIGNADO') {
            this.nombreRol = payload.nombreRol
            this.descripcionRol = payload.descripcionRol
            this.bando = payload.bando
            this.imagenUrl = this.getImagenPorRol(payload.nombreRol)
            this.setRol({
              nombreRol: payload.nombreRol,
              descripcionRol: payload.descripcionRol,
              bando: payload.bando,
            })
            setTimeout(() => this.$router.push({ name: 'jugador' }), 5000)
          }
        })
      }
      this.stompClient.onStompError = (frame) => {
        console.error('Error STOMP:', frame.headers['message'])
      }
      this.stompClient.activate()
    },
    getImagenPorRol(nombreRol) {
      const map = { BRUJA: BrujaImg, LOBO: BrujaImg }
      return map[nombreRol.toUpperCase()] || BrujaImg
    },
  },
}
</script>

<style scoped>
.contenedor-carga {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: #8b0000;
  color: white;
  text-align: center;
}

.tarjeta-rol {
  background-color: #1f1f1f;
  border-radius: 15px;
  padding: 30px;
  max-width: 400px;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.imagen-personaje {
  width: 150px;
  height: 150px;
  object-fit: contain;
  margin-bottom: 20px;
}

.info-rol h2 {
  margin-bottom: 15px;
  font-size: 22px;
}

.descripcion {
  margin-bottom: 15px;
  font-size: 16px;
  line-height: 1.4;
}

.bando {
  font-weight: bold;
  font-size: 16px;
  color: #dddddd;
}

.mensaje-espera {
  margin-top: 30px;
  font-size: 16px;
  color: #ffffff;
}

.slider-frases {
  margin-top: 40px;
  height: 30px;
  overflow: hidden;
}

.frase {
  font-style: italic;
  color: white;
  transition: opacity 0.5s;
  font-size: 16px;
}

@media (max-width: 480px) {
  .tarjeta-rol {
    padding: 20px;
  }

  .imagen-personaje {
    width: 200px;
    height: 200px;
  }

  .info-rol h2 {
    font-size: 20px;
  }

  .descripcion,
  .bando,
  .mensaje-espera,
  .frase {
    font-size: 14px;
  }
}
</style>
