-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Mar 05, 2026 at 08:37 AM
-- Server version: 8.3.0
-- PHP Version: 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ocean_view_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
CREATE TABLE IF NOT EXISTS `reservations` (
  `reservation_number` int NOT NULL AUTO_INCREMENT,
  `guest_name` varchar(100) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `contact_number` varchar(20) DEFAULT NULL,
  `room_type` varchar(50) DEFAULT NULL,
  `check_in` date DEFAULT NULL,
  `check_out` date DEFAULT NULL,
  `total_bill` double DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `room_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`reservation_number`)
) ENGINE=MyISAM AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `reservations`
--

INSERT INTO `reservations` (`reservation_number`, `guest_name`, `address`, `contact_number`, `room_type`, `check_in`, `check_out`, `total_bill`, `email`, `room_number`) VALUES
(1, 'Dinithi ', 'Homagama', '0768095022', 'Standard', '2026-02-13', '2026-02-28', 75000, 'dedunudinithi@gmail.com', 'D_001'),
(2, 'Himal ', 'Maharagama', '0767898765', 'Luxury', '2026-02-26', '2026-03-07', 270000, 'himal@gmail.com', 'L_001'),
(45, 'Anu', 'Colombo', '0762345768', 'Deluxe', '2026-03-04', '2026-03-22', 180000, 'anu@gmail.com', 'D_006'),
(4, 'Kamali', 'Kottawa', '0789876543', 'Luxury', '2026-02-10', '2026-02-11', 30000, 'k@gmail.com', 'L_003'),
(5, 'Sanduni', 'Homagama', '0765463423', 'Luxury', '2026-02-10', '2026-02-11', 30000, 's@gmail.com', 'L_004'),
(6, 'Madhuri', 'Maharagama', '0771926095', 'Standard', '2026-02-11', '2026-02-12', 5000, 'madu@gmail.com', 'S_001'),
(7, 'Thilak', 'Kottawa', '0789876543', 'Luxury', '2026-02-19', '2026-02-28', 135000, 'thilak@gmail.com', 'L_005'),
(8, 'Hesandu Yasas', 'Maharagama', '0786543212', 'Deluxe', '2026-02-28', '2026-03-09', 90000, 'sandu@gmail.com', 'D_002'),
(9, 'Samadhi', 'Rathnapura', '0786753849', 'Deluxe', '2026-02-21', '2026-02-28', 70000, 'sama@gmail.com', 'D_003'),
(11, 'Nikii', 'Colombo', '0787878787', 'Standard', '2026-02-21', '2026-03-22', 145000, 'nikii@gmail.com', 'S_002'),
(13, 'Anne', 'Dompe', '0789898798', 'Luxury', '2026-02-26', '2026-02-27', 20000, 'anne@gmail.com', 'L_006'),
(18, 'Saara', 'Kurunegala', '0789898989', 'Standard', '2026-02-23', '2026-02-24', 5000, 'sara@gmail.com', 'S_003'),
(17, 'Malitha', 'Maharagama', '0713907676', 'Standard', '2026-02-24', '2026-02-27', 15000, 'malitha@gmail.com', 'S_004'),
(22, 'Chathura', 'Kottawa', '0712345678', 'Luxury', '2026-03-06', '2026-03-14', 160000, 'chathura@gmail.com', 'L_007'),
(44, 'Ruwan', 'Kottawa', '0789878500', 'Deluxe', '2026-03-04', '2026-03-13', 90000, 'ruwan@gmail.com', 'D_005'),
(27, 'Geethma', 'Kurunegala', '0712345678', 'Luxury', '2026-02-23', '2026-02-24', 20000, 'gee@gmail.com', 'L_008'),
(46, 'Nimal', '', '0789898989', 'Standard', '2026-03-04', '2026-03-12', 40000, 'nima@gmail.com', 'S_004'),
(33, 'nimal', 'Kottawa', '0762345768', 'Deluxe', '2026-02-28', '2026-03-04', 40000, 'nima@gmail.com', 'D_004'),
(41, 'Priya', 'Katuwana', '0786789898', 'Standard', '2026-02-28', '2026-03-03', 15000, 'priya@gmail.com', 'S_003'),
(42, 'Shima', 'Kurunegala', '0786543456', 'Deluxe', '2026-03-01', '2026-03-02', 10000, 'shima@gmail.com', 'D_003'),
(40, 'Sunil', 'Maharagama', '0789898989', 'Deluxe', '2026-02-28', '2026-03-03', 30000, 'sunill@gmail.com', 'D_002'),
(43, 'Gaya', 'Homagama', '0789876789', 'Deluxe', '2026-03-01', '2026-03-19', 180000, 'gaya@gmail.com', 'D_004'),
(47, 'Shashi', 'Maharagama', '0783152637', 'Standard', '2026-03-04', '2026-03-12', 40000, 'shsashi@gmail.com', 'S_005'),
(48, 'Gaya', 'Colombo', '0789878909', 'Deluxe', '2026-03-10', '2026-03-15', 50000, 'gaya@gmail.com', 'D101'),
(49, 'Gaya', 'Colombo', '0789878909', 'Deluxe', '2026-03-10', '2026-03-15', 50000, 'gaya@gmail.com', 'D101'),
(50, 'Guest', 'Addr', '011', 'Deluxe', '2026-03-01', '2026-03-02', 10000, 'invalid-email', '101'),
(51, 'Gaya', 'Colombo', '0789878909', 'Deluxe', '2026-03-10', '2026-03-15', 50000, 'gaya@gmail.com', 'D101'),
(52, 'Shashika', 'Maharagama', '0783152637', 'Deluxe', '2026-03-05', '2026-03-12', 70000, 'shsashi@gmail.com', 'D_010'),
(53, 'Nimasha', 'Kottawa', '0786767543', 'Luxury', '2026-03-05', '2026-03-13', 160000, 'nimasha@gmail.com', 'L_001');

-- --------------------------------------------------------

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
CREATE TABLE IF NOT EXISTS `rooms` (
  `room_number` varchar(10) NOT NULL,
  `room_type` varchar(50) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'Available',
  PRIMARY KEY (`room_number`),
  KEY `room_type` (`room_type`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `rooms`
--

INSERT INTO `rooms` (`room_number`, `room_type`, `status`) VALUES
('D_001', 'Deluxe', 'Occupied'),
('D_002', 'Deluxe', 'Occupied'),
('S_001', 'Standard', 'Occupied'),
('D_003', 'Deluxe', 'Occupied'),
('D_004', 'Deluxe', 'Occupied'),
('D_005', 'Deluxe', 'Occupied'),
('D_006', 'Deluxe', 'Occupied'),
('D_007', 'Deluxe', 'Occupied'),
('D_008', 'Deluxe', 'Occupied'),
('D_009', 'Deluxe', 'Occupied'),
('D_010', 'Deluxe', 'Occupied'),
('S_002', 'Standard', 'Occupied'),
('S_003', 'Standard', 'Occupied'),
('S_004', 'Standard', 'Occupied'),
('S_005', 'Standard', 'Occupied'),
('S_006', 'Standard', 'Available'),
('S_007', 'Standard', 'Available'),
('S_008', 'Standard', 'Available'),
('S_009', 'Standard', 'Available'),
('S_010', 'Standard', 'Available'),
('L_001', 'Luxury', 'Occupied'),
('L_002', 'Luxury', 'Available'),
('L_003', 'Luxury', 'Available'),
('L_004', 'Luxury', 'Available'),
('L_005', 'Luxury', 'Available'),
('L_006', 'Luxury', 'Available'),
('L_007', 'Luxury', 'Available'),
('L_008', 'Luxury', 'Available'),
('L_009', 'Luxury', 'Available'),
('L_010', 'Luxury', 'Available'),
('R-999', 'Luxury', 'Available');

-- --------------------------------------------------------

--
-- Table structure for table `room_rates`
--

DROP TABLE IF EXISTS `room_rates`;
CREATE TABLE IF NOT EXISTS `room_rates` (
  `room_type` varchar(50) NOT NULL,
  `price_per_night` double DEFAULT NULL,
  `max_capacity` int DEFAULT NULL,
  PRIMARY KEY (`room_type`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `room_rates`
--

INSERT INTO `room_rates` (`room_type`, `price_per_night`, `max_capacity`) VALUES
('Standard', 5000, 2),
('Deluxe', 10000, 5),
('Luxury', 20000, 2),
('Minimalist Hotel Rooms', 3000, 15),
('Suites', 12000, 4),
('Suite', 25000, 5);

-- --------------------------------------------------------

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
CREATE TABLE IF NOT EXISTS `staff` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `role` enum('ADMIN','RECEPTIONIST') DEFAULT NULL,
  `profile_pic` varchar(255) DEFAULT 'default-avatar.png',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=MyISAM AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `staff`
--

INSERT INTO `staff` (`id`, `username`, `password`, `role`, `profile_pic`) VALUES
(12, 'admin', '$2a$12$ehU/6VcWOKjpeGtPlL/t3ufeeuFk61EcyGyyOvcZ/ju7rLlqN4r3C', 'ADMIN', 'admin1.png'),
(13, 'recep_user', '$2a$12$f6dR0yzTsLfmu.Dvc1ZBb.DkXTU9mHfmPeaIxUoAB3WsfGAvSJHHS', 'RECEPTIONIST', 'default-avatar.png'),
(15, 'recep_user2', '$2a$12$tLKt0Zj/ZhdxBg6pCcdHp.WRSO6OiGc1XgALpCYYxy1iTDTX7Du1e', 'RECEPTIONIST', 'default-avatar.png'),
(17, 'recep_user3', '$2a$12$18NQI3wT844QRXdFADtpCuqsgdDaxeEDhiz.kUzPG4EIEdiC5w44G', 'RECEPTIONIST', 'default-avatar.png');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
