from random import random
def getRandom(m, n):
	d = int(abs(m-n)+1)
	# print int(random()*d)+m
	return int(random()*d)+m


sum_a = 0
sum_b = 0
for i in range(10000):
	a= getRandom(-2,2)
	if a>0: sum_a+=1
	elif a<0: sum_b+=1
print(sum_a)
print(sum_b)
