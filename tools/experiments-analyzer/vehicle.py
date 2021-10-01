import pandas as pd

scenario = "barcelona"
experiment_name = "variable_source_interval"
interval = 100

baseline_results = f"/home/parallels/Documents/experiments/{scenario}/{experiment_name}/baseline.csv"
expermient_results = f"/home/parallels/Documents/experiments/{scenario}/{experiment_name}/interval_{interval}.csv"

field_names = ["vehicleId", "timestamp", "latitude", "longitude", "speed"]
vehicle = "veh_5"

df_base = pd.read_csv(baseline_results, names=field_names)
df_exp = pd.read_csv(expermient_results, names=field_names)

baseline_len = df_base.shape[0]
exp_len = df_exp.shape[0]

veh_base = df_base[df_base["vehicleId"]==vehicle].reset_index(drop=True)
veh_exp = df_exp[df_exp["vehicleId"]==vehicle].reset_index(drop=True)

print(veh_base.shape)
print(veh_exp.shape)

veh_base.to_csv(f"baseline_{vehicle}.csv", sep=',', index=False)
veh_exp.to_csv(f"interval_{interval}_{vehicle}.csv", sep=',', index=False)
