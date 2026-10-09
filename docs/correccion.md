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

# A partir de acá se desarrolla el taller (lo anterior es el ejemplo del profesor)

---------------------------

# Punto 1: Informe de corrección de cesar

Fundamentos de Programación Funcional y Concurrente.

Aquí queremos demostrar, y no solo probar con ejemplos, que `cesar` siempre devuelve lo que el enunciado pide. Como `cesar` es recursiva lineal sobre un mensaje, y un mensaje es una lista de caracteres (o es vacío, o es un carácter seguido de otro mensaje), la herramienta natural es la **inducción estructural**.

## 1. Qué debe hacer cesar (especificación)

Para entender el problema, primero fijamos unas definiciones.

Sea $\text{pos}(c) = c - \text{'a'}$ la posición de una letra minúscula en el alfabeto (de 0 a 25), y sea $\text{letra}(q)$ la letra que está en la posición $q$, con $0 \leq q \leq 25$.

Para cualquier entero $x$, escribimos $x \bmod 26$ para el módulo **matemático**: el único número $r$ con $0 \leq r \leq 25$ tal que $x = 26q + r$ para algún entero $q$. Esto importa porque, a diferencia del `%` de Scala, el módulo matemático nunca es negativo: $(-1) \bmod 26 = 25$.

Con eso, definimos qué le pasa a un solo carácter $c$ cuando se corre $k$ posiciones:

```math
d(c, k) = \begin{cases} \text{letra}\big((\text{pos}(c) + k) \bmod 26\big) & \text{si } c \in \{\text{'a'}, \ldots, \text{'z'}\} \\ c & \text{en otro caso} \end{cases}
```

Y definimos $f(m, k)$, la especificación del cifrado, que corre cada carácter del mensaje. Como un mensaje $m$ es vacío o es de la forma $c :: r$ (un carácter $c$ seguido de un mensaje $r$), lo definimos así:

```math
f(\text{""}, k) = \text{""} \qquad\qquad f(c :: r, k) = d(c, k) \mathbin{+\!\!+} f(r, k)
```

donde $+\!\!+$ es la concatenación de cadenas. Esta definición dice, en palabras, lo mismo que el enunciado: cifrar es cifrar el primer carácter y pegarle el cifrado del resto.

Lo que queremos demostrar es que, si $P_f$ es la función `cesar`:

```math
\forall m \in \text{Mensaje},\ \forall k \in \mathbb{Z} : P_f(m, k) == f(m, k)
```

## 2. El programa

```scala
def cesar(m: Mensaje, k: Int): Mensaje =
  if (m.isEmpty) {
    ""
  } else {
    val c = m.head
    if (esMinuscula(c)) {
      val posicion = c - 'a'
      val nuevaposicion = (posicion + k).toInt % 26

      val ajustada = if (nuevaposicion < 0) nuevaposicion + 26 else nuevaposicion
      val nuevaLetra = ('a' + ajustada).toChar
      nuevaLetra.toString + cesar(m.tail, k)
    } else {
      c.toString + cesar(m.tail, k)
    }
  }
```

## 3. Un lema sobre el módulo de Scala

Antes de la inducción hay que quitar una duda: en el programa se usa `% 26` y después se corrige con `+ 26` si salió negativo. ¿Eso es el módulo matemático que usamos en la especificación? Lo demostramos.

**Lema.** Sea $x$ un entero cualquiera. Sea $r$ el resultado de `x % 26` en Scala y sea $a = r$ si $r \geq 0$, o $a = r + 26$ si $r < 0$. Entonces $a = x \bmod 26$.

**Demostración.** El `%` de Scala trunca hacia cero: $x = 26q + r$ con $|r| < 26$, y $r$ tiene el mismo signo de $x$ (o es cero). Hay dos casos:

- Si $r \geq 0$: entonces $0 \leq r \leq 25$ y $x = 26q + r$. Por la definición de módulo matemático, $r = x \bmod 26$, y como $a = r$, se cumple $a = x \bmod 26$.
- Si $r < 0$: entonces $-25 \leq r \leq -1$, así que $a = r + 26$ cumple $1 \leq a \leq 25$. Además $x = 26q + r = 26(q - 1) + (r + 26) = 26(q-1) + a$. Como $0 \leq a \leq 25$, resulta $a = x \bmod 26$.

