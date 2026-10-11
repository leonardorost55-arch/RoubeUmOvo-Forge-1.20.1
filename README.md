# Roube um Ovo — Minecraft Java 1.20.1 (Forge)

Mod inspirado no jogo **Roube um Ovo / Steal An Egg**, com visual Minecraft e detalhes dourados.

## Requisitos

- Minecraft Java Edition 1.20.1
- Forge 47.2.0 ou outra versão Forge 47 compatível
- Java 17
- Gradle (ou wrapper Gradle, quando incluído)

## Estado atual

- [x] Estrutura inicial do projeto Forge
- [x] Tela de inventário dourada aberta no lugar do inventário padrão ao pressionar **E**
- [x] Mantém os slots reais do inventário, armaduras e movimentação de itens
- [ ] Itens e ovos com raridades
- [ ] Galinhas guardiãs e mecânica de perseguição
- [ ] Chocagem de ovos e pets
- [ ] Bases que geram dinheiro
- [ ] Loja, melhorias, fusão e índice
- [ ] Biomas/regiões e eventos
- [ ] Texturas pixel-art completas e sons

> O inventário atual é a primeira fatia funcional da interface. As mecânicas listadas como pendentes ainda não estão implementadas.

## Compilar

Em um ambiente com Java 17 e acesso à internet:

```bash
gradle build
```

O JAR deverá aparecer em `build/libs/`. Para desenvolver no cliente, use `gradle runClient`.

## Instalação

Copie o JAR gerado para a pasta `mods` de uma instalação Minecraft 1.20.1 com Forge.

## Licença

MIT. Veja `LICENSE`.
