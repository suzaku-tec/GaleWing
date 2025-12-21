delete FROM from feed_category fc where not EXISTS (
    SELECT * from feed f WHERE
    fc.link = f.link
    )
