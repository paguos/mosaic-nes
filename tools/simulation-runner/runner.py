from __future__ import print_function, unicode_literals

import os

from loguru import logger
from pathlib import Path
from PyInquirer import prompt

from simulation import Configuration, Simulation

class SimulationRunner:

    def __init__(self, name: str, scenario: str, configs: list, mosaic_path: Path):
        self.cwd = Path(f"{os.getcwd()}/experiments")
        self.name = name
        self.scenario = scenario
        self.configs = configs
        self.mosaic_path = mosaic_path

    def start(self, brake=1):
        logger.info("Starting simulation runner ...")
        if not self.cwd.exists():
            self.cwd.mkdir(parents=True)
        os.chdir(self.mosaic_path)

        for config in self.configs:
            logger.info(f"Starting {self.scenario} with config {config.attribute} -> {config.value} ...")
            config.apply()
            simulation = Simulation(self.scenario, f"{self.name}_{config.attribute}_{config.value}", brake)
            simulation.run()
            simulation.copy_results(self.cwd)
            logger.info(f"{self.scenario} with config {config.attribute} -> {config.value} done!")
        
        logger.info("Simulation runner done!")

if __name__ == "__main__":

    questions = [
        {
            'type': 'input',
            'name': 'experiment',
            'message': 'Experiment name:'
        },
        {
            'type': 'list',
            'name': 'scenario',
            'message': 'Scenario:',
            'choices': ['barcelona', 'barcelona-moving-range', 'barcelona-range-query', 'berlin', 'berlin-moving-range', 'berlin-range-query'],
        },
        {
            'type': 'input',
            'name': 'brake',
            'message': 'Brake:',
            'default': '1'
        },
        {
            'type': 'list',
            'name': 'config_file',
            'message': 'File for config:',
            'choices': ['application/NesApp.json', 'mapping/mapping.json', 'nes/nes_config.json'],
        },
        {
            'type': 'input',
            'name': 'config_attribute',
            'message': 'Attribute for config:',
            'default': 'updateLocationInterval'
        },
        {
            'type': 'input',
            'name': 'config_start',
            'message': 'Start value for config:',
            'default': '0'
        },
        {
            'type': 'input',
            'name': 'config_interval',
            'message': 'Interval value for config:',
            'default': '500'
        },
        {
            'type': 'input',
            'name': 'config_repetitions',
            'message': 'Number of repetitions for config:',
            'default': '5'
        },
        {
            'type': 'input',
            'name': 'mosaic_path',
            'message': 'Mosaic path:',
            'default': f"{Path(os.getcwd()).parent.parent}/mosaic"
        }
    ]

    answers = prompt(questions)
    scenario = answers.get("scenario")

    configs = []
    config_attribute = answers.get("config_attribute")

    for i in range(0, int(answers.get("config_repetitions"))):
        value = int(answers.get("config_start")) + (i * int(answers.get("config_interval")))
        config_file = Path(f"scenarios/{scenario}/{answers.get('config_file')}")
        config = Configuration(config_file, config_attribute, value)
        configs.append(config)

    experiment_name = answers.get('experiment')
    mosaic_path = Path(answers.get('mosaic_path'))
    brake_value = int(answers.get('brake'))

    runner = SimulationRunner(experiment_name, scenario, configs, mosaic_path)
    runner.start(brake=brake_value)
    