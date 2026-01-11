update news_summary
set summary=/*summary*/''
where feed_uuid =/*uuid*/'1'
  and type =/*type*/'1';