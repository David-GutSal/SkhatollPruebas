<template>
    <main class="contenido">
      <h1>Contenido aquí</h1>
    </main>
</template>

<script>
import TopBar from "@/components/layout/TopBar.vue"
import Header from "@/components/layout/Header.vue"
import Navbar from "@/components/layout/Navbar.vue"

export default {
  name:"InicioView",
  components:{
    TopBar,
    Header,
    Navbar
  }
}
</script>

<style scoped>
.pagina{
  display:grid;
  grid-template-rows: auto auto auto 1fr;
  min-height:100vh;
}

.contenido{
  padding:40px;
  text-align:center;
}
</style>