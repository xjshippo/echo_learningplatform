-- ========================================
-- echo_learning_platform 数据库初始化脚本
-- 数据库全部使用 utf8mb4 以支持表情符号
-- ========================================

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `rc_learn`    DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `rc_personal` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `rc_question` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `rc_comment`  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `rc_message`  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `rc_manage`   DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- ==========================================
-- rc_learn — 学习中心
-- ==========================================
USE `rc_learn`;

CREATE TABLE IF NOT EXISTS `invitation_table` (
  `id`          VARCHAR(64) NOT NULL COMMENT '文章ID',
  `title`       VARCHAR(255) DEFAULT NULL COMMENT '标题',
  `context`     LONGTEXT COMMENT '内容',
  `userId`      VARCHAR(64)  DEFAULT NULL COMMENT '作者ID',
  `type`        VARCHAR(64)  DEFAULT NULL COMMENT '标签/分类',
  `state`       INT          DEFAULT 0 COMMENT '状态: 0=待审核, 1=已发布, 2=已删除',
  `likenumber`  INT          DEFAULT 0 COMMENT '点赞数',
  `visitnumber` INT          DEFAULT 0 COMMENT '访问数',
  `commentnumber` INT        DEFAULT 0 COMMENT '评论数',
  `creat_time`  DATETIME     DEFAULT NULL COMMENT '创建时间',
  `coverImgUrl` VARCHAR(512) DEFAULT NULL COMMENT '封面图URL',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`),
  KEY `idx_state` (`state`),
  KEY `idx_create_time` (`creat_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

CREATE TABLE IF NOT EXISTS `incitaion_imag_table` (
  `id`          VARCHAR(64) NOT NULL COMMENT '图片ID',
  `iid`         VARCHAR(64) DEFAULT NULL COMMENT '关联文章ID',
  `url`         VARCHAR(512) DEFAULT NULL COMMENT '图片URL',
  `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_iid` (`iid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章图片表';

CREATE TABLE IF NOT EXISTS `like_user_invitation_table` (
  `id`  VARCHAR(64) NOT NULL COMMENT 'ID',
  `iid` VARCHAR(64) DEFAULT NULL COMMENT '文章ID',
  `uid` VARCHAR(64) DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`),
  KEY `idx_iid_uid` (`iid`, `uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章点赞表';

CREATE TABLE IF NOT EXISTS `comment_table` (
  `id`            VARCHAR(64) NOT NULL COMMENT '评论ID',
  `parentId`      VARCHAR(64) DEFAULT NULL COMMENT '父评论ID',
  `invitationId`  VARCHAR(64) DEFAULT NULL COMMENT '关联文章ID',
  `context`       TEXT COMMENT '评论内容',
  `likenumber`    INT         DEFAULT 0 COMMENT '点赞数',
  `commentnumber` INT         DEFAULT 0 COMMENT '回复数',
  `uid`           VARCHAR(64) DEFAULT NULL COMMENT '评论用户ID',
  `create_time`   DATETIME    DEFAULT NULL COMMENT '创建时间',
  `state`         INT         DEFAULT 0 COMMENT '状态',
  PRIMARY KEY (`id`),
  KEY `idx_invitationId` (`invitationId`),
  KEY `idx_parentId` (`parentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

CREATE TABLE IF NOT EXISTS `invitation_comment_table` (
  `id`  VARCHAR(64) NOT NULL,
  `iid` VARCHAR(64) DEFAULT NULL COMMENT '文章ID',
  `cid` VARCHAR(64) DEFAULT NULL COMMENT '评论ID',
  PRIMARY KEY (`id`),
  KEY `idx_iid` (`iid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章-评论关联表';

CREATE TABLE IF NOT EXISTS `like_user_comment_table` (
  `id`  VARCHAR(64) NOT NULL,
  `cid` VARCHAR(64) DEFAULT NULL COMMENT '评论ID',
  `uid` VARCHAR(64) DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`),
  KEY `idx_cid_uid` (`cid`, `uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论点赞表';

CREATE TABLE IF NOT EXISTS `resource_table` (
  `id`            VARCHAR(64) NOT NULL COMMENT '资源ID',
  `content`       VARCHAR(255) DEFAULT NULL COMMENT '资源名称/描述',
  `uid`           VARCHAR(64)  DEFAULT NULL COMMENT '上传用户ID',
  `type`          VARCHAR(64)  DEFAULT NULL COMMENT '资源类型',
  `tags`          VARCHAR(255) DEFAULT NULL COMMENT '标签',
  `url`           VARCHAR(512) DEFAULT NULL COMMENT '资源URL',
  `imageurl`      VARCHAR(512) DEFAULT NULL COMMENT '缩略图URL',
  `create_time`   DATETIME     DEFAULT NULL COMMENT '创建时间',
  `visitednumber` INT          DEFAULT 0 COMMENT '访问数',
  `likenumber`    INT          DEFAULT 0 COMMENT '点赞数',
  `state`         INT          DEFAULT 1 COMMENT '状态: 0=待审核, 1=已发布, 2=已删除',
  `commentnumber` INT          DEFAULT 0 COMMENT '评论数',
  `size`          VARCHAR(64)  DEFAULT NULL COMMENT '文件大小',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习资源表';

CREATE TABLE IF NOT EXISTS `video_table` (
  `id`        VARCHAR(64) NOT NULL COMMENT '视频ID',
  `content`   VARCHAR(255) DEFAULT NULL COMMENT '视频名称',
  `url`       VARCHAR(512) DEFAULT NULL COMMENT '视频URL(阿里云VOD)',
  `imageUrl`  VARCHAR(512) DEFAULT NULL COMMENT '封面图URL',
  `userId`    VARCHAR(64)  DEFAULT NULL COMMENT '上传用户ID',
  `category`  VARCHAR(64)  DEFAULT NULL COMMENT '分类',
  `state`     INT          DEFAULT 0 COMMENT '状态: 0=待审核, 1=已发布',
  `create_time` DATETIME   DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频表';

CREATE TABLE IF NOT EXISTS `video_user_table` (
  `id`  VARCHAR(64) NOT NULL,
  `uid` VARCHAR(64) DEFAULT NULL COMMENT '用户ID',
  `vid` VARCHAR(64) DEFAULT NULL COMMENT '视频ID',
  PRIMARY KEY (`id`),
  KEY `idx_uid` (`uid`),
  KEY `idx_vid` (`vid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频观看记录表';

CREATE TABLE IF NOT EXISTS `like_user_resource_table` (
  `id`  VARCHAR(64) NOT NULL,
  `rid` VARCHAR(64) DEFAULT NULL COMMENT '资源ID',
  `uid` VARCHAR(64) DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`),
  KEY `idx_rid_uid` (`rid`, `uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源点赞表';

-- ==========================================
-- rc_personal — 用户中心
-- ==========================================
USE `rc_personal`;

CREATE TABLE IF NOT EXISTS `user_table` (
  `id`        VARCHAR(64) NOT NULL COMMENT '用户ID',
  `login_act` VARCHAR(64)  DEFAULT NULL COMMENT '登录账号',
  `pass_word` VARCHAR(255) DEFAULT NULL COMMENT '密码(MD5)',
  `nick_name` VARCHAR(64)  DEFAULT NULL COMMENT '昵称',
  `nickName`  VARCHAR(64)  DEFAULT NULL COMMENT '昵称(冗余)',
  `state`     INT          DEFAULT 0 COMMENT '状态: 0=待审核, 1=正常',
  `image_url` VARCHAR(512) DEFAULT NULL COMMENT '头像URL',
  `phone`     VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
  `open_id`   VARCHAR(128) DEFAULT NULL COMMENT '微信openId',
  `last_time` DATETIME     DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_open_id` (`open_id`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_nick_name` (`nick_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `basic_info_table` (
  `id`            VARCHAR(64) NOT NULL COMMENT 'ID',
  `userId`        VARCHAR(64)  DEFAULT NULL COMMENT '用户ID',
  `sex`           VARCHAR(10)  DEFAULT NULL COMMENT '性别',
  `address`       VARCHAR(255) DEFAULT NULL COMMENT '地址',
  `phone`         VARCHAR(20)  DEFAULT NULL COMMENT '电话',
  `QQ`            VARCHAR(20)  DEFAULT NULL COMMENT 'QQ号',
  `email`         VARCHAR(64)  DEFAULT NULL COMMENT '邮箱',
  `professional`  VARCHAR(64)  DEFAULT NULL COMMENT '专业',
  `medals`        VARCHAR(255) DEFAULT NULL COMMENT '勋章',
  `skills_tree_id` VARCHAR(64) DEFAULT NULL COMMENT '技能树ID',
  `messageId`     VARCHAR(64)  DEFAULT NULL COMMENT '消息ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户详细信息表';

CREATE TABLE IF NOT EXISTS `favorites_table` (
  `id`         VARCHAR(64) NOT NULL COMMENT '收藏夹ID',
  `userId`     VARCHAR(64) DEFAULT NULL COMMENT '用户ID',
  `name`       VARCHAR(64) DEFAULT NULL COMMENT '收藏夹名称',
  `createTime` DATETIME    DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏夹表';

CREATE TABLE IF NOT EXISTS `favorites_invitation_table` (
  `favoritesId`  VARCHAR(64) NOT NULL COMMENT '收藏夹ID',
  `invitationId` VARCHAR(64) NOT NULL COMMENT '文章ID',
  `userId`       VARCHAR(64) DEFAULT NULL COMMENT '用户ID',
  `createTime`   DATETIME    DEFAULT NULL COMMENT '收藏时间',
  PRIMARY KEY (`favoritesId`, `invitationId`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏夹-文章关联表';

CREATE TABLE IF NOT EXISTS `history_view_table` (
  `id`         VARCHAR(64) NOT NULL,
  `viewId`     VARCHAR(64) DEFAULT NULL COMMENT '浏览对象ID(文章ID)',
  `createTime` DATETIME    DEFAULT NULL COMMENT '浏览时间',
  `userId`     VARCHAR(64) DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`),
  KEY `idx_viewId` (`viewId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='浏览历史表';

CREATE TABLE IF NOT EXISTS `professional_table` (
  `content` VARCHAR(64) DEFAULT NULL COMMENT '专业名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专业分类表';

CREATE TABLE IF NOT EXISTS `skill_table` (
  `id`      VARCHAR(64) NOT NULL,
  `content` VARCHAR(64) DEFAULT NULL COMMENT '技能名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='技能表';

-- ==========================================
-- rc_question — 题库
-- ==========================================
USE `rc_question`;

CREATE TABLE IF NOT EXISTS `multiple_choice_table` (
  `id`            VARCHAR(64) NOT NULL COMMENT '题目ID',
  `describe`      TEXT COMMENT '题目描述/题干',
  `op_one`        VARCHAR(255) DEFAULT NULL COMMENT '选项A',
  `op_two`        VARCHAR(255) DEFAULT NULL COMMENT '选项B',
  `op_three`      VARCHAR(255) DEFAULT NULL COMMENT '选项C',
  `op_four`       VARCHAR(255) DEFAULT NULL COMMENT '选项D',
  `op_answer`     VARCHAR(64)  DEFAULT NULL COMMENT '正确答案',
  `source`        VARCHAR(255) DEFAULT NULL COMMENT '题目来源',
  `analysis`      TEXT COMMENT '解析',
  `tags`          VARCHAR(255) DEFAULT NULL COMMENT '标签',
  `question_type` INT          DEFAULT 0 COMMENT '题目类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='选择题表';

CREATE TABLE IF NOT EXISTS `problemsets_table` (
  `id`          VARCHAR(64) NOT NULL COMMENT '试卷ID',
  `skillid`     VARCHAR(64)  DEFAULT NULL COMMENT '关联技能ID',
  `passrate`    INT          DEFAULT 0 COMMENT '通过率',
  `number`      INT          DEFAULT 0 COMMENT '题目数量',
  `passnumbers` INT          DEFAULT 0 COMMENT '通过人数',
  `createtime`  DATETIME     DEFAULT NULL COMMENT '创建时间',
  `level`       INT          DEFAULT 0 COMMENT '难度等级',
  `name`        VARCHAR(255) DEFAULT NULL COMMENT '试卷名称',
  PRIMARY KEY (`id`),
  KEY `idx_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试卷表';

CREATE TABLE IF NOT EXISTS `problemsets_question_table` (
  `id`   VARCHAR(64) NOT NULL,
  `pid`  VARCHAR(64) DEFAULT NULL COMMENT '试卷ID',
  `qid`  VARCHAR(64) DEFAULT NULL COMMENT '题目ID',
  `type` INT         DEFAULT 0 COMMENT '题目类型',
  PRIMARY KEY (`id`),
  KEY `idx_pid` (`pid`),
  KEY `idx_qid` (`qid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试卷-题目关联表';

CREATE TABLE IF NOT EXISTS `problemsets_user_table` (
  `id`         VARCHAR(64) NOT NULL,
  `pid`        VARCHAR(64) DEFAULT NULL COMMENT '试卷ID',
  `userid`     VARCHAR(64) DEFAULT NULL COMMENT '用户ID',
  `createtime` DATETIME    DEFAULT NULL COMMENT '作答时间',
  `passRate`   INT         DEFAULT 0 COMMENT '得分/通过率',
  PRIMARY KEY (`id`),
  KEY `idx_pid_uid` (`pid`, `userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户答题记录表';

CREATE TABLE IF NOT EXISTS `mistake_table` (
  `id`           VARCHAR(64) NOT NULL COMMENT '错题记录ID',
  `questionid`   VARCHAR(64)  DEFAULT NULL COMMENT '题目ID',
  `create_time`  DATETIME     DEFAULT NULL COMMENT '创建时间',
  `error_choice` VARCHAR(64)  DEFAULT NULL COMMENT '错误答案',
  `type`         INT          DEFAULT 0 COMMENT '题目类型',
  `analysis`     TEXT COMMENT '解析',
  `right_choice` VARCHAR(64)  DEFAULT NULL COMMENT '正确答案',
  PRIMARY KEY (`id`),
  KEY `idx_questionid` (`questionid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='错题表';

CREATE TABLE IF NOT EXISTS `mistake_user_table` (
  `id`        VARCHAR(64) NOT NULL,
  `userId`    VARCHAR(64) DEFAULT NULL COMMENT '用户ID',
  `mistakeId` VARCHAR(64) DEFAULT NULL COMMENT '错题记录ID',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-错题关联表';

CREATE TABLE IF NOT EXISTS `skill_problemset_table` (
  `skiiId`       VARCHAR(64) NOT NULL COMMENT '技能ID',
  `problemsetId` VARCHAR(64) NOT NULL COMMENT '试卷ID',
  PRIMARY KEY (`skiiId`, `problemsetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='技能-试卷关联表';

-- ==========================================
-- rc_comment — 讨论区
-- ==========================================
USE `rc_comment`;

CREATE TABLE IF NOT EXISTS `discuss_community_table` (
  `id`           VARCHAR(64) NOT NULL COMMENT '讨论区ID',
  `discuss_name` VARCHAR(255) DEFAULT NULL COMMENT '讨论区名称',
  `tags`         VARCHAR(255) DEFAULT NULL COMMENT '标签',
  `create_time`  DATETIME     DEFAULT NULL COMMENT '创建时间',
  `image_url`    VARCHAR(512) DEFAULT NULL COMMENT '封面图',
  `number`       INT          DEFAULT 0 COMMENT '关注数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='讨论区表';

CREATE TABLE IF NOT EXISTS `discuss_comment_table` (
  `id`          VARCHAR(64) NOT NULL COMMENT '帖子ID',
  `title`       VARCHAR(255) DEFAULT NULL COMMENT '帖子标题',
  `content`     TEXT COMMENT '帖子内容',
  `discussid`   VARCHAR(64)  DEFAULT NULL COMMENT '所属讨论区ID',
  `userid`      VARCHAR(64)  DEFAULT NULL COMMENT '发帖用户ID',
  `parentid`    VARCHAR(64)  DEFAULT NULL COMMENT '父帖子ID(回复用)',
  `likenumber`  INT          DEFAULT 0 COMMENT '点赞数',
  `visitnumber` INT          DEFAULT 0 COMMENT '访问数',
  `create_time` DATETIME     DEFAULT NULL COMMENT '发帖时间',
  `state`       INT          DEFAULT 0 COMMENT '状态: 0=待审核, 1=已发布',
  `collectNum`  INT          DEFAULT 0 COMMENT '收藏数',
  PRIMARY KEY (`id`),
  KEY `idx_discussid` (`discussid`),
  KEY `idx_userid` (`userid`),
  KEY `idx_parentid` (`parentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='讨论帖子表';

CREATE TABLE IF NOT EXISTS `discuss_image_table` (
  `id`  VARCHAR(64) NOT NULL,
  `did` VARCHAR(64) DEFAULT NULL COMMENT '帖子ID',
  `url` VARCHAR(512) DEFAULT NULL COMMENT '图片URL',
  PRIMARY KEY (`id`),
  KEY `idx_did` (`did`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子图片表';

CREATE TABLE IF NOT EXISTS `discuss_community_user_table` (
  `id`  VARCHAR(64) NOT NULL,
  `uid` VARCHAR(64) DEFAULT NULL COMMENT '用户ID',
  `did` VARCHAR(64) DEFAULT NULL COMMENT '讨论区ID',
  PRIMARY KEY (`id`),
  KEY `idx_uid` (`uid`),
  KEY `idx_did` (`did`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户关注讨论区表';

CREATE TABLE IF NOT EXISTS `discuss_like_comment_table` (
  `id`   VARCHAR(64) NOT NULL,
  `dcid` VARCHAR(64) DEFAULT NULL COMMENT '帖子ID',
  `uid`  VARCHAR(64) DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`),
  KEY `idx_dcid_uid` (`dcid`, `uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子点赞表';

CREATE TABLE IF NOT EXISTS `type_table` (
  `id`   VARCHAR(64) NOT NULL,
  `type` VARCHAR(64) DEFAULT NULL COMMENT '分类名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='讨论区分类表';

-- ==========================================
-- rc_message — WebSocket消息
-- ==========================================
USE `rc_message`;

-- 消息表（根据WebSocket业务需求，存储历史消息）
CREATE TABLE IF NOT EXISTS `message_table` (
  `id`          VARCHAR(64) NOT NULL COMMENT '消息ID',
  `from_user`   VARCHAR(64)  DEFAULT NULL COMMENT '发送者',
  `to_user`     VARCHAR(64)  DEFAULT NULL COMMENT '接收者',
  `content`     TEXT COMMENT '消息内容',
  `create_time` DATETIME     DEFAULT NULL COMMENT '发送时间',
  `is_read`     INT          DEFAULT 0 COMMENT '是否已读: 0=未读, 1=已读',
  PRIMARY KEY (`id`),
  KEY `idx_from_to` (`from_user`, `to_user`),
  KEY `idx_to_user` (`to_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

-- ==========================================
-- rc_manage — 管理后台(日志)
-- ==========================================
USE `rc_manage`;

CREATE TABLE IF NOT EXISTS `log_table` (
  `id`         VARCHAR(64) NOT NULL COMMENT '日志ID',
  `ip`         VARCHAR(64)  DEFAULT NULL COMMENT '请求IP',
  `createTime` DATETIME     DEFAULT NULL COMMENT '操作时间',
  `userId`     VARCHAR(64)  DEFAULT NULL COMMENT '操作用户ID',
  `content`    TEXT COMMENT '操作内容',
  `userName`   VARCHAR(64)  DEFAULT NULL COMMENT '操作用户名',
  `type`       VARCHAR(64)  DEFAULT NULL COMMENT '操作类型',
  `code`       VARCHAR(64)  DEFAULT NULL COMMENT '状态码',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`),
  KEY `idx_createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';
