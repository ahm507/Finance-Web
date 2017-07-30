
Drop database IF EXISTS pf_test;
CREATE SCHEMA `pf_test` DEFAULT CHARACTER SET utf8 ;

USE `pf_test`;

--  ===============================

DROP table IF EXISTS `transaction`;
CREATE TABLE  `transaction` (
  `id` char(40) NOT NULL DEFAULT '',
  `user_id` char(40) NOT NULL DEFAULT '',
  `date` datetime NOT NULL,
  `description` char(100) DEFAULT '',
  `withdraw_id` char(40) NOT NULL DEFAULT '',
  `deposit_id` char(40) NOT NULL DEFAULT '',
  `amount` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;


--  ===============================

DROP table IF EXISTS account;

CREATE TABLE `account` (
  `id` char(40) NOT NULL DEFAULT '',
  `user_id` char(40) NOT NULL DEFAULT '',
  `text` char(100) NOT NULL DEFAULT '',
  `parent` char(40) NOT NULL DEFAULT '',
  `description` char(225) DEFAULT '',
  `type` char(20) NOT NULL DEFAULT '',
  `currency` char(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

--  ===============================

DROP table IF EXISTS user;

CREATE TABLE  `user` (
  `id` char(40) NOT NULL DEFAULT '',
  `email` char(60) NOT NULL DEFAULT '',
  `password` char(45) NOT NULL DEFAULT '',
  `creation_date` datetime NOT NULL,
  `verified` tinyint(1) DEFAULT '0',
  `verification_key` char(40) DEFAULT NULL,
  `reset_password_key` char(40) DEFAULT NULL,
  `usd_rate` double DEFAULT 9.0,
  `sar_rate` double DEFAULT 2.5,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- ALTER TABLE `user` ADD INDEX (`email`);



-- ALTER TABLE `account` ADD INDEX (`user_id`);
-- ALTER TABLE `account` ADD INDEX (`parent`);

-- =========================================================



--
--
-- ALTER TABLE `transaction` ADD INDEX (`user_id`);
-- ALTER TABLE `transaction` ADD INDEX (`date`);
-- ALTER TABLE `transaction` ADD INDEX (`withdraw_id`);
-- ALTER TABLE `transaction` ADD INDEX (`deposit_id`);


-- Add Rleationships

-- ALTER TABLE `account` ADD FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
-- ALTER TABLE `transaction` ADD FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

