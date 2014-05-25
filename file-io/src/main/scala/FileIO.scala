package com.tky.sample.fileio

import java.io.PrintWriter
import java.io.File
import scala.io.Source

trait FileWritable[A] {
  def format(v: A): String
}

trait FileReadable[A] {
  def parse(v: String): A
}

object DefaultSettings {
  implicit object StringWritable extends FileWritable[String] {
    def format(v: String): String = v
  }

  implicit object IntWritable extends FileWritable[Int] {
    def format(v: Int): String = v.toString
  }

  implicit object IntReadable extends FileReadable[Int] {
    def parse(v: String): Int = v.toInt
  }
}

object FileIO {

  def using[A <: {def close(): Unit}, B](param: A)(f: A => B): B = {
    try { f(param) } finally { param.close() }
  }

  def write(file: File, value: String): Unit = {
    using (new PrintWriter(file)) { writer =>
      writer.println(value)
    }
  }

  def writeStrings(file: File, values: Seq[String]): Unit = {
    using (new PrintWriter(file)) { writer =>
      values.foreach { v => 
        writer.println(v)
      }
    }
  }

  def writeObjects[A](file: File, values: Seq[A])(implicit w: FileWritable[A]): Unit = {
    using (new PrintWriter(file)) { writer =>
      values.foreach { v => 
        writer.println(w.format(v))
      }
    }
  }

  def read(file: File): Iterator[String] = {
    Source.fromFile(file).getLines
  }

  def readObjects[A](file: File)(implicit r: FileReadable[A]): Iterator[A] = {
    Source.fromFile(file).getLines.map { v =>
      r.parse(v)
    }
  }
}
