WITH filtered_trend AS (
    -- 最初に対象期間のデータだけに絞り込む
    SELECT * FROM public.trend
    WHERE "date" BETWEEN /*minDate*/'2026-01-01' AND /*maxDate*/'2026-01-18'
),
     ranked_data AS (
         -- 絞り込んだデータに対して順位をつける
         SELECT
             "date",
             word,
             count,
             RANK() OVER (PARTITION BY "date" ORDER BY count DESC) as rank
         FROM
             filtered_trend
     ),
     top_10_words AS (
         -- 指定期間中に一度でも10位以内に入った単語を特定
         SELECT DISTINCT word
         FROM ranked_data
         WHERE rank <= 10
     )
-- 最終結果を出力
SELECT
    r."date" as label,
    r.word as data,
    r.rank as rank
FROM
    ranked_data r
        JOIN
    top_10_words t10 ON r.word = t10.word
ORDER BY
    r."date", r."rank" ;