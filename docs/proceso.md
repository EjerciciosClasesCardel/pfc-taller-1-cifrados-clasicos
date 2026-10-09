# Punto 1: Cifrado César con recursión lineal (proceso)

## Definición del algoritmo

```Scala
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

La idea del César es sencilla: cada letra se corre `k` lugares en el alfabeto, y si nos pasamos de la `z` seguimos contando desde la `a`. Lo que no es una letra minúscula (espacios, números, mayúsculas, signos) se deja igual.

Así quedó pensada la función:

* Si el mensaje está vacío, no hay nada que cifrar y devolvemos el mensaje vacío.
* Si no está vacío, miramos solo la **primera letra** (`m.head`), la cifrada, y le pegamos adelante el resultado de cifrar **todo lo demás** (`m.tail`). Ese "todo lo demás" lo resuelve la misma función, llamándose a sí misma con un mensaje más corto.
* Cada llamada hace el mismo trabajo con una letra menos, y por eso se llama recursión **lineal**: hay un único llamado recursivo por llamada y el mensaje se acorta de a una letra.

## Explicación paso a paso

### Caso base

```Scala
if (m.isEmpty) ""
```

Cuando el mensaje ya no tiene letras no queda nada por hacer y devolvemos `""`. Es el punto donde la recursión se detiene. Sin este caso la función se llamaría para siempre, porque `m.tail` de un mensaje vacío da error.

### Caso recursivo

Si el mensaje tiene al menos un carácter, lo separamos en `c = m.head` y `m.tail`. Aquí hay dos ramas.

**Rama 1: `c` es una letra minúscula.** Hacemos cuatro cálculos en orden:

1. `posicion = c - 'a'`: la posición de la letra en el alfabeto (`a` es 0, `b` es 1, ..., `z` es 25).
2. `nuevaposicion = (posicion + k) % 26`: le sumamos el desplazamiento y usamos el módulo 26 para que, si nos pasamos de la `z`, volvamos a empezar. Con `k = 29` y `a` quedaría `29 % 26 = 3`, o sea `d`. Por eso un desplazamiento de 29 es igual a uno de 3.
3. `ajustada`: aquí hay un detalle importante. En Scala el `%` conserva el signo del número. Por ejemplo `-1 % 26` da `-1` y no `25`. Entonces, si `nuevaposicion` salió negativa, le sumamos 26 para llevarla de vuelta al rango 0 a 25. Es lo que hace funcionar los desplazamientos negativos: con `cesar("abc", -1)` la `a` da `0 + (-1) = -1`, luego `-1 % 26 = -1`, luego `-1 + 26 = 25`, que es la `z`.
4. `nuevaLetra = ('a' + ajustada).toChar`: convertimos la posición de nuevo en letra.

Y el resultado de esta rama es:

```Scala
nuevaLetra.toString + cesar(m.tail, k)
```

**Rama 2: `c` no es una letra minúscula.** No se cifra, se copia tal cual:

```Scala
c.toString + cesar(m.tail, k)
```

### La operación pendiente

Aquí está lo más importante para entender el proceso. En las dos ramas, la llamada `cesar(m.tail, k)` **no es lo último que se hace**: cuando regresa, todavía hay que pegarle adelante la letra (`nuevaLetra.toString + ...`). Mientras esa llamada se resuelve, la letra ya cifrada tiene que quedarse guardada esperando. Ese "guardado" es justamente un marco en la pila de llamados. Por eso esta versión es recursión lineal y no de cola: una operación pendiente por cada letra.

---

## Llamados de pila: `cesar("casa", 3)`

Primero calculamos a mano qué le pasa a cada letra con `k = 3`:

| Letra | Posición | Posición + 3 | Nueva letra |
|---|---|---|---|
| `c` | 2 | 5 | `f` |
| `a` | 0 | 3 | `d` |
| `s` | 18 | 21 | `v` |
| `a` | 0 | 3 | `d` |

Ahora la ejecución. Al principio la pila **crece** (cada llamada se queda esperando) y al final se **vacía** (cada llamada recibe su respuesta y completa su pendiente). En los dibujos, lo de arriba es la llamada que se está ejecutando y lo de abajo son las que esperan.

### Fase 1: la pila crece

**Paso 1: llamada inicial**

```Scala
cesar("casa", 3)       // lee 'c' -> 'f'; llama a cesar("asa", 3)
```

Pila: 1 marco.

**Paso 2**

```Scala
cesar("asa", 3)        // lee 'a' -> 'd'; llama a cesar("sa", 3)
cesar("casa", 3)       // espera: "f" + (resultado de arriba)
```

Pila: 2 marcos.

**Paso 3**

```Scala
cesar("sa", 3)         // lee 's' -> 'v'; llama a cesar("a", 3)
cesar("asa", 3)        // espera: "d" + (resultado de arriba)
cesar("casa", 3)       // espera: "f" + (resultado de arriba)
```

Pila: 3 marcos.

**Paso 4**

```Scala
cesar("a", 3)          // lee 'a' -> 'd'; llama a cesar("", 3)
cesar("sa", 3)         // espera: "v" + (resultado de arriba)
cesar("asa", 3)        // espera: "d" + (resultado de arriba)
cesar("casa", 3)       // espera: "f" + (resultado de arriba)
```

Pila: 4 marcos.

**Paso 5: caso base**

```Scala
cesar("", 3)           // mensaje vacío: devuelve ""
cesar("a", 3)          // espera: "d" + (resultado de arriba)
cesar("sa", 3)         // espera: "v" + (resultado de arriba)
cesar("asa", 3)        // espera: "d" + (resultado de arriba)
cesar("casa", 3)       // espera: "f" + (resultado de arriba)
```

Pila: 5 marcos. Este es el punto más alto de la pila: uno por cada letra, más uno por el mensaje vacío.

### Fase 2: la pila se vacía

Ahora cada llamada recibe lo que devolvió la de arriba y completa su operación pendiente, de arriba hacia abajo:

```Scala
cesar("", 3)     = ""                    // el caso base
cesar("a", 3)    = "d" + ""     = "d"
cesar("sa", 3)   = "v" + "d"    = "vd"
cesar("asa", 3)  = "d" + "vd"   = "dvd"
cesar("casa", 3) = "f" + "dvd"  = "fdvd"
```

Resultado final: `"fdvd"`, que es el esperado por el enunciado.

---

## Diferencia con recursión de cola

* Con un mensaje de `n` letras, esta versión necesita `n + 1` marcos en la pila al mismo tiempo. Con mensajes muy largos (cientos de miles de letras) la pila se puede llenar y el programa se cae con un `StackOverflowError`.
* En el Punto 2 resolvemos lo mismo moviendo la operación pendiente a un parámetro (el acumulador), así la llamada recursiva sí queda como lo último que se hace.

---

## Ejemplo de uso

```Scala
val resultado = cesar("casa", 3)
println(resultado)  // fdvd
```

El resultado de `cesar("casa", 3)` es `"fdvd"`.


## Diagrama de llamados de pila con recursión lineal

Las flechas continuas hacia la derecha son los llamados (la pila crece) y las punteadas son los retornos (la pila se vacía):

```mermaid
sequenceDiagram
    participant A as cesar(casa, 3)
    participant B as cesar(asa, 3)
    participant C as cesar(sa, 3)
    participant D as cesar(a, 3)
    participant E as cesar(vacio, 3)

    A->>B: pendiente f + resultado
    B->>C: pendiente d + resultado
    C->>D: pendiente v + resultado
    D->>E: pendiente d + resultado
    E-->>D: devuelve vacio
    D-->>C: devuelve d
    C-->>B: devuelve vd
    B-->>A: devuelve dvd
    Note over A: devuelve fdvd
