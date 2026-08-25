select *
from feed
where uuid = /*uuid*/'1'
  and readed = false
  and opened = false
  and COALESCE(translatetitle, '') = ''