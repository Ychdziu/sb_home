create or replace package sb_core is
/*
    Author  : AJ
    Created : 2026-07-10 22:01:14
    Purpose : Main package of core functionality
    Version : 1.0.0
    History log:
    User      Date         Comments
    ------  -----------  ------------
      AJ    2026-07-10   #REF initial version v1.0.0 - new fuction "get_age_text" for TASK_1
  
*/
/*
  Returns corresponding text of provided age
*/
function get_age_text(pn_age number) return varchar2;
end sb_core;
/
create or replace package body sb_core is
/*
  Author  : AJ
  Created : 2026-07-10 22:01:14
  Purpose : Main package of core functionality
  Version : 1.0.0
  History log:
  User      Date         Comments
  ------  -----------  ------------
    AJ    2026-07-10   #REF initial version v1.0.0 - new fuction "get_age_text" for TASK_1
  
*/
gcn_max_age constant number := 150;
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
    pn_age < nvl(att.age_to,gcn_max_age);
    
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
end sb_core;
/
