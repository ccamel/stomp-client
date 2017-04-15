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

import me.ccm.stomp.protocol.frames._
import me.ccm.stomp.protocol.frames.client.AckHeaderValue.withName

object AckHeaderValue {
  sealed abstract class EnumVal( val name: String) {
    override def toString: String = name
  }
  case object Auto extends EnumVal( "auto")
  case object Client extends EnumVal("client")
  case object ClientIndividual extends EnumVal("client-individual")

  def withName(v: String): Option[EnumVal] = v match {
    case Auto.name => Some(Auto)
    case Client.name => Some(Client)
    case ClientIndividual.name => Some(ClientIndividual)
    case _ => None
  }
}

case class Subscribe(destination: String,
                     id: String,
                     ack: AckHeaderValue.EnumVal,
                     additionalHeaders: Map[String, String] = Map.empty) extends ClientStompFrame {

  override val command: String = Subscribe.command

  override val headers: Map[String, String] =
    additionalHeaders ++
      Some(Subscribe.DESTINATION -> destination) ++
      Some(Subscribe.ID -> id) ++
      Some(Subscribe.ACK -> ack.name)
}

object Subscribe extends ClientFrameProps
  with StandardStompHeader
  with IdHeader
  with AckHeader
  with DestinationHeader {
  override val command: String = "SUBSCRIBE"
  override val hasBody: Boolean = false

  def apply(headers: Map[String, String]): Subscribe = {
    val destination = headers(DESTINATION)
    val id = headers(ID)
    val ack = withName(headers(ACK)).get
    val additionalHeaders = headers - (DESTINATION, ID, ACK)

    Subscribe(destination, id, ack, additionalHeaders)
  }
}
