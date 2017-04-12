/*
 * MIT License
 *
 * Copyright (c) 2017 Chris Camel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.ccm.stomp.protocol.frames.client

import org.scalatest._
import org.scalatest.prop.TableDrivenPropertyChecks

import scala.Array.emptyByteArray

/**
  *
  */
class AbortTest extends FlatSpec with Matchers with GivenWhenThen with TableDrivenPropertyChecks {
  behavior of "The ABORT frame"

  val transaction = "tx1"

  it must "be named abort" in {
    val f = Abort(transaction)

    Abort.frameName should be("ABORT")
    f.frameName should be("ABORT")
  }

  it must "include a transaction header" in {
    val f = Abort(transaction)

    Abort.TRANSACTION should be("transaction")
    f.transaction should equal(transaction)
    f.headers should contain(Abort.TRANSACTION -> transaction)
    f.additionalHeaders should not contain key(Abort.TRANSACTION)
  }

  it must "not have a body" in {
    val f = Abort("123")

    Abort.hasBody should be(false)
    f.body should be(emptyByteArray)
    f.hasBodyContent should be(false)
  }

  it must "be correctly constructed from a map" in {
    val data =
      Table(
        ("map", "result"),
        (Map("transaction" -> transaction), Abort(transaction)),
        (Map("transaction" -> transaction, "a" -> "1"), Abort(transaction, Map("a" -> "1")))
      )

    forAll(data) { (m: Map[String, String], result: Abort) =>
      Given(s"the map $m")
      Then(s"Abort is $result")
      Abort(m) should equal(result)
    }

    a[NoSuchElementException] should be thrownBy {
      Abort(Map("foo" -> "bar"))
    }
  }
}