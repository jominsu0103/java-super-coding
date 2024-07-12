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

ALTER TABLE users MODIFY shopping_pay DECIMAL(10,2) DEFAULT 0;
INSERT INTO users (username, email, password, phone_num, address, gender, profile_picture_url, about_me)
VALUES ('johndoe', 'johndoe@example.com', 'password1234', '010-1234-5678', '1234 Main St, Anytown', 'Male', 'http://example.com/profile.jpg', 'Just a regular John Doe.');

INSERT INTO users (username, email, password, phone_num, address, gender, profile_picture_url, about_me)
VALUES ('example', 'example@example.com', 'example1234', '010-1111-1111', 'example address', 'Female', 'http://example.com/profile.jpg', 'Just a example');

ALTER TABLE items CHANGE category category_id INT;

desc items ;

ALTER TABLE items ADD CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES categories(id);

INSERT INTO categories(category_name) VALUES ('상의'),('하의'),('아우터'),('모자');

INSERT INTO items (name, description, price, stock, seller_id, category_id, image_url, listed_date, end_date)
VALUES ('셔츠', '기본형 와이셔츠',25000,100,2,1,'item example image url','2024-06-20','2024-07-20');

INSERT INTO items (name, description, price, stock, seller_id, category_id, image_url, listed_date, end_date)
VALUES ('와이드 데님팬츠', '쿨링 와이드 밴딩 데님팬츠',38000,100,2,2,'jean example image url','2024-06-20','2024-07-20');

INSERT INTO items_options(item_id,option_name,additional_cost) VALUES (1,'색상',0);
INSERT INTO items_options(item_id,option_name,additional_cost) VALUES (1,'사이즈',0);

INSERT INTO items_options(item_id,option_name,additional_cost) VALUES (2,'색상',0);
INSERT INTO items_options(item_id,option_name,additional_cost) VALUES (2,'사이즈',0);

INSERT INTO option_values (option_id,value_name,additional_cost) VALUES (1,'빨강',0),(1,'주황',0),(1,'노랑',0),(1,'초록',0),(1,'파랑',0),(1,'남색',0),(1,'보라',0);
INSERT INTO option_values (option_id,value_name,additional_cost) VALUES (2,'90',0),(2,'95',0),(2,'100',0),(2,'105',0);

INSERT INTO option_values (option_id,value_name,additional_cost) VALUES (3,'연청',0),(3,'진청',0),(3,'흑청',0);
INSERT INTO option_values (option_id,value_name,additional_cost) VALUES (4,'S',0),(4,'M',0),(4,'L',0),(4,'XL',0);

INSERT INTO carts(user_id) VALUES (1);

INSERT INTO cart_items (cart_id,item_id,quantity) VALUES (1,1,2);
INSERT INTO cart_items_item_options (cart_items_id , items_options_id) VALUES (1,1);
INSERT INTO cart_items_item_options (cart_items_id , items_options_id) VALUES (1,2);
INSERT INTO cart_items_option_values(cart_items_id , option_values_id) VALUES (1,6);
INSERT INTO cart_items_option_values (cart_items_id, option_values_id) VALUES (1,10);

INSERT INTO cart_items (cart_id,item_id,quantity) VALUES (1,2,1);
INSERT INTO cart_items_item_options (cart_items_id , items_options_id) VALUES (2,3);
INSERT INTO cart_items_item_options (cart_items_id , items_options_id) VALUES (2,4);
INSERT INTO cart_items_option_values(cart_items_id , option_values_id) VALUES (2,13);
INSERT INTO cart_items_option_values (cart_items_id, option_values_id) VALUES (2,17);

INSERT INTO orders(user_id , total_price , order_date) VALUES (1 , 0,'2024-06-22 23:00');
INSERT INTO order_items (order_id , item_id , quantity , price_per_unit) VALUES (1 , 1 , 2 , 25000);
INSERT INTO order_items (order_id, item_id , quantity , price_per_unit) VALUES (1 , 2 , 1 , 38000);
UPDATE orders SET total_price = (25000 + 38000);

SELECT
    o.id AS order_id,
    o.total_price AS total_price,
    o.order_date AS order_date,
    JSON_ARRAYAGG(
            JSON_OBJECT(
                    'item_id', oi.item_id,
                    'quantity', oi.quantity,
                    'price_per_unit', oi.price_per_unit
            )
    ) AS items
FROM
    orders o
        JOIN
    order_items oi ON o.id = oi.order_id
WHERE
    o.user_id = 1
GROUP BY
    o.id, o.total_price, o.order_date;

-- json-arrayagg와 json_object를 사용해 json 형태의 배열로 감싸서 여러 정보들을 취합해 조회할 수 있음

-- @Query(value = "SELECT JSON_ARRAYAGG(" +
--             "JSON_OBJECT('order_id', o.id, 'total_price', o.total_price, 'order_date', o.order_date, " +
--             "'items', (SELECT JSON_ARRAYAGG(JSON_OBJECT('item_id', oi.item_id, 'quantity', oi.quantity, 'price_per_unit', oi.price_per_unit, 'name', i.name)) " +
--             "FROM order_items oi " +
--             "JOIN items i ON oi.item_id = i.id " +
--             "WHERE oi.order_id = o.id))) AS order_details " +
--             "FROM orders o " +
--             "WHERE o.user_id = ?1 " +
--             "GROUP BY o.id, o.total_price, o.order_date", nativeQuery = true)
--     String findOrdersWithItemsByUserId(Integer userId);

-- 배열을 파싱할때 생기는 문제 해결

-- project2nd S3 access
-- S3 logic update
-- log info warn error 작성 및 2차 프로젝트 종료
-- project 피드백 대기 및 3차 프로젝트 준비
-- project 자유주제 및 ERD 기획단계 준비
-- 파이널 프로젝트 주제 선정 및 대분류 기능기획
-- figma wireframe 작성중
-- ERD v1 작성 project 3rd
-- ERD v2 작성 project 3rd 노션 참고 가능하도록 read.me 작성예정