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

import me.ccm.stomp.protocol.frames.server.Receipt
import me.ccm.stomp.protocol.frames.{HeaderKeys, StompFrame, StompFrameProps}


trait ClientHeaderKeys extends HeaderKeys {
  def RECEIPT: String = "receipt"
}

trait ClientStompFrame extends StompFrame {
  /**
    * @return the receipt stomp frame related to this given client frame, only if it contains a receipt id.
    */
  def receiptFrame(): Option[Receipt] =
    headers.get(ClientStompFrame.RECEIPT).map(id ⇒ Receipt(id))
}

object ClientStompFrame extends ClientHeaderKeys

trait ClientFrameProps extends StompFrameProps {
  override def isClientFrame: Boolean = true
}