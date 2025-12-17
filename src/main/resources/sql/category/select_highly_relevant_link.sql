select fc.link as cnt from feed_category fc where fc.tag in /*tags*/('炎上','番組') group by fc.link having  /*matchRate*/3 <= COUNT(*)
