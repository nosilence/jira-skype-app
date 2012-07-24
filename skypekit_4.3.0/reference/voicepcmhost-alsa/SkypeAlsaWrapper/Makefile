SID_DIR=../../..
include $(SID_DIR)/buildfiles/makefile.header.mk

ifndef config
  config=release-x86
endif
export config

ifndef verbose
  SILENT = @
endif

ifndef CC
  CC = gcc
endif

ifndef CXX
  CXX = g++
endif

ifndef AR
  AR = ar
endif

ifeq ($(config),debug-x86)
  OBJDIR     = obj/Debug-x86
  TARGETDIR  = .
  TARGET     = $(TARGETDIR)/libSkypeAlsaWrapper.so
  DEFINES   += -D_DEBUG -DDEBUG_
  CPPFLAGS  += -fPIC -g -MMD -MP $(DEFINES) $(INCLUDES)
  CFLAGS    += $(CPPFLAGS) $(ARCH) -Wall -g -fno-use-cxa-atexit
  CXXFLAGS  += $(CFLAGS) -fno-rtti
  LDFLAGS   +=
  LIBS      += -lasound -lc
  RESFLAGS  += $(DEFINES) $(INCLUDES) 
  LDDEPS    += 
  LINKCMD    = $(CXX) -shared -g -o $(TARGET) $(OBJECTS) $(LDFLAGS) $(RESOURCES) $(ARCH) $(LIBS)
  define PREBUILDCMDS
  endef
  define PRELINKCMDS
  endef
  define POSTBUILDCMDS
  endef
endif

ifeq ($(config),release-x86)
  OBJDIR     = obj/Release-x86
  TARGETDIR  = .
  TARGET     = $(TARGETDIR)/libSkypeAlsaWrapper.so
  DEFINES   += -DNDEBUG
  CPPFLAGS  += -fPIC -MMD -MP $(DEFINES) $(INCLUDES)
  CFLAGS    += $(CPPFLAGS) $(ARCH) -Wall -O2 -fno-use-cxa-atexit
  CXXFLAGS  += $(CFLAGS) -fno-rtti
  LDFLAGS   +=
  LIBS      += -lasound -lc
  RESFLAGS  += $(DEFINES) $(INCLUDES) 
  LDDEPS    += 
  LINKCMD    = $(CXX) -shared -o $(TARGET) $(OBJECTS) $(LDFLAGS) $(RESOURCES) $(ARCH) $(LIBS)
  define PREBUILDCMDS
  endef
  define PRELINKCMDS
  endef
  define POSTBUILDCMDS
  endef
endif

OBJECTS := \
	$(OBJDIR)/AlsaDeviceDetection.o \
	$(OBJDIR)/AlsaFormat.o \
	$(OBJDIR)/AlsaBackend.o \
	$(OBJDIR)/AlsaHardwareParams.o \
	$(OBJDIR)/AlsaSoftwareParams.o \
	$(OBJDIR)/AlsaVolumeControl.o \
	$(OBJDIR)/AudioStream.o \
	$(OBJDIR)/DeviceInfo.o \
	$(OBJDIR)/StreamSpec.o

RESOURCES := \

SHELLTYPE := msdos
ifeq (,$(ComSpec)$(COMSPEC))
  SHELLTYPE := posix
endif
ifeq (/bin,$(findstring /bin,$(SHELL)))
  SHELLTYPE := posix
endif

.PHONY: clean prebuild prelink

all: $(TARGETDIR) $(OBJDIR) prebuild prelink $(TARGET)
	@:

$(TARGET): $(GCH) $(OBJECTS) $(LDDEPS) $(RESOURCES)
	@echo Linking SkypeAlsaWrapper
	$(SILENT) $(LINKCMD)
	$(POSTBUILDCMDS)

$(TARGETDIR):
	@echo Creating $(TARGETDIR)
ifeq (posix,$(SHELLTYPE))
	$(SILENT) mkdir -p $(TARGETDIR)
else
	$(SILENT) mkdir $(subst /,\\,$(TARGETDIR))
endif

$(OBJDIR):
	@echo Creating $(OBJDIR)
ifeq (posix,$(SHELLTYPE))
	$(SILENT) mkdir -p $(OBJDIR)
else
	$(SILENT) mkdir $(subst /,\\,$(OBJDIR))
endif

clean:
	@echo Cleaning SkypeAlsaWrapper
ifeq (posix,$(SHELLTYPE))
	$(SILENT) rm -f  $(TARGET)
	$(SILENT) rm -rf $(OBJDIR)
else
	$(SILENT) if exist $(subst /,\\,$(TARGET)) del $(subst /,\\,$(TARGET))
	$(SILENT) if exist $(subst /,\\,$(OBJDIR)) rmdir /s /q $(subst /,\\,$(OBJDIR))
endif

prebuild:
	$(PREBUILDCMDS)

prelink:
	$(PRELINKCMDS)

ifneq (,$(PCH))
$(GCH): $(PCH)
	@echo $(notdir $<)
	-$(SILENT) cp $< $(OBJDIR)
	$(SILENT) $(CXX) $(CXXFLAGS) -o "$@" -c "$<"
endif

$(OBJDIR)/%.o: ./%.cpp
	@echo $(notdir $<)
	$(SILENT) $(CXX) $(CXXFLAGS) -o "$@" -c "$<"
