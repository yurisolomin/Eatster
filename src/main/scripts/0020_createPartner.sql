
CREATE TABLE partner (
    id bigserial primary key,
    deleted boolean NOT NULL,
    modified Timestamp NOT NULL,
    name character varying(80) NOT NULL,
    password character varying(16) NOT NULL,
    contact_name character varying(60) NOT NULL,
    contact_post character varying(80) NOT NULL,
    contact_email character varying(60) NOT NULL,
    contact_note character varying(128) NOT NULL,
    contact_phone character varying(20) NOT NULL,
    admin boolean NOT NULL
);

create unique index ndx_partner_name on partner (name);
