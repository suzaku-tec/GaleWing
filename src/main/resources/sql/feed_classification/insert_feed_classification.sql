insert
into
    feed_classification
(feed_link,
 primary_category,
 categories_json,
 scores_json,
 matched_rule_ids_json,
 classifier_version,
 classified_at)
values
    (/*feedLink*/'',
     /*primaryCategory*/'',
     /*categoriesJson*/'',
     /*scoresJson*/'',
     /*matchedRuleIdsJson*/'',
     /*classifierVersion*/'',
     /*classifiedAt*/'')
on
    conflict (feed_link)
do update set
    primary_category = /*primaryCategory*/'',
    categories_json = /*categoriesJson*/'',
    scores_json = /*scoresJson*/'',
    matched_rule_ids_json = /*matchedRuleIdsJson*/'',
    classifier_version = /*classifierVersion*/'',
    classified_at = /*classifiedAt*/''
