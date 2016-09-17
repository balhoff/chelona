/*
* Copyright (C) 2014, 2015, 2016 Juergen Pfundt
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.chelona

object TurtleBitValue {
  final val EMPTY = 0

  final val BLANK_NODE_LABEL = 1
  final val ISA = BLANK_NODE_LABEL << 1
  final val IRIREF = ISA << 1
  final val PREFIXID = IRIREF << 1
  final val BASE = PREFIXID << 1
  final val SPARQLBASE = BASE << 1
  final val SPARQLPREFIX = SPARQLBASE << 1
  final val INTEGER = SPARQLPREFIX << 1
  final val DECIMAL = INTEGER << 1
  final val DOUBLE = DECIMAL << 1
  final val STRING_LITERAL_QUOTE = IRIREF << 1
  final val LANGTAG = STRING_LITERAL_QUOTE << 1
  final val ANON = LANGTAG << 1
  final val BOOLEAN_LITERAL = ANON << 1
  final val PNAMENS = BOOLEAN_LITERAL << 1
  final val PNAMELN = PNAMENS << 1
  final val PNPREFIX = PNAMELN << 1
  final val PNLOCAL = PNPREFIX << 1
  final val BLANK_LINE = PNLOCAL << 1
  final val COMMENT = BLANK_LINE << 1

  type TurtleToken = Int

  @inline
  def isBLANK_NODE_LABEL (`type`: TurtleToken) = (`type` & BLANK_NODE_LABEL) == BLANK_NODE_LABEL

  @inline
  def isIRIREF (`type`: TurtleToken) = (`type` & IRIREF) == IRIREF
}