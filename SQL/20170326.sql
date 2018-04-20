--序列
DROP TABLE IF EXISTS `sequence`;
CREATE TABLE `sequence` (
  `seq_name` varchar(255) NOT NULL,
  `seq_count` decimal(10,0) NOT NULL,
  PRIMARY KEY (`seq_name`)
);

-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nick_name` varchar(255) NOT NULL,
  `mobile` varchar(255) NOT NULL,
  `address` varchar(225) DEFAULT NULL,
  `sex` varchar(10) DEFAULT NULL,
  `head_id` bigint(20) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `delete_status` bit(1) DEFAULT b'0',
  `create_time` datetime NOT NULL,
  `create_user_id` bigint(20) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 用户登录退出日志表
DROP TABLE IF EXISTS `user_login_log`;
CREATE TABLE `user_login_log` (
  `id` bigint(20) NOT NULL,
  `session_id` varchar(255) NOT NULL,
  `ip` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `agent` longtext NOT NULL,
  `status` int(2) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `delete_status` bit(1) DEFAULT b'0',
  `create_time` datetime NOT NULL,
  `create_user_id` bigint(20) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--好友表
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `friend_id` bigint(20) NOT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `friend_name` varchar(255) DEFAULT NULL,
  `agree` int(2) DEFAULT '0',
  `delete_status` bit(1) DEFAULT b'0',
  `create_time` datetime NOT NULL,
  `create_user_id` bigint(20) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--系统图片表
DROP TABLE IF EXISTS `accessory`;
CREATE TABLE `accessory` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `path` varchar(255) NOT NULL,
  `size` float DEFAULT NULL,
  `width` int(4) DEFAULT NULL,
  `height` int(4) DEFAULT NULL,
  `ext` varchar(255) DEFAULT NULL,
  `info` varchar(255) DEFAULT NULL,
  `delete_status` bit(1) DEFAULT b'0',
  `create_time` datetime NOT NULL,
  `create_user_id` bigint(20) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--消息表
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `friend_id` bigint(20) NOT NULL,
  `mark` varchar(50) NOT NULL,
  `delete_status` bit(1) NOT NULL DEFAULT b'0',
  `create_time` datetime NOT NULL,
  `create_user_id` bigint(20) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mark` (`mark`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--消息明细表
DROP TABLE IF EXISTS `message_info`;
CREATE TABLE `message_info` (
  `id` bigint(20) NOT NULL,
  `message_id` bigint(20) NOT NULL,
  `content` varchar(255) NOT NULL,
  `message_status` int(2) NOT NULL,
  `read_status` bit(1) NOT NULL DEFAULT b'0',
  `delete_status` bit(1) DEFAULT b'0',
  `create_time` datetime NOT NULL,
  `create_user_id` bigint(20) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

