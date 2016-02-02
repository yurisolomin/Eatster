
CREATE TABLE averagecheck (
    id bigserial primary key,
    deleted boolean NOT NULL,
    modified Timestamp NOT NULL,
    name character varying(80) NOT NULL,
    amount int NOT NULL
);

create index ndx_averagecheck_amount on averagecheck (amount);
