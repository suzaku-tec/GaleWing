--*DataTitle '"feed"'
--*CaptionFromComment
SELECT "title"                                                             -- title
     , "uuid"                                                              -- uuid
     , "link"                                                              -- link
     , "uri"                                                               -- uri
     , "author"                                                            -- author
     , "comments"                                                          -- comments
     , "publisheddate" as                                  publishedDate   -- publishedDate
     , "readed"                                                            -- readed
     , /*IF imgFlg == "0"*/ '' /*END*/ /*IF imgFlg != "0"*/imageUrl/*END*/ -- imageUrl
FROM "feed" f
WHERE readed = false
  and not exists(select * from read_list_queue rlq where rlq.url = f.uri)
order by publishedDate ASC
