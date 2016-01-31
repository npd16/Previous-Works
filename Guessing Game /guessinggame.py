# Define a function to retrieve the answer.
def get_answer(guess):

  # Initialize answer
  answer = ''

  # We only want to accept one of these values.
  while not answer in ['correct', 'higher', 'lower']:
    answer = raw_input("Is your age " + str(guess) + "? Enter correct, higher, or lower:\n")

    # Convert the value to lower case.
    answer = answer.lower()

  return answer


print 'Welcome to guessing game!'

low =  0
high = 126
answer = ''
guess = (high + low) / 2

while answer != 'correct':
	answer = get_answer(guess)
		
	if answer == 'lower':
		high = guess
	elif answer == 'higher':	
		low = guess
	guess = (high + low) / 2
	
print 'Yea I thought so'
	