from Strategy import Strategy
import random


class TeamB(Strategy):
	WIDTH = 64
	HEIGHT = 24

	def execute(self, currentStatus, resultOrder):
		resultOrder.clear()
		#프리볼
		if currentStatus.ballOwner == -1:
			for i in range(6):
				self.move2(i, currentStatus.mine[i], currentStatus.ball, resultOrder)
		#우리볼일경우
		elif currentStatus.ballOwner < 6:
			for i in range(6):
				resultOrder.dx[i] += 1
				if currentStatus.mine[i].getX() <= self.WIDTH/2:
					resultOrder.dy[i] = self.getRandom(-1, 1)
				else:
					self.move2goal(i, currentStatus.mine[i], resultOrder)
			if currentStatus.turn % 2 == 0:
				ballOwnersX = currentStatus.mine[currentStatus.ballOwner].getX()
				for j in range(6):
					if ballOwnersX < currentStatus.enemy[j].getX():
						resultOrder.passto = self.getRandom(0, 5)
						break
		# 수비;
		else:
			for i in range(3):
				self.move2(i, currentStatus.mine[i], currentStatus.ball, resultOrder)
			for i in range(3,6):
				if currentStatus.mine[i].getX()<20:
					if currentStatus.turn%3==0:
						self.move2(i, currentStatus.mine[i], currentStatus.ball, resultOrder)
					elif currentStatus.turn%3==1:
						resultOrder.dx[i]=0
						resultOrder.dy[i]=0
					else:
						self.runAway2(i,currentStatus.mine[i],currentStatus.ball,resultOrder)
				else:
					resultOrder.dx[i]-=1
					resultOrder.dy[i]=self.HEIGHT/2-currentStatus.mine[i].getY()

			
			# for i in range(3):
			# 	self.move2(i, currentStatus.mine[i], currentStatus.ball, resultOrder)
			# for i in range(3, 6):
			# 	if currentStatus.mine[i].getX() < 20:
			# 		if currentStatus.turn % 3 == 1:
			# 			resultOrder.dx[i] = 0
			# 			resultOrder.dy[i] = 0
			# 		else:
			# 			self.runAway2(i, currentStatus.mine[i], currentStatus.ball, resultOrder)
			# 	else:
			# 		resultOrder.dx[i] -= 1
			# 		resultOrder.dy[i] = self.HEIGHT/2-currentStatus.mine[i].getY()
			# for i in range(6):
			# 	self.move2(i, currentStatus.mine[i], currentStatus.enemy[i], resultOrder)
			# 	resultOrder.dx[i] -= 1
			# for (int i = 0; i < 6; i++) {
			# 	move2(i, currentStatus.mine[i], currentStatus.enemy[i], resultOrder);
			# 	resultOrder.dx[i]--;
			# }
			# 	resultOrder.dx[i]-=1
			# for i in range(3):
			# 	self.move2(i, currentStatus.mine[i], currentStatus.ball, resultOrder)
			# for i in range(3, 6):
			# 	if currentStatus.mine[i].getX() < 20:
			# 		if currentStatus.turn % 3 == 1:
			# 			resultOrder.dx[i] = 0
			# 			resultOrder.dy[i] = 0
			# 		else:
			# 			self.runAway2(i, currentStatus.mine[i], currentStatus.ball, resultOrder)
			# 	else:
			# 		resultOrder.dx[i] -= 1
			# 		resultOrder.dy[i] = self.HEIGHT/2-currentStatus.mine[i].getY()

	def move2(self, number, src, dest, resultOrder):
		dx = dy = 0
		if src.getX() == dest.getX():
			dx = 0
		else:
			dx = (dest.getX()-src.getX())/abs(dest.getX()-src.getX())
		if src.getY() == dest.getY():
			dy = 0
		else:
			dy = (dest.getY()-src.getY())/abs(dest.getY()-src.getY())
		resultOrder.dx[number] = dx
		resultOrder.dy[number] = dy

	def runAway2(self, number, src, dest, resultOrder):
		dx = dy = 0
		if src.getX() == dest.getX():
			dx = 0
		else:
			dx = -(dest.getX()-src.getX())/abs(dest.getX()-src.getX())
		if src.getY() == dest.getY():
			dy = 0
		else:
			dy = -(dest.getY()-src.getY())/abs(dest.getY()-src.getY())
		resultOrder.dx[number] = dx
		resultOrder.dy[number] = dy

	def move2goal(self, number, u, resultOrder):
		resultOrder.dx[number] += 1
		if u.getY() >= self.HEIGHT/2+5:
			resultOrder.dy[number] -= 1
		elif(u.getY() <= self.HEIGHT/2-5):
			resultOrder.dy[number] += 1
		else:
			resultOrder.dy[number] = 0

	def getRandom(self, m, n):
		return int(random.randint(m,n))
