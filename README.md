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
	
	CREATE TABLE `user` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`username` varchar(255) NOT NULL,
	`password` varchar(255) NOT NULL,
	PRIMARY KEY (`id`)
	);

	
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
	 
	INSERT INTO users (id, username, password) VALUES (1,'admin','admin');
	 	
	INSERT INTO categories (ID, NAME) VALUES (1,'Acessórios');

	INSERT INTO product (id, name, description, price) VALUES (1,'Produto de Teste','Teste Do Produto',1500.0);

	INSERT INTO product_category (product_id, category_id) VALUES (1,1);

	INSERT INTO client (id, name) VALUES (1,'Client');

	INSERT INTO invoice (invoice_id, invoice_date, invoice_value, client_id) VALUES (1,'2022-4-20 14:27:32.00',1500.0,1);

	INSERT INTO invoice_product (invoice_id, product_id) VALUES (1,1);
	 

Após a configuração das tabelas pode-se iniciar os testes nos endpoints criados

--------------------------
EndPoints
--------------------------

	#User
	www.localhost:4502/bin/keepalive/adminService - POST, GET, DELETE e PUT 

	#Login
	www.localhost:4502/bin/keepalive/adminServlet - POST, DELETE

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


# Como Utilizar

	
	
## Login
	
	------------------------------------------
	localhost:4502/bin/keepalive/LoginServlet
	------------------------------------------
	
Payload

	{
	    "username":"admin",
	    "password":"admin"
	}

POST 
- Loga o Usuário passado na payload

	- `Parâmetros`: Nenhum.
	
	- `Payload`: Username e senha em formato Json no corpo da requisição.

DELETE 

- Desloga o Usuário atual se houver algum logado

	- `Parâmetros`: Nenhum.

	- `Payload`: Nenhum.

##

## AdminService
#### Login Necessário 

	
	------------------------------------------
	localhost:4502/bin/keepalive/adminService
	-----------------------------------------
	
Payload
	
	{
	"username":"Teste",
	"password":"Teste"
	}

POST 
- Adiciona um novo Usuário ao sistema.

	- `Parâmetros`: Nenhum.
	
	- `Payload`: usuário e senha em formato Json no corpo da requisição com exceção id.

GET
- Recupera a lista de Usuários cadastrados no sistema (Sem Senha).
	- `Parâmetros`: 
		- Opcional 
			- userId='id'.
	
	- `Payload`: Nenhum.

DELETE 
- Deleta um Usuário.

	- `Parâmetros`: Obrigatório - userId='id'.

	- `Payload`: Nenhum.

PUT
- Atualiza um Usuário escolhido.
	- `Parâmetros`: Nenhum.

	- `Payload`: Todos os campos.

##

## CategoryService
#### Login Necessário 

	
	--------------------------------------------
	localhost:4502/bin/keepalive/categoryService
	--------------------------------------------
Payload
	
	{
	    "name":"Teste"
	}
	
POST 
- Adiciona uma nova categoria.

	- `Parâmetros`: Nenhum.
	
	- `Payload`: nome da categoria em formato Json no corpo da requisição com exceção id.

GET
- Recupera a lista de categorias.
	-  `Parâmetros`: 
		-  Opcional 
			- categoryId='id'.
	
	- `Payload`: Nenhum.

DELETE 
- Deleta uma categoria.

	- `Parâmetros`: 
		- Obrigatório 
			- categoryId='id'.

	- `Payload`: Nenhum.

PUT
- Atualiza uma categoria escolhida.
	- `Parâmetros`: Nenhum.

	- `Payload`: Todos os campos.

##

## ProductService
#### Login Necessário 

	
	--------------------------------------------
	localhost:4502/bin/keepalive/productService
	--------------------------------------------
	
Payload

	[
	    {
	    "name": "Teste",
	    "categoryId": [
		1
	    ],
	    "description": "Teste",
	    "price":150.0
	    }
	]

