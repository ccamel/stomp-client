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

import me.ccm.stomp.protocol.frames.CommonHeaderKeys


trait ConnectHeaderKeys extends ClientHeaderKeys with CommonHeaderKeys {
  /** The Constant HEART_BEAT. */
  def HEART_BEAT: String = "heart-beat"

  /** The Constant PASSCODE. */
  def PASSCODE: String = "passcode"

  /** The Constant LOGIN. */
  def LOGIN: String = "login"

  /** The Constant HOST. */
  def HOST: String = "host"

  /** The Constant ACCEPT_VERSION. */
  def ACCEPT_VERSION: String = "accept-version"
}

case class Connect(acceptVersion: String,
                   host: String,
                   login: Option[String] = None,
                   passcode: Option[String] = None,
                   heartBeat: Option[String] = None,
                   additionalHeaders: Map[String, String] = Map.empty) extends ClientStompFrame {

  override val frameName: String = Connect.frameName

  override val headers: Map[String, String] =
    additionalHeaders ++
      Some(Connect.ACCEPT_VERSION -> acceptVersion) ++
      Some(Connect.HOST -> host) ++
      login.map(Connect.LOGIN -> _) ++
      passcode.map(Connect.PASSCODE -> _) ++
      heartBeat.map(Connect.HEART_BEAT -> _)

  override def body: Array[Byte] = Array.emptyByteArray
}

object Connect extends ClientFrameProps with ConnectHeaderKeys {
  override val frameName: String = "CONNECT"
  override val hasBody: Boolean = false

  def apply(headers: Map[String, String]): Connect = {
    val acceptVersion = headers.getOrElse(ACCEPT_VERSION, "1.0")
    val host = headers.getOrElse(HOST, "localhost")
    val login = headers.get(LOGIN)
    val passcode = headers.get(PASSCODE)
    val heartBeat = headers.get(HEART_BEAT)
    val additionalHeaders = headers - (ACCEPT_VERSION, HOST, LOGIN, PASSCODE, HEART_BEAT)

    Connect(acceptVersion, host, login, passcode, heartBeat, additionalHeaders)
  }
}
