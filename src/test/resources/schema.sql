
CREATE SCHEMA IF NOT EXISTS `pf_test` DEFAULT CHARACTER SET utf8 ;

DROP table account;

CREATE TABLE  IF NOT EXISTS `account` (
  `id` char(40) NOT NULL DEFAULT '',
  `user_id` char(40) NOT NULL DEFAULT '',
  `text` char(100) NOT NULL DEFAULT '',
  `parent` char(40) NOT NULL DEFAULT '',
  `description` char(225) DEFAULT '',
  `type` char(20) NOT NULL DEFAULT '',
  `currency` char(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP table transaction;

CREATE TABLE  IF NOT EXISTS `transaction` (
  `id` char(40) NOT NULL DEFAULT '',
  `user_id` char(40) NOT NULL DEFAULT '',
  `date` datetime NOT NULL,
  `description` char(100) DEFAULT '',
  `withdraw_id` char(40) NOT NULL DEFAULT '',
  `deposit_id` char(40) NOT NULL DEFAULT '',
  `amount` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP table user;

CREATE TABLE  IF NOT EXISTS `user` (
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;







