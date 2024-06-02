Para rodar o front-end, basta instalar a última lts do Node.js, entrar na pasta frontend pelo terminal e executar os comandos:

`corepack enable` (se já tiver yarn desconsiderar esse comando)

`yarn` e `yarn dev`

Para rodar o back-end, basta ter a SDK 21 do Java, abrir o projeto pelo IntelliJ, instalar as dependências e startar a aplicação (pode ser necessário trocar a URL do CORS no arquivo `LoanController.java` caso a porta do front-end seja diferente de `5173`).
