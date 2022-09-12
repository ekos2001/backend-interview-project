--security user
INSERT INTO USER(LOGIN, PASSWORD) VALUES ('admin', '$2a$12$nygY3A4rca2BvwUEk/qKTuKYfZ6G9WpshlfxIFui/LRAoecs2n.KS');
--customers
INSERT INTO CUSTOMER(ID, NAME, COST) VALUES (1, 'Google', 71);
INSERT INTO CUSTOMER(ID, NAME, COST) VALUES (2, 'Amazon', 28);

--Device type
INSERT INTO DEVICE_TYPE(ID, TYPE, NAME, COST) values (1, 'WINDOWS_WORKSTATION', 'Windows Workstation', 4);
INSERT INTO DEVICE_TYPE(ID, TYPE, NAME, COST) values (2, 'WINDOWS_SERVER', 'Windows Server', 4);
INSERT INTO DEVICE_TYPE(ID, TYPE, NAME, COST) values (3, 'MAC', 'Mac OS', 4);

--Service type
INSERT INTO SERVICE_TYPE(ID, TYPE, NAME, COST) values (1, 'ANTIVIRUS_WIN', 'Antivirus for Windows', 5);
INSERT INTO SERVICE_TYPE(ID, TYPE, NAME, COST) values (2, 'ANTIVIRUS_MAC', 'Antivirus for macOS', 7);
INSERT INTO SERVICE_TYPE(ID, TYPE, NAME, COST) values (3, 'BACKUP', 'Backup service', 3);
INSERT INTO SERVICE_TYPE(ID, TYPE, NAME, COST) values (4, 'PSA', 'Professional Services Automation system', 2);
INSERT INTO SERVICE_TYPE(ID, TYPE, NAME, COST) values (5, 'SCREEN_SHARE', 'Screen Share service', 1);

--Google devices
INSERT INTO DEVICE(ID, NAME, DEVICE_TYPE_ID, CUSTOMER_ID) values (1, 'Dell Inspiron', 1, 1);
INSERT INTO DEVICE(ID, NAME, DEVICE_TYPE_ID, CUSTOMER_ID) values (2, 'HP Server', 2, 1);
INSERT INTO DEVICE(ID, NAME, DEVICE_TYPE_ID, CUSTOMER_ID) values (3, 'MacBook Pro', 3, 1);
INSERT INTO DEVICE(ID, NAME, DEVICE_TYPE_ID, CUSTOMER_ID) values (4, 'MacBook Air', 3, 1);
INSERT INTO DEVICE(ID, NAME, DEVICE_TYPE_ID, CUSTOMER_ID) values (5, 'MacBook Air 2022', 3, 1);

--Google antivirus
INSERT INTO SERVICE(SERVICE_TYPE_ID, DEVICE_ID) VALUES (1, 1); --Windows Dell Inspiron
INSERT INTO SERVICE(SERVICE_TYPE_ID, DEVICE_ID) VALUES (1, 2); --Windows HP Server
INSERT INTO SERVICE(SERVICE_TYPE_ID, DEVICE_ID) VALUES (2, 3); --MacBook Pro
INSERT INTO SERVICE(SERVICE_TYPE_ID, DEVICE_ID) VALUES (2, 4); --MacBook Air
INSERT INTO SERVICE(SERVICE_TYPE_ID, DEVICE_ID) VALUES (2, 5); --MacBook Air 2022

--Google backup
INSERT INTO SERVICE(SERVICE_TYPE_ID, DEVICE_ID) VALUES (3, 1); --Windows Dell Inspiron
INSERT INTO SERVICE(SERVICE_TYPE_ID, DEVICE_ID) VALUES (3, 2); --Windows HP Server
INSERT INTO SERVICE(SERVICE_TYPE_ID, DEVICE_ID) VALUES (3, 3); --MacBook Pro
INSERT INTO SERVICE(SERVICE_TYPE_ID, DEVICE_ID) VALUES (3, 4); --MacBook Air
INSERT INTO SERVICE(SERVICE_TYPE_ID, DEVICE_ID) VALUES (3, 5); --MacBook Air 2022

--Google screen share
INSERT INTO SERVICE(SERVICE_TYPE_ID, DEVICE_ID) VALUES (5, 1); --Windows Dell Inspiron
INSERT INTO SERVICE(SERVICE_TYPE_ID, DEVICE_ID) VALUES (5, 2); --Windows HP Server
INSERT INTO SERVICE(SERVICE_TYPE_ID, DEVICE_ID) VALUES (5, 3); --MacBook Pro
INSERT INTO SERVICE(SERVICE_TYPE_ID, DEVICE_ID) VALUES (5, 4); --MacBook Air
INSERT INTO SERVICE(SERVICE_TYPE_ID, DEVICE_ID) VALUES (5, 5); --MacBook Air 2022

--Amazon devices
INSERT INTO DEVICE(ID, NAME, DEVICE_TYPE_ID, CUSTOMER_ID) values (6, 'Dell Latitude', 1, 2);
INSERT INTO DEVICE(ID, NAME, DEVICE_TYPE_ID, CUSTOMER_ID) values (7, 'Dell OptiPlex', 2, 2);
INSERT INTO DEVICE(ID, NAME, DEVICE_TYPE_ID, CUSTOMER_ID) values (8, 'MacBook Pro 2020', 3, 2);

--Amazon antivirus
INSERT INTO SERVICE(SERVICE_TYPE_ID, DEVICE_ID) VALUES (1, 6); --Windows Dell Latitude
INSERT INTO SERVICE(SERVICE_TYPE_ID, DEVICE_ID) VALUES (2, 7); --Windows Dell OptiPlex

--Amazon PSA
INSERT INTO SERVICE(SERVICE_TYPE_ID, DEVICE_ID) VALUES (4, 7); --Windows Dell OptiPlex
INSERT INTO SERVICE(SERVICE_TYPE_ID, DEVICE_ID) VALUES (4, 8); --MacBook Pro 2020