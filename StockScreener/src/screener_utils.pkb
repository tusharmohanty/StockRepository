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


    function get_previous_high_low(p_stock_code in varchar2,
                                p_txn_date   in date DEFAULT NULL,
                                p_data_type  in varchar2 DEFAULT 'HIGH',
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
        if p_data_type = 'HIGH' then
            select max(close) into previous_data
            from stock_data
            where stock_code = p_stock_code
            and txn_date between (max_txn_date -p_offset) and max_txn_date;
            return previous_data;
        elsif p_data_type ='LOW' then
            select min(close) into previous_data
                from stock_data
                where stock_code = p_stock_code
                and txn_date between (max_txn_date -p_offset) and max_txn_date;
                return previous_data;
        end if;
    end get_previous_high_low;

    procedure  update_stats(stats IN stats_type)
    as
    stock_list stats_type;
    begin
    for temp_count in 1 .. stats.count loop
        if stats(temp_count) = '4_WEEK_HIGH_LOW' then
            select stock_code bulk collect into stock_list
            from stocks;
            for temp_count1 in 1 .. stock_list.count loop
                update stats  set week_high_4 = (select screener_utils.get_previous_high_low(st.stock_code,st.txn_date,'HIGH',28)
                from stats stat, stock_data st
                where stat.stock_code = st.stock_code
                and st.stock_code =stats.stock_code
                and st.txn_date = stat.txn_date
                and st.txn_date = stats.txn_date) where stats.stock_code =stock_list(temp_count1);

                update stats  set week_low_4 = (select screener_utils.get_previous_high_low(st.stock_code,st.txn_date,'LOW',28)
                from stats stat, stock_data st
                where stat.stock_code = st.stock_code
                and st.stock_code =stats.stock_code
                and st.txn_date = stat.txn_date
                and st.txn_date = stats.txn_date) where stats.stock_code =stock_list(temp_count1);
                commit;
            end loop;
        end if;
    end loop;
    end update_stats;

end screener_utils;
/
commit;
exit;