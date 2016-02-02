CREATE OR REPLACE FUNCTION fnAddTestCoupleRestaurantAndPartner(_start_nom integer,_end_nom integer) returns integer AS
$BODY$
DECLARE 
  p_partner_name character varying(40);
  p_restaurant_name character varying(40);
  p_partner_id bigint;
  p_subway_id bigint;
  p_coord_lat double precision;
  p_coord_lon double precision;
  start_nom integer;
begin
start_nom = _start_nom;
while start_nom <= _end_nom LOOP
  p_subway_id = start_nom;
  p_coord_lat = cast(55.0 + random() as numeric(10,4));
  p_coord_lon = cast(36.0 + random() as numeric(10,4));
  p_partner_name = 'partner' || cast(start_nom as character varying(10)) || '@test.ru';
  p_restaurant_name = 'Ресторан' || cast(start_nom as character varying(10));
  
insert into partner(deleted,modified,name,password,contact_name,contact_post,contact_email,contact_note,contact_phone,admin)
    values(false,now(),p_partner_name,cast(start_nom as character varying(10)),'','','','','',false)
    RETURNING id INTO p_partner_id;

INSERT INTO restaurant (deleted,modified,
name,description,status,cuisine_id1,cuisine_id2,cuisine_id3,address,coord_lat,coord_lon,subway_id,phone,website,averagecheck_id,
wifi,parking_id,kids_menu,entertainments,
cards_visa,cards_mastercard,cards_maestro,cards_unionpay,cards_visaelectron,cards_americanexpress,cards_dinersclub,cards_pro100,
workday_starttime,workday_endtime,holiday_starttime,holiday_endtime,
workday_monday,workday_tuesday,workday_wednesday,workday_thursday,workday_friday,workday_saturday,workday_sunday,
legal_name,legal_address,director,OGRN,INN,KPP,bank,BIC,cust_account,corr_account,
partner_id
)
VALUES (false,now(),
p_restaurant_name,'','active',1,2,3,'',p_coord_lat,p_coord_lon,p_subway_id,'','',1,
false,1,true,'',
true,true,true,true,true,true,true,true,
'12:00','23:00','12:00','06:00',
true,true,true,true,true,true,true,
'','','','','','','','','','',
p_partner_id
);

  start_nom = start_nom + 1;
  END LOOP;
  

return 0;
end;
$BODY$
language 'plpgsql';

--select fnAddTestCoupleRestaurantAndPartner(20,20);
--DROP FUNCTION fnAddTestCoupleRestaurantAndPartner(integer,integer);
