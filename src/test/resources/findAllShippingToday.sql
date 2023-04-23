ALTER SEQUENCE item_group_seq RESTART WITH 2;
UPDATE item_group SET id=nextval('item_group_seq');

insert into orders(customer_id, item_group_id) VALUES ( 1, null );
insert into item_group(id, item_id, price, amount, shipping_date, order_id) VALUES ( 1, 1, 2.22, 1, current_date, 1 );
update orders set item_group_id = 1 where id = 1;
