-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: pharmaceutical_system
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
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
  `id` int NOT NULL AUTO_INCREMENT,
  `ip` varchar(45) NOT NULL,
  `tentativas` int DEFAULT '0',
  `bloqueado_ate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
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
  `descricao` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias`
--

LOCK TABLES `categorias` WRITE;
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
INSERT INTO `categorias` VALUES (1,'Medicamentos Genéricos','Medicamentos com o mesmo princípio ativo'),(2,'Higiene e Cuidados Diários','Produtos para uso diário e higiene pessoal'),(3,'Suplementos Alimentares','Vitaminas e complementos nutricionais'),(6,'Equipamentos Médicos','Aparelhos e testes de monitoramento');
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
  `nome` varchar(100) NOT NULL,
  `cpf` varchar(14) NOT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cpf` (`cpf`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (1,'Fulano de Tal','111.111.111-11','(11) 99999-9999','fulano@email.com'),(2,'Ciclano de Sousa','222.222.222-22','(11) 98888-8888','ciclano@email.com');
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
  `data_compra` datetime DEFAULT CURRENT_TIMESTAMP,
  `valor_total` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fornecedor_id` (`fornecedor_id`),
  CONSTRAINT `compras_ibfk_1` FOREIGN KEY (`fornecedor_id`) REFERENCES `fornecedores` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `compras`
--

LOCK TABLES `compras` WRITE;
/*!40000 ALTER TABLE `compras` DISABLE KEYS */;
INSERT INTO `compras` VALUES (1,3,'2026-06-01 17:10:50',1500.00),(2,4,'2026-06-01 17:10:50',2300.50);
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
  `categoria_fornecimento` varchar(100) DEFAULT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `ativo` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cnpj` (`cnpj`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fornecedores`
--

LOCK TABLES `fornecedores` WRITE;
/*!40000 ALTER TABLE `fornecedores` DISABLE KEYS */;
INSERT INTO `fornecedores` VALUES (3,'Eurofarma Laboratórios S.A.','61.190.096/0001-92','Medicamentos Genéricos e Similares',NULL,NULL,1),(4,'EMS Sigma Pharma Ltda','57.507.378/0001-01','Medicamentos Controlados e Tarjados',NULL,NULL,1);
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
  CONSTRAINT `itens_compra_ibfk_1` FOREIGN KEY (`compra_id`) REFERENCES `compras` (`id`),
  CONSTRAINT `itens_compra_ibfk_2` FOREIGN KEY (`produto_id`) REFERENCES `produtos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `itens_compra`
--

LOCK TABLES `itens_compra` WRITE;
/*!40000 ALTER TABLE `itens_compra` DISABLE KEYS */;
INSERT INTO `itens_compra` VALUES (1,1,1,100,2.50,250.00),(2,1,2,50,3.00,150.00),(3,2,11,10,110.00,1100.00);
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
  CONSTRAINT `itens_venda_ibfk_1` FOREIGN KEY (`venda_id`) REFERENCES `vendas` (`id`),
  CONSTRAINT `itens_venda_ibfk_2` FOREIGN KEY (`produto_id`) REFERENCES `produtos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `itens_venda`
--

LOCK TABLES `itens_venda` WRITE;
/*!40000 ALTER TABLE `itens_venda` DISABLE KEYS */;
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
  `acao` varchar(255) NOT NULL,
  `ip` varchar(45) DEFAULT NULL,
  `data_acesso` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `log_acessos_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_acessos`
--

