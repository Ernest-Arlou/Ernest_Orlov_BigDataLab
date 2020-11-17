SELECT DISTINCT
"officer-defined-ethnicity",
"Stops by ethnicity"

FROM
(SELECT 
"officer-defined-ethnicity",
(COUNT(*) OVER(PARTITION BY "officer-defined-ethnicity")) AS "Stops by ethnicity",
(COUNT(*) OVER(PARTITION BY outcome)) AS "Stops by outcome",
(COUNT(*) OVER(PARTITION BY "object-of-search")) AS "popularity"
FROM
"Stop-and-search"
WHERE "officer-defined-ethnicity" IS NOT NULL
ORDER BY "officer-defined-ethnicity" DESC) AS DERIVED