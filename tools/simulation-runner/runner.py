from __future__ import print_function, unicode_literals

import os

from loguru import logger
from pathlib import Path
from PyInquirer import prompt
from experiment import ExperimentFactory

from questions import questions
from simulation import Configuration, Simulation

class SimulationRunner:

    def __init__(self, name: str, scenario: str, experiments: list, mosaic_path: Path):
        self.cwd = Path(f"{os.getcwd()}/experiments")
        self.name = name
        self.scenario = scenario
        self.experiments = experiments
        self.mosaic_path = mosaic_path

    def start(self, brake=1):
        logger.info("Starting simulation runner ...")
        if not self.cwd.exists():
            self.cwd.mkdir(parents=True)
        os.chdir(self.mosaic_path)

        for experiment in self.experiments:
            logger.info(f"Starting {self.scenario} for experiment {experiment.name} with value {experiment.value} ...")
            experiment.apply(experiment.value)
            simulation = Simulation(self.scenario, f"{self.name}_{experiment.value}", brake)
            simulation.run()
            simulation.copy_results(self.cwd)
            experiment.restore()
            logger.info(f"Starting {self.scenario} for experiment {experiment.name} with value {experiment.value} ... done!")
        
        logger.info("Simulation runner done!")

if __name__ == "__main__":
    answers = prompt(questions)
    scenario = answers.get("scenario")
    experiments = []
    for i in range(0, int(answers.get("config_repetitions"))):
        value = int(answers.get("config_start")) + (i * int(answers.get("config_interval")))
        experiment = ExperimentFactory.create(scenario, answers.get("experiment"), value)
        experiments.append(experiment)

    experiment = answers.get('experiment')
    mosaic_path = Path(answers.get('mosaic_path'))
    brake_value = int(answers.get('brake'))

    runner = SimulationRunner(experiment, scenario, experiments, mosaic_path)
    runner.start(brake=brake_value)
    