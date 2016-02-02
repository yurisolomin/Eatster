
begin transaction;

alter table restaurant alter column cuisine_id2 DROP NOT NULL;
alter table restaurant alter column cuisine_id3 DROP NOT NULL;

commit;
