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

import me.ccm.stomp.protocol.frames._
import me.ccm.stomp.protocol.frames.server.Error._

case class Message(destination: String,
                   messageId: String,
                   subscription: String,
                   ack: Option[String] = None,
                   contentLength: Option[Long] = None,
                   contentType: Option[String] = None,
                   body: Array[Byte] = Array.emptyByteArray,
                   additionalHeaders: Map[String, String] = Map.empty) extends ServerStompFrame with Body {

  override val command: String = Message.command

  override val headers: Map[String, String] =
    additionalHeaders ++
      Some(Message.DESTINATION -> destination) ++
      Some(Message.MESSAGE_ID -> messageId) ++
      Some(Message.SUBSCRIPTION -> subscription) ++
      ack.map(Message.ACK -> _) ++
      contentLength.map(Message.CONTENT_LENGTH -> _.toString) ++
      contentType.map(Message.CONTENT_TYPE -> _)
}

object Message extends ServerFrameProps
  with StandardStompHeader
  with AckHeader
  with SubscriptionHeader
  with MessageIdHeader
  with DestinationHeader {
  override val command: String = "MESSAGE"
  override val hasBody: Boolean = true

  def apply(headers: Map[String, String], body: Array[Byte]): Message = {
    val destination = headers(DESTINATION)
    val messageId = headers(MESSAGE_ID)
    val subscription = headers(SUBSCRIPTION)
    val ack = headers.get(ACK)
    val contentLength = headers.get(CONTENT_LENGTH).map(_.toLong)
    val contentType = headers.get(CONTENT_TYPE)
    val additionalHeaders = headers - (RECEIPT_ID, CONTENT_LENGTH, CONTENT_TYPE, MESSAGE)

    Message(destination, messageId, subscription, ack, contentLength, contentType, body, additionalHeaders)
  }
}
