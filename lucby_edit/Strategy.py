from abc import *
import random


class Strategy(metaclass=ABCMeta):
	@abstractmethod
	def execute(self, currentStatus, resultOrder):
		pass
	#추상메소드는 호출할일이 없어서 그냥 pass~ 상속받은 클래스에서 구현~

	@abstractmethod
	def getRandom(m, n):
		pass
