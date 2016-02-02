
CREATE TABLE actionsubtype (
    id bigserial primary key,
    deleted boolean NOT NULL,
    modified Timestamp NOT NULL,
    name character varying(80) NOT NULL,
    actiontype_id bigint references actiontype(id) NOT NULL
);

