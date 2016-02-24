d:
cd \Java\Eatster\eatster\src\main\scripts
rem copy /A 0*.sql createAll.sql
if exist createAll.sql del createAll.sql
for /f %%f in ('dir /b /O:N 0*.sql') do type %%f >>createAll.sql
pause