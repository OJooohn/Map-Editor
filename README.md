# Map Editor

## Descrição do Projeto
O programa Map Editor, é uma ferramente em Java que permite o usuário criar | editar mapa com uma fonte ASCII personalizada
do nosso jogo Crypt Crawler! O principal objetivo é fornecer uma interface visual interativa, que seja fácil de usar e manipular
o mapa, do jeito que você quiser.

## Objetivos
- Facilitar a criação e edição de mapa
- Permitir utilizar cores em cada elemento do mapa
- Distribuir uma interface intuitiva

## Principais Funções
- Carregar e salvar mapas
- Edição de mapas usando caracteres em ASCII com personalização de cores
- Funções que ajudam ao editar | criar, como a criação fácil de quadriláteros
- Interface gráfica intuitiva

## Classes

### MapEditor
**Descrição**: A classe principal do projeto, que faz o gerenciamento da interface gráfica

**Relações**:
- Contém uma instância de `AsciiPanel` para a renderização do mapa.
- Interage com a classe `World` para salvar e carregar o estado do mapa.

### AsciiPanel
**Descrição**: Classe responsável por renderizar os caracteres em ASCII do mapa, na interface

**Relações**:
- Utilizada pela `MapEditor`

### World
**Descrição**: Classe responsável por representar o estado do mapa (mundo), que inclui as matrizes de caracteres e cores

**Relações**:
- A clasee `MapEditor` pode criar ou manipular instâncias da classe, para salvar e carregar mapas

## Como Executar o Projeto

### Requisitos
- Java Development Kit (JDK) 8 ou superior
- Ambiente de Desenvolvimento Integrado (IDE) como IntelliJ IDEA ou Eclipse

### Método 1
1. Baixar o [arquivo .zip](https://github.com/OJooohn/Map-Editor/raw/main/MapEditor.zip) e extrair para sua Área de Trabalho
2. Na Área de Trabalho, execute o arquivo `mapEditor.jar`

### Método 2
1. Clone o repositório do projeto para sua máquina local.
    ```sh
    git clone https://github.com/OJooohn/map-editor.git
    ```
2. Abra o projeto na sua IDE de preferência.
3. Compile o projeto para garantir não haver erros.
4. Execute a classe principal `MapEditor.java` para iniciar o programa.

## Uso do ChatGPT 4o

O ChatGPT foi usado primeiramente, para um experimento de capacidade da Inteligência Artificial.
Após fazer a estrutura inicial do código, onde tinha apenas os botões com os caracteres não utilizados do jogo, e um preview do mapa,
o ChatGPT foi utilizado para:
- Fornecer exemplos de como implementar funcionalidades específicas (Mouse Listener, Mouse Released, Personalizar JPanel, etc...)
- Auxiliar no entendimento de variáveis novas (Path, File, Map<String, Color>)
- Ajudar com erros de compilação e melhoria de lógica de programação

A utilização de uma Inteligência Artificial permitiu acelerar o processo do desenvolvimento da ferramente, para 
melhorar a criação e edição de mapa, visto que seria muito mais demorado escrever linhas de código para criar um mapa,
também melhorando a interatividade entre o usuário e o jogo, pois o usuário pode criar o seu próprio mapa.

## Referências e Recursos

### Bibliotecas Externas Usadas
- **AsciiPanel:** Biblioteca que simula um terminal em ASCII
- **OBS:** A biblioteca foi editada para adicionar novas fontes

### Recursos Adicionais
- **Java AWT e Swing:** Utilizadas para criar a interface gráfica do usuário.
- **Fontes:** Fonte `SpaceMono-Regular.ttf` utilizada para estilização do texto na interface.

### Link Úteis
- [AsciiPanel GitHub Repository](https://github.com/trystan/AsciiPanel): Repositório oficial da biblioteca AsciiPanel.
- [Documentação Oficial do Java](https://docs.oracle.com/en/java/): Referência para a API do Java e guias de desenvolvimento.

## Comandos interface

### Carregar Mapa
- Certifique-se que na pasta `User/Desktop/World` tenha o arquivo `world.save`
- Aperte R para carregar o mapa para o editor

### Salvar Mapa
- Aperte a tecla ENTER para salvar o mapa no diretório: `User/Desktop/World/world.save`

### Mover Mapa
- W: subir a pré-visualização do mapa
- A: Mover à esquerda a pré-visualização do mapa
- S: Descer a pré-visualização do mapa
- D: Mover à direita a pré-visualização do mapa

### Mudar Borda do Mapa
- Por padrão o mapa vem com uma borda
- Para mudar, selecione o caractere com as cores desejadas e aperte a tecla Q

### Desenhar no Mapa
- Botão Esquerdo do Mouse: adicionar caractere na posição (X, Y) do mapa
- Botão Direito do Mouse: remover caractere na posição (X, Y) do mapa

#### Truques
- Ao segurar o Botão Esquerdo do Mouse e soltar em outra posição, irá criar um quadrilátero somente com as bordas do caractere selecionado
- Se quiser que o quadrilátero seja preenchido, segure a tecla CTRL ao arrastar o mouse
- Ao segurar o Botão Direito do Mouse e soltar em outra posição, irá excluir todos os caracteres entre as posições (X, Y) inicial e (X, Y) final
