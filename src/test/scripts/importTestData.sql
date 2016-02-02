CREATE OR REPLACE FUNCTION fnAddTestData() returns integer AS
$BODY$
DECLARE 
begin
--INSERT INTO "user" (deleted,modified,name,birthday,gender,email,phone,registration_date,referrals,password,registred,new_password) 
--           VALUES (false,now(),'Пользователь1','1970-01-01','M','yuri@mail.ru','+79053632154','',0,'0001',false,'');

INSERT INTO restaurant (deleted,modified,
name,description,status,cuisine_id1,cuisine_id2,cuisine_id3,address,coord_lat,coord_lon,subway_id,phone,website,averagecheck_id,
wifi,parking_id,kids_menu,entertainments,
cards_visa,cards_mastercard,cards_maestro,cards_unionpay,cards_visaelectron,cards_americanexpress,cards_dinersclub,cards_pro100,
workday_starttime,workday_endtime,holiday_starttime,holiday_endtime,
workday_monday,workday_tuesday,workday_wednesday,workday_thursday,workday_friday,workday_saturday,workday_sunday,
legal_name,legal_address,director,OGRN,INN,KPP,bank,BIC,cust_account,corr_account,
contact_name,contact_post,contact_email,contact_note,contact_phone,
user_name,user_password,user_email
)
VALUES (false,now(),
'Ресторан1','description','inactive',1,2,3,'addr',55.5087,36.0283,1,'+79030000011','website.ru',1,
false,1,true,'entertainments',
true,true,true,true,true,true,true,true,
'12:00','23:00','12:00','06:00',
true,true,true,true,true,true,true,
'legal_name','legal_address','director','OGRN','INN','KPP','bank','BIC','cust_account','corr_account',
'contact_name','contact_post','contact_email','contact_note','contact_phone',
'test','1','user_email'
);

insert into "user" (deleted,modified,name,birthday,gender,email,phone,registration_date,referrals,password,registred,new_password,promocode) 
values(false,now(),'name','1900-01-01','M','email','+79030000011','',0,'11',true,'','promocode');

INSERT INTO waiter (deleted,modified,name,restaurant_Id,login,password) VALUES (false,now(),'официант1',1,'','');

INSERT INTO operation (deleted,modified,restaurant_Id,user_Id,waiter_Id,oper_Date,oper_Time,check_Sum,score,comment) 
VALUES (false,now(),1,1,1,'2015-11-01','11:10',10,1,'чек1');       

INSERT INTO operation (deleted,modified,restaurant_Id,user_Id,waiter_Id,oper_Date,oper_Time,check_Sum,score,comment) 
VALUES (false,now(),1,1,1,'2015-11-01','11:10',10,2,'чек2');       

INSERT INTO operation (deleted,modified,restaurant_Id,user_Id,waiter_Id,oper_Date,oper_Time,check_Sum,score,comment) 
VALUES (false,now(),1,1,1,'2015-11-01','11:10',10,-3,'чек3');       

INSERT INTO operation (deleted,modified,restaurant_Id,user_Id,waiter_Id,oper_Date,oper_Time,check_Sum,score,comment) 
VALUES (false,now(),1,1,1,'2015-11-01','11:10',10,-4,'чек4');       

insert into action (deleted,modified,name,restaurant_id,status,actiontype_id,actionsubtype_id,actiontimerange_id,
photo_urlparams,comment,infoworkdays,onmonday,ontuesday,onwednesday,onthursday,onfriday,onsaturday,onsunday) 
values(false,now(),'name',1,'published',1,1,1,'','comment','infoworkdays',true,true,true,true,true,true,true);

return 0;
end;
$BODY$
language 'plpgsql';

select fnAddTestData();
DROP FUNCTION fnAddTestData();


/*
--select * from waiter
--truncate TABLE waiter RESTART IDENTITY cascade;

INSERT INTO partner (modified,deleted,name,password,contact_name,contact_post,contact_email,contact_note,contact_phone,admin)
values(now(),false,'test2@easter.ru','2','contact_name','contact_post','contact_email','contact_note','contact_phone',false)

INSERT INTO restaurant (deleted,modified,
name,description,status,cuisine_id1,cuisine_id2,cuisine_id3,address,coord_lat,coord_lon,subway_id,phone,website,averagecheck_id,
wifi,parking_id,kids_menu,entertainments,
cards_visa,cards_mastercard,cards_maestro,cards_unionpay,cards_visaelectron,cards_americanexpress,cards_dinersclub,cards_pro100,
workday_monday,workday_tuesday,workday_wednesday,workday_thursday,workday_friday,workday_saturday,workday_sunday,
legal_name,legal_address,director,OGRN,INN,KPP,bank,BIC,cust_account,corr_account,
contact_name,contact_post,contact_email,contact_note,contact_phone,
user_name,user_password,user_email,partner_id,workday_starttime,workday_endtime,holiday_starttime,holiday_endtime
)
VALUES (false,now(),
'Ресторан2','description2','inactive',4,5,6,'addr',54.5087,36.1283,1,'+79030000022','website2.ru',1,
false,1,true,'entertainments2',
true,true,true,true,true,true,true,true,
true,true,true,true,true,true,true,
'legal_name2','legal_address2','director2','OGRN2','INN2','KPP2','bank2','BIC2','cust_account2','corr_account2',
'contact_name2','contact_post2','contact_email2','contact_note2','contact_phone2',
'user_name2','user_password2','user_email2',5,'10:00','18:00','12:00','22:00'
);


*/