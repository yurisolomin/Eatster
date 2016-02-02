
CREATE TABLE waiter (
    id bigserial primary key,
    deleted boolean NOT NULL,
    modified Timestamp NOT NULL,
    name character varying(80) NOT NULL,
    restaurant_id bigint references restaurant(id) NOT NULL,
    login character varying(20) NOT NULL,
    password character varying(20) NOT NULL
);

create unique index ndx_waiter_login on waiter (login);
