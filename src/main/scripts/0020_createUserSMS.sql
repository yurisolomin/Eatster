
CREATE TABLE user_sms (
    id bigserial primary key,
    modified Timestamp NOT NULL,
    phone character varying(12) NOT NULL,
    text character varying(20) NOT NULL
);

create index ndx_user_sms_phone on user_sms (phone);

