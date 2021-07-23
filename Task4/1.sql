SELECT DISTINCT
"Street".id AS street_id,
"Street".name AS street_name,
(COUNT("street-id") OVER(PARTITION BY "street-id")) crime_count,
CONCAT_WS(' ','from',?,'till',?) AS Period
FROM "Crimes" 
INNER JOIN "Location" ON "Crimes"."location-id" = "Location".id
INNER JOIN "Street" ON "Location"."street-id" = "Street".id
WHERE 
month BETWEEN ? AND ?
ORDER BY crime_count DESC
