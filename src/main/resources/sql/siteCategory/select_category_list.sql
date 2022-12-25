select
  *
from
  feed,
  (
   select * from site_category where category_uuid in (/*categoryList*/'1')
  ) sc
where sc.site_uuid = feed.uuid;

