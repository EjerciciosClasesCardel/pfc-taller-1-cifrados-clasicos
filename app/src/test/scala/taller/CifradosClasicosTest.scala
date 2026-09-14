package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

/**
 * Cada ejemplo del enunciado es una prueba. Si el enunciado promete un valor,
 * aquí se comprueba que la solución lo produce.
 */
@RunWith(classOf[JUnitRunner])
class CifradosClasicosTest extends AnyFunSuite {

  val c = new CifradosClasicos()
  import c._

  // Punto 1: ejemplos del enunciado -------------------------------------------

  test("cesar: casa con 3 da fdvd") { assert(cesar("casa", 3) == "fdvd") }
  test("cesar: fdvd con -3 vuelve a casa") { assert(cesar("fdvd", -3) == "casa") }
  test("cesar: hola mundo con 1") { assert(cesar("hola mundo", 1) == "ipmb nvoep") }
  test("cesar: zzz con 1 da aaa") { assert(cesar("zzz", 1) == "aaa") }
  test("cesar: 29 es lo mismo que 3") { assert(cesar("abc", 29) == "def") }
  test("cesar: el mensaje vacío sale vacío") { assert(cesar("", 5) == "") }

  test("cesar: la puntuación y los dígitos pasan sin cambio") {
    assert(cesar("ab, 12!", 1) == "bc, 12!")
  }

  test("cesar: las mayúsculas no se cifran") {
    assert(cesar("Casa", 3) == "Cdvd")
  }

  test("cesar: cifrar y descifrar es la identidad") {
    assert(cesar(cesar("un mensaje cualquiera", 11), -11) == "un mensaje cualquiera")
  }

  // Punto 2 -------------------------------------------------------------------

  test("cesarCola: casa con 3 da fdvd") { assert(cesarCola("casa", 3) == "fdvd") }
  test("cesarCola: hola mundo con 1") { assert(cesarCola("hola mundo", 1) == "ipmb nvoep") }
  test("cesarCola: con 0 el mensaje no cambia") { assert(cesarCola("abc", 0) == "abc") }

  test("cesarCola: da lo mismo que la versión lineal") {
    val casos = List(("casa", 3), ("hola mundo", 1), ("zzz", 1), ("abc", 29),
                     ("", 5), ("ab, 12!", -4))
    assert(casos.forall { case (m, k) => cesarCola(m, k) == cesar(m, k) })
  }

  test("cesarCola: aguanta un mensaje largo sin desbordar la pila") {
    val largo = "abcdefghij" * 20000
    assert(cesarCola(largo, 1).length == largo.length)
  }

  // Punto 3 -------------------------------------------------------------------

  test("frecuencias: casa") {
    assert(frecuencias("casa") == List(('a', 2), ('c', 1), ('s', 1)))
  }

  test("frecuencias: aabbbc") {
    assert(frecuencias("aabbbc") == List(('b', 3), ('a', 2), ('c', 1)))
  }

  test("frecuencias: hola mundo") {
    assert(frecuencias("hola mundo") ==
      List(('o', 2), ('a', 1), ('d', 1), ('h', 1), ('l', 1), ('m', 1),
           ('n', 1), ('u', 1)))
  }

  test("frecuencias: el mensaje vacío no tiene letras") {
    assert(frecuencias("") == List())
  }

  test("frecuencias: un mensaje sin letras no tiene frecuencias") {
    assert(frecuencias("123 !?") == List())
  }

  test("frecuencias: en empate manda el orden alfabético") {
    assert(frecuencias("ba") == List(('a', 1), ('b', 1)))
  }

  // Punto 4 -------------------------------------------------------------------

  test("desplazamientoProbable: h está 3 después de e") {
    assert(desplazamientoProbable("h") == 3)
  }

  test("desplazamientoProbable: hhhaa, con h como la más frecuente") {
    assert(desplazamientoProbable("hhhaa") == 3)
  }

  test("desplazamientoProbable: sin letras da 0") {
    assert(desplazamientoProbable("123") == 0)
  }

  test("desplazamientoProbable: en empate manda la primera alfabéticamente") {
    // 'a' y 'h' aparecen tres veces; gana 'a', que está 22 después de 'e'.
    assert(desplazamientoProbable("hhhaaa") == 22)
  }

  test("romperCesar: recupera un mensaje con suficientes letras e") {
    val original = "el mensaje secreto"
    assert(romperCesar(cesar(original, 7)) == original)
  }

  test("romperCesar: el método falla cuando la e no es la más frecuente") {
    // En este mensaje la letra más frecuente es la 'a', no la 'e'.
    val original = "cada casa amarilla"
    assert(romperCesar(cesar(original, 7)) != original)
  }

  // Punto 5 -------------------------------------------------------------------

  test("combinaciones: con longitud 0 hay un mensaje, el vacío") {
    assert(combinaciones(0, 26) == BigInt(1))
  }

  test("combinaciones: con longitud 1 hay tantos como letras") {
    assert(combinaciones(1, 26) == BigInt(26))
  }

  test("combinaciones: 3 letras sobre 26 dan 16250") {
    assert(combinaciones(3, 26) == BigInt(16250))
  }

  test("combinaciones: 2 letras sobre un alfabeto de 2 dan 2") {
    assert(combinaciones(2, 2) == BigInt(2))
  }

  test("combinaciones: crece según la recurrencia") {
    assert(combinaciones(5, 4) == BigInt(3) * combinaciones(4, 4))
  }

  test("vigenere: ataque con la clave sol") {
    assert(vigenere("ataque", "sol") == "shliip")
  }

  test("vigenere: hola mundo con la clave ab") {
    assert(vigenere("hola mundo", "ab") == "hplb mvneo")
  }

  test("vigenere: con la clave vacía el mensaje no cambia") {
    assert(vigenere("casa", "") == "casa")
  }

  test("vigenere: el espacio no consume letra de la clave") {
    // Sin el espacio la clave iría corrida y la m se cifraría con b.
    assert(vigenere("hola mundo", "ab").charAt(5) == 'm')
  }

  test("vigenere: con una clave de una sola letra es un César") {
    assert(vigenere("hola mundo", "d") == cesar("hola mundo", 3))
  }
}
