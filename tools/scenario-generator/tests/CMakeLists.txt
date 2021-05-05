# Google Testing Framework ----------------------------------------------------
include(ExternalProject)

include(FetchContent)
FetchContent_Declare(
        googletest
        # Specify the commit you depend on and update it regularly.
        URL https://github.com/google/googletest/archive/609281088cfefc76f9d0ce82e1ff6c30cc3591e5.zip
)
# For Windows: Prevent overriding the parent project's compiler/linker settings
set(gtest_force_shared_crt ON CACHE BOOL "" FORCE)
FetchContent_MakeAvailable(googletest)

#------------------------------- # Unit Test Suite --------------------------------

add_executable(generator-config-test GeneratorConfigTest.cpp)
target_link_libraries(generator-config-test gtest_main gmock_main mosaic)
add_test(NAME generator-config-test COMMAND generator-config-test)

add_executable(scenario-convert-test ScenarioConvertTest.cpp)
target_link_libraries(scenario-convert-test  gtest_main gmock_main mosaic)
add_test(NAME scenario-convert-test COMMAND scenario-convert-test)

add_executable(scenario-test ScenarioTest.cpp)
target_compile_definitions(scenario-test PUBLIC TEST_FIXTURES_DIR="${CMAKE_CURRENT_SOURCE_DIR}/fixtures/")
target_link_libraries(scenario-test  gtest_main gmock_main mosaic)
add_test(NAME scenario-test COMMAND scenario-test)

# Custom test commands --------------------------------------------------------
add_custom_target(test_debug COMMAND ${CMAKE_CTEST_COMMAND} --output-on-failure DEPENDS ${UNITTESTS})
