
CREATE TABLE actiontype (
    id bigserial primary key,
    deleted boolean NOT NULL,
    modified Timestamp NOT NULL,
    name character varying(80) NOT NULL
);

create index ndx_actiontype_name on actiontype (name);
