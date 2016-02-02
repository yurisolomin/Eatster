
CREATE TABLE action (
    id bigserial primary key,
    deleted boolean NOT NULL,
    modified Timestamp NOT NULL,
    name character varying(128) NOT NULL,
    restaurant_id bigint references restaurant(id) NOT NULL,
    status character varying(20) NOT NULL,
    actiontype_id bigint references actiontype(id) NOT NULL,
    actionsubtype_id bigint references actionsubtype(id) NOT NULL,
    photo_urlparams character varying(40) NOT NULL,
    comment text NOT NULL,
    onmonday    boolean NOT NULL,
    ontuesday   boolean NOT NULL,
    onwednesday boolean NOT NULL,
    onthursday  boolean NOT NULL,
    onfriday    boolean NOT NULL,
    onsaturday  boolean NOT NULL,
    onsunday    boolean NOT NULL,
    start_time character varying(5) NOT NULL,
    end_time  character varying(5) NOT NULL

);

create index ndx_action_restaurant_id on action (restaurant_id);
