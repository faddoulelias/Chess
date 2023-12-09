-- -----------------------------------------------------
-- Schema chess
-- -----------------------------------------------------
CREATE DATABASE IF NOT EXISTS `chess` DEFAULT CHARACTER
SET
  utf8;

USE `chess`;

-- -----------------------------------------------------
-- Table `chess`.`player`
-- -----------------------------------------------------
CREATE TABLE
  IF NOT EXISTS `chess`.`player` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE
  ) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `chess`.`game`
-- -----------------------------------------------------
CREATE TABLE
  IF NOT EXISTS `chess`.`game` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `playerWhite` INT NOT NULL,
    `playerBlack` INT NOT NULL,
    `board` JSON NOT NULL,
    `history` JSON NULL,
    `isWhiteTurn` TINYINT NOT NULL,
    `turnCount` INT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_ChessGame_Player_idx` (`playerWhite` ASC) VISIBLE,
    INDEX `fk_ChessGame_Player1_idx` (`playerBlack` ASC) VISIBLE,
    CONSTRAINT `fk_ChessGame_Player` FOREIGN KEY (`playerWhite`) REFERENCES `chess`.`player` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT `fk_ChessGame_Player1` FOREIGN KEY (`playerBlack`) REFERENCES `chess`.`player` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
  ) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `chess`.`credentials`
-- -----------------------------------------------------
CREATE TABLE
  IF NOT EXISTS `chess`.`credentials` (
    `player_id` INT NOT NULL,
    `email` VARCHAR(320) NOT NULL,
    `salt` CHAR(16) NOT NULL,
    `sha256` CHAR(64) NOT NULL,
    INDEX `fk_credentials_Player1_idx` (`player_id` ASC) VISIBLE,
    PRIMARY KEY (`player_id`),
    UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
    CONSTRAINT `fk_credentials_Player1` FOREIGN KEY (`player_id`) REFERENCES `chess`.`player` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
  ) ENGINE = InnoDB;