import sys, string
import numpy as np
from collections import Counter

# Leet translation
leet = {'a': '@',
        'b': '8',
        'e': '3',
        'o': '0',
        's': '$',
        't': '7'}


# Takes a text file as input, replaces alphabet with leet characters, and returns array with words as strings
def leetify(file):
    # Read as characters
    characters = np.array([' '] + list(open(file).read()) + [' '])

    # Normalize
    characters[~np.char.isalpha(characters)] = ' '
    characters = np.char.lower(characters)

    # Leetify
    translate = lambda x: leet.get(x, x)
    characters = np.vectorize(translate)(characters)

    # Split the words by finding the indices of delimiter
    sp = np.where(characters == ' ')

    # A little trick: let's double each index, and then take pairs
    sp2 = np.repeat(sp, 2)

    # Get the pairs as a 2D matrix, skip the first and the last
    w_ranges = np.reshape(sp2[1:-1], (-1, 2))

    # Remove the indexing to the spaces themselves
    w_ranges = w_ranges[np.where(w_ranges[:, 1] - w_ranges[:, 0] > 2)]

    # Voila! Words are in between spaces, given as pairs of indices
    words = list(map(lambda r: characters[r[0]:r[1]], w_ranges))

    # Let's recode the characters as strings
    swords = np.array(list(map(lambda w: ''.join(w).strip(), words)))
    return swords


#
# The main function
#
words = leetify(sys.argv[1])
stop_words = leetify("../stop_words.txt")

# Next, let's remove stop words
non_stop_words = words[~np.isin(words, stop_words)]

# Find all bi-grams
bi_grams = zip(non_stop_words, non_stop_words[1:])
count = Counter(bi_grams)

# Print 5 most common bi-grams
for w, c in count.most_common(5):
    print(w[0], w[1], end='')
    print(' - ', end='')
    print(c)
