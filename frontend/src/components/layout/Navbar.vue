<template>
  <nav class="navbar">
    <router-link to="/">Inicio</router-link>
    <router-link to="/sala">Sala de juegos</router-link>
    <router-link to="/reglas">Reglas y personajes</router-link>
    <router-link to="/contacto">Contacto</router-link>
  </nav>
</template>

<script>
export default {
  name:"Navbar"
}
</script>

<style scoped>
.navbar{
  display:flex;
  justify-content:center;
  gap:40px;
  padding:15px;
  background:#222;
}

.navbar a{
  color:white;
  text-decoration:none;
  font-weight:bold;
}
</style>