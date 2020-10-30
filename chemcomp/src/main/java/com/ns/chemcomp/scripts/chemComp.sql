DROP DATABASE IF EXISTS chemComp;
CREATE DATABASE IF NOT EXISTS chemComp;

USE chemComp;

CREATE TABLE role
(
    roleId INT PRIMARY KEY AUTO_INCREMENT,
    role   VARCHAR(30) NOT NULL
);

CREATE TABLE user
(
    userId        INT PRIMARY KEY AUTO_INCREMENT,
    username      VARCHAR(50) UNIQUE NOT NULL,
    password      VARCHAR(50)        NOT NULL,
    enabled       BIT                NOT NULL,
    name          VARCHAR(50),
    phone         CHAR(13),
    email         VARCHAR(50)        NOT NULL,
    address       VARCHAR(50)        NOT NULL,
    photoFilename VARCHAR(255)
);

-- bridge
CREATE TABLE userRole
(
    roleId INT NOT NULL,
    userId INT NOT NULL,
    PRIMARY KEY (roleId, userId),
    CONSTRAINT `fk_role_userRole` FOREIGN KEY (roleId)
        REFERENCES role (roleId),
    CONSTRAINT `fk_user_userRole` FOREIGN KEY (userId)
        REFERENCES user (userId)
);

CREATE TABLE state
(
    stateId      INT PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(50)   NOT NULL,
    abbreviation CHAR(2),
    taxRate      DECIMAL(4, 2) NOT NULL
);

CREATE TABLE product
(
    productId     INT PRIMARY KEY AUTO_INCREMENT,
    name          VARCHAR(50),
    chemicalName  VARCHAR(50)   NOT NULL,
    measurement   VARCHAR(15)   NOT NULL,
    unitCost      DECIMAL(6, 2) NOT NULL,
    handlingCost  DECIMAL(6, 2) NOT NULL,
    photoFilename VARCHAR(255)
);

CREATE TABLE `order`
(
    orderId   INT PRIMARY KEY AUTO_INCREMENT,
    orderDate DATETIME      NOT NULL DEFAULT NOW(),
    quantity  DECIMAL(6, 2) NOT NULL,
    netPrice  DECIMAL(8, 2) NOT NULL, -- unit cost * quantity
    tax       DECIMAL(8, 2) NOT NULL, -- state tax rate * net price
    total     DECIMAL(8, 2) NOT NULL,
    -- fk's
    userId    INT           NOT NULL,
    stateId   INT           NOT NULL,
    productId INT           NOT NULL,
    CONSTRAINT `fk_user_order` FOREIGN KEY (userId)
        REFERENCES user (userId),
    CONSTRAINT `fk_state_order` FOREIGN KEY (stateId)
        REFERENCES state (stateId),
    CONSTRAINT `fk_product_order` FOREIGN KEY (productId)
        REFERENCES product (productId)
);