OMNETPP_ROOT ?= C:\tools64\omnetpp-4.6
INET_DIR ?= ${OMNETPP_ROOT}/../inet
MIXIM_DIR ?= ${OMNETPP_ROOT}/../mixim

OMNETPP_SRC_DIR ?= ${OMNETPP_ROOT}/src
OMNETPP_INCL_DIR ?= ${OMNETPP_INCL_DIR}/include

# TODO: Is this really the only way in 21st century?
JAVA_HOME:=$(shell cygpath "$(JAVA_HOME)" | sed 's/Program Files/Progra~1/')

CONFIGFILE = ${OMNETPP_ROOT}/Makefile.inc
include $(CONFIGFILE)

LDFLAGS += -Wl,--allow-multiple-definition,--kill-at

all: checkmakefiles header
	cd src && $(MAKE) LDFLAGS="$(LDFLAGS)"
	
copy: omnet\jdeeco-omnetpp.dll

omnet\jdeeco-omnetpp.dll: out\gcc-debug\src\libjdeeco-omnetpp.dll
	cp out/gcc-debug/src/libjdeeco-omnetpp.dll omnet/jdeeco-omnetpp.dll
	
header:
	$(JAVA_HOME)/bin/javah -o src/OMNeTJDEECoAPI.h -classpath src cz.cuni.mff.d3s.deeco.simulation.omnet.OMNeTNative

clean: checkmakefiles
	cd src && $(MAKE) clean
	rm -f src/OMNeTJDEECoAPI.h
	rm -f src/Makefile

cleanall: checkmakefiles
	cd src && $(MAKE) clean
	rm -f src/Makefile
	rm -rf out
	rm -rf libs
	
