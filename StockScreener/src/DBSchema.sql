alter session set "_ORACLE_SCRIPT"=true;
CREATE USER stocks
  IDENTIFIED BY linuxing;
  
  GRANT create session TO stocks;
GRANT create table TO stocks;
GRANT create view TO stocks;
GRANT create any trigger TO stocks;
GRANT create any procedure TO stocks;
GRANT create sequence TO stocks;
GRANT create synonym TO stocks;
GRANT create any type to stocks;
ALTER USER stocks quota unlimited on USERS;


create table stocks (
stock_code varchar2(50) NOT NULL,
stock_name varchar2(200) NOT NULL,
exchange   varchar2(10) NOT NULL,
watchlist_flag varchar2(10),
CONSTRAINT stocks_pk PRIMARY KEY (stock_code)
);

create table stock_data(						
stock_code VARCHAR2(50) NOT NULL,
txn_date   Date NOT NULL,
open       NUMBER(18,2),
close      NUMBER(18,2),
high       NUMBER(18,2),
low        NUMBER(18,2),
volume     NUMBER(18,2),
CONSTRAINT stock_data_pk PRIMARY KEY (stock_code,txn_date)
);

create table holiday_list(
holiday Date NOT NULL,
CONSTRAINT holiday_list_pk PRIMARY KEY (holiday)
);

create table unzip_bhavcopy(
	bhav_copy VARCHAR2(200),
	CONSTRAINT unzip_bhavcopy_pk PRIMARY KEY (bhav_copy)
);

create table earnings(
	stock_code varchar2(50) NOT NULL,
    effective_start_date Date NOT NULL,
    effective_end_date Date,
    eps NUMBER(18,2) NOT NULL
);

create table stats(
  stock_code  varchar2(50) NOT NULL,
  txn_date   Date NOT NULL,
  sma_16     NUMBER(18,2),
  sma_26     NUMBER(18,2),
  ema_16     NUMBER(18,2),
  ema_26     NUMBER(18,2),
  macd       NUMBER(18,2),
  macd_9     NUMBER(18,2),
  ema_12     NUMBER(18,2),
  week_high_4 NUMBER(18,2),
  week_low_4 NUMBER(18,2),
  CONSTRAINT stats_pk PRIMARY KEY (stock_code,txn_date)
);


create or replace context STOCKENV using setenv;
create or replace procedure setenv( stock_code in varchar2)
as
begin
dbms_session.set_context('stockenv','stock_code',stock_code);
end;
exec setenv('RELIANCE');

create or replace view basic_stats
as
select b.* ,b.close as today_close,s1.close as prev_close,s2.close as next_close,
decode(sign(b.close - s1.close),1,1,sign(b.close-s2.close))as dlow,
decode(sign(b.close - s1.close),1,1,-1)as TC_PC,
decode(sign(b.low - s1.close),1,1,-1) as TL_PL, 
decode (sign(b.high-s1.high),1,1,-1)  as TH_PH,
decode(sign(b.volume - s1.volume),1,1,-1)as TV_PV,
decode(sign(b.close - s1.high),1,1,-1)as TC_PH
from (select s.* , (select max(p.txn_date) from stock_data p where p.txn_date < s.txn_date and p.stock_code = s.stock_code) as prev_day ,
                    (select min(p.txn_date) from stock_data p where p.txn_date > s.txn_date and p.stock_code = s.stock_code) as next_day from STOCK_DATA s where s.stock_code = sys_context('stockenv','stock_code')) b,
STOCK_DATA s1,
stock_data s2
where b.stock_code = s1.stock_code
and b.prev_day = s1.txn_date
and b.stock_code =s2.stock_code(+)
and b.next_day = s2.txn_date(+)
order by b.TXN_DATE desc;




----TEst low scan

select b.*,s1.close as prev_close,s2.close as next_low from (
select c.* ,
(select max(p.txn_date) from basic_stats p where trunc(p.txn_date) < trunc(c.txn_date) and p.dlow= -1) as prev_low,
(select min(p.txn_date) from basic_stats p where trunc(p.txn_date) > trunc(c.txn_date) and p.dlow= -1) as next_low
from basic_stats c 
where c.dlow= -1) b,
stock_data s1,
stock_data s2
where b.stock_code = s1.stock_code
and b.prev_low = s1.txn_date
and b.next_low = s2.txn_date(+)
and b.stock_code = s1.stock_code
and b.stock_code = s2.stock_code(+)
and (b.close < s1.close )
order by b.TXN_DATE desc;


create table portfolio(
 portfolio_id NUMBER(18),
 stock_code varchar2(100),
 qty number(18),
 price number(18),
 txn_date date
);

create table comments(
comment_id number (18),
stock_code varchar2(100),
comment_date date,
comment_basis varchar2(20),
comment_type varchar2(20),
comment_text clob
);


create table  earnings_call_notes(
call_id number(18),
quarter varchar2(10),
earning_year number(4),
earnings_notes Blob,
"STOCK_CODE" VARCHAR2(100 BYTE)
);

CREATE SEQUENCE key_seq
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

create table stock_alerts(
stock_code varchar2(100),
alert_type varchar2(10),
threshold  number(18,2),
comments varchar2(4000),
alert_date date,
alert_price NUMBER(18,2));

  create or replace view portfolio_cons as select ti,tp, (tp-ti) as pl , (round(((tp -ti)/ti)*100,2)) as pl_percent from (select sum(price*qty) as ti , sum (current_price*qty) as tp from (select p.stock_code, qty, price, (select close
                                  from stock_data s
                                  where s.stock_code = p.stock_code
                                  and s.txn_date = (select max(sd.txn_date)
                                                   from stock_data sd
                                                   where sd.stock_code = s.stock_code )) as current_price
                                                                    from portfolio p, stocks st
                                                                    where p.stock_code = st.stock_code
                                                                    and st.exchange='NSE'));

CREATE OR REPLACE TYPE stats_type AS VARRAY(2000) OF VARCHAR2(50);
create or replace type date_type  AS VARRAY(200000) OF DATE;

create table access_tokens (
request_token VARCHAR2(2000) NOT NULL,
creation_date TIMESTAMP NOT NULL,
access_token  varchar2(2000) NOT NULL,
public_token  varchar2(2000) NOT NULL,
CONSTRAINT access_token_pk PRIMARY KEY (request_token)
);


create table instruments (
instrument_id NUMBER(18),
exchange VARCHAR2(10),
instrument_name varchar2(2000),
instrument_symbol VARCHAR2(100),
CONSTRAINT instruments_pk PRIMARY KEY (instrument_id)
);