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
ALTER USER stocks quota unlimited on USERS;


create table stocks (
stock_code varchar2(50) NOT NULL,
stock_name varchar2(200) NOT NULL,
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
)

create table holiday_list(
holiday Date NOT NULL,
CONSTRAINT holiday_list_pk PRIMARY KEY (holiday)
)

create table unzip_bhavcopy(
	bhav_copy VARCHAR2(200),
	CONSTRAINT unzip_bhavcopy_pk PRIMARY KEY (bhav_copy)
)

create table earnings(
	stock_code varchar2(50) NOT NULL,
    effective_start_date Date NOT NULL,
    effective_end_date Date,
    eps NUMBER(18,2) NOT NULL
)

create table stats(
  stock_code  varchar2(50) NOT NULL,
  txn_date   Date NOT NULL,
  sma_16     NUMBER(18,2),
  sma_26     NUMBER(18,2),
  CONSTRAINT stats_pk PRIMARY KEY (stock_code,txn_date)
)