
insert into partner (deleted,modified,name,password,contact_name,contact_post,contact_email,contact_note,contact_phone,admin) values(false,now(),'admin','admin','','','','','',true);


INSERT INTO restaurant (deleted,modified,
name,description,status,cuisine_id1,cuisine_id2,cuisine_id3,address,coord_lat,coord_lon,subway_id,phone,website,averagecheck_id,
wifi,parking_id,kids_menu,entertainments,
cards_visa,cards_mastercard,cards_maestro,cards_unionpay,cards_visaelectron,cards_americanexpress,cards_dinersclub,cards_pro100,
workday_monday,workday_tuesday,workday_wednesday,workday_thursday,workday_friday,workday_saturday,workday_sunday,
legal_name,legal_address,director,OGRN,INN,KPP,bank,BIC,cust_account,corr_account,
partner_id,
cards_jcb,cards_mir,
workday_starttime,workday_endtime,holiday_starttime,holiday_endtime
)
VALUES (false,now(),
'Клуб Eatster','','inactive',1,null,null,'',0,0,1,'','',1,
false,1,false,'',
false,false,false,false,false,false,false,false,
false,false,false,false,false,false,false,
'','','','','','','','','','',
(select id from partner where admin = true limit 1),
false,false,
'','','',''
);

insert into waiter (deleted,name,restaurant_id,login,password,modified)
values (false,'Клуб Eatster',
(select id from restaurant where name = 'Клуб Eatster' limit 1),
'Клуб Eatster','Клуб_Eatster',now());

