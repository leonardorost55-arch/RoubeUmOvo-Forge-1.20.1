# Roube um Ovo — Minecraft Java 1.20.1 (Forge)

Mod inspirado em **Roube um Ovo / Steal An Egg**, com visual Minecraft e detalhes dourados.

## Requisitos

- Minecraft Java Edition 1.20.1
- Minecraft Forge 47.x (configuração atual usa 47.2.0)
- Java 17
- Gradle 8.8

## Funcionalidades no código atual

- Estrutura de projeto Forge 1.20.1.
- Inventário com moldura dourada, aberto ao pressionar **E**, preservando os slots reais do inventário vanilla.
- Seis itens de ovo registrados: Comum, Raro, Épico, Lendário, Mítico e Secreto.
- Aba criativa do mod para acessar os ovos.
- Mecânica inicial: agache e clique com o botão direito em uma galinha para roubar um Ovo Comum. Há um cooldown curto para impedir repetição imediata.

## Ainda em desenvolvimento

- Texturas PNG finais dos ovos e da interface.
- Galinhas guardiãs que perseguem o jogador, ninhos e retorno/queda do ovo.
- Chocagem com temporizador e pets colecionáveis.
- Bases com slots de pets e geração de dinheiro.
- Loja, melhorias, fusão, índice e salvamento de progressão.
- Biomas/regiões especiais, armadilhas e eventos.
- Interface completa com dinheiro, velocidade, ovo carregado e botões próprios.

## Compilar

Com Java 17 e acesso à internet:

```bash
gradle build
```

Para abrir o cliente de desenvolvimento:

```bash
gradle runClient
```

O arquivo JAR deverá ser gerado em `build/libs/` caso a compilação termine sem erros. Copie o JAR para a pasta `mods` de uma instalação Forge 1.20.1.

**Aviso:** a compilação ainda precisa ser confirmada por uma execução real do build. As funcionalidades listadas como em desenvolvimento não estão implementadas.

## Licença

MIT. Veja `LICENSE`.
