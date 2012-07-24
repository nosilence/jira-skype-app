SID_ARCH_INCLUDED := yes

ifeq ($(INTERNAL_BUILD),yes)
CFLAGS+= -g
endif

STLPORT_PATH ?= $(SID_DIR)/ext_libs/stlport/stlport
export STLPORT_PATH

ifeq ($(USE_SKYLIBC),yes)
        SKYLIBC_LDPREFLAGS += -static -nostdlib -L$(SKYLIBC)/lib $(SKYLIBC)/lib/crt1.o $(SKYLIBC)/lib/crti.o $(DEVROOT)/lib/gcc/$(TCPREFIX)/$(GCCVERSION)$(CRTPREFIX)/crtbegin.o -Wl,--gc-sections -Wl,--start-group
        SKYLIBC_LDPOSTFLAGS += -lstlport -lgcc -lc++ -lc -lsupc++ -lstdc++ -lgcc_eh -Wl,--eh-frame-hdr -Wl,--end-group $(DEVROOT)/lib/gcc/$(TCPREFIX)/$(GCCVERSION)$(CRTPREFIX)/crtend.o $(SKYLIBC)/lib/crtn.o
endif
