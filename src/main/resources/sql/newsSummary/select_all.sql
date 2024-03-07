select
ns.feed_uuid
, ns.summary
, f.title
from news_summary ns
, feed f
where ns.feed_uuid = f.link and ns.summary is not null