```

---

# Punto 2: Cifrado César con recursión de cola (proceso)

## Definición del algoritmo

```Scala
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

Esta función hace exactamente lo mismo que `cesar`, pero cambiando la forma de llevar el trabajo:

* Aparece un tercer parámetro, `acc` (el **acumulador**), donde vamos guardando el mensaje ya cifrado. Empieza en `""` por el valor por defecto, así que se puede llamar solo con `cesarCola("casa", 3)`.
* En vez de esperar a que vuelva la respuesta del resto para pegarle la letra adelante, **cifra la letra y la agrega al acumulador antes de llamar**. Lo que viaja al siguiente llamado ya trae el trabajo hecho.
* Así, cuando se hace la llamada recursiva no queda ninguna operación pendiente: es lo último que hace la función. Eso es una recursión de cola.
* La anotación `@tailrec` le pide al compilador que verifique esto. Si en algún punto la llamada recursiva no fuera lo último, el programa no compilaría. Además, la función es `final` porque `@tailrec` solo funciona en métodos que no se pueden sobrescribir.

## Explicación paso a paso

### Caso base

```Scala
if (m.isEmpty) acc
```

Cuando ya no quedan letras por leer, el trabajo está completo y el acumulador **es** la respuesta. A diferencia de `cesar`, donde el caso base devolvía `""` y la respuesta se armaba al regresar, aquí devolvemos `acc` tal cual.

### Caso recursivo

Se toma `c = m.head` y se cifra con la misma cuenta de `cesar` (posición, módulo 26, ajuste si es negativo y conversión a letra). Después:

```Scala
cesarCola(m.tail, k, acc + nuevaletra)   // si c es una letra minúscula
cesarCola(m.tail, k, acc + c)            // si no lo es
```

En cada llamada:

* El mensaje pierde su primera letra (`m.tail`).
* El desplazamiento `k` no cambia.
* El acumulador gana una letra al final: la cifrada si era minúscula, o la misma si no lo era.
* La llamada recursiva es la **última instrucción**: la suma `acc + nuevaletra` se calcula **antes** de llamar y viaja como argumento, así que nada queda esperando.

---

## Llamados de pila: `cesarCola("casa", 3)`

Las letras se cifran igual que en el Punto 1 (`c` a `f`, `a` a `d`, `s` a `v`, `a` a `d`). Lo que cambia es cómo se ve la ejecución. Aquí cada paso reemplaza al anterior en vez de apilarse:

### Paso 1: llamada inicial

```Scala
cesarCola("casa", 3, "")      // lee 'c' -> 'f'
```

### Paso 2

```Scala
cesarCola("asa", 3, "f")      // lee 'a' -> 'd'
```

### Paso 3

