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
class AckTest extends FlatSpec with Matchers with GivenWhenThen with TableDrivenPropertyChecks {
  behavior of "The ACK frame"

  it must "be named ack" in {
    val f = Ack("123")

    Ack.frameName should be("ACK")
    f.frameName should be("ACK")
  }

  it must "include an id header" in {
    val id = "123"
    val f = Ack(id)

    Ack.ID should be("id")
    f.id should equal(id)
    f.headers should contain(Ack.ID -> id)
    f.additionalHeaders should not contain key(Ack.ID)
  }

  it must "include an optional transaction header" in {
    {
      Given("an ACK frame without transaction")
      val id = "123"
      val f = Ack(id)

      Then("there must be no transaction at all")
      f.headers should not contain key(Ack.TRANSACTION)
      f.transaction should be(None)
      f.additionalHeaders should not contain key(Ack.TRANSACTION)
    }
    {
      Given("an ACK frame with transaction")
      val transaction = "tr1"
      val f = Ack("foo", Some(transaction))

      Then("there must be a transaction")
      Ack.TRANSACTION should be("transaction")
      f.transaction should equal(Some(transaction))
      f.headers should contain(Ack.TRANSACTION -> transaction)
      f.additionalHeaders should not contain key(Ack.TRANSACTION)
    }
  }

  it must "not have a body" in {
    val f = Ack("123")

    Ack.hasBody should be(false)
  }

  it must "be correctly constructed from a map" in {
    val data =
      Table(
        ("map", "result"),
        (Map("id" -> "123"), Ack("123")),
        (Map("id" -> "123", "transaction" -> "tr1"), Ack("123", Some("tr1"))),
        (Map("id" -> "123", "a" -> "1", "b" -> "2"), Ack("123", None, Map("a" -> "1", "b" -> "2")))
      )

    forAll(data) { (m: Map[String, String], result: Ack) =>
      Given(s"the map $m")
      Then(s"Ack is $result")
      Ack(m) should equal(result)
    }

    a[NoSuchElementException] should be thrownBy {
      Ack(Map("foo" -> "bar"))
    }
  }
}