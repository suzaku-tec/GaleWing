select * from feed f where uuid = /*uuid*/ and readed = false
and not exists(select * from read_list_queue rlq where rlq.url = f.uri )
order by publishedDate ASC
