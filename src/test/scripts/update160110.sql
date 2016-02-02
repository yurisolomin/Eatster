
begin transaction;

alter table "user" alter column promocode DROP NOT NULL;
update "user" set promocode = null;
alter table "user" alter column promocode TYPE character varying(10);
create unique index ndx_user_promocode on "user" (promocode);

commit;
