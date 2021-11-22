import json
import os
import numpy as np
import pandas as pd

from pathlib import Path

from loguru import logger

from lib import check_duplicates, compare_df

field_names = ["vehicleId", "timestamp", "latitude", "longitude", "speed"]
data_types = {
  "vehicleId": np.string_,
  "timestamp": np.int64,
  "latitude": np.float64,
  "longitude": np.float64,
  "speed": np.float64
}

class ExperimentAnalyzer:

    def __init__(self, scenario, name, resource, experiments_path, baseline) -> None:
        self.scenario = scenario
        self.name = name
        self.resource = resource
        self.experiments_path = experiments_path
        self.baseline = baseline

    def run(self):
        metrics = self.collect_metrics()
        results = Path(f"results/{scenario}")

        if not results.exists():
            results.mkdir(parents=True)

        with open(f"results/{scenario}/{experiment_name}.json", 'w+') as f:     
            json.dump(metrics, f, indent=4, sort_keys=True)
            f.truncate()

    def collect_metrics(self) -> dict:
        data = {}

        for filename in os.listdir(f"{self.experiments_path}/{self.scenario}"):
            if filename.startswith(self.name):
                logger.info(f"Processing {filename} ...")

                expermient_results = f"{self.experiments_path}/{self.scenario}/{filename}/SpeedReport-{self.resource}.csv"
                df_exp = pd.read_csv(expermient_results, names=field_names, dtype=data_types)

                baseline_results = f"{self.experiments_path}/{self.baseline}/{filename}/SpeedReport-{self.resource}.csv"
                df_base = pd.read_csv(baseline_results, names=field_names, dtype=data_types)
                interval = filename.split(self.name)[1].split("_")[1]

                baseline_len = df_base.shape[0]
                exp_len = df_exp.shape[0]

                exp_duplicates = check_duplicates(df_exp)
                overlaping_tuples = compare_df(df_base, df_exp)
                missing_tuples = baseline_len - overlaping_tuples
                extra_tuples = exp_len - overlaping_tuples

                experiment_data = {
                    "tuples" : exp_len,
                    "duplicates": exp_duplicates,
                    "correct": overlaping_tuples,
                    "missing": missing_tuples,
                    "extra": extra_tuples,
                    "expected": baseline_len
                }

                data[int(interval)] = experiment_data
                logger.info(f"Processing {filename} ... completed!")

        return data



if __name__ == "__main__":
    scenario = "leipzig-moving-range"
    experiment_name = "moving_range"
    resource_name = "veh_0"
    experimments_path = "/home/parallels/Development/mosaic-nes/tools/simulation-runner/experiments"
    baseline_results = f"leipzig"

    analyzer = ExperimentAnalyzer(scenario, experiment_name, resource_name, experimments_path, baseline_results)
    analyzer.run()
