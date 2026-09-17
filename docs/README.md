# Informes

Aquí van los dos informes que pide el enunciado, en Markdown:

- `proceso.md` — el informe de proceso: la pila de llamados paso a paso para
  `cesar("casa", 3)` y `cesarCola("casa", 3)`, y por qué una crece y la otra no.
- `correccion.md` — el informe de corrección: la argumentación de que cada
  función hace lo que el enunciado pide.

La notación matemática se escribe en LaTeX dentro del Markdown y los
diagramas se generan con `mermaid`. No se aceptan imágenes insertadas.

En `ejemplos/` hay uno de cada, sobre el factorial y el máximo de una lista:

- [`ejemplos/proceso.md`](ejemplos/proceso.md): la traza de `factorial(5)`
  con recursión de cola, paso a paso y con el diagrama de la pila en `mermaid`.
- [`ejemplos/correccion.md`](ejemplos/correccion.md): cómo se argumenta la
  corrección de un programa recursivo por inducción estructural y la de uno
  iterativo con estado, invariante y transformación.

Los informes del taller siguen esa misma forma sobre las funciones del
enunciado.
