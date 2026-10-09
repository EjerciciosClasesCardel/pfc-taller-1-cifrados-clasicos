# Informe de corrección

Se cifran únicamente las letras minúsculas de `a` a `z`; los demás
caracteres permanecen iguales. En cada función recursiva, el caso base
detiene el proceso y el caso recursivo reduce el mensaje pendiente o avanza
hacia la longitud indicada.

## César lineal y César de cola

Sea $p$ la posición de una letra en el alfabeto, entre 0 y 25. El cifrado
definido es:

$$
E_k(p) = (p + k) \bmod 26
$$

Para `cesar`, el caso base es el mensaje vacío, que se transforma en vacío.
Para un mensaje no vacío, se cifra la primera letra y se concatena con el
cifrado recursivo del resto. Por inducción estructural, si el resto se
cifra correctamente, añadir la transformación correcta de la primera letra
produce el cifrado de todo el mensaje. La llamada sobre el resto reduce la
longitud en uno, así que se alcanza el caso base.

`cesarCola` mantiene en `acc` el resultado de los caracteres ya consumidos.
Al inicio no se ha consumido ninguno. Cada llamada cifra la primera letra
restante y la agrega al acumulador, preservando el invariante: `acc` es el
cifrado del prefijo procesado. Cuando el mensaje restante queda vacío,
devuelve el acumulador, que contiene el resultado completo. La llamada de
cola se verifica con `@tailrec`.

## Frecuencias

Para cada letra $c$, el auxiliar recorre el mensaje y aumenta su contador
exactamente cuando encuentra $c$. Al terminar, ese contador es el número de
apariciones de $c$ en el mensaje. Se consideran las letras del alfabeto y se
omiten las de frecuencia cero. La inserción conserva el orden definido:

$$
(c_1,n_1) < (c_2,n_2)
\quad\text{si}\quad
n_1 > n_2
\;\lor\;
(n_1 = n_2 \land c_1 < c_2)
$$

Por tanto, el resultado queda de mayor a menor frecuencia y, en empate, de
menor a mayor letra. Los recorridos que cuentan y ordenan avanzan hasta su
caso base mediante llamadas de cola.

## Romper César

`desplazamientoProbable` toma la primera letra de `frecuencias`, que es la
más frecuente y, en empate, la menor alfabéticamente. Si no hay letras
devuelve cero. Bajo la hipótesis de que esa letra cifrada corresponde a `e`,
la distancia modular desde `e` es el desplazamiento del cifrado. `romperCesar`
aplica el desplazamiento opuesto. Si el estimado es $k$, la composición
cancela el cifrado:

$$
(p + k - k) \bmod 26 = p
$$

El método puede fallar si la letra más frecuente del original no es `e`.
Por ejemplo, en `cada casa amarilla`, la letra `a` es la más frecuente.
Al cifrar con 7, la más frecuente pasa a ser `h`; el método estima 3 (distancia
de `e` a `h`) en lugar de 7 y devuelve `gehe gewe eqevmppe`, no el mensaje
original.

## Combinaciones

Sea $C(n,a)$ el número de mensajes de longitud $n$ que se pueden formar con
$a$ letras sin repetir dos consecutivas. El caso base de longitud cero es
$C(0,a)=1$. Para longitud uno, $C(1,a)=a$. Para $n>1$, se puede extender cada
mensaje válido de longitud $n-1$ con cualquiera de las $a-1$ letras distintas
de la última:

$$
C(n,a) = (a-1)C(n-1,a)
$$

La función sigue esa recurrencia hasta llegar a uno de los casos base. Cada
paso reduce $n$ en uno, y `BigInt` mantiene el resultado sin limitarlo al
rango de `Int`.

## Vigenère

Para cada letra minúscula del mensaje, `vigenere` obtiene la letra actual de
la clave y aplica su desplazamiento con la misma regla modular del César. El
índice de la clave avanza después de cifrar una letra y vuelve a cero al
llegar al final. Los caracteres que no son minúsculas se copian y el índice
de clave no cambia. Así, cada letra del mensaje consume exactamente una
letra de la secuencia repetida de la clave. Si la clave está vacía, el caso
especial devuelve el mensaje sin cambios.
