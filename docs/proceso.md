# Algoritmo Cesar con Recursión Lineal

### Tabla de Códigos ASCII (Minúsculas a-z)

| Letra | Código ASCII | Posición (0-25) |
| :---: | :----------: | :-------------: |
|   a   |      97      |        0        |
|   b   |      98      |        1        |
|   c   |      99      |        2        |
|   d   |     100      |        3        |
|   e   |     101      |        4        |
|   f   |     102      |        5        |
|   g   |     103      |        6        |
|   h   |     104      |        7        |
|   i   |     105      |        8        |
|   j   |     106      |        9        |
|   k   |     107      |       10        |
|   l   |     108      |       11        |
|   m   |     109      |       12        |
|   n   |     110      |       13        |
|   o   |     111      |       14        |
|   p   |     112      |       15        |
|   q   |     113      |       16        |
|   r   |     114      |       17        |
|   s   |     115      |       18        |
|   t   |     116      |       19        |
|   u   |     117      |       20        |
|   v   |     118      |       21        |
|   w   |     119      |       22        |
|   x   |     120      |       23        |
|   y   |     121      |       24        |
|   z   |     122      |       25        |


```Scala
 def cesar(m: Mensaje, k: Int): Mensaje = {
     if(m.isEmpty){""}
     else {
       val c = m.head
       val charCifrado = if (c.isLetter && esMinuscula(c)) {
         (((c.toInt - primera + k) % 26 + letras) % 26 + primera).toChar
       } else {
         c
         }
       charCifrado + cesar(m.tail, k)
     }
  }
```

* la funcion `cesar` recorre toda la cadena carácter por carácter y aplica el cifrado César a las letras minúsculas. Los demás caracteres no cambian.
* Usa recursion lineal, cada llamada utiliza una llamada recursiva para procesar el resto de la cadena.
 * Recibe dos parametros:
   * `m`: es la cadena de texto o mensaje que se quiere cifrar.
   * `k`: es la clave que indica el numero de posiciones que se va a mover la letra.

## Explicación paso a paso
### Caso base

```Scala
 if(m.isEmpty){""}
```
Cuando m no tiene caracteres, devuelve una cadena vacia, se termina la recursion.

### Caso recursivo
Si la cadena no está vacia
```Scala
 val c = m.head
```
Se le asigna a c el primer carácter.

```Scala
 val charCifrado = if (c.isLetter && esMinuscula(c)) 
```
Se verifica que la cadena sea una letra y que sea minuscula  con `esMinuscula`, 
si cumple se asigna a `charCifrado` el resultado de lo que hay adentro de la condicion.

```Scala
 (((c.toInt - primera + k) % 26 + letras) % 26 + primera).toChar
```
* Según el codigo ASCII cada letra del mundo tiene un númeroasignado, 
  al hacer `c.toInt` se le dice que cambie `c` a 99 que es su número asignado.
  `primera` corresponde a  `a` con número 97. k es la clave, letras es el número de letras del abecedario.

  * `c.toInt - primera ` al restar el primer caracter menos la base
    el resultado da la posicion del caracter en nuestro abecedario, empezando desde 0.
  
  * `+ k`  aqui se aplica el cifrado sumando el número de posiciones que se va a mover según k.
    si  `k` es positiva avanza, si es negativa, retrocede.
  
  * si el resultado de `(c.toInt - primera + k)` es un numero mayor al numero de las letras del abecedario,
     se aplica `% 26`, el residuo de la division entre 26.
  
  * si es un número negativo, al hacer el módulo sigue siendo negativo y no hay posiciones negativas,
   se suma `+ letras`, la cantidad de letras del abecedario, a esa posicion negativa.
  
  * si el resultado de `(c.toInt - primera + k) % 26 ` es un número  positivo al sumar `+ letras`,
    se sale del rango letras, otra vez, entonces se vuelve aplicar `% 26`. Asi da un número entre 0-25.
  
  * Hasta aqui lleva la posicion exacta de la nueva letra.
  
  * `+ primera` se vuelve a sumar el codigo ASCII de la primera.
  
  * `.toChar` toma ese número  y lo muestra como el caracter que le corresponde.

