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
    userId   INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100)       NOT NULL,
    enabled  BIT                NOT NULL,
    name     VARCHAR(50),
    phone    CHAR(13),
    email    VARCHAR(50)        NOT NULL,
    address  VARCHAR(50)        NOT NULL
);

CREATE TABLE state
(
    stateId      INT PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(50)   NOT NULL,
    abbreviation CHAR(2),
    taxRate      DECIMAL(4, 2) NOT NULL
);

CREATE TABLE category
(
    categoryId   INT PRIMARY KEY AUTO_INCREMENT,
    categoryName VARCHAR(50) NOT NULL
);

CREATE TABLE product
(
    productId     INT PRIMARY KEY AUTO_INCREMENT,
    name          VARCHAR(50),
    chemicalName  VARCHAR(50)   NOT NULL,
    massVolume    DECIMAL(5, 2) NOT NULL,
    measurement   VARCHAR(15)   NOT NULL,
    unitCost      DECIMAL(6, 2) NOT NULL,
    handlingCost  DECIMAL(6, 2) NOT NULL,
    photoFilename VARCHAR(255)
);

CREATE TABLE `order`
(
    orderId   INT PRIMARY KEY AUTO_INCREMENT,
    orderDate DATE          NOT NULL,
    quantity  INT           NOT NULL,
    netPrice  DECIMAL(8, 2) NOT NULL,
    tax       DECIMAL(8, 2) NOT NULL,
    total     DECIMAL(8, 2) NOT NULL,
    -- fk's
    userId    INT           NOT NULL,
    CONSTRAINT `fk_user_order` FOREIGN KEY (userId)
        REFERENCES user (userId)
);

-- bridge tables
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

CREATE TABLE productCategory
(
    productId  INT NOT NULL,
    categoryId INT NOT NULL,
    PRIMARY KEY (productId, categoryId),
    CONSTRAINT `fk_product_productCategory` FOREIGN KEY (productId)
        REFERENCES product (productId),
    CONSTRAINT `fk_category_productCategory` FOREIGN KEY (categoryId)
        REFERENCES category (categoryId)
);

CREATE TABLE orderProduct
(
    orderId   INT NOT NULL,
    productId INT NOT NULL,
    PRIMARY KEY (orderId, productId),
    CONSTRAINT `fk_order_orderProduct` FOREIGN KEY (orderId)
        REFERENCES `order` (orderId),
    CONSTRAINT `fk_product_orderProduct` FOREIGN KEY (productId)
        REFERENCES product (productId)
);

CREATE TABLE orderState
(
    orderId INT NOT NULL,
    stateId INT NOT NULL,
    PRIMARY KEY (orderId, stateId),
    CONSTRAINT `fk_order_orderState` FOREIGN KEY (orderId)
        REFERENCES `order` (orderId),
    CONSTRAINT `fk_state_orderState` FOREIGN KEY (stateId)
        REFERENCES state (stateId)
);