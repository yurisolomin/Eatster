
CREATE TABLE subway (
    id bigserial primary key,
    deleted boolean NOT NULL,
    modified Timestamp NOT NULL,
    name character varying(80) NOT NULL
);

create index ndx_subway_name on subway (name);
