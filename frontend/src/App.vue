<template>
  <div id="app">
    <TopBar/>
    <Header/>
    <Navbar/>
    <router-view/>
  </div>
</template>

<script>
import TopBar from "@/components/layout/TopBar.vue"
import Header from "@/components/layout/Header.vue"
import Navbar from "@/components/layout/Navbar.vue"

export default {
  name: "App",
  components: { TopBar, Header, Navbar }
}
</script>

<style>
#app {
  display: grid;
  grid-template-rows: auto auto auto 1fr;
  min-height: 100vh;
}

#app > router-view {
  padding: 20px;
}
</style>