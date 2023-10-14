# TeamWorkTask

Projeto integrado de conclusão de curso - Pós-graduação Lato Sensu em Desenvolvimento Mobile (PONTIFÍCIA UNIVERSIDADE CATÓLICA DE MINAS GERAIS - PUC Minas Virtual)

# Introdução

Para auxiliar na elaboração destes projetos, iremos desenvolver uma ferramenta que irá ser a base para a elaboração de projetos, como um sistema de gerenciamento de projetos, que permitirá a equipe desenvolver as ideias, estrutura-las e unifica-las em um único espaço onde todos da equipe poderão ter acesso.

É sabido que existe outras ferramentas no mercado de gerenciamento de projeto, porém é necessário que os dados nele contidos sejam restritos a empresa em questão e sigilosos.

O sistema proposto tem como objetivo gerenciar projetos. Após um projeto ser criado pode-se convidar outros usuários que tenham cadastro na aplicação para fazer parte da equipe. Como uma equipe, serão criadas tarefas, e essas tarefas podem possuir os seguintes status: Aberta, Em desenvolvimento, em validação e concluída. Vale lembrar que toda tarefa criada tem por obrigatoriedade o status de “Aberto”, sendo assim somente após a criação da tarefa que é possível atualizar o status. É possível também atribuir a uma tarefa uma ou mais Tags para uma rastreabilidade maior.

Atualmente o projeto já está implementado na linguagem Java e acessível somente internamente na empresa, o que não condiz com o cenário atual do mercado de desenvolvimento, onde a maioria dos envolvidos estão a trabalhar a distância.

# O objetivo deste projeto será:

• O sistema Teamwork Tasks tem como objetivo principal ser um apoio as equipes no que tange a organização do trabalho, permitindo uma visão clara do escopo de um projeto;

• Sejam equipes profissionais ou amadoras, no caso de projetos pessoais, o sistema visa apoiar todos os tipos de projeto, através de compartilhamento de informações entre os integrantes das equipes;

• Tem como objetivo também ser um projeto de fácil implementação e manutenibilidade;

• Além disso, visa permitir a participação em outros projetos da mesma equipe ou de outra, e no caso de um projeto pessoal, poder compartilhar com outras pessoas através da função de participação que sistema possui;

• Alterar a arquitetura para permitir acesso as informações através de uma aplicação móvel;

• Por meio de uma aplicação móvel agilizar as tomadas de decisão;

• Utilizar os recursos dos dispositivos moveis para o compartilhamento de informações


# Padrão Arquitetural

Essa aplicação foi  desenvolvida em Android seguindo a linguagem Kotlin que é a linguagem recomendada pela Google para se desenvolver uma nova aplicação e foi usado o padrão de arquitetura MVVM.

Para o desenvolvimento foram usadas as seguintes tecnologias:

• Android Studio (IDE)

• Kotlin (Linguagem de programação)

• Retrofit (Rest Client onde iremos acessar e persistir os dados)

• livedata

• Binding

• Material Layout 3

