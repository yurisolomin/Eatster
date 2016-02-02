
CREATE TABLE cuisine (
    id bigserial primary key,
    deleted boolean NOT NULL,
    modified Timestamp NOT NULL,
    name character varying(80) NOT NULL
);

create index ndx_cuisine_name on cuisine (name);