```Scala
cesarCola("sa", 3, "fd")      // lee 's' -> 'v'
```

### Paso 4

```Scala
cesarCola("a", 3, "fdv")      // lee 'a' -> 'd'
```

### Paso 5: caso base

```Scala
cesarCola("", 3, "fdvd")      // mensaje vacío: devuelve acc = "fdvd"
```

Y la pila en cada paso:

```
Paso 1:   [ cesarCola("casa", 3, "")     ]
Paso 2:   [ cesarCola("asa",  3, "f")    ]
Paso 3:   [ cesarCola("sa",   3, "fd")   ]
Paso 4:   [ cesarCola("a",    3, "fdv")  ]
Paso 5:   [ cesarCola("",     3, "fdvd") ]   -> devuelve "fdvd"
```

Siempre hay **un solo marco**. Cuando se hace la llamada recursiva, el marco anterior ya no tiene nada más que hacer, así que se puede reutilizar. Eso es lo que hace el compilador con `@tailrec`: convierte la recursión en un ciclo que va cambiando los valores de `m` y `acc`. Tampoco hay fase de regreso: el valor que se obtiene en el caso base es directamente la respuesta final, sin pegar nada en el camino de vuelta.

---

## ¿Por qué una crece y la otra no?

La diferencia está en **dónde se guarda el trabajo que falta**:

| | `cesar` (lineal) | `cesarCola` (de cola) |
|---|---|---|
| Qué hace con la letra cifrada | la deja esperando en el marco de la pila | la agrega al acumulador antes de llamar |
| ¿Qué pasa después de la llamada recursiva? | hay que pegarle la letra adelante | nada, el resultado se devuelve tal cual |
| Marcos en la pila con `n` letras | `n + 1` | 1 |
| Espacio en la pila | crece con el tamaño del mensaje | constante |
| Riesgo con mensajes muy largos | `StackOverflowError` | ninguno |

En `cesar`, la letra cifrada solo existe dentro del marco que la calculó. Si ese marco desaparece, se pierde, y por eso cada llamada tiene que quedarse esperando a que la de más arriba termine. En `cesarCola`, esa letra ya fue pasada al siguiente llamado dentro del acumulador, así que el marco actual no tiene nada que proteger y puede reutilizarse.

---

## Ejemplo de uso

```Scala
val resultado = cesarCola("casa", 3)
println(resultado)  // fdvd
```

El resultado de `cesarCola("casa", 3)` es `"fdvd"`, el mismo de `cesar("casa", 3)`.


## Diagrama de llamados de pila con recursión de cola

```mermaid
sequenceDiagram
    participant Main as cesarCola(casa, 3)
    participant L1 as cesarCola(casa, 3, vacio)
    participant L2 as cesarCola(asa, 3, f)
    participant L3 as cesarCola(sa, 3, fd)
    participant L4 as cesarCola(a, 3, fdv)
    participant L5 as cesarCola(vacio, 3, fdvd)

    Main->>L1: llamada inicial con acc vacio
    L1->>L2: tail call con acc = f
    L2->>L3: tail call con acc = fd
    L3->>L4: tail call con acc = fdv
    L4->>L5: tail call con acc = fdvd
    L5-->>Main: return fdvd
```

---

# Punto 3: Algoritmo frecuencias con recursión de cola

## Definición del algoritmo

```Scala
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

* La función `frecuencias` recibe un mensaje y devuelve una lista de pares `(letra, cuenta)` con las letras minúsculas que aparecen, de la más frecuente a la menos frecuente. Si dos letras empatan, va primero la que viene antes en el alfabeto.
* El trabajo se reparte en cuatro funciones internas:

  * `sumar`: le suma 1 a la cuenta de una letra dentro de la lista. Si la letra todavía no estaba, la agrega con cuenta 1.
  * `contar`: recorre el mensaje de izquierda a derecha y va llamando a `sumar`. Es la función con recursión de cola y por eso lleva `@tailrec`.
  * `vaAntes`: dice si un par debe ir antes que otro (más cuenta, o igual cuenta y letra menor).
  * `insertar` y `ordenar`: dejan la lista final en el orden que pide el enunciado, insertando los pares de uno en uno.
* El mensaje puede ser tan largo como se quiera, pero la lista de pares nunca tiene más de 26 elementos, porque hay una sola entrada por letra.

## Explicación paso a paso

### Caso base de contar

```Scala
if (i >= m.length) acc
```

Cuando `i` llega a la longitud del mensaje ya no queda nada por leer, así que `contar` devuelve el acumulador tal cual. En ese momento `acc` tiene todas las cuentas, pero todavía sin ordenar.

### Caso recursivo de contar

```Scala
else if (esMinuscula(m(i))) contar(i + 1, sumar(m(i), acc))
else contar(i + 1, acc)
```

En cada llamada:

* Se mira el carácter de la posición `i`.
* Si es una letra minúscula, se actualiza el acumulador con `sumar` antes de llamar. Si no lo es (un espacio, un número, una mayúscula), el acumulador pasa igual.
* En los dos casos `i` avanza en 1.
* La llamada recursiva es lo último que se hace. La suma de la letra se resuelve antes de llamar y viaja dentro del parámetro `acc`, así que no queda nada pendiente cuando la llamada regrese.

### La función sumar

```Scala
def sumar(c: Char, l: Frecuencias): Frecuencias =
  l match {
    case Nil => List((c, 1))
    case (letra, cuenta) :: resto =>
      if (letra == c) (letra, cuenta + 1) :: resto
      else (letra, cuenta) :: sumar(c, resto)
  }
