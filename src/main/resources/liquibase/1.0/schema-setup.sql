-- liquibase formatted SQL
-- changeset automation:1
-- Dumping database structure for automation


CREATE TABLE IF NOT EXISTS `project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_iflk2yk9ma95q0q9ovhftpi63` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `environment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `variables` varchar(255) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_f7r1jbacxgn7g52pj17tn6s3d` (`name`,`project_id`),
  KEY `FK_fs0g1vvbx6ctnlgtsnia7bomy` (`project_id`),
  CONSTRAINT `FK_fs0g1vvbx6ctnlgtsnia7bomy` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `environmentresource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `variables` varchar(255) NOT NULL,
  `environment_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_34ajwq2gtr7lawrdqfgvo3355` (`environment_id`),
  CONSTRAINT `FK_34ajwq2gtr7lawrdqfgvo3355` FOREIGN KEY (`environment_id`) REFERENCES `environment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `environment_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_9e05f57c7hm857wy98m78ypc7` (`name`),
  KEY `FK_fmlwcv95eyfopswswl33pkdia` (`environment_id`),
  CONSTRAINT `FK_fmlwcv95eyfopswswl33pkdia` FOREIGN KEY (`environment_id`) REFERENCES `environment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `schedule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `grid_url` varchar(255) DEFAULT NULL,
  `schedule_date` datetime DEFAULT NULL,
  `schedule_id` bigint(20) DEFAULT NULL,
  `environment_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_8ulxgl9lpbp6q8a7m3vlv98fl` (`environment_id`),
  CONSTRAINT `FK_8ulxgl9lpbp6q8a7m3vlv98fl` FOREIGN KEY (`environment_id`) REFERENCES `environment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `schedule_params` (
  `schedule_id` bigint(20) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`schedule_id`,`name`),
  CONSTRAINT `FK_ido1mkohfrw92k6mog0t46qmo` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `testcase` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `needResource` bit(1) NOT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_imuxe8474te44mhjdsuol9xry` (`name`,`project_id`),
  KEY `FK_19212dinwr6vdoaln24af3opd` (`project_id`),
  CONSTRAINT `FK_19212dinwr6vdoaln24af3opd` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `command` (
  `TestCase_id` bigint(20) NOT NULL,
  `element` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `params` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `commands_ORDER` int(11) NOT NULL,
  PRIMARY KEY (`TestCase_id`,`commands_ORDER`),
  CONSTRAINT `FK_og9o5wyoeo9awpjjymc7rpiy2` FOREIGN KEY (`TestCase_id`) REFERENCES `testcase` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `testsuite` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_9omatar6a3avqefscerkprlrw` (`name`,`project_id`),
  KEY `FK_5dorkidvo9j4ig43vm2as994a` (`project_id`),
  CONSTRAINT `FK_5dorkidvo9j4ig43vm2as994a` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `testsuite_testcase` (
  `TestSuite_id` bigint(20) NOT NULL,
  `testCases_id` bigint(20) NOT NULL,
  PRIMARY KEY (`TestSuite_id`,`testCases_id`),
  KEY `FK_khv8wjj9nfi9s8mbuhus9mkx1` (`testCases_id`),
  CONSTRAINT `FK_c1hv76f14wscei1j1hk2wus8r` FOREIGN KEY (`TestSuite_id`) REFERENCES `testsuite` (`id`),
  CONSTRAINT `FK_khv8wjj9nfi9s8mbuhus9mkx1` FOREIGN KEY (`testCases_id`) REFERENCES `testcase` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `executionlist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `command_index` int(11) DEFAULT NULL,
  `custom_parameters` varchar(255) DEFAULT NULL,
  `error_message` longtext,
  `parent` bigint(20) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `when_complete` datetime DEFAULT NULL,
  `when_created` datetime DEFAULT NULL,
  `when_modified` datetime DEFAULT NULL,
  `schedule_id` bigint(20) DEFAULT NULL,
  `test_case_id` bigint(20) DEFAULT NULL,
  `test_suite_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_98bpq6qmct3jayemeosoaj0aq` (`schedule_id`),
  KEY `FK_1ceby5rfrh90h7ad6qy91816r` (`test_case_id`),
  KEY `FK_67rpiq0hiyalosy2pnjnke7on` (`test_suite_id`),
  CONSTRAINT `FK_1ceby5rfrh90h7ad6qy91816r` FOREIGN KEY (`test_case_id`) REFERENCES `testcase` (`id`),
  CONSTRAINT `FK_67rpiq0hiyalosy2pnjnke7on` FOREIGN KEY (`test_suite_id`) REFERENCES `testsuite` (`id`),
  CONSTRAINT `FK_98bpq6qmct3jayemeosoaj0aq` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `testcaseexecution` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `command` varchar(255) DEFAULT NULL,
  `comment` longtext,
  `error_message` longtext,
  `screenshot` longtext,
  `status` varchar(255) DEFAULT NULL,
  `time_taken` varchar(255) DEFAULT NULL,
  `when_created` datetime DEFAULT NULL,
  `execution_list_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_eooe2npqgiturqfo3jw3yw8k4` (`execution_list_id`),
  CONSTRAINT `FK_eooe2npqgiturqfo3jw3yw8k4` FOREIGN KEY (`execution_list_id`) REFERENCES `executionlist` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table automation.testsuite_testsuite
CREATE TABLE IF NOT EXISTS `testsuite_testsuite` (
  `TestSuite_id` bigint(20) NOT NULL,
  `testSuites_id` bigint(20) NOT NULL,
  PRIMARY KEY (`TestSuite_id`,`testSuites_id`),
  KEY `FK_n2hw4csv79j1c2rsawbt2e0wc` (`testSuites_id`),
  CONSTRAINT `FK_2949cb3uteh3g9t6xsdg49256` FOREIGN KEY (`TestSuite_id`) REFERENCES `testsuite` (`id`),
  CONSTRAINT `FK_n2hw4csv79j1c2rsawbt2e0wc` FOREIGN KEY (`testSuites_id`) REFERENCES `testsuite` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `executionlist_params` (
  `executionlist_id` bigint(20) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`executionlist_id`,`name`),
  CONSTRAINT `FK_pyru2bnj0fw5nvb2g4u027v4k` FOREIGN KEY (`executionlist_id`) REFERENCES `executionlist` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `userType` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `environment_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_exewsri04d0lko8lytlgydrm3` (`username`,`environment_id`),
  KEY `FK_323hgwpib3dqd3b0ylp5qpgpk` (`environment_id`),
  CONSTRAINT `FK_323hgwpib3dqd3b0ylp5qpgpk` FOREIGN KEY (`environment_id`) REFERENCES `environment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