En ambos casos queda demostrado. $\square$

Un ejemplo concreto: para la letra `a` (posición 0) con $k = -1$ se tiene $x = -1$, el `%` de Scala da $r = -1$, entonces $a = -1 + 26 = 25$, y la letra en la posición 25 es la `z`.

## 4. Demostración por inducción estructural

Vamos a demostrar, por inducción sobre la estructura del mensaje $m$, que para todo $k \in \mathbb{Z}$ se cumple $P_f(m, k) == f(m, k)$.

**Caso base:** $m = \text{""}$

```math
P_f(\text{""}, k) \rightarrow \text{if } (\text{"".isEmpty})\ \text{""} \text{ else } \ldots \rightarrow \text{""}
```

Por otro lado, por definición, $f(\text{""}, k) = \text{""}$. Entonces $P_f(\text{""}, k) == f(\text{""}, k)$.

**Caso de inducción:** $m = c :: r$, es decir, $c = m.\text{head}$ y $r = m.\text{tail}$. La hipótesis de inducción (HI) dice que $P_f(r, k) == f(r, k)$. Hay que demostrar que $P_f(c :: r, k) == f(c :: r, k)$.

Como $m$ no es vacío, el `if` de la primera línea no entra al caso base y se evalúa el `else`, que toma $c$ y se divide en dos ramas:

**Rama 1: $c$ es una letra minúscula.** Usando el modelo de sustitución:

```math
P_f(c :: r, k) \rightarrow \text{nuevaLetra.toString} \mathbin{+\!\!+} P_f(r, k)
```

donde `nuevaLetra` es la letra que sale de calcular, en orden, $p = \text{pos}(c)$, luego `nuevaposicion` $= (p + k)$ `% 26`, luego `ajustada`, y luego `('a' + ajustada).toChar`. Por el lema de la sección 3 aplicado a $x = p + k$, se tiene `ajustada` $= (p + k) \bmod 26$, que está entre 0 y 25. Por lo tanto `nuevaLetra` $= \text{letra}((p + k) \bmod 26) = d(c, k)$.

Usando la HI, $P_f(r, k) = f(r, k)$. Entonces:

```math
P_f(c :: r, k) \rightarrow d(c, k) \mathbin{+\!\!+} f(r, k) = f(c :: r, k)
```

**Rama 2: $c$ no es una letra minúscula.** Usando el modelo de sustitución:

```math
P_f(c :: r, k) \rightarrow c.\text{toString} \mathbin{+\!\!+} P_f(r, k) \overset{\text{HI}}{=} c \mathbin{+\!\!+} f(r, k)
```

Como $c$ no es minúscula, por definición $d(c, k) = c$. Entonces $c \mathbin{+\!\!+} f(r, k) = d(c, k) \mathbin{+\!\!+} f(r, k) = f(c :: r, k)$.

En las dos ramas se cumple $P_f(m, k) == f(m, k)$.

Además, el programa termina siempre: en cada llamada recursiva el mensaje pasa de $m$ a $m.\text{tail}$, que tiene una letra menos. Como la longitud es un natural, no puede decrecer para siempre y en algún momento llegamos a $\text{""}$, el caso base.

Concluimos por inducción que:

```math
\forall m \in \text{Mensaje},\ \forall k \in \mathbb{Z} : P_f(m, k) == f(m, k)
```

## 5. Cómo se encadenan los llamados: `cesar("casa", 3)`

Para ver la prueba funcionando, aplicamos el modelo de sustitución sobre un ejemplo. Cada vez que el programa llega a `cesar(m.tail, k)`, esa llamada se reemplaza por su propio resultado. En cada paso el mensaje pierde la primera letra, que es justo lo que pide la inducción (se pasa de $c :: r$ a $r$):

