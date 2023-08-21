
# Compass Classrooms - API
### Segundo desafio

#### PT BR
O sistema consiste na criacão de um gerenciador de turmas, abstraindo o funcionamento e organizacão do programa de bolsas da Compass.uol™. O sistema permite a criacão e gerenciamento de turmas, instrutores, scrum masters e estudantes. Há a possibiliade de agrupar os estudantes em squads e atribuir notas para os mesmo.

#### EN US
The system consists of the creation of a classroom manager, abstracting the operation and organization of the Compass.uol™ scholarship program. The system allows the creation and management of classes, instructors, scrum masters and students. There is the possibility to group students into squads and assign grades to them.



## Documentation

#### Swagger

```http
  GET /swagger-ui/index.html
```

#### Free resources

```http
  POST /auth/register
```

| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `firstName`      | `string` | **Mandatory**. user`s first name |
| `lastName`      | `string` | **Mandatory**. user`s last name |
| `email`      | `string` | **Mandatory**. user`s email/username |
| `password`      | `string` | **Mandatory**. user`s password |

whil create a user for first access

---

```http
  POST /auth/signin
```

| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `username`      | `string` | **Mandatory**. user`s email/username |
| `password`      | `string` | **Mandatory**. user`s password |

whil return a jwt token, to authenticate any other protected resources

---



## Run locally

Clone the project

```bash
  git clone https://github.com/ThiagoHenriqueFP/cspc-api
```

Move to project directory

```bash
  cd cspc-api
```

Make sure your mysql database ir running and your application.properties is rigth

Create a database called 'scholarship_db'

**we recomend to open an IDE to run maven**

Launch the application inside an IDE (Intellij, Eclipse etc)


## Autores

- [@DaviVerissimo](https://www.github.com/DaviVerissimo)
- [@Gablier-R](https://www.github.com/Gablier-R)
- [@MakVinicius](https://www.github.com/MakVinicius)
- [@ThiagoHenriqueFP](https://www.github.com/ThiagoHenriqueFP)
