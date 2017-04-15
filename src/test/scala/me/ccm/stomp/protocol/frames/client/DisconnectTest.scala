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
 * OUT OF OR IN DISCONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.ccm.stomp.protocol.frames.client

import org.scalatest._
import org.scalatest.prop.TableDrivenPropertyChecks

import scala.Array.emptyByteArray

/**
  *
  */
class DisconnectTest extends FlatSpec with Matchers with GivenWhenThen with TableDrivenPropertyChecks {
  val receipt = "77"

  behavior of "The DISCONNECT frame"

  it must "be named connect" in {
    val f = Disconnect(receipt)

    Disconnect.frameName should be("DISCONNECT")
    f.frameName should be("DISCONNECT")
  }

  it must "include an receipt header" in {
    val f = Disconnect(receipt)

    Disconnect.RECEIPT should be("receipt")
    f.receipt should equal(receipt)
    f.headers should contain(Disconnect.RECEIPT -> receipt)
    f.additionalHeaders should not contain key(Disconnect.RECEIPT)
  }

  it must "not have a body" in {
    val f = Disconnect(receipt)

    Disconnect.hasBody should be(false)
  }

  it must "be correctly constructed from a map" in {
    val data =
      Table[Map[String, String], Disconnect](
        ("map", "result"),
        (Map(
          "receipt" -> receipt),
          Disconnect(receipt)),
        (Map(
          "receipt" -> receipt,
          "a" -> "1"),
          Disconnect(receipt, Map("a" -> "1")))
      )

    forAll(data) { (m, result) =>
      Given(s"the map $m")
      Then(s"Ack is $result")
      Disconnect(m) should equal(result)
    }
  }
}