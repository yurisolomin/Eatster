begin transaction;

-- *****************************************************************************
create sequence commons_files_sequence
	minvalue 1
	increment 1;

-- *****************************************************************************
create table file_content (
	id bigint not null primary key
	
	, storage_uri varchar(32) not null
	, created timestamp without time zone not null
	
	, prev_version_id bigint null
	, constraint fk_prev_version_id foreign key(prev_version_id) references file_content(id)
	
) with (
	oids = false
);
	
-- *****************************************************************************
create table file_info (

	id bigint not null primary key
	
	, created timestamp without time zone not null
	, modified timestamp without time zone not null
	
	, content_id bigint not null
	, constraint fk_content_id foreign key(content_id) references file_content(id)

	, deleted boolean not null
	
	, filename varchar(256) not null
	
	, content_type varchar(64) not null
	, aux_business_type varchar(64) not null
) with (
	oids = false
);

-- *****************************************************************************
commit;