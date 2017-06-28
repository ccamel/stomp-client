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

package me.ccm.stomp.protocol.frames.server

import org.scalatest._
import org.scalatest.prop.TableDrivenPropertyChecks

/**
  *
  */
class ReceiptTest extends FlatSpec with Matchers with GivenWhenThen with TableDrivenPropertyChecks {
  val receiptId = "123"

  behavior of "The RECEIPT frame"

  it must "be named receipt" in {
    val f = Receipt(receiptId)

    Receipt.command should be("RECEIPT")
    f.command should be("RECEIPT")
  }

  it must "include an receiptId header" in {
    val f = Receipt(receiptId)

    Receipt.RECEIPT_ID should be("receipt-id")
    f.receiptId should equal(receiptId)
    f.headers should contain(Receipt.RECEIPT_ID -> receiptId)
    f.additionalHeaders should not contain key(Receipt.RECEIPT_ID)
  }

  it must "not have a body" in {
    val f = Receipt(receiptId)

    Receipt.hasBody should be(false)
  }

  it must "be correctly constructed from a map" in {
    val data =
      Table(
        ("map", "result"),
        (Map("receipt-id" -> receiptId), Receipt(receiptId)),
        (Map("receipt-id" -> receiptId, "a" -> "1", "b" -> "2"), Receipt("123", Map("a" -> "1", "b" -> "2")))
      )

    forAll(data) { (m: Map[String, String], result: Receipt) =>
      Given(s"the map $m")
      Then(s"Receipt is $result")
      Receipt(m) should equal(result)
    }

    a[NoSuchElementException] should be thrownBy {
      Receipt(Map("foo" -> "bar"))
    }
  }
}