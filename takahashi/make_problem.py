import numpy as np
import random

for i in range(10):
    num_bidders = 10
    num_goods = random.randint(10, 50)
    print(f"入札者:{num_bidders}")
    print(f"財の種類:{num_goods}")
    print("評価額:")
    for j in range(num_bidders):
        values = [random.randint(1, 100) for _ in range(num_goods)]
        print(" ".join(map(str, values)))
    print("-" * 20)
