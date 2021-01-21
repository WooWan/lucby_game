import math

class Unit:
	x=y=0
	def __init__(self,u=None,x: int =None,y: int =None, mirror=None):
		if u!=None:
			if mirror!=None:
				self.x = int(64- u.x) - 1
				self.y = int(u.y)
			else:
				self.x=int(u.x)
				self.y=int(u.y)
		else:
			self.x=int(x)
			self.y=int(y)
	def getDistance(self, u):
		return int(math.sqrt(math.pow(self.x-u.x, 2)+math.pow(self.y-u.y, 2)))
	def getX(self):
		return int(self.x)
	def getY(self):
		return int(self.y)
	