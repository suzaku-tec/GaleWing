--*DataTitle '"settings_class"'
--*CaptionFromComment
SELECT
    "id"                                        -- id
    , "name"                                    -- name
    , "parendId"                                -- parendId
FROM
    "settings_class"
WHERE
    "parendId" = /*id*/
ORDER BY
    "id"