```

Va recorriendo la lista hasta encontrar la letra. Si la encuentra, le suma 1 y deja el resto como estaba. Si llega al final sin encontrarla, la agrega con cuenta 1. Esta función no es de cola (queda pendiente pegar el par de adelante), pero como la lista tiene máximo 26 pares, nunca apila más de 26 llamados.

### Las funciones vaAntes, insertar y ordenar

```Scala
def vaAntes(letra1: Char, cuenta1: Int, letra2: Char, cuenta2: Int): Boolean =
  cuenta1 > cuenta2 || (cuenta1 == cuenta2 && letra1 < letra2)
```

`insertar` recorre una lista que ya está ordenada y mete el par nuevo justo antes del primer elemento al que le gana según `vaAntes`. `ordenar` toma el primer par de la lista, ordena el resto y lo inserta. Igual que `sumar`, ninguna de las dos es de cola, y de nuevo trabajan sobre máximo 26 pares.

---

## Llamados de pila en frecuencias

Ejemplo:

```Scala
frecuencias("casa")
```

Primero se cuenta con `contar`.

### Paso 1: Llamada inicial

```Scala
contar(0, Nil)
```

### Paso 2: Primera iteración

```Scala
contar(1, List((c,1)))                 // lee c, no estaba: sumar la agrega
```

### Paso 3: Segunda iteración

```Scala
contar(2, List((c,1),(a,1)))           // lee a, no estaba: sumar la agrega
```

### Paso 4: Tercera iteración

```Scala
contar(3, List((c,1),(a,1),(s,1)))     // lee s, no estaba: sumar la agrega
```

### Paso 5: Cuarta iteración

```Scala
contar(4, List((c,1),(a,2),(s,1)))     // lee a, ya estaba: sumar sube su cuenta a 2
```

### Paso 6: Caso base

```Scala
List((c,1),(a,2),(s,1))                // i = 4 es la longitud, devuelve el acumulador
```

Así se ve por dentro el último `sumar`, el del paso 5:

```Scala
sumar(a, List((c,1),(a,1),(s,1)))
= (c,1) :: sumar(a, List((a,1),(s,1)))    // c no es a, sigue buscando
= (c,1) :: (a,2) :: List((s,1))           // encontró la a y le sumó 1
= List((c,1),(a,2),(s,1))
```

Después se ordena la lista que devolvió `contar`:

```Scala
ordenar(List((c,1),(a,2),(s,1)))
= insertar(c, 1, ordenar(List((a,2),(s,1))))
= insertar(c, 1, List((a,2),(s,1)))       // ordenar de las dos últimas ya dio List((a,2),(s,1))
```

Y se ubica la `c`:

```Scala
vaAntes(c, 1, a, 2)  // false: 1 no es mayor que 2, entonces sigue buscando
vaAntes(c, 1, s, 1)  // true: igual cuenta y c va antes que s, entonces se pone ahí
```

```Scala
List((a,2),(c,1),(s,1))
```

---

## Diferencia con recursión normal

* Si el recorrido del mensaje se hiciera con recursión normal, cada letra dejaría un llamado esperando en la pila, y con un mensaje muy largo se podría desbordar. Con `contar` eso no pasa: la llamada recursiva es la última instrucción, el compilador la convierte en un ciclo y siempre hay un único llamado en la pila, sin importar qué tan largo sea el mensaje.
* `sumar`, `insertar` y `ordenar` sí son recursión normal, pero recorren la lista de pares y no el mensaje. Como esa lista tiene máximo 26 elementos, su pila nunca pasa de 26 llamados, por muy grande que sea la entrada.

---

## Ejemplo de uso

```Scala
val resultado = frecuencias("casa")
println(resultado)  // List((a,2), (c,1), (s,1))
```

El resultado de `frecuencias("casa")` es `List((a,2), (c,1), (s,1))`.


## Diagrama de llamados de pila con recursión de cola

```mermaid
sequenceDiagram
    participant Main as frecuencias(casa)
    participant L1 as contar(0, Nil)
    participant L2 as contar(1, List((c,1)))
    participant L3 as contar(2, List((c,1),(a,1)))
    participant L4 as contar(3, List((c,1),(a,1),(s,1)))
    participant L5 as contar(4, List((c,1),(a,2),(s,1)))
    participant Ord as ordenar

    Main->>L1: llamada inicial
    L1->>L2: tail call con (1, List((c,1)))
    L2->>L3: tail call con (2, List((c,1),(a,1)))
    L3->>L4: tail call con (3, List((c,1),(a,1),(s,1)))
    L4->>L5: tail call con (4, List((c,1),(a,2),(s,1)))
    L5-->>Main: devuelve List((c,1),(a,2),(s,1))
    Main->>Ord: ordenar de esa lista
    Ord-->>Main: devuelve List((a,2),(c,1),(s,1))
```

---

# Punto 4: Algoritmo para Romper el Cifrado César con Recursión de Cola (proceso)

## Definición del Algoritmo

```Scala
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

