/*
MySQL Data Transfer
Source Host: localhost
Source Database: contactreport
Target Host: localhost
Target Database: contactreport
Date: 12/17/2010 12:56:43 PM
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for contactinformation
-- ----------------------------
CREATE TABLE `contactinformation` (
  `contactid` int(10) NOT NULL,
  `contactname` varchar(30) NOT NULL,
  `phone` varchar(15) default NULL,
  `address` varchar(150) default NULL,
  `email` varchar(30) default NULL,
  `groupname` varchar(20) default NULL,
  PRIMARY KEY  (`contactid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for tblgroup
-- ----------------------------
CREATE TABLE `tblgroup` (
  `groupid` int(10) NOT NULL,
  `groupname` varchar(20) NOT NULL,
  `description` varchar(150) default NULL,
  PRIMARY KEY  (`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for tbluser
-- ----------------------------
CREATE TABLE `tbluser` (
  `userid` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  PRIMARY KEY  (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `contactinformation` VALUES ('1', 'aaaaa', '33333', 'ddddd444h', 'aa@gmail.com', 'test group');
INSERT INTO `contactinformation` VALUES ('2', 'TEST123a', '23434254343', 'MMMMMMM', 'tt@d.vvv', 'test group');
INSERT INTO `contactinformation` VALUES ('3', 'fffffa', '122', 'dddddd', 'aa@dd.ccc', 'test group');
INSERT INTO `contactinformation` VALUES ('5', 'test2', '98787797', 'test123', 'test@gmail.com', 'GROUP123');
INSERT INTO `contactinformation` VALUES ('6', 'test2', '11111', 'gjhgjhg45', null, null);
INSERT INTO `contactinformation` VALUES ('7', 'Test Contact', '9874568974', 'XYZ \nKol: 700032', null, null);
INSERT INTO `contactinformation` VALUES ('8', 'Test12', '656665', 'hgggjgg', null, null);
INSERT INTO `contactinformation` VALUES ('9', 'test34', '46464664', 'hjkhkk', null, null);
INSERT INTO `contactinformation` VALUES ('10', 'hkkhkhk', '6465464', 'jhggjgjg', null, null);
INSERT INTO `contactinformation` VALUES ('11', 'uuuuuiuiu', '7787777', 'ikjkjj', null, null);
INSERT INTO `contactinformation` VALUES ('12', 'hjhjhhh', '545454', 'kjjjjj', null, null);
INSERT INTO `contactinformation` VALUES ('13', 'fgfggdg', '456466', 'dfgdgdg', null, null);
INSERT INTO `contactinformation` VALUES ('14', 'eetrterter', '4534535', 'sdfggg', null, null);
INSERT INTO `contactinformation` VALUES ('15', 'dfgdgdg', '65465646', 'dggdfgdfg', null, null);
INSERT INTO `contactinformation` VALUES ('16', 'rgfgfdgfdg', '455646', 'dfgfdgdgfdg', null, null);
INSERT INTO `contactinformation` VALUES ('17', 'dsfsfsf', 'w54355', 's45rrffs', null, null);
INSERT INTO `contactinformation` VALUES ('18', 'dfgdgdfg', '54355345', 'dfggfgfdg', null, null);
INSERT INTO `contactinformation` VALUES ('19', 'fsdfsdfsfdsf', '54', 'ffsdfsfsdfds', null, null);
INSERT INTO `contactinformation` VALUES ('20', 'fgdgfgfgf', '3343434', 'dfdfdfgf', null, null);
INSERT INTO `contactinformation` VALUES ('21', 'dfdsfsf', '4e556', 'ggdfgdg', null, null);
INSERT INTO `contactinformation` VALUES ('22', 'fdggfgdg', '566566', 'fgfgfgdg', null, null);
INSERT INTO `contactinformation` VALUES ('23', 'fgfggfdg', 'te6t5', 'dfggdfgfd', null, null);
INSERT INTO `contactinformation` VALUES ('24', 'dddd', '4555', 'dddd', 'ggg@fff.hjhjh', null);
INSERT INTO `contactinformation` VALUES ('25', 'sdddasdd', '4234434', '123,dsaddasd\ndffsdfdfd', 'dsd@gfg.hggh', 'test group');
INSERT INTO `tblgroup` VALUES ('1', 'test group', 'this group for test');
INSERT INTO `tblgroup` VALUES ('2', 'GROUP123', 'Test Group');
INSERT INTO `tbluser` VALUES ('admin', 'admin');
