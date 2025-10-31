import json
from pathlib import Path
from typing import List  # Ensure List is imported

nb_path = Path(r"c:\Users\tatib\mics_exp\takahashi\exp_notebook.ipynb")
nb = json.loads(nb_path.read_text(encoding='utf-8'))

code = None
for cell in nb.get('cells', []):
    if cell.get('cell_type') == 'code':
        src = ''.join(cell.get('source', []))
        if 'class Hungarian' in src:
            # join the source lines to form code
            code = src
            break

if code is None:
    raise RuntimeError('Hungarian class cell not found')

# Execute the code to define Hungarian in this env
local = {}
exec(code, globals(), local)
Hungarian = local.get('Hungarian')
if Hungarian is None:
    raise RuntimeError('Hungarian not defined after exec')

# Test matrix
test_tensor = [
    [7, 8, 5, 6],
    [6, 5, 9, 10],
    [4, 1, 10, 7],
    [3, 4, 6, 5]
]

print('Instantiating Hungarian...')
h = Hungarian(test_tensor)
print('tmp_tensor after construction:')
print(h.tmp_tensor)

# Call _zero_coords and _step3 explicitly
zc = h._zero_coords(h.input_tensor)
print('zero coords:', zc)
covered = h._step3()
print('covered lines count returned by _step3():', covered)
print('tmp_tensor after _step3():')
print(h.tmp_tensor)
