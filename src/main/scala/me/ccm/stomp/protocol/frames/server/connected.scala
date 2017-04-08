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

import me.ccm.stomp.protocol.frames.CommonHeaderKeys

trait ConnectedHeaderKeys extends ServerHeaderKeys with CommonHeaderKeys {
  /** The Constant SESSION. */
  val SESSION: String = "session"
  /** The Constant SERVER. */
  val SERVER: String = "server"
  /** The Constant HEART_BEAT. */
  val HEART_BEAT: String = "heart-beat"
  /** The Constant VERSION. */
  val VERSION: String = "version"
}

case class Connected(version: String,
                     session: Option[String] = None,
                     server: Option[String] = None,
                     heartBeat: Option[String] = None,
                     additionalHeaders: Map[String, String] = Map.empty) extends ServerStompFrame {

  override val frameName: String = Connected.frameName

  override val headers: Map[String, String] =
    additionalHeaders ++
      Some(Connected.VERSION -> version) ++
      session.map(Connected.SESSION -> _) ++
      server.map(Connected.SERVER -> _) ++
      heartBeat.map(Connected.HEART_BEAT -> _)

  override val body: Array[Byte] = Array.emptyByteArray
}

object Connected extends ServerFrameProps with ConnectedHeaderKeys {
  override val frameName: String = "CONNECTED"
  override val hasBody: Boolean = false

  def apply(headers: Map[String, String]): Connected = {
    val version = headers(VERSION)
    val session = headers.get(SESSION)
    val server = headers.get(SERVER)
    val heartBeat = headers.get(HEART_BEAT)
    val additionalHeaders = headers - (VERSION, SESSION, SERVER, HEART_BEAT)

    Connected(version, session, server, heartBeat, additionalHeaders)
  }
}
