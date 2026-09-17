# Informes

Aquí van los dos informes que pide el enunciado, en Markdown:

- `proceso.md` — el informe de proceso: la pila de llamados paso a paso para
  `cesar("casa", 3)` y `cesarCola("casa", 3)`, y por qué una crece y la otra no.
- `correccion.md` — el informe de corrección: la argumentación de que cada
  función hace lo que el enunciado pide.

Los dos archivos ya existen y traen un ejemplo de lo que se espera:
`proceso.md` sigue la traza de `factorial(5)` con recursión de cola y dibuja la
pila con `mermaid`; `correccion.md` argumenta por inducción estructural la
corrección del factorial y del máximo de una lista, y con estado, invariante y
transformación la de sus versiones iterativas. El informe del taller reemplaza
ese contenido y sigue la misma forma sobre las funciones del enunciado.

La notación matemática se escribe en LaTeX dentro del Markdown y los
diagramas se generan con `mermaid`. No se aceptan imágenes insertadas.
