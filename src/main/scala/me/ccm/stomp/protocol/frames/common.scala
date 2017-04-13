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
package me.ccm.stomp.protocol.frames

trait StompFrame extends Immutable {
  def frameName: String

  def headers: Map[String, String]

  def body: Array[Byte]

  def hasHeaders: Boolean = headers.nonEmpty

  def hasBodyContent: Boolean = !body.isEmpty
}

trait StompFrameProps extends Immutable {
  def isClientFrame: Boolean

  def isServerFrame: Boolean = !isClientFrame

  def hasBody: Boolean = false

  def frameName: String
}

trait StompHeader


trait ContentLengthHeader extends StompHeader {
  val CONTENT_LENGTH: String = "content-length"
}

trait ContentTypeHeader extends StompHeader {
  val CONTENT_TYPE: String = "content-type"
}

trait ReceiptHeader extends StompHeader {
  val RECEIPT: String = "receipt"
}

trait StandardStompHeader extends StompHeader
  with ContentLengthHeader
  with ContentTypeHeader
  with ReceiptHeader

trait AcceptVersionHeader extends StompHeader {
  val ACCEPT_VERSION: String = "accept-version"
}

trait HostHeader extends StompHeader {
  val HOST: String = "host"
}

trait LoginHeader extends StompHeader {
  val LOGIN: String = "login"
}

trait PasscodeHeader extends StompHeader {
  val PASSCODE: String = "passcode"
}

trait HeartBeatHeader extends StompHeader {
  val HEART_BEAT: String = "heart-beat"
}

trait VersionHeader extends StompHeader {
  val VERSION: String = "version"
}

trait SessionHeader extends StompHeader {
  val SESSION: String = "session"
}

trait ServerHeader extends StompHeader {
  val SERVER: String = "server"
}

trait DestinationHeader extends StompHeader {
  val DESTINATION: String = "destination"
}

trait TransactionHeader extends StompHeader {
  val TRANSACTION: String = "transaction"
}

trait IdHeader extends StompHeader {
  val ID: String = "id"
}

trait AckHeader extends StompHeader {
  val ACK: String = "ack"
}

trait MessageHeader extends StompHeader {
  val MESSAGE: String = "message"
}

trait SubscriptionHeader extends StompHeader {
  val SUBSCRIPTION: String = "subscription"
}

trait MessageIdHeader extends StompHeader {
  val MESSAGE_ID: String = "message-id"
}

trait ReceiptIdHeader extends StompHeader {
  val RECEIPT_ID: String = "receipt-id"
}

object Headers extends StompHeader
  with ContentLengthHeader
  with ContentTypeHeader
  with ReceiptHeader
  with AcceptVersionHeader
  with HostHeader
  with LoginHeader
  with PasscodeHeader
  with HeartBeatHeader
  with VersionHeader
  with SessionHeader
  with ServerHeader
  with DestinationHeader
  with TransactionHeader
  with IdHeader
  with AckHeader
  with MessageHeader
  with SubscriptionHeader
  with MessageIdHeader
  with ReceiptIdHeader