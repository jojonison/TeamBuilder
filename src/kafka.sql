-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: May 17, 2023 at 04:03 PM
-- Server version: 8.0.31
-- PHP Version: 8.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kafka`
--

-- --------------------------------------------------------

--
-- Table structure for table `application`
--

DROP TABLE IF EXISTS `application`;
CREATE TABLE IF NOT EXISTS `application` (
                                             `applicationid` int NOT NULL,
                                             `studentid` int NOT NULL,
                                             `sportid` int NOT NULL,
                                             `tryoutid` int NOT NULL,
                                             `approvalstatus` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `applicationdate` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`applicationid`),
    KEY `FK_student_application` (`studentid`),
    KEY `FK_tryoutdetails_application` (`tryoutid`) USING BTREE,
    KEY `FK_sports_application` (`sportid`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `application`
--

INSERT INTO `application` (`applicationid`, `studentid`, `sportid`, `tryoutid`, `approvalstatus`, `applicationdate`) VALUES
                                                                                                                         (1001, 2221213, 7, 1417, 'Qualified', '2023-02-04'),
                                                                                                                         (2000, 2201234, 5, 3255, 'Pending', '2023-02-02'),
                                                                                                                         (2001, 2212121, 1, 6446, 'Qualified', '2023-02-05'),
                                                                                                                         (3000, 2235432, 1, 3005, 'Denied', '2023-02-03'),
                                                                                                                         (3001, 2202020, 2, 7543, 'Qualified', '2023-02-06'),
                                                                                                                         (8890, 2223905, 5, 7545, 'Pending', '2023-05-17');

-- --------------------------------------------------------

--
-- Table structure for table `coach`
--

DROP TABLE IF EXISTS `coach`;
CREATE TABLE IF NOT EXISTS `coach` (
                                       `coachid` int NOT NULL,
                                       `firstname` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `lastname` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `sportid` int NOT NULL,
    `departmentkey` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`coachid`),
    KEY `FK_sport_coach` (`sportid`),
    KEY `FK_departmentkey_coach` (`departmentkey`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `coach`
--

INSERT INTO `coach` (`coachid`, `firstname`, `lastname`, `sportid`, `departmentkey`) VALUES
                                                                                         (1000, 'Peter', 'Parker', 1, 'SEA'),
                                                                                         (1001, 'Gwen', 'Stacy', 4, 'SEA'),
                                                                                         (2000, 'Miles', 'Morales', 3, 'SAMCIS'),
                                                                                         (2001, 'Lucius', 'Fox', 7, 'SAMCIS'),
                                                                                         (3000, 'Michael', 'Morbius', 5, 'SOM'),
                                                                                         (3001, 'Maryjane', 'Watson', 9, 'STELA'),
                                                                                         (4000, 'Charlie', 'Cox', 10, 'SOL'),
                                                                                         (5000, 'Stephen', 'Strange', 8, 'SOM'),
                                                                                         (6000, 'Pamella', 'Isley', 11, 'SONAHBS'),
                                                                                         (6001, 'Groot', 'Grootingson', 2, 'SONAHBS');

-- --------------------------------------------------------

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
CREATE TABLE IF NOT EXISTS `department` (
    `departmentkey` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `departmentname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`departmentkey`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `department`
--

INSERT INTO `department` (`departmentkey`, `departmentname`) VALUES
                                                                 ('SAMCIS', 'School of Accountancy, Management, Computer and Information Studies'),
                                                                 ('SEA', 'School of Engineering and Architecture'),
                                                                 ('SOL', 'School of Law'),
                                                                 ('SOM', 'School of Medicine'),
                                                                 ('SONAHBS', 'School of Nursing, Allied Health, and Biological Sciences'),
                                                                 ('STELA', 'School of Teacher Education and Liberal Arts');

-- --------------------------------------------------------

--
-- Table structure for table `sport`
--

