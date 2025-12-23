delete
FROM feed_category as fc
where not EXISTS (SELECT *
                  from feed f
                  WHERE fc.link = f.link)
