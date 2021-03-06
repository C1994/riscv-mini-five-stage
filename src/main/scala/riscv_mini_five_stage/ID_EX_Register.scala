/*
* Filename : ID_EX_Register.scala
* Date     : 03-01-2019
* Author   : SunnyChen
*
* Pipeline register between ID stage and EX stage
* */

package riscv_mini_five_stage

import chisel3._

class ID_EX_Registerio extends Bundle with Config {
  val id_pc       = Input(UInt(WLEN.W))
  val id_pc_4     = Input(UInt(WLEN.W))
  val id_rs1_out  = Input(UInt(WLEN.W))
  val id_rs2_out  = Input(UInt(WLEN.W))
  val id_imm      = Input(UInt(WLEN.W))
  val id_inst     = Input(UInt(WLEN.W))
  val id_rs1      = Input(UInt(REG_LEN.W))
  val id_rs2      = Input(UInt(REG_LEN.W))
  val ID_EX_Flush = Input(UInt(ID_EX_FLUSH_SIG_LEN.W))

  /* control signals */
  // EX stage
  val ALU_Src    = Input(UInt(ALU_SRC_SIG_LEN.W))
  val ALUOp      = Input(UInt(ALUOP_SIG_LEN.W))
  val Branch     = Input(UInt(BRANCH_SIG_LEN.W))
  val Branch_Src = Input(UInt(BRANCH_SRC_SIG_LEN.W))
  val Jump_Type  = Input(UInt(JUMP_TYPE_SIG_LEN.W))
  val Imm_Sel    = Input(UInt(IMM_SEL_SIG_LEN.W))

  // MEM stage
  val Mem_Read   = Input(UInt(MEM_READ_SIG_LEN.W))
  val Mem_Write  = Input(UInt(MEM_WRITE_SIG_LEN.W))
  val Data_Size  = Input(UInt(DATA_SIZE_SIG_LEN.W))
  val Load_Type  = Input(UInt(LOAD_TYPE_SIG_LEN.W))

  // WB stage
  val Reg_Write  = Input(UInt(REGWRITE_SIG_LEN.W))
  val Mem_to_Reg = Input(UInt(REG_SRC_SIG_LEN.W))

  /* Output */
  // Control signals output
  val ex_ALU_Src    = Output(UInt(ALU_SRC_SIG_LEN.W))
  val ex_ALUOp      = Output(UInt(ALUOP_SIG_LEN.W))
  val ex_Branch     = Output(UInt(BRANCH_SIG_LEN.W))
  val ex_Branch_Src = Output(UInt(BRANCH_SRC_SIG_LEN.W))
  val ex_Jump_Type  = Output(UInt(JUMP_TYPE_SIG_LEN.W))
  val ex_Imm_Sel    = Output(UInt(IMM_SEL_SIG_LEN.W))
  val ex_Mem_Read   = Output(UInt(MEM_READ_SIG_LEN.W))
  val ex_Mem_Write  = Output(UInt(MEM_WRITE_SIG_LEN.W))
  val ex_Data_Size  = Output(UInt(DATA_SIZE_SIG_LEN.W))
  val ex_Load_Type  = Output(UInt(LOAD_TYPE_SIG_LEN.W))
  val ex_Reg_Write  = Output(UInt(REGWRITE_SIG_LEN.W))
  val ex_Mem_to_Reg = Output(UInt(REG_SRC_SIG_LEN.W))

  val ex_pc         = Output(UInt(WLEN.W))
  val ex_pc_4       = Output(UInt(WLEN.W))
  val ex_rs1_out    = Output(UInt(WLEN.W))
  val ex_rs2_out    = Output(UInt(WLEN.W))
  val ex_rd         = Output(UInt(REG_LEN.W))   /* < pass to the MEM/WB pipeline register*/
  val ex_imm        = Output(UInt(WLEN.W))
  val ex_rs1        = Output(UInt(REG_LEN.W))
  val ex_rs2        = Output(UInt(REG_LEN.W))
  val ex_inst       = Output(UInt(WLEN.W))
}

class ID_EX_Register extends Module with Config {
  val io = IO(new ID_EX_Registerio)

  // Reg for storing the signal and data
  val pc          = RegInit(0.U(WLEN.W))
  val pc_4        = RegInit(0.U(WLEN.W))
  val rs1_out     = RegInit(0.U(WLEN.W))
  val rs2_out     = RegInit(0.U(WLEN.W))
  val imm         = RegInit(0.U(WLEN.W))
  val inst        = RegInit(0.U(WLEN.W))
  val rs1         = RegInit(0.U(WLEN.W))
  val rs2         = RegInit(0.U(WLEN.W))
  val alu_src     = RegInit(0.U(ALU_SRC_SIG_LEN.W))
  val aluop       = RegInit(0.U(ALUOP_SIG_LEN.W))
  val branch      = RegInit(0.U(BRANCH_SIG_LEN.W))
  val branch_src  = RegInit(0.U(BRANCH_SRC_SIG_LEN.W))
  val jump_type   = RegInit(0.U(JUMP_TYPE_SIG_LEN.W))
  val mem_read    = RegInit(0.U(MEM_READ_SIG_LEN.W))
  val mem_write   = RegInit(0.U(MEM_WRITE_SIG_LEN.W))
  val data_size   = RegInit(0.U(DATA_SIZE_SIG_LEN.W))
  val load_type   = RegInit(0.U(LOAD_TYPE_SIG_LEN.W))
  val reg_write   = RegInit(0.U(REGWRITE_SIG_LEN.W))
  val mem_to_reg  = RegInit(0.U(REG_SRC_SIG_LEN.W))
  val imm_sel     = RegInit(0.U(IMM_SEL_SIG_LEN.W))

  // apply regs
  pc                := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.id_pc)
  pc_4              := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.id_pc_4)
  rs1_out           := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.id_rs1_out)
  rs2_out           := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.id_rs2_out)
  imm               := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.id_imm)
  inst              := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.id_inst)
  rs1               := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.id_rs1)
  rs2               := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.id_rs2)
  alu_src           := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.ALU_Src)
  aluop             := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.ALUOp)
  branch            := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.Branch)
  branch_src        := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.Branch_Src)
  jump_type         := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.Jump_Type)
  mem_read          := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.Mem_Read)
  mem_write         := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.Mem_Write)
  data_size         := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.Data_Size)
  load_type         := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.Load_Type)
  reg_write         := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.Reg_Write)
  mem_to_reg        := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.Mem_to_Reg)
  imm_sel           := Mux(io.ID_EX_Flush.toBool(), 0.U(WLEN.W), io.Imm_Sel)

  // output
  io.ex_ALU_Src     := alu_src
  io.ex_ALUOp       := aluop
  io.ex_Branch      := branch
  io.ex_Branch_Src  := branch_src
  io.ex_Jump_Type   := jump_type
  io.ex_Mem_Read    := mem_read
  io.ex_Mem_Write   := mem_write
  io.ex_Reg_Write   := reg_write
  io.ex_Mem_to_Reg  := mem_to_reg
  io.ex_Data_Size   := data_size
  io.ex_Load_Type   := load_type
  io.ex_pc          := pc
  io.ex_pc_4        := pc_4
  io.ex_rs1_out     := rs1_out
  io.ex_rs2_out     := rs2_out
  io.ex_rs1         := rs1
  io.ex_rs2         := rs2
  io.ex_imm         := imm
  io.ex_inst        := inst
  io.ex_rd          := inst(11, 7)
  io.ex_Imm_Sel     := imm_sel
}

object ID_EX_Register extends App {
  chisel3.Driver.execute(args, () => new ID_EX_Register)
}
