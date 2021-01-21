from Unit import Unit

class Player(Unit):
	WIDTH=64
	HEIGHT=24
	__id=0
	__number=0
	ball=None
	#original stat
	__maxStrngth=0
	__maxPass=0
	__maxStamina=0
	#current stat
	__strength=0
	__pass1=0
	__stamina=0
	def __init__(self, id,x,y,maxStrength ,maxPass,maxStamina):
		super().__init__(x=int(x),y=int(y))
		self.__id=id
		if id>=0 and id<6:
			self.__number=id
		elif id>=6 and id<12:
			self.__number=id-6
		self.__maxStrngth=self.__strength=maxStrength
		self.__maxPass=self.__pass1=maxPass
		self.__maxStamina=self.__stamina=maxStamina
	
	def getBall(self,ball):
		self.ball=ball

	def move(self, dx, dy):
		if dx!=0: dx/=abs(dx)
		if dy!=0: dy/=abs(dy)
		if self.x==self.WIDTH-1 and dx==1: dx=0
		elif self.x==0 and dx==-1: dx=0
		if self.y==self.HEIGHT-1 and dy==1: dy=0
		elif self.y==0 and dy==-1: dy=0

		if self.ball!=None and self.__stamina<6 or self.__stamina<3:
			dx=dy=0

		if dx==0 and dy==0: self.restore()
		else: 
			if self.ball!=None: self.__consumeStamina(6)
			else: self.__consumeStamina(3)
			self.x+=dx
			self.y+=dy
	
	def restore(self):
		self.__stamina+=10
		if self.__stamina>self.__maxStamina: self.__stamina=self.__maxStamina
		self.__updateStat()

	def isSamePositionWith(self,p):
		if p.x==self.x and p.y==self.y: return True
		else: return False
	
	def winContending(self):
		self.__consumeStamina(2)
	def loseContending(self,x,y):
		self.__consumeStamina(5)
		if self.ball!=None: self.ball=None
		self.x=x
		self.y=y
	
	def throwBall(self):
		if self.ball!=None: self.ball=None
		else: print("The player doesn't have a ball")

	def __consumeStamina(self, n):
		self.__stamina-=n
		if self.__stamina<0: self.__stamina=0
		self.__updateStat()
	def __updateStat(self):
		a=self.__stamina/self.__maxStamina
		self.__strength=int(a*self.__maxStrngth)
		self.__pass1= int(a*self.__maxPass)
	def getId(self):
		return int(self.__id)
	def getNumber(self):
		return int(self.__number)
	def getStamina(self):
		return int(self.__stamina)
	def getStrength(self):
		if self.ball!=None: return int(0.75*self.__strength)
		else: return self.__strength
		
	def getPass(self):
		return int(self.__pass1)
	def isBallOwner(self):
		return self.ball!=None

	def __str__(self):
		if self.ball != None:
			if self.__id >= 0 and self.__id < 6:
				return "X"
			elif self.__id >= 6 and self.__id < 12:
				return "O"
			return None
		else:
			if self.getId() >= 0 and self.getId() < 6:
				return chr(ord('a')+self.getNumber)
			elif self.getId() >= 6 and self.getId() < 12:
				return str(1+self.getNumber)
			return None

		
	

