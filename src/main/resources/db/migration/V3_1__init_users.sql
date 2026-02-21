ALTER TABLE salesman
ADD COLUMN user_id INT,
ADD FOREIGN KEY (user_id) REFERENCES car_dealership_user (user_id);

ALTER TABLE mechanic
ADD COLUMN user_id INT,
ADD FOREIGN KEY (user_id) REFERENCES car_dealership_user (user_id);

insert into car_dealership_user (user_id, user_name, email, password, active) values (1, 'stefan_sprzedawca', 'stefan_sprzedawca@zajavka.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into car_dealership_user (user_id, user_name, email, password, active) values (2, 'agnieszka_samochodowa', 'agnieszka_samochodowa@zajavka.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into car_dealership_user (user_id, user_name, email, password, active) values (3, 'tomasz_kombi', 'tomasz_kombi@zajavka.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into car_dealership_user (user_id, user_name, email, password, active) values (4, 'rafal_dach', 'rafal_dach@zajavka.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);

insert into car_dealership_user (user_id, user_name, email, password, active) values (5, 'robert_srubokret', 'robert_srubokret@zajavka.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into car_dealership_user (user_id, user_name, email, password, active) values (6, 'zygmunt_naprawa', 'zygmunt_naprawa@zajavka.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into car_dealership_user (user_id, user_name, email, password, active) values (7, 'remigiusz_alufelga', 'remigiusz_alufelga@zajavka.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);

insert into car_dealership_user (user_id, user_name, email, password, active) values (8, 'test_user', 'test_user@zajavka.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);

UPDATE salesman SET user_id = 1 WHERE pesel = '67020499436';
UPDATE salesman SET user_id = 2 WHERE pesel = '73021314515';
UPDATE salesman SET user_id = 3 WHERE pesel = '55091699846';
UPDATE salesman SET user_id = 4 WHERE pesel = '62081825675';

UPDATE mechanic SET user_id = 5 WHERE pesel = '52070997836';
UPDATE mechanic SET user_id = 6 WHERE pesel = '83011863727';
UPDATE mechanic SET user_id = 7 WHERE pesel = '67111396321';

insert into car_dealership_role (role_id, role) values (1, 'SALESMAN'), (2, 'MECHANIC'), (3, 'REST_API');

insert into car_dealership_user_role (user_id, role_id) values (1, 1), (2, 1), (3, 1), (4, 1);
insert into car_dealership_user_role (user_id, role_id) values (5, 2), (6, 2), (7, 2);
insert into car_dealership_user_role (user_id, role_id) values (8, 3);

ALTER TABLE salesman
ALTER COLUMN user_id SET NOT NULL;

ALTER TABLE mechanic
ALTER COLUMN user_id SET NOT NULL;