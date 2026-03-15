import { gameState } from '../store/gameState'

export function enviarEvento(tipo, data) {
  // plantilla envío backend
  /*
stompClient.send(
"/app/evento",
{},
JSON.stringify({
tipo,
...data
})
)
*/
}

export function procesarEvento(payload) {
  switch (payload.tipo) {
    case 'FASE':
      gameState.fase = payload.fase

      break

    case 'VOTACION':
      Object.keys(payload.votos).forEach((nombre) => {
        const jugador = gameState.jugadores.find((j) => j.nombre === nombre)

        if (jugador) {
          jugador.votos = payload.votos[nombre]
        }
      })

      break

    case 'MUERTE':
      const muerto = gameState.jugadores.find((j) => j.nombre === payload.jugador)

      if (muerto) {
        muerto.vivo = false
      }

      break
  }
}
