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

import me.ccm.stomp.protocol.frames.{DestinationHeader, StandardStompHeader, TransactionHeader}

case class Send(destination: String,
                transaction: Option[String] = None,
                contentType: Option[String] = None,
                body: Array[Byte] = Array.emptyByteArray,
                additionalHeaders: Map[String, String] = Map.empty) extends ClientStompFrame {

  override val frameName: String = Send.frameName
  override val headers: Map[String, String] =
    additionalHeaders ++
      Some(Send.DESTINATION -> destination) ++
      transaction.map(Send.TRANSACTION -> _) ++
      contentLength.map(Send.CONTENT_LENGTH -> _) ++
      contentType.map(Send.CONTENT_TYPE -> _)

  def contentLength: Option[String] = if (!body.isEmpty) Some(body.length.toString) else None

}

object Send extends ClientFrameProps with StandardStompHeader with TransactionHeader with DestinationHeader {
  override val frameName: String = "SEND"
  override val hasBody: Boolean = true

  def apply(headers: Map[String, String], body: Array[Byte]): Send = {
    val destination = headers(DESTINATION)
    val transaction = headers.get(TRANSACTION)
    val contentType = headers.get(CONTENT_TYPE)
    val additionalHeaders = headers - (DESTINATION, TRANSACTION, CONTENT_LENGTH, CONTENT_TYPE)

    Send(destination, transaction, contentType, body, additionalHeaders)
  }
}