* La función `romperCesar` descifra un mensaje cifrado con el cifrado César sin conocer el desplazamiento. Para eso supone que la letra más frecuente del mensaje original era la **"e"**.
* La función `desplazamientoProbable` calcula cuántas posiciones se desplazó el mensaje, usando **recursión de cola** en sus dos funciones internas.
* La función interna `cuentaLetra` cuenta cuántas veces aparece una letra en el mensaje, con la función auxiliar `cuenta`:

  * Recibe dos parámetros:

    * `i`: la posición actual del mensaje, que crece hasta llegar al final.
    * `acc`: el acumulador donde se guarda el conteo parcial.
* La función interna `buscar` recorre las 26 letras del alfabeto para encontrar la más frecuente:

  * Recibe tres parámetros:

    * `k`: la letra que se va a revisar (`'a' + k`), de 0 a 25.
    * `kMejor`: la mejor letra encontrada hasta el momento.
    * `cantMejor`: cuántas veces aparece esa mejor letra.
* El decorador `@tailrec` obliga a que ambas funciones sean optimizadas como recursión de cola, es decir, **no se acumulan llamados en la pila**.

## Explicación paso a paso

### Función `cuenta`

#### Caso base

```Scala
if (i >= m.length) acc
```

Cuando `i` llega al final del mensaje, la función retorna directamente el conteo acumulado.

#### Caso recursivo

```Scala
else if (m(i) == c) cuenta(i + 1, acc + 1)
else cuenta(i + 1, acc)
```

En cada llamada:

* Se avanza una posición en el mensaje (`i + 1`).
* Si la letra en la posición `i` es la que se busca, se suma 1 al acumulador. Si no, el acumulador queda igual.
* Como la llamada recursiva es la **última instrucción** en ejecutarse, Scala puede optimizar la pila.

### Función `buscar`

#### Caso base

```Scala
if (k > 25) {
  if (cantMejor == 0) 0
  else (kMejor - 4 + 26) % 26
}
```

Cuando `k` pasa de 25 ya se revisó todo el alfabeto:

* Si `cantMejor` es 0, el mensaje no tiene letras y se retorna 0.
* Si no, se retorna `(kMejor - 4 + 26) % 26`. El 4 es la posición de la "e" (a = 0, b = 1, c = 2, d = 3, e = 4), así que restarlo indica cuánto se movió la "e" hasta la letra más frecuente. Se suma 26 y se toma módulo 26 para que el resultado nunca sea negativo.

#### Caso recursivo

```Scala
val n = cuentaLetra(('a' + k).toChar)
if (n > cantMejor) buscar(k + 1, k, n)
else buscar(k + 1, kMejor, cantMejor)
```

En cada llamada:

* Se cuenta cuántas veces aparece la letra `'a' + k`.
* Si aparece **más** veces que la mejor actual, esa letra pasa a ser la nueva mejor.
* Si no, se conserva la mejor anterior. Como se usa `>` estricto, en caso de empate se queda la primera letra.
* La llamada recursiva es la **última instrucción** en ejecutarse.

### Función `romperCesar`

Con el desplazamiento probable `d`, se aplica `cesar` con el desplazamiento **inverso** `(26 - d) % 26`, que devuelve el mensaje a su forma original.

---

## Llamados de pila en recursión de cola

Ejemplo: el mensaje original `"pepe"` fue cifrado con desplazamiento 3, y se obtuvo `"shsh"`.

```Scala
romperCesar("shsh")
```

### Paso 1: Llamada inicial

```Scala
desplazamientoProbable("shsh")  // inicia con buscar(0, 0, 0)
```

### Paso 2: Letras `'a'` a `'g'` (k = 0 a 6)

Ninguna aparece en el mensaje, así que `n = 0` y no supera a `cantMejor = 0`:

```Scala
buscar(0, 0, 0)
buscar(1, 0, 0)
...
buscar(7, 0, 0)   // k = 6 no cambia el estado
```

### Paso 3: Letra `'h'` (k = 7)

`cuentaLetra('h')` devuelve 2, y como `2 > 0` la letra `'h'` pasa a ser la mejor:

```Scala
buscar(7, 0, 0)   // n = 2 > 0
buscar(8, 7, 2)   // kMejor = 7, cantMejor = 2
```

### Paso 4: Letras `'i'` a `'r'` (k = 8 a 17)

Ninguna aparece, el estado no cambia:

```Scala
buscar(8, 7, 2)
...
buscar(18, 7, 2)
```

### Paso 5: Letra `'s'` (k = 18)

`cuentaLetra('s')` también devuelve 2, pero `2 > 2` es falso, así que se queda `'h'` (empate: gana la primera):

```Scala
buscar(18, 7, 2)  // n = 2, no es mayor que 2
buscar(19, 7, 2)
```

### Paso 6: Letras `'t'` a `'z'` (k = 19 a 25)

Ninguna aparece, el estado no cambia:

```Scala
buscar(19, 7, 2)
...
buscar(26, 7, 2)
```

### Paso 7: Caso base de `buscar`

```Scala
buscar(26, 7, 2)  // k > 25 y cantMejor != 0
return (7 - 4 + 26) % 26 = 29 % 26 = 3
```

