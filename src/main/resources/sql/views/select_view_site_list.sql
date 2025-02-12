select
case when a.site_id is null then 0 else 1 end chk
, s.uuid
, s.title
from site s left join
(select vs.site_id from views_site vs, views v where vs.views_id = v.id and v.id =
/*viewId*/"691172a3-693f-402e-ae97-1cfad460baab") a
on s.uuid = a.site_id
