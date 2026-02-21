import json
from os import listdir
from os.path import isfile, join

inputFolder = 'src/main/resources/assets/furnish/models/item'
outputFolder = 'src/main/resources/assets/furnish/items/'

def convertModel(model):
    with open(join(inputFolder, model), 'r') as file:
        data = json.load(file)

    newData = {
        'model': {
            'type': "minecraft:model",
            'model': data['parent']
        }
    }

    # print(json.dumps(newData, indent=2))

    with open(join(outputFolder, model), 'w') as file:
        file.write(json.dumps(newData, indent=2))

if __name__ == '__main__':
    inputModels = [f for f in listdir(inputFolder) if isfile(join(inputFolder, f))]
    for model in inputModels:
        convertModel(model)