LOCK TABLES `log_acessos` WRITE;
/*!40000 ALTER TABLE `log_acessos` DISABLE KEYS */;
INSERT INTO `log_acessos` VALUES (1,1,'LOGIN SUCESSO','127.0.0.1','2026-06-01 17:10:50'),(2,2,'LOGIN SUCESSO','127.0.0.1','2026-06-01 17:10:50'),(3,3,'ABERTURA DE CAIXA','127.0.0.1','2026-06-01 17:10:50');
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
  `codigo_barras` varchar(50) NOT NULL,
  `nome` varchar(150) NOT NULL,
  `descricao` text,
  `preco_custo` decimal(10,2) NOT NULL,
  `preco_venda` decimal(10,2) NOT NULL,
  `qtd_atual` int NOT NULL DEFAULT '0',
  `qtd_minima` int NOT NULL DEFAULT '0',
  `categoria_id` int DEFAULT NULL,
  `fornecedor_id` int DEFAULT NULL,
  `data_validade` date DEFAULT NULL,
  `ativo` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `codigo_barras` (`codigo_barras`),
  KEY `categoria_id` (`categoria_id`),
  KEY `fornecedor_id` (`fornecedor_id`),
  CONSTRAINT `produtos_ibfk_1` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`),
  CONSTRAINT `produtos_ibfk_2` FOREIGN KEY (`fornecedor_id`) REFERENCES `fornecedores` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produtos`
--

LOCK TABLES `produtos` WRITE;
/*!40000 ALTER TABLE `produtos` DISABLE KEYS */;
INSERT INTO `produtos` VALUES (1,'7891020304001','Paracetamol 500mg','Analgésico e antitérmico',2.50,5.50,200,20,1,3,NULL,1),(2,'7891020304002','Dipirona Monoidratada 1g','Analgésico eficaz contra dores',3.00,6.50,150,15,1,3,NULL,1),(3,'7891020304003','Amoxicilina 500mg','Antibiótico para infecções bacterianas',12.50,24.90,90,10,1,3,NULL,1),(4,'7891020304004','Ibuprofeno 600mg','Anti-inflamatório e analgésico',4.20,9.80,110,15,1,3,NULL,1),(5,'7891020304005','Loratadina 10mg','Anti-histamínico para alergias',3.50,8.20,85,12,1,3,NULL,1),(6,'7891020304006','Omeprazol 20mg','Protetor gástrico para azia e refluxo',5.00,11.50,130,20,1,3,NULL,1),(7,'7891020304007','Losartana Potássica 50mg','Anti-hipertensivo para controle de pressão',2.10,6.00,300,40,1,3,NULL,1),(8,'7891020304008','Simvシナスタatina 20mg','Medicamento para controle do colesterol',6.80,14.90,70,10,1,3,NULL,1),(9,'7891020304009','Metformina 850mg','Antidiabético para controle da glicemia',3.20,7.50,250,30,1,3,NULL,1),(10,'7891020304051','Fralda Pampers Confort Sec G','Fraldas descartáveis tamanho G',65.00,89.90,50,10,2,4,NULL,1),(11,'7891020304052','Suplemento Whey Protein 900g','Suplemento concentrado sabor baunilha',110.00,149.00,30,5,3,4,NULL,1),(12,'7891020304053','Polivitamínico Centrum Todo Dia','Suplemento vitamínico completo',55.00,85.50,40,8,3,4,NULL,1),(13,'7891020304054','Aparelho de Pressão Digital Omron','Monitor de pressão arterial de pulso',130.00,189.90,15,3,6,4,NULL,1),(14,'7891020304146','Protetor Solar Sundown FPS 50','Protetor solar corporal 200ml',32.00,49.90,45,8,2,4,NULL,1),(15,'7891020304153','Desodorante Rexona Clinical Men','Antitranspirante aerosol masculino 150ml',11.50,18.90,90,15,2,4,NULL,1),(16,'7891020304160','Creme Dental Colgate Total 12','Creme dental proteção total 90g',4.50,7.90,200,30,2,4,NULL,1),(17,'7891020304177','Enxaguante Bucal Listerine 500ml','Antisséptico bucal sabor menta leve',13.00,21.50,65,10,2,4,NULL,1),(18,'7891020304184','Shampoo Pantene Restauração 400ml','Shampoo para cabelos danificados',12.50,19.80,55,12,2,4,NULL,1),(19,'7891020304191','Condicionador Pantene 400ml','Condicionador para cabelos danificados',14.00,22.10,50,12,2,4,NULL,1),(20,'7891020304207','Creme Hidratante Cerave 454g','Creme hidratante corporal pele seca',75.00,99.90,18,5,2,4,NULL,1),(21,'7891020304214','Algodão em Bola Apolo 100g','Algodão hidrófilo em bolas macias',5.00,8.50,110,20,2,4,NULL,1),(22,'7891020304220','Termômetro Digital G-Tech','Termômetro clínico digital corporal',15.00,29.90,100,15,6,4,NULL,1),(23,'7891020304237','Inalador Compressor Nebulizador','Aparelho nebulizador residencial',105.00,159.00,12,2,6,4,NULL,1),(24,'7891020304244','Protetor Labial Nivea Med Repair','Protetor labial hidratante FPS 15',12.00,19.90,80,20,2,4,NULL,1),(25,'7891020304251','Sabonete Líquido Protex Vitaminas','Sabonete líquido antibacteriano refil',9.50,14.50,120,25,2,4,NULL,1),(26,'7891020304268','Creatina Monohidratada 300g','Suplemento creatina pura',65.00,90.00,25,5,3,4,NULL,1),(27,'7891020304275','Vitamina C 1g Cenevit 30 Pastilhas','Suplemento alimentar de Vitamina C',14.00,22.40,60,15,3,3,NULL,1),(28,'7891020304282','Colágeno Hidrolisado Verisol','Suplemento em pó para firmeza da pele',80.00,110.00,20,4,3,3,NULL,1),(29,'7891020304299','Tira de Glicemia Accu-Chek 50 Un','Tiras reagentes para medição',52.00,74.90,35,10,6,3,NULL,1),(30,'7891020304305','Curativo Antisséptico Band-Aid 40 Un','Curativos tradicionais protetores',7.50,12.80,150,30,6,4,NULL,1);
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
  `perfil` varchar(20) NOT NULL,
  `ativo` tinyint(1) DEFAULT '1',
  `token_recuperacao` varchar(255) DEFAULT NULL,
  `token_expiracao` datetime DEFAULT NULL,
  `tentativas_falhas` int DEFAULT '0',
  `tentativas_falhas_totais` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `login` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Carlos Diretoria','carlos.admin@farmacia.com','carlos.admin','62a53050b4d4a7f...hash','ADMIN',1,NULL,NULL,0,0),(2,'Marina Gerente','marina.ger@farmacia.com','marina.ger','62a53050b4d4a7f...hash','GERENTE',1,NULL,NULL,0,0),(3,'Lucas Caixa','lucas.cx@farmacia.com','lucas.cx','62a53050b4d4a7f...hash','CAIXA',1,NULL,NULL,0,0),(9,'Vitor Hugo Leitão','vhdol05@gmail.com','vitor.admin','62a53050b4d4a7f...hash','ADMIN',1,NULL,NULL,0,0),(10,'Michel Hilario','michelhluce@gmail.com','michel.admin','62a53050b4d4a7f...hash','ADMIN',1,NULL,NULL,0,0);
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
  `data_venda` datetime DEFAULT CURRENT_TIMESTAMP,
  `valor_total` decimal(10,2) NOT NULL,
  `forma_pagamento` varchar(50) DEFAULT NULL,
  `status_pagamento` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `cliente_id` (`cliente_id`),
  CONSTRAINT `vendas_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `vendas_ibfk_2` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vendas`
--

LOCK TABLES `vendas` WRITE;
/*!40000 ALTER TABLE `vendas` DISABLE KEYS */;
INSERT INTO `vendas` VALUES (1,1,1,'2026-06-01 16:55:48',25.50,'PIX','CONCLUIDO'),(2,1,2,'2026-06-01 16:55:48',35.00,'CREDIT_CARD','CONCLUIDO');
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

-- Dump completed on 2026-06-03 13:41:12
