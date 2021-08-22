--*DataTitle '"settings_class"'
--*CaptionFromComment
SELECT
    "id"                                        -- id
    , "name"                                    -- name
    , "parendId"                                -- parendId
FROM
    "settings_class"
WHERE
    "parendId" IS NULL
ORDER BY
    "id"
