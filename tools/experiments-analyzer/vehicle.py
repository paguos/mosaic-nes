import pandas as pd

from loguru import logger

scenario = "leipzig"
experiment_name = "variable_source_interval"
interval = 500

logger.info("Starting vehicle analyzis ...")

baseline_results = f"/home/parallels/Documents/experiments/{scenario}/{experiment_name}/baseline.csv"
expermient_results = f"/home/parallels/Documents/experiments/{scenario}/{experiment_name}/interval_{interval}.csv"

field_names = ["vehicleId", "timestamp", "latitude", "longitude", "speed"]

logger.info("Reading datasets ...")
df_base = pd.read_csv(baseline_results, names=field_names)
df_exp = pd.read_csv(expermient_results, names=field_names)
logger.info("Reading datasets ... done!")

for i in range(1,8):
    vehicle = f"veh_{i}"
    logger.deug(f"Generating dataset of vehicle '{vehicle}' ...")

    veh_base = df_base[df_base["vehicleId"]==vehicle].reset_index(drop=True)
    veh_exp = df_exp[df_exp["vehicleId"]==vehicle].reset_index(drop=True)

    veh_base.to_csv(f"baseline_{vehicle}.csv", sep=',', index=False)
    veh_exp.to_csv(f"interval_{interval}_{vehicle}.csv", sep=',', index=False)
    logger.debug(f"Generating dataset of vehicle '{vehicle}' ... done!")

logger.info("Vehicle analyzis completed!")
