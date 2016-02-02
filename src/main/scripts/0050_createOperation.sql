
CREATE TABLE "operation" (
    id bigserial primary key,
    modified Timestamp NOT NULL,
    restaurant_id bigint references restaurant(id) NOT NULL,
    user_id bigint references "user"(id) NOT NULL,
    waiter_id bigint references waiter(id) NOT NULL,
    oper_date character varying(10) NOT NULL,
    oper_time character varying(5) NOT NULL,
    check_sum int NOT NULL,
    score int NOT NULL,
    comment character varying(40) NOT NULL,
    status character varying(10) NOT NULL,
    sms_code character varying(10) NOT NULL,
    sms_time Timestamp NULL
);

create index ndx_operation_restaurant_id on operation (restaurant_id,oper_date);
create index ndx_operation_user_id on operation (user_id,oper_date);
create index ndx_operation_oper_date on operation (oper_date);
