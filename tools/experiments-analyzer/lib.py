def compare(x, y, i=6):
  return round(x, i) == round(y, i)
  
def compare_df(df1, df2):
  count = 0
  for index, row in df1.iterrows():
    for index_base, row_base in df2.iterrows():
      if row["vehicleId"] == row_base["vehicleId"] and row["timestamp"] == row_base["timestamp"] and compare(row["latitude"], row_base["latitude"]) and compare(row["longitude"], row_base["longitude"]) and compare(row["speed"], row_base["speed"]):
        count = count + 1
        break
    
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