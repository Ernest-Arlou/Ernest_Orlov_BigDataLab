;WITH CTE_Stop_by_street
AS
(
SELECT DISTINCT
"Street".id AS "Street ID",
"Street".name AS "Street name",	
"object-of-search",
"age-range",
gender,
"officer-defined-ethnicity",
outcome,
(COUNT("object-of-search") OVER(PARTITION BY "Street".id, "object-of-search")) AS search_obj_count,
(COUNT("age-range") OVER(PARTITION BY "Street".id, "age-range")) AS age_range_count,
(COUNT(gender) OVER(PARTITION BY "Street".id, gender)) AS gender_count,
(COUNT("officer-defined-ethnicity") OVER(PARTITION BY "Street".id, "officer-defined-ethnicity")) AS ethnicity_count,
(COUNT(outcome) OVER(PARTITION BY "Street".id, outcome)) AS outcome_count	
FROM
"Stop-and-search"
INNER JOIN "Location" on "Stop-and-search"."location-id" = "Location".id
INNER JOIN "Street" on "Location"."street-id" = "Street".id
WHERE "officer-defined-ethnicity" IS NOT NULL
AND "object-of-search" IS NOT NULL
AND "date-time" BETWEEN '2018-01-01' AND '2018-05-28' 

),
CTE_Most_popular_objects
AS
(
SELECT DISTINCT
"Street ID",
"Street name",
"object-of-search",
"age-range",
gender,
"officer-defined-ethnicity",
outcome,
search_obj_count,
age_range_count,
gender_count,
ethnicity_count,
outcome_count,
(MAX(search_obj_count) OVER(PARTITION BY "Street ID")) AS max_searced_obj_count,
(MAX(age_range_count) OVER(PARTITION BY "Street ID")) AS max_age_range_count,
(MAX(gender_count) OVER(PARTITION BY "Street ID")) AS max_gender_count,
(MAX(ethnicity_count) OVER(PARTITION BY "Street ID")) AS max_ethnicity_count,
(MAX(outcome_count) OVER(PARTITION BY "Street ID")) AS max_outcome_count
FROM CTE_Stop_by_street
)

SELECT DISTINCT
"Street ID",
"Street name",
"age-range" AS "Most popular age range" ,
gender AS "Most popular gender",
"officer-defined-ethnicity" AS "Most popular ethnicity",
"object-of-search" AS "Most popular object_of_search",
outcome AS "Most popular outcome"
FROM CTE_Most_popular_objects
WHERE search_obj_count = max_searced_obj_count
AND age_range_count = max_age_range_count
AND gender_count = max_gender_count
AND ethnicity_count = max_ethnicity_count
AND outcome_count = max_outcome_count
ORDER BY  "Street ID", "Street name"


