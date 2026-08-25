select
    title
    , uuid
    , link
    , uri
    , author
    , comments
    , publishedDate
    , readed
    , opened
    , /*IF imgFlg == "0"*/ '' /*END*/ /*IF imgFlg != "0"*/imageUrl/*END*/
    , contentTerxt
    , translatetitle
from feed f where uuid = /*uuid*/ and readed = false
and not exists(select * from read_list_queue rlq where rlq.url = f.uri )

order by publishedDate ASC
