
begin transaction;

alter table restaurant add cards_jcb boolean;
update restaurant set cards_jcb = false;
alter table restaurant alter column cards_jcb set NOT NULL;

alter table restaurant add cards_mir boolean;
update restaurant set cards_mir = false;
alter table restaurant alter column cards_mir set NOT NULL;

commit;
