build-cmake:
	mkdir -p build
	cd build && cmake ..
	cd build && make -j4

tests: build-cmake
	cd build/tests && make test_debug