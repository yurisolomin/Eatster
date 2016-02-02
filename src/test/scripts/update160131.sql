

CREATE OR REPLACE FUNCTION fnCalcDistance(
lat1 double precision, --широта  1
lon1 double precision, --долгота 1
lat2 double precision, --широта  2
lon2 double precision  --долгота 2
) returns int AS
$BODY$
DECLARE 
 pi double precision;
begin
/*
http://savepearlharbor.com/?p=216569
dist = 6372797*acos(sin(PI*Lt1/180)*sin(PI*Lt2/180)+ cos(PI*Lt1/180)* cos(PI*Lt2/180)* cos(abs(PI*Lg2/180- PI*Lg1/180))) 
где: Lt1, Lg1 – широта/долгота первой точки Lt2, Lg2 – широта/долгота второй точки PI – PI 
К сожалению, она измеряется не в метрах/километрах/милях, а в мало-понятных радианах. 
Дело осложняется, как уже было сказано выше, шарообразностью нашей планеты. 
Строго говоря, количество метров в одном радиане разное на разных широтах. 
Чем выше широта, тем метров в радиане меньше. 
К счастью, в большинстве задач поиска близлежащих объектов особой точности не требуется 
и можно пользоваться примерными коэффициентами. Для наших широт, если ищем в километрах, 
исходное значение радиуса поиска нужно делить на 111.
*/
 if (lat1 = 0) and (lon1 = 0) then
   return 40000*1000;
 end if;
 if (lat2 = 0) and (lon2 = 0) then
   return 40000*1000;
 end if;
 pi = 3.14159265358;
 return cast(
 6372797*acos(sin(PI*lat1/180)*sin(PI*lat2/180)+cos(PI*lat1/180)* cos(PI*lat2/180)* cos(abs(PI*lon2/180-PI*lon1/180))) 
 as int);
end;
$BODY$
language 'plpgsql';

