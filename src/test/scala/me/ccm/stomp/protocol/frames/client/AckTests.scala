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

import scala.Array.emptyByteArray

/**
  *
  */
class AckTest extends FlatSpec with Matchers {
  "The ACK frame" must "be named ack" in {
    val f = Ack("123")

    Ack.frameName should be("ACK")
    f.frameName should be("ACK")
  }

  "The ACK frame" must "include an id header" in {
    val id = "123"
    val f = Ack(id)

    Ack.ID should be("id")
    f.id should equal(id)
    f.headers should contain(Ack.ID -> id)
    f.additionalHeaders should not contain key(Ack.ID)
  }

  "The ACK frame" must "include an optional transaction header" in {
    { // no transaction
      val id = "123"
      val f = Ack(id)

      f.headers should not contain key(Ack.TRANSACTION)
      f.transaction should be(None)
      f.additionalHeaders should not contain key(Ack.TRANSACTION)
    }
    { // with transaction
      val transaction = "tr1"
      val f = Ack("foo", Some(transaction))

      Ack.TRANSACTION should be("transaction")
      f.transaction should equal(Some(transaction))
      f.headers should contain(Ack.TRANSACTION -> transaction)
      f.additionalHeaders should not contain key(Ack.TRANSACTION)
    }
  }

  "The ACK frame" must "not have a body" in {
    val f = Ack("123")

    f.body should be(emptyByteArray)
    f.hasBodyContent should be(false)
  }

  "The ACK frame" must "be correctly constructed from headers" in {
    Ack(Map("id" -> "123")) should equal(Ack("123"))
    Ack(Map("id" -> "123", "transaction" -> "tr1")) should equal(Ack("123", Some("tr1")))
    Ack(Map("id" -> "123", "a" -> "1", "b" -> "2")) should equal(Ack("123", None, Map("a" -> "1", "b" -> "2")))
  }
}