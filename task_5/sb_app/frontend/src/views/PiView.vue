<script setup lang="ts">
import { ref } from 'vue'
import { getPiValue } from '@/services/api'

const precision = ref<number | null>(null)
const resultValue = ref<string | null>(null)
const errorMessage = ref<string | null>(null)
const loading = ref(false)

async function submit() {
  errorMessage.value = null
  resultValue.value = null

  if (precision.value === null) {
    errorMessage.value = 'Please enter a precision.'
    return
  }

  loading.value = true
  try {
    const result = await getPiValue(precision.value)
    resultValue.value = result.value
  } catch (err: any) {
    errorMessage.value = err.response?.data?.error ?? 'Something went wrong.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="pi-view">
    <h1>Pi Calculator</h1>
    <form @submit.prevent="submit">
      <label for="precision">Precision:</label>
      <input id="precision" type="number" v-model.number="precision" />
      <button type="submit" :disabled="loading">
        {{ loading ? 'Loading...' : 'Submit' }}
      </button>
    </form>

    <p v-if="resultValue" class="result">{{ resultValue }}</p>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </div>
</template>

<style scoped>
.error {
  color: red;
}
.result {
  font-weight: bold;
  font-family: monospace;
}
</style>