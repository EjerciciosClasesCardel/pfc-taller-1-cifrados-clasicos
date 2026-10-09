package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CasosPropiosTest extends AnyFunSuite {
  val c = new CifradosClasicos()
  import c._

  // Cinco casos propios para el punto 1.
  test("propio punto 1: el desplazamiento negativo cruza el inicio") {
    assert(cesar("abc", -1) == "zab")
  }

  test("propio punto 1: Int.MinValue se normaliza sin desbordamiento") {
    assert(cesar("a", Int.MinValue) == "c")
  }

  test("propio punto 1: conserva tabuladores y saltos de línea") {
    assert(cesar("a\tb\nc", 2) == "c\td\ne")
  }

  test("propio punto 1: un desplazamiento múltiplo de 26 no cambia letras") {
    assert(cesar("xyz", 52) == "xyz")
  }

  test("propio punto 1: una sola letra minúscula se cifra") {
    assert(cesar("z", 1) == "a")
  }

  // Cinco casos propios para el punto 2.
  test("propio punto 2: respeta el acumulador inicial") {
    assert(cesarCola("abc", 1, "prefijo:") == "prefijo:bcd")
  }

  test("propio punto 2: admite desplazamientos negativos grandes") {
    assert(cesarCola("abc", -53) == "zab")
  }

  test("propio punto 2: un mensaje largo conserva su contenido transformado") {
    assert(cesarCola("z" * 10000, 1) == "a" * 10000)
  }

  test("propio punto 2: conserva Unicode y puntuación") {
    assert(cesarCola("ñ, a!", 1) == "ñ, b!")
  }

  test("propio punto 2: coinciden ambas versiones con Int.MaxValue") {
    assert(cesarCola("recursion", Int.MaxValue) == cesar("recursion", Int.MaxValue))
  }

  // Cinco casos propios para el punto 3.
  test("propio punto 3: una sola letra tiene frecuencia uno") {
    assert(frecuencias("q") == List(('q', 1)))
  }

  test("propio punto 3: ignora mayúsculas, espacios y símbolos") {
    assert(frecuencias("Aa a!B") == List(('a', 2)))
  }

  test("propio punto 3: ordena frecuencias antes que el alfabeto") {
    assert(frecuencias("bbaccc") == List(('c', 3), ('b', 2), ('a', 1)))
  }

  test("propio punto 3: varios empates quedan en orden alfabético") {
    assert(frecuencias("dcba") == List(('a', 1), ('b', 1), ('c', 1), ('d', 1)))
  }

  test("propio punto 3: cuenta repeticiones en un mensaje largo") {
    assert(frecuencias("a" * 10000) == List(('a', 10000)))
  }

  // Cinco casos propios para el punto 4.
  test("propio punto 4: e como letra más frecuente implica desplazamiento cero") {
    assert(desplazamientoProbable("eeeeab") == 0)
  }

  test("propio punto 4: la letra z produce el desplazamiento 21 desde e") {
    assert(desplazamientoProbable("zzza") == 21)
  }

  test("propio punto 4: romper César devuelve vacío para el mensaje vacío") {
    assert(romperCesar("") == "")
  }

  test("propio punto 4: romper César descifra con otro desplazamiento") {
    val original = "este texto tiene muchas letras e"
    assert(romperCesar(cesar(original, 19)) == original)
  }

  test("propio punto 4: los caracteres ajenos al alfabeto no cuentan") {
    assert(desplazamientoProbable("!!!") == 0)
  }

  // Cinco casos propios para el punto 5.
  test("propio punto 5: longitud cero tiene una combinación aun con alfabeto vacío") {
    assert(combinaciones(0, 0) == BigInt(1))
  }

  test("propio punto 5: alfabeto unitario solo admite longitud uno") {
    assert(combinaciones(1, 1) == BigInt(1) && combinaciones(4, 1) == BigInt(0))
  }

  test("propio punto 5: combinaciones grandes usan BigInt") {
    assert(combinaciones(20, 26) == BigInt(26) * BigInt(25).pow(19))
  }

  test("propio punto 5: Vigenère repite la clave sobre las letras") {
    assert(vigenere("aaaaaa", "bc") == "bcbcbc")
  }

  test("propio punto 5: Vigenère no consume clave por símbolos") {
    assert(vigenere("a!a a", "bc") == "b!c b")
  }
}