### Paso 8: Descifrar

```Scala
cesar("shsh", (26 - 3) % 26)  // cesar("shsh", 23)
return "pepe"
```

### Cómo se calcula cada `cuentaLetra`

Por ejemplo, para `cuentaLetra('h')` sobre `"shsh"`:

```Scala
cuenta(0, 0)   // m(0) = 's' != 'h'
cuenta(1, 0)   // m(1) = 'h' == 'h', acc + 1
cuenta(2, 1)   // m(2) = 's' != 'h'
cuenta(3, 1)   // m(3) = 'h' == 'h', acc + 1
cuenta(4, 2)   // i >= m.length
return 2
```

---

## Diferencia con recursión normal

* En **recursión normal** cada llamada queda en la pila esperando a que termine la siguiente. Por ejemplo, al contar letras se tendría algo como `if (m(i) == c) 1 + cuenta(i + 1) else cuenta(i + 1)`, donde el `1 +` obliga a esperar la respuesta de la llamada interna, y con mensajes muy largos puede haber desbordamiento de pila.
* En **recursión de cola**, el acumulador (`acc`, `kMejor` y `cantMejor`) lleva el resultado parcial, así que la llamada recursiva es lo último que se hace. El compilador transforma el proceso en un **bucle optimizado**, por lo que no se guarda cada llamada en la pila y el algoritmo funciona con mensajes de cualquier tamaño.

---

## Ejemplo de uso

```Scala
val resultado = romperCesar("shsh")
println(resultado)  // pepe
```

El resultado de `romperCesar("shsh")` es `"pepe"`.

## Diagrama de llamados de pila con recursión de cola

### `cuentaLetra('h')` sobre `"shsh"`

```mermaid
sequenceDiagram
    participant Main as cuentaLetra('h')
    participant L1 as cuenta(0, 0)
    participant L2 as cuenta(1, 0)
    participant L3 as cuenta(2, 1)
    participant L4 as cuenta(3, 1)
    participant L5 as cuenta(4, 2)

    Main->>L1: llamada inicial
    L1->>L2: tail call con (1, 0)
    L2->>L3: tail call con (2, 1)
    L3->>L4: tail call con (3, 1)
    L4->>L5: tail call con (4, 2)
    L5-->>Main: return 2
```

### `buscar` sobre `"shsh"` (solo los pasos donde cambia el estado)

```mermaid
sequenceDiagram
    participant Main as desplazamientoProbable
    participant B1 as buscar(0, 0, 0)
    participant B2 as buscar(7, 0, 0)
    participant B3 as buscar(8, 7, 2)
    participant B4 as buscar(18, 7, 2)
    participant B5 as buscar(26, 7, 2)

    Main->>B1: llamada inicial
    B1->>B2: tail calls (k = 0 a 6, sin cambios)
    B2->>B3: tail call con (8, 7, 2), 'h' es la mejor
    B3->>B4: tail calls (k = 8 a 17, sin cambios)
    B4->>B5: tail calls (k = 18 a 25, 's' empata y no gana)
    B5-->>Main: return (7 - 4 + 26) % 26 = 3
```

---

# Punto 5: `combinaciones` y `vigenere` (proceso)

Este punto tiene dos funciones: `combinaciones`, que cuenta mensajes sin letras iguales seguidas, y `vigenere`, que cifra con una palabra clave. Las dos usan una función auxiliar `aux` con **recursión de cola**.

---

# Parte A: `combinaciones`

## Definición del algoritmo

```Scala
def combinaciones(n: Int, a: Int): BigInt = {
  @tailrec
  def aux(i: Int, acc: BigInt): BigInt =
    if (i > n) acc
    else aux(i + 1, acc * (a - 1))

  if (n <= 0) BigInt(1)
  else if (n == 1) BigInt(a)
  else aux(2, BigInt(a))
}
```

El enunciado dice que $C(n, a) = (a - 1) \cdot C(n-1, a)$, con $C(0, a) = 1$ y $C(1, a) = a$. Es decir, para formar un mensaje de longitud `n` se toma uno de longitud `n - 1` y se le agrega una letra distinta de la última: hay `a - 1` opciones.

La idea del algoritmo:

* Los casos `n = 0` y `n = 1` se contestan directo, sin recursión.
* Para `n >= 2` se parte del valor de `n = 1` (que es `a`) y se **multiplica por `a - 1`** una vez por cada longitud que falta. Ese valor que va creciendo se guarda en el acumulador `acc`.
* `i` dice para qué longitud se va a calcular el siguiente valor: `acc` siempre vale $C(i-1, a)$.
* Se usa `BigInt` porque el resultado crece muy rápido (con 26 letras, cada longitud nueva lo multiplica por 25) y un `Int` se desbordaría pronto.

## Explicación paso a paso

### Casos base de `combinaciones`

```Scala
if (n <= 0) BigInt(1)
else if (n == 1) BigInt(a)
```

* Con `n = 0` solo existe el mensaje vacío, así que la respuesta es 1.
* Con `n = 1` hay un mensaje por cada letra del alfabeto, o sea `a`.

### Caso base de `aux`

```Scala
if (i > n) acc
```

Cuando `i` ya pasó de `n`, se calcularon todas las longitudes hasta `n`, así que el acumulador **es** la respuesta.

