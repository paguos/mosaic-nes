
#include <gtest/gtest.h>

#include "src/Generator/GeneratorConfig.h"

const GeneratorConfig config(false, "test-output", "test-osm", "test-config", "test-scenario");

TEST(GenConfig, Info) {
    const char *expected = "Generator Config {\n\tOutput path: test-output\n\tOSM file: test-osm\n\tConfig file: test-config\n\tScenario convert path: test-scenario\n}";
    EXPECT_STREQ(expected, config.info().c_str());
}
