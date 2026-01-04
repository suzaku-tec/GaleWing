select count(*)                                      as total_delivered
     , count(case when f.readed = true and f.opened != 'true' then 1 end)   as active_reads
     , count(case when f.opened = 'true' then 1 end) as opened
from feed f
where f.publisheddate >= TO_CHAR((CURRENT_DATE - INTERVAL '7 days'), 'YYYY-MM-DD')
