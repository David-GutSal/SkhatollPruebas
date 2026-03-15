<template>
  <div class="contenedor-lobby">
    <div class="grid-lobby">
      <div class="panel-izquierdo">
        <h2 v-if="esCreador">Código sala</h2>
        <h2 v-else>Introduce aquí el código de la sala</h2>

        <input v-if="esCreador" class="input-codigo" :value="codigoSala" readonly />

        <input
          v-else
          class="input-codigo"
          v-model="inputCodigo"
          placeholder="Escribe el código de sala"
        />
        <div class="botones">
          <button v-if="esCreador" class="boton" @click="iniciarPartida">Iniciar partida</button>

          <button v-if="!esCreador" class="boton" @click="handleUnirse">Unirse</button>

          <button v-if="!esCreador" class="boton-salir" @click="salirSala">
            Salir de la partida
          </button>
        </div>

        <p v-if="mensajeUnion" class="mensaje">
          ¡Te has unido a la partida!<br />
          Ten paciencia y espera al resto de jugadores
        </p>
      </div>

      <div class="panel-jugadores">
        <h2>Jugadores en sala</h2>
        <ul>
          <li v-for="jugador in jugadores" :key="jugador.idUsuario">
            <label v-if="esCreador">
              <input
                type="radio"
                name="narrador"
                :checked="jugador.esNarrador"
                @change="asignarNarrador(jugador.idUsuario)"
              />
              {{ jugador.nombre }}
            </label>
            <span v-else>
              {{ jugador.nombre }}
            </span>
          </li>
        </ul>

        <p v-if="esCreador" class="mensaje">
          Puedes seleccionar a otro jugador como narrador o serlo tú mismo
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import axiosInstance from '@/plugins/axios'
import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { mapGetters, mapActions } from 'vuex'
export default {
  data() {
    return {
      inputCodigo: '',
      mensajeUnion: false,
      stompClient: null,
    }
  },
  computed: {
    ...mapGetters('sala', ['codigoSala', 'esCreador', 'jugadores']),
  },
  created() {
    if (this.esCreador) {
      this.cargarJugadores()
      this.conectarWebSocket()
    }
  },
  methods: {
    ...mapActions('sala', ['unirse', 'setJugadores', 'salir']),
    async cargarJugadores() {
      try {
        const res = await axiosInstance.get(`/salas/${this.codigoSala}/jugadores`)
        this.setJugadores(res.data)
      } catch (error) {
        alert('Error al cargar jugadores')
      }
    },
    async iniciarPartida() {
      try {
        await axiosInstance.post(`/salas/${this.codigoSala}/iniciar`)
      } catch (error) {
        alert(error.response?.status === 409 ? 'No hay suficientes jugadores' : 'Error al iniciar')
      }
    },
    async asignarNarrador(idUsuario) {
      try {
        await axiosInstance.put(`/salas/${this.codigoSala}/narrador`, { idUsuario })
      } catch (error) {
        alert('Error al asignar narrador')
      }
    },
    async handleUnirse() {
      try {
        await axiosInstance.post('/salas/unirse', { codigoSala: this.inputCodigo })
        this.unirse(this.inputCodigo)
        this.mensajeUnion = true
        await this.cargarJugadores()
        this.conectarWebSocket()
      } catch (error) {
        alert(
          error.response?.status === 409
            ? 'Ya estás en esta sala o está llena'
            : 'Sala no encontrada',
        )
      }
    },
    salirSala() {
      this.salir()
      this.$router.push({ name: 'sala' })
    },
    conectarWebSocket() {
      const token = this.$store.getters['auth/token']
      const cliente = new Client({
        webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
        connectHeaders: { Authorization: `Bearer ${token}` },
      })
      cliente.onConnect = () => {
        cliente.subscribe(`/topic/sala/${this.codigoSala}`, (msg) => {
          const payload = JSON.parse(msg.body)
          if (payload.tipo === 'JUGADOR_UNIDO') this.setJugadores(payload.jugadores)
        })
        cliente.subscribe(`/topic/sala/${this.codigoSala}/inicio`, () => {
          this.$router.push({ name: 'cargaRol' })
        })
        cliente.subscribe(`/user/queue/rol`, (msg) => {
          const payload = JSON.parse(msg.body)
          if (payload.tipo === 'ROL_ASIGNADO') {
            this.$store.dispatch('sala/setRol', {
              nombreRol: payload.nombreRol,
              descripcionRol: payload.descripcionRol,
              bando: payload.bando,
            })
          }
        })
      }
      cliente.activate()
      this.stompClient = cliente
    },
  },
}
</script>

<style scoped>
.contenedor-lobby {
  display: flex;
  justify-content: center;
  padding: 40px;
}

.grid-lobby {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 30px;
  width: 100%;
  max-width: 900px;
}

.panel-izquierdo,
.panel-jugadores {
  background: #1f1f1f;
  padding: 30px;
  border-radius: 10px;
  color: white;
}

.input-codigo {
  width: 100%;
  padding: 12px;
  border-radius: 8px;
  border: none;
  margin-top: 10px;
  margin-bottom: 20px;
}

.botones {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.boton {
  background: #8b0000;
  color: white;
  border: none;
  padding: 12px 20px;
  border-radius: 8px;
  cursor: pointer;
}

.boton:hover {
  background: #a30000;
}

.boton-salir {
  background: #333;
  color: white;
  border: none;
  padding: 12px 20px;
  border-radius: 8px;
  cursor: pointer;
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

@media (max-width: 768px) {
  .grid-lobby {
    grid-template-columns: 1fr;
  }
}
</style>
