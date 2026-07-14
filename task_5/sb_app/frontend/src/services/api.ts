import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
})

export interface AgeTextResponse {
  age: number
  text: string
}

export async function getAgeText(age: number): Promise<AgeTextResponse> {
  const response = await api.get<AgeTextResponse>('/age-text', { params: { age } })
  return response.data
}

export interface PiValueResponse {
  precision: number
  value: string
}

export async function getPiValue(precision: number): Promise<PiValueResponse> {
  const response = await api.get<PiValueResponse>('/pi', { params: { precision } })
  return response.data
}

export interface InvoiceResponse {
  invoiceId: number
  invoiceDate: string
}

export async function getUnpaidInvoices(): Promise<InvoiceResponse[]> {
  const response = await api.get<InvoiceResponse[]>('/invoices/unpaid')
  return response.data
}

export default api