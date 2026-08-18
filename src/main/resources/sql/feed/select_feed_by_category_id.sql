select f.*
from feed_classification fc,
     feed f
where fc.feed_link = f.link
  and fc.primary_category = /*categoryId*/''
order by fc.classified_at asc;
