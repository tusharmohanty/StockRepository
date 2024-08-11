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
    elsif p_data_type ='EMA_12' then
        select ema_12 into previous_data
                       from stats
                       where stock_code = p_stock_code
                       and txn_date = (select txn_date from (select txn_date as txn_date, rownum as count
                       from stats s
                       where s.stock_code = p_stock_code
                       and s.txn_date < trunc(max_txn_date) order by txn_date desc) where count =p_offset);
    elsif p_data_type ='EMA_16' then
        select ema_12 into previous_data
                       from stats
                       where stock_code = p_stock_code
                       and txn_date = (select txn_date from (select txn_date as txn_date, rownum as count
                       from stats s
                       where s.stock_code = p_stock_code
                       and s.txn_date < trunc(max_txn_date) order by txn_date desc) where count =p_offset);
    elsif p_data_type ='MACD' then
        select macd into previous_data
                       from stats
                       where stock_code = p_stock_code
                       and txn_date = (select txn_date from (select txn_date as txn_date, rownum as count
                       from stats s
                       where s.stock_code = p_stock_code
                       and s.txn_date < trunc(max_txn_date) order by txn_date desc) where count =p_offset);
    elsif p_data_type ='MACD_9' then
        select macd_9 into previous_data
                       from stats
                       where stock_code = p_stock_code
                       and txn_date = (select txn_date from (select txn_date as txn_date, rownum as count
                       from stats s
                       where s.stock_code = p_stock_code
                       and s.txn_date < trunc(max_txn_date) order by txn_date desc) where count =p_offset);
    end if;
    return previous_data;
    end get_previous_data;

    function calculate_sma(p_stock_code in varchar2,
                           p_txn_date   in date DEFAULT NULL,
                           p_field      in VARCHAR2,
                           p_interval   in number)
                           return number IS
    sum_total   NUMBER :=0;
    count_total NUMBER :=0;
    return_sma  NUMBER(18,2) :=0;
    begin
        if p_field = 'CLOSE' then
            select sum(close) into sum_total
            from (select close from stock_data
                  where stock_code = p_stock_code
                  and trunc(txn_date) <= trunc(p_txn_date)
                  order by txn_date desc )
            where rownum <= p_interval;

            select decode(count(1),0,1) into count_total
            from stock_data
            where stock_code = p_stock_code
            and txn_date > (p_txn_date -p_interval);
        elsif p_field = 'MACD' then
            select sum(macd) into sum_total
            from (select macd from stats
                  where stock_code = p_stock_code
                  and trunc(txn_date) <= trunc(p_txn_date)
                  order by txn_date desc )
            where rownum <= p_interval;

            select decode(count(1),0,1) into count_total
            from stats
            where stock_code = p_stock_code
            and txn_date > (p_txn_date -p_interval);
        end if ;

        if(count_total < p_interval) then
           return_sma := sum_total/count_total;
        else
           return_sma := sum_total/p_interval;
        end if;
        return return_sma;
    end calculate_sma;

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
    date_list date_type;
    previous_data number :=0;
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
        if stats(temp_count) = 'EMA' then
            select stock_code bulk collect into stock_list
            from stocks;
            for temp_count1 in 1 .. stock_list.count loop
                select txn_date bulk collect into date_list
                from stock_data
                where stock_code = stock_list(temp_count1)
                order by txn_date;
                for temp_count2 in 1 .. date_list.count loop
                    dbms_output.put_line ('processing ' || stock_list(temp_count1) || '   "' || trunc(date_list(temp_count2)));
                    select ((s.close*2/13) + (nvl(screener_utils.get_previous_data(s.stock_code,s.txn_date,'EMA_12'),s.close) *(1-2/13))) into previous_data
                                              from stock_data s
                                              where s.stock_code=stock_list(temp_count1)
                                              and trunc(s.txn_date) = trunc(date_list(temp_count2));

                    update stats s set s.ema_12 =previous_data
                                              where s.stock_code=stock_list(temp_count1)
                                              and trunc(s.txn_date) = trunc(date_list(temp_count2));

                    select ((s.close*2/27) + (nvl(screener_utils.get_previous_data(s.stock_code,s.txn_date,'EMA_26'),s.close) *(1-2/27))) into previous_data
                                              from stock_data s
                                              where s.stock_code=stock_list(temp_count1)
                                              and trunc(s.txn_date) = trunc(date_list(temp_count2));
                    update stats s set s.ema_26 =previous_data
                                              where s.stock_code=stock_list(temp_count1)
                                              and trunc(s.txn_date) = trunc(date_list(temp_count2));

                    select (ema_12-ema_26) into previous_data
                                              from stats s
                                              where s.stock_code=stock_list(temp_count1)
                                              and trunc(s.txn_date) = trunc(date_list(temp_count2));
                    update stats s set s.macd =previous_data
                                              where s.stock_code=stock_list(temp_count1)
                                              and trunc(s.txn_date) = trunc(date_list(temp_count2));

                    select screener_utils.calculate_sma(s.stock_code,s.txn_date,'MACD',9) into previous_data
                    from stats s
                    where s.stock_code =stock_list(temp_count1)
                    and trunc(s.txn_date) = trunc(date_list(temp_count2));

                    update stats s set s.macd_9 =previous_data
                                              where s.stock_code=stock_list(temp_count1)
                                              and trunc(s.txn_date) = trunc(date_list(temp_count2));


                    commit;
                end loop;
            end loop;
        end if;

    end loop;
    end update_stats;

end screener_utils;
/
commit;
exit;