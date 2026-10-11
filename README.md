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
- Roubo de ovo: agache e clique com o botão direito em uma galinha para obter um Ovo Comum; há cooldown para impedir repetição imediata.
- Perseguição da guardiã: a galinha roubada persegue o jogador por até 15 segundos, enquanto ele permanece a até 24 blocos. A perseguição termina se o jogador escapar, morrer/desconectar ou o tempo acabar.

## Ainda em desenvolvimento

- Texturas PNG finais dos ovos e da interface.
- Ninhos e mecânicas de retorno/queda do ovo; a perseguição inicial da galinha já foi implementada.
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

**Aviso:** o GitHub Actions confirmou uma compilação bem-sucedida no commit `e26564349eca2404663266d1c74edceb4f039dc0`. A nova mecânica de perseguição foi adicionada depois desse commit e está aguardando sua própria compilação. As funcionalidades listadas como em desenvolvimento não estão implementadas.

## Licença

MIT. Veja `LICENSE`.
