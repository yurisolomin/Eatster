
CREATE TABLE photo (
    id bigserial primary key,
    deleted boolean NOT NULL,
    modified Timestamp NOT NULL,
    object_id bigint NOT NULL,
    object_type character varying(20) NOT NULL,
    file_info_id bigint NOT NULL,
    photo_urlparams character varying(40) NOT NULL,
    file_info_mini_id bigint NOT NULL,
    photo_urlparams_mini character varying(40) NOT NULL,
    status character varying(20) NOT NULL
);

create index ndx_photo_object on photo (object_id,object_type);
