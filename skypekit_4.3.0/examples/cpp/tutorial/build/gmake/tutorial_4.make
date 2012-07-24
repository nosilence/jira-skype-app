# GNU Make project makefile autogenerated by Premake
ifndef config
  config=debug
endif

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

ifeq ($(config),debug)
  OBJDIR     = obj/Debug/tutorial_4
  TARGETDIR  = ../../step4
  TARGET     = $(TARGETDIR)/tutorial_4
  DEFINES   += -D_DEBUG -DDEBUG -DSSL_LIB_CYASSL -DNO_FILESYSTEM
  INCLUDES  += -I../../../../../interfaces/skype/cpp_embedded/src/api -I../../../../../interfaces/skype/cpp_embedded/src/ipc -I../../../../../interfaces/skype/cpp_embedded/src/types -I../../../../../interfaces/skype/cpp_embedded/src/platform/threading -I../../../../../interfaces/skype/cpp_embedded/src/platform/transport -I../../../../../interfaces/skype/cpp_embedded/src/platform/transport/tcp -I../../../../../ipc/cpp -I../../../../../ipc/cpp/platform/se -I../../../../../ipc/cpp/ssl/cyassl/include -I../../../../../ipc/cpp/ssl/cyassl/ctaocrypt/include -I../../../../../ipc/cpp/ssl/cyassl/include/openssl -I../../keypair -I../../common -I../../../../../interfaces/skype/cpp_embedded/src/platform/threading/pthread
  CPPFLAGS  += -MMD -MP $(DEFINES) $(INCLUDES)
  CFLAGS    += $(CPPFLAGS) $(ARCH) -Wall -g
  CXXFLAGS  += $(CFLAGS) -fno-exceptions -fno-rtti
  LDFLAGS   += -L../../../../../interfaces/skype/cpp_embedded/build -L../../../../../bin/linux-x86
  LIBS      += -lskypekit-cppwrapper_2_lib -lskypekit-cyassl_lib -lpthread
  RESFLAGS  += $(DEFINES) $(INCLUDES) 
  LDDEPS    += 
  LINKCMD    = $(CXX) -o $(TARGET) $(OBJECTS) $(LDFLAGS) $(RESOURCES) $(ARCH) $(LIBS)
  define PREBUILDCMDS
  endef
  define PRELINKCMDS
  endef
  define POSTBUILDCMDS
  endef
endif

ifeq ($(config),release)
  OBJDIR     = obj/Release/tutorial_4
  TARGETDIR  = ../../step4
  TARGET     = $(TARGETDIR)/tutorial_4
  DEFINES   += -DNDEBUG -DSSL_LIB_CYASSL -DNO_FILESYSTEM
  INCLUDES  += -I../../../../../interfaces/skype/cpp_embedded/src/api -I../../../../../interfaces/skype/cpp_embedded/src/ipc -I../../../../../interfaces/skype/cpp_embedded/src/types -I../../../../../interfaces/skype/cpp_embedded/src/platform/threading -I../../../../../interfaces/skype/cpp_embedded/src/platform/transport -I../../../../../interfaces/skype/cpp_embedded/src/platform/transport/tcp -I../../../../../ipc/cpp -I../../../../../ipc/cpp/platform/se -I../../../../../ipc/cpp/ssl/cyassl/include -I../../../../../ipc/cpp/ssl/cyassl/ctaocrypt/include -I../../../../../ipc/cpp/ssl/cyassl/include/openssl -I../../keypair -I../../common -I../../../../../interfaces/skype/cpp_embedded/src/platform/threading/pthread
  CPPFLAGS  += -MMD -MP $(DEFINES) $(INCLUDES)
  CFLAGS    += $(CPPFLAGS) $(ARCH) -Wall -O2
  CXXFLAGS  += $(CFLAGS) -fno-exceptions -fno-rtti
  LDFLAGS   += -s -L../../../../../interfaces/skype/cpp_embedded/build -L../../../../../bin/linux-x86
  LIBS      += -lskypekit-cppwrapper_2_lib -lskypekit-cyassl_lib -lpthread
  RESFLAGS  += $(DEFINES) $(INCLUDES) 
  LDDEPS    += 
  LINKCMD    = $(CXX) -o $(TARGET) $(OBJECTS) $(LDFLAGS) $(RESOURCES) $(ARCH) $(LIBS)
  define PREBUILDCMDS
  endef
  define PRELINKCMDS
  endef
  define POSTBUILDCMDS
  endef
endif

OBJECTS := \
	$(OBJDIR)/tutorial_4.o \

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
	@echo Linking tutorial_4
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
	@echo Cleaning tutorial_4
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

$(OBJDIR)/tutorial_4.o: ../../step4/tutorial_4.cpp
	@echo $(notdir $<)
	$(SILENT) $(CXX) $(CXXFLAGS) -o "$@" -c "$<"

-include $(OBJECTS:%.o=%.d)
