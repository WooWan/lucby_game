from Ball import Ball
from Order import Order
from Player import Player
from Status import Status
from Strategy import Strategy
from TeamA import TeamA
from TeamB import TeamB
from Unit import Unit
from random import random
import sys, traceback, time, os






# //
# //	                   ###initial position###
# //     0123456789012345678901234567890123456789012345678901234567890123
# //	  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% |
# //	0 %                                                                % |
# //	1 %                                                                % |
# //	2 %                                                                % |
# //	3 %                                                                % |
# //	4 %                                                                % |
# //	5 %                                                                % |
# //	6 %                                                                % |
# //	7                                                                    |
# //	8                                                                    |
# //	9                                                                    |
# //	0                                                                    |
# //	1    f    c                                                 |
# //	2                                                                    |
# //	3                                                                    |
# //	4                                                                    |
# //	5                                                                    |
# //	6                                                                    |
# //	7 %                                                                % |
# //	8 %                                                                % |
# //	9 %                                                                % |
# //	0 %                                                                % |
# //	1 %                                                                % |
# //	2 %                                                                % |
# //	3 %                                                                % |
# //	  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% |

class _Game:
	__WIDTH=64
	__HEIGHT=24
	__NPLAYER=6
	__normalWaitTime=0
	__eventWaitTime=0
	__eventTurn=False
	__nTurn=0
	DEBUGMODE=False
	teamA=TeamA()
	teamB=TeamB()
	__score_a=0
	__score_b=0
	players=[]
	playerOfTeamA=[]
	playerOfTeamB=[]
	ball=None
	ballOwner=None
	__screen=[[]]
	__statusA=Status()
	__statusB=Status()
	__orderA=Order()
	__orderB=Order()
	delay_time=0
	# public _Game() {
	# 	this(1000, 500);
	# }

	def __init__(self, normalWaitTime=None, eventWaitTime=None):
		self.__normalWaitTime=normalWaitTime
		self.__eventWaitTime=eventWaitTime

	def run(self):
		self.initialize(0)
		#self.updateScr()
		self.__printScr()
		self.delay(self.delay_time)
		while True:
			self.__nTurn+=1
			self.clearScr()
			self.onetick()
			self.applyOrder(self.__orderA, self.__orderB)
			self.__printScr()
			goalCheck=self.__goalCheck()
			if goalCheck==1:
				self.__eventTurn=True
				self.initialize(2)
			elif goalCheck==2:
				self.__eventTurn=True
				self.initialize(1)
			if self.endCheck()==1:
				print("endgame")
				if self.__score_a>self.__score_b: return 1
				elif self.__score_b>self.__score_a: return 2
				else: return 0
			if self.__eventTurn:
				self.delay(self.delay_time*5)
				self.__eventTurn=False
			else:
				self.delay(self.delay_time)
	def initialize(self, starter):
		self.__screen= [[None for _ in range(self.__HEIGHT)] for _ in range(self.__WIDTH)]
		self.playerOfTeamA=[None]*6
		self.playerOfTeamA[0]=Player(0, ((self.__WIDTH-1)/2)-25 , self.__HEIGHT/2-4, 30,30,40)
		self.playerOfTeamA[1]=Player(1, ((self.__WIDTH-1)/2)-24 , self.__HEIGHT/2-2, 25,40,50)
		self.playerOfTeamA[2]=Player(2, ((self.__WIDTH-1)/2)-23 , self.__HEIGHT/2, 20,50,35)
		self.playerOfTeamA[3]=Player(3, ((self.__WIDTH-1)/2)-24 , self.__HEIGHT/2+2, 25,40,50)
		self.playerOfTeamA[4] = Player(4, ((self.__WIDTH-1)/2)-25, self.__HEIGHT/2+4, 30, 30, 40)
		self.playerOfTeamA[5] = Player(5, ((self.__WIDTH-1)/2)-28, self.__HEIGHT/2, 50, 50, 55)
		self.playerOfTeamB=[None]*6
		self.playerOfTeamB[0]=Player(6, ((self.__WIDTH-1)/2)+26 , self.__HEIGHT/2-4, 30,30,40)
		self.playerOfTeamB[1]=Player(7, ((self.__WIDTH-1)/2)+25 , self.__HEIGHT/2-2, 25,40,50)
		self.playerOfTeamB[2]=Player(8, ((self.__WIDTH-1)/2)+24 , self.__HEIGHT/2, 20,50,35)
		self.playerOfTeamB[3]=Player(9, ((self.__WIDTH-1)/2)+25 , self.__HEIGHT/2+2, 25,40,50)
		self.playerOfTeamB[4] = Player(10, ((self.__WIDTH-1)/2)+26,self.__HEIGHT/2+4, 30, 30, 40)
		self.playerOfTeamB[5] = Player(11, ((self.__WIDTH-1)/2)+29,self.__HEIGHT/2, 50, 50, 55)

		self.players=[None]*12
		for i in range(12):
			if i<6: self.players[i]=self.playerOfTeamA[i]
			else: self.players[i]=self.playerOfTeamB[i-6]
		if starter==0:
			self.__score_a = 0
			self.__score_b = 0
			self.__nTurn = 0
			if self.__throwCointWithProbability(50):
				self.ball=Ball(self.playerOfTeamA[2].x,self.playerOfTeamA[2].y)
				self.ballOwner=self.playerOfTeamA[2]
				self.playerOfTeamA[2].getBall(self.ball)
			else:
				self.ball=Ball(self.playerOfTeamB[2].x, self.playerOfTeamB[2].y)
				self.ballOwner=self.playerOfTeamB[2]
				self.playerOfTeamB[2].getBall(self.ball)
		elif starter==1:
			self.ball=Ball(self.playerOfTeamA[2].x,self.playerOfTeamA[2].y)
			self.ballOwner=self.playerOfTeamA[2]
			self.playerOfTeamA[2].getBall(self.ball)
		elif starter==2:
			self.ball=Ball(self.playerOfTeamB[2].x, self.playerOfTeamB[2].y)
			self.ballOwner=self.playerOfTeamB[2]
			self.playerOfTeamB[2].getBall(self.ball)

		for p in self.players:
			self.__screen[int(p.x)][int(p.y)]=p
	
	def onetick(self):
		ballOwner=-1 if self.ballOwner==None else self.ballOwner.getId()
		self.__statusA.setStatus(self.generateUnitInfo(False), ballOwner, self.__nTurn)
		self.teamA.execute(self.__statusA,self.__orderA)
		ballOwnerForB=0
		if ballOwner==-1: ballOwnerForB=-1
		elif ballOwner<6: ballOwnerForB=ballOwner+6
		else: ballOwnerForB=ballOwner-6
		self.__statusB.setStatus(self.generateUnitInfo(False), ballOwnerForB,self.__nTurn)
		self.teamB.execute(self.__statusB,self.__orderB)
		# // debug
		# // for (int i = 0; i < 6; i++) {
		# // System.out.println((char) ('a' + i) + ":(" + orderA.dx[i] + "," +
		# // orderA.dy[i] + ")\t" + (char) ('1' + i)
		# // + ":(" + orderB.dx[i] + "," + orderB.dy[i] + ")");
		# // }
	def generateUnitInfo(self, mirror):
		result = [None]*13
		#teamB
		if mirror:
			result[0]=Unit(self.ball,True)
			for i in range(1,13):
				if i<=6: result[i]=Unit(self.playerOfTeamB[i-1],True)
				else: result[i]=Unit(self.playerOfTeamA[i-7],True)
		#teamA
		else:
			result[0]=Unit(self.ball)
			for i in range(1,13):
				if i<=6: result[i]= Unit(self.playerOfTeamA[i-1])
				else: result[i]=Unit(self.playerOfTeamB[i-7])
		return result

	#main process
	def applyOrder(self, oa, obr):
		ob=Order(obr,True)
		#move하기 전에 좌표를 none처리 필요해보임 edit**************
		for p in self.playerOfTeamA:
			p.move(oa.dx[p.getNumber()], oa.dy[p.getNumber()])
			self.__screen[int(p.x)][int(p.y)] = p
			# if oa.dx[p.getNumber()]!=0 or oa.dy[p.getNumber()]!=0:
			# 	self.__screen[int(p.x)][int(p.y)]=None
			# 	p.move(oa.dx[p.getNumber()],oa.dy[p.getNumber()])
			# 	self.__screen[int(p.x)][int(p.y)] = p
		for p in self.playerOfTeamB:
			p.move(ob.dx[p.getNumber()], ob.dy[p.getNumber()])
			self.__screen[int(p.x)][int(p.y)] = p
			# if ob.dx[p.getNumber()]!=0 or ob.dy[p.getNumber()]!=0:
			# 	self.__screen[int(p.x)][int(p.y)]=None
			# 	p.move(ob.dx[p.getNumber()], ob.dy[p.getNumber()])
			# 	self.__screen[int(p.x)][int(p.y)] = p
		self.__contendCheck()
		self.__passCheck()
		self.__ballCheck()

	def __contendCheck(self):
		for i in range(self.__NPLAYER*2):
			contendingPlayers=[]
			for j in range(i+1,self.__NPLAYER*2):
				if self.players[i].isSamePositionWith(self.players[j]):
					contendingPlayers.append(self.players[j])
			if len(contendingPlayers)>=1:
				contendingPlayers.append(self.players[i])
				self.contend(contendingPlayers)
	
	def contend(self, players):
		rlength=0
		winner=-1
		for p in players:
			rlength+=p.getStrength()
		if rlength==0: winner=self.getRandom(0, len(players)-1)
		else:
			r=self.getRandom(0, rlength-1)
			rm=rM=0
			for i in range(len(players)):
				rM+= players[i].getStrength()
				if r>=rm and r<rM:
					winner=i
					break
				rm=rM
		
		wx=int(players[winner].x)
		wy=int(players[winner].y)
		for i in range(len(players)):
			if i ==winner: 
				players[i].winContending()
				self.__screen[wx][wy] = players[i]
			#공 날라감
			#공이 날라가는 좌표는 None이 아니여도 된다 수정 필요해보임*******************, ball.fly2함수 필요해보임 **
			else:
				if players[i].isBallOwner():
					ix=wx+self.getRandom(-3,3)
					iy=wy+self.getRandom(-3,3)
					while (ix<0 or ix>=self.__WIDTH or iy<0 or iy>=self.__HEIGHT) or (self.__screen[int(ix)][int(iy)]!=None):
						ix=wx+self.getRandom(-3,3)
						iy=wy+ self.getRandom(-3,3)
					# self.ball.fly2(ix, iy)
					self.ballOwner=None

				ix=int(wx+self.getRandom(-3,3))
				iy=int(wy+self.getRandom(-3,3))
				count=0
				while (ix<0 or ix>=self.__WIDTH or iy<0 or iy>=self.__HEIGHT) or (self.__screen[int(ix)][int(iy)]!=None):
					count+=1
					ix = int(wx+self.getRandom(-3, 3))
					iy = int(wy+self.getRandom(-3, 3))
				self.__screen[int(ix)][int(iy)]=players[i]
				if self.DEBUGMODE: print(f' player {i} is intend to go to {ix}, {iy}')
				#apply stamina, set position, ballowner false
				players[i].loseContending(ix,iy)
	
	def __passCheck(self):
		if self.ballOwner!=None:
			passtarget=None
			if (self.ballOwner.getId()>=0 and self.ballOwner.getId()<6 ) and self.__orderA.passto>=0 and self.ballOwner!=self.playerOfTeamA[self.__orderA.passto]:
				passtarget=self.playerOfTeamA[self.__orderA.passto]
			elif (self.ballOwner.getId() >= 6 and self.ballOwner.getId() < 12) and self.__orderB.passto >= 0 and self.ballOwner != self.playerOfTeamB[self.__orderB.passto]:
				passtarget = self.playerOfTeamB[self.__orderB.passto]
			else: return

			if self.DEBUGMODE: print(f' pass from {self.ballOwner.getId()} to {passtarget.getId()} ')
			#같은 x선 좌표에 있을경우
			m=n=0
			if self.ballOwner.getX()== passtarget.getX():
				yV=int((passtarget.getY()-self.ballOwner.getY())/abs(passtarget.getY()-self.ballOwner.getY()))
				i=int(self.ballOwner.getY())
				while i!= passtarget.getY()-yV:
					i+=yV
					if(self.__screen[self.ballOwner.getX()][i] != None):
						#intercepted by screen[ballOwner.x][i]
						print("intercepted!!!")
						self.ballOwner.throwBall()
						interceptPlayer = self.__screen[self.ballOwner.getX()][i]
						interceptPlayer.getBall(self.ball)
						self.ballOwner = interceptPlayer
						return
				# ballowner의 y좌표가 passtarget의 y좌표가 작을 경우만 고려되어있음 수정필요 (**********************)
				# for i in range(self.ballOwner.getY()+ yV, passtarget.getY()-yV,yV):
				# 	if self.__screen[self.ballOwner.getX()][i]!=None:
				# 		print('intercepted')
				# 		self.ballOwner.throwBall()
				# 		interceptPlayer= self.__screen[self.ballOwner.getX()][i]
				# 		interceptPlayer.getBall(self.ball)
				# 		self.ballOwner=interceptPlayer
				# 		return
				# ?? Ballowner가 passtarget보다 큰 경우가 있어 수정이 필요
			#대각선 혹은 y좌표가 같을경우
			else:
				m=(self.ballOwner.getY()-passtarget.getY())/(self.ballOwner.getX()-passtarget.getX())
				n=self.ballOwner.getY()-m*self.ballOwner.getX()
				xV= int((passtarget.getX()-self.ballOwner.getX())/abs(passtarget.getX()-self.ballOwner.getX()))
				i=self.ballOwner.getX()+xV
				while i!=passtarget.getX():
					i+=xV
					fi=int(m*i+n+0.4)
					if self.__screen[int(i)][int(fi)]!=None:
						print("intercepted!!!")
						self.ballOwner.throwBall()
						interceptPlayer=self.__screen[int(i)][int(fi)]
						interceptPlayer.getBall(self.ball)
						self.ballOwner=interceptPlayer
						return
			#인터셉트 안당했을 경우 거리에 따라 다른 확률 적용
			d=self.ballOwner.getDistance(passtarget)
			if d>5:
				r= self.getRandom(0, self.ballOwner.getPass()+d*5-1)
				if r<self.ballOwner.getPass():
					self.ballOwner.throwBall()
					passtarget.getBall(self.ball)
					self.ballOwner=passtarget
				else:
					self.ballOwner.throwBall()
					ix=passtarget.getX()+self.getRandom(-2,2)
					iy=passtarget.getY()+self.getRandom(-2,2)
					while(ix==passtarget.getX() and iy== passtarget.getY()) or (ix<0 or ix>=self.__WIDTH or iy<0 or iy>=self.__HEIGHT):
						ix=passtarget.getX()+self.getRandom(-2,2)
						iy=passtarget.getY()+self.getRandom(-2,2)
					self.ball.fly2(ix,iy)
					self.ballOwner=None
			else:
				self.ballOwner.throwBall()
				passtarget.getBall(self.ball)
				self.ballOwner=passtarget
	
	def __ballCheck(self):
		if self.ballOwner!=None: self.ball.dribbled(self.ballOwner)
		else:
			if self.__screen[self.ball.getX()][self.ball.getY()]!=None:
				self.ballOwner=self.__screen[self.ball.getX()][self.ball.getY()]
				self.ballOwner.getBall(self.ball)
	
	def getRandom(self,m,n):
		d= int(abs(m-n)+1)
		return int(random()*d)+m
	
	def __throwCointWithProbability(self, probability):
		if probability<0 or probability>100:
			print('probability error')
			return False
		r= int(random()*100)
		if r<probability: return True
		else: return False
	
	def __printScr(self):
		# if self.ballOwner==None: self.__screen[int(self.ball.x)][int(self.ball.y)]=self.ball
		print(f' Score {self.__score_a}: {self.__score_b} ')
		print(f' Turn: {self.__nTurn}')
		print('%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%')
		for i in range(self.__HEIGHT):
			print_list=list()
			if i<self.__HEIGHT/2-5 or i>=self.__HEIGHT/2+5: print('%', end='')
			else: print(" ", end='')
			for j in range(self.__WIDTH):
				if self.__screen[j][i]==None: print_list.append(" ")			
				else:	
					if self.__screen[j][i].getId() >= 0 and self.__screen[j][i].getId() < 6:
						print_list.append(chr(ord('a')+self.__screen[j][i].getNumber()))
					elif self.__screen[j][i].getId() >= 6 and self.__screen[j][i].getId() < 12:
						print_list.append(str(1+self.__screen[j][i].getNumber()))
			print("".join(print_list), end='')
			if i< self.__HEIGHT/2-5 or i>=self.__HEIGHT/2+5: print('%')
			else: print(" ")
		print('%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%')
		os.system('cls')
	
	def clearScr(self):
		# line=5
		# if self.DEBUGMODE==False: line+=10
		# for i in range(line):
		# 	print(" ")
		# print("\033[H\033[2J")
		os.system('cls')
		sys.stdout.flush()
		self.__screen= [[None for _ in range(self.__HEIGHT)] for _ in range(self.__WIDTH)]
	
	def __goalCheck(self):
		if self.ballOwner!=None:
			if self.ballOwner.getId()>=0 and self.ballOwner.getId()<6:
				if self.ballOwner.getX()==self.__WIDTH-1 and (self.ballOwner.getY()>=self.__HEIGHT/2-5 and self.ballOwner.getY()<self.__HEIGHT/2+5):
					self.__score_a+=1
					return 1
			elif self.ballOwner.getId()>=6 and self.ballOwner.getId()<12:
				if self.ballOwner.getX()==0 and  (self.ballOwner.getY()>=self.__HEIGHT/2-5 and self.ballOwner.getY()<self.__HEIGHT/2+5):
					self.__score_b+=1
					return 2
		return -1
	
	def endCheck(self):
		if self.__nTurn>=1000: return 1
		else: return 0
	
	def delay(self, n):
		try: time.sleep(n)
		except: traceback.print_exc()

def main():
	print('Input turn delay time(in ms):')
	num = int(input())/10000
	print(num)
	if _Game.DEBUGMODE:
		game=_Game(0,0)
		game.delay_time=num
		A=B=draw=0
		#항상 한번만 실행되면서 for문 불필요해보임 수정필요***********
		for i in range(1):
			result=game.run()
			if result==1: A+=1
			elif result==2: B+=1
			else: draw+=1
		print(f' result->A: {A} B:{B},draw: {draw}')
	else:
		game=_Game(0,0)
		game.delay_time=num
		game.run()
main()


