


DELETE from account where user_id='4f680c93-838d-4329-9aba-7bedca232a89';

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;

INSERT INTO `account` (`id`, `user_id`, `text`, `parent`, `description`, `type`, `currency`)
VALUES
	('1f00ce66-de99-45eb-92b9-a35ae41444d5','4f680c93-838d-4329-9aba-7bedca232a89','Home','c8cfc2b1-6239-4940-b3bc-98d3e0108519','','expense', 'egp'),
	('2100ba44-4d4d-49dd-a358-8ceb43ff2714','4f680c93-838d-4329-9aba-7bedca232a89','Master Card','46ed73c2-c377-4e6f-947f-3eff4cfce41f','','liability', 'egp'),
	('34791941-6b49-4a1b-9cdb-48908a863166','4f680c93-838d-4329-9aba-7bedca232a89','Income','0','','income', 'egp'),
	('4018d883-5936-468b-8fd8-d0f84adad927','4f680c93-838d-4329-9aba-7bedca232a89','Salary','34791941-6b49-4a1b-9cdb-48908a863166','','income', 'egp'),
	('46818ca8-690a-413f-8e0d-6c88b8835971','4f680c93-838d-4329-9aba-7bedca232a89','Gifts','c8cfc2b1-6239-4940-b3bc-98d3e0108519','','expense', 'egp'),
	('46ed73c2-c377-4e6f-947f-3eff4cfce41f','4f680c93-838d-4329-9aba-7bedca232a89','Liabilities','0','','liability', 'egp'),
	('509bc2c7-8e3e-4816-9028-5c1486ab2f1c','4f680c93-838d-4329-9aba-7bedca232a89','Assets','0','','asset', 'egp'),
	('52e2a234-498a-44da-abc2-5c95807ba531','4f680c93-838d-4329-9aba-7bedca232a89','Bank','509bc2c7-8e3e-4816-9028-5c1486ab2f1c','','asset', 'egp'),
	('912d844a-acb9-445a-9112-23a323d09691','4f680c93-838d-4329-9aba-7bedca232a89','Other Income','34791941-6b49-4a1b-9cdb-48908a863166','','income', 'egp'),
	('92c1a767-1ec8-4270-b277-7ea27d1f1563','4f680c93-838d-4329-9aba-7bedca232a89','Travel Allowance','34791941-6b49-4a1b-9cdb-48908a863166','','income', 'egp'),
	('ada765a9-60f1-4441-8e40-cb9c10794585','4f680c93-838d-4329-9aba-7bedca232a89','Cash in Wallet','509bc2c7-8e3e-4816-9028-5c1486ab2f1c','','asset', 'egp'),
	('b77e3103-612c-45a9-a4fc-fe2f0530f682','4f680c93-838d-4329-9aba-7bedca232a89','Car','c8cfc2b1-6239-4940-b3bc-98d3e0108519','','expense', 'egp'),
	('c8cfc2b1-6239-4940-b3bc-98d3e0108519','4f680c93-838d-4329-9aba-7bedca232a89','Expenses','0','','expense', 'egp'),
	('d2cedb8e-b049-464a-8142-c92814036047','4f680c93-838d-4329-9aba-7bedca232a89','Bonus','34791941-6b49-4a1b-9cdb-48908a863166','','income', 'egp'),
	('e08426cf-2457-4b81-a7b1-3939422d6a04','4f680c93-838d-4329-9aba-7bedca232a89','Tourism Allowance','34791941-6b49-4a1b-9cdb-48908a863166','','income', 'egp'),
	('e4ca9a32-850c-434e-af94-15d4bf2b9714','4f680c93-838d-4329-9aba-7bedca232a89','Charity','c8cfc2b1-6239-4940-b3bc-98d3e0108519','','expense', 'egp'),
	('ef8fc4d3-18f5-466e-abc3-d1e48d7ac48d','4f680c93-838d-4329-9aba-7bedca232a89','Car Allowance','34791941-6b49-4a1b-9cdb-48908a863166','','income', 'egp'),
	('f17c1285-5e89-4c96-a94e-992afb2da2df','4f680c93-838d-4329-9aba-7bedca232a89','MySelf','c8cfc2b1-6239-4940-b3bc-98d3e0108519','','expense', 'egp');

/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

DELETE from user where id='4f680c93-838d-4329-9aba-7bedca232a89';

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO `user` (`id`, `email`, `password`, `creation_date`, `verified`, `verification_key`, `reset_password_key`)
VALUES
	('4f680c93-838d-4329-9aba-7bedca232a89','test@test.test','caed27881edcf87a75fbaa57aaf4144c','2013-10-27 00:00:00','1','6eb2fecc-2799-4106-a192-d0824108c2f9','fake-reset-code');

UNLOCK TABLES;

DELETE from transaction  where user_id='4f680c93-838d-4329-9aba-7bedca232a89';

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;

