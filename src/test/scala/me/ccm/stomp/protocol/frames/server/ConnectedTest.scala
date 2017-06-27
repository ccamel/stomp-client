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
class ConnectedTest extends FlatSpec with Matchers with GivenWhenThen with TableDrivenPropertyChecks {
  val version = "1.2"

  behavior of "The CONNECTED frame"

  it must "be named connected" in {
    val f = Connected(version)

    Connected.command should be("CONNECTED")
    f.command should be("CONNECTED")
  }

  it must "include a version header" in {
    val f = Connected(version)

    Connected.VERSION should be("version")
    f.version should equal(version)
    f.headers should contain(Connected.VERSION -> version)
    f.additionalHeaders should not contain key(Connected.VERSION)
  }

  it must "include an optional session header" in {
    {
      Given("a CONNECTED frame without session")
      val f = Connected(version)

      Then("there must be no session at all")
      f.headers should not contain key(Connected.SESSION)
      f.session should be(None)
      f.additionalHeaders should not contain key(Connected.SESSION)
    }
    {
      Given("a CONNECTED frame with session")
      val session = "s123"
      val f = Connected(version, Some(session))

      Then("there must be a session")
      Connected.SESSION should be("session")
      f.session should equal(Some(session))
      f.headers should contain(Connected.SESSION -> session)
      f.additionalHeaders should not contain key(Connected.SESSION)
    }
  }

  it must "include an optional server header" in {
    {
      Given("a CONNECTED frame without server")
      val f = Connected(version)

      Then("there must be no server at all")
      f.headers should not contain key(Connected.SERVER)
      f.session should be(None)
      f.additionalHeaders should not contain key(Connected.SERVER)
    }
    {
      Given("a CONNECTED frame with server")
      val server = "srv123"
      val f = Connected(version, None, Some(server))

      Then("there must be a server")
      Connected.SERVER should be("server")
      f.server should equal(Some(server))
      f.headers should contain(Connected.SERVER -> server)
      f.additionalHeaders should not contain key(Connected.SERVER)
    }
  }

  it must "include an optional heart-beat header" in {
    {
      Given("a CONNECT frame without heart-beat")
      val f = Connected(version)

      Then("there must be no heart-beat at all")
      f.headers should not contain key(Connected.HEART_BEAT)
      f.heartBeat should be(None)
      f.additionalHeaders should not contain key(Connected.HEART_BEAT)
    }
    {
      Given("a CONNECTED frame with heart-beat")
      val heartBeat = "100"
      val f = Connected(version, None, None, Some(heartBeat))

      Then("there must be a heart-beat")
      Connected.HEART_BEAT should be("heart-beat")
      f.heartBeat should equal(Some(heartBeat))
      f.headers should contain(Connected.HEART_BEAT -> heartBeat)
      f.additionalHeaders should not contain key(Connected.HEART_BEAT)
    }
  }

  it must "not have a body" in {
    val f = Connected(version)

    Connected.hasBody should be(false)
  }

  it must "be correctly constructed from a map" in {
    val data =
      Table[Map[String, String], Connected](
        ("map", "result"),
        (Map(
          "version" -> version),
          Connected(version)),
        (Map(
          "version" -> version,
          "session" -> "s123",
          "server" -> "srv123",
          "heart-beat" -> "1000"),
          Connected(version, Some("s123"), Some("srv123"), Some("1000")))
      )

    forAll(data) { (m, result) =>
      Given(s"the map $m")
      Then(s"Connected is $result")
      Connected(m) should equal(result)
    }
  }
}