// src/store/GameEvents.js

import { gameState } from './gameState'

// Tipos de eventos que puede procesar el juego
// FAKE: sirve para simulación sin backend
export const GameEvents = {
  // Cambiar la fase del juego: 'DIA' o 'NOCHE'
  cambiarFase(fase) {
    if (fase !== 'DIA' && fase !== 'NOCHE') return
    gameState.fase = fase
    console.log(`Fase cambiada a: ${fase}`)
  },

  // Votación: payload = { tipo: 'VOTACION', votos: { Jugador1: 1, Jugador2: 2 } }
  procesarVotacion(votos) {
    Object.keys(votos).forEach((nombre) => {
      const jugador = gameState.jugadores.find((j) => j.nombre === nombre)
      if (jugador) jugador.votos = votos[nombre]
    })
    console.log(
      'Votos actualizados:',
      gameState.jugadores.map((j) => ({ nombre: j.nombre, votos: j.votos })),
    )
  },

  // Marcar jugador como muerto: payload = { tipo: 'MUERTE', jugador: 'Jugador1' }
  marcarMuerto(nombreJugador) {
    const jugador = gameState.jugadores.find((j) => j.nombre === nombreJugador)
    if (jugador) {
      jugador.estaVivo = false
      console.log(`Jugador ${nombreJugador} ha muerto`)
    }
  },

  // Designar alcalde: payload = { tipo: 'ALCALDE', jugador: 'Jugador3' }
  designarAlcalde(nombreJugador) {
    gameState.jugadores.forEach((j) => (j.alcalde = false)) // Resetear alcalde anterior
    const jugador = gameState.jugadores.find((j) => j.nombre === nombreJugador)
    if (jugador) jugador.alcalde = true
    console.log(`Jugador ${nombreJugador} es el nuevo alcalde`)
  },

  // Evento de turno nocturno: payload = { tipo: 'TURNO', jugador: 'Jugador2' }
  turnoNocturno(nombreJugador) {
    gameState.jugadores.forEach((j) => (j.esTurno = false))
    const jugador = gameState.jugadores.find((j) => j.nombre === nombreJugador)
    if (jugador) jugador.esTurno = true
    console.log(`Es el turno de ${nombreJugador} en la noche`)
  },

  // Reiniciar votos (para nueva votación)
  reiniciarVotos() {
    gameState.jugadores.forEach((j) => (j.votos = 0))
    console.log('Votos reiniciados')
  },

  // Evento genérico de simulación para prueba sin backend
  eventoSimulado(tipo, payload) {
    switch (tipo) {
      case 'FASE':
        this.cambiarFase(payload.fase)
        break
      case 'VOTACION':
        this.procesarVotacion(payload.votos)
        break
      case 'MUERTE':
        this.marcarMuerto(payload.jugador)
        break
      case 'ALCALDE':
        this.designarAlcalde(payload.jugador)
        break
      case 'TURNO':
        this.turnoNocturno(payload.jugador)
        break
      case 'REINICIAR_VOTOS':
        this.reiniciarVotos()
        break
      default:
        console.warn('Evento desconocido:', tipo)
    }
  },
}

/* 
USO:
1. Importar GameEvents y gameState en cualquier vista:

import { gameState } from '@/store/gameState'
import { GameEvents } from '@/store/GameEvents'

2. Simular eventos:

GameEvents.cambiarFase('NOCHE')
GameEvents.marcarMuerto('Jugador3')
GameEvents.designarAlcalde('Jugador5')
GameEvents.procesarVotacion({Jugador1: 2, Jugador2: 3})
GameEvents.turnoNocturno('Jugador6')

3. Las vistas reactivas (NarradorView, JugadorView) se actualizarán automáticamente.
*/
