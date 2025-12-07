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
from
	feed f
where
	readed = false
	and exists (
	select
		*
	from
		views_site vs
	where
		vs.site_id = f.uuid
		and vs.views_id = /*id*/'1')
	and not exists(select * from read_list_queue rlq where rlq.url = f.uri )
order by
	publishedDate ASC
