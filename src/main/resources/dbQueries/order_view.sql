CREATE VIEW order_view AS
SELECT
	ROW_NUMBER() OVER (ORDER BY o.id) AS id,
	o.id AS order_id,
	o.order_status_type,
	c.name AS company_name,
	GROUP_CONCAT(yn.type, ' :', oi.quantity, ' kgs') AS items_with_quantities
FROM
	orders o
JOIN
	company c ON o.company_id = c.id
LEFT JOIN
	company_yarn_order co ON o.id = co.order_id
LEFT JOIN
	yarn_order_item oi ON co.id = oi.company_yarn_order_id
LEFT JOIN
	yarn yn ON yn.id = oi.yarn_id
GROUP BY
	o.id, o.order_status_type, c.name ;