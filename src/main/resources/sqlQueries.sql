---Query to get job rates,product,quantity,total_price,total_amount etc.. for all companies---
SELECT c.name, p.name, min(pd.rate),max(pd.rate),
	sum(pd.quantity), sum(pd.total_price), sum(i.total_amount), sum(i.total_tax_amount), sum(i.total_amount_after_tax)
FROM invoice i
JOIN company c ON i.bill_to_party_id=c.id
JOIN product_detail pd ON i.id=pd.invoice_id
JOIN product p ON pd.product_id=p.id
GROUP BY c.name,p.name
ORDER BY c.name;
---total amount of bill generated for all companies---
SELECT 
		i.invoice_no, c.name, COUNT(i.invoice_no), sum(i.total_amount_after_tax) amount

FROM invoice i
JOIN company c ON i.bill_to_party_id = c.id
GROUP BY c.name
ORDER BY amount DESC;
