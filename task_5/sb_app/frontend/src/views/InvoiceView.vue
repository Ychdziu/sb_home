<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getUnpaidInvoices, type InvoiceResponse } from '@/services/api'

const invoices = ref<InvoiceResponse[]>([])
const errorMessage = ref<string | null>(null)
const loading = ref(false)

async function loadInvoices() {
  errorMessage.value = null
  loading.value = true
  try {
    invoices.value = await getUnpaidInvoices()
  } catch (err: any) {
    errorMessage.value = err.response?.data?.error ?? 'Something went wrong.'
  } finally {
    loading.value = false
  }
}

onMounted(loadInvoices)
</script>

<template>
  <div class="invoice-view">
    <h1>Unpaid Invoices</h1>
    <p v-if="loading">Loading...</p>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

    <table v-if="!loading && invoices.length">
      <thead>
        <tr>
          <th>Invoice ID</th>
          <th>Invoice Date</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="invoice in invoices" :key="invoice.invoiceId">
          <td>{{ invoice.invoiceId }}</td>
          <td>{{ invoice.invoiceDate }}</td>
        </tr>
      </tbody>
    </table>

    <p v-if="!loading && !invoices.length && !errorMessage">No unpaid invoices found.</p>
  </div>
</template>

<style scoped>
.error {
  color: red;
}
table {
  border-collapse: collapse;
  margin-top: 1rem;
}
th, td {
  border: 1px solid #ccc;
  padding: 0.5rem 1rem;
  text-align: left;
}
</style>