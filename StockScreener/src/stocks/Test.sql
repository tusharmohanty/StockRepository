declare
stats_list stats_type;
begin
stats_list := stats_type('EMA');
screener_utils.update_stats(stats_list);
end;
