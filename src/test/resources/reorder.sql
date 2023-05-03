ALTER SEQUENCE item_group_seq RESTART WITH 2;
UPDATE item_group SET id=nextval('item_group_seq');
ALTER SEQUENCE order_seq RESTART WITH 2;
UPDATE orders SET id=nextval('order_seq');

insert into orders(customer_id, item_group_id) values ( 1, null );
insert into item_group(item_id, price, amount, shipping_date, order_id) VALUES (1, 2.22, 1, now(), 1 );
update orders set item_group_id = 1 where orders.id = 1;
