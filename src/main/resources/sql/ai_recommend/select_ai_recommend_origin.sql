select f.*
from feed_ai fa,
     feed f
where fa.url = f.link
  and /*id*/'1' = fa.id