### Caso recursivo de `aux`

```Scala
else aux(i + 1, acc * (a - 1))
```

En cada llamada:

* `i` avanza de a uno.
* El acumulador se multiplica por `a - 1`. Esa multiplicación se hace **antes** de llamar y viaja como argumento.
* La llamada recursiva es lo último que se hace, así que no queda nada pendiente.

---

## Llamados de pila: `combinaciones(4, 3)`

Con 3 letras y mensajes de longitud 4 se espera $3 \cdot 2^3 = 24$.

### Paso 1: llamada inicial

```Scala
combinaciones(4, 3)    // n = 4 >= 2: llama a aux(2, 3)
```

### Paso 2

```Scala
aux(2, 3)              // 2 <= 4: llama a aux(3, 3 * 2)
```

### Paso 3

```Scala
aux(3, 6)              // 3 <= 4: llama a aux(4, 6 * 2)
```

### Paso 4

```Scala
aux(4, 12)             // 4 <= 4: llama a aux(5, 12 * 2)
```

### Paso 5: caso base

```Scala
aux(5, 24)             // 5 > 4: devuelve acc = 24
```

Y la pila en cada paso:

```
Paso 1:   [ combinaciones(4, 3) ]
Paso 2:   [ aux(2, 3)   ]
Paso 3:   [ aux(3, 6)   ]
Paso 4:   [ aux(4, 12)  ]
Paso 5:   [ aux(5, 24)  ]   -> devuelve 24
```

Siempre hay **un solo marco de `aux`**: cuando se hace la llamada recursiva, el marco anterior ya no tiene nada más que hacer y se reutiliza. Con `@tailrec` el compilador lo convierte en un ciclo que va cambiando `i` y `acc`. La función `combinaciones` solo se queda esperando el resultado final, sin hacer nada con él.

---

## Diferencia con recursión normal

Si se escribiera la fórmula tal cual, $C(n, a) = (a-1) \cdot C(n-1, a)$, la multiplicación quedaría pendiente en cada llamado:

```Scala
combinaciones(4, 3)
= 2 * combinaciones(3, 3)
= 2 * (2 * combinaciones(2, 3))
= 2 * (2 * (2 * combinaciones(1, 3)))
= 2 * (2 * (2 * 3))       // recién aquí se multiplica
= 24
```

Cada `2 *` es una operación esperando en la pila, así que con un `n` grande habría `n` marcos y podría haber desbordamiento. Con el acumulador la multiplicación se hace antes de llamar, y el resultado parcial viaja como parámetro.

---

## Ejemplo de uso

```Scala
val resultado = combinaciones(4, 3)
println(resultado)  // 24
```

El resultado de `combinaciones(4, 3)` es `24`.

## Diagrama de llamados de pila con recursión de cola

```mermaid
sequenceDiagram
    participant Main as combinaciones(4, 3)
    participant L1 as aux(2, 3)
    participant L2 as aux(3, 6)
    participant L3 as aux(4, 12)
    participant L4 as aux(5, 24)

    Main->>L1: llamada inicial
    L1->>L2: tail call con (3, 3*2)
    L2->>L3: tail call con (4, 6*2)
    L3->>L4: tail call con (5, 12*2)
    L4-->>Main: return 24
```

---

# Parte B: `vigenere`

## Definición del algoritmo

```Scala
def vigenere(m: Mensaje, clave: Clave): Mensaje = {
  if (clave.isEmpty) {
    m
  } else {
    @tailrec
    def aux(i: Int, idxClave: Int, acc: Mensaje): Mensaje = {
      if (i >= m.length) {
        acc
      } else {
        val c = m(i)
        if (esMinuscula(c)) {
          val posicion = c - 'a'
          val k = clave(idxClave % clave.length) - 'a'
          val nuevaposicion = (posicion + k) % 26
          val ajustada = if (nuevaposicion < 0) nuevaposicion + 26 else nuevaposicion
          val nuevaLetra = ('a' + ajustada).toChar
          aux(i + 1, idxClave + 1, acc + nuevaLetra)
        } else {
          aux(i + 1, idxClave, acc + c)
        }
      }
    }

    aux(0, 0, "")
  }
}
```

Vigenère es como el César, pero en lugar de un solo desplazamiento usa **una palabra clave**: cada letra del mensaje se corre según la letra de la clave que le toca, y la clave se repite cuando se acaba.

* `i` es la posición del mensaje que se está leyendo.
* `idxClave` cuenta **cuántas letras se han cifrado**. Con él se sabe qué letra de la clave toca: `clave(idxClave % clave.length)`. El `%` es lo que hace que la clave se repita.
* `acc` es el mensaje cifrado hasta el momento (igual que en `cesarCola`).
* Aquí hay dos contadores, `i` e `idxClave`, y no siempre avanzan juntos: lo que no es una letra minúscula (espacios, números, signos) se copia pero **no gasta letra de la clave**, así que en ese caso solo avanza `i`.

## Explicación paso a paso

### Clave vacía

```Scala
if (clave.isEmpty) m
```

Sin clave no hay desplazamiento, así que el mensaje sale igual. Además esto evita dividir por cero en `idxClave % clave.length`.

### Caso base de `aux`

```Scala
if (i >= m.length) acc
```

