select * from feed a
    where exists (
        select * from feed b where b.link = /*link*/'https://www.watch.impress.co.jp/docs/news/2152097.html'
        and similarity(b.title , a.title ) > /*rate*/0.4
        and a.link != b.link
)