## Llamados de pila en recursión lineal
Ejemplo:
### Paso 1: Llamada inicial

```Scala
cesar("casa", 3) 
```
### Paso 2: Primera iteración

```Scala
cesar("casa", 3) 
```
  * m no esta vacio. 
  * c= `c`
  * c es letra minuscula.
  * ((99 - 97 + 3) % 26 + 26) % 26 + 97 = 5 + 97 = 102 ('f'). 
  * 'f' + cesar("asa", 3), se queda esperando...

### Paso 3: Segunda iteración
```Scala
cesar("asa", 3) 
```
* m no está vacio.
* c= `a`
* c es letra minuscula.
* ((97 - 97 + 3) % 26 + 26) % 26 + 97 = 3 + 97 = 100 ('d').
* 'd' + cesar("sa", 3), se queda esperando...
* 'f' + ('d' + cesar("sa", 3)), se acomula en la pila.

### Paso 4: Tercera iteración
```Scala
cesar("sa", 3) 
```
* m no está vacio.
* c= `s`
* c es letra minuscula.
* ((115 - 97 + 3) % 26 + 26) % 26 + 97 = 21 + 97 = 118 ('v').
* 'v' + cesar("a", 3), se queda esperando...
* 'f' + 'd'+ ('v'+ cesar("a", 3)), se acomula en la pila.

### Paso 5: Cuarta iteración
```Scala
cesar("a", 3) 
```
* m no está vacio.
* c= `a`
* c es letra minuscula.
* ((97- 97 + 3) % 26 + 26) % 26 + 97 = 3 + 97 = 118 ('d').
* 'd' + cesar(" ", 3), se queda esperando...
* 'f' + 'd'+ 'v'('d'+ cesar(" ", 3)), se acomula en la pila.

### Paso 6: Caso base
```Scala
cesar(" ", 3) 
```
* m está vacio.
* devuelve " "

### Desapilado: Paso 6 al paso 5
* 'f' + 'd'+ 'v'('d'+ cesar(" ", 3)), esperaba ahora recibe " "
* 'f' + 'd'+ 'v'+('d'+ " ")
* resuelve ('d'+ " ") = 'd'

### Desapilado: Paso 5 al paso 4
* 'f' + 'd'+ ('v'+ cesar("a", 3)), esperaba ahora recibe 'd'
* 'f' + 'd'+ ('v'+ 'd')
* resuelve ('v'+ "d") = 'vd'

### Desapilado: Paso 4 al paso 3
* 'f' + ('d' + cesar("sa", 3)), esperaba ahora recibe 'vd'
*  'f' + ('d'+ "vd")
* resuelve ('d'+ "vd") = 'dvd'

### Desapilado: Paso 2 al paso 1
* 'f' + cesar("asa", 3), esperaba ahora recibe "dvd"
* 'f'+ 'dvd'
*  resuelve 'fdvd'

El resultado de cesar("casa", 3) es 'fdvd'

## Diagrama de llamados de pila con recursión de cola

```mermaid
sequenceDiagram
    participant Main as cesar("casa", 3)
    participant L1 as cesar("casa", 3)
    participant L2 as cesar("asa", 3)
    participant L3 as cesar("sa", 3)
    participant L4 as cesar("a", 3)
    participant L5 as cesar("", 3)

    Main->>L1: llamada inicial
    L1->>L2: 'f' + cesar("asa", 3)
    L2->>L3: 'd' + cesar("sa", 3)
    L3->>L4: 'v' + cesar("a", 3)
    L4->>L5: 'd' + cesar("", 3)
    
    Note over L5: Caso Base
    L5-->>L4: return ""
    L4-->>L3: return 'd' + "" = "d"
    L3-->>L2: return 'v' + "d" = "vd"
    L2-->>L1: return 'd' + "vd" = "dvd"
    L1-->>Main: return 'f' + "dvd" = "fdvd"
```


