import json

from abc import ABC
from abc import abstractmethod

from simulation import Simulation

class Experiment(ABC):

    def __init__(self, scenario, name, value) -> None:
        self.scenario = scenario
        self.name = name
        self.value = value

    @abstractmethod
    def apply(self, value):
        pass
    
    @abstractmethod
    def restore(self):
        pass

class ExperimentFactory:

    @staticmethod
    def create(scenario: str, name: str, value) -> Experiment:
        if name =="variable_source_update_interval":
            return VarialeSourceInterval(scenario, name, value)
        

class VarialeSourceInterval(Experiment):

    DEFAULT_VALUE = 500

    def __init__(self, scenario, name, value) -> None:
       super().__init__(scenario, name, value)

    def apply(self):
        with open(f"scenarios/{self.scenario}/application/NesApp.json", 'r+') as f:
            data = json.load(f)
            data["updateLocationInterval"] = self.value
            f.seek(0)       
            json.dump(data, f, indent=4)
            f.truncate()

    def restore(self):
        self.apply(self.DEFAULT_VALUE)

