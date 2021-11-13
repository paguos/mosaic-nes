import json

from abc import ABC
from abc import abstractmethod
from pathlib import Path

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

        if name == "moving_range":
            return MovingRange(scenario, name, value)

        if name == "rsu_range":
            return RSURange(scenario, name, value)

        if name == "range_query_interval":
            return RangeQueryInterval(scenario, name, value)

        if name == "filter_storage":
            return FilterStorageSize(scenario, name, value)

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


class MovingRange(Experiment):
    DEFAULT_VALUE = 1000000

    def __init__(self, scenario, name, value) -> None:
       super().__init__(scenario, name, value)

    def apply(self, value):
        path = Path(f"scenarios/{self.scenario}/application/NesApp.json")

        if not path.exists():
            path = Path(f"scenarios/{self.scenario}/application/SinkApp.json")

        with open(path, 'r+') as f:
            data = json.load(f)
            data["rangeArea"] = value
            f.seek(0)       
            json.dump(data, f, indent=4)
            f.truncate()

    def restore(self):
        self.apply(self.DEFAULT_VALUE)


class RSURange(Experiment):
    DEFAULT_VALUE = 100

    def __init__(self, scenario, name, value) -> None:
       super().__init__(scenario, name, value)

    def apply(self, value):
        path = Path(f"scenarios/{self.scenario}/application/NesApp.json")
        key = "sourceRangeRadius"

        if not path.exists():
            path = Path(f"scenarios/{self.scenario}/application/SourceApp.json")
            key = "broadcastRadius"

        with open(path, 'r+') as f:
            data = json.load(f)
            data[key] = value
            f.seek(0)       
            json.dump(data, f, indent=4)
            f.truncate()

    def restore(self):
        self.apply(self.DEFAULT_VALUE)

class RangeQueryInterval(Experiment):

    DEFAULT_VALUE = 6

    def __init__(self, scenario, name, value) -> None:
       super().__init__(scenario, name, value)

    def apply(self, value):
        with open(f"scenarios/{self.scenario}/application/NesApp.json", 'r+') as f:
            data = json.load(f)
            data["queryInterval"] = value
            f.seek(0)       
            json.dump(data, f, indent=4)
            f.truncate()

    def restore(self):
        self.apply(self.DEFAULT_VALUE)


class FilterStorageSize(Experiment):
    DEFAULT_VALUE = 50

    def __init__(self, scenario, name, value) -> None:
       super().__init__(scenario, name, value)

    def apply(self, value):
        with open(f"scenarios/{self.scenario}/nes/nes_config.json", 'r+') as f:
            data = json.load(f)
            data["coordinator"]["numberOfTuplesInFilterStorage"] = value
            f.seek(0)       
            json.dump(data, f, indent=4)
            f.truncate()

    def restore(self):
        self.apply(self.DEFAULT_VALUE)

