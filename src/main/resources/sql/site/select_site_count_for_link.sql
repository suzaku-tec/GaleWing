select
    s.uuid as uuid
    , s.title as title
    , f.count as count
from
    site s
    , (
        select
            f1.uuid
            , count(*) as count
        from
            (
                select
                    uuid
                from
                    feed
                where
                    link = /*link*/
            ) fsub
            , feed f1
        where
            f1.uuid = fsub.uuid
            and f1.readed = false
        group by
            f1.uuid
    ) f
where
    s.uuid = f.uuid
