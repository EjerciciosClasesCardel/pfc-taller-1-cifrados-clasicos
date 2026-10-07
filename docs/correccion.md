# Informe de Corrección: Cifrados Clásicos con Recursión

Fundamentos de Programación Funcional y Concurrente  
Escuela de Ingeniería de Sistemas y Computación, Universidad del Valle  

---

## Punto 1: Cifrado César Lineal (`cesar`)

Demostramos la corrección de la función `cesar` mediante **inducción estructural** sobre cadenas de texto (considerando una cadena como una lista de caracteres donde un mensaje es la cadena vacía `""` o una cabeza seguida de una cola `c +: resto`).

### Código analizado
```scala
def cesar(m: Mensaje, k: Int): Mensaje = {
  if (m.isEmpty) {
    ""
  } else {
    cifrarChar(m.head, k).toString + cesar(m.tail, k)
  }
}
```

### Especificación
Sea $f(m, k)$ el resultado esperado de aplicar el cifrado César a un mensaje $m$ con desplazamiento $k$. Queremos demostrar que para cualquier mensaje $m$ y clave $k$:
$$\text{cesar}(m, k) == f(m, k)$$

### 1. Caso base ($m = ""$)
* Si pasamos una cadena vacía, la condición `m.isEmpty` se cumple de inmediato y el código retorna `""`.
* Por especificación, cifrar un mensaje sin letras debe dar un mensaje vacío: $f("", k) = ""$.
* Por lo tanto:
  $$\text{cesar}("", k) == f("", k)$$
  El caso base se cumple.

### 2. Hipótesis de Inducción (HI)
Asumimos que para la cola del mensaje (`m.tail`, que es una cadena de menor longitud) la función ya opera correctamente:
$$\text{cesar}(m.\text{tail}, k) == f(m.\text{tail}, k)$$

### 3. Paso inductivo ($m = c +: s$)
Analizamos una cadena completa formada por un primer carácter $c$ (`m.head`) y el resto de la cadena $s$ (`m.tail`).

Siguiendo el código en la rama del `else`:
$$\text{cesar}(c +: s, k) \to \text{cifrarChar}(c, k) + \text{cesar}(s, k)$$

Por la Hipótesis de Inducción, reemplazamos la llamada recursiva por su especificación:
$$\to \text{cifrarChar}(c, k) + f(s, k)$$

Por la definición del cifrado César, cifrar un mensaje completo consiste en cifrar su primera letra y pegarla al cifrado del resto:
$$\text{cifrarChar}(c, k) + f(s, k) = f(c +: s, k)$$

Por lo tanto:
$$\text{cesar}(c +: s, k) == f(c +: s, k)$$

**Conclusión:** Por inducción estructural, la función `cesar(m, k)` siempre produce el resultado esperado para cualquier mensaje.

---

## Punto 2: Cifrado César con Recursión de Cola (`cesarCola`)

Como `cesarCola` es una función con acumulador que representa un proceso iterativo, argumentamos su corrección formalizando su estado, su invariante y su terminación.

### Código analizado
```scala
@annotation.tailrec
final def cesarCola(m: Mensaje, k: Int, acc: Mensaje = ""): Mensaje = {
  if (m.isEmpty) acc
  else {
    cesarCola(m.tail, k, acc + cifrarChar(m.head, k))
  }
}
```

### 1. Formalización del proceso iterativo

* **Estado ($s$):** Representado por la tupla $(m, acc)$, donde $m$ es el mensaje pendiente por procesar y $acc$ es el texto cifrado acumulado hasta el momento.
* **Estado inicial ($s_0$):** $(m_0, "")$, donde $m_0$ es el mensaje original completo y el acumulador arranca vacío por defecto.
* **Estado final ($s_f$):** Se reconoce cuando el mensaje restante está vacío, es decir, cuando $m = ""$ (`m.isEmpty == true`). El estado final es $("", acc_f)$.
* **Respuesta del algoritmo:** Cuando se llega al estado final, la función retorna directamente el acumulador: $\text{respuesta}(("", acc_f)) = acc_f$.
* **Transformación de estado:** En cada paso recursivo se pasa al siguiente estado:
  $$\text{transformar}((m, acc)) = (m.\text{tail}, acc + \text{cifrarChar}(m.\text{head}, k))$$

### 2. Invariante de ciclo ($\text{Inv}$)

La condición que siempre se mantiene verdadera a lo largo de toda la ejecución es:
$$\text{Inv}(m, acc) \equiv acc + f(m, k) == f(m_0, k)$$

> **En palabras sencillas:** Lo que ya llevamos acumulado en la maleta $acc$ más lo que falta por cifrar del mensaje $m$, siempre equivale al resultado final esperado del mensaje original completo $m_0$.

### 3. Demostración de los 4 pasos de corrección

#### Paso 1: El estado inicial cumple el invariante ($\text{Inv}(s_0)$)
En el estado inicial $s_0 = (m_0, "")$:
$$acc + f(m, k) = "" + f(m_0, k) = f(m_0, k)$$
El invariante se cumple al inicio.

#### Paso 2: El invariante se mantiene en cada paso
Supongamos que estamos en un paso donde el mensaje no está vacío ($m = c +: s$) y se cumple el invariante:
$$acc + f(c +: s, k) = f(m_0, k)$$

Por definición de cifrado: $f(c +: s, k) = \text{cifrarChar}(c, k) + f(s, k)$. Sustituyendo:
$$acc + \text{cifrarChar}(c, k) + f(s, k) = f(m_0, k)$$

Al agrupar el nuevo acumulador $acc' = acc + \text{cifrarChar}(c, k)$ y el nuevo mensaje restante $m' = s$:
$$acc' + f(m', k) = f(m_0, k)$$

Esto demuestra que el nuevo estado $(m', acc')$ sigue cumpliendo el invariante.

#### Paso 3: El estado final da la respuesta correcta
Al llegar al estado final $s_f = ("", acc_f)$:
$$acc_f + f("", k) = f(m_0, k)$$
Como $f("", k) = ""$:
$$acc_f + "" = acc_f = f(m_0, k)$$

Como el programa retorna $acc_f$, la respuesta obtenida coincide exactamente con la especificación esperada $f(m_0, k)$.

#### Paso 4: Terminación
En cada iteración tomamos `m.tail`, por lo que la longitud del mensaje se reduce estrictamente en 1 ($|m.\text{tail}| = |m| - 1$). Como cualquier cadena de texto tiene una longitud finita, tras $|m_0|$ pasos el mensaje alcanzará obligatoriamente la cadena vacía `""`, garantizando que el algoritmo siempre termina y no entra en bucles infinitos.

---

## Punto 3: Frecuencias (`frecuencias`)

*(Sección correspondiente al integrante del equipo a cargo del Punto 3).*

---

## Punto 4: Romper César (`desplazamientoProbable` y `romperCesar`)

*(Sección correspondiente al integrante del equipo a cargo del Punto 4).*

---

## Punto 5: Combinaciones y Cifrado Vigenère (`combinaciones` y `vigenere`)

*(Sección correspondiente al integrante del equipo a cargo del Punto 5).*
