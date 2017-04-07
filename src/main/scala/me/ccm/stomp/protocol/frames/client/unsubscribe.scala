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

trait UnsubscribeHeaderKeys extends ClientHeaderKeys with CommonHeaderKeys {
  /** The Constant ID. */
  val ID: String = "id"
}

case class Unsubscribe(id: String,
                       additionalHeaders: Map[String, String] = Map.empty) extends ClientStompFrame {

  override val frameName: String = Unsubscribe.frameName

  override val headers: Map[String, String] =
    additionalHeaders ++
      Some(Unsubscribe.ID -> id)

  override val body: Array[Byte] = Array.emptyByteArray
}

object Unsubscribe extends ClientFrameProps with UnsubscribeHeaderKeys {
  override val frameName: String = "UNSUBSCRIBE"
  override val hasBody: Boolean = false

  def apply(headers: Map[String, String]): Unsubscribe = {
    val id = headers(ID)
    val additionalHeaders = headers - (ID)

    Unsubscribe(id, additionalHeaders)
  }
}
