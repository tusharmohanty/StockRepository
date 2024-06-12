create or replace PACKAGE BODY screener_utils AS
    function get_previous_close(p_stock_code in varchar2,
                                p_txn_date   in date DEFAULT NULL)
                                return NUMBER IS
    previous_close NUMBER :=0;
    max_txn_date date;
    BEGIN
    if p_txn_date is null then
       select max(txn_date) into max_txn_date
       from stock_data
       where stock_code = p_stock_code;
    else
       max_txn_date :=p_txn_date;
    end if;


    select close into previous_close
                   from stock_data
                   where stock_code = p_stock_code
                   and txn_date = (select max(txn_date)
                   from stock_data s
                   where s.stock_code = p_stock_code
                   and s.txn_date < trunc(max_txn_date));

        return previous_close;
    end get_previous_close;
end screener_utils;
/
commit;
exit;