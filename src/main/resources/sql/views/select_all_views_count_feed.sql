select
    v.id
    , v.name as title
    , ifnull(c.view_count, 0) as count
from
	views v
    inner join (
    	select
			vs.views_id
			, SUM(f.f_count) as view_count
		from
	    	views_site vs
		    left join (
		        select
		            uuid
		            , count(*) as f_count
		        from
		            feed
		        where
		            readed = false
		        group by
		            uuid
		    ) f on vs.site_id = f.uuid
		 group by vs.views_id
    ) c on v.id = c.views_id
order by count desc