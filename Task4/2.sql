SELECT
*,
(LAG("Current month count",1) OVER(PARTITION BY "Crime category" )) AS "Previous month count",
(	round((
	("Current month count" - LAG("Current month count",1) OVER(PARTITION BY "Crime category"))/
	(LAG("Current month count",1) OVER(PARTITION BY "Crime category"))::float * 100
		)::numeric, 2)
) AS "Basic growth rate %",
(abs(("Current month count" - LAG("Current month count",1) OVER(PARTITION BY "Crime category")))) AS "Delta"
FROM
(SELECT DISTINCT
category AS "Crime category",
month AS "Month",
(COUNT(category) OVER( PARTITION BY category, month  )) "Current month count"
FROM "Crimes" 
WHERE month BETWEEN ? AND ?
ORDER BY category, month) AS DERIVED