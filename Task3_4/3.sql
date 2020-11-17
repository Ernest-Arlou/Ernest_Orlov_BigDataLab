SELECT 
*,
(PERCENT_RANK() OVER(ORDER BY "Crimes by street")) "% of total crimes"
FROM
(SELECT 
"Street".id AS "Street ID",
"Street".name AS "Street name",
"Outcome-status".category AS "Outcome category value",
(COUNT("Street".name) OVER(PARTITION BY "Outcome-status".category, "Street".name)) "Crimes by street",
(COUNT("Street".name) OVER(PARTITION BY "Outcome-status".category)) "Total crimes by outcome"
FROM "Crimes" 
INNER JOIN "Location" ON "Crimes"."location-id" = "Location".id
INNER JOIN "Street" ON "Location"."street-id" = "Street".id
INNER JOIN "Outcome-status" ON "Crimes"."outcome-status-id" = "Outcome-status".id 
WHERE 
month BETWEEN ? AND ?
AND "Outcome-status".category = ?
ORDER BY "Street".name ) AS DERIVED
ORDER BY "Crimes by street" DESC