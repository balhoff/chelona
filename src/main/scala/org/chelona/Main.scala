package org.chelona

import java.io.{ OutputStreamWriter, BufferedWriter }
import java.nio.charset.StandardCharsets

import org.chelona.GetCmdLineArgs._
import org.parboiled2.{ ParseError, ParserInput }

import scala.util.Failure

/*
* Copyright (C) 2014 Juergen Pfundt
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
object Main extends App {

  val cmdLineArgs = argsParser.parse(args, Config())

  if (cmdLineArgs == None) {
    sys.exit(1)
  }

  if (cmdLineArgs.get.version) {
    System.err.println("Cheló̱n version 0.8")
    sys.exit(2)
  }

  val file = cmdLineArgs.get.file
  val validate = cmdLineArgs.get.validate

  System.err.println((if (!validate) "Convert: " else "Validate: ") + file(0))
  System.err.flush()

  val ms: Double = System.currentTimeMillis

  lazy val inputfile: ParserInput = io.Source.fromFile(file(0)).mkString

  val bo = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8))

  val parser = ChelonaParser(inputfile, bo, validate, cmdLineArgs.get.out.toLowerCase() != "raw")

  val res = parser.turtleDoc.run()

  bo.close()

  res match {
    case scala.util.Success(tripleCount) ⇒
      val me: Double = System.currentTimeMillis - ms
      if (!validate) {
        System.err.println("Input file '" + file(0) + ": " + (me / 1000.0) + "sec " + tripleCount + " triples (triples per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")")
      } else {
        System.err.println("Input file '" + file(0) + "' composed of " + tripleCount + " statements successfully validated in " + (me / 1000.0) + "sec (statements per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")")
      }
    case Failure(e: ParseError) ⇒ System.err.println("File '" + file(0) + "': " + parser.formatError(e))
    case Failure(e)             ⇒ System.err.println("File '" + file(0) + "': Unexpected error during parsing run: " + e)
  }
}