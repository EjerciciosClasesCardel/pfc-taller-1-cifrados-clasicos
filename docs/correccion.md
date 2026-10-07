# Informe de Corrección: Cifrados Clásicos con Recursión

Fundamentos de Programación Funcional y Concurrente  


---

## Punto 1: Cifrado César Lineal (`cesar`) — Inducción Estructural

Demostramos que `cesar(m, k)` cifra correctamente cualquier mensaje $m$ mediante inducción estructural sobre cadenas:

* **Caso base:** Si la cadena es vacía `""`, la condición `if (m.isEmpty)` retorna `""` inmediatamente. Esto es correcto porque un mensaje vacío no tiene caracteres para cifrar.
* **Hipótesis de inducción (HI):** Asumimos que para la cola del mensaje (`m.tail`) la función ya opera correctamente y devuelve todas sus letras cifradas con la clave $k$.
* **Paso inductivo:** Para un mensaje completo `m.head +: m.tail`, el código ejecuta:
  $$\text{cifrarChar}(m.\text{head}, k) + \text{cesar}(m.\text{tail}, k)$$
  Como `cifrarChar` cifra correctamente el primer carácter y por hipótesis `cesar(m.tail, k)` ya tiene cifrado todo el resto, al concatenar ambos resultados obtenemos el mensaje completo cifrado correctamente.

Por lo tanto, la función es correcta para cualquier mensaje por inducción estructural.

---

## Punto 2: Cifrado César de Cola (`cesarCola`) — Proceso Iterativo con Acumulador

Argumentamos la corrección de `cesarCola` formalizando su proceso iterativo:

* **Estado:** $(m, acc)$, donde $m$ es el mensaje que falta por procesar y $acc$ es el texto que ya acumulamos en la maleta.
* **Estado inicial:** $(m_0, "")$, arrancando con el mensaje original y el acumulador vacío.
* **Estado final:** $("", acc_f)$, cuando no quedan caracteres por procesar (`m.isEmpty == true`).
* **Respuesta:** En el estado final retorna el acumulador: $\text{respuesta}(("", acc_f)) = acc_f$.

### Invariante
En todo momento de la ejecución se cumple:
$$acc + \text{cifrar}(m, k) == \text{cifrar}(m_0, k)$$

> **En palabras sencillas:** Lo que ya llevo en el acumulador $acc$ más lo que me falta por cifrar del mensaje $m$, siempre equivale al mensaje final esperado.

### Demostración:
1. **Inicio:** Al comenzar, $acc$ es `""`, por lo que `"" + cifrar(m_0, k) == cifrar(m_0, k)`. El invariante se cumple.
2. **Paso a paso (Preservación):** En cada llamada recursiva hacemos `cesarCola(m.tail, k, acc + cifrarChar(m.head, k))`. La letra que quitamos de la cabeza de $m$ pasa cifrada al acumulador $acc$, por lo que la suma total de lo acumulado más lo pendiente se mantiene intacta.
3. **Final:** Al llegar al caso base ($m = ""$), se tiene $acc + "" = acc$. Por el invariante, ese valor acumulado $acc$ es exactamente el mensaje completo cifrado.
4. **Terminación:** En cada iteración procesamos `m.tail`, reduciendo la longitud de $m$ en 1 carácter. Como el mensaje tiene una longitud finita, obligatoriamente llegará a `""` tras un número finito de pasos, garantizando que el programa siempre termina.

---

## Punto 3: Frecuencias (`frecuencias`)

*(A cargo del integrante del equipo responsable del Punto 3).*

---

## Punto 4: Romper César (`desplazamientoProbable` y `romperCesar`)

*(A cargo del integrante del equipo responsable del Punto 4).*

---

## Punto 5: Combinaciones y Cifrado Vigenère (`combinaciones` y `vigenere`)

*(A cargo del integrante del equipo responsable del Punto 5).*