INSERT INTO `transaction` (`id`, `user_id`, `date`, `description`, `withdraw_id`, `deposit_id`, `amount`)
VALUES
	('0c237e2b-ae89-4011-8b10-8d00f9a53645','4f680c93-838d-4329-9aba-7bedca232a89','2016-11-19','salary','52e2a234-498a-44da-abc2-5c95807ba531','46818ca8-690a-413f-8e0d-6c88b8835971',200),
	('15918936-7138-4de3-aad5-e891037bbff3','4f680c93-838d-4329-9aba-7bedca232a89','2016-10-19','salary','52e2a234-498a-44da-abc2-5c95807ba531','1f00ce66-de99-45eb-92b9-a35ae41444d5',600),
	('6d5a184b-7342-48e8-ac4d-2c5e3c7afb73','4f680c93-838d-4329-9aba-7bedca232a89','2016-11-19','salary','d2cedb8e-b049-464a-8142-c92814036047','52e2a234-498a-44da-abc2-5c95807ba531',9000),
	('78db8700-879f-4dfb-9fc6-b1dc054b0c8e','4f680c93-838d-4329-9aba-7bedca232a89','2016-12-19','travel allowance','92c1a767-1ec8-4270-b277-7ea27d1f1563','ada765a9-60f1-4441-8e40-cb9c10794585',500),
	('946e34c4-ed79-4478-a92e-09f43c7296aa','4f680c93-838d-4329-9aba-7bedca232a89','2016-11-19','allowance','ef8fc4d3-18f5-466e-abc3-d1e48d7ac48d','52e2a234-498a-44da-abc2-5c95807ba531',2000),
	('9de0a0df-c5dc-4392-b3c5-97def27d2909','4f680c93-838d-4329-9aba-7bedca232a89','2016-12-19','other','e08426cf-2457-4b81-a7b1-3939422d6a04','52e2a234-498a-44da-abc2-5c95807ba531',200),
	('ab994870-4b28-4e50-ae83-8b944cc6264d','4f680c93-838d-4329-9aba-7bedca232a89','2016-02-19','other','92c1a767-1ec8-4270-b277-7ea27d1f1563','52e2a234-498a-44da-abc2-5c95807ba531',200),
	('c9fab3da-faa4-41d4-b898-9eb94b5ab297','4f680c93-838d-4329-9aba-7bedca232a89','2016-11-19','Safari books','2100ba44-4d4d-49dd-a358-8ceb43ff2714','f17c1285-5e89-4c96-a94e-992afb2da2df',110),
	('d9be0dc7-7722-4c49-9d17-46078aceb369','4f680c93-838d-4329-9aba-7bedca232a89','2016-09-19','other','4018d883-5936-468b-8fd8-d0f84adad927','52e2a234-498a-44da-abc2-5c95807ba531',3000),
	('df570cd4-e4ce-4991-a4de-e3c0b069af35','4f680c93-838d-4329-9aba-7bedca232a89','2016-09-19','other','912d844a-acb9-445a-9112-23a323d09691','52e2a234-498a-44da-abc2-5c95807ba531',200),
	('e0dbd27f-e824-4562-b04b-fdca1964c54b','4f680c93-838d-4329-9aba-7bedca232a89','2016-08-19','salary','52e2a234-498a-44da-abc2-5c95807ba531','b77e3103-612c-45a9-a4fc-fe2f0530f682',100),
	('f1751737-2274-4f0d-a47e-26841816658f','4f680c93-838d-4329-9aba-7bedca232a89','2016-08-19','salary','52e2a234-498a-44da-abc2-5c95807ba531','f17c1285-5e89-4c96-a94e-992afb2da2df',110),
	('ff251761-5acb-4a4a-965d-45e98ecae933','4f680c93-838d-4329-9aba-7bedca232a89','2016-07-19','charity','52e2a234-498a-44da-abc2-5c95807ba531','e4ca9a32-850c-434e-af94-15d4bf2b9714',1000),
	
	('ff251761-5acb-4a4a-965d-45e98ecae933-2','4f680c93-838d-4329-9aba-7bedca232a89','2017-01-19','charity','52e2a234-498a-44da-abc2-5c95807ba531','e4ca9a32-850c-434e-af94-15d4bf2b9714',1000),
	('c9fab3da-faa4-41d4-b898-9eb94b5ab297-2','4f680c93-838d-4329-9aba-7bedca232a89','2017-01-19','Safari books','2100ba44-4d4d-49dd-a358-8ceb43ff2714','f17c1285-5e89-4c96-a94e-992afb2da2df',110),
	('946e34c4-ed79-4478-a92e-09f43c7296aa-2','4f680c93-838d-4329-9aba-7bedca232a89','2017-02-19','allowance','ef8fc4d3-18f5-466e-abc3-d1e48d7ac48d','52e2a234-498a-44da-abc2-5c95807ba531',2000),
	('15918936-7138-4de3-aad5-e891037bbff3-2','4f680c93-838d-4329-9aba-7bedca232a89','2017-03-19','salary','52e2a234-498a-44da-abc2-5c95807ba531','1f00ce66-de99-45eb-92b9-a35ae41444d5',600);
	
	

/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;


