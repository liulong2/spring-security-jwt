SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for check_user
-- ----------------------------
DROP TABLE IF EXISTS `check_user`;
CREATE TABLE `check_user` (
  `user_id` varchar(32) NOT NULL COMMENT '用户id',
  `user_name` varchar(64) DEFAULT NULL COMMENT '用户名称',
  `password` varchar(256) DEFAULT NULL COMMENT '密码',
  `role` varchar(32) DEFAULT NULL COMMENT '权限',
  `data_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '数据状态 0为正常1为删除',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for check_role
-- ----------------------------
DROP TABLE IF EXISTS `check_role`;
CREATE TABLE `check_role` (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限id',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '权限名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

