select
  category.*
from
  category
  inner join site_category
  on category.uuid = site_category.category_uuid and site_category.site_uuid = /*siteUuid*/"1"
