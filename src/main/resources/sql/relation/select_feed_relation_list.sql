SELECT f2.title, f2.link
FROM feed_relation fr left join feed f1 on fr.feedUuid1  = f1.uuid left join feed f2 on fr.feedUuid2 = f2.uuid
where fr.feedUuid1 = /*uuid*/'test'
limit 3
