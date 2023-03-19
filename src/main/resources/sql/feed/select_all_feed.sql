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
    "feed" 
WHERE
    readed = false
order by publishedDate DESC
