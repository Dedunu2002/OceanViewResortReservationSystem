USE ocean_view_db;
-- Then your CREATE TABLE statements...
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
)

CREATE TABLE IF NOT EXISTS `rooms` (
  `room_number` varchar(10) NOT NULL,
  `room_type` varchar(50) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'Available',
  PRIMARY KEY (`room_number`),
  KEY `room_type` (`room_type`)
)

CREATE TABLE IF NOT EXISTS `room_rates` (
  `room_type` varchar(50) NOT NULL,
  `price_per_night` double DEFAULT NULL,
  `max_capacity` int DEFAULT NULL,
  PRIMARY KEY (`room_type`)
)

CREATE TABLE IF NOT EXISTS `staff` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `role` enum('ADMIN','RECEPTIONIST') DEFAULT NULL,
  `profile_pic` varchar(255) DEFAULT 'default-avatar.png',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
)
