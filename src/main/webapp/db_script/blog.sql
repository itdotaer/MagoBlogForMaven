CREATE TABLE IF NOT EXISTS user_base(userId int PRIMARY KEY AUTO_INCREMENT, userName VARCHAR(50) UNIQUE NOT NULL, userMail VARCHAR(50), userPassword VARCHAR(20) );
CREATE TABLE IF NOT EXISTS user_ext(id int PRIMARY KEY  AUTO_INCREMENT, userId int, sex int, fullName VARCHAR(10),description VARCHAR(512), createdBy int, createDate timestamp, lastUpdatedBy int, lastUpdateDate timestamp);
CREATE TABLE IF NOT EXISTS classe(classId int PRIMARY KEY AUTO_INCREMENT, className VARCHAR(50) UNIQUE NOT NULL, productId int, parentId int, createdBy int, createDate timestamp, lastUpdatedBy int, lastUpdateDate timestamp);
CREATE TABLE IF NOT EXISTS product(productId int PRIMARY KEY AUTO_INCREMENT, productName VARCHAR(50) UNIQUE NOT NULL, productDesc VARCHAR(256), createdBy int, createDate timestamp, lastUpdatedBy int, lastUpdateDate timestamp);
CREATE TABLE IF NOT EXISTS article_base(articleId int PRIMARY KEY AUTO_INCREMENT, articleName VARCHAR(128) NOT NULL, createdBy int, createDate timestamp, lastUpdatedBy int, lastUpdateDate timestamp, isPublish int, publishedBy int, publishDate timestamp, classId int, viewNum int, isMainService int, isAds int, filePath VARCHAR(128));
CREATE TABLE IF NOT EXISTS article_ext(id int PRIMARY KEY AUTO_INCREMENT, articleId int, articleDescription VARCHAR(512),articleContent TEXT);

INSERT INTO user_base(userName,userMail,userPassword) VALUES('hujiangtao','hujiangtao1235@qq.com','123456');

INSERT INTO user_ext(userId,sex,fullName,description,createdBy,createDate,lastUpdatedBy,lastUpdateDate) VALUES(1,0,'管理员','屌丝程序猿',1,NOW(),null, null);

INSERT INTO classe(className,productId,parentId,createdBy,createDate,lastUpdatedBy,lastUpdateDate) VALUES('博客分享',0,0,1,NOW(),null,null);
INSERT INTO classe(className,productId,parentId,createdBy,createDate,lastUpdatedBy,lastUpdateDate) VALUES('生活杂记',0,0,1,NOW(),null,null);
INSERT INTO classe(className,productId,parentId,createdBy,createDate,lastUpdatedBy,lastUpdateDate) VALUES('美图',0,0,1,NOW(),null,null);
INSERT INTO classe(className,productId,parentId,createdBy,createDate,lastUpdatedBy,lastUpdateDate) VALUES('视频分享',0,0,1,NOW(),null,null);
INSERT INTO classe(className,productId,parentId,createdBy,createDate,lastUpdatedBy,lastUpdateDate) VALUES('关于我',0,0,1,NOW(),null,null);

INSERT INTO classe(className,productId,parentId,createdBy,createDate,lastUpdatedBy,lastUpdateDate) VALUES('技术博客',0,1,1,NOW(),null,null);
INSERT INTO classe(className,productId,parentId,createdBy,createDate,lastUpdatedBy,lastUpdateDate) VALUES('吃货天地',0,2,1,NOW(),null,null);
INSERT INTO classe(className,productId,parentId,createdBy,createDate,lastUpdatedBy,lastUpdateDate) VALUES('旅游分享',0,3,1,NOW(),null,null);
INSERT INTO classe(className,productId,parentId,createdBy,createDate,lastUpdatedBy,lastUpdateDate) VALUES('Dota视频',0,4,1,NOW(),null,null);
INSERT INTO classe(className,productId,parentId,createdBy,createDate,lastUpdatedBy,lastUpdateDate) VALUES('程序猿',0,5,1,NOW(),null,null);

INSERT INTO article_base(articleName,createdBy,createDate,isPublish,publishedBy,publishDate,classId,viewNum,isMainService,isAds)VALUES('测试文章1',1,NOW(),1,1,NOW(),6,100,1,1);
INSERT INTO article_base(articleName,createdBy,createDate,isPublish,publishedBy,publishDate,classId,viewNum,isMainService,isAds)VALUES('测试文章2',1,NOW(),1,1,NOW(),7,100,1,1);
INSERT INTO article_base(articleName,createdBy,createDate,isPublish,publishedBy,publishDate,classId,viewNum,isMainService,isAds,filePath)VALUES('测试文章3',1,NOW(),1,1,NOW(),8,100,1,1,'/upload/images/dog.jpg');
INSERT INTO article_base(articleName,createdBy,createDate,isPublish,publishedBy,publishDate,classId,viewNum,isMainService,isAds)VALUES('测试文章4',1,NOW(),1,1,NOW(),9,100,1,1);
INSERT INTO article_base(articleName,createdBy,createDate,isPublish,publishedBy,publishDate,classId,viewNum,isMainService,isAds)VALUES('测试文章5',1,NOW(),1,1,NOW(),10,100,1,1);
INSERT INTO article_base(articleName,createdBy,createDate,isPublish,publishedBy,publishDate,classId,viewNum,isMainService,isAds)VALUES('测试文章6',1,NOW(),1,1,NOW(),6,100,1,1);
INSERT INTO article_base(articleName,createdBy,createDate,isPublish,publishedBy,publishDate,classId,viewNum,isMainService,isAds)VALUES('测试文章7',1,NOW(),1,1,NOW(),7,100,1,1);
INSERT INTO article_base(articleName,createdBy,createDate,isPublish,publishedBy,publishDate,classId,viewNum,isMainService,isAds,filePath)VALUES('测试文章8',1,NOW(),1,1,NOW(),8,100,1,1,'/upload/images/dog.jpg');
INSERT INTO article_base(articleName,createdBy,createDate,isPublish,publishedBy,publishDate,classId,viewNum,isMainService,isAds)VALUES('测试文章9',1,NOW(),1,1,NOW(),9,100,1,1);
INSERT INTO article_base(articleName,createdBy,createDate,isPublish,publishedBy,publishDate,classId,viewNum,isMainService,isAds)VALUES('测试文章10',1,NOW(),1,1,NOW(),10,100,1,1);

INSERT INTO article_ext(articleId,articleDescription,articleContent) VALUES(1,'文章描述一','这里是测试文章');
INSERT INTO article_ext(articleId,articleDescription,articleContent) VALUES(2,'文章描述二','这里是测试文章');
INSERT INTO article_ext(articleId,articleDescription,articleContent) VALUES(3,'文章描述三','这里是测试文章');
INSERT INTO article_ext(articleId,articleDescription,articleContent) VALUES(4,'文章描述四','这里是测试文章');
INSERT INTO article_ext(articleId,articleDescription,articleContent) VALUES(5,'文章描述五','这里是测试文章');
INSERT INTO article_ext(articleId,articleDescription,articleContent) VALUES(6,'文章描述一','这里是测试文章');
INSERT INTO article_ext(articleId,articleDescription,articleContent) VALUES(7,'文章描述一','这里是测试文章');
INSERT INTO article_ext(articleId,articleDescription,articleContent) VALUES(8,'文章描述一','这里是测试文章');
INSERT INTO article_ext(articleId,articleDescription,articleContent) VALUES(9,'文章描述一','这里是测试文章');
INSERT INTO article_ext(articleId,articleDescription,articleContent) VALUES(10,'文章描述一','这里是测试文章');
