<script setup lang="ts">
import { ref } from 'vue'
import { getAgeText } from '@/services/api'

const age = ref<number | null>(null)
const resultText = ref<string | null>(null)
const errorMessage = ref<string | null>(null)
const loading = ref(false)

async function submit() {
  errorMessage.value = null
  resultText.value = null

  if (age.value === null) {
    errorMessage.value = 'Please enter an age.'
    return
  }

  loading.value = true
  try {
    const result = await getAgeText(age.value)
    resultText.value = result.text
  } catch (err: any) {
    errorMessage.value = err.response?.data?.error ?? 'Something went wrong.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="age-view">
    <h1>Age to Text</h1>
    <form @submit.prevent="submit">
      <label for="age">Age:</label>
      <input id="age" type="number" v-model.number="age" />
      <button type="submit" :disabled="loading">
        {{ loading ? 'Loading...' : 'Submit' }}
      </button>
    </form>

    <p v-if="resultText" class="result">{{ resultText }}</p>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </div>
</template>

<style scoped>
.error {
  color: red;
}
.result {
  font-weight: bold;
}
</style>