create sequence role_seq start with 1 increment by 1;
create table role
(
    id   bigint default nextval('role_seq') primary key,
    name varchar(20) not null
);

create sequence address_seq start with 1 increment by 1;
create table address
(
    id      bigint default nextval('address_seq') primary key,
    street  varchar not null,
    number  varchar not null,
    zipcode varchar not null,
    city    varchar not null
);

create table customer_roles
(
    customer_id bigint not null,
    roles_id    bigint not null
);

create sequence customer_seq start with 1 increment by 1;
create table customer
(
    id           bigint default nextval('customer_seq') primary key,
    first_name   varchar not null,
    last_name    varchar not null,
    email        varchar not null,
    address_id   bigint  not null,
    phone_number varchar not null,
    password     varchar not null,
    constraint address_id foreign key (address_id) references address (id)
);

create sequence item_seq start with 1 increment by 1;
create table item
(
    id           bigint default nextval('item_seq') primary key,
    name         varchar not null,
    description  varchar not null,
    price        float   not null,
    amount       integer not null,
    item_urgency varchar not null
);

create sequence item_group_seq start with 1 increment by 1;
create table item_group
(
    id            bigint default nextval('item_group_seq') primary key,
    item_id       bigint not null,
    price         float  not null,
    amount        int    not null,
    shipping_date date   not null,
    order_id      bigint not null
);


create sequence order_seq start with 1 increment by 1;
create table orders
(
    id            bigint default nextval('order_seq') primary key,
    customer_id   bigint not null,
    item_group_id bigint,
    constraint customer_id foreign key (customer_id) references customer (id),
    constraint item_group_id foreign key (item_group_id) references item_group (id)
);

alter table item_group
    add constraint order_id foreign key (order_id) references orders (id);



insert into role(id, name)
VALUES (1, 'ADMIN'),
       (2, 'USER');



insert into address(street, number, zipcode, city)
values ('cantersteen', '14', '1337', 'brussels');

insert into customer( first_name, last_name, email, address_id, phone_number, password)
values ('customer', 'customer', 'customer@customer.local', 1, '123456',
        '$2a$12$RBhGJSErgv30ciB4HjgvvumvkZ6/K2V2X84wxGfSZr3HJkC8JLxqm'); // pw is customer
insert into customer_roles(customer_id, roles_id)
VALUES (1, 1),
       (1, 2);
insert into item(name, description, price, amount, item_urgency)
values ('apple', 'It''s green', 2.22, 10, 'STOCK_HIGH');



