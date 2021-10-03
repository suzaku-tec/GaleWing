INSERT
INTO "stack" (
    "title"
    , "uuid"
    , "link"
    , "uri"
    , "author"
    , "comments"
    , "publishedDate"
    , "stackDate"
)
SELECT
    f.title                                     -- title
    , f.uuid                                    -- uuid
    , f.link                                    -- link
    , f.uri                                     -- uri
    , f.author                                  -- author
    , f.comments                                -- comments
    , f.publishedDate                           -- publishedDate
    , CURRENT_TIMESTAMP                         -- stackDate
FROM
    feed f
WHERE
    f.uuid = /*uuid*/
    and f.link = /*link*/
    and NOT EXISTS(
      SELECT * FROM stack s
      WHERE
        s.uuid = f.uuid
        AND s.link = f.link
    )
