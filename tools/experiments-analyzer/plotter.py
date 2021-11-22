import json

import matplotlib.pyplot as plt

from pathlib import Path

class ExperimentPlotter:

    def __init__(self, scenario, experiments, name, x_axis_label) -> None:
        self.scenario = scenario
        self.experiments = experiments
        self.name = name
        self.x_axis_label = x_axis_label
        self.output_path = Path(f"plots/{scenario}")

    def plot(self):

        if not self.output_path.exists():
            self.output_path.mkdir(parents=True)

        results = {}
        for experiment in self.experiments.keys():
            experiment_file =  f"results/{self.scenario}/{experiment}.json"
            with open(experiment_file, "r+") as f:
                data = json.load(f)
            intervals = [float(k) / 1000 for k in data.keys()]

            precision = []
            recall = []
            f1 = []

            for v in data.values():
                p = v["correct"] / (v["correct"] + v["extra"])
                precision.append(p)

                r = v["correct"] / (v["correct"] + v["missing"])
                recall.append(r)

                f =  2 * ((p * r) / (p + r))
                f1.append(f)

            results[experiment] = {
                "precision" : precision,
                "recall" : recall,
                "f1": f1
            }

        plt.figure("precision")
        for experiment, label in self.experiments.items():
            plt.plot(intervals, results[experiment]["precision"], label=label)
        plt.grid(axis="x", color="0.95")
        plt.xlabel(self.x_axis_label)
        plt.ylim(bottom=0.5, top=1.01)
        plt.ylabel("precision")
        if (len(self.experiments) > 1):
            plt.legend()
        plt.savefig(f"{self.output_path}/{self.name}-precision.png", bbox_inches='tight')

        plt.figure("recall")
        for experiment, label in self.experiments.items():
            plt.plot(intervals, results[experiment]["recall"], label=label)
        plt.grid(axis="x", color="0.95")
        plt.ylim(bottom=0.5, top=1.01)
        plt.xlabel(self.x_axis_label)
        plt.ylabel("recall")
        if (len(self.experiments) > 1):
            plt.legend()
        plt.savefig(f"{self.output_path}/{self.name}-recall.png", bbox_inches='tight')

        plt.figure("f1")
        for experiment, label in self.experiments.items():
            plt.plot(intervals, results[experiment]["f1"], label=label)
        plt.grid(axis="x", color="0.95")
        plt.ylim(bottom=0.5, top=1.01)
        plt.xlabel(self.x_axis_label)
        plt.ylabel("f1-score")
        if (len(self.experiments) > 1):
            plt.legend()
        plt.savefig(f"{self.output_path}/{self.name}-f1.png", bbox_inches='tight')
        

if __name__ == "__main__":
    configs = {
        "variable_update_intervals": "t_coordinator & t_source",
        "variable_coordinator_update_interval": "t_coordinator",
        "variable_source_update_interval": "t_source"
    }

    # configs = {
    #     "range_query_time_intervals": "range query",
    #     "variable_update_intervals": "moving range query"
    # }
    plotter = ExperimentPlotter("leipzig-moving-range", configs, "time_intervals", r'update interval time ($seconds$)')
    plotter.plot()
    