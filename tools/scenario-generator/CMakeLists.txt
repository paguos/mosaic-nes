cmake_minimum_required(VERSION 3.17)
project(scenario_generator)

set(CMAKE_CXX_STANDARD 17)

# cxxopts
set(cxxopts_RELEASE "v2.2.1")
include(FetchContent)
FetchContent_Declare(
        cxxopts
        GIT_REPOSITORY https://github.com/jarro2783/cxxopts
        GIT_TAG        ${cxxopts_RELEASE}
)
set(FETCHCONTENT_QUIET OFF)
FetchContent_MakeAvailable(cxxopts)

# json
set(json_RELEASE "v3.9.1")
FetchContent_Declare(
        json
        GIT_REPOSITORY https://github.com/ArthurSonzogni/nlohmann_json_cmake_fetchcontent
        GIT_TAG        ${json_RELEASE}
)
FetchContent_GetProperties(json)
if(NOT json_POPULATED)
    FetchContent_Populate(json)
    add_subdirectory(${json_SOURCE_DIR} ${json_BINARY_DIR} EXCLUDE_FROM_ALL)
endif()

# loguru
set(loguru_RELEASE "v2.1.0")
FetchContent_Declare(
        loguru
        GIT_REPOSITORY https://github.com/emilk/loguru
        GIT_TAG        ${loguru_RELEASE}
)
FetchContent_MakeAvailable(loguru)
include_directories("${loguru_SOURCE_DIR}")
add_library(loguru "${loguru_SOURCE_DIR}/loguru.cpp" "${loguru_SOURCE_DIR}/loguru.hpp")

# sqlite
set(sqlite_RELEASE "3.1.1")
FetchContent_Declare(
        sqlite
        GIT_REPOSITORY https://github.com/SRombauts/SQLiteCpp
        GIT_TAG        ${sqlite_RELEASE}
)
FetchContent_MakeAvailable(sqlite)

# cmake macros
include(macros.cmake)

# Add Source Code
add_subdirectory(src)

get_source_mosaic(mosaic_SOURCE_FILES)
add_library(mosaic ${mosaic_SOURCE_FILES})

target_include_directories(mosaic PUBLIC "${CMAKE_CURRENT_SOURCEDIR}")
target_include_directories(mosaic PUBLIC "${CMAKE_CURRENT_BINARY_DIR}")
target_link_libraries(mosaic cxxopts nlohmann_json::nlohmann_json loguru SQLiteCpp)

add_executable(scenarioGenerator src/Executables/ScenarioGeneratorStarter.cpp)
target_link_libraries(scenarioGenerator mosaic)

# Add tests with command
enable_testing()
add_subdirectory(tests)