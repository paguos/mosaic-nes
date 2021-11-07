import json

from abc import ABC
from abc import abstractmethod

from loguru import logger

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
        if name == "variable_update_intervals":
            return VariableUpdateInterval(scenario, name, value)

        if name == "variable_coordinator_update_interval":
            return VariableCoordinatorInterval(scenario, name, value)

        if name =="variable_source_update_interval":
            return VarialeSourceInterval(scenario, name, value)

        if name == "max_speed_sink":
            return SinkMaxSpeed(scenario,  name, value)

        if name == "max_speed_source":
            return SourceMaxSpeed(scenario,  name, value)

        logger.error("Not supported configuration!")
        raise Exception("Not supported configuration!")

class VariableUpdateInterval(Experiment):

    def __init__(self, scenario, name, value) -> None:
       super().__init__(scenario, name, value)
       self.coordinatorInterval = VariableCoordinatorInterval(scenario, name, value)
       self.sourceInterval = VarialeSourceInterval(scenario, name, value)

    def apply(self, value):
        self.coordinatorInterval.apply(value)
        self.sourceInterval.apply(value)

    def restore(self):
        self.coordinatorInterval.restore()
        self.sourceInterval.restore()

class VarialeSourceInterval(Experiment):

    DEFAULT_VALUE = 500

    def __init__(self, scenario, name, value) -> None:
       super().__init__(scenario, name, value)

    def apply(self, value):
        with open(f"scenarios/{self.scenario}/application/NesApp.json", 'r+') as f:
            data = json.load(f)
            data["updateLocationInterval"] = value
            f.seek(0)       
            json.dump(data, f, indent=4)
            f.truncate()

    def restore(self):
        self.apply(self.DEFAULT_VALUE)

class VariableCoordinatorInterval(Experiment):
    DEFAULT_VALUE = 500

    def __init__(self, scenario, name, value) -> None:
       super().__init__(scenario, name, value)

    def apply(self, value):
        with open(f"scenarios/{self.scenario}/nes/nes_config.json", 'r+') as f:
            data = json.load(f)
            data["coordinator"]["updateLocationInterval"] = value
            f.seek(0)       
            json.dump(data, f, indent=4)
            f.truncate()

    def restore(self):
        self.apply(self.DEFAULT_VALUE)

class SinkMaxSpeed(Experiment):
    DEFAULT_VALUE = 10.0

    def __init__(self, scenario, name, value) -> None:
       super().__init__(scenario, name, value)

    def apply(self, value):
        with open(f"scenarios/{self.scenario}/mapping/mapping_config.json", 'r+') as f:
            data = json.load(f)
            for i in range(0, len(data["prototypes"])):
                if "Sink" in data["prototypes"][i]["name"]:
                    logger.info("here!")
                    data["prototypes"][i]["maxSpeed"] = float(value)
        
            f.seek(0)       
            json.dump(data, f, indent=4)
            f.truncate()

    def restore(self):
        self.apply(self.DEFAULT_VALUE)

class SourceMaxSpeed(Experiment):
    DEFAULT_VALUE = 10.0

    def __init__(self, scenario, name, value) -> None:
       super().__init__(scenario, name, value)

    def apply(self, value):
        with open(f"scenarios/{self.scenario}/mapping/mapping_config.json", 'r+') as f:
            data = json.load(f)
            for i in range(0, len(data["prototypes"])):
                if data["prototypes"][i]["name"] == "Car":
                    logger.info("here!")
                    data["prototypes"][i]["maxSpeed"] = float(value)
        
            f.seek(0)       
            json.dump(data, f, indent=4)
            f.truncate()

    def restore(self):
        self.apply(self.DEFAULT_VALUE)

