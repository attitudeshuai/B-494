CREATE DATABASE IF NOT EXISTS lost_and_found DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE lost_and_found;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 1. 用户表
CREATE TABLE IF NOT EXISTS `users` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `student_id` VARCHAR(50) NOT NULL UNIQUE COMMENT '学号',
  `email` VARCHAR(100) COMMENT '邮箱',
  `phone` VARCHAR(20) COMMENT '手机号',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `avatar` VARCHAR(255) COMMENT '头像路径',
  `address` VARCHAR(255) COMMENT '地址',
  `status` INT DEFAULT 1 COMMENT '状态 1:正常 0:禁用',
  `create_time` BIGINT COMMENT '注册时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 管理员表
CREATE TABLE IF NOT EXISTS `admins` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '管理员ID',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `account` VARCHAR(50) NOT NULL UNIQUE COMMENT '登录账号',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `create_time` BIGINT COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

INSERT INTO `admins` (`name`, `account`, `password`, `create_time`) VALUES ('Admin', 'admin', '123456', 1705660000000);

-- 3. 失物信息表
CREATE TABLE IF NOT EXISTS `lost_items` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '物品ID',
  `name` VARCHAR(100) NOT NULL COMMENT '物品名称',
  `place` VARCHAR(100) NOT NULL COMMENT '丢失地点',
  `lost_time` BIGINT NOT NULL COMMENT '丢失时间',
  `description` TEXT COMMENT '描述',
  `image_path` VARCHAR(255) COMMENT '图片路径',
  `user_id` INT NOT NULL COMMENT '发布用户ID',
  `status` INT DEFAULT 0 COMMENT '状态 0:待审核 1:已通过 2:未通过',
  `create_time` BIGINT COMMENT '发布时间',
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='失物信息表';

-- 4. 招领信息表
CREATE TABLE IF NOT EXISTS `found_items` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '物品ID',
  `name` VARCHAR(100) NOT NULL COMMENT '物品名称',
  `place` VARCHAR(100) NOT NULL COMMENT '捡到地点',
  `found_time` BIGINT NOT NULL COMMENT '捡到时间',
  `description` TEXT COMMENT '描述',
  `image_path` VARCHAR(255) COMMENT '图片路径',
  `user_id` INT NOT NULL COMMENT '发布用户ID',
  `status` INT DEFAULT 0 COMMENT '状态 0:待审核 1:已通过 2:未通过',
  `create_time` BIGINT COMMENT '发布时间',
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='招领信息表';

-- 5. 留言表
CREATE TABLE IF NOT EXISTS `comments` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '留言ID',
  `user_id` INT NOT NULL COMMENT '留言用户ID',
  `item_type` TINYINT NOT NULL COMMENT '物品类型 1:失物 2:招领',
  `item_id` INT NOT NULL COMMENT '信息ID',
  `content` TEXT NOT NULL COMMENT '留言内容',
  `create_time` BIGINT COMMENT '留言时间',
  `status` INT DEFAULT 0 COMMENT '状态 0:待审核 1:已发布',
  `parent_id` INT DEFAULT NULL COMMENT '父留言ID',
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='留言表';