install:
	mkdir -p libs/debug/omnet
	mkdir -p libs/release/omnet
	mkdir -p libs/ned/omnet/inet
	mkdir -p libs/ned/omnet/mixim
	
	# Copy libraries
	
	# libjdeeco
	cp out/gcc-debug/src/libjdeeco-omnetpp.dll libs/debug/omnet/jdeeco-omnetpp.dll
	cp out/gcc-release/src/libjdeeco-omnetpp.dll libs/release/omnet/jdeeco-omnetpp.dll
	
	# omnetpp
	cp ${OMNETPP_ROOT}/out/gcc-debug/src/cmdenv/liboppcmdenvd.dll libs/debug/omnet
	cp ${OMNETPP_ROOT}/out/gcc-debug/src/common/liboppcommond.dll libs/debug/omnet
	cp ${OMNETPP_ROOT}/out/gcc-debug/src/envir/liboppenvird.dll libs/debug/omnet
	cp ${OMNETPP_ROOT}/out/gcc-debug/src/eventlog/liboppeventlogd.dll libs/debug/omnet
	cp ${OMNETPP_ROOT}/out/gcc-debug/src/layout/libopplayoutd.dll libs/debug/omnet
	cp ${OMNETPP_ROOT}/out/gcc-debug/src/nedxml/liboppnedxmld.dll libs/debug/omnet
	cp ${OMNETPP_ROOT}/out/gcc-debug/src/scave/liboppscaved.dll libs/debug/omnet
	cp ${OMNETPP_ROOT}/out/gcc-debug/src/sim/liboppsimd.dll libs/debug/omnet
	cp ${OMNETPP_ROOT}/out/gcc-debug/src/tkenv/libopptkenvd.dll libs/debug/omnet
	
	cp ${OMNETPP_ROOT}/out/gcc-release/src/cmdenv/liboppcmdenv.dll libs/release/omnet
	cp ${OMNETPP_ROOT}/out/gcc-release/src/common/liboppcommon.dll libs/release/omnet
	cp ${OMNETPP_ROOT}/out/gcc-release/src/envir/liboppenvir.dll libs/release/omnet
	cp ${OMNETPP_ROOT}/out/gcc-release/src/eventlog/liboppeventlog.dll libs/release/omnet
	cp ${OMNETPP_ROOT}/out/gcc-release/src/layout/libopplayout.dll libs/release/omnet
	cp ${OMNETPP_ROOT}/out/gcc-release/src/nedxml/liboppnedxml.dll libs/release/omnet
	cp ${OMNETPP_ROOT}/out/gcc-release/src/scave/liboppscave.dll libs/release/omnet
	cp ${OMNETPP_ROOT}/out/gcc-release/src/sim/liboppsim.dll libs/release/omnet
	cp ${OMNETPP_ROOT}/out/gcc-release/src/tkenv/libopptkenv.dll libs/release/omnet
	
	# inet
	cp ${INET_DIR}/out/gcc-debug/src/libinet.dll libs/debug/omnet
	cp ${INET_DIR}/out/gcc-release/src/libinet.dll libs/release/omnet
	
	# mixim
	cp ${MIXIM_DIR}/out/gcc-debug/src/libmixim.dll libs/debug/omnet
	cp ${MIXIM_DIR}/out/gcc-release/src/libmixim.dll libs/release/omnet
	
	# gcc
	cp ${OMNETPP_ROOT}/tools/win32/mingw64/bin/libgcc_s_seh-1.dll libs/debug/omnet
	cp ${OMNETPP_ROOT}/tools/win32/mingw64/bin/libgcc_s_seh-1.dll libs/release/omnet
	cp ${OMNETPP_ROOT}/tools/win32/mingw64/bin/libstdc++-6.dll libs/debug/omnet
	cp ${OMNETPP_ROOT}/tools/win32/mingw64/bin/libstdc++-6.dll libs/release/omnet
	cp ${OMNETPP_ROOT}/tools/win32/mingw64/bin/libwinpthread-1.dll libs/debug/omnet
	cp ${OMNETPP_ROOT}/tools/win32/mingw64/bin/libwinpthread-1.dll libs/release/omnet
	
	# ned files
	echo 'cd ${INET_DIR}/src; find -name "*.ned" -exec cp --parents {} $(shell pwd)/libs/ned/omnet/inet ";"' | sh
	echo 'cd ${MIXIM_DIR}; find -name "*.ned" -exec cp --parents {} $(shell pwd)/libs/ned/omnet/mixim ";"' | sh
	
	# custom model ned files
	cp -rv model libs/ned/omnet
	
	# custom config files
	cp -rv configs/* libs/ned/omnet
	

# Create makefiles that allows building mixim without a separate INET installation.
makefiles:
	cd src && ${OMNETPP_ROOT}/bin/opp_makemake -f --deep --make-so -o jdeeco-omnetpp -O out \
	-I${JAVA_HOME}/include \
	-I${JAVA_HOME}/include/win32 \
	-I${OMNETPP_SRC_DIR}/common -I${OMNETPP_INCL_DIR}/platdep -I${OMNETPP_SRC_DIR}/envir \
	-I${MIXIM_DIR}/src \
	-I${MIXIM_DIR}/src/base \
	-I${MIXIM_DIR}/src/base/modules \
	-I${MIXIM_DIR}/src/base/utils \
	-I${MIXIM_DIR}/src/base/connectionManager \
	-I${MIXIM_DIR}/src/base/phyLayer \
	-I${MIXIM_DIR}/src/base/messages \
	-I${INET_DIR}/src/linklayer/ieee80211/radio \
	-I${INET_DIR}/src/linklayer/ethernet \
	-I${INET_DIR}/src/networklayer/common \
	-I${INET_DIR}/src \
	-I${INET_DIR}/src/networklayer/icmpv6 \
	-I${INET_DIR}/src/world/obstacles \
	-I${INET_DIR}/src/networklayer/xmipv6 \
	-I${INET_DIR}/src/networklayer/manetrouting/base \
	-I${INET_DIR}/src/networklayer/contract \
	-I${INET_DIR}/src/mobility/models \
	-I${INET_DIR}/src/mobility \
	-I${INET_DIR}/src/networklayer/autorouting/ipv4 \
	-I${INET_DIR}/src/linklayer/ieee80211/mgmt \
	-I${INET_DIR}/src/util \
	-I${INET_DIR}/src/linklayer/common \
	-I${INET_DIR}/src/transport/contract \
	-I${INET_DIR}/src/status \
	-I${INET_DIR}/src/linklayer/radio/propagation \
	-I${INET_DIR}/src/linklayer/ieee80211/radio/errormodel \
	-I${INET_DIR}/src/linklayer/radio \
	-I${INET_DIR}/src/util/headerserializers/tcp \
	-I${INET_DIR}/src/networklayer/ipv4 \
	-I${INET_DIR}/src/util/headerserializers/ipv4 \
	-I${INET_DIR}/src/base \
	-I${INET_DIR}/src/util/headerserializers \
	-I${INET_DIR}/src/world/radio \
	-I${INET_DIR}/src/linklayer/ieee80211/mac \
	-I${INET_DIR}/src/networklayer/ipv6 \
	-I${INET_DIR}/src/transport/sctp \
	-I${INET_DIR}/src/util/headerserializers/udp \
	-I${INET_DIR}/src/networklayer/ipv6tunneling \
	-I${INET_DIR}/src/applications/pingapp \
	-I${INET_DIR}/src/battery/models \
	-I${INET_DIR}/src/util/headerserializers/sctp \
	-I${INET_DIR}/src/linklayer/contract \
	-I${INET_DIR}/src/networklayer/arp \
	-I${INET_DIR}/src/transport/tcp_common \
	-I${INET_DIR}/src/transport/udp \
	-I${INET_DIR}/src/mobility/static \
	-I${INET_DIR}/src/mobility/common \
	-I${INET_DIR}/src/mobility/contract \
	-L${MIXIM_DIR}/out/$(CONFIGNAME)/src \
	-L${INET_DIR}/out/$(CONFIGNAME)/src \
	-loppcommond -lmixim -linet -DMIXIM_INET=1 -DINET_IMPORT -KMIXIM_PROJ=${MIXIM_DIR} -KINET_PROJ=${INET_DIR} -KV=1

checkmakefiles:
	@if [ ! -f src/Makefile ]; then \
	echo; \
	echo '=========================================================='; \
	echo 'src/Makefile does not exist. Please use "make makefiles"'; \
	echo 'to generate it!'; \
	echo '=========================================================='; \
	echo; \
	exit 1; \
	fi

