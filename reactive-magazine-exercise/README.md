# Reactive Magazine Publisher

## Caso de Uso

Suponha um Editor de Revistas que possui 2 tipos de assinantes: um consumidor 
rápido, que lê as revistas na velocidade em que são entregues e um outro lento, 
que recebe muito mais revistas do que sua capacidade de ler. 

O editor tem uma capacidade de produzir 20 revistas por assinantes.

O editor sabe que seus leitores não costumam ficar em casa no horário de entrega 
e sabem que seus assinantes tem uma caixa de correio pequena. 

Para  evitar que o entregador devolva as revistas ou as jogue fora, o editor 
planeja implementar um sistema de entrega inovador: os assinantes solicitam a 
entrega quando estão em casa e, nesse caso, a entrega é feita (a próxima para 
esse assinante) quase imediatamente. 

O editor planeja manter uma pequena caixa por assinante no escritório, caso 
alguns deles não liguem para receber a revista assim que ela for publicada. 
O gerente da editora acha que manter um espaço de até 8 revistas por assinante 
no escritório é mais do que suficiente.

Um dos funcionários da editora trouxe a seguinte questão para o gerente da editora.

Se os assinantes forem rápidos o suficiente para solicitar uma entrega, ou pelo 
menos tão rápido quanto o editor estiver imprimindo novas revistas, não haverá 
problemas de espaço.

No entanto, se os assinantes não estiverem ligando para a editora no mesmo ritmo 
em que as revistas são impressas, as caixas podem ficar cheias. 
Nesse caso, como resolver?

1. Incrementa para 20 o tamanho das caixas de cada assinante (ou seja, mais 
recursos no lado do editor).
2. Interrompe a impressão até que a situação seja corrigida (o assinante solicitar 
pelo menos uma revista) e, em seguida, diminua a velocidade de impressão, em 
detrimento de alguns assinantes que podem ser rápidos o suficiente para manter 
suas caixas vazias.
3.Descarta qualquer revista que não caiba na caixa de assinantes imediatamente 
após sua produção.
4. Se alguma das caixas estiver cheia, aguarde antes de imprimir o próximo 
número por um período máximo de tempo. Se, após esse período, ainda não houver 
espaço, descarta o novo número para o assinante que não tiver mais espaço 
disponível no escritório.


## Exercício 1

Qual das opções acima você escolheria se fosse o gerente da editora?

## Exercício 2

Crie a classe MagazineSubscriber que representa um assinante das revistas. 
Essa classe deve implementar a interface Flow.Subscriber<Integer>. 

Note que usaremos um consumidor de valores do tipo Integer para representar os 
números das revistas.

A criação de um novo assinante deve receber o nome do assinante e um tempo que 
usaremos para simular a "velocidade" com a qual o assinante requisita novas revistas.

* Ao fazer uma assinatura (onSubscribe), o assinante já requisita 1 revista.
* A cada  nova revista produzida (onNext), o assinante deve verificar se perdeu 
alguma revista, considerando a última que recebeu e, nesse caso, emitir um alerta na console. 
Uma nova revista só é requisitada após o assinante "descansar" pelo tempo definido na sua criação.
* No caso de erro (onError), o assinante deve apenas emitir um alerta na console.
* No caso do produtor não possuir mais revistas para serem entregues (onComplete), 
o assinante deve escrever na console quantas revistas recebeu no total. 

A classe MagazineApplication implementa um produtor com seus dois assinantes, 
ilustrando 3 casos diferentes:

**Caso 1**: Assinantes são rápidos e, portanto, o tamanho do buffer não é tão 
importante nesse caso
**Caso 2**: Um assinante lento mas um bom tamanho de buffer para garantir a 
entrega de todas as revistas
**Caso 3**: Um assinante lento e um buffer limitado.




