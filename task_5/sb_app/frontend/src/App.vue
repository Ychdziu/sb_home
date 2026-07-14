<script setup lang="ts">
</script>

<template>
  <nav>
    <RouterLink to="/age">Age</RouterLink>
    <RouterLink to="/pi">Pi</RouterLink>
    <RouterLink to="/invoices">Invoices</RouterLink>
  </nav>
  <RouterView />
</template>

<style scoped>
nav {
  display: flex;
  gap: 1rem;
  padding: 1rem;
}
</style>