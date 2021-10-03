--*DataTitle '"stack"'
--*CaptionFromComment
SELECT
    "title"                                     -- title
    , "uuid"                                    -- uuid
    , "link"                                    -- link
    , "uri"                                     -- uri
    , "author"                                  -- author
    , "comments"                                -- comments
    , "publishedDate"                           -- publishedDate
    , "stackDate"                               -- stackDate
FROM
    "stack"
ORDER BY
    "stackDate"
