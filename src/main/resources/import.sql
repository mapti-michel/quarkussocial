create schema quarkussocial;

CREATE TABLE IF NOT EXISTS quarkussocial.tbluser(
id int(8) not null auto_increment,
nome varchar(50) null,
idade int null,
primary key(id)
);

CREATE TABLE `quarkussocial`.`tblpost` (
  `idPost` INT NOT NULL AUTO_INCREMENT,
  `post_text` VARCHAR(150) NULL,
  `dateTime` TIMESTAMP NULL,
  `user_id` BIGINT NULL,
  PRIMARY KEY (`idPost`),
  INDEX `id_key` (`user_id` ASC) INVISIBLE,
  CONSTRAINT `id_key`
    FOREIGN KEY (`user_id`)
    REFERENCES `quarkussocial`.`tbluser` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `quarkussocial`.`tblfollowers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NULL,
  `follow_id` BIGINT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_id_key` (`user_id` ASC) INVISIBLE,
  INDEX `follow_id_key` (`follow_id` ASC) INVISIBLE,
  CONSTRAINT `user_id_key`
    FOREIGN KEY (`user_id`)
    REFERENCES `quarkussocial`.`tbluser` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `follow_id_key`
    FOREIGN KEY (`follow_id`)
    REFERENCES `quarkussocial`.`tbluser` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

