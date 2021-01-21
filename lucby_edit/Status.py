class Status:
	ballOwner=0
	turn=0
	mine=[]
	enemy=[]
	ball=None
	
	def __init__(self):
		self.mine=[0]*6
		self.enemy=[0]*6
	
	def setStatus(self, info, ballOwner, turn):
		self.ball=info[0]
		for i in range(0, 6):
			self.mine[i] = info[i+1]
		for i in range(0, 6):
			self.enemy[i] = info[i+7]
		self.ballOwner=ballOwner
		self.turn=turn
