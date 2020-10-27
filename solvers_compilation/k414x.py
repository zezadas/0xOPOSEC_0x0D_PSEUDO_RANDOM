from javarandom import Random
targets = [ 'bb bb bb de d5 a1 5b 60  b0 d7 fd fe 92 2e 53 a5'.replace(' ','').decode('hex'),
            '2d cd cd cd a7 51 4e 47  fd 46 e8 e0 83 34 ea de'.replace(' ','').decode('hex'),
            'da da da 4d 43 e1 95 95  39 14 ca 23 d8 4a df 52'.replace(' ','').decode('hex')] 
for i,target in enumerate(targets):
    iv=[0x00]*16
    r=Random(5040508L)
    while ''.join(map(chr,iv)) != target:
        n=r.nextInt(256)
        iv=iv[1:]
        iv.append(n)
    print 'found the iv ', i
    k= ''.join(chr(r.nextInt(256)) for _ in range(16))
    print 'key%s'%i,k.encode('hex')
#flag{The_truth_is_out_there}
