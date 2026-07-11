create or replace package sb_core is
/*
    Author  : AJ
    Created : 2026-07-10 22:01:14
    Purpose : Main package of core functionality
    Version : 1.1.0
    History log:
    User      Date         Comments
    ------  -----------  ------------
      AJ    2026-07-10   #REF initial version v1.0.0 - new fuction "get_age_text" for TASK_1
      AJ    2026-07-11   #REF v1.1.0 - new function "get_pi" for TASK_2
  
*/
/*
  Returns corresponding text of provided age
*/
function get_age_text(pn_age number) return varchar2;
/*
  Does the actual work: validates precision, clears prior run,
  computes each BBP term, persists it, accumulates the total.
  A procedure (not a function) because it performs DML — not
  callable from SQL regardless, since the task requires
  persisting intermediate results.
*/
procedure retrieve_pi_value (
  pii_precision in  integer,
  pon_pi_value  out number);
/*
  Returns pi value till provided precision
  Bailey–Borwein–Plouffe formula, of calculating PI value
  Notice: It is not a common good practice to put DML logic inside function. This approach will only work with PL/SQL usage.
  Satisfies the task's required function purpose.
  Delegates all persistence to retrieve_pi_value per CQS — functions shouldn't own DML. 
  Still not SQL-callable, since the call chain includes DML.
*/
function get_pi_value(pi_precision integer) return number;  
end sb_core;
/
create or replace package body sb_core is
/*
  Author  : AJ
  Created : 2026-07-10 22:01:14
  Purpose : Main package of core functionality
  Version : 1.1.0
  History log:
  User      Date         Comments
  ------  -----------  ------------
    AJ    2026-07-10   #REF initial version v1.0.0 - new fuction "get_age_text" for TASK_1
    AJ    2026-07-11   #REF v1.1.0 - new function "get_pi" for TASK_2
*/
gci_max_age       constant integer := 150;
gci_max_precision constant integer := 38;
/*
  Returns corresponding text of provided age
*/
function get_age_text(pn_age number) return varchar2 is
  
  le_negative_age exception;
  ln_result       sb_age_to_text.age_text%type;
  
begin
  
  if nvl(pn_age,0) < 0 then
    raise le_negative_age;
  end if;
  
  select att.age_text
  into ln_result
  from sb_age_to_text att
  where
    pn_age >= att.age_from and
    pn_age < nvl(att.age_to,gci_max_age);
    
  return ln_result;

exception
  when le_negative_age then
    raise_application_error(
      -20000,
      'ERROR: Provided age -> "' || to_char(pn_age) ||
      '" is negative. Age should be a positive number.');
  when no_data_found then
    raise_application_error(
      -20000,
      'ERROR: Provided age -> "' || to_char(pn_age) ||
      '" is not defined in the designated table.');
      
end get_age_text;
/*
  Save intermediate data of calculating pi value
*/
procedure save_pi_iteration(
  pii_iteration in integer,
  pin_value     in number) is
  
  lv_user      sb_pi_calculations.created_by%type := 'SYSTEM';
  ld_calc_date sb_pi_calculations.creation_date%type := sysdate;
  
begin
  
  insert into sb_pi_calculations(
    pi_iteration, 
    pi_value, 
    created_by, 
    creation_date) 
  values(
    pii_iteration,
    pin_value,
    lv_user,
    ld_calc_date);

end save_pi_iteration;
/*
  Get iteration value of a pi, by formula
*/
function get_pi_iteration_value(pi_iteration integer) return number is
  ln_result number;
begin
  
  ln_result := (1 / power(16,pi_iteration)) * (4 / (8 * pi_iteration + 1) - 2 / (8 * pi_iteration + 4) - 1 / (8 * pi_iteration + 5) - 1 / (8 * pi_iteration + 6));
  
  return ln_result;
end get_pi_iteration_value;
/*
  Does the actual work: validates precision, clears prior run,
  computes each BBP term, persists it, accumulates the total.
  A procedure (not a function) because it performs DML — not
  callable from SQL regardless, since the task requires
  persisting intermediate results.
*/
procedure retrieve_pi_value (
  pii_precision in  integer,
  pon_pi_value  out number) is
  
  li_precision      number;
  
begin
  
  if nvl(pii_precision,0) < 0 then
    raise_application_error(-20000, 'ERROR: Precision cannot be negative.');
  end if;
  
  --In regards of Oracle type NUMBER behaviour, that it can hold only 38 significant digits -> any more than that is waste of resources
  if pii_precision > gci_max_precision then
    li_precision := gci_max_precision;
  else
    li_precision := pii_precision;
  end if;
  
  delete /*+ NO_PARALLEL(sb_pi_calculations) */ from sb_pi_calculations;

  for li_itr in 0..li_precision loop
    
    declare
      ln_inner_result number;
    begin
      
      ln_inner_result := get_pi_iteration_value(pi_iteration => li_itr);
      
      save_pi_iteration(
        pii_iteration => li_itr,
        pin_value     => ln_inner_result);
      
      pon_pi_value := nvl(pon_pi_value,0) + ln_inner_result;
    
    end;
  end loop;
  
  commit;

end retrieve_pi_value;
/*
  Returns pi value till provided precision
  Bailey–Borwein–Plouffe formula, of calculating PI value
  Notice: It is not a common good practice to put DML logic inside function. This approach will only work with PL/SQL usage.
  Satisfies the task's required function purpose.
  Delegates all persistence to retrieve_pi_value per CQS — functions shouldn't own DML. 
  Still not SQL-callable, since the call chain includes DML.
*/
function get_pi_value(pi_precision integer) return number is
  
  ln_result number;
  
begin
  
  retrieve_pi_value(
    pii_precision => pi_precision, 
    pon_pi_value => ln_result);
    
  return ln_result;
end get_pi_value;
end sb_core;
/
