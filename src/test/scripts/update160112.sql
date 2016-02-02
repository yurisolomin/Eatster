
begin transaction;

alter table "user" add column bonus_activated boolean NOT NULL default false;
alter table "user" add column bonus_date_end character varying(10) NOT NULL default '';
alter table "user" add column friend_user_id bigint references "user"(id) NULL;
alter table "user" add column friend_bonus_date_end character varying(10) NOT NULL default '';
alter table "user" add column friend_promocode character varying(10) NOT NULL default '';

create index ndx_user_friend_user_id on "user" (friend_user_id);

commit;
