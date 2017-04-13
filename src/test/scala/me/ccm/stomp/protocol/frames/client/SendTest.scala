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
  behavior of "The SEND frame"

  val destination = "/queue/a"
  val transaction = "tx1"
  val contentType = "text/plain"
  val body = "Hello world".getBytes

  it must "be named send" in {
    val f = Send(destination)

    Send.frameName should be("SEND")
    f.frameName should be("SEND")
  }

  it must "include a destination header" in {
    val f = Send(destination)

    Send.DESTINATION should be("destination")
    f.destination should equal(destination)
    f.headers should contain(Send.DESTINATION -> destination)
    f.additionalHeaders should not contain key(Send.DESTINATION)
  }

  it must "include an optional transaction header" in {
    {
      Given("a SEND frame without transaction")
      val f = Send(destination)

      Then("there must be no transaction at all")
      f.headers should not contain key(Send.TRANSACTION)
      f.transaction should be(None)
      f.additionalHeaders should not contain key(Send.TRANSACTION)
    }
    {
      Given("a SEND frame with transaction")
      val f = Send(destination, Some(transaction))

      Then("there must be a transaction")
      Send.TRANSACTION should be("transaction")
      f.transaction should equal(Some(transaction))
      f.headers should contain(Send.TRANSACTION -> transaction)
      f.additionalHeaders should not contain key(Send.TRANSACTION)
    }
  }

  it must "include an optional content-type header" in {
    {
      Given("a SEND frame without content-type")
      val f = Send(destination)

      Then("there must be no content-type at all")
      f.headers should not contain key(Send.CONTENT_TYPE)
      f.contentType should be(None)
      f.additionalHeaders should not contain key(Send.CONTENT_TYPE)
    }
    {
      Given("a SEND frame with content-type")
      val f = Send(destination, None, Some(contentType))

      Then("there must be a content-type")
      Send.CONTENT_TYPE should be("content-type")
      f.contentType should equal(Some(contentType))
      f.headers should contain(Send.CONTENT_TYPE -> contentType)
      f.additionalHeaders should not contain key(Send.CONTENT_TYPE)
    }
  }

  it must "have a body" in {
    val f = Send(destination, None, None, body)

    Send.hasBody should be(true)
    f.body should be(body)
    f.hasBodyContent should be(true)
  }

  it must "have a content-length when frame has a body" in {
    val f = Send(destination, None, None, body)

    f.contentLength should be (Some(body.length.toString))
  }

  it must "be correctly constructed from a map" in {
    val data =
      Table(
        ("map", "result"),
        (Map(
          "destination" -> destination
        ),
          Send(destination)),
        (Map(
          "destination" -> destination,
          "transaction" -> transaction,
          "content-type" -> contentType
        ),
          Send(destination, Some(transaction), Some(contentType)))
      )

    forAll(data) { (m: Map[String, String], result: Send) =>
      Given(s"the map $m")
      Then(s"Send is $result")
      Send(m, Array.emptyByteArray) should equal(result)
    }

    a[NoSuchElementException] should be thrownBy {
      Send(Map("foo" -> "bar"), Array.emptyByteArray)
    }
  }
}