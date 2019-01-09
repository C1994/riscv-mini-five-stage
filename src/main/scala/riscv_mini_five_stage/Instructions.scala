/*
* Filename : Instructions.scala
* Date     : 02-01-2019
* Author   : SunnyChen
*
* Instruction object for processor
* 02-01-2019 - implemented 7 basic
* */

package riscv_mini_five_stage

import chisel3._
import chisel3.util.BitPat

object Instructions {
  /* ALU operations */
  // Arithmetic operations
  def ADD   = BitPat("b0000000??????????000?????0110011")
  def SUB   = BitPat("b0100000??????????000?????0110011")
  def AND   = BitPat("b0000000??????????111?????0110011")
  def OR    = BitPat("b0000000??????????110?????0110011")
  def XOR   = BitPat("b0000000??????????100?????0110011")

  def ADDI  = BitPat("b?????????????????000?????0010011")
  def ANDI  = BitPat("b?????????????????111?????0010011")
  def ORI   = BitPat("b?????????????????110?????0010011")
  def XORI  = BitPat("b?????????????????100?????0010011")

  // Shift operations
  def SLL   = BitPat("b0000000??????????001?????0110011")
  def SRL   = BitPat("b0000000??????????101?????0110011")
  def SRA   = BitPat("b0100000??????????101?????0110011")

  def SLLI  = BitPat("b0000000??????????001?????0010011")
  def SRLI  = BitPat("b0000000??????????101?????0010011")
  def SRAI  = BitPat("b0100000??????????101?????0010011")

  /* Memory operations */
  // Load operations
  def LW    = BitPat("b?????????????????010?????0000011")

  // Store operations
  def SW    = BitPat("b?????????????????010?????0100011")

  /* Conditional Branch operations */
  def BEQ   = BitPat("b?????????????????000?????1100011")

  // NOP - ADDI x0, 0x00(x0)
  def NOP   = BitPat("b00000000000000000000000000010011")
}