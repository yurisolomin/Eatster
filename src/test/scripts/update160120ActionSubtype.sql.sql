
/*
это был разовый апдейт при корректировке справочников

begin transaction;

ALTER TABLE action DROP CONSTRAINT action_actionsubtype_id_fkey;
ALTER TABLE action DROP CONSTRAINT action_actiontype_id_fkey;
ALTER TABLE actionsubtype DROP CONSTRAINT actionsubtype_actiontype_id_fkey;

truncate TABLE actiontype RESTART IDENTITY;
truncate TABLE actionsubtype RESTART IDENTITY;


INSERT INTO actiontype (deleted,modified,name) VALUES (false,now(),'Скидка');
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'5%',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'10%',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'15%',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'20%',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'25%',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'30%',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'35%',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'50%',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'скидка',(select max(id) from actiontype));

INSERT INTO actiontype (deleted,modified,name) VALUES (false,now(),'Подарок');
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'1=2',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'1+1=3',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'подарок',(select max(id) from actiontype));

INSERT INTO actiontype (deleted,modified,name) VALUES (false,now(),'Счастливые часы');
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'Завтрак',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'Бизнес-ланч',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'Счастливые часы',(select max(id) from actiontype));

INSERT INTO actiontype (deleted,modified,name) VALUES (false,now(),'Групповые');
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'групповые',(select max(id) from actiontype));

INSERT INTO actiontype (deleted,modified,name) VALUES (false,now(),'Праздник');
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'Новый год',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'День всех влюбленных',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'День защитника отечества',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'Женский день',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'Масленица',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'Майские праздники',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'День победы',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'День России',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'Великий пост',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'Пасха',(select max(id) from actiontype));
--эта строчка ('Ресторана') появилась позже, убедитесь что в МП она будет иметь тот-же ID
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'Ресторана',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'Праздник',(select max(id) from actiontype));

INSERT INTO actiontype (deleted,modified,name) VALUES (false,now(),'Специальная акция');
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'Специальная акция',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'Блюдо дня',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'День рождения',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'Студенческая скидка',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'Возьми с собой',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'Для пар',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'Мероприятия',(select max(id) from actiontype));
INSERT INTO actionsubtype (deleted,modified,name,actiontype_id) VALUES (false,now(),'Новинка',(select max(id) from actiontype));

update action set actionsubtype_id = actionsubtype_id + 1 where actionsubtype_id > 26;

ALTER TABLE action
  ADD CONSTRAINT action_actiontype_id_fkey FOREIGN KEY (actiontype_id)
      REFERENCES actiontype (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE action
  ADD CONSTRAINT action_actionsubtype_id_fkey FOREIGN KEY (actionsubtype_id)
      REFERENCES actionsubtype (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE actionsubtype
  ADD CONSTRAINT actionsubtype_actiontype_id_fkey FOREIGN KEY (actiontype_id)
      REFERENCES actiontype (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

commit;
*/
