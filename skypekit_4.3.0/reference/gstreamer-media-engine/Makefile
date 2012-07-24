SID_DIR=../..
include $(SID_DIR)/buildfiles/makefile.header.mk

CONFIG = debug-$(TARGET_ARCH)


all: gstreamer-media-engine install

gstreamer-media-engine: premake
	$(MAKE) -C build/gmake $@ config=$(CONFIG)

premake:
	$(PREMAKE) --target-os=$(TARGET_OS) --target-arch=$(TARGET_ARCH) gmake

install:
	$(PREMAKE) --target-os=$(TARGET_OS) --target-arch=$(TARGET_ARCH) install

clean:
	$(RM) -rf build
	$(RM) -rf tests/build
	if [ -e docs/Makefile ]; then \
		$(MAKE) -C docs/ clean; \
	fi

check: gstreamer-media-engine
	$(MAKE) -C tests $@ config=$(CONFIG)

docs/Makefile:
	cd docs/; ./autogen.sh

docs: gstreamer-media-engine docs/Makefile
	$(MAKE) -C docs/
