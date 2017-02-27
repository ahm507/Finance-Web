

LOCK TABLES `account` WRITE;

INSERT INTO `account` (`id`, `userId`, `text`, `parent`, `description`, `type`)
VALUES
	('1','1','Income','0','','income'),
	('10','1','Expenses','0','','expense'),
	('11','1','Salary','1','','income'),
	('12','1','Bonus','1','','income'),
	('13','1','Cash','4','','asset'),
	('14','1','Bank Account','4','','asset'),
	('15','1','Master Card','7','','liability'),
	('16','1','Visa Card','7','','liability'),
	('17','1','Food','10','','expense'),
	('18','1','Clothes','10','','expense'),
	('19','1','Education','10','','expense'),
	('21','2','Income','0','','income'),
	('22','2','Salary2','1','','income'),
	('23','2','Bonus2','1','','income'),
	('24','2','Assets','0','','asset'),
	('25','2','Cash2','4','','asset'),
	('4','1','Assets','0','','asset'),
	('7','1','Liability','0','','liability');

UNLOCK TABLES;

LOCK TABLES `transaction` WRITE;

INSERT INTO `transaction` (`id`, `user`, `date`, `description`, `withdrawId`, `depositId`, `amount`)
VALUES
	('1','1','2013-12-01','desc','11','13',1000),
	('10','1','2012-11-01','desc','13','17',20),
	('11','1','2012-11-01','desc','13','17',50),
	('12','1','2013-11-01','desc','13','17',50),
	('13','1','2013-11-01','desc','13','17',50),
	('14','1','2013-11-01','desc','13','17',50),
	('15','1','2014-01-01','desc','13','17',30),
	('2','1','2013-12-01','desc','12','14',1000),
	('21','2','2013-12-01','desc','11','13',1000),
	('22','2','2013-12-01','desc','12','13',1000),
	('3','1','2013-12-01','desc','14','13',900),
	('4','1','2013-12-01','desc','16','13',300),
	('5','1','2013-12-01','desc','15','18',100),
	('6','1','2012-10-01','desc','13','18',150),
	('7','1','2013-10-01','desc','13','18',50),
	('8','1','2012-12-01','desc','13','19',350),
	('9','1','2013-12-01','desc','13','19',200);

UNLOCK TABLES;


LOCK TABLES `user` WRITE;

INSERT INTO `user` (`id`, `email`, `password`, `creation_date`, `verified`, `verification_key`, `reset_password_key`)
VALUES
	('1','ahm507@gmail.com','68783f989c5bb0a47a0ad24c87994f28','2013-10-01 00:00:00','1',NULL,NULL),
	('2','some@gmail.com','ece3231f76805e66e7a8767d4ae432b8','2013-10-01 00:00:00','1',NULL,NULL);

UNLOCK TABLES;

