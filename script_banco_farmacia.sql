-- MySQL dump 10.13  Distrib 8.0.45, for Linux (x86_64)
--
-- Host: localhost    Database: erp
-- ------------------------------------------------------
-- Server version	8.0.45-0ubuntu0.24.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bloqueio_ip`
--

DROP TABLE IF EXISTS `bloqueio_ip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bloqueio_ip` (
  `ip` varchar(45) NOT NULL,
  `tentativas` int DEFAULT '0',
  `ultima_tentativa` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `bloqueado_ate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ip`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bloqueio_ip`
--

LOCK TABLES `bloqueio_ip` WRITE;
/*!40000 ALTER TABLE `bloqueio_ip` DISABLE KEYS */;
/*!40000 ALTER TABLE `bloqueio_ip` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categorias`
--

DROP TABLE IF EXISTS `categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias`
--

LOCK TABLES `categorias` WRITE;
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
INSERT INTO `categorias` VALUES (1,'Medicamentos','Remédios em geral'),(2,'Higiene','Produtos de cuidado pessoal');
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome_completo` varchar(100) NOT NULL,
  `cpf` varchar(14) NOT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `pontos_fidelidade` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cpf` (`cpf`),
  UNIQUE KEY `uk_cliente_cpf` (`cpf`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (1,'João Silva','111.111.111-11','21999999999',0),(2,'Maria Oliveira','222.222.222-22','21988888888',50);
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `compras`
--

DROP TABLE IF EXISTS `compras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `compras` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fornecedor_id` int NOT NULL,
  `usuario_id` int NOT NULL,
  `data_compra` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `valor_total` decimal(10,2) NOT NULL,
  `status_pedido` varchar(50) DEFAULT 'PENDENTE',
  PRIMARY KEY (`id`),
  KEY `fornecedor_id` (`fornecedor_id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `compras_ibfk_1` FOREIGN KEY (`fornecedor_id`) REFERENCES `fornecedores` (`id`),
  CONSTRAINT `compras_ibfk_2` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `compras`
--

LOCK TABLES `compras` WRITE;
/*!40000 ALTER TABLE `compras` DISABLE KEYS */;
/*!40000 ALTER TABLE `compras` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fornecedores`
--

DROP TABLE IF EXISTS `fornecedores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fornecedores` (
  `id` int NOT NULL AUTO_INCREMENT,
  `razao_social` varchar(150) NOT NULL,
  `cnpj` varchar(18) NOT NULL,
  `categoria_fornecimento` varchar(50) DEFAULT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `ativo` tinyint DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cnpj` (`cnpj`),
  UNIQUE KEY `uk_fornecedor_cnpj` (`cnpj`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fornecedores`
--

LOCK TABLES `fornecedores` WRITE;
/*!40000 ALTER TABLE `fornecedores` DISABLE KEYS */;
/*!40000 ALTER TABLE `fornecedores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `itens_compra`
--

DROP TABLE IF EXISTS `itens_compra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `itens_compra` (
  `id` int NOT NULL AUTO_INCREMENT,
  `compra_id` int NOT NULL,
  `produto_id` int NOT NULL,
  `quantidade` int NOT NULL,
  `preco_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `compra_id` (`compra_id`),
  KEY `produto_id` (`produto_id`),
  CONSTRAINT `itens_compra_ibfk_1` FOREIGN KEY (`compra_id`) REFERENCES `compras` (`id`) ON DELETE CASCADE,
  CONSTRAINT `itens_compra_ibfk_2` FOREIGN KEY (`produto_id`) REFERENCES `produtos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `itens_compra`
--

LOCK TABLES `itens_compra` WRITE;
/*!40000 ALTER TABLE `itens_compra` DISABLE KEYS */;
/*!40000 ALTER TABLE `itens_compra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `itens_venda`
--

DROP TABLE IF EXISTS `itens_venda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `itens_venda` (
  `id` int NOT NULL AUTO_INCREMENT,
  `venda_id` int NOT NULL,
  `produto_id` int NOT NULL,
  `quantidade` int NOT NULL,
  `preco_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `venda_id` (`venda_id`),
  KEY `produto_id` (`produto_id`),
  CONSTRAINT `itens_venda_ibfk_1` FOREIGN KEY (`venda_id`) REFERENCES `vendas` (`id`) ON DELETE CASCADE,
  CONSTRAINT `itens_venda_ibfk_2` FOREIGN KEY (`produto_id`) REFERENCES `produtos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `itens_venda`
--

LOCK TABLES `itens_venda` WRITE;
/*!40000 ALTER TABLE `itens_venda` DISABLE KEYS */;
INSERT INTO `itens_venda` VALUES (1,1,1,1,25.50,25.50),(2,1,1,1,25.50,25.50),(3,1,1,1,25.50,25.50),(4,1,2,1,35.00,35.00),(5,1,3,1,18.90,18.90),(6,2,1,1,25.50,25.50),(7,3,1,1,25.50,25.50),(8,4,1,1,25.50,25.50),(9,5,1,1,25.50,25.50),(10,6,1,1,25.50,25.50),(11,7,2,1,35.00,35.00),(12,8,1,1,25.50,25.50),(13,9,1,1,25.50,25.50),(14,10,1,1,25.50,25.50),(15,11,1,1,25.50,25.50),(16,12,1,1,25.50,25.50),(17,13,1,1,25.50,25.50),(18,14,3,1,18.90,18.90),(19,15,3,1,18.90,18.90),(20,16,2,1,35.00,35.00),(21,16,3,1,18.90,18.90),(22,17,1,1,25.50,25.50),(23,17,2,1,35.00,35.00),(24,17,3,1,18.90,18.90),(25,18,2,1,35.00,35.00),(26,19,1,1,25.50,25.50),(27,19,2,1,35.00,35.00),(28,19,3,1,18.90,18.90),(29,20,1,1,25.50,25.50);
/*!40000 ALTER TABLE `itens_venda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_acessos`
--

DROP TABLE IF EXISTS `log_acessos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_acessos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int DEFAULT NULL,
  `data_hora` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `acao` varchar(255) DEFAULT NULL,
  `ip` varchar(45) DEFAULT NULL,
  `resultado` varchar(20) DEFAULT NULL,
  `detalhes` text,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `log_acessos_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_acessos`
--

LOCK TABLES `log_acessos` WRITE;
/*!40000 ALTER TABLE `log_acessos` DISABLE KEYS */;
INSERT INTO `log_acessos` VALUES (1,1,'2026-04-29 13:37:44','LOGIN','0:0:0:0:0:0:0:1','FALHA - TENTATIVA 1',NULL),(2,1,'2026-04-29 13:38:23','LOGIN','0:0:0:0:0:0:0:1','FALHA - TENTATIVA 2',NULL),(3,6,'2026-04-29 13:43:52','LOGIN','0:0:0:0:0:0:0:1','FALHA - TENTATIVA 1',NULL),(4,1,'2026-04-29 17:49:39','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(5,1,'2026-04-29 17:53:35','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(6,1,'2026-04-29 17:53:58','LOGIN','0:0:0:0:0:0:0:1','FALHA - TENTATIVA 1',NULL),(7,1,'2026-04-29 17:54:07','LOGIN','0:0:0:0:0:0:0:1','FALHA - TENTATIVA 2',NULL),(8,6,'2026-04-29 17:55:07','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(9,1,'2026-04-29 17:57:44','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(10,1,'2026-04-29 17:59:12','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(11,1,'2026-04-29 17:59:33','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(12,1,'2026-04-29 18:01:08','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(13,1,'2026-04-29 18:05:06','LOGIN','0:0:0:0:0:0:0:1','FALHA - TENTATIVA 1',NULL),(14,1,'2026-04-29 19:38:52','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(15,1,'2026-04-29 19:43:01','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(16,1,'2026-04-29 19:45:15','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(17,1,'2026-04-29 19:51:16','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(18,1,'2026-04-29 19:54:42','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(19,1,'2026-04-29 19:59:19','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(20,1,'2026-04-29 20:00:10','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(21,1,'2026-04-29 20:05:33','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(22,1,'2026-04-30 17:56:49','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(23,1,'2026-04-30 17:56:49','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(24,8,'2026-04-30 17:58:28','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(25,1,'2026-04-30 18:00:49','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(26,1,'2026-04-30 21:35:22','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(27,6,'2026-04-30 21:35:58','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(28,1,'2026-04-30 23:49:45','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(29,6,'2026-05-01 15:33:22','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(30,1,'2026-05-01 17:48:36','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(31,1,'2026-05-01 17:49:49','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(32,1,'2026-05-01 17:59:17','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(33,1,'2026-05-01 18:00:29','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(34,1,'2026-05-01 18:30:52','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(35,1,'2026-05-01 19:26:47','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(36,1,'2026-05-01 19:29:13','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(37,1,'2026-05-01 19:34:59','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(38,1,'2026-05-01 19:39:46','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(39,1,'2026-05-01 19:43:55','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(40,1,'2026-05-01 19:46:07','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(41,1,'2026-05-01 19:46:39','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(42,1,'2026-05-01 19:49:54','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(43,1,'2026-05-01 20:00:09','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(44,1,'2026-05-01 20:02:07','LOGIN','0:0:0:0:0:0:0:1','FALHA - TENTATIVA 1',NULL),(45,1,'2026-05-01 20:02:12','LOGIN','0:0:0:0:0:0:0:1','FALHA - TENTATIVA 2',NULL),(46,1,'2026-05-01 20:02:22','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(47,1,'2026-05-02 00:03:47','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(48,6,'2026-05-02 00:05:42','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(49,6,'2026-05-02 18:19:49','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(50,6,'2026-05-02 18:21:06','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(51,6,'2026-05-02 18:31:35','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(52,6,'2026-05-02 18:36:29','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(53,6,'2026-05-02 18:37:16','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(54,1,'2026-05-02 18:39:18','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(55,1,'2026-05-02 18:42:08','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(56,6,'2026-05-02 18:47:07','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(57,1,'2026-05-02 19:04:58','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(58,1,'2026-05-02 19:05:58','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(59,1,'2026-05-02 19:07:54','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(60,1,'2026-05-02 19:09:46','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(61,1,'2026-05-02 23:48:37','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(62,1,'2026-05-03 00:00:36','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(63,1,'2026-05-03 00:02:18','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(64,1,'2026-05-03 00:03:22','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(65,1,'2026-05-03 00:06:46','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(66,1,'2026-05-03 16:08:56','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(67,1,'2026-05-03 16:25:07','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(68,1,'2026-05-03 16:27:44','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL),(69,1,'2026-05-03 16:30:10','LOGIN','0:0:0:0:0:0:0:1','SUCESSO',NULL);
/*!40000 ALTER TABLE `log_acessos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produtos`
--

DROP TABLE IF EXISTS `produtos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produtos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `codigo_barras` varchar(50) DEFAULT NULL,
  `nome` varchar(150) NOT NULL,
  `descricao` text,
  `preco_custo` decimal(10,2) NOT NULL,
  `preco_venda` decimal(10,2) NOT NULL,
  `qtd_atual` int NOT NULL DEFAULT '0',
  `qtd_minima` int NOT NULL DEFAULT '5',
  `categoria_id` int DEFAULT NULL,
  `fornecedor_id` int DEFAULT NULL,
  `ativo` tinyint DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `codigo_barras` (`codigo_barras`),
  KEY `categoria_id` (`categoria_id`),
  KEY `fornecedor_id` (`fornecedor_id`),
  CONSTRAINT `produtos_ibfk_1` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`),
  CONSTRAINT `produtos_ibfk_2` FOREIGN KEY (`fornecedor_id`) REFERENCES `fornecedores` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produtos`
--

LOCK TABLES `produtos` WRITE;
/*!40000 ALTER TABLE `produtos` DISABLE KEYS */;
INSERT INTO `produtos` VALUES (1,'789101010','Tylenol 750mg',NULL,10.00,25.50,83,10,1,NULL,1),(2,'789202020','Amoxicilina 500mg',NULL,15.00,35.00,94,15,1,NULL,1),(3,'789303030','Desodorante Rexona',NULL,8.00,18.90,94,5,2,NULL,1);
/*!40000 ALTER TABLE `produtos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `login` varchar(50) NOT NULL,
  `senha_hash` varchar(255) NOT NULL,
  `perfil` varchar(50) NOT NULL,
  `ativo` tinyint(1) DEFAULT '1',
  `token_recuperacao` varchar(100) DEFAULT NULL,
  `token_expiracao` datetime DEFAULT NULL,
  `tentativas_falhas` int DEFAULT '0',
  `tentativasFalhas` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `login` (`login`),
  UNIQUE KEY `uk_usuario_email` (`email`),
  UNIQUE KEY `uk_usuario_login` (`login`),
  UNIQUE KEY `token_recuperacao` (`token_recuperacao`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Carlos Diretoria','carlos.admin@farmacia.com','carlos.admin','$2a$10$0wU4a/ZwzzDgB9Oa7k0ONuFOOgnS13KSID5qtqPIMR48bJxvceVDO','ADMIN',1,NULL,NULL,0,0),(2,'Marina Gerente','marina.ger@farmacia.com','marina.ger','$2a$10$0wU4a/ZwzzDgB9Oa7k0ONuFOOgnS13KSID5qtqPIMR48bJxvceVDO','GERENTE',1,NULL,NULL,0,0),(3,'Lucas Caixa','lucas.cx@farmacia.com','lucas.cx','$2a$10$0wU4a/ZwzzDgB9Oa7k0ONuFOOgnS13KSID5qtqPIMR48bJxvceVDO','CAIXA',1,NULL,NULL,0,0),(4,'Ana Silva','ana.cx@farmacia.com','ana.cx','$2a$10$0wU4a/ZwzzDgB9Oa7k0ONuFOOgnS13KSID5qtqPIMR48bJxvceVDO','CAIXA',1,NULL,NULL,0,0),(5,'Eduardo da Silva','eduardo@farmacia.com','eduardo.caixa','$2a$10$TJuCXnCCWk5d6RmfBA5CbewaO7QaEB783xkxHg0Dlilw/wSbMd1IS','OPERADOR',1,NULL,NULL,0,0),(6,'marcos felipe','marcos@farmacia.com','marcos.caixa','$2a$10$HOXbVKOL0fLUqjOeIaibr.SpnqCIP2eJKIEzwte7ecIOkYuKV29g6','CAIXA',1,NULL,NULL,0,0),(8,'joao','joaovictorximenes0@gmail.com','joao.caixa','$2a$10$DNMjqQQz/YQA.86c0gcCVe870pBKjPI8RuD/oH7zSrY/9jXQMk0XC','CAIXA',1,NULL,NULL,0,0);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vendas`
--

DROP TABLE IF EXISTS `vendas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vendas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `cliente_id` int DEFAULT NULL,
  `data_venda` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `valor_total` decimal(10,2) NOT NULL,
  `forma_pagamento` varchar(50) NOT NULL,
  `status_pagamento` varchar(50) DEFAULT 'CONCLUIDO',
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `cliente_id` (`cliente_id`),
  CONSTRAINT `vendas_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `vendas_ibfk_2` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vendas`
--

LOCK TABLES `vendas` WRITE;
/*!40000 ALTER TABLE `vendas` DISABLE KEYS */;
INSERT INTO `vendas` VALUES (1,6,1,'2026-05-02 18:47:20',130.40,'PIX','CONCLUIDO'),(2,1,1,'2026-05-02 19:05:06',25.50,'PIX','CONCLUIDO'),(3,1,NULL,'2026-05-02 19:05:11',25.50,'PIX','CONCLUIDO'),(4,1,NULL,'2026-05-02 19:05:18',25.50,'CREDIT_CARD','CONCLUIDO'),(5,1,NULL,'2026-05-02 19:06:04',25.50,'PIX','CONCLUIDO'),(6,1,NULL,'2026-05-02 19:09:59',25.50,'PIX','CONCLUIDO'),(7,1,NULL,'2026-05-02 19:10:25',35.00,'CREDIT_CARD','CONCLUIDO'),(8,1,NULL,'2026-05-02 19:10:37',25.50,'PIX','CONCLUIDO'),(9,1,NULL,'2026-05-02 19:10:43',25.50,'CASH','CONCLUIDO'),(10,1,NULL,'2026-05-02 23:48:43',25.50,'PIX','CONCLUIDO'),(11,1,NULL,'2026-05-03 00:00:42',25.50,'PIX','CONCLUIDO'),(12,1,NULL,'2026-05-03 00:02:22',25.50,'PIX','CONCLUIDO'),(13,1,NULL,'2026-05-03 00:03:25',25.50,'PIX','CONCLUIDO'),(14,1,NULL,'2026-05-03 00:06:50',18.90,'PIX','CONCLUIDO'),(15,1,NULL,'2026-05-03 00:06:58',18.90,'PIX','CONCLUIDO'),(16,1,NULL,'2026-05-03 00:07:04',53.90,'PIX','CONCLUIDO'),(17,1,NULL,'2026-05-03 00:07:13',79.40,'PIX','CONCLUIDO'),(18,1,NULL,'2026-05-03 00:07:20',35.00,'CREDIT_CARD','CONCLUIDO'),(19,1,1,'2026-05-03 00:10:05',79.40,'PIX','CONCLUIDO'),(20,1,NULL,'2026-05-03 16:09:10',25.50,'CREDIT_CARD','CONCLUIDO');
/*!40000 ALTER TABLE `vendas` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-03 13:36:36
