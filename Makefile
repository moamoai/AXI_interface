#
# Building Chisel examples without too much sbt/scala/... stuff
#
# sbt looks for default into a folder ./project and . for build.sdt and Build.scala
# sbt creates per default a ./target folder

SBT = sbt

# Generate Verilog code

axi4-lite-slave:
	$(SBT) "runMain simple.Axi4LiteSlaveMain"

slaveifm:
	$(SBT) "runMain simple.Axi4LiteSlaveIFMMain"

axi4-lite-slave-test:
	$(SBT) "test:runMain simple.Axi4LiteSlaveTester --backend-name verilator"

bus:
	$(SBT) "runMain simple.Axi4LiteBusMain"
top:
	$(SBT) "runMain simple.Axi4LiteTopMain"
bus-test:
	$(SBT) "test:runMain simple.Axi4LiteBusTester --backend-name verilator"

GTKWAVE = /Applications/gtkwave.app/Contents/Resources/bin/gtkwave
view:
	# $(GTKWAVE) ./test_run_dir/simple.Axi4LiteSlaveTester1484209783/Axi4LiteSlave.gtkw
	# $(GTKWAVE) ./test_run_dir/simple.Axi4LiteSlaveTester1484209783/Axi4LiteSlave.vcd
	# $(GTKWAVE) ./test_run_dir/simple.Axi4LiteBusTester1084326894/Axi4LiteTop.vcd
	$(GTKWAVE) ./test_run_dir/simple.Axi4LiteBusTester1084326894/Top.gtkw

# clean everything (including IntelliJ project settings)

clean:
	git clean -fd
