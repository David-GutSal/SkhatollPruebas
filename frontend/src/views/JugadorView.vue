<template>
  <div class="contenedor-jugador">
    <CabeceraJugador :nombreJugador="nombre" :esDia="esDia" />

    <PanelVotacionesJugador
      :esDia="esDia"
      :votacionActiva="votacionActiva"
      :jugadorSeleccionado="jugadorSeleccionado"
      @votarAlcalde="votarAlcalde"
      @votarCulpable="votarCulpable"
    />

    <MesaJugadores
      :jugadores="jugadoresVisibles"
      :jugadorSeleccionado="jugadorSeleccionado"
      :esDia="esDia"
      @seleccionarJugador="seleccionarJugador"
    />

    <BotonMiRol :miRol="miRol" />

    <ZonaPoderes
      :miRol="miRol"
      :jugadorSeleccionado="jugadorSeleccionado"
      :esMiTurno="esMiTurno"
      @devorar="devorarJugador"
      @premonicion="usarPremonicion"
    />
  </div>
</template>

<script>
import axiosInstance from '@/plugins/axios'
import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { mapGetters } from 'vuex'
import CabeceraJugador from '@/components/juego/CabeceraJugador.vue'
import PanelVotacionesJugador from '@/components/juego/PanelVotacionesJugador.vue'
import MesaJugadores from '@/components/juego/MesaJugadores.vue'
import BotonMiRol from '@/components/juego/BotonMiRol.vue'
import ZonaPoderes from '@/components/juego/ZonaPoderes.vue'

export default {
  components: {
    CabeceraJugador,
    PanelVotacionesJugador,
    MesaJugadores,
    BotonMiRol,
    ZonaPoderes,
  },

  data() {
    return {
      esDia: true,
      votacionActiva: false,
      esMiTurno: false,
      jugadorSeleccionado: null,
      stompClient: null,
    }
  },

  computed: {
    ...mapGetters('auth', ['nombre']),
    ...mapGetters('sala', ['codigoSala', 'jugadores', 'miRol']),
    jugadoresVisibles() {
      if (!this.esDia && this.miRol && this.miRol.toLowerCase() === 'lobo') {
        return this.jugadores.filter((j) => j.nombre !== this.nombre)
      }
      return this.jugadores
    },
  },

  async created() {
    const res = await axiosInstance.get(`/salas/${this.codigoSala}/jugadores`)
    this.$store.dispatch('sala/setJugadores', res.data)
    this.conectarWebSocket()
  },

  methods: {
    seleccionarJugador(j) {
      this.jugadorSeleccionado = j
    },

    async votarAlcalde() {
      if (!this.jugadorSeleccionado) return
      try {
        await axiosInstance.post(`/partida/${this.codigoSala}/votar`, {
          idObjetivo: this.jugadorSeleccionado.idUsuario,
        })
      } catch (error) {
        alert('Error al votar')
      }
    },

    async votarCulpable() {
      if (!this.jugadorSeleccionado) return
      try {
        await axiosInstance.post(`/partida/${this.codigoSala}/votar`, {
          idObjetivo: this.jugadorSeleccionado.idUsuario,
        })
      } catch (error) {
        alert('Error al votar')
      }
    },

    async devorarJugador() {
      if (!this.jugadorSeleccionado) return
      try {
        await axiosInstance.post(`/partida/${this.codigoSala}/votar`, {
          idObjetivo: this.jugadorSeleccionado.idUsuario,
        })
      } catch (error) {
        alert('Error al devorar')
      }
    },

    usarPremonicion() {
      if (!this.jugadorSeleccionado) return
      // La vidente solo ve el rol si el narrador le da permiso (esMiTurno)
      // Por ahora mostramos el nombre, el rol no viene en la lista de jugadores
      alert('Has usado tu premonición sobre: ' + this.jugadorSeleccionado.nombre)
    },

    conectarWebSocket() {
      const token = this.$store.getters['auth/token']
      const cliente = new Client({
        webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
        connectHeaders: { Authorization: `Bearer ${token}` },
      })
      cliente.onConnect = () => {
        cliente.subscribe(`/topic/partida/${this.codigoSala}/fase`, (msg) => {
          const payload = JSON.parse(msg.body)
          this.esDia = payload.fase === 'DIA'
        })
        cliente.subscribe(`/topic/partida/${this.codigoSala}/muerte`, (msg) => {
          const payload = JSON.parse(msg.body)
          this.$store.dispatch('sala/marcarMuerto', payload.nombreJugador)
        })
        cliente.subscribe(`/topic/partida/${this.codigoSala}/votos`, (msg) => {
          const payload = JSON.parse(msg.body)
          this.$store.dispatch('sala/actualizarVotos', payload.votos)
        })
        cliente.subscribe(`/topic/partida/${this.codigoSala}/votacion`, (msg) => {
          const payload = JSON.parse(msg.body)
          this.votacionActiva = payload.abierta ?? false
        })
        cliente.subscribe(`/topic/partida/${this.codigoSala}/fin`, (msg) => {
          const payload = JSON.parse(msg.body)
          this.$store.dispatch('sala/setResultado', {
            bandoGanador: payload.bandoGanador,
            mensaje: payload.mensaje,
          })
          this.$router.push({ name: 'resultados' })
        })
      }
      cliente.activate()
      this.stompClient = cliente
    },
  },
}
</script>

<style scoped>
.contenedor-jugador {
  min-height: 100vh;
  padding: 20px;

  display: flex;
  flex-direction: column;
  gap: 20px;
}
</style>
