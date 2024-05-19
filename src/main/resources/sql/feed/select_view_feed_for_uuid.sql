select
	*
from
	feed f
where
	readed = false
	and exists (
	select
		*
	from
		views_site vs
	where
		vs.site_id = f.uuid
		and vs.views_id = /*id*/'1')
order by
	publishedDate DESC
