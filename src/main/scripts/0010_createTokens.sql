
CREATE TABLE tokens
(
  phone character varying(20) NOT NULL,
  platform character varying(50) NOT NULL,
  token character varying(1000) NOT NULL,
  device_id character varying(1000) NOT NULL,
  last_activity Timestamp NOT NULL,
  CONSTRAINT pkey_tokens PRIMARY KEY (phone,device_id)
)
WITH (
  OIDS=FALSE
);

create index ndx_tokens_phone on tokens (phone);
