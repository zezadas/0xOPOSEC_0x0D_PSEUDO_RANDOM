import javarandom
import sys
​
if __name__ == '__main__':
    # print(sys.argv)
    if len(sys.argv) < 2:
        print(f"Usage: {sys.argv[0]} <file>")
        sys.exit(1)
​
    leaked_seed = 5040508
    rng = javarandom.Random(leaked_seed)
​
    with open(sys.argv[1], "rb") as f:
        target_bytes = f.read(16)
​
    # print(target_bytes)
​
    last_sixteen = [0]*16
    # Needs to be converted to bytes to be compared - converts int list to bytes easily :)
    while bytes(last_sixteen) != target_bytes:
        # print(last_sixteen)
        # Generate another integer and cycle list
        new_val = rng.nextInt(256)
        last_sixteen.append(new_val)
        last_sixteen.pop(0)
        
    print(f"Iv found! - {last_sixteen}")
    print('Generating key...')
    key = [rng.nextInt(256) for _ in range(16)]
    print(f"Key: {key}")
    hexed_key = ''.join(['{:02x}'.format(key_num) for key_num in key])    
    print(f"Hexed key: {hexed_key}")
