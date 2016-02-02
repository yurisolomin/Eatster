
CREATE TABLE "user" (
    id bigserial primary key,
    deleted boolean NOT NULL,
    modified Timestamp NOT NULL,
    name character varying(80) NOT NULL,
    birthday character varying(10) NOT NULL,
    gender character varying(1) NOT NULL,
    email character varying(80) NOT NULL,
    phone character varying(12) NOT NULL,
    registration_date character varying(10) NOT NULL,
    referrals int NOT NULL,
    password character varying(16) NOT NULL,
    promocode character varying(10) NULL,
    bonus_activated boolean NOT NULL,
    bonus_date_end character varying(10) NOT NULL,
    friend_user_id bigint references "user"(id) NULL,
    friend_bonus_date_end character varying(10) NOT NULL,
    friend_promocode character varying(10) NOT NULL
);

create unique index ndx_user_phone on "user" (phone);
create unique index ndx_user_promocode on "user" (promocode);
create index ndx_user_friend_user_id on "user" (friend_user_id);