```math
\begin{aligned}
P_f(\text{"casa"}, 3) &\rightarrow \text{"f"} \mathbin{+\!\!+} P_f(\text{"asa"}, 3) \\
&\rightarrow \text{"f"} \mathbin{+\!\!+} \big(\text{"d"} \mathbin{+\!\!+} P_f(\text{"sa"}, 3)\big) \\
&\rightarrow \text{"f"} \mathbin{+\!\!+} \big(\text{"d"} \mathbin{+\!\!+} \big(\text{"v"} \mathbin{+\!\!+} P_f(\text{"a"}, 3)\big)\big) \\
&\rightarrow \text{"f"} \mathbin{+\!\!+} \big(\text{"d"} \mathbin{+\!\!+} \big(\text{"v"} \mathbin{+\!\!+} \big(\text{"d"} \mathbin{+\!\!+} P_f(\text{""}, 3)\big)\big)\big) \\
&\rightarrow \text{"f"} \mathbin{+\!\!+} \big(\text{"d"} \mathbin{+\!\!+} \big(\text{"v"} \mathbin{+\!\!+} \big(\text{"d"} \mathbin{+\!\!+} \text{""}\big)\big)\big) \\
&\rightarrow \text{"fdvd"}
\end{aligned}
```

Cada letra se calcula con la misma fórmula del lema: la `c` tiene posición 2 y $2 + 3 = 5$, que es `f`; la `a` queda en 3, que es `d`; la `s` tiene posición 18 y $18 + 3 = 21$, que es `v`. Los paréntesis que se van abriendo son las operaciones pendientes: no se pueden resolver hasta que llegue el caso base, y después se resuelven de adentro hacia afuera. Eso es lo que hace crecer la pila, como se ve en el informe de proceso.

## 6. Conclusión

Por el caso base, el programa devuelve la cadena vacía cuando no hay nada que cifrar. Por el caso de inducción, si `cesar` cifra bien el resto del mensaje, entonces cifra bien el mensaje completo, tanto cuando la primera letra es minúscula (gracias al lema que garantiza que el módulo se calcula bien incluso con desplazamientos negativos o mayores que 26) como cuando no lo es. Por lo tanto `cesar` es correcta con respecto a su especificación.

---------------------------

# Punto 2: Informe de corrección de cesarCola

Fundamentos de Programación Funcional y Concurrente.

`cesarCola` es el mismo César pero como proceso iterativo: lleva un acumulador y la llamada recursiva es lo último que se hace. Por eso aquí usamos la otra herramienta del ejemplo del profesor: **estado, invariante y transformación**. La especificación es la misma $f(m, k)$ del Punto 1.

## 1. El programa

```scala
@tailrec
final def cesarCola(m: Mensaje, k: Int, acc: Mensaje = ""): Mensaje =

  if (m.isEmpty)
    acc
  else {
    val c = m.head
    if (esMinuscula(c)) {
      val posicion = c - 'a'
      val nuevapo = (posicion + k).toInt % 26
      val ajustar = if (nuevapo < 0) nuevapo + 26 else nuevapo
      val nuevaletra = ('a' + ajustar).toChar
      cesarCola(m.tail, k, acc + nuevaletra)
    } else {
      cesarCola(m.tail, k, acc + c)
    }
  }
```

Sea $M$ el mensaje original con el que se llama la función y $n$ su longitud. La función se llama con $\text{acc} = \text{""}$, que es su valor por defecto. Queremos demostrar:

```math
\forall M \in \text{Mensaje},\ \forall k \in \mathbb{Z} : \text{cesarCola}(M, k, \text{""}) == f(M, k)
```

## 2. Dos hechos que vamos a usar

**Hecho 1: el carácter se cifra igual que en el Punto 1.** Los cálculos de `posicion`, `nuevapo`, `ajustar` y `nuevaletra` son los mismos de `cesar`, así que por el lema del Punto 1 (sección 3) cuando $c$ es minúscula se tiene $\text{nuevaletra} = d(c, k)$. Cuando no lo es, el programa agrega $c$ tal cual, y por definición $d(c, k) = c$. Es decir, en ambas ramas lo que se agrega al acumulador es $d(c, k)$.

