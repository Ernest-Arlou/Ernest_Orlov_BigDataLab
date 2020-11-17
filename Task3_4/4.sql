;WITH CTE_STOP_BY_OUTCOME
AS
(
SELECT DISTINCT
*,
(COUNT(*) OVER(PARTITION BY "officer-defined-ethnicity", outcome)) AS stops_by_ethicity_and_outcome,
(COUNT(*) OVER(PARTITION BY "officer-defined-ethnicity")) AS "Stops by ethnicity",
(COUNT("object-of-search") OVER(PARTITION BY "officer-defined-ethnicity","object-of-search")) AS populariti_objects
FROM
"Stop-and-search"
WHERE "officer-defined-ethnicity" IS NOT NULL
AND "date-time" BETWEEN ? AND ?
),
CTE_ARRESTS
AS
(
SELECT DISTINCT
"officer-defined-ethnicity",
"Stops by ethnicity",
stops_by_ethicity_and_outcome AS "Arests"	
FROM
CTE_STOP_BY_OUTCOME
WHERE outcome = 'Arrest'
),
CTE_NOTHING_DONE
AS
(
SELECT DISTINCT
"officer-defined-ethnicity",
"Stops by ethnicity",
stops_by_ethicity_and_outcome AS "Noting done"	
FROM
CTE_STOP_BY_OUTCOME
WHERE outcome = 'A no further action disposal'
),
CTE_OTHER_OUTCOMES
AS 
(
SELECT DISTINCT
"officer-defined-ethnicity",
"Stops by ethnicity",
(count(stops_by_ethicity_and_outcome) OVER(PARTITION BY "officer-defined-ethnicity")) AS "Other outcomes"
FROM
CTE_STOP_BY_OUTCOME
WHERE outcome != 'A no further action disposal'
AND outcome != 'Arrest'
),
CTE_Popular_Object_by_Ethn
AS
(
SELECT DISTINCT
"officer-defined-ethnicity",
(MAX(populariti_objects) OVER(PARTITION BY "officer-defined-ethnicity")) AS max_pop
FROM
CTE_STOP_BY_OUTCOME
WHERE outcome = 'A no further action disposal'
),
CTE_Obj_list
AS
(
SELECT DISTINCT
"object-of-search",
populariti_objects	
FROM
CTE_STOP_BY_OUTCOME
)

SELECT DISTINCT
CTE_NOTHING_DONE."officer-defined-ethnicity" AS "Officer-defined ethnicity",
CTE_NOTHING_DONE."Stops by ethnicity",
"Arests" AS "Arrest rate",
"Noting done" AS "No action rate",
"Other outcomes" AS "Other outcome rate",
CTE_Obj_list."object-of-search" AS "Most popular object of search"
FROM 
CTE_NOTHING_DONE 
INNER JOIN CTE_ARRESTS ON CTE_NOTHING_DONE."officer-defined-ethnicity" = CTE_ARRESTS."officer-defined-ethnicity"
INNER JOIN CTE_OTHER_OUTCOMES ON CTE_ARRESTS."officer-defined-ethnicity" = CTE_OTHER_OUTCOMES."officer-defined-ethnicity"
INNER JOIN CTE_Popular_Object_by_Ethn ON CTE_ARRESTS."officer-defined-ethnicity" = CTE_Popular_Object_by_Ethn."officer-defined-ethnicity"
INNER JOIN CTE_Obj_list ON CTE_Popular_Object_by_Ethn.max_pop = CTE_Obj_list.populariti_objects
ORDER BY CTE_NOTHING_DONE."officer-defined-ethnicity" 

