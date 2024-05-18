select
	*
from
	site a
where
	exists(
	select
		*
	from
		views_site vs
	where
		a.uuid = vs.site_id
		and vs.views_id = /*viewsId*/'1'
	)