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
- Perseguição da guardiã: a galinha roubada persegue o jogador por até 15 segundos, enquanto ele permanece a até 24 blocos. A perseguição termina se o jogador escapar, morrer/desconectar ou o tempo acabar.\n- Chocagem inicial: use qualquer ovo do mod para criar um pet galinha com nome colorido pela raridade.\n- Economia inicial: cada pet gera 1 moeda por minuto; o saldo fica salvo nos dados persistentes do jogador e é exibido ao receber moedas.
- Pets seguem o dono quando ficam a mais de 4 blocos e param para descansar perto de uma Base Dourada.
- Bloco Base Dourada: clique com o botão direito para estacionar até 4 pets próximos em slots lógicos persistentes; clique novamente para liberar os pets guardados. O alcance da base é de 8 blocos.

## Ainda em desenvolvimento

- Texturas PNG finais dos ovos e da interface.
- Ninhos e mecânicas de retorno/queda do ovo; a perseguição inicial da galinha já foi implementada.
- Chocagem com temporizador e pets colecionáveis.
- Interface visual com quatro slots individuais da Base Dourada; por enquanto, os quatro slots funcionam como estacionamento lógico de pets, sem menu gráfico próprio.
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

**Aviso:** as mecânicas de perseguição, chocagem e renda dos pets foram adicionadas em commits posteriores à primeira compilação confirmada. Consulte a aba Actions para verificar o resultado da compilação mais recente. As funcionalidades listadas como em desenvolvimento ainda não estão implementadas.

## Licença

MIT. Veja `LICENSE`.
