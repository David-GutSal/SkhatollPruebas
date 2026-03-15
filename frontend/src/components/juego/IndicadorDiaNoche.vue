<template>
  <div class="selector">
    <div class="sol" @click="$emit('cambiarFase', 'dia')">
      <i class="fas fa-sun"></i>
    </div>

    <div class="luna" @click="$emit('cambiarFase', 'noche')">
      <i class="fas fa-moon"></i>
    </div>
  </div>
</template>

<style scoped>
.selector {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  flex-wrap: wrap;
  width: 100%;
}

.sol,
.luna {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  flex-shrink: 0;
}

.sol {
  font-size: 30pt;
  background: white;
  color: black;
}

.luna {
  font-size: 30pt;
  background: black;
  color: white;
}
</style>
