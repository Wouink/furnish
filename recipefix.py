import json
import sys

recipeinput = open(sys.argv[1], 'r')
recipe = json.load(recipeinput)
recipeinput.close()
#print(recipe)
resultid = recipe['result']
resultcount = recipe['count']
recipe['result'] = {'id': resultid, 'count': resultcount}
del recipe['count']
#print("Modified version")
#print(recipe)
recipeoutput = open(sys.argv[1], 'w')
json.dump(recipe, recipeoutput, indent=2)
recipeoutput.close()
