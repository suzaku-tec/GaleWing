WITH daily AS (
    SELECT
        to_timestamp(
                f.publisheddate,
                'YYYY-MM-DD HH24:MI:SS'
        )::date AS stat_date,
        fk.normalized_keyword,
        COUNT(
                DISTINCT (f.uuid, f.link)
        ) AS article_count
    FROM public.feed AS f
             INNER JOIN public.feed_keyword AS fk
                        ON fk.feed_uuid = f.uuid
                            AND fk.feed_link = f.link
    WHERE f.publisheddate IS NOT NULL
      AND fk.normalized_keyword IS NOT NULL
      AND fk.normalized_keyword <> ''
    GROUP BY
        to_timestamp(
                f.publisheddate,
                'YYYY-MM-DD HH24:MI:SS'
        )::date,
    fk.normalized_keyword
    ),
    with_previous AS (
SELECT
    stat_date,
    normalized_keyword,
    article_count,
    LAG(article_count) OVER (
    PARTITION BY normalized_keyword
    ORDER BY stat_date
    ) AS previous_count
FROM daily
    )
SELECT
    stat_date,
    normalized_keyword,
    article_count,
    COALESCE(
            previous_count,
            0
    ) AS previous_count,
    article_count
        - COALESCE(
            previous_count,
            0
          ) AS increase
FROM with_previous
WHERE stat_date =  CAST(/*targetDate*/'2026-09-23' AS date)
ORDER BY
    increase DESC,
    article_count DESC
    LIMIT 30