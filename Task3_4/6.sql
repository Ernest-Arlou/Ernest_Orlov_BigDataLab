WITH 
CTE_all_stops_and_searches
AS (
SELECT
"Street".id AS street_id,
"Street".name AS street_name,
"object-of-search" AS object_of_search,
to_char("date-time", 'yyyy-MM') AS date
FROM "Stop-and-search"
INNER JOIN "Location" ON "Stop-and-search"."location-id" = "Location".id 
INNER JOIN "Street" ON "Location"."street-id" = "Street".id 	
WHERE 
"date-time" BETWEEN ? AND ?
AND outcome = 'Arrest'
AND (
"object-of-search" = 'Controlled drugs' 
OR "object-of-search" = 'Offensive weapons' 
OR "object-of-search" = 'Firearms' 
OR "object-of-search" = 'Stolen goods')
GROUP BY street_id, "object-of-search", "date-time"
),  
CTE_stops_drugs 
AS (
SELECT
street_id,
date,
SUM(COUNT(*)) OVER (PARTITION BY street_id, date) searches
FROM 
CTE_all_stops_and_searches
WHERE 
object_of_search = 'Controlled drugs'
GROUP BY street_id, date
),  
CTE_stops_weapons
AS (
SELECT
street_id,
date,
SUM(COUNT(*)) OVER (PARTITION BY street_id, date) searches
FROM 
CTE_all_stops_and_searches
WHERE 
(object_of_search = 'Offensive weapons' OR object_of_search = 'Firearms')
GROUP BY street_id, date 
),  
CTE_stops_thefts 
AS (
SELECT
street_id,
date,
SUM(COUNT(*)) OVER (PARTITION BY street_id, date) searches
FROM 
CTE_all_stops_and_searches
WHERE 
object_of_search = 'Stolen goods'
GROUP BY street_id, date  
),
CTE_all_crimes 
AS (
SELECT
"Street".id AS street_id,
to_char(month, 'yyyy-MM') AS date,
category
FROM "Crimes"
INNER JOIN "Location" ON "Crimes"."location-id" = "Location".id 
INNER JOIN "Street" ON "Location"."street-id" = "Street".id 
WHERE 
month BETWEEN ? AND ? 
), 
CTE_crimes_drugs 
AS (
SELECT
street_id,
date,
SUM(COUNT(*)) OVER (PARTITION BY street_id, date) crimes
FROM CTE_all_crimes
WHERE 
category = 'drugs'
GROUP BY street_id, date   
),  
CTE_crimes_weapons 
AS (
SELECT
street_id,
date,
SUM(COUNT(*)) OVER (PARTITION BY street_id, date) crimes
FROM
CTE_all_crimes
WHERE 
category = 'possession-of-weapons'
GROUP BY street_id, date   
),  
CTE_crimes_thefts 
AS (
SELECT
street_id,
date,
SUM(COUNT(*)) OVER (PARTITION BY street_id, date) crimes
FROM
CTE_all_crimes
WHERE 
(category = 'theft-FROM-the-person' OR category = 'shoplifting')
GROUP BY street_id, date   
) 
SELECT
all_stops.street_id AS "Street ID", 
street_name AS "Street Name",
all_stops.date AS "Month",
COALESCE(drugs_cr.crimes, 0) AS "Drugs stop-AND-search count",
COALESCE(drugs_st.searches, 0) AS "Drugs crimes count",
COALESCE(weapons_cr.crimes, 0) AS "Weapons crimes count",
COALESCE(weapons_st.searches, 0) AS "Weapons stop-AND-search count",
COALESCE(thefts_cr.crimes, 0) AS "Weapons crimes count",
COALESCE(thefts_st.searches, 0) AS "Weapons stop-AND-search count"
FROM 
CTE_all_stops_and_searches 
AS all_stops
LEFT JOIN CTE_crimes_drugs drugs_cr ON all_stops.street_id = drugs_cr.street_id AND all_stops.date = drugs_cr.date
LEFT JOIN CTE_crimes_weapons weapons_cr ON all_stops.street_id = weapons_cr.street_id AND all_stops.date = weapons_cr.date
LEFT JOIN CTE_crimes_thefts thefts_cr ON all_stops.street_id = thefts_cr.street_id AND all_stops.date = thefts_cr.date
LEFT JOIN CTE_stops_drugs drugs_st ON all_stops.street_id = drugs_st.street_id AND all_stops.date = drugs_st.date
LEFT JOIN CTE_stops_weapons weapons_st ON all_stops.street_id = weapons_st.street_id AND all_stops.date = weapons_st.date
LEFT JOIN CTE_stops_thefts thefts_st ON all_stops.street_id = thefts_st.street_id AND all_stops.date = thefts_st.date
ORDER BY street_name, all_stops.date
