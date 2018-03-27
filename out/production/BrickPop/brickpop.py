colors = ['R', 'Y', 'G', 'B', 'P', 'D']
board_string =  'RGYYGGGGYG' + \
				'GGRYGGGYYG' + \
				'RGRYYGRYYG' + \
				'YYRYYRRYYG' + \
				'RGGYYRYYYY' + \
				'RRRYYRRYGY' + \
				'RRRYGYYYGY' + \
				'RRRYYRRRGY' + \
				'RGGGYRRRGY' + \
				'GYYGYYGGGY'

board_string2 = 'R*********' + \
				'R*********' + \
				'R*****RY*G' + \
				'R**RR*YRRG' + \
				'R**RRRYYRR' + \
				'R**RYYYYRR' + \
				'Y**YYGYRRR' + \
				'YY*YGGYRYY' + \
				'YYRYRYYRYY' + \
				'GYYYYYYYRY'


		
class Brick:
	def __init__(self, row, column, color):
		self.row = row
		self.column = column
		self.color = color

	# Returns a list of all bricks adjacent to self that are the same color
	def sameAdjacent(self, board):
		adjacents = []
		if self.row == 0:
			down = board[1][self.column]
			if down.color == self.color:
				adjacents.append(down)
			if self.column == 0:
				right = board[0][1]
				if right.color == self.color:
					adjacents.append(right)
			elif self.column == 9:
				left = board[0][8]		
				if left.color == self.color:
					adjacents.append(left)
			else:
				left = board[0][self.column - 1]	
				right = board[0][self.column + 1]
				if left.color == self.color:
					adjacents.append(left)
				if right.color == self.color:
					adjacents.append(right)
		elif self.row == 9:
			up = board[8][self.column]
			if up.color == self.color:
				adjacents.append(up)
			if self.column == 0:
				right = board[9][1]
				if right.color == self.color:
					adjacents.append(right)
			elif self.column == 9:
				left = board[9][8]		
				if left.color == self.color:
					adjacents.append(left)
			else:
				left = board[9][self.column - 1]	
				right = board[9][self.column + 1]
				if left.color == self.color:
					adjacents.append(left)
				if right.color == self.color:
					adjacents.append(right)
		else:
			up = board[self.row - 1][self.column]	
			down = board[self.row + 1][self.column]
			if down.color == self.color:
				adjacents.append(down)
			if up.color == self.color:
				adjacents.append(up)
			if self.column == 0:
				right = board[self.row][1]
				if right.color == self.color:
					adjacents.append(right)
			elif self.column == 9:
				left = board[self.row][8]		
				if left.color == self.color:
					adjacents.append(left)
			else:
				left = board[self.row][self.column - 1]	
				right = board[self.row][self.column + 1]
				if left.color == self.color:
					adjacents.append(left)
				if right.color == self.color:
					adjacents.append(right)
		return adjacents

	#Returns the block of same color that contains self
	def block(self, board):
		if self.color == '*':
			return []
		block = [self]
		i = 0
		while not hasAllBlock(board, block):
			for adjacent in block[i].sameAdjacent(board):
				if adjacent not in block:
					block.append(adjacent)
			i += 1
		return block

	def __str__(self):
		return '(' + str(self.row) + ', ' + str(self.column) + ', ' + self.color + ')'

	def __repr__(self):
		return '(' + str(self.row) + ', ' + str(self.column) + ', ' + self.color + ')'

	def __eq__(self, other):
		return self.row == other.row and self.column == other.column and self.color == other.color

BOARD = [[Brick(row, column, board_string[row * 10 + column]) for column in range(10)] for row in range(10)]	
TESTBOARD = [[Brick(row, column, board_string2[row * 10 + column]) for column in range(10)] for row in range(10)]	

# Returns whether bricks contains a complete block in board 
def hasAllBlock(board, bricks):
	for brick in bricks:
		for adjacents in brick.sameAdjacent(board):
			if adjacents not in bricks:
				return False
	return True

# Returns a new board with all empty spaces dealt with (aside from empty columns)
def fillEmpty(board):
	new_board = copyBoard(board)
	row = 9
	while row >= 1:
		for brick in new_board[row]:
			if brick.color == '*':
				top = new_board[row - 1][brick.column]
				while top.color == '*' and top.row > 0:
					top = new_board[top.row - 1][brick.column]
				if top.color != '*':
					new_board[row][brick.column] = Brick(row, brick.column, top.color)
					new_board[top.row][brick.column] = Brick(top.row, brick.column, '*')
		row -= 1

	return new_board

# Returns a new board with columns shifted such that column is an empty column
def fillEmptyColumn(board, column):
	assert isEmptyColumn(board, column)

	new_board = copyBoard(board)

	while column != 9:
		for row in range(10):
			new_board[row][column] = Brick(row, column, new_board[row][column + 1].color)
		column += 1

		if column == 9:
			for row in range(10):
				new_board[row][9] = Brick(row, 9, '*')

	return new_board

# Returns whether column in board is empty
def isEmptyColumn(board, column):
	for row in range(10):
		if board[row][column].color != '*':
			return False
	return True

# Returns whether every column to the right of column including column is empty
def isEmptyRightColumns(board, column):
	for col in range(column, 10):
		for row in range(10):
			if board[row][col].color != '*':
				return False
	return True

# Prints the board in a readable 10 x 10 grid
def printBoard(board):
	for row in board:
		for brick in row:
			print(brick.color, end = " ")
		print()

# Returns a new board if brick's block is popped
def pop(board, brick):
	new_board = copyBoard(board)
	block = brick.block(new_board)
	if len(block) <= 1:
		return new_board
	for row in new_board:
		for item in row:
			if item in block:
				new_board[item.row][item.column] = Brick(item.row, item.column, '*')
	new_board = fillEmpty(new_board)
	for column in range(10):
		while isEmptyColumn(new_board, column) and not isEmptyRightColumns(new_board, column):
			new_board = fillEmptyColumn(new_board, column)

	return new_board

# Returns all possible ways to finish the game
def finishGame(board):
	unique_pops = uniquePops(board)
	ways = []

	if emptyBoard(board):
		return [[]]

	for unique_pop in unique_pops:
		ways += [[unique_pop] + way for way in finishGame(pop(board, unique_pop))]

	return ways

# Returns whether any item in contains is in lst
def hasAny(lst, contains):
	for i in contains:
		if i in lst:
			return True
	return False

# Returns whether board in entirety is empty
def emptyBoard(board):
	for row in board:
		for brick in row:
			if brick.color != '*':
				return False
	return True

# Returns a list of all possible unique pops in board
def uniquePops(board):
	unique_pops = []
	for row in board:
		for brick in row:
			block = brick.block(board)
			if len(block) > 1 and not hasAny(unique_pops, block):
				unique_pops.append(brick)
	return unique_pops

# Returns a copy of the board
def copyBoard(board):
	new_board = [[] for i in range(10)]
	for row in board:
		for brick in row:
			new_board[brick.row].append(Brick(brick.row, brick.column, brick.color))
	return new_board

# Returns the score of popping brick in board
def popScore(board, brick):
	num_blocks = len(brick.block(board))
	return num_blocks * (num_blocks - 1)

# Returns the score of the entire game if way is completed
def gameScore(board, way):
	new_board = copyBoard(board)
	score = 0
	for brick in way:
		score += popScore(new_board, brick)
		new_board = pop(new_board, brick)
	return score

# Prints in a readable format the steps to get the maximum score
def bestFinish(board):
	ways = finishGame(board)
	if len(ways) == 0:
		print("No win possible")
	else:
		best_way = max(ways, key = lambda way: gameScore(board, way))
		for i in range(1, len(best_way) + 1):
			print(str(i) + '.', 'Pop', best_way[i - 1])
		print('Score:', gameScore(board, best_way))

def replace_all(board, bricks):
	new_board = board[:]
	for brick in bricks:
		new_board = replace(new_board, brick)
	return new_board


def replace(board, brick):
	return [[board[row][column] if brick != board[row][column] else Brick(row, column, '*') for column in range(10)] for row in range(10)]


