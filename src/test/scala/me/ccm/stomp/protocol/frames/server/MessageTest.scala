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

import me.ccm.stomp.protocol.frames.client.Send
import org.scalatest._
import org.scalatest.prop.TableDrivenPropertyChecks

/**
  *
  */
class MessageTest extends FlatSpec with Matchers with GivenWhenThen with TableDrivenPropertyChecks {
  behavior of "The MESSAGE frame"

  val destination = "/queue/a"
  val messageId = "123"
  val subscription = "sub1"
  val ack = "ack1"
  val contentType = "text/plain"
  val body = "Hello world".getBytes

  it must "be named message" in {
    val f = Message(destination, messageId, subscription)

    Message.command should be("MESSAGE")
    f.command should be("MESSAGE")
  }

  it must "include a destination header" in {
    val f = Message(destination, messageId, subscription)

    Message.DESTINATION should be("destination")
    f.destination should equal(destination)
    f.headers should contain(Message.DESTINATION -> destination)
    f.additionalHeaders should not contain key(Message.DESTINATION)
  }

  it must "include a messageId header" in {
    val f = Message(destination, messageId, subscription)

    Message.MESSAGE_ID should be("message-id")
    f.messageId should equal(messageId)
    f.headers should contain(Message.MESSAGE_ID -> messageId)
    f.additionalHeaders should not contain key(Message.MESSAGE_ID)
  }

  it must "include a subscription header" in {
    val f = Message(destination, messageId, subscription)

    Message.SUBSCRIPTION should be("subscription")
    f.subscription should equal(subscription)
    f.headers should contain(Message.SUBSCRIPTION -> subscription)
    f.additionalHeaders should not contain key(Message.SUBSCRIPTION)
  }

  it must "include an optional ack header" in {
    {
      Given("a MESSAGE frame without ack")
      val f = Message(destination, messageId, subscription)

      Then("there must be no ack at all")
      f.headers should not contain key(Message.ACK)
      f.ack should be(None)
      f.additionalHeaders should not contain key(Message.ACK)
    }
    {
      Given("a MESSAGE frame with ack")
      val f = Message(destination, messageId, subscription, Some(ack))

      Then("there must be a ack")
      Message.ACK should be("ack")
      f.ack should equal(Some(ack))
      f.headers should contain(Message.ACK -> ack)
      f.additionalHeaders should not contain key(Message.ACK)
    }
  }

  it must "include an optional contentType header" in {
    {
      Given("a MESSAGE frame without contentType")
      val f = Message(destination, messageId, subscription)

      Then("there must be no contentType at all")
      f.headers should not contain key(Message.CONTENT_TYPE)
      f.ack should be(None)
      f.additionalHeaders should not contain key(Message.CONTENT_TYPE)
    }
    {
      Given("a MESSAGE frame with contentType")
      val f = Message(destination, messageId, subscription, None, None, Some(contentType))

      Then("there must be a ack")
      Message.CONTENT_TYPE should be("content-type")
      f.contentType should equal(Some(contentType))
      f.headers should contain(Message.CONTENT_TYPE -> contentType)
      f.additionalHeaders should not contain key(Message.CONTENT_TYPE)
    }
  }

  it must "have a body" in {
    val f = Message(destination, messageId, subscription, Some(ack), None, Some(contentType), body)

    Message.hasBody should be(true)
  }

  it must "be correctly constructed from a map" in {
    val data =
      Table(
        ("map", "result"),
        (Map[String, String](
          "destination" -> destination,
          "message-id" -> messageId,
          "subscription" -> subscription
        ),
          Message(destination, messageId, subscription)),

        (Map[String, String](
          "destination" -> destination,
          "message-id" -> messageId,
          "subscription" -> subscription,
          "ack" -> ack,
          "content-length" -> "0",
          "content-type" -> contentType
        ),
          Message(destination, messageId, subscription, Some(ack), Some(0), Some(contentType)))
      )

    forAll(data) { (m: Map[String, String], result: Message) =>
      val x = Message(m, Array.emptyByteArray)
      Given(s"the map $x")
      Then(s"Message is $result")
      Message(m, Array.emptyByteArray) should equal(result)
    }

    a[NoSuchElementException] should be thrownBy {
      Send(Map("foo" -> "bar"), Array.emptyByteArray)
    }
  }
}