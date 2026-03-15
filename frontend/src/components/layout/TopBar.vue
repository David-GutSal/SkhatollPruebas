<template>
  <div class="topbar">
    <div class="logo">
      <img src="@/assets/imgs/logo-provisional.png" alt="logo" />
    </div>

    <div class="auth">
      <form class="login">
        <input type="text" v-model="loginNombre" placeholder="Usuario" />
        <input type="password" v-model="loginPassword" placeholder="Contraseña" />
        <button @click.prevent="handleLogin">Login</button>
      </form>

      <form class="registro">
        <input type="text" v-model="registroNombre" placeholder="Usuario" />
        <input type="password" v-model="registroPassword" placeholder="Contraseña" />
        <button @click.prevent="handleRegistro">Registro</button>
      </form>
    </div>
  </div>
</template>

<script>
import axiosInstance from '@/plugins/axios'
import { mapActions } from 'vuex'
export default {
  name: 'TopBar',
  data() {
    return {
      loginNombre: '',
      loginPassword: '',
      registroNombre: '',
      registroPassword: '',
    }
  },
  methods: {
    ...mapActions('auth', ['login']),
    async handleLogin() {
      console.log('Intentando login con:', this.loginNombre)
      try {
        const res = await axiosInstance.post('/auth/login', {
          nombre: this.loginNombre,
          password: this.loginPassword,
        })
        console.log('Login exitoso:', res.data)
        this.login({ token: res.data.token, nombre: res.data.nombre, uuid: res.data.codigoUuid })
      } catch (error) {
        console.error('Error login:', error)
        console.error('Status:', error.response?.status)
        console.error('Data:', error.response?.data)
        alert(
          error.response?.status === 401 ? 'Credenciales incorrectas' : 'Error al iniciar sesión',
        )
      }
    },

    async handleRegistro() {
      console.log('Intentando registro con:', this.registroNombre)
      try {
        const res = await axiosInstance.post('/auth/registro', {
          nombre: this.registroNombre,
          password: this.registroPassword,
        })
        console.log('Registro exitoso:', res.data)
        this.login({ token: res.data.token, nombre: res.data.nombre, uuid: res.data.codigoUuid })
        alert('Registro exitoso. ¡Bienvenido ' + res.data.nombre + '!')
      } catch (error) {
        console.error('Error registro:', error)
        console.error('Status:', error.response?.status)
        console.error('Data:', error.response?.data)
        alert(error.response?.status === 409 ? 'Ese nombre ya está en uso' : 'Error al registrarse')
      }
    },
  },
}
</script>

<style scoped>
.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 30px;
  background: #111;
  color: white;
}
.logo img {
  height: 50px;
}
.auth {
  display: flex;
  gap: 20px;
}
.auth form {
  display: flex;
  gap: 5px;
}
.auth input {
  padding: 4px;
}
</style>
