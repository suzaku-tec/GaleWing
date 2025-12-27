select *
from rss_bridge rb
where json_extract(rb.json, '$.id') = /*jsonId*/'https://www.instagram.com/p/DSq8xmOiZ-t/';
