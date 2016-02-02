create role eatster_user login password 'eat' nosuperuser;
create database eatster with owner=eatster_user encoding='UTF8';


