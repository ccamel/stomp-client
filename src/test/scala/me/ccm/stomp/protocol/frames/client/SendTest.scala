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
class SendTest extends FlatSpec with Matchers with GivenWhenThen with TableDrivenPropertyChecks {
  behavior of "The BEGIN frame"

  val transaction = "tx1"

  it must "be named begin" in {
    val f = Begin(transaction)

    Begin.frameName should be("BEGIN")
    f.frameName should be("BEGIN")
  }

  it must "include a transaction header" in {
    val f = Begin(transaction)

    Begin.TRANSACTION should be("transaction")
    f.transaction should equal(transaction)
    f.headers should contain(Begin.TRANSACTION -> transaction)
    f.additionalHeaders should not contain key(Begin.TRANSACTION)
  }

  it must "not have a body" in {
    val f = Begin("123")

    Begin.hasBody should be(false)
    f.body should be(emptyByteArray)
    f.hasBodyContent should be(false)
  }

  it must "be correctly constructed from a map" in {
    val data =
      Table(
        ("map", "result"),
        (Map("transaction" -> transaction), Begin(transaction)),
        (Map("transaction" -> transaction, "a" -> "1"), Begin(transaction, Map("a" -> "1")))
      )

    forAll(data) { (m: Map[String, String], result: Begin) =>
      Given(s"the map $m")
      Then(s"Begin is $result")
      Begin(m) should equal(result)
    }

    a[NoSuchElementException] should be thrownBy {
      Begin(Map("foo" -> "bar"))
    }
  }
}