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
class ConnectTest extends FlatSpec with Matchers with GivenWhenThen with TableDrivenPropertyChecks {
  val acceptVersion = "1.2"
  val host = "127.0.0.1"

  behavior of "The CONNECT frame"

  it must "be named connect" in {
    val f = Connect(acceptVersion, host)

    Connect.command should be("CONNECT")
    f.command should be("CONNECT")
  }

  it must "include an accept-version header" in {
    val f = Connect(acceptVersion, host)

    Connect.ACCEPT_VERSION should be("accept-version")
    f.acceptVersion should equal(acceptVersion)
    f.headers should contain(Connect.ACCEPT_VERSION -> acceptVersion)
    f.additionalHeaders should not contain key(Connect.ACCEPT_VERSION)
  }

  it must "include a host header" in {
    val f = Connect(acceptVersion, host)

    Connect.HOST should be("host")
    f.host should equal(host)
    f.headers should contain(Connect.HOST -> host)
    f.additionalHeaders should not contain key(Connect.HOST)
  }

  it must "include an optional login header" in {
    {
      Given("a CONNECT frame without login")
      val f = Connect(acceptVersion, host)

      Then("there must be no login at all")
      f.headers should not contain key(Connect.LOGIN)
      f.login should be(None)
      f.additionalHeaders should not contain key(Connect.LOGIN)
    }
    {
      Given("a CONNECT frame with login")
      val login = "john-doe"
      val f = Connect(acceptVersion, host, Some(login))

      Then("there must be a login")
      Connect.LOGIN should be("login")
      f.login should equal(Some(login))
      f.headers should contain(Connect.LOGIN -> login)
      f.additionalHeaders should not contain key(Connect.LOGIN)
    }
  }

  it must "include an optional passcode header" in {
    {
      Given("a CONNECT frame without passcode")
      val f = Connect(acceptVersion, host)

      Then("there must be no passcode at all")
      f.headers should not contain key(Connect.PASSCODE)
      f.passcode should be(None)
      f.additionalHeaders should not contain key(Connect.PASSCODE)
    }
    {
      Given("a CONNECT frame with passcode")
      val login = "john-doe"
      val passcode = "secret"
      val f = Connect(acceptVersion, host, Some(login), Some(passcode))

      Then("there must be a passcode")
      Connect.PASSCODE should be("passcode")
      f.login should equal(Some(login))
      f.headers should contain(Connect.PASSCODE -> passcode)
      f.additionalHeaders should not contain key(Connect.PASSCODE)
    }
  }

  it must "include an optional heart-beat header" in {
    {
      Given("a CONNECT frame without heart-beat")
      val f = Connect(acceptVersion, host)

      Then("there must be no heart-beat at all")
      f.headers should not contain key(Connect.HEART_BEAT)
      f.heartBeat should be(None)
      f.additionalHeaders should not contain key(Connect.HEART_BEAT)
    }
    {
      Given("a CONNECT frame with heart-beat")
      val heartBeat = "100"
      val f = Connect(acceptVersion, host, None, None, Some(heartBeat))

      Then("there must be a heart-beat")
      Connect.HEART_BEAT should be("heart-beat")
      f.heartBeat should equal(Some(heartBeat))
      f.headers should contain(Connect.HEART_BEAT -> heartBeat)
      f.additionalHeaders should not contain key(Connect.HEART_BEAT)
    }
  }

  it must "not have a body" in {
    val f = Connect(acceptVersion, host)

    Connect.hasBody should be(false)
  }

  it must "be correctly constructed from a map" in {
    val data =
      Table[Map[String, String], Connect](
        ("map", "result"),
        (Map.empty,
          Connect("1.0", "localhost")),
        (Map(
          "accept-version" -> acceptVersion,
          "host" -> host),
          Connect(acceptVersion, host)),
        (Map(
          "accept-version" -> acceptVersion,
          "host" -> host,
          "login" -> "john-doe",
          "passcode" -> "secret",
          "heart-beat" -> "1000"),
          Connect(acceptVersion, host, Some("john-doe"), Some("secret"), Some("1000")))
      )

    forAll(data) { (m, result) =>
      Given(s"the map $m")
      Then(s"Ack is $result")
      Connect(m) should equal(result)
    }
  }
}