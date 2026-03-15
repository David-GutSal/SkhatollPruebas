<template>
  <div class="mirol">
    <button @click="mostrarRol">Mi Rol</button>
  </div>
</template>

<script>
export default {
  props: ['miRol'],

  methods: {
    mostrarRol() {
      alert('Tu rol es: ' + this.miRol)
    },
  },
}
</script>

<style scoped>
.mirol {
  display: flex;
  justify-content: center;
}

button {
  background: black;
  color: white;

  padding: 10px 20px;
}
</style>
