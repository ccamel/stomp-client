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

import me.ccm.stomp.protocol.frames.CommonWithBodyHeaderKeys

trait ErrorHeaderKeys extends ServerHeaderKeys with CommonWithBodyHeaderKeys {
  /** The Constant MESSAGE. */
  val MESSAGE: String = "message"
  /** The Constant RECEIPT_ID. */
  val RECEIPT_ID: String = "receipt-id"
}

case class Error(receiptId: Option[String] = None,
                 contentType: Option[String] = None,
                 message: Option[String] = None,
                 body: Array[Byte] = Array.emptyByteArray,
                 additionalHeaders: Map[String, String] = Map.empty) extends ServerStompFrame {

  override val frameName: String = Error.frameName

  override val headers: Map[String, String] =
    additionalHeaders ++
      receiptId.map(Error.RECEIPT_ID -> _) ++
      (if (!body.isEmpty) Some(Error.CONTENT_LENGTH → body.length.toString) else None) ++
      contentType.map(Error.CONTENT_TYPE -> _) ++
      message.map(Error.MESSAGE -> _)
}

object Error extends ServerFrameProps with ErrorHeaderKeys {
  override val frameName: String = "ERROR"
  override val hasBody: Boolean = true

  def apply(headers: Map[String, String], body: Array[Byte]): Error = {
    val receiptId = headers.get(RECEIPT_ID)
    val contentType = headers.get(CONTENT_TYPE)
    val message = headers.get(MESSAGE)
    val additionalHeaders = headers - (RECEIPT_ID, CONTENT_LENGTH, CONTENT_TYPE, MESSAGE)

    Error(receiptId, contentType, message, body, additionalHeaders)
  }
}
