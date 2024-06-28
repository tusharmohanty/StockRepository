create or replace PACKAGE BODY screener_utils AS
    function get_previous_data(p_stock_code in varchar2,
                                p_txn_date   in date DEFAULT NULL,
                                p_data_type  in varchar2 DEFAULT 'CLOSE',
                                p_offset     in NUMBER  DEFAULT 1)
                                return NUMBER IS
    previous_data NUMBER :=0;
    max_txn_date date;
    BEGIN
    if p_txn_date is null then
       select max(txn_date) into max_txn_date
       from stock_data
       where stock_code = p_stock_code;
    else
       max_txn_date :=p_txn_date;
    end if;

    if p_data_type = 'CLOSE' then
        select close into previous_data
                       from stock_data
                       where stock_code = p_stock_code
                       and txn_date = (select txn_date from (select txn_date as txn_date, rownum as count
                       from stock_data s
                       where s.stock_code = p_stock_code
                       and s.txn_date < trunc(max_txn_date) order by txn_date desc) where count =p_offset);
    elsif p_data_type ='EMA_26' then
        select ema_26 into previous_data
                       from stats
                       where stock_code = p_stock_code
                       and txn_date = (select txn_date from (select txn_date as txn_date, rownum as count
                       from stats s
                       where s.stock_code = p_stock_code
                       and s.txn_date < trunc(max_txn_date) order by txn_date desc) where count =p_offset);
    end if;
    return previous_data;
    end get_previous_data;
end screener_utils;
/
commit;
exit;