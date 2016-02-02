
begin transaction;

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

create index ndx_partner_name on partner (name);

INSERT INTO partner (modified,deleted,name,password,contact_name,contact_post,contact_email,contact_note,contact_phone,admin)
select now(),false,user_email,user_password,contact_name,contact_post,contact_email,contact_note,contact_phone,false
from restaurant where id = 1;

INSERT INTO partner (modified,deleted,name,password,contact_name,contact_post,contact_email,contact_note,contact_phone,admin)
select now(),false,user_email,user_password,contact_name,contact_post,contact_email,contact_note,contact_phone,false
from restaurant where id = 2;

INSERT INTO partner (modified,deleted,name,password,contact_name,contact_post,contact_email,contact_note,contact_phone,admin)
select now(),false,user_email,user_password,contact_name,contact_post,contact_email,contact_note,contact_phone,false
from restaurant where id = 3;

--admin
INSERT INTO partner (modified,deleted,name,password,contact_name,contact_post,contact_email,contact_note,contact_phone,admin)
values(now(),false,'admin','admin','','','','','',true);

----------------
alter table restaurant add partner_id bigint references partner(id);

update restaurant set 
 partner_id = p.id
 from restaurant r
 join partner p on p.name = r.user_email;
 
alter table restaurant alter column partner_id set NOT NULL;

create index ndx_restaurant_partner_id on restaurant (partner_id);
----------------

ALTER TABLE restaurant DROP COLUMN contact_name;
ALTER TABLE restaurant DROP COLUMN contact_post;
ALTER TABLE restaurant DROP COLUMN contact_email;
ALTER TABLE restaurant DROP COLUMN contact_note;
ALTER TABLE restaurant DROP COLUMN contact_phone;
ALTER TABLE restaurant DROP COLUMN user_name;
ALTER TABLE restaurant DROP COLUMN user_password;
ALTER TABLE restaurant DROP COLUMN user_email;

commit;