DROP TABLE IF EXISTS `sport`;
CREATE TABLE IF NOT EXISTS `sport` (
                                       `sportid` int NOT NULL,
                                       `sportname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `sporttype` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `availability` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`sportid`),
    UNIQUE KEY `sportname` (`sportname`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `sport`
--

INSERT INTO `sport` (`sportid`, `sportname`, `sporttype`, `availability`) VALUES
                                                                              (1, 'Volleyball Men', 'Team', 'Available'),
                                                                              (2, 'Volleybal Women', 'Team', 'Available'),
                                                                              (3, 'Basketball Men', 'Team', 'Available'),
                                                                              (4, 'Chess Women', 'Singleplayer', 'Available'),
                                                                              (5, 'Chess Men', 'Singleplayer', 'Available'),
                                                                              (7, 'Table Tennis Men', 'Singleplayer', 'Available'),
                                                                              (8, 'Table Tennis Women', 'Singleplayer', 'Available'),
                                                                              (9, 'Basketball Women', 'Singleplayer', 'Available'),
                                                                              (10, 'Badminton Men', 'Singleplayer', 'Available'),
                                                                              (11, 'Badminton Women', 'Singleplayer', 'Available');

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
CREATE TABLE IF NOT EXISTS `student` (
                                         `studentid` int NOT NULL,
                                         `firstname` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `lastname` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `emailaddress` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `departmentkey` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`studentid`),
    UNIQUE KEY `AK_emailaddress_student` (`emailaddress`) USING BTREE,
    KEY `FK_departmentkey_student` (`departmentkey`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`studentid`, `firstname`, `lastname`, `emailaddress`, `departmentkey`) VALUES
                                                                                                  (2201234, 'Jonison', 'Molintas', '2201234@slu.edu.ph', 'SOM'),
                                                                                                  (2202020, 'Susan', 'Saison', '2202020@slu.edu.ph', 'SONAHBS'),
                                                                                                  (2212121, 'Jonathan', 'Than', '2212121@slu.edu.ph', 'SEA'),
                                                                                                  (2221213, 'Bob', 'Bobberson', '2221213@slu.edu.ph', 'SAMCIS'),
                                                                                                  (2223905, 'Valjunyor', 'Alfiler', '2223905@slu.edu.ph', 'SOL'),
                                                                                                  (2235432, 'Chris Pi', 'Bacon', '2235432@slu.edu.ph', 'SEA');

-- --------------------------------------------------------

--
-- Table structure for table `tryoutdetails`
--

DROP TABLE IF EXISTS `tryoutdetails`;
CREATE TABLE IF NOT EXISTS `tryoutdetails` (
                                               `tryoutid` int NOT NULL,
                                               `sportid` int NOT NULL,
                                               `schedule` datetime NOT NULL,
                                               `location` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `coachid` int NOT NULL,
    PRIMARY KEY (`tryoutid`),
    KEY `coachid` (`coachid`),
    KEY `FK_sport_tryoutdetails_idx` (`sportid`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `tryoutdetails`
--

INSERT INTO `tryoutdetails` (`tryoutid`, `sportid`, `schedule`, `location`, `coachid`) VALUES
                                                                                           (1417, 7, '2023-02-01 16:30:00', 'SLU Covered Courts', 2001),
                                                                                           (3005, 1, '2023-02-01 16:30:00', 'SLU Covered Courts', 1000),
                                                                                           (3255, 5, '2023-02-02 16:30:00', 'Prince Bernhard Gym', 3000),
                                                                                           (3284, 3, '2023-02-01 16:30:00', 'Prince Bernhard Gym', 2000),
                                                                                           (6446, 1, '2023-02-01 16:30:00', 'SLU Covered Courts', 1000),
                                                                                           (7543, 2, '2023-02-01 16:30:00', 'SLU Covered Courts', 6001),
                                                                                           (7545, 5, '2023-02-02 16:30:00', 'Prince Bernhard Gym', 3000);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `application`
--
ALTER TABLE `application`
    ADD CONSTRAINT `sportid` FOREIGN KEY (`sportid`) REFERENCES `sport` (`sportid`),
  ADD CONSTRAINT `studentid` FOREIGN KEY (`studentid`) REFERENCES `student` (`studentid`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tryoutid` FOREIGN KEY (`tryoutid`) REFERENCES `tryoutdetails` (`tryoutid`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `coach`
--
ALTER TABLE `coach`
    ADD CONSTRAINT `FK_departmentkey_coach` FOREIGN KEY (`departmentkey`) REFERENCES `department` (`departmentkey`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `FK_sport_coach` FOREIGN KEY (`sportid`) REFERENCES `sport` (`sportid`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Constraints for table `student`
--
ALTER TABLE `student`
    ADD CONSTRAINT `FK_departmentkey_student` FOREIGN KEY (`departmentkey`) REFERENCES `department` (`departmentkey`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Constraints for table `tryoutdetails`
--
ALTER TABLE `tryoutdetails`
    ADD CONSTRAINT `FK_coach_tryoutdetails` FOREIGN KEY (`coachid`) REFERENCES `coach` (`coachid`),
  ADD CONSTRAINT `FK_sport_tryoutdetails` FOREIGN KEY (`sportid`) REFERENCES `sport` (`sportid`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