POST 
- Adiciona um novo Produto.

	- `Parâmetros`: Nenhum.
	
	- `Payload`: Todos os campos em formato Json no corpo da requisição com exceção id.

GET
- Recupera a lista de Produtos.
	- `Parâmetros`: 
		- Opcionais:
			- productId='id' - Recupera um único produto. (único)
			- categoryId='categoryId' - Recupera todos os produtos com determinada categoria. (Combinavel)
			- priceDown - Ordena do menor preço para o maior. (Combinavel)
			- word='palavra-chave' - Recupera todas os produtos com essa palavra chave. (Combinavel)
	
	- `Payload`: Nenhum.

DELETE 
- Deleta um Produto.

	- `Parâmetros`: 
		- Obrigatório 
			- productId='id'.
		-O produto não será deletado se já estiver em uma nota fiscal. 	

	- `Payload`: Nenhum.

PUT
- Atualiza um Produto escolhido.
	- `Parâmetros`: Nenhum.

	- `Payload`: Todos os campos.

##

## ClientService
#### Login Necessário 

	
	--------------------------------------------
	localhost:4502/bin/keepalive/clientService
	--------------------------------------------
	
Payload

	{
	    "name":"André Monteiro"
	}

POST 
- Adiciona um novo Cliente.

	- `Parâmetros`: Nenhum.
	
	- `Payload`: Todos os campos em formato Json no corpo da requisição com exceção do id.

GET
- Recupera a lista de Clientes.
	- `Parâmetros`: 
		- Opcional:
			- clientId='id' - Recupera um único Cliente.
			
	- `Payload`: Nenhum.

DELETE 
- Deleta um Cliente.

	- `Parâmetros`: 
		- Obrigatório 
			- clientId='id'.
		-O cliente não será deletado se já estiver em uma nota fiscal. 	

	- `Payload`: Nenhum.

PUT
- Atualiza um Cliente escolhido.
	- `Parâmetros`: Nenhum.

	- `Payload`: Todos os campos.

##

## InvoiceService
#### Login Necessário 

	
	--------------------------------------------
	localhost:4502/bin/keepalive/invoiceService
	--------------------------------------------
	
Payload

	{
	    "invoiceDate":"YYYY-mm-ddTHH:mm:ss.SSSX",
	    "invoiceItens":[
		1
	    ],
	    "invoiceValue":150.0,
	     "clientId": 1
	}

POST 
- Adiciona uma nova Nota Fiscal.

	- `Parâmetros`: Nenhum.
	
	- `Payload`: Todos os campos em formato Json no corpo da requisição com exceção do id.

GET
- Recupera a lista de Notas Fiscais.
	- `Parâmetros`: 
		- Opcional:
			- clientId='id' - Recupera um único Cliente.
			
	- `Payload`: Nenhum.

DELETE 
- Deleta uma Nota Fiscal.

	- `Parâmetros`: 
		- Obrigatório 
			- clientId='id'.
		- O cliente não será deletado se já estiver em uma nota fiscal. 	

	- `Payload`: Nenhum.

PUT
- Atualiza uma Nota Fiscal escolhida.
	- `Parâmetros`: Nenhum.

	- `Payload`: Todos os campos.

##

## ReportService
#### Login Necessário 

	
	------------------------------------------------
	localhost:4502/bin/keepalive/product/report
	-------------------------------------------------
	
	

GET
- Recupera o relatório de todos os produtos comprados por um determinado Cliente.
	- `Parâmetros`: 
		- Obrigatório:
			- userId='id'.
			
	- `Payload`: Nenhum.


#

# Sample AEM project template

This is a project template for AEM-based applications. It is intended as a best-practice set of examples as well as a potential starting point to develop your own functionality.

## Modules

The main parts of the template are:

