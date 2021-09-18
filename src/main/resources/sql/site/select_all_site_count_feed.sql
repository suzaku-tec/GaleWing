select
    s.uuid
    , s.title
    , ifnull(f.count, 0) as count
    , s.faviconBase64
from
    site s
    left join (
        select
            uuid
            , count(*) as count
        from
            feed
        where
            readed = false
        group by
            uuid
    ) f
        on s.uuid = f.uuid
