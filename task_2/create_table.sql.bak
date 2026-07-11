-- Create table
create table SB_PI_CALCULATIONS
(
  pi_iteration  INTEGER not null,
  pi_value      NUMBER not null,
  created_by    VARCHAR2(30) not null,
  creation_date DATE not null
)
tablespace DATA
  pctfree 10
  initrans 10
  maxtrans 255;
-- Add comments to the table 
comment on table SB_PI_CALCULATIONS
  is 'Pi calculations by Bailey-Borwein-Plouffe formula';
-- Add comments to the columns 
comment on column SB_PI_CALCULATIONS.pi_iteration
  is 'Precision or iterator, to what value we use formula';
comment on column SB_PI_CALCULATIONS.pi_value
  is 'Calculated pi value with provided precision/iteration';
comment on column SB_PI_CALCULATIONS.created_by
  is 'Who created';
comment on column SB_PI_CALCULATIONS.creation_date
  is 'When created';