* core: Java bundle containing all core functionality like OSGi services, listeners or schedulers, as well as component-related Java code such as servlets or request filters.
* it.tests: Java based integration tests
* ui.apps: contains the /apps (and /etc) parts of the project, ie JS&CSS clientlibs, components, and templates
* ui.content: contains sample content using the components from the ui.apps
* ui.config: contains runmode specific OSGi configs for the project
* ui.frontend: an optional dedicated front-end build mechanism (Angular, React or general Webpack project)
* ui.tests: Selenium based UI tests
* all: a single content package that embeds all of the compiled modules (bundles and content packages) including any vendor dependencies
* analyse: this module runs analysis on the project which provides additional validation for deploying into AEMaaCS

## How to build

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

To build all the modules and deploy the `all` package to a local instance of AEM, run in the project root directory the following command:

    mvn clean install -PautoInstallSinglePackage

Or to deploy it to a publish instance, run

    mvn clean install -PautoInstallSinglePackagePublish

Or alternatively

    mvn clean install -PautoInstallSinglePackage -Daem.port=4503

Or to deploy only the bundle to the author, run

    mvn clean install -PautoInstallBundle

Or to deploy only a single content package, run in the sub-module directory (i.e `ui.apps`)

    mvn clean install -PautoInstallPackage

## Testing

There are three levels of testing contained in the project:

### Unit tests

This show-cases classic unit testing of the code contained in the bundle. To
test, execute:

    mvn clean test

### Integration tests

This allows running integration tests that exercise the capabilities of AEM via
HTTP calls to its API. To run the integration tests, run:

    mvn clean verify -Plocal

Test classes must be saved in the `src/main/java` directory (or any of its
subdirectories), and must be contained in files matching the pattern `*IT.java`.

The configuration provides sensible defaults for a typical local installation of
AEM. If you want to point the integration tests to different AEM author and
publish instances, you can use the following system properties via Maven's `-D`
flag.

| Property | Description | Default value |
| --- | --- | --- |
| `it.author.url` | URL of the author instance | `http://localhost:4502` |
| `it.author.user` | Admin user for the author instance | `admin` |
| `it.author.password` | Password of the admin user for the author instance | `admin` |
| `it.publish.url` | URL of the publish instance | `http://localhost:4503` |
| `it.publish.user` | Admin user for the publish instance | `admin` |
| `it.publish.password` | Password of the admin user for the publish instance | `admin` |

The integration tests in this archetype use the [AEM Testing
Clients](https://github.com/adobe/aem-testing-clients) and showcase some
recommended [best
practices](https://github.com/adobe/aem-testing-clients/wiki/Best-practices) to
be put in use when writing integration tests for AEM.

## Static Analysis

The `analyse` module performs static analysis on the project for deploying into AEMaaCS. It is automatically
run when executing

    mvn clean install

from the project root directory. Additional information about this analysis and how to further configure it
can be found here https://github.com/adobe/aemanalyser-maven-plugin

### UI tests

They will test the UI layer of your AEM application using Selenium technology. 

To run them locally:

    mvn clean verify -Pui-tests-local-execution

This default command requires:
* an AEM author instance available at http://localhost:4502 (with the whole project built and deployed on it, see `How to build` section above)
* Chrome browser installed at default location

Check README file in `ui.tests` module for more details.

## ClientLibs

The frontend module is made available using an [AEM ClientLib](https://helpx.adobe.com/experience-manager/6-5/sites/developing/using/clientlibs.html). When executing the NPM build script, the app is built and the [`aem-clientlib-generator`](https://github.com/wcm-io-frontend/aem-clientlib-generator) package takes the resulting build output and transforms it into such a ClientLib.

A ClientLib will consist of the following files and directories:

- `css/`: CSS files which can be requested in the HTML
- `css.txt` (tells AEM the order and names of files in `css/` so they can be merged)
- `js/`: JavaScript files which can be requested in the HTML
- `js.txt` (tells AEM the order and names of files in `js/` so they can be merged
- `resources/`: Source maps, non-entrypoint code chunks (resulting from code splitting), static assets (e.g. icons), etc.

## Maven settings

The project comes with the auto-public repository configured. To setup the repository in your Maven settings, refer to:

    http://helpx.adobe.com/experience-manager/kb/SetUpTheAdobeMavenRepository.html
