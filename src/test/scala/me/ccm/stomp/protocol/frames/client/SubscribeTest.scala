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
class SubscribeTest extends FlatSpec with Matchers with GivenWhenThen with TableDrivenPropertyChecks {
  behavior of "The SUBSCRIBE frame"

  val destination = "/queue/foo/"
  val id = "id-0"
  val ack = "client"

  it must "be named subscribe" in {
    val f = Subscribe(destination, id, ack)

    Subscribe.frameName should be("SUBSCRIBE")
    f.frameName should be("SUBSCRIBE")
  }

  it must "include a destination header" in {
    val f = Subscribe(destination, id, ack)

    Subscribe.DESTINATION should be("destination")
    f.destination should equal(destination)
    f.headers should contain(Subscribe.DESTINATION -> destination)
    f.additionalHeaders should not contain key(Subscribe.DESTINATION)
  }

  it must "include a ack header" in {
    val f = Subscribe(destination, id, ack)

    Subscribe.ACK should be("ack")
    f.ack should equal(ack)
    f.headers should contain(Subscribe.ACK -> ack)
    f.additionalHeaders should not contain key(Subscribe.ACK)
  }

  it must "include an id header" in {
    val id = "123"
    val f = Subscribe(destination, id, ack)

    Subscribe.ID should be("id")
    f.id should equal(id)
    f.headers should contain(Subscribe.ID -> id)
    f.additionalHeaders should not contain key(Subscribe.ID)
  }

  it must "not have a body" in {
    val f = Subscribe(destination, id, ack)

    Subscribe.hasBody should be(false)
    f.body should be(emptyByteArray)
    f.hasBodyContent should be(false)
  }

  it must "be correctly constructed from a map" in {
    val data =
      Table(
        ("map", "result"),
        (Map(
          "destination" -> destination,
          "id" -> id,
          "ack" -> ack
        ),
          Subscribe(destination, id, ack))
      )

    forAll(data) { (m: Map[String, String], result: Subscribe) =>
      Given(s"the map $m")
      Then(s"Subscribe is $result")
      Subscribe(m) should equal(result)
    }

    a[NoSuchElementException] should be thrownBy {
      Subscribe(Map("foo" -> "bar"))
    }
  }
}