DEVROOT = /opt/cs09q2
COMPROOT = $(DEVROOT)/bin
TCPREFIX = mips-linux-gnu
GCCVERSION = 4.3.3

TOOLCHAIN_PREFIX ?= $(COMPILER_PREFIX) $(COMPROOT)/$(TCPREFIX)-
CXX=$(TOOLCHAIN_PREFIX)g++
CC=$(TOOLCHAIN_PREFIX)gcc

CRTPREFIX=/mips32/soft-float

include $(SID_DIR)/buildfiles/arch/_common.mk

CFLAGS+=-msoft-float
LDFLAGS+=-msoft-float

CFLAGS+= $(ARCH_FLAGS) -D_FILE_OFFSET_BITS=32

CRYPTO_OPT_CFLAGS ?= -O3

ifeq ($(DEBUG),)
CFLAGS+= -Os
else
CFLAGS += -O0 
endif

