
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
--select fnCalcDistance(36.026386000000002,55.505118000000003,36.028371999999997,55.508758)


/*
--другая функция, но считает так же
--https://www.kobzarev.com/programming/calculation-of-distances-between-cities-on-their-coordinates.html
create or replace function calculateTheDistance (pLat1 double precision, pLon1 double precision, pLat2 double precision, pLon2 double precision)
 returns double precision AS
 $BODY$
DECLARE 
 M_PI double precision;
 lat1 double precision;
 lat2 double precision;
 long1 double precision;
 long2 double precision;
 cl1 double precision;
 cl2 double precision;
 sl1 double precision;
 sl2 double precision;
 delta double precision;
 cdelta double precision;
 sdelta double precision;
 y double precision;
 x double precision;
 ad  double precision;
 dist double precision;
begin
  M_PI = 3.14159265358;

    -- перевести координаты в радианы
    lat1 = pLat1 * M_PI / 180;
    lat2 = pLat2 * M_PI / 180;
    long1 = pLon1 * M_PI / 180;
    long2 = pLon2 * M_PI / 180;
 
    -- косинусы и синусы широт и разницы долгот
    cl1 = cos(lat1);
    cl2 = cos(lat2);
    sl1 = sin(lat1);
    sl2 = sin(lat2);
    delta = long2 - long1;
    cdelta = cos(delta);
    sdelta = sin(delta);
 
    -- вычисления длины большого круга
    y = sqrt(pow(cl2 * sdelta, 2) + pow(cl1 * sl2 - sl1 * cl2 * cdelta, 2));
    x = sl1 * sl2 + cl1 * cl2 * cdelta;
 
    ad = atan2(y, x);
    dist = ad * 6372795;--*EARTH_RADIUS
 
    return dist;
end;
$BODY$
language 'plpgsql';



*/