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
class ErrorTest extends FlatSpec with Matchers with GivenWhenThen with TableDrivenPropertyChecks {
  behavior of "The ERROR frame"

  val body = "Hello world".getBytes
  val receiptId = "message-12345"
  val contentType = "text/plain"
  val contentLength = body.length
  val message = "malformed frame received"

  it must "be named error" in {
    val f = Error()

    Error.frameName should be("ERROR")
    f.frameName should be("ERROR")
  }

  it must "include an optional receiptId header" in {
    {
      Given("a ERROR frame without receiptId")
      val f = Error()

      Then("there must be no receiptId at all")
      f.headers should not contain key(Error.RECEIPT_ID)
      f.receiptId should be(None)
      f.additionalHeaders should not contain key(Error.RECEIPT_ID)
    }
    {
      Given("a ERROR frame with receiptId")
      val f = Error(Some(receiptId))

      Then("there must be a receiptId")
      Error.RECEIPT_ID should be("receipt-id")
      f.receiptId should equal(Some(receiptId))
      f.headers should contain(Error.RECEIPT_ID -> receiptId)
      f.additionalHeaders should not contain key(Error.RECEIPT_ID)
    }
  }

  it must "include an optional content-type header" in {
    {
      Given("a ERROR frame without content-type")
      val f = Error()

      Then("there must be no content-type at all")
      f.headers should not contain key(Error.CONTENT_TYPE)
      f.contentType should be(None)
      f.additionalHeaders should not contain key(Error.CONTENT_TYPE)
    }
    {
      Given("a ERROR frame with content-type")
      val f = Error(Some(receiptId), None, Some(contentType))

      Then("there must be a content-type")
      Error.CONTENT_TYPE should be("content-type")
      f.contentType should equal(Some(contentType))
      f.headers should contain(Error.CONTENT_TYPE -> contentType)
      f.additionalHeaders should not contain key(Error.CONTENT_TYPE)
    }
  }

  it must "include an optional content-length header" in {
    {
      Given("a ERROR frame without content-length")
      val f = Error()

      Then("there must be no content-type at all")
      f.headers should not contain key(Error.CONTENT_LENGTH)
      f.contentLength should be(None)
      f.additionalHeaders should not contain key(Error.CONTENT_LENGTH)
    }
    {
      Given("a ERROR frame with content-length")
      val f = Error(Some(receiptId), Some(contentLength.toLong))

      Then("there must be a content-length")
      Error.CONTENT_LENGTH should be("content-length")
      f.contentLength should equal(Some(contentLength))
      f.headers should contain(Error.CONTENT_LENGTH -> contentLength.toString)
      f.additionalHeaders should not contain key(Error.CONTENT_LENGTH)
    }
  }

  it must "include an optional message header" in {
    {
      Given("a ERROR frame without message")
      val f = Error()

      Then("there must be no message at all")
      f.headers should not contain key(Error.MESSAGE)
      f.message should be(None)
      f.additionalHeaders should not contain key(Error.MESSAGE)
    }
    {
      Given("a ERROR frame with message")
      val f = Error(Some(receiptId), Some(contentLength), Some(contentType), Some(message))

      Then("there must be a message")
      Error.MESSAGE should be("message")
      f.message should equal(Some(message))
      f.headers should contain(Error.MESSAGE -> message)
      f.additionalHeaders should not contain key(Error.MESSAGE)
    }
  }

  it must "have a body" in {
    val f = Error(Some(receiptId), None, None, None, body)

    Error.hasBody should be(true)
    f.body should be(body)
    f.hasBodyContent should be(true)
  }

//  it must "be correctly constructed from a map" in {
//    val data =
//      Table(
//        ("map", "result"),
//        (Map("transaction" -> transaction), Error(transaction)),
//        (Map("transaction" -> transaction, "a" -> "1"), Error(transaction, Map("a" -> "1")))
//      )
//
//    forAll(data) { (m: Map[String, String], result: Error) =>
//      Given(s"the map $m")
//      Then(s"Error is $result")
//      Error(m) should equal(result)
//    }
//
//    a[NoSuchElementException] should be thrownBy {
//      Error(Map("foo" -> "bar"))
//    }
//  }
}