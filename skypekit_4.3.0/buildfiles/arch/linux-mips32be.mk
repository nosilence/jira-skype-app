ARCH_FLAGS += -msoft-float -meb -mips32
LDFLAGS += -msoft-float -meb -mips32

#for cyassl
ARCH_FLAGS += -DBIG_ENDIAN_ORDER

include $(SID_DIR)/buildfiles/arch/_common-linux-mips.mk
