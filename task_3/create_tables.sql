create table SB_INVOICES
(
  invoice_id     NUMBER not null,
  invoice_date   DATE not null,
  invoice_amount NUMBER not null,
  currency       VARCHAR2(3) not null
);
-- Add comments to the columns 
comment on column SB_INVOICES.invoice_id
  is 'PK';
comment on column SB_INVOICES.invoice_date
  is 'Invoice date';
comment on column SB_INVOICES.invoice_amount
  is 'Invoice amount';
comment on column SB_INVOICES.currency
  is 'invoice currency';
-- Create/Recreate primary, unique and foreign key constraints 
alter table SB_INVOICES
  add constraint INVOICE_PK primary key (INVOICE_ID);
-- Create table
create table SB_PAYMENTS
(
  payment_id     NUMBER not null,
  payment_date   DATE,
  payment_amount NUMBER,
  currency       VARCHAR2(3),
  invoice_id     NUMBER
);
-- Add comments to the columns 
comment on column SB_PAYMENTS.payment_id
  is 'PK';
comment on column SB_PAYMENTS.payment_date
  is 'Payment date';
comment on column SB_PAYMENTS.payment_amount
  is 'Payment amount';
comment on column SB_PAYMENTS.currency
  is 'Payment currency';
comment on column SB_PAYMENTS.invoice_id
  is 'Reference to SB_INVOICES';
-- Create/Recreate primary, unique and foreign key constraints 
alter table SB_PAYMENTS
  add constraint PAYMENT_PK primary key (PAYMENT_ID);
alter table SB_PAYMENTS
  add constraint PAYMENT_INVOICE_FK foreign key (INVOICE_ID)
  references SB_INVOICES (INVOICE_ID);
