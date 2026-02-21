insert into SALESMAN (name, surname, pesel)
values
('Stefan', 'Sprzedawca', '67020499436'),
('Agnieszka', 'Samochodowa', '73021314515'),
('Tomasz', 'Kombi', '55091699846'),
('Rafał', 'Dach', '62081825675');

insert into MECHANIC (name, surname, pesel)
values
('Robert', 'Śrubokręt', '52070997836'),
('Zygmunt', 'Naprawa', '83011863727'),
('Remigiusz', 'Alufelga', '67111396321');

insert into CAR_TO_BUY (vin, brand, model, production_year, color, price)
values
('1FT7X2B60FEA74019', 'BMW', 'Series 1', '2020', 'black', '20000'),
('1N6BD06T45C416702', 'BMW', 'Series 3', '2020', 'black', '30000'),
('1G1PE5S97B7239380', 'BMW', 'Series 3', '2020', 'black', '30000'),
('1GCEC19X27Z109567', 'BMW', 'Series 5', '2020', 'black', '40000'),
('2C3CDYAG2DH731952', 'BMW', 'Series 5', '2020', 'black', '40000'),
('1GB6G5CG1C1105936', 'BMW', 'Series 7', '2020', 'black', '60000');

insert into SERVICE (service_code, description, price)
values
('58394-014', 'Wymiana koła', '240.20'),
('55319-866', 'Strojenie koła', '50.20'),
('0008-0407', 'Wymiana oleju', '140.15'),
('43063-180', 'Wymiana korka do oleju', '17.19'),
('14222-2022', 'Wymiana filtra paliwa', '14.98');

insert into PART (serial_number, description, price)
values
('11523-7310', 'Koło', '320.11'),
('54340-777', 'Olej', '270.18'),
('68180-556', 'Korek do oleju', '140.15'),
('0268-1264', 'Filtr powietrza', '90.19');