Cuando `i` llega al final del mensaje ya no hay nada por leer, y el acumulador es la respuesta.

### Caso recursivo de `aux`

Se toma `c = m(i)`. Hay dos ramas.

**Rama 1: `c` es una letra minúscula.** Los cálculos son los mismos del César, pero el desplazamiento sale de la clave:

1. `posicion = c - 'a'`: la posición de la letra en el alfabeto.
2. `k = clave(idxClave % clave.length) - 'a'`: el desplazamiento que corresponde, que es la posición de la letra de la clave que toca.
3. `nuevaposicion = (posicion + k) % 26`: se suman y se da la vuelta al alfabeto con el módulo.
4. `ajustada`: se suma 26 si saliera negativo (aquí no pasa, porque las dos posiciones son positivas, pero se deja igual que en el César).
5. `nuevaLetra = ('a' + ajustada).toChar`: la posición se vuelve letra.

Luego se llama avanzando **los dos contadores**:

```Scala
aux(i + 1, idxClave + 1, acc + nuevaLetra)
```

**Rama 2: `c` no es una letra minúscula.** Se copia tal cual y la clave no se toca:

```Scala
aux(i + 1, idxClave, acc + c)
```

En las dos ramas la llamada recursiva es lo último que se hace: el `acc + ...` se calcula antes de llamar. Por eso es recursión de cola.

---

## Llamados de pila: `vigenere("no si", "abc")`

Los desplazamientos de la clave son `a` = 0, `b` = 1, `c` = 2, y se repiten. Primero a mano:

| Carácter | ¿Letra? | Letra de la clave | Cálculo | Resultado |
|---|---|---|---|---|
| `n` | sí | `a` (0) | 13 + 0 = 13 | `n` |
| `o` | sí | `b` (1) | 14 + 1 = 15 | `p` |
| ` ` | no | no se usa | se copia | ` ` |
| `s` | sí | `c` (2) | 18 + 2 = 20 | `u` |
| `i` | sí | `a` (0), la clave dio la vuelta | 8 + 0 = 8 | `i` |

Fíjese en el espacio: no gasta clave. Por eso a la `s` le toca la `c` y no la `a`.

### Paso 1: llamada inicial

```Scala
vigenere("no si", "abc")     // clave no vacía: llama a aux(0, 0, "")
```

### Paso 2

```Scala
aux(0, 0, "")                // lee 'n', clave 'a' -> 'n'
```

### Paso 3

```Scala
aux(1, 1, "n")               // lee 'o', clave 'b' -> 'p'
```

### Paso 4

```Scala
aux(2, 2, "np")              // lee ' ': se copia, idxClave no cambia
```

### Paso 5

```Scala
aux(3, 2, "np ")             // lee 's', clave 'c' -> 'u'
```

### Paso 6

```Scala
aux(4, 3, "np u")            // lee 'i', clave 'a' (3 % 3 = 0) -> 'i'
```

### Paso 7: caso base

```Scala
aux(5, 4, "np ui")           // i = 5 es la longitud: devuelve acc = "np ui"
```

Y la pila en cada paso:

```
Paso 1:   [ vigenere("no si", "abc") ]
Paso 2:   [ aux(0, 0, "")      ]
Paso 3:   [ aux(1, 1, "n")     ]
Paso 4:   [ aux(2, 2, "np")    ]
Paso 5:   [ aux(3, 2, "np ")   ]
Paso 6:   [ aux(4, 3, "np u")  ]
Paso 7:   [ aux(5, 4, "np ui") ]   -> devuelve "np ui"
```

Igual que en `cesarCola`, siempre hay **un solo marco de `aux`**. Cada llamada reemplaza a la anterior, el compilador lo convierte en un ciclo, y no hay fase de regreso: el valor del caso base es directamente la respuesta final.

---

## Diferencia con recursión normal

Si el cifrado se hiciera con recursión normal (cifrar la primera letra y pegarla adelante del resultado de cifrar el resto), cada letra dejaría un llamado esperando en la pila, y con mensajes largos podría desbordarse. Además habría que pasar la posición de la clave por parámetro para saber cuál letra toca. Con `aux` el mensaje cifrado se va armando en `acc` y la posición de la clave viaja en `idxClave`, así que la pila es constante sin importar el largo del mensaje.

---

## Ejemplo de uso

```Scala
val resultado = vigenere("ataque", "sol")
println(resultado)  // shliip
```

El resultado de `vigenere("ataque", "sol")` es `"shliip"`.

## Diagrama de llamados de pila con recursión de cola

```mermaid
sequenceDiagram
    participant Main as vigenere(no si, abc)
    participant L1 as aux(0, 0, vacio)
    participant L2 as aux(1, 1, n)
    participant L3 as aux(2, 2, np)
    participant L4 as aux(3, 2, np espacio)
    participant L5 as aux(4, 3, np u)
    participant L6 as aux(5, 4, np ui)

    Main->>L1: llamada inicial
    L1->>L2: n + a = n, idxClave sube
    L2->>L3: o + b = p, idxClave sube
    L3->>L4: espacio se copia, idxClave igual
    L4->>L5: s + c = u, idxClave sube
    L5->>L6: i + a = i, idxClave sube
    L6-->>Main: return np ui
```
