# Ejemplo de informe de corrección

Fundamentos de Programación Funcional y Concurrente.
Documento realizado por el docente Juan Francisco Díaz.

## 1. Argumentar la corrección de programas recursivos

Sea $f : A \to B$ una función, y $A$ un conjunto definido recursivamente
(recordar la definición de Matemáticas Discretas I), como por ejemplo los
naturales o las listas.

Sea $P_f$ un programa recursivo (lineal o en árbol) desarrollado en Scala (o en
cualquier lenguaje de programación) hecho para calcular $f$:

```scala
def Pf(a: A): B = { // Pf recibe a de tipo A, y devuelve f(a) de tipo B
  ...
}
```

¿Cómo argumentar que $P_f(a)$ siempre devuelve $f(a)$ como respuesta? Es decir,
¿cómo argumentar que $P_f$ es correcto con respecto a su especificación?

La respuesta es sencilla: demostrando el siguiente teorema.

```math
\forall a \in A : P_f(a) == f(a)
```

Cuando uno tiene que demostrar que algo se cumple para todos los elementos de
un conjunto definido recursivamente, es natural usar inducción estructural. En
términos prácticos, esto significa demostrar que:

- Para cada valor básico $a$ de $A$, se tiene que $P_f(a) == f(a)$.
- Para cada valor $a \in A$ construido recursivamente a partir de otro(s)
  valor(es) $a' \in A$, se tiene que
  $P_f(a') == f(a') \rightarrow P_f(a) == f(a)$. (Esta es la hipótesis de
  inducción).

### Ejemplo: factorial recursivo

Sea $f : \mathbb{N} \to \mathbb{N}$ la función que calcula el factorial de un
número natural, es decir, $f(n) = n!$. Y sea $P_f$ el siguiente programa en
Scala:

```scala
def Pf(n: Int): Int = { // Pf recibe n de tipo Int, y devuelve n! de tipo Int
  if (n == 0) 1 else n * Pf(n - 1)
}
```

Vamos a demostrar que $\forall n \in \mathbb{N} : P_f(n) == n!$

**Caso base:** $n = 0$

```math
P_f(0) \rightarrow \text{if } (0 == 0)\ 1 \text{ else } 0 \ast P_f(-1) \rightarrow 1
```

Por otro lado, $f(0) = 0! = 1$. Entonces $P_f(0) == f(0)$.

**Caso de inducción:** $n = k + 1$, $k \geq 0$. Hay que demostrar:
$P_f(k) == f(k) \rightarrow P_f(k + 1) == f(k + 1)$

```math
P_f(k+1) \rightarrow \text{if } (k+1 == 0)\ 1 \text{ else } (k+1) \ast P_f(k) \rightarrow (k+1) \ast P_f(k)
```

Usando la hipótesis de inducción (HI):

```math
\rightarrow (k+1) \ast k! = (k+1)!
```

Por lo tanto, $P_f(k + 1) == f(k + 1)$.

Concluimos por inducción que $\forall n \in \mathbb{N} : P_f(n) == n!$

### Ejemplo: el máximo de una lista

Sea $f : \text{List}[\mathbb{N}] \to \mathbb{N}$ la función que calcula el
máximo de una lista de enteros positivos, no vacía. Y sea $P_f$ el siguiente
programa en Scala:

```scala
def maxLin(l: List[Int]): Int = {
  if (l.tail.isEmpty) l.head
  else math.max(maxLin(l.tail), l.head)
}
```

Demostraremos que:

```math
\forall n \in \mathbb{N} \setminus \{0\} : P_f(\text{List}(a_1, a_2, \ldots, a_n)) == f(\text{List}(a_1, a_2, \ldots, a_n))
```

**Caso base:** $n = 1$

```math
P_f(\text{List}(a_1)) \rightarrow \text{if } \text{List}(a_1).\text{tail.isEmpty then } \text{List}(a_1).\text{head else } \ldots \rightarrow \text{List}(a_1).\text{head} \rightarrow a_1
```

Por otro lado, $f(\text{List}(a_1)) = a_1$. Entonces
$P_f(\text{List}(a_1)) == f(\text{List}(a_1))$.

**Caso de inducción:** $n = k + 1$, $k \geq 1$. Se debe demostrar:

```math
P_f(\text{List}(b_1, b_2, \ldots, b_k)) == f(\text{List}(b_1, b_2, \ldots, b_k)) \rightarrow P_f(\text{List}(a_1, a_2, \ldots, a_{k+1})) == f(\text{List}(a_1, a_2, \ldots, a_{k+1}))
```

Empecemos por calcular qué devuelve $P_f$ usando el modelo de sustitución:

```math
P_f(L) \rightarrow \text{if } L.\text{tail.isEmpty then } L.\text{head else math.max}(P_f(L.\text{tail}), L.\text{head})
```

```math
\rightarrow \text{math.max}(P_f(\text{List}(a_2, \ldots, a_{k+1})), a_1)
```

Sea $b = P_f(\text{List}(a_2, \ldots, a_{k+1}))$; por la hipótesis de
inducción, $b = f(\text{List}(a_2, \ldots, a_{k+1}))$. Hay dos posibilidades:

- Si $\text{math.max}(b, a_1) = b$, entonces $b \geq a_1$ y
 $b == f(\text{List}(a_1, a_2, \ldots, a_{k+1}))$.
- Si $\text{math.max}(b, a_1) = a_1$, entonces $a_1 \geq b$ y
 $a_1 == f(\text{List}(a_1, a_2, \ldots, a_{k+1}))$.

Por lo tanto, $P_f(L) == f(L)$.

Concluimos por inducción que:

```math
\forall n \in \mathbb{N} \setminus \{0\} : P_f(\text{List}(a_1, a_2, \ldots, a_n)) == f(\text{List}(a_1, a_2, \ldots, a_n))
```

## 2. Argumentar la corrección de programas iterativos

Para argumentar la corrección de programas iterativos, se debe formalizar cómo
es la iteración. Esto implica definir:

- Cómo se representa un estado de la iteración, $s$.
- Cuál es el estado inicial, $s_0$.
- Cuál es el estado final (o cómo se reconoce que un estado es final): $s_f$.
- Qué condición (o predicado) cumple todo estado: $\text{Inv}(s)$ (invariante
  de la iteración).
- El mecanismo para pasar de un estado al siguiente: $\text{transformar}(s)$.
  Si $s_i$ es el estado $i$, entonces $\text{transformar}(s_i) = s_{i+1}$.

Un programa iterativo tiene la siguiente forma:

```scala
def Pf(a: A): B = { // Pf recibe a de tipo A, y devuelve f(a) de tipo B
  def Pf_iter(s: Estado): B =
    if (esFinal(s)) respuesta(s) else Pf_iter(transformar(s))
  Pf_iter(s0)
}
```

Demostración de corrección:

- $\text{Inv}(s_0)$: el estado inicial cumple la condición invariante.
- Si $(s_i \neq s_f \land \text{Inv}(s_i)) \rightarrow \text{Inv}(\text{transformar}(s_i))$:
  el nuevo estado cumple la condición invariante si el estado anterior la
  cumplía.
- De lo anterior se concluye $\text{Inv}(s_f)$, es decir, el estado final
  cumple la condición invariante. Luego,
  $\text{Inv}(s_f) \rightarrow \text{respuesta}(s_f) == f(a)$.
- Finalmente, demostrar que siempre se llega al estado final $s_f$. Esto
  implica que
  $P_f(a) == \text{iter}(s_0) == \text{respuesta}(s_f) == f(a)$.

### Ejemplo: factorial iterativo

Considere el siguiente programa iterativo en Scala para calcular la función
factorial:

```scala
def Pf(n: Int): Int = { // Pf recibe n de tipo Int, y devuelve n! de tipo Int
  def Pf_iter(i: Int, n: Int, ac: Int): Int =
    if (i > n) ac else Pf_iter(i + 1, n, i * ac)
  Pf_iter(1, n, 1)
}
```

Este programa implementa el siguiente proceso iterativo:

- Un estado $s = (i, n, ac)$.
- El estado inicial es $s_0 = (1, n, 1)$.
- $(i, n, ac)$ es final si $i > n$, o lo que es lo mismo, si $i = n + 1$.
- La invariante de ciclo es
  $\text{Inv}(i, n, ac) \equiv i \leq n + 1 \land ac = (i-1)!$.
  La invariante de ciclo es una relación que SIEMPRE se cumple en el ciclo.
- $\text{transformar}((i, n, ac)) = (i+1, n, i \ast ac)$.

Ahora, demostramos los puntos mencionados:

**1.** $\text{Inv}(s_0)$: el estado inicial cumple la condición invariante.

```math
s_0 = (1, n, 1) \implies 1 \leq n + 1 \land 1 = 0!
```

**2.** La invariante se mantiene con la transformación de estados,
$(s_i \neq s_f \land \text{Inv}(s_i)) \rightarrow \text{Inv}(\text{transformar}(s_i))$:

1. Primer cambio, $i = i + 1$, lo que implica $ac = ((i+1) - 1)! = i!$.
2. Segundo cambio, $ac = i \ast ac$, entonces $ac = (i - 1)! \ast i = i!$.
3. Como se puede ver en ambos cambios indicados en la transformación, la
   invariante se mantiene.

**3.** $\text{Inv}(s_f) \rightarrow \text{respuesta}(s_f) == f(a)$

```math
(n + 1 \leq n + 1) \land ac = ((n+1)-1)! \rightarrow ac == n!
```

**4.** En cada paso, la componente $i$ del estado incrementa, acercándose a $n+1$.
Después de $n$ iteraciones, se alcanza $n+1$.

Esto implica que $P_f(n) == \text{iter}(1, n, 1) == n!$

### Ejemplo: el máximo de una lista

Se desea calcular el máximo de una lista de enteros positivos, no vacía. Sea
$f : \text{List}[\mathbb{N}] \to \mathbb{N}$ la función que calcula ese valor.
Y sea $P_f$ el siguiente programa en Scala:

```scala
def maxIt(l: List[Int]): Int = {
  def maxAux(max: Int, l: List[Int]): Int = {
    if (l.isEmpty) max
    else maxAux(math.max(max, l.head), l.tail)
  }
  maxAux(l.head, l.tail)
}
```

Este programa implementa el siguiente proceso iterativo:

- Un estado $s = (max, l)$ donde $l = \text{List}(a_i, a_{i+1}, \ldots, a_k)$
  es una cola de $L$.
- El estado inicial es
  $s_0 = (L.\text{head}, L.\text{tail}) = (a_1, \text{List}(a_2, \ldots, a_k))$.
- $s = (max, l)$ es final si $l$ es vacía.
- $\text{Inv}(max, l) \equiv l = \text{List}(a_i, a_{i+1}, \ldots, a_k) \land max = f(\text{List}(a_1, a_2, \ldots, a_{i-1}))$.
- $\text{transformar}((max, l)) = (nmax, l.\text{tail})$ donde $nmax = max$ si
  $max \geq l.\text{head}$, y $nmax = l.\text{head}$ si no.

Demostración de los puntos:

**1.** $\text{Inv}(s_0)$: el estado inicial cumple la condición invariante.

```math
s_0 = (a_1, \text{List}(a_2, \ldots, a_k)) \implies a_1 = f(\text{List}(a_1))
```

**2.** $(s_i \neq s_f \land \text{Inv}(s_i)) \rightarrow \text{Inv}(\text{transformar}(s_i))$

```math
\neg\, l.\text{isEmpty} \land l = \text{List}(a_i, a_{i+1}, \ldots, a_k) \land max = f(\text{List}(a_1, a_2, \ldots, a_{i-1}))
```

```math
\rightarrow l.\text{tail} = \text{List}(a_{i+1}, \ldots, a_k) \land nmax = f(\text{List}(a_1, \ldots, a_i))
```

**3.** $\text{Inv}(s_f) \rightarrow \text{respuesta}(s_f) == f(a)$

```math
\text{Inv}((max, \text{List}())) \rightarrow max = f(\text{List}(a_1, \ldots, a_k))
```

**4.** En cada paso, la lista $l$ se reduce, acercándose a ser vacía. Después de
$k$ iteraciones, $l = \text{List}()$.

Esto implica que $P_f(L) == \text{maxAux}(L.\text{head}, L.\text{tail}) == f(L)$

---------------------------
## A partir de acá, se hace el desarrollo del taller, lo anterior es el ejemplo que puso Cardel

## Punto 3:

Aquí queremos convencernos, con argumentos matemáticos y no solo con pruebas, de que `frecuencias` siempre devuelve lo que el enunciado pide. Primero dejamos clara la especificación, y luego miramos cada pedazo del programa con la herramienta que le corresponde: inducción estructural para las funciones recursivas sobre listas (`sumar`, `insertar` y `ordenar`) y estado, invariante y transformación para el recorrido del mensaje (`contar`).

## 0. Qué debe hacer frecuencias

Sea $m$ un mensaje de longitud $n$, con caracteres $m_0, m_1, \ldots, m_{n-1}$. Para una letra minúscula $c$, definimos cuántas veces aparece:

```math
\text{cuenta}(c, m) = |\{\, j < n : m_j = c \,\}|
```

Y llamamos $\text{Frec}(m)$ a la lista de pares $(c, \text{cuenta}(c, m))$, uno por cada letra $c$ que aparezca al menos una vez en $m$, ordenada así: el par $(c, x)$ va antes que el par $(c', x')$ cuando $x > x'$, o cuando $x = x'$ y $c < c'$.

Lo que vamos a demostrar es que, para todo mensaje $m$:

```math
\text{frecuencias}(m) == \text{Frec}(m)
```

Por comodidad, vamos a usar dos palabras. Decimos que una lista de pares es sin repetidas si no hay dos pares con la misma letra, y escribimos $\text{conj}(l)$ para el conjunto de pares de una lista $l$ (sin importar el orden).

El código que se realizó es el siguiente:

```scala
def frecuencias(m: Mensaje): Frecuencias = {

  // Le suma 1 a la cuenta de la letra c; si c no estaba, la agrega con 1.
  def sumar(c: Char, l: Frecuencias): Frecuencias =
    l match {
      case Nil => List((c, 1))
      case (letra, cuenta) :: resto =>
        if (letra == c) (letra, cuenta + 1) :: resto
        else (letra, cuenta) :: sumar(c, resto)
    }

  // Cuenta las letras de m a partir de la posición i, con lo contado hasta ahí en acc.
  @tailrec
  def contar(i: Int, acc: Frecuencias): Frecuencias =
    if (i >= m.length) acc
    else if (esMinuscula(m(i))) contar(i + 1, sumar(m(i), acc))
    else contar(i + 1, acc)

  // Un par va antes que otro si tiene más cuenta, o igual cuenta y letra menor.
  def vaAntes(letra1: Char, cuenta1: Int, letra2: Char, cuenta2: Int): Boolean =
    cuenta1 > cuenta2 || (cuenta1 == cuenta2 && letra1 < letra2)

  // Mete el par (c, n) en la lista ya ordenada, en el lugar que le toca.
  def insertar(c: Char, n: Int, l: Frecuencias): Frecuencias =
    l match {
      case Nil => List((c, n))
      case (letra, cuenta) :: resto =>
        if (vaAntes(c, n, letra, cuenta)) (c, n) :: l
        else (letra, cuenta) :: insertar(c, n, resto)
    }

  // Ordena la lista insertando uno por uno sus elementos.
  def ordenar(l: Frecuencias): Frecuencias =
    l match {
      case Nil => Nil
      case (letra, cuenta) :: resto => insertar(letra, cuenta, ordenar(resto))
    }

  ordenar(contar(0, Nil))
}
```

## 1. Corrección de las funciones recursivas sobre listas

Las tres funciones de esta parte trabajan sobre listas de pares, y las listas se definen recursivamente: o son `Nil`, o son un par seguido de otra lista. Eso nos deja usar inducción estructural: se prueba para `Nil`, y luego se supone que la propiedad vale para la cola `r` (hipótesis de inducción, HI) y se prueba para `x :: r`.

### 1.1 La función sumar

Sea $f(c, l)$ la lista que resulta de aumentar en 1 la cuenta de la letra $c$ en $l$, o de agregar el par $(c, 1)$ si $c$ no aparecía. Vamos a demostrar que, si $l$ es sin repetidas, entonces $\text{sumar}(c, l)$ es sin repetidas y $\text{conj}(\text{sumar}(c, l)) = \text{conj}(f(c, l))$.

Caso base: $l = \text{Nil}$.

```math
\text{sumar}(c, \text{Nil}) \rightarrow \text{List}((c, 1))
```

Como $c$ no aparecía en $\text{Nil}$, $f(c, \text{Nil}) = \text{List}((c, 1))$. Además es una lista de un solo par, así que es sin repetidas. Entonces el caso base se cumple.

Caso de inducción: $l = (x, n) :: r$, con $l$ sin repetidas. Hay que demostrar que si la propiedad vale para $r$, también vale para $l$. Por la HI, $\text{sumar}(c, r)$ es sin repetidas y tiene los pares de $f(c, r)$. Con el modelo de sustitución hay dos posibilidades:

Si $x = c$:

```math
\text{sumar}(c, (x, n) :: r) \rightarrow \text{if } (x == c)\ (x, n+1) :: r \text{ else } \ldots \rightarrow (c, n+1) :: r
```

La cuenta de $c$ subió de $n$ a $n + 1$ y el resto $r$ quedó igual, que es justo lo que hace $f$. Y como $l$ era sin repetidas, $c$ no aparece en $r$, así que la lista resultante también es sin repetidas.

Si $x \neq c$:

```math
\text{sumar}(c, (x, n) :: r) \rightarrow (x, n) :: \text{sumar}(c, r)
```

Por la HI, $\text{sumar}(c, r)$ tiene los pares de $r$ con la cuenta de $c$ aumentada (o con $(c, 1)$ agregado). El par $(x, n)$ se queda como estaba, porque $x \neq c$, que es lo que hace $f$. Para ver que no quedan letras repetidas: $x$ no aparecía en $r$ (porque $l$ era sin repetidas), y lo único nuevo que puede aparecer en $\text{sumar}(c, r)$ es la letra $c$, que es distinta de $x$.

Concluimos por inducción que, para toda lista $l$ sin repetidas, $\text{sumar}(c, l)$ es sin repetidas y tiene los pares de $f(c, l)$.

### 1.2 Un orden para los pares

Antes de hablar de `insertar` y `ordenar` hay que entender bien `vaAntes`. Cuando las letras de dos pares son distintas, decir que $\text{vaAntes}(p, q)$ es lo mismo que comparar las claves $(-\text{cuenta}, \text{letra})$ en orden lexicográfico: primero gana la cuenta más grande, y si empatan, la letra más pequeña. Eso es un orden total estricto sobre los pares de una lista sin repetidas: dados dos pares distintos, exactamente uno de los dos va antes que el otro, y la relación es transitiva.

Decimos que una lista está ordenada si cada par va antes que el que le sigue.

### 1.3 La función insertar

Vamos a demostrar que, si $l$ es una lista ordenada y sin repetidas, y la letra de $p = (c, n)$ no está en $l$, entonces $\text{insertar}(c, n, l)$ es una lista ordenada y con los pares de $l$ más $p$.

Caso base: $l = \text{Nil}$.

```math
\text{insertar}(c, n, \text{Nil}) \rightarrow \text{List}((c, n))
```

Es una lista de un solo par, que está ordenada, y tiene los pares de $\text{Nil}$ más $p$.

Caso de inducción: $l = x :: r$, con $x = (\text{letra}, \text{cuenta})$. La HI dice que $\text{insertar}(c, n, r)$ está ordenada y tiene los pares de $r$ más $p$. Hay dos posibilidades:

Si $\text{vaAntes}(p, x)$:

```math
\text{insertar}(c, n, x :: r) \rightarrow p :: (x :: r)
```

La lista queda ordenada porque $p$ va antes que $x$, y $x :: r$ ya estaba ordenada. Tiene los pares de $l$ más $p$.

Si no se cumple $\text{vaAntes}(p, x)$:

```math
\text{insertar}(c, n, x :: r) \rightarrow x :: \text{insertar}(c, n, r)
```

Por la HI, $\text{insertar}(c, n, r)$ está ordenada y tiene los pares de $r$ más $p$. Falta ver que $x$ va antes que el primer elemento de esa lista. Ese primer elemento es $p$ o es la cabeza $y$ de $r$. Si es $y$, $x$ va antes que $y$ porque $x :: r$ estaba ordenada. Si es $p$, como no se cumple $\text{vaAntes}(p, x)$ y las letras son distintas, por ser un orden total tiene que cumplirse $\text{vaAntes}(x, p)$. En los dos casos la lista queda ordenada y tiene los pares de $l$ más $p$.

Concluimos por inducción que $\text{insertar}$ cumple lo que dijimos.

### 1.4 La función ordenar

Vamos a demostrar que, si $l$ es sin repetidas, $\text{ordenar}(l)$ es una lista ordenada con exactamente los pares de $l$.

Caso base: $l = \text{Nil}$.

```math
\text{ordenar}(\text{Nil}) \rightarrow \text{Nil}
```

La lista vacía está ordenada y tiene los mismos pares que $\text{Nil}$.

Caso de inducción: $l = p :: r$, con $p = (\text{letra}, \text{cuenta})$. Por la HI, $\text{ordenar}(r)$ está ordenada y tiene los pares de $r$.

```math
\text{ordenar}(p :: r) \rightarrow \text{insertar}(\text{letra}, \text{cuenta}, \text{ordenar}(r))
```

Como $l$ es sin repetidas, la letra de $p$ no está en $r$, y entonces tampoco en $\text{ordenar}(r)$. Por lo que probamos en 1.3, el resultado está ordenado y tiene los pares de $r$ más $p$, que son justo los de $l$.

Concluimos por inducción que $\text{ordenar}$ cumple lo que dijimos.

## 2. Corrección del recorrido del mensaje: contar

`contar` es un programa iterativo (recursión de cola), así que lo formalizamos como un proceso con estados. Recordemos que $m$ es el mensaje original y $n$ su longitud.

Este programa implementa el siguiente proceso iterativo:

* Un estado $s = (i, acc)$, donde $i$ es la posición del mensaje que falta por leer y $acc$ es la lista de cuentas que llevamos.
* El estado inicial es $s_0 = (0, \text{Nil})$.
* $(i, acc)$ es final si $i \geq n$.
* La invariante de ciclo es:

```math
\text{Inv}(i, acc) \equiv 0 \leq i \leq n \ \land\ acc \text{ es sin repetidas} \ \land\ \text{conj}(acc) = \{\, (c, \text{cuenta}(c, m[..i))) : c \text{ letra},\ \text{cuenta}(c, m[..i)) > 0 \,\}
```

donde $m[..i)$ son los primeros $i$ caracteres de $m$. Dicho en palabras: en todo momento, $acc$ tiene la cuenta exacta de lo que ya se leyó.
* $\text{transformar}((i, acc)) = (i + 1, acc')$, donde $acc' = acc$ si $m_i$ no es una letra, y $acc' = \text{sumar}(m_i, acc)$ si lo es.

Ahora demostramos los puntos:

1. $\text{Inv}(s_0)$: el estado inicial cumple la invariante.

```math
s_0 = (0, \text{Nil}) \implies 0 \leq 0 \leq n \ \land\ \text{Nil} \text{ es sin repetidas} \ \land\ \text{conj}(\text{Nil}) = \emptyset
```

Con $i = 0$ no se ha leído nada, así que ninguna letra tiene cuenta positiva y el conjunto de la derecha también es vacío.

2. La invariante se mantiene: $(s_j \neq s_f \land \text{Inv}(s_j)) \rightarrow \text{Inv}(\text{transformar}(s_j))$. Sea $s_j = (i, acc)$ un estado que no es final, o sea $i < n$, y sea $c = m_i$.

  1. Primer cambio, $i = i + 1$: sigue valiendo $0 \leq i + 1 \leq n$ porque $i < n$.
  2. Segundo cambio, el acumulador. Si $c$ no es una letra, entre $m[..i)$ y $m[..i+1)$ no cambia ninguna cuenta de letras, y $acc' = acc$, así que la invariante se mantiene tal cual. Si $c$ es una letra, su cuenta en $m[..i+1)$ es una más que en $m[..i)$ y las de las demás letras no cambian. Eso es exactamente lo que hace $\text{sumar}(c, acc)$ según lo probado en 1.1, y además el resultado sigue siendo sin repetidas.
  3. Como se ve en los dos cambios, la invariante se mantiene.

3. $\text{Inv}(s_f) \rightarrow \text{respuesta}(s_f) == \text{Frec}(m)$ sin contar el orden. En un estado final $i \geq n$, y como la invariante dice $i \leq n$, tenemos $i = n$. Entonces $m[..n) = m$ y

```math
\text{conj}(acc) = \{\, (c, \text{cuenta}(c, m)) : c \text{ letra},\ \text{cuenta}(c, m) > 0 \,\}
```

que son exactamente los pares de $\text{Frec}(m)$, aunque todavía sin el orden pedido. La respuesta de `contar` es $acc$.

4. En cada paso, la componente $i$ del estado aumenta en 1, acercándose a $n$. Después de $n$ iteraciones se alcanza $n$ y el proceso termina.

Esto implica que $\text{contar}(0, \text{Nil})$ es una lista sin repetidas con los pares de $\text{Frec}(m)$.

## 3. Juntando las piezas

La función completa hace `ordenar(contar(0, Nil))`. Por lo que vimos en la sección 2, $\text{contar}(0, \text{Nil})$ es una lista sin repetidas con los pares de $\text{Frec}(m)$. Por lo que vimos en 1.4, `ordenar` la deja ordenada, con exactamente esos pares.

Solo falta ver que ese orden es el que pide el enunciado. Como $\text{vaAntes}$ es un orden total estricto sobre pares con letras distintas, solo hay una manera de ordenar un conjunto de pares con ese criterio, y esa manera es la de $\text{Frec}(m)$. Por lo tanto:

```math
\text{frecuencias}(m) == \text{Frec}(m)
```

para todo mensaje $m$, incluyendo los casos extremos: si $m$ es vacío o no tiene letras, $\text{contar}$ devuelve $\text{Nil}$, `ordenar` devuelve $\text{Nil}$ y $\text{Frec}(m)$ también es la lista vacía.


# Punto 4: Informe de corrección de romperCesar

Fundamentos de Programación Funcional y Concurrente.

## 1. Descripción del problema

La función `romperCesar` descifra un mensaje que fue cifrado con el cifrado
César, es decir, un mensaje en el que cada letra fue desplazada $d$ posiciones
en el alfabeto. Como no se conoce $d$, se usa la siguiente heurística: en
español la letra más frecuente suele ser la "e". Entonces se busca la letra
que más aparece en el mensaje cifrado y se asume que corresponde a una "e"
desplazada. A partir de ahí se calcula el desplazamiento y se aplica su
inverso para recuperar el mensaje original.

El programa está compuesto por tres funciones:

```scala
def desplazamientoProbable(m: Mensaje): Int = {
  def cuentaLetra(c: Char): Int = {
    @tailrec
    def cuenta(i: Int, acc: Int): Int =
      if (i >= m.length) acc
      else if (m(i) == c) cuenta(i + 1, acc + 1)
      else cuenta(i + 1, acc)

    cuenta(0, 0)
  }

  @tailrec
  def buscar(k: Int, kMejor: Int, cantMejor: Int): Int =
    if (k > 25) {
      if (cantMejor == 0) 0
      else (kMejor - 4 + 26) % 26
    }
    else {
      val n = cuentaLetra(('a' + k).toChar)
      if (n > cantMejor) buscar(k + 1, k, n)
      else buscar(k + 1, kMejor, cantMejor)
    }

  buscar(0, 0, 0)
}

def romperCesar(m: Mensaje): Mensaje =
  cesar(m, (26 - desplazamientoProbable(m)) % 26)
```

## 2. Especificación

Sea $m$ un mensaje. Se define:

- $\text{cant}(c, m)$ como la cantidad de veces que aparece el caracter $c$ en $m$.
- $M = \max\{\text{cant}(\text{'a'}+j, m) \mid 0 \leq j \leq 25\}$ como la mayor
  frecuencia entre las letras del alfabeto.
- $k^{*} = \min\{j \mid 0 \leq j \leq 25 \land \text{cant}(\text{'a'}+j, m) = M\}$
  como la primera letra que alcanza esa frecuencia máxima.

Entonces $f : \text{Mensaje} \to \mathbb{N}$, la función que calcula el
desplazamiento probable, se define como:

```math
f(m) = \begin{cases} 0 & \text{si } M = 0 \\ (k^{*} - 4 + 26) \bmod 26 & \text{si } M > 0 \end{cases}
```

Aquí el 4 es la posición de la "e" en el alfabeto (a = 0, b = 1, c = 2, d = 3,
e = 4). Restarlo indica cuántas posiciones se movió la "e" hasta llegar a la
letra más frecuente. Se suma 26 y se toma módulo 26 para que el resultado
nunca sea negativo.

Se debe argumentar que:

```math
\forall m \in \text{Mensaje} : P_f(m) == f(m)
```

donde $P_f$ es `desplazamientoProbable`. Como `desplazamientoProbable` usa dos
funciones auxiliares iterativas, `cuenta` y `buscar`, se argumenta primero la
corrección de cada una con el método del invariante.

## 3. Corrección de `cuenta` (dentro de `cuentaLetra`)

Esta función cuenta cuántas veces aparece el caracter $c$ en el mensaje $m$.
Es un programa iterativo (recursión de cola) que implementa el siguiente
proceso:

- Un estado $s = (i, acc)$, donde $i$ es la posición que se va a revisar y
  $acc$ es el conteo acumulado.
- El estado inicial es $s_0 = (0, 0)$.
- $(i, acc)$ es final si $i \geq m.\text{length}$.
- La invariante de ciclo es:

```math
\text{Inv}(i, acc) \equiv 0 \leq i \leq m.\text{length} \land acc = \#\{j \mid 0 \leq j < i \land m(j) = c\}
```

Es decir, $acc$ es la cantidad de veces que aparece $c$ en las primeras $i$
posiciones de $m$.
- $\text{transformar}((i, acc)) = (i+1, acc')$, donde $acc' = acc + 1$ si
  $m(i) = c$, y $acc' = acc$ si no.

Ahora, demostramos los puntos:

**1.** $\text{Inv}(s_0)$: el estado inicial cumple la condición invariante.

```math
s_0 = (0, 0) \implies 0 \leq 0 \leq m.\text{length} \land 0 = \#\{j \mid 0 \leq j < 0 \land m(j) = c\} = \#\emptyset
```

**2.** $(s_i \neq s_f \land \text{Inv}(s_i)) \rightarrow \text{Inv}(\text{transformar}(s_i))$

Sea $(i, acc)$ un estado no final que cumple la invariante, es decir,
$i < m.\text{length}$. Entonces $i + 1 \leq m.\text{length}$. Al pasar de $i$ a
$i + 1$ se incorpora la posición $i$ al conteo:

- Si $m(i) = c$, el conteo de las primeras $i+1$ posiciones es $acc + 1$, y el
  programa suma 1.
- Si $m(i) \neq c$, el conteo de las primeras $i+1$ posiciones sigue siendo
  $acc$, y el programa no suma.

En ambos casos $acc'$ es el conteo de las primeras $i + 1$ posiciones, así que
se mantiene la invariante.

**3.** $\text{Inv}(s_f) \rightarrow \text{respuesta}(s_f) == f(a)$

```math
(i \geq m.\text{length}) \land (i \leq m.\text{length}) \rightarrow i = m.\text{length} \rightarrow acc = \#\{j \mid 0 \leq j < m.\text{length} \land m(j) = c\} = \text{cant}(c, m)
```

**4.** En cada paso, la componente $i$ del estado incrementa en 1, acercándose
a $m.\text{length}$. Después de $m.\text{length}$ iteraciones se alcanza el
estado final.

Esto implica que `cuentaLetra(c)` $== \text{cuenta}(0, 0) == \text{cant}(c, m)$.

## 4. Corrección de `buscar`

Esta función recorre las 26 letras del alfabeto y se queda con la primera que
tiene mayor frecuencia en $m$. Implementa el siguiente proceso iterativo:

- Un estado $s = (k, kMejor, cantMejor)$, donde $k$ es la letra que se va a
  revisar, $kMejor$ es la mejor letra encontrada hasta el momento y
  $cantMejor$ es su cantidad de apariciones.
- El estado inicial es $s_0 = (0, 0, 0)$.
- $(k, kMejor, cantMejor)$ es final si $k > 25$, o lo que es lo mismo, si
  $k = 26$.
- Sea $M_k = \max(\{0\} \cup \{\text{cant}(\text{'a'}+j, m) \mid 0 \leq j < k\})$.
  La invariante de ciclo es:

```math
\text{Inv}(k, kMejor, cantMejor) \equiv 0 \leq k \leq 26 \land cantMejor = M_k \land kMejor = \begin{cases} 0 & \text{si } M_k = 0 \\ \min\{j < k \mid \text{cant}(\text{'a'}+j, m) = M_k\} & \text{si } M_k > 0 \end{cases}
```

- $\text{transformar}((k, kMejor, cantMejor)) = (k+1, k, n)$ si
  $n > cantMejor$, y $(k+1, kMejor, cantMejor)$ si no, donde
  $n = \text{cuentaLetra}(\text{'a'}+k) = \text{cant}(\text{'a'}+k, m)$
  (por lo demostrado en la sección 3).

Ahora, demostramos los puntos:

**1.** $\text{Inv}(s_0)$: el estado inicial cumple la condición invariante.

```math
s_0 = (0, 0, 0) \implies 0 \leq 0 \leq 26 \land cantMejor = 0 = M_0 \land kMejor = 0
```

Con $k = 0$ no se ha revisado ninguna letra, así que el máximo es 0.

**2.** $(s_i \neq s_f \land \text{Inv}(s_i)) \rightarrow \text{Inv}(\text{transformar}(s_i))$

Sea $(k, kMejor, cantMejor)$ un estado con $k \leq 25$ que cumple la
invariante. Entonces $k + 1 \leq 26$ y $M_{k+1} = \max(M_k, n)$. Hay dos casos:

- Si $n > cantMejor = M_k$: el nuevo máximo es $M_{k+1} = n$ y $n \geq 1$. Como
  todas las letras anteriores tienen frecuencia a lo sumo $M_k < n$, la letra
  $k$ es la primera que alcanza $n$. El nuevo estado es $(k+1, k, n)$, que
  cumple la invariante.
- Si $n \leq cantMejor = M_k$: el máximo no cambia, $M_{k+1} = M_k$. Si
  $M_k > 0$, la primera letra que alcanza el máximo sigue siendo $kMejor$,
  incluso en caso de empate ($n = M_k$), porque la comparación es
  estrictamente mayor. Si $M_k = 0$, entonces $n = 0$ y $kMejor = 0$ sigue
  siendo válido. El nuevo estado $(k+1, kMejor, cantMejor)$ cumple la
  invariante.

**3.** $\text{Inv}(s_f) \rightarrow \text{respuesta}(s_f) == f(a)$

En el estado final $k = 26$, por lo que $cantMejor = M_{26} = M$ y
$kMejor = k^{*}$ si $M > 0$ (o $kMejor = 0$ si $M = 0$). Entonces:

```math
\text{respuesta}(s_f) = \begin{cases} 0 & \text{si } cantMejor = 0 \\ (kMejor - 4 + 26) \bmod 26 & \text{si } cantMejor > 0 \end{cases} = \begin{cases} 0 & \text{si } M = 0 \\ (k^{*} - 4 + 26) \bmod 26 & \text{si } M > 0 \end{cases} = f(m)
```

**4.** En cada paso, la componente $k$ del estado incrementa en 1, acercándose
a 26. Después de 26 iteraciones se alcanza el estado final.

Esto implica que `buscar(0, 0, 0)` $== f(m)$.

## 4. Corrección de `desplazamientoProbable` y `romperCesar`

Como `desplazamientoProbable(m)` solo evalúa `buscar(0, 0, 0)`, por la sección
4 se tiene:

```math
P_f(m) == \text{buscar}(0, 0, 0) == f(m)
```

Ahora argumentamos que `romperCesar` recupera el mensaje original bajo el
supuesto de la heurística. Sea $o$ el mensaje original, cuya letra más
frecuente es la "e" (posición 4), y sea $m = \text{cesar}(o, d_0)$ el mensaje
cifrado con desplazamiento $d_0$. Cada letra se movió $d_0$ posiciones, así
que la letra más frecuente de $m$ es la que está en la posición
$(4 + d_0) \bmod 26$, es decir, $k^{*} = (4 + d_0) \bmod 26$.
Entonces:

```math
f(m) = (k^{*} - 4 + 26) \bmod 26 = d_0
```

Finalmente, `romperCesar(m)` aplica $\text{cesar}(m, (26 - d_0) \bmod 26)$,
que es el desplazamiento inverso, y por lo tanto devuelve el mensaje $o$.

Concluimos que:

```math
\forall m \in \text{Mensaje} : P_f(m) == f(m)
```

y que `romperCesar(m)` recupera el mensaje original siempre que la letra más
frecuente del mensaje original sea la "e" (y que en caso de empate entre
letras, la "e" desplazada sea la primera de ellas).
