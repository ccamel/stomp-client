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

/**
  *
  */
class UnsubscribeTest extends FlatSpec with Matchers with GivenWhenThen with TableDrivenPropertyChecks {
  behavior of "The UNSUBSCRIBE frame"

  val id = "id-0"

  it must "be named unsubscribe" in {
    val f = Unsubscribe(id)

    Unsubscribe.command should be("UNSUBSCRIBE")
    f.command should be("UNSUBSCRIBE")
  }

  it must "include an id header" in {
    val f = Unsubscribe(id)

    Unsubscribe.ID should be("id")
    f.id should equal(id)
    f.headers should contain(Unsubscribe.ID -> id)
    f.additionalHeaders should not contain key(Unsubscribe.ID)
  }

  it must "not have a body" in {
    val f = Unsubscribe(id)

    Unsubscribe.hasBody should be(false)
  }

  it must "be correctly constructed from a map" in {
    val data =
      Table(
        ("map", "result"),
        (Map(
          "id" -> id
        ),
          Unsubscribe(id)),
        (Map(
          "id" -> id,
          "foo" -> "bar"
        ),
          Unsubscribe(id, Map("foo" -> "bar")))
      )

    forAll(data) { (m: Map[String, String], result: Unsubscribe) =>
      Given(s"the map $m")
      Then(s"Unsubscribe is $result")
      Unsubscribe(m) should equal(result)
    }

    a[NoSuchElementException] should be thrownBy {
      Unsubscribe(Map("foo" -> "bar"))
    }
  }
}