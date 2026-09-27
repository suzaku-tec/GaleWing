select *
from feed f
where exists (select 1
              from feed_keyword fk
              where f.uuid = fk.feed_uuid
                and f.link = fk.feed_link
                and fk.normalized_keyword = /*keyword*/'GPT')