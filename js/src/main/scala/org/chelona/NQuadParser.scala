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

import org.chelona.NQuadParser.NQAST
import org.parboiled2._

import scala.collection.mutable
import scala.io.BufferedSource
import scala.scalajs.js.annotation.JSExport
import scala.util.{ Failure, Success }

@JSExport
object NQuadParser {

  def apply(input: ParserInput, renderStatement: (NTripleAST) ⇒ Int, validate: Boolean = false, basePath: String = "http://chelona.org", label: String = "") = {
    new NQuadParser(input, renderStatement, validate, basePath, label)
  }

  sealed trait NQAST extends NQuadAST
}

class NQuadParser(input: ParserInput, renderStatement: (NTripleAST) ⇒ Int, validate: Boolean = false, basePath: String = "http://chelona.org", label: String = "") extends NTriplesParser(input: ParserInput, renderStatement: (NTripleAST) ⇒ Int, validate, basePath, label) with NQAST {

  //[1]	nquadsDoc	::=	statement? (EOL statement)* EOL?
  def nquadsDoc = rule {
    (statement ~> ((ast: NTripleType) ⇒
      if (!__inErrorAnalysis) {
        if (!validate) {
          renderStatement(ast)
        } else
          ast match {
            case ASTComment(s) ⇒ 0
            case _             ⇒ 1
          }
      } else { 0 })).*(EOL) ~ EOL.? ~ EOI ~> ((v: Seq[Int]) ⇒ {
      v.sum
    })
  }

  //[2]	statement	::=	subject predicate object graphLabel? '.'
  def statement: Rule1[NTripleType] = rule {
    ws ~ (subject ~ predicate ~ `object` ~ graphLabel.? ~ '.' ~ ws ~ comment.? ~> ASTStatement | comment ~> ASTTripleComment) | quiet(anyOf(" \t").+) ~ push("") ~> ASTBlankLine
  }

  //[6]	graphLabel	::=	IRIREF | BLANK_NODE_LABEL
  def graphLabel = rule {
    (IRIREF | BLANK_NODE_LABEL) ~> ASTGraphLabel
  }
}