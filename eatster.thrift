namespace java ru.baccasoft.eatster.thrift.generated

include "commons.thrift"


// Профиль пользователя
struct ThriftUserData {
		// Уникальный идентификатор
	1: i64 id,
		// Наименование
	2: string name,
		// Дата рождения varchar(10) в формате "yyyy-MM-dd" или "", если пусто
	3: string birthday,
		// Пол. Мужской "M", женский "F" или "", если пусто
	4: string gender,
		// email пользователя или "", если пусто
	5: string email,
		// promocode пользователя или "", если пусто
		// readonly!
	6: string promocode, 
		// номер телефона пользователя или "", если пусто
	7: string phone,
		// промокод друга или "", если пусто
		// сервер обрабатывает промокод, если он не пустой. Сервер выдаст ошибку если:
		// 1. Происходит смена существующего промокода
		// 2. Пользователь с таким промокодом не найден
		// 3. Промокод друга равен личному промокоду
	8: string friendPromocode,
}


//Операции
struct ThriftOperationData {
		// Дата операции в формате "yyyy-MM-dd" или "", если пусто
	1: string operDate,
		// Время операции в формате "HH:MM" или "", если пусто
	2: string operTime,
		// Ресторан id
	3: i64 restaurantId,
		// Ресторан название
	4: string restaurantName,
		// Сумма чека
	5: i32 checkSum,
		// начислено (+), списано (-)
	6: i32 score,
}

// Мои баллы
struct ThriftMyScoresData {
		// Баланс
	1: i32 balance,
		// Потрачено
	2: i32 spent,
		// Всего
	3: i32 total,
		// Осталось дней бонусного периода
	4: i32 bonusPeriodEstimate,
        // Операции
    5: list<ThriftOperationData> operations,
}


// Акция
struct ThriftActionData {
		// Уникальный идентификатор
	1: i64 id,
		// Наименование
	2: string name,
		// Полный текст акции
	3: string actionDescription,
        //параметры для получения фотографий ("servlets/file?"+photo)
	4: string photo,
		// Тип акции
	5: string actionType,
		// Время начало ации
	6: string startTime,
		// Время окончания акции
	7: string endTime,	
		// Дни недели в акции. Битовая маска (понедельник и суббота = 2 + 128 = 130)
		// 2 - понедельник
		// 4 - вторник
		// 8 - среда
		// 16 - четверг
		// 32 - пятница
		// 64 - суббота
		// 128 - воскресенье
		// 256 - зарезервировано на случай 32го мая
	8: i32 actionDays,
		// Подтип акции
	9: string actionSubType,
}


// Текущее положение пользователя
struct ThriftPosition {
	// ширита, долгота
	1: double latitude, 
	2: double longitude, 
}

// Параметры для запроса Pagination
struct ThriftPaginationInfo {
	// смещение относительно начала списка, 0 - первый элемент
	1: i32 offset, 
	// сколько вернуть ресторанов
	2: i32 count, 
}

struct ThriftRestaurantData {
		// Уникальный идентификатор
	1: i64 id,
		// Наименование
	2: string name,
		// Кухни
    3: list<string> cuisines,
		// email пользователя или "", если пусто
	4: list<ThriftActionData> actions,
		// средний чек
	5: string averageCheck,
		//широта, долгота
    6: ThriftPosition position
		//расстояние до ресторана
	7: i32 distance,
		//адрес ресторана
    8: string address,
		//график работы в рабочией дни
    9: string infoWorkdays,
		//график работы в праздники
    10: string infoHolidays,
	   //Фотография в описании ресторана. для получения фотографий ("servlets/file?"+mainPhoto)
	11: string mainPhoto,
	   //параметры для получения фотографий ("servlets/file?"+photos)
	12: list<string> photos,
		//сайт ресторана
	13: string url,
	    //описание
	14: string restaurantDescription,
	    //телефон
	15: string phone,
	    //метро
	16: string subway,
	    //развлечения
	17: string entertainments,
	    //платежные карты
    18: list<string> paymentCards,
	    //парковка
    19: string parking;
	    //wifi
    20: bool wifi;
	    //детское меню
    21: bool kidsMenu;
	   //Логотип ресторана. для получения фотографий ("servlets/file?"+logo)
	22: string logo,
}

//метро
struct ThriftSubwayData {
		// Уникальный идентификатор
	1: i64 id,
		// Наименование
	2: string name,
}

//средний чек
struct ThriftAverageCheckData {
		// Уникальный идентификатор
	1: i64 id,
		// Наименование
	2: string name,
}

//тип акции
struct ThriftActionTypeData {
		// Уникальный идентификатор
	1: i64 id,
		// Наименование
	2: string name,
}

//кухня
struct ThriftCuisineData {
		// Уникальный идентификатор
	1: i64 id,
		// Наименование
	2: string name,
}

// Данные для синхронизации
struct ThriftChangesData {
	1: list<ThriftSubwayData> changedSubway,
	2: list<ThriftAverageCheckData> changedAverageCheck,
	3: list<ThriftActionTypeData> changedActionType,
	4: list<ThriftCuisineData> changedCuisine,
}

struct ThriftRestaurantFilter {
		// Метро (если 0 - любое метро)
	1: i64 subwayId,
		// Средний чек (если 0 - любой чек)
	2: i64 averageCheckId,
		// Тип акции (если 0 - любая акция)
	3: i64 actionTypeId,
		// Кухня (если 0 - любая кухня)
	4: i64 cuisineId,
}
service ThriftService {

    //Обязательны к заполнению:
	//request.clientPlatform  - платформа
	//request.protocolVersion - eatster_2015-11-01.001

	//Обязательные проверки при вызове любого метода:
	//1.Проверяю платформу из request.clientPlatform
	//  Если неудачно то ThriftException с типом ThriftExceptionType.SERVICE_VERSION_MISMATCH
	//2.Проверяю версию    из request.protocolVersion
	//  Если неудачно то ThriftException с типом ThriftExceptionType.UNDEFINED

	//получение текущего время сервера serverTimestamp и других данных
	//авторизация не требуется
	commons.ThriftPingResponse ping( 1: commons.ThriftRequestCommonData request ) throws ( 1: commons.ThriftException ex ),


	//Регистрация пользователя
	//request.userLogin - телефон
	//действия:
	//1.Проверяю номер телефона на соответствие формату "\+\d{11}" (+79030000011)
	//  Если неудачно то ThriftException с ThriftExceptionType.UNDEFINED
	//2.Создаю новый пароль (4 цифры). Пока нет смс, беру 4 последние цифры телефона
	//3.Ищу пользователя с телефоном request.userLogin. Если нет, то создаю его
	//4.Сохраняю в пользователя этот пароль и текущее время в поля "новый пароль" и "время нового пароля"
	//5.Отправляю смс на телефон
	//  Если неудачно, то ThriftException с ThriftExceptionType.UNDEFINED
	void registration( 1: commons.ThriftRequestCommonData request ) throws ( 1: commons.ThriftException ex ),

	//авторизация на сервере
	//request.userLogin - телефон
	//request.authToken - код подтверждения
	//request.deviceId  - id устройства
	//request.pushToken - токен для отправки push-уведомления
	//действия:
	//1.Поиск пользователя с именем request.userLogin (телефон) и паролем request.authToken в пользователях системы
	//  Если неудачно то ThriftException с типом ThriftExceptionType.AUTHENTICATION_FAILED 
	//2.Если указаны request.deviceId и request.pushToken то сохраняю их в базе токенов для push-уведомлений
	//3.Возвращаю профиль пользователя
	ThriftUserData login( 1: commons.ThriftRequestCommonData request ) throws ( 1: commons.ThriftException ex ),

	//сохранение профиля пользователя
	//действия:
	//1.Ищу пользователя по userData.id
	//  Если неудачно то ThriftException с ThriftExceptionType.UNDEFINED
	//2.Обновляю все данные пользователя из ThriftUserData
	void saveUserData( 1: commons.ThriftRequestCommonData request, 2:ThriftUserData userData ) throws ( 1: commons.ThriftException ex ),

	//получение списка из maxRecCount ближайших салонов
	//Сервер хранит только последний запрос пользователя. 
	//авторизация не требуется
	//latitude  - широта
	//longitude - долгота
    //currentPosition.offset - последний id предыдущей выборки
	//currentPosition.count - коллическо элемнтов, которые необходимо вернуть
    //для получения 1-й страницы - равно 0 (в ответе будут 20 записей)
    //для получения следующей страницы - равно последнему id текущей страницы (в ответе будут 10 записей)
	list<ThriftRestaurantData> getNearestRestaurants( 1: commons.ThriftRequestCommonData request, 2: ThriftPosition currentPosition, 3: ThriftPaginationInfo pagination, 4: ThriftRestaurantFilter filter ) throws ( 1: commons.ThriftException ex ),

    //авторизация
	//request.userLogin - номер телефона
	//request.authToken - код подтверждения (пин)
	ThriftMyScoresData getMyScores( 1: commons.ThriftRequestCommonData request ) throws ( 1: commons.ThriftException ex ),

	//зажато, т.к. эти справочники придут при инсталляции
	//получение данных для синхронизации
	//получение обновленных данных, начиная с previousTimestamp
	//авторизация не требуется
	//ThriftChangesData getChanges ( 1: commons.ThriftRequestCommonData request, 2: i64 previousTimestamp ) throws ( 1: commons.ThriftException ex ),

	//получение данных о ресторане
	//авторизация не требуется
	ThriftRestaurantData getRestaurant( 1: commons.ThriftRequestCommonData request, 2: i64 restaurantId ) throws ( 1: commons.ThriftException ex ),

	//получение данных о ресторанах
	//авторизация не требуется
	list<ThriftRestaurantData> getRestaurants( 1: commons.ThriftRequestCommonData request, 2: list<i64> restaurantIds ) throws ( 1: commons.ThriftException ex ),
}

