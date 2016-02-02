
begin transaction;

drop index ndx_user_phone;
create unique index ndx_user_phone on "user" (phone);

drop index ndx_partner_name;
create unique index ndx_partner_name on partner (name);

drop index ndx_waiter_name;
create unique index ndx_waiter_login on waiter (login);

commit;
