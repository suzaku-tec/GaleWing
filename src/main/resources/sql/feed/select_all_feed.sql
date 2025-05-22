--*DataTitle '"feed"'
--*CaptionFromComment
SELECT
    "title"                                     -- title
    , "uuid"                                    -- uuid
    , "link"                                    -- link
    , "uri"                                     -- uri
    , "type"                                    -- type
    , "author"                                  -- author
    , "comments"                                -- comments
    , "publishedDate"                           -- publishedDate
    , "readed"                                  -- readed
    , "imageUrl"                                -- imageUrl
FROM
    "feed" f
WHERE
    readed = false
    and not exists(select * from read_list_queue rlq where rlq.url = f.uri )
order by publishedDate DESC