# Algoritmo Cesar con Recursión en cola

## Definición del Algoritmo

```Scala
@tailrec
final def cesarCola(m: Mensaje, k: Int, acc: Mensaje = ""): Mensaje = {
if(m.isEmpty){acc}
else {
val c = m.head
val charCifrado = if (c.isLetter && esMinuscula(c)) {
(((c.toInt - primera + k) % 26 + letras) % 26 + primera).toChar
} else {
c
}
cesarCola (m.tail, k,acc + charCifrado)
}
}
```
* la funcion cesarCola al igual que la funcion cesar recibe un mensaje 
  y una clave para cifrarlo, recorre el texto letra a letra de principio a fin.
* Recibe dos parametros:
  * `m`: es la cadena de texto o mensaje que se quiere cifrar.
  * `k`: es la clave que indica el numero de posiciones que se va a mover la letra.

* El decorador `@tailrec` permite que el programa procese textos infinitos usando el mismo espacio de memoria.




## Explicación paso a paso

### Caso base

```Scala
if(m.isEmpty){acc}
```
Cuando m esta vacio devuelve lo que hay en el acomulador y termina la ejecucion


### Caso recursivo

```Scala
 cesarCola(m.tail, k, acc + charCifrado)
```

En cada llamada:
* Se reduce el tamaño del mensaje pasando solo lo que queda de la cadena (m.tail).
* Se calcula el cifrado del carácter actual (m.head) y se concatena al final del acumulador (acc + charCifrado).
* Al ser una recursión de cola, 
* la llamada a cesarCola es la última instrucción en ejecutarse, 
* permitiendo al compilador de Scala optimizar la pila de memoria.

## Llamados de pila en recursión de cola

Ejemplo:
### Paso 1: Llamado inicial
```Scala
cesarCola("hola", 3) // se asigna "" por defecto
```
Tambien podria ser  `cesarCola("casa", 3, "")`

### Paso 2: Primera iteración
```Scala
cesarCola("asa", 3, "f") // c = 'c' -> 'f' | acc = "" + "f"
```
### Paso 3: segunda iteración
```Scala
cesarCola("sa", 3, "fd") // c = 'a' -> 'd' | acc = "f" + "d"
```

### Paso 4: tercera iteración
```Scala
cesarCola("a", 3, "fdv") // c = 's' -> 'v' | acc = "fd" + "v"
```

### Paso 5: cuarta iteración
```Scala
cesarCola("", 3, "fdvd") // c = 'a' -> 'd' | acc = "fdv" + "d"
```

### Paso 6: quinta iteración

```Scala
return "fdvd" // m.isEmpty es verdadero, retorna acc
```

## Ejemplo de uso

```Scala
val mensajeCifrado = cesarCola("casa", 3)
println(mensajeCifrado) // "fdvd"
```

## Diferencia con recursión normal
El lineal necesita volver sobre sus pasos para construir el String final;
el de cola construye el String mientras avanza y no necesita regresar.


## Diagrama de llamados de pila con recursión de cola

```mermaid

sequenceDiagram
    participant Main as cesarCola("casa", 3)
    participant L1 as cesarCola("casa", 3, "")
    participant L2 as cesarCola("asa", 3, "f")
    participant L3 as cesarCola("sa", 3, "fd")
    participant L4 as cesarCola("a", 3, "fdv")
    participant L5 as cesarCola("", 3, "fdvd")

    Main->>L1: llamada inicial (asigna acc = "")
    L1->>L2: tail call con ("asa", 3, "f")
    L2->>L3: tail call con ("sa", 3, "fd")
    L3->>L4: tail call con ("a", 3, "fdv")
    L4->>L5: tail call con ("", 3, "fdvd")
    L5-->>Main: return "fdvd"
```