**Hecho 2: $f$ respeta la concatenación.** Para dos mensajes $u$ y $v$ se cumple:

```math
f(u \mathbin{+\!\!+} v, k) = f(u, k) \mathbin{+\!\!+} f(v, k)
```

*Demostración:* por inducción sobre $u$. Si $u = \text{""}$, entonces $f(\text{""} \mathbin{+\!\!+} v, k) = f(v, k) = \text{""} \mathbin{+\!\!+} f(v, k) = f(\text{""}, k) \mathbin{+\!\!+} f(v, k)$. Si $u = c :: u'$, entonces $f((c :: u') \mathbin{+\!\!+} v, k) = d(c, k) \mathbin{+\!\!+} f(u' \mathbin{+\!\!+} v, k)$, y por la HI esto es $d(c, k) \mathbin{+\!\!+} (f(u', k) \mathbin{+\!\!+} f(v, k)) = (d(c, k) \mathbin{+\!\!+} f(u', k)) \mathbin{+\!\!+} f(v, k) = f(c :: u', k) \mathbin{+\!\!+} f(v, k)$. $\square$

En palabras: cifrar un mensaje en dos partes y pegarlas es lo mismo que cifrarlo de una vez. Este hecho es el que permite ir acumulando letra por letra.

## 3. Formalización como proceso iterativo

Este programa implementa el siguiente proceso iterativo (el desplazamiento $k$ no cambia, así que no es parte del estado):

* Un estado $s = (m', \text{acc})$, donde $m'$ es la parte del mensaje que falta por leer y $\text{acc}$ es lo que llevamos cifrado.
* El estado inicial es $s_0 = (M, \text{""})$.
* $(m', \text{acc})$ es final si $m'$ es vacío.
* Si $i$ es la cantidad de letras ya leídas, entonces $m' = M.\text{drop}(i)$ (lo que queda por leer) y $M.\text{take}(i)$ es lo ya leído. La invariante de ciclo es:

```math
\text{Inv}(m', \text{acc}) \equiv \exists\, i,\ 0 \leq i \leq n \ \land\ m' = M.\text{drop}(i) \ \land\ \text{acc} = f(M.\text{take}(i), k)
```

Dicho en palabras: en todo momento, $\text{acc}$ es el cifrado exacto de lo que ya se leyó.
* $\text{transformar}((c :: r, \text{acc})) = (r, \text{acc} \mathbin{+\!\!+} d(c, k))$. Es decir, se descarta la primera letra del mensaje por leer, y se agrega al acumulador esa letra cifrada (por el Hecho 1).

## 4. Demostración de los puntos

**1.** $\text{Inv}(s_0)$: el estado inicial cumple la condición invariante.

```math
s_0 = (M, \text{""}) \implies i = 0:\ M = M.\text{drop}(0) \ \land\ \text{""} = f(\text{""}, k) = f(M.\text{take}(0), k)
```

Con $i = 0$ no se ha leído nada, así que lo cifrado es la cadena vacía.

**2.** La invariante se mantiene: $(s_j \neq s_f \land \text{Inv}(s_j)) \rightarrow \text{Inv}(\text{transformar}(s_j))$.

Sea $s_j = (m', \text{acc})$ un estado que no es final y que cumple la invariante con cierto $i$. Como $m'$ no es vacío, $i < n$ y $m' = c :: r$ con $c = M_i$ (el carácter en la posición $i$) y $r = M.\text{drop}(i+1)$.

1. Primer cambio, el mensaje por leer: $m'$ pasa a ser $r = M.\text{drop}(i+1)$, y se sigue cumpliendo $0 \leq i + 1 \leq n$.
2. Segundo cambio, el acumulador. El nuevo acumulador es $\text{acc}' = \text{acc} \mathbin{+\!\!+} d(c, k)$. Por la invariante, $\text{acc} = f(M.\text{take}(i), k)$ y, por el Hecho 2:

```math
\text{acc}' = f(M.\text{take}(i), k) \mathbin{+\!\!+} f(c :: \text{""}, k) = f(M.\text{take}(i) \mathbin{+\!\!+} c, k) = f(M.\text{take}(i+1), k)
```

porque $f(c :: \text{""}, k) = d(c, k) \mathbin{+\!\!+} \text{""} = d(c, k)$.
3. Como se ve en los dos cambios, el nuevo estado cumple la invariante con $i + 1$.

**3.** $\text{Inv}(s_f) \rightarrow \text{respuesta}(s_f) == f(M, k)$

En un estado final $m'$ es vacío, y como $m' = M.\text{drop}(i)$ eso significa $i = n$. Entonces:

```math
\text{acc} = f(M.\text{take}(n), k) = f(M, k)
```

y la respuesta de la función en el caso base es justamente $\text{acc}$. Aquí se ve la diferencia con el Punto 1: en `cesar` la respuesta se armaba al regresar de las llamadas, mientras que aquí ya está completa en el caso base.

**4.** En cada paso la longitud de $m'$ disminuye en 1, acercándose a 0. Después de $n$ iteraciones, $m'$ es vacío y se llega al estado final.

Esto implica que $\text{cesarCola}(M, k, \text{""}) == \text{iter}(s_0) == \text{respuesta}(s_f) == f(M, k)$.

## 5. Cómo se encadenan los llamados: `cesarCola("casa", 3)`

Aplicamos el modelo de sustitución. A diferencia del Punto 1, aquí no se abren paréntesis: cada llamada se **reemplaza** por la siguiente, que es justamente una recursión de cola:

```math
\begin{aligned}
&\text{cesarCola}(\text{"casa"}, 3, \text{""}) \\
&\rightarrow \text{cesarCola}(\text{"asa"}, 3, \text{"f"}) \\
&\rightarrow \text{cesarCola}(\text{"sa"}, 3, \text{"fd"}) \\
&\rightarrow \text{cesarCola}(\text{"a"}, 3, \text{"fdv"}) \\
&\rightarrow \text{cesarCola}(\text{""}, 3, \text{"fdvd"}) \\
&\rightarrow \text{"fdvd"}
\end{aligned}
```

Y esta es la tabla de estados con la invariante en cada paso:

| $i$ | $m'$ (por leer) | $M.\text{take}(i)$ (ya leído) | $\text{acc}$ | ¿$\text{acc} = f(M.\text{take}(i), 3)$? |
|---|---|---|---|---|
| 0 | `"casa"` | `""` | `""` | sí |
| 1 | `"asa"` | `"c"` | `"f"` | sí |
| 2 | `"sa"` | `"ca"` | `"fd"` | sí |
| 3 | `"a"` | `"cas"` | `"fdv"` | sí |
| 4 | `""` | `"casa"` | `"fdvd"` | sí, y es el estado final |

## 6. Conclusión

La invariante vale al inicio, se conserva en cada transformación y, como el mensaje por leer se acorta en cada paso, el proceso termina. En el estado final la invariante dice que el acumulador es el cifrado de todo el mensaje, y eso es lo que devuelve la función. Por lo tanto `cesarCola` es correcta, y como ambas funciones cumplen la misma especificación $f$:

```math
\forall m \in \text{Mensaje},\ \forall k \in \mathbb{Z} : \text{cesarCola}(m, k, \text{""}) == f(m, k) == \text{cesar}(m, k)
```

---------------------------

# Punto 3: Informe de corrección de frecuencias

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

---------------------------

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

## 5. Corrección de `desplazamientoProbable` y `romperCesar`

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

## 6. ¿Cuándo falla romperCesar?

La demostración de la sección anterior dice que `desplazamientoProbable` siempre calcula lo que su especificación pide, pero eso no es lo mismo que acertar el desplazamiento real. La función no sabe cuál fue el desplazamiento $d_0$; lo **estima** suponiendo que la letra más frecuente del mensaje original era la "e". Si esa suposición es falsa, el programa sigue siendo correcto (hace exactamente lo que se le pidió), pero la respuesta es incorrecta como descifrado.

### Condición exacta de fallo

Sea $o$ el mensaje original, sea $a$ la posición de su letra más frecuente (la primera alfabéticamente si hay empate), y sea $m = \text{cesar}(o, d_0)$ el mensaje cifrado. Entonces $k^{*} = (a + d_0) \bmod 26$, y el desplazamiento estimado es:

```math
\hat{d} = (k^{*} - 4 + 26) \bmod 26 = (a + d_0 - 4) \bmod 26
```

El mensaje que devuelve `romperCesar` es $\text{cesar}(m, -\hat{d})$, y como los desplazamientos se suman módulo 26:

```math
\text{romperCesar}(m) = \text{cesar}(o,\ d_0 - \hat{d}) = \text{cesar}(o,\ 4 - a)
```

Esto es lo más importante: **el resultado no depende de $d_0$**, solo de $a$. Entonces:

- Si $a = 4$ (la letra más frecuente del original es la "e"), el desplazamiento extra es $4 - 4 = 0$ y se recupera $o$ exactamente.
- Si $a \neq 4$, el resultado es el original corrido $(4 - a)$ posiciones, que es un mensaje distinto de $o$. Por eso el método falla.

Dicho de otra forma, `romperCesar` falla exactamente cuando la letra más frecuente del mensaje original (o la primera alfabéticamente, en caso de empate) no es la "e". Esto pasa en tres situaciones típicas:

1. **Mensajes cortos**, donde por azar otra letra le gana a la "e".
2. **Mensajes sin "e" o con pocas**, como palabras sueltas.
3. **Empates**, porque `buscar` usa `>` estricto y se queda con la primera letra del alfabeto que alcanza el máximo. Si una letra anterior a la "e" empata con ella, gana la otra.

### Un mensaje concreto donde falla

Tomemos el original $o = \text{"casa blanca"}$, cifrado con $d_0 = 3$:

```scala
cesar("casa blanca", 3)   // "fdvd eodqfd"
```

Contamos las letras del mensaje cifrado `"fdvd eodqfd"`: la `d` aparece 4 veces, la `f` 2 veces, y la `e`, `o`, `q` y `v` una vez cada una. La letra más frecuente es la `d`, que está en la posición 3, así que `buscar` termina con $k^{*} = 3$ y:

```math
\hat{d} = (3 - 4 + 26) \bmod 26 = 25
```

El programa cree que el desplazamiento fue 25, cuando en realidad fue 3. Entonces descifra con el inverso, $(26 - 25) \bmod 26 = 1$:

```scala
romperCesar("fdvd eodqfd")  // cesar("fdvd eodqfd", 1) = "gewe fperge"
```

El resultado es `"gewe fperge"` y no `"casa blanca"`. Esto coincide con la fórmula: la letra más frecuente del original es la `a` ($a = 0$), así que el resultado es el original corrido $4 - 0 = 4$ posiciones, es decir `cesar("casa blanca", 4) = "gewe fperge"`.

### Un segundo caso: el empate

Tomemos $o = \text{"eeaa"}$, cifrado con $d_0 = 3$, lo que da `"hhdd"`. Aquí la "e" original sí es de las más frecuentes, pero empata con la "a": ambas aparecen 2 veces. En el mensaje cifrado, la `h` y la `d` también empatan con 2 apariciones cada una. Como la `d` (posición 3) viene antes que la `h` (posición 7) en el alfabeto, `buscar` se queda con $k^{*} = 3$ y se estima $\hat{d} = 25$ en vez de 3:

```scala
romperCesar("hhdd")  // cesar("hhdd", 1) = "iiee"
```

El resultado es `"iiee"` y no `"eeaa"`. De nuevo coincide con la fórmula, porque la letra elegida del original fue la `a` ($a = 0$) y el resultado es el original corrido 4 posiciones.

### Conclusión

`romperCesar` es una heurística de análisis de frecuencias: es correcta respecto a su especificación para todo mensaje (lo demostramos en las secciones 3, 4 y 5), pero solo recupera el mensaje original cuando la letra más frecuente del original, con el desempate alfabético, es la "e". Ese supuesto se cumple con textos largos en español y falla con textos cortos, sin "e" o con empates, como los ejemplos de arriba.

---------------------------

# Punto 5: Vigenère y conteo de mensajes (corrección)