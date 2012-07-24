DEVROOT = /opt/cs10q1
COMPROOT = $(DEVROOT)/bin
TCPREFIX = arm-none-linux-gnueabi
GCCVERSION = 4.4.1

TOOLCHAIN_PREFIX ?= $(COMPILER_PREFIX)$(COMPROOT)/$(TCPREFIX)-
CXX=$(TOOLCHAIN_PREFIX)g++
CC=$(TOOLCHAIN_PREFIX)gcc

include $(SID_DIR)/buildfiles/arch/_common.mk

CFLAGS+=-msoft-float -D__ARMELF__ -ffunction-sections -fdata-sections -Wno-psabi 

LDFLAGS += -Wl,--gc-sections

CFLAGS+= $(ARCH_FLAGS)

CRYPTO_OPT_CFLAGS ?= -O2 -fno-schedule-insns

# Don't remove this, or -O3 may end up on compiler command line
CFLAG_MAX_OPTIMIZE = -Os

ifeq ($(DEBUG),)
CFLAGS+= -Os
else
CFLAGS += -O0 -mapcs-frame
endif

ifneq ($(ENABLE_CONTEXT_DUMPING),)
CFLAGS += -mapcs-frame
endif

ifeq ($(SAMPLING_PROFILER_SUPPORT),yes)
CFLAGS += -fno-omit-frame-pointer
ifneq ($(ENABLE_CONTEXT_DUMPING),)
# already added
else
CFLAGS += -mapcs-frame
endif
endif
