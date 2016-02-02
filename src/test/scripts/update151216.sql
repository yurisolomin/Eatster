
begin transaction;

alter table "operation" add status character varying(10);
update "operation" set status = 'confirmed';
alter table "operation" alter column status set NOT NULL;

alter table "operation" drop column deleted;

alter table "operation" add sms_code character varying(10);
update "operation" set sms_code = '';
alter table "operation" alter column sms_code set NOT NULL;

alter table "operation" add sms_time Timestamp;

commit;
