import os
import json
import shutil

from pathlib import Path

class Configuration:

    def __init__(self, config_file: str, attribute: str, value):
        self.config_file = config_file
        self.attribute = attribute
        self.value = value

    def apply(self):
        with open(self.config_file, 'r+') as f:
            data = json.load(f)
            data[self.attribute] = self.value
            f.seek(0)       
            json.dump(data, f, indent=4)
            f.truncate()

class Simulation:
    
    def __init__(self, scenario: str, id: str, brake: int = 1):
        self.scenario = scenario
        self.id = id
        self.brake = brake

    def run (self):
        if self.brake > 0:
            os.system(f"./mosaic.sh -s {self.scenario} -b {self.brake}")
        else:
            os.system(f"./mosaic.sh -s {self.scenario}")

    def copy_results(self, path: Path):
        logs_path = Path("logs")
        for l in logs_path.iterdir():
            if Path(f"{path}/{self.scenario}/{self.id}").exists():
                shutil.rmtree(f"{path}/{self.scenario}/{self.id}")
            shutil.copytree(l, f"{path}/{self.scenario}/{self.id}")
            shutil.rmtree(l)
            

