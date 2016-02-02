
begin transaction;

alter table action drop column infoworkdays;
alter table action drop column actiontimerange_id;
alter table "user" drop column new_password;
alter table "user" drop column new_password_time;
alter table "user" drop column registred;

alter table action add start_time character varying(5);
update action set start_time = '';
alter table action alter column start_time set NOT NULL;

alter table action add end_time character varying(5);
update action set end_time = '';
alter table action alter column end_time set NOT NULL;


commit;
