delete
from
	feed as f
where
	f.readed
	and f.publishedDate < /*daysRetained*/'2022-02-26'
	and not exists (
	    select * from news_summary ns where ns.feed_uuid = f.link
	)
;
