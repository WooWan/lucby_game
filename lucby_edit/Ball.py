from Unit import Unit

class Ball(Unit):
	
	def __init__(self,x,y):
		super().__init__(x=x,y=y)
	
	def fly2(self, x,y):
		self.x=x
		self.y=y
	
	def dribbled(self, p):
		self.x=p.x
		self.y=p.y
	
	def getX(self):
		return int(self.x)
	def getY(self):
		return int(self.y)
	
	def __str__(self):
		return "$"