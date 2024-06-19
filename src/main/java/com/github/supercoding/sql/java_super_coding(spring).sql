use electronic;

create table store_sales(
	id int auto_increment primary key,
    store_name varchar(30),
    amount int not null default 0 check(amount >= 0)
);

create table item(
	id int auto_increment primary key,
	name varchar(50) not null unique,
	type varchar(50) not null,
	price int,
    store_id int,
    stock int not null default 0 check(stock >= 0),
	cpu varchar(30),
	capacity varchar(30),
    foreign key (store_id) references store_sales(id)
);

insert into store_sales(store_name , amount)
values
	('store a',10000),
    ('store b',10000),
    ('store c',15000),
    ('store d',20000),
    ('store e',18000);


insert into item(name ,type, price,store_id,stock,cpu,capacity)
values
('apple iphone 12 pro max','smartphone',1490000,1,100,'a14 bionic','512gb'),
('samsung galaxy 21 ultra','smartphone',1690000,2,80,'exynos 2100','256gb'),
('google pixel 6 pro','smartphone',1290000,3,120,'goggle tensor','128gb'),
('dell xps 15','laptop',2490000,4,50,'intel core i9','1tb ssd'),
('sony alpha 7','mirrorless camera',2590000,5,70,'bionz x','no internal stoage'),
('microsoft xbox series x','gaming console',490000,5,10,'cutom amd ze','1tb ssd');

drop table item;
drop table store_sales;

select * from item;
select * from store_sales;

select * from users;
select * from items;
select * from items_options;
select * from option_values;