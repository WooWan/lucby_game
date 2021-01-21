class Order:
	dx=[0 for _ in range(6)]
	dy=[0 for _ in range(6)]
	passto =-1

	def clear(self):
		self.dx = [0 for _ in range(6)]
		self.dy = [0 for _ in range(6)]
		self.passto = -1

	
	def __init__(self,o=None, mirror=None):
		if o != None:
			self.dx = o.dx
			self.dy = o.dy
			self.passto = o.passto
			for i in range(len(self.dx)):
				self.dx[i] *= -1
		else:
			pass


