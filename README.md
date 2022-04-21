# Projeto Final PB
Projeto final desenvolvido com base em todo o aprendizado em Java do Programa de bolsas da Compass.oul, para avaliação do desenvolvimento e aprimoramento das habilidade no BackEnd java. Projeto desenvolvido em AEM na instancia Author.

--------------------------
Própositos
--------------------------
	Projeto foi desenvolvido a fim de mostrar todo o desenvolvimento e aprendizado dos Bolsistas BackEnd em java. 
	O Projeto tem como objetivo o desenvolvimento de um catalogo de produtos no padrão REST utilizando a tecnologia de servlets do Sling.
	
--------------------------
Banco De Dados
--------------------------
	
	
	Utilizar o banco de dados mySQL com a database "aem".
	usuario = root;
	senha = suaSenhaSql;
	
	Tabelas:
	 CREATE TABLE `categories` (
	  `id` int NOT NULL AUTO_INCREMENT,
	  `name` varchar(255) NOT NULL,
	  PRIMARY KEY (`id`)
	 );
	 
	 CREATE TABLE `product`(
	  `id` int NOT NULL AUTO_INCREMENT,
	  `name` varchar(255) NOT NULL,
	  `description` text NOT NULL,
	  `price` decimal(10,2) NOT NULL,
	  PRIMARY KEY (`id`)
	);
	
	CREATE TABLE `client` (
	  `id` int NOT NULL AUTO_INCREMENT,
	  `name` varchar(255) NOT NULL,
	  PRIMARY KEY (`id`)
	 );
	 
	CREATE TABLE `product_category`(
	  `product_id` int,	
	  `category_id` int,
	  foreign key(`product_id`)references product(`id`),
	  foreign key(`category_id`)references categories(`id`)
	 );
	 
	CREATE TABLE `invoice` (
	  `invoice_id` int NOT NULL AUTO_INCREMENT,
	  `invoice_date` TIMESTAMP NOT NULL,
	  `invoice_value` DECIMAL(10,2) NOT NULL,
	  `client_id` INT NOT NULL,
	  PRIMARY KEY (`invoice_id`)
	);
	
	CREATE TABLE invoice_product(
	  `invoice_id` int,
    	  `product_id` int,
    	  foreign key(`invoice_id`)references invoice(`invoice_id`),
   	   foreign key(`product_id`)references product(`id`)
	 );
	 

Após a configuração das tabelas pode-se iniciar os testes nos endpoints criados

--------------------------
EndPoints
--------------------------

	#Categorias - Retorno json
	www.localhost:4502/bin/keepalive/categoryService - POST, GET, DELETE e PUT 
	
	#Produtos - Retorno json
	www.localhost:4502/bin/keepalive/productService - POST, GET, DELETE e PUT
	
	#Clientes - Retorno json
	www.localhost:4502/bin/keepalive/clientService - POST, GET, DELETE e PUT
	
	#Notas Fiscais - Retorno json
	www.localhost:4502/bin/keepalive/invoiceService - POST, GET, DELETE e PUT
	
	#Report de Produtos - Retorno HTML
	www.localhost:4502/bin/keepalive/product/user/report - GET

--------------------------
Como Utilizar
--------------------------
	
	Ao fazer uma Requisição para o catalogo "www.localhost:4502/bin/keepalive/productService" pode-se utilizar alguns filtros:
	'/priceDown/' ordena os preços do menor para o maior. 
	'/digiteOSeuTexto/' procura por palavras chaves dentro do catalogo.
	'/c"n"/' onde "n" representa um inteiro, procura por produtos com a categoria correspondente ao numero "n".
	Esses filtros podem ser usados em conjunto e em qualquer ordem exemplo:
	www.localhost:4502/bin/keepalive/productService/c1/roupa/priceDown
	
	Ao consultar sem nenhum parametro o programa apresentara a lista completa de produtos.
	E com apenas um parametro numerico, trará o produto correspondente aquele Id.
	