-- 6. 私信表
CREATE TABLE IF NOT EXISTS `messages` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '私信ID',
  `sender_id` INT NOT NULL COMMENT '发送者ID',
  `receiver_id` INT NOT NULL COMMENT '接收者ID',
  `content` TEXT NOT NULL COMMENT '私信内容',
  `send_time` BIGINT COMMENT '发送时间',
  FOREIGN KEY (`sender_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`receiver_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='私信表';

-- 7. 公告表
CREATE TABLE IF NOT EXISTS `announcements` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '公告ID',
  `title` VARCHAR(255) NOT NULL COMMENT '标题',
  `content` TEXT NOT NULL COMMENT '内容',
  `create_time` BIGINT COMMENT '创建时间',
  `update_time` BIGINT COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- Data Insertion

-- 1. Reset Data (Optional, but good for re-init)
-- TRUNCATE TABLE `messages`;
-- TRUNCATE TABLE `comments`;
-- TRUNCATE TABLE `lost_items`;
-- TRUNCATE TABLE `found_items`;
-- TRUNCATE TABLE `users`;
-- TRUNCATE TABLE `admins`;

-- 2. Insert Admins (Already inserted above in table creation, but let's make sure id=1 is consistent if we want specific IDs)
-- The INSERT above was: INSERT INTO `admins` (`name`, `account`, `password`, `create_time`) VALUES ('Admin', 'admin', '123456', 1705660000000);
-- The data.sql had: INSERT INTO `admins` (`id`, `name`, `account`, `password`, `create_time`) VALUES (1, '超级管理员', 'admin', '123456', 1704067200000);
-- I'll use the data.sql version as it specifies ID 1 which might be important.
TRUNCATE TABLE `admins`;
INSERT INTO `admins` (`id`, `name`, `account`, `password`, `create_time`) VALUES
(1, '超级管理员', 'admin', '123456', 1704067200000);

-- 3. Insert Users
INSERT INTO `users` (`id`, `name`, `student_id`, `email`, `phone`, `password`, `avatar`, `address`, `status`, `create_time`) VALUES
(1, '张三', '2021001', 'zhangsan@school.edu.cn', '13800138001', '123456', 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=100&q=80', '南苑3号楼', 1, 1704067200000),
(2, '李四', '2021002', 'lisi@school.edu.cn', '13900139002', '123456', 'https://images.unsplash.com/photo-1527980965255-d3b416303d12?auto=format&fit=crop&w=100&q=80', '北苑1号楼', 1, 1704153600000),
(3, '王五', '2021003', 'wangwu@school.edu.cn', '13700137003', '123456', 'https://images.unsplash.com/photo-1599566150163-29194dcaad36?auto=format&fit=crop&w=100&q=80', '西苑5号楼', 1, 1704240000000);

-- 4. Insert Lost Items (Real Images)
INSERT INTO `lost_items` (`id`, `name`, `place`, `lost_time`, `description`, `image_path`, `user_id`, `status`, `create_time`) VALUES
(1, '黑色双肩包', '第二教学楼301教室', 1709251200000, '黑色耐克双肩包，里面有高数课本和一把雨伞。', 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?auto=format&fit=crop&w=800&q=80', 1, 1, 1709254800000),
(2, '苹果 iPhone 13', '食堂二楼', 1709337600000, '白色 iPhone 13，带透明手机壳，锁屏壁纸是星空。', 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?auto=format&fit=crop&w=800&q=80', 2, 0, 1709341200000),
(3, '一串钥匙', '图书馆门口', 1709424000000, '大概有4把钥匙，挂着一个皮卡丘挂件。', 'https://images.unsplash.com/photo-1582139329536-e7284fece509?auto=format&fit=crop&w=800&q=80', 3, 1, 1709427600000),
(4, '索尼蓝牙耳机', '操场看台', 1709510400000, '黑色 Sony WH-1000XM4，在跑步时遗落在看台上。', 'https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?auto=format&fit=crop&w=800&q=80', 1, 1, 1709514000000),
(5, '高数教材', '4号楼自习室', 1709596800000, '高等数学（下册），封面写着名字。', 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&w=800&q=80', 2, 2, 1709600400000);

-- 5. Insert Found Items (Real Images)
INSERT INTO `found_items` (`id`, `name`, `place`, `found_time`, `description`, `image_path`, `user_id`, `status`, `create_time`) VALUES
(1, '透明雨伞', '综合楼大厅', 1709683200000, '一把透明长柄雨伞，放在大厅角落。', 'https://images.unsplash.com/photo-1516055209355-6c73801f9b33?auto=format&fit=crop&w=800&q=80', 2, 1, 1709686800000),
(2, '蓝色保温杯', '篮球场', 1709769600000, '膳魔师保温杯，深蓝色，有点掉漆。', 'https://images.unsplash.com/photo-1602143407151-0111d25649e8?auto=format&fit=crop&w=800&q=80', 3, 0, 1709773200000),
(3, '校园卡', '北苑食堂', 1709856000000, '捡到一张校园卡，姓名：赵六，学号：2021004。', 'https://images.unsplash.com/photo-1627123424574-181ce5171c98?auto=format&fit=crop&w=800&q=80', 1, 1, 1709859600000),
(4, '拍立得相机', '草坪音乐节现场', 1709942400000, '粉色富士拍立得，希望能找到失主。', 'https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?auto=format&fit=crop&w=800&q=80', 2, 1, 1709946000000),
(5, '棕色笔记本', '3102教室', 1710028800000, '皮质封面笔记本，记录了很多笔记。', 'https://images.unsplash.com/photo-1544816155-12df9643f363?auto=format&fit=crop&w=800&q=80', 3, 1, 1710032400000);

-- 6. Insert Comments
INSERT INTO `comments` (`user_id`, `item_type`, `item_id`, `content`, `create_time`) VALUES
(2, 1, 1, '同学，我在教务处看到类似的包，你可以去问问。', 1709258400000),
(3, 1, 1, '希望能早日找回！', 1709262000000),
(1, 2, 1, '这是我丢的雨伞！太感谢了，怎么联系你？', 1709690400000);

-- 7. Insert Messages
INSERT INTO `messages` (`sender_id`, `receiver_id`, `content`, `send_time`) VALUES
(1, 2, '你好，我看到了你发布的雨伞招领信息，是我的。', 1709691000000),
(2, 1, '好的，你在哪里？我给你送过去。', 1709691600000);

SET FOREIGN_KEY_CHECKS = 1;
