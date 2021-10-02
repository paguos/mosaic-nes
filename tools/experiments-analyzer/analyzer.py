import pandas as pd
import numpy as np

def compare(x, y, i=6):
  return round(x, i) == round(y, i)
  
def compare_df(df1, df2):
  count = 0
  for index, row in df1.iterrows():
    found = False
    for index_base, row_base in df2.iterrows():
      if row["vehicleId"] == row_base["vehicleId"] and row["timestamp"] == row_base["timestamp"] and compare(row["latitude"], row_base["latitude"]) and compare(row["longitude"], row_base["longitude"]) and compare(row["speed"], row_base["speed"]):
        count = count + 1
        found = True
        break
    if not found and row['vehicleId'] == "veh_2":
      print(f"Not found: {row['vehicleId']} -> {row['timestamp']}")
    
  return count

def check_duplicates(df):
  vehicles_data = {}
  duplicates = 0

  for index, row in df.iterrows():
    vehicleId = row["vehicleId"]
    timestamp = row["timestamp"]
    
    if vehicleId in vehicles_data:
      if timestamp in vehicles_data[vehicleId]:
        duplicates += 1
      else:
        vehicles_data[vehicleId].append(timestamp)
    else:
      vehicles_data[vehicleId] = [timestamp]

  return duplicates

scenario = "barcelona"
experiment_name = "variable_source_interval"
interval = 0

baseline_results = f"/home/parallels/Documents/experiments/{scenario}/{experiment_name}/baseline.csv"
expermient_results = f"/home/parallels/Documents/experiments/{scenario}/{experiment_name}/interval_{interval}.csv"

field_names = ["vehicleId", "timestamp", "latitude", "longitude", "speed"]
data_types = {
  "vehicleId": np.string_,
  "timestamp": np.int64,
  "latitude": np.float64,
  "longitude": np.float64,
  "speed": np.float64

}

df_base = pd.read_csv(baseline_results, names=field_names, dtype=data_types)
df_exp = pd.read_csv(expermient_results, names=field_names, dtype=data_types)

baseline_len = df_base.shape[0]
exp_len = df_exp.shape[0]

print(f"Baseline tuples: {baseline_len}")
print(f"Experiment tuples: {exp_len}")

exp_duplicates = check_duplicates(df_exp)
print(f"Experiment duplicates: {exp_duplicates}")

overlaping_tuples = compare_df(df_base, df_exp)
print(f"Overlap: {overlaping_tuples}")

missing_tuples = baseline_len - overlaping_tuples
print(f"Missing: {missing_tuples}")

extra_tuples = exp_len - overlaping_tuples
print(f"Extra: {extra_tuples}")