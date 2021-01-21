
from Ball import Ball
from Order import Order
from Player import Player
from TeamA import TeamA
from TeamB import TeamB
from Status import Status
from Strategy import Strategy
from Unit import Unit
import sys, traceback, time, os
import random


# //Rule

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

class Game:
	__WIDTH=64
	__HEIGHT=24
	__NPLAYER=6
	__normalWaitTime=0
	__eventWaitTime=0
	__eventTurn= False
	__nTurn=0
	DEBUGMODE= False
	teamA=TeamA()
	teamB=TeamB()
	__score_a=0
	__score_b=0
	players= []
	playerOfTeamA=[]
	playerOfTeamB=[]
	ball=None
	ballOwner= None
	__screen=[[]]
	__statusA=Status()
	__statusB=Status()
	__orderA= Order()
	__orderB= Order()
	delay_time=0
	# public _Game() {
	# 	this(1000, 500);
	# }
	def __init__(self, normalWaitTime, eventWaitTime):
		self.__normalWaitTime=normalWaitTime
		self.__eventWaitTime=eventWaitTime
		self.__eventTurn=False
		self.__nTurn=0
		self.teamA=TeamA()
		self.teamB=TeamB()
		self.__score_a=0
		self.__score_b=0

	def run(self):
		self.__initialize(0)
		self.__printScr()
		self.__delay(self.delay_time)
		while True:
			self.__nTurn+=1
			self.clearScr()
			self.onetick()
			self.__applyOrder(self.__orderA, self.__orderB)
			self.__printScr()
			goalCheck=self.goalCheck()
			if goalCheck==1:
				self.__eventTurn=True
				self.__initialize(2)
			elif goalCheck==2:
				self.__eventTurn=True
				self.__initialize(1)

			
			if self.__endCheck()==1:
				print("end game")
				if self.__score_a>self.__score_b: return 1
				elif self.__score_b>self.__score_a: return 2
				else: return 0
			if self.__eventTurn:
				self.__delay(self.delay_time*5)
				self.__eventTurn=False
			else:
				self.__delay(self.delay_time)
	
	def __initialize(self, starter):
		self.__screen=[[None for _ in range(self.__HEIGHT)] for _ in range(self.__WIDTH)]
		self.playerOfTeamA=[0]*6
		self.playerOfTeamA[0]=Player(0, ((self.__WIDTH-1)/2) -25, self.__HEIGHT/2-4, 30, 30, 40)
		self.playerOfTeamA[1]=Player(1, ((self.__WIDTH-1)/2) -24, self.__HEIGHT/2-2, 25, 40, 50)
		self.playerOfTeamA[2]=Player(2, ((self.__WIDTH-1)/2) -23, self.__HEIGHT/2, 20, 50, 35)
		self.playerOfTeamA[3]=Player(3, ((self.__WIDTH-1)/2) -24, self.__HEIGHT/2+2, 25, 40, 50)
		self.playerOfTeamA[4]=Player(4, ((self.__WIDTH-1)/2) -25, self.__HEIGHT/2+4, 30, 30, 40)
		self.playerOfTeamA[5]=Player(5, ((self.__WIDTH-1)/2) -28, self.__HEIGHT/2, 50, 50, 55)
		self.playerOfTeamB=[0]*6
		self.playerOfTeamB[0]=Player(6, ((self.__WIDTH-1)/2)+26 , self.__HEIGHT/2-4, 30, 30, 40)
		self.playerOfTeamB[1]=Player(7, ((self.__WIDTH-1)/2) +25, self.__HEIGHT/2-2, 25, 40, 50)
		self.playerOfTeamB[2]=Player(8, ((self.__WIDTH-1)/2)+24 , self.__HEIGHT/2, 20, 50, 35)
		self.playerOfTeamB[3]=Player(9, ((self.__WIDTH-1)/2) +25, self.__HEIGHT/2+2, 25, 40, 50)
		self.playerOfTeamB[4]=Player(10, ((self.__WIDTH-1)/2) +26, self.__HEIGHT/2+4, 30, 30, 40)
		self.playerOfTeamB[5]=Player(11, ((self.__WIDTH-1)/2) +29, self.__HEIGHT/2, 50, 50, 55)
		self.players=[0]*12

		for i in range(len(self.players)):
			if i<6: self.players[i]=self.playerOfTeamA[i]
			else: self.players[i]=self.playerOfTeamB[i-6]
		
		if starter==0:
			self.__score_a=0
			self.__score_b=0
			self.__nTurn=0
			if self.__throwCoinWithProbability(50):
				self.ball=Ball(self.playerOfTeamA[2].x, self.playerOfTeamA[2].y)
				self.ballOwner=self.playerOfTeamA[2]
				self.playerOfTeamA[2].getBall(self.ball)
			else:
				self.ball=Ball(self.playerOfTeamB[2].x, self.playerOfTeamB[2].y)
				self.ballOwner=self.playerOfTeamB[2]
				self.playerOfTeamB[2].getBall(self.ball)
		elif starter==1:
			self.ball=Ball(self.playerOfTeamA[2].x, self.playerOfTeamA[2].y)
			self.ballOwner=self.playerOfTeamA[2]
			self.playerOfTeamA[2].getBall(self.ball)
		elif starter==2:
			self.ball=Ball(self.playerOfTeamB[2].x, self.playerOfTeamB[2].y)
			self.ballOwner=self.playerOfTeamB[2]
			self.playerOfTeamB[2].getBall(self.ball)
		
		for p in self.players:
			self.__screen[int(p.x)][int(p.y)] = p
	
	def onetick(self):
		ballOwner= -1 if self.ballOwner==None else self.ballOwner.getId()
		self.__statusA.setStatus(self.__generateUnitInfo(False), ballOwner, self.__nTurn)
		self.teamA.execute(self.__statusA,self.__orderA)
		ballOwnerForB=0
		if ballOwner == -1: ballOwnerForB=-1
		elif ballOwner<6: ballOwnerForB=ballOwner+6
		else: ballOwnerForB=ballOwner-6
		self.__statusB.setStatus(self.__generateUnitInfo(False),ballOwnerForB, self.__nTurn)
		self.teamB.execute(self.__statusB, self.__orderB)
	
	def __generateUnitInfo(self,mirror):
		result=[0]*13
		if mirror:
			result[0]=Unit(self.ball, True)
			for i in range(1,13):
				if i<=6: result[i]= Unit(self.playerOfTeamB[i-1],True)
				else: result[i]=Unit(self.playerOfTeamA[i-7],True)
		else:
			result[0]=Unit(self.ball)
			for i in range(1,13):
				if i<=6: result[i]=Unit(self.playerOfTeamA[i-1])
				else: result[i]=Unit(self.playerOfTeamB[i-7])
		return result
	
	def __applyOrder(self, oa, obr):
		ob=Order(obr,True)
		for p in self.playerOfTeamA:
			p.move(oa.dx[p.getNumber()], oa.dy[p.getNumber()])
			self.__screen[int(p.x)][int(p.y)]=p
		for p in self.playerOfTeamB:
			p.move(ob.dx[p.getNumber()], ob.dy[p.getNumber()])
			self.__screen[int(p.x)][int(p.y)] = p
		self.__contendCheck()
		self.__passCheck()
		self.__ballCheck()
	def __contendCheck(self):
		for i in range(self.__NPLAYER*2):
			contendingPlayers=[]
			for j in range(i+1, self.__NPLAYER*2):
				if self.players[i].isSamePositionWith(self.players[j]):
					contendingPlayers.append(self.players[j])
			if len(contendingPlayers)>=1:
				contendingPlayers.append(self.players[i])
				self.__contend(contendingPlayers)

	def __contend(self, players):
		rlength=0
		winner=-1
		for p in players:
			rlength+=p.getStrength()
		if rlength==0:
			winner=self.getRandom(0, len(players)-1)
		else:
			r= self.getRandom(0, rlength-1)
			rm=rM=0
			for i in range(len(players)):
				rM+=players[i].getStrength()
				if r>=rm and r<rM:
					winner=i
					break
				rm=rM
		wx=int(players[winner].x)
		wy=int(players[winner].y)
		for i in range(len(players)):
			if i==winner:
				players[i].winContending()
				self.__screen[wx][wy]=self.players[i]
			else:
				if players[i].isBallOwner():
					ix=wx+self.getRandom(-3,3)
					iy=wy+self.getRandom(-3,3)
					#screen!=none이여야 하나? ^^^^^^^^^^^^^^^^^^^^
					while (ix<0 or ix>=self.__WIDTH or iy<0 or iy>=self.__HEIGHT) or self.__screen[int(ix)][int(iy)]!=None:
						print("a")
						ix=wx+self.getRandom(-3,3)
						iy=wy+self.getRandom(-3,3)
					self.ball.fly2(ix, iy)
					self.ballOwner=None
			
				ix = int(wx+self.getRandom(-3, 3))
				iy = int(wy+self.getRandom(-3, 3))
				count = 0
				while (ix < 0 or ix >= self.__WIDTH or iy < 0 or iy >= self.__HEIGHT) or (self.__screen[int(ix)][int(iy)] != None):
					count += 1
					ix = int(wx+self.getRandom(-3, 3))
					iy = int(wy+self.getRandom(-3, 3))
				self.__screen[int(ix)][int(iy)] = players[i]


	def __passCheck(self):
		if self.ballOwner!=None:
			passtarget=None
			if (self.ballOwner.getId()>=0 and self.ballOwner.getId()<6) and self.__orderA.passto>=0 and self.ballOwner!=self.playerOfTeamA[self.__orderA.passto]:
				passtarget=self.playerOfTeamA[self.__orderA.passto]
			elif (self.ballOwner.getId()>=6 and self.ballOwner.getId()<12) and self.__orderB.passto>=0 and self.ballOwner!=self.playerOfTeamB[self.__orderB.passto]:
				passtarget=self.playerOfTeamB[self.__orderB.passto]
			else: return
			m=n=0
			if self.ballOwner.getX()==passtarget.getX():
				yV=int((passtarget.getY()- self.ballOwner.getY())/ abs(passtarget.getY()-self.ballOwner.getY()))
				for i in range(self.ballOwner.getY(), passtarget.getY()-yV, yV):
					if self.__screen[int(self.ballOwner.getX())][int(i)]!=None:
						print("intercepted")
						self.ballOwner.throwBall()
						interceptPlayer=self.__screen[int(self.ballOwner.getX())][int(i)]
						interceptPlayer.getBall(self.ball)
						self.ballOwner=interceptPlayer
						return
			else:
				m=(self.ballOwner.getY()- passtarget.getY())/(self.ballOwner.getX()-passtarget.getX())
				n=self.ballOwner.getY()-m*self.ballOwner.getX()
				xV=int((passtarget.getX()-self.ballOwner.getX())/abs(passtarget.getX()-self.ballOwner.getX()))
				i= int(self.ballOwner.getX()+xV)
				while i!=passtarget.getX():
					i+=xV
					fi=int(m*i +n+0.4)
					if self.__screen[int(i)][int(fi)]!=None:
						print("intercepted")
						self.ballOwner.throwBall()
						interceptPlayer=self.__screen[int(i)][int(fi)]
						interceptPlayer.getBall(self.ball)
						self.ballOwner=interceptPlayer
						return
			d= int(self.ballOwner.getDistance(passtarget))
			if d>5:
				r=self.getRandom(0,self.ballOwner.getPass()+d*5-1)
				if r<self.ballOwner.getPass():
					self.ballOwner.throwBall()
					passtarget.getBall(self.ball)
					self.ballOwner=passtarget
				#공이 날라갔을 경우
				else:
					self.ballOwner.throwBall()
					ix=passtarget.getX()+self.getRandom(-2,2)
					iy=passtarget.getY()+self.getRandom(-2,2)
					while (ix<0 or ix>self.__WIDTH or iy<0 or iy>=self.__HEIGHT) or (ix==passtarget.getX() and iy==passtarget.getY()):
						ix = passtarget.getX()+self.getRandom(-2, 2)
						iy = passtarget.getY()+self.getRandom(-2, 2)
					self.ball.fly2(ix,iy)
					self.ballOwner=None
			#pass 성공된 경우
			else:
				self.ballOwner.throwBall()
				passtarget.getBall(self.ball)
				self.ballOwner=passtarget
	
	def __ballCheck(self):
		if self.ballOwner!=None:
			self.ball.dribbled(self.ballOwner)
		else:
			if self.__screen[self.ball.getX()][self.ball.getY()]!=None:
				self.ballOwner=self.__screen[self.ball.getX()][self.ball.getY()]
				self.ballOwner.getBall(self.ball)
	
	def getRandom(self,m,n):
		return int(random.randint(m,n))
	
	def __throwCoinWithProbability(self, probability):
		if probability<0 or probability>100:
			print("probability error")
			return False
		r= int(random.random()*100)
		if r<probability: return True
		else: return False

	def __printScr(self):
		# if self.ballOwner==None: self.__screen[self.ball.x][self.ball.y]=self.ball
		print(f'Score: {self.__score_a} : {self.__score_b} ')
		print(f' Turn: {self.__nTurn}')
		
		print('%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%')
		for i in range(self.__HEIGHT):
			print_list = list()
			if i < self.__HEIGHT/2-5 or i >= self.__HEIGHT/2+5: print('%', end='')
			else: print(" ", end='')
			for j in range(self.__WIDTH):
				if self.__screen[j][i] == None: print_list.append(" ")
				else:
					if self.__screen[j][i].getId() >= 0 and self.__screen[j][i].getId() < 6:
						print_list.append(chr(ord('a')+self.__screen[j][i].getNumber()))
					elif self.__screen[j][i].getId() >= 6 and self.__screen[j][i].getId() < 12:
						print_list.append(str(1+self.__screen[j][i].getNumber()))
			print("".join(print_list), end='')
			if i < self.__HEIGHT/2-5 or i >= self.__HEIGHT/2+5: print('%')
			else: print(" ")
		print('%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%')
		os.system('cls')
	
	def clearScr(self):
		# System.out.println("\033[H\033[2J");
		# System.out.flush(); 
		self.__screen=[[None for _ in range(self.__HEIGHT)] for _ in range(self.__WIDTH)]
		sys.stdout.flush()
	
	def goalCheck(self):
		if self.ballOwner!=None:
			if self.ballOwner.getId()>=0 and self.ballOwner.getId()<6:
				if self.ballOwner.getX()== self.__WIDTH-1 and self.ballOwner.getY()>=self.__HEIGHT/2-5 and self.ballOwner.getY()< self.__HEIGHT/2+5:
					self.__score_a+=1
					return 1
			elif self.ballOwner.getId()>=6 and self.ballOwner.getId()<12:
				if self.ballOwner.getX()==0 and (self.ballOwner.getY()>= self.__HEIGHT/2-5 and self.ballOwner.getY()<self.__HEIGHT/2+5):
					self.__score_b+=1
					return 2
		return -1
	
	def __endCheck(self):
		if self.__nTurn<1000: return 0
		elif self.__nTurn==1000: return 1
	
	def __delay(self, n):
		try:
			time.sleep(n)
		except:
			traceback.print_exc()

def main():
	print('Input turn delay time(in ms):')
	num = int(input())/10000
	game=Game(0,0)
	game.delay=num
	game.run()


main()
