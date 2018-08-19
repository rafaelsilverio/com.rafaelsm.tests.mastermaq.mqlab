# Solução
A solução completa do problema proposto se encontra no arquivo Documentacao_Desafio_MQLab.pdf, que referencia o arquivo Anexo1_Arquitetura_Completa.jpg.
O arquivo mastermaq.mqlab.postman_collection.json é uma coleção gerada pelo software POSTMAN com todas as chamadas do micro serviço que foi implementado.

# Implementação item 1 do backlog
Para rodar a aplicação é necessário java8 e maven.

Para executar o micro serviço Movimentação Financeira (financial-transaction), na pasta /microservices/financial-transaction-service execute:
```
$ mvn spring-boot:run
```
Para executar os testes, na mesma pasta, execute:
```
$ mvn test
```
# End-Points disponíveis
```
POST http://localhost:8080/rest/financial-transaction
```
Insere uma movimentação financeira
```
PUT http://localhost:8080/rest/financial-transaction/{id}
```
Edita uma movimentação financeira
```
DELETE http://localhost:8080/rest/financial-transaction/{id}
```
Remove uma movimentação financeira
```
GET http://localhost:8080/rest/financial-transaction/all/{cnpj}
```
Pesquisa por todas movimentações financeiras de um cnpj
```
GET http://localhost:8080/rest/financial-transaction/{id}
```
Pesquisa por uma movimentação financeira por id
```
GET http://localhost:8080/rest/financial-transaction/report/{cnpj}/{month}/{year}
```
Pesquisa por todas movimentações financeiras de um cnpj, para um determinado mês de um ano

Não foi possível implementar o End-Point exportação por arquivo devido a tempo. Softwares externos poderiam requisitar as movimentações de uma empresa através do End-Point report nessa primeira entrega.

