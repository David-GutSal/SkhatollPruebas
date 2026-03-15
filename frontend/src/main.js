import { createApp } from 'vue'
import App from './App.vue'
import router from './router/index'
import store from './store/index'
import axiosInstance from './plugins/axios'
import './assets/styles/main.css'

const app = createApp(App)

axiosInstance.interceptors.request.use((config) => {
  const token = store.getters['auth/token']
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

app.use(router)
app.use(store)

app.mount('#app')
