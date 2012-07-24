include $(SID_DIR)/buildfiles/arch/_common-linux-mips.mk

ARCH_FLAGS += -msoft-float -mel -mips32
LDFLAGS += -msoft-float -mel -mips32
CRTPREFIX := $(CRTPREFIX)/el
