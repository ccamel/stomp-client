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

import me.ccm.stomp.protocol.frames.{IdHeader, StandardStompHeader, TransactionHeader}

case class Nack(id: String,
                transaction: Option[String] = None,
                additionalHeaders: Map[String, String] = Map.empty) extends ClientStompFrame {

  override val command: String = Nack.command

  override val headers: Map[String, String] =
    additionalHeaders ++
      Some(Nack.ID -> id) ++
      transaction.map(Nack.TRANSACTION -> _)
}

object Nack extends ClientFrameProps with StandardStompHeader with TransactionHeader with IdHeader {
  override val command: String = "NACK"
  override val hasBody: Boolean = false

  def apply(headers: Map[String, String]): Nack = {
    val id = headers(ID)
    val transaction = headers.get(TRANSACTION)
    val additionalHeaders = headers - (ID, TRANSACTION)

    Nack(id, transaction, additionalHeaders)
  }
}
