select
  inv.invoice_id,
  inv.invoice_date
from 
  sb_invoices inv
left join sb_payments pay on pay.invoice_id = inv.invoice_id and
  pay.currency = inv.currency
group by
  inv.invoice_id,
  inv.invoice_date,
  inv.invoice_amount
having 
  inv.invoice_amount > nvl(sum(pay.payment_amount),0)
order by
  inv.invoice_id
