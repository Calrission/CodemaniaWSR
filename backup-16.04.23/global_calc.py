import os
file_path = os.path.abspath(__file__)
file_dir = file_path[:file_path.index("global.py")]
objects = os.listdir(file_dir)
with open("global.sql", mode="w", encoding="utf-8") as out:
	for obj in objects:
		if os.path.isfile(os.path.join(file_dir, obj)) and obj.endswith('.sql'):
			with open(obj, mode="r", encoding="utf-8") as inp: 
				data = inp.readlines()
				out.writelines(data)
