class TeamA(Strategy):
	WIDTH = 64
	HEIGHT = 24
	def execute(self, currentStatus, resultOrder):
		resultOrder.clear()
		if currentStatus.ballOwner == -1:
				shortestPlayer = 0
				shortestLength = currentStatus.mine[0].getDistance(currentStatus.ball)
				for i in range(6):
					length = currentStatus.mine[i].getDistance(currentStatus.ball)
					if shortestLength > length:
						shortestLength = length
						shortestPlayer = i
					for j in range(6):
						if j == i:
							self.move2(
								shortestPlayer, currentStatus.mine[shortestPlayer], currentStatus.ball, resultOrder)
						else:
							self.move2goal(j, currentStatus.mine[j], resultOrder)
					self.move2(shortestPlayer,
							currentStatus.mine[shortestPlayer], currentStatus.ball, resultOrder)
			elif currentStatus.ballOwner < 6:
				for i in range(6):
					if i == currentStatus.ballOwner:
						if currentStatus.mine[i].getX() <= self.WIDTH-60:
							self.move2goal(i, currentStatus.mine[i], resultOrder)
						resultOrder.passto = self.perfectPassPlayer(i, currentStatus)
						if int(3*random.random() != 0):
							self.move2goal(i, currentStatus.mine[i], resultOrder)
					else:
						#ballowner와 player거리가 2이하일때
						if currentStatus.mine[i].getDistance(currentStatus.mine[currentStatus.ballOwner]) < 2:
							if int(3*random.random() != 0):
								self.runAway2(i, currentStatus.mine[i], currentStatus.ball, resultOrder)
						# ballOwner와 players 거리가 5보다 클 때
						elif currentStatus.mine[i].getDistance(currentStatus.mine[currentStatus.ballOwner]) > 5:
							if int(3*random.random() != 0):
								self.move2(i, currentStatus.mine[i], currentStatus.ball, resultOrder)
						#  ballOwner와 players 거리를 3으로 유지
						else:
							if int(3*random.random() != 0):
								self.move2goal(i, currentStatus.mine[i], resultOrder)
			#수비
			else:
				# 우리팀 i
				for i in range(6):
					properPlayer = 3  # 임의의 player 설정
					# 우리팀 j
					for j in range(6):
						maxDistance = 1
						enemyDistance = 1
						if j == i:
							continue
						else:
							# 상대팀 선수 k에 대해서
							for k in range(6):
								newEnemyDistance = currentStatus.mine[j].getDistance(
																currentStatus.enemy[k])
								if enemyDistance > newEnemyDistance:
									enemyDistance = newEnemyDistance
						if maxDistance < enemyDistance:
							maxDistance = enemyDistance
						# 거리가 가장 먼 distance의 주인 j가 properPlayer이 됨
						properPlayer = j
						if i == properPlayer:
							resultOrder.dx[i] -= 1
							if currentStatus.mine[i].getY() > 18:
								resultOrder.dy[i] -= 1
							elif currentStatus.mine[i].getY() < 6:
								resultOrder.dy[i] += 1
						# 	수비수 중 enemy와 가장 거리가 먼 player를 우리 진영 골대로 이동시킴. x좌표 - -, y좌표는 currentStatus에 따라
						#    + 또는 -
							else:
								self.move2(i, currentStatus.mine[i], currentStatus.ball, resultOrder)
	
	def runAway2(self, number, src, dest, resultOrder):
		dx = dy = 0
		if src.getX() == dest.getX(): dx = 0
		else: dx = -(dest.getX()-src.getX())/abs(dest.getX()-src.getX())
		if src.getY() == dest.getY(): dy = 0
		else: dy = -(dest.getY()-src.getY())/abs(dest.getY()-src.getY())
		resultOrder.dx[number] = dx
		resultOrder.dy[number] = dy

	def move2goal(self, number, u, resultOrder):
		resultOrder.dx[number] += 1
		if u.getY() >= self.HEIGHT/2+5: resultOrder.dy[number] -= 1
		elif(u.getY() <= self.HEIGHT/2-5): resultOrder.dy[number] += 1
		else: resultOrder.dy[number] = 0

	def getRandom(self, m, n):
		d = abs(m-n)+1
		return int(random.random()*d)+m

	def perfectPassPlayer(self, mineBallOwner, currentStatus):
		perfectPassPlayers = [0]*6
		ballOwnersX = currentStatus.mine[currentStatus.ballOwner].getX()
		ballOwnersY = currentStatus.mine[currentStatus.ballOwner].getY()
		minesX = [0]*6
		minesY = [0]*6
		enemysX = [0]*6
		enemysY = [0]*6
		for num in range(6):
			minesX[num] = currentStatus.mine[num].getX()
			minesY[num] = currentStatus.mine[num].getY()
			enemysX[num] = currentStatus.enemy[num].getX()
			enemysY[num] = currentStatus.enemy[num].getY()
			#outerloop
		for i in range(6):
			#본인일 경우
			flag = False
			if mineBallOwner == i:
				continue
			else:
				# 같은 x선 상에 있을 경우
				if ballOwnersX == minesX[i]:
					#같은 좌표에 있을 경우
					if ballOwnersY == minesY[i]:
						continue
					yVmine = int((minesY[i]-ballOwnersY)/abs(minesY[i]-ballOwnersY))
					#y좌표 거리
					mineDistance = abs(minesY[i]-ballOwnersY)
					for j in range(6):
						if enemysX[j] == ballOwnersX:
							if enemysY[j] == ballOwnersY:
								continue
							yVenemy = int((enemysY[j]-ballOwnersY)/abs(enemysY[j]-ballOwnersY))
							enemyDistance = abs(enemysY[i]-ballOwnersY)
							if yVmine == yVenemy and enemyDistance < mineDistance:
								flag = True
								break
					if flag:
						continue
					perfectPassPlayers[i] = i
				else:
					#대각선 혹은 같은 y선상에 있을경우
					m = (ballOwnersY-minesY[i])/(ballOwnersX-minesX[i])
					xVmine = int((minesX[i]-ballOwnersX)/abs(minesX[i]-ballOwnersX))
					mineDistance = abs(minesX[i]-ballOwnersX)
					for j in range(6):
						if ballOwnersX == enemysX[j]:
							continue
						else:
							n = (ballOwnersY-enemysY[j])/(ballOwnersX-enemysX[j])
							xVenemy = int((enemysX[j]-ballOwnersX)/abs(enemysX[j]-ballOwnersX))
							enemyDistance = abs(enemysX[i]-ballOwnersX)
							if m == n and xVmine == xVenemy and enemyDistance < mineDistance:
								#그냥 break도 가능
								flag = True
								break
					if flag:
						continue
					perfectPassPlayers[i] = i
		perfectPassPlayer = currentStatus.ballOwner
		mostclosetogoal = 100
		for i in range(6):
			if perfectPassPlayers[i] == 0:
				continue
			else:
				distance = 63-minesX[i]
				if mostclosetogoal > distance:
					mostclosetogoal = distance
					perfectPassPlayer = i
		return perfectPassPlayer
