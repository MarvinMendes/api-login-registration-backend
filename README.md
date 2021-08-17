# **Este é um projeto para criação de login utilizando Spring Boot**

## Utilizei no projeto
* **Java-11**
* **Spring Boot**
* **Spring Security**
* **JPA**
* **Java Mail**
* **Verificação de email com espiração**

### Diagrama
![imagem-diagrama](https://github.com/MarvinMendes/api-login-registration-backend/blob/master/src/main/java/com/marvin/apiuserregisteremailsender/images/diagrama.png)
### Este é um exemplo do Json que você pode usar para criar um Person com o POST!

```
{ 
    "firstName": "Marvin",
    "lastName": "Mendes L",
    "email": "marvin@email.com",
    "password": "swordfish"
}
```

### Email de verificação utilizando o MailDev
![imagem-email](https://github.com/MarvinMendes/api-login-registration-backend/blob/master/src/main/java/com/marvin/apiuserregisteremailsender/images/confirm.png)

### No postman deve ser passado nos parâmetros da URL o token gerado após o POST

![imagen-postman](https://github.com/MarvinMendes/api-login-registration-backend/blob/master/src/main/java/com/marvin/apiuserregisteremailsender/images/postman.png)



