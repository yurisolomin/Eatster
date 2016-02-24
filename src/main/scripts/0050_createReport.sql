
CREATE TABLE report (
    id bigserial primary key,
    modified Timestamp NOT NULL,
    restaurant_id bigint references restaurant(id) NOT NULL,
    report_year int NOT NULL,
    report_month int NOT NULL,
    scores_total int NOT NULL,
    scores_spent int NOT NULL,
    oper_count int NOT NULL,
    check_sum int NOT NULL,
    status character varying(20) NOT NULL,
    commission_sum int NOT NULL
);
--in a formation stage
create unique index ndx_report_restaurant_id on report (restaurant_id,report_year,report_month);
create index ndx_report_month on report (report_year,report_month);
