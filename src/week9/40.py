import random
import string
import sys

import numpy as np
from keras.layers import Dense, SimpleRNN
from keras.models import Sequential

#
# Exercise 40.2, Exercises in Programming Style:
# Change the example program so that it also eliminates 2-letter words.
#

characters = string.printable
char_indices = dict((c, i) for i, c in enumerate(characters))
indices_char = dict((i, c) for i, c in enumerate(characters))

INPUT_VOCAB_SIZE = len(characters)
BATCH_SIZE = 200
HIDDEN_SIZE = 100
TIME_STEPS = 5


def encode_one_hot(line):
    x = np.zeros((len(line), INPUT_VOCAB_SIZE))
    for i, c in enumerate(line):
        index = char_indices[c] if c in characters else char_indices[' ']
        x[i][index] = 1
    return x


def decode_one_hot(x):
    s = []
    for onehot in x:
        one_index = np.argmax(onehot)
        s.append(indices_char[one_index])
    return ''.join(s)


def prepare_for_rnn(x):
    # All slices of size TIME_STEPS, sliding through x
    ind = [np.array(np.arange(i, i + TIME_STEPS)) for i in range(x.shape[0] - TIME_STEPS + 1)]
    ind = np.array(ind, dtype=np.int32)
    x_rnn = x[ind]
    return x_rnn


def input_generator(nsamples):
    def generate_line():
        inline = ['  ']
        outline = []
        for _ in range(nsamples):
            c = random.choice(characters)
            expected = c.lower() if c in string.ascii_letters else ' '
            inline.append(c)
            outline.append(expected)
        inline.append('  ')
        for i in range(nsamples):
            if outline[i] == ' ': continue
            #
            # One letter words:
            #
            # Middle case:
            if i > 0 and i < nsamples - 1:
                # Eliminates one-letter words
                if outline[i - 1] == ' ' and outline[i + 1] == ' ':
                    outline[i] = ' '
            # Beginning or End case:
            if (i == 0 and outline[1] == ' ') or (i == nsamples - 1 and outline[nsamples - 2] == ' '):
                outline[i] = ' '
            #
            # Two letter words:
            #
            # Middle case:
            if 1 < i < nsamples - 1:
                if outline[i - 2] == ' ' and outline[i + 1] == ' ':
                    outline[i] = ' '
                    outline[i - 1] = ' '
            if 0 < i < nsamples - 2:
                if outline[i - 1] == ' ' and outline[i + 2] == ' ':
                    outline[i] = ' '
                    outline[i + 1] = ' '

            # Beginning or End case:
            if (i == 0 and outline[2] == ' ') or (i == nsamples - 2 and outline[nsamples - 3] == ' '):
                outline[i] = ' '
                outline[i + 1] = ' '

        return ''.join(inline), ''.join(outline)

    while True:
        input_data, expected = generate_line()
        data_in = encode_one_hot(input_data)
        data_out = encode_one_hot(expected)
        yield prepare_for_rnn(data_in), data_out


def train(model):
    model.compile(loss='categorical_crossentropy',
                  optimizer='adam',
                  metrics=['accuracy'])
    input_gen = input_generator(BATCH_SIZE)
    validation_gen = input_generator(BATCH_SIZE)
    model.fit_generator(input_gen,
                        epochs=100, workers=1,
                        steps_per_epoch=50,
                        validation_data=validation_gen,
                        validation_steps=10)


def build_model():
    model = Sequential()
    model.add(SimpleRNN(HIDDEN_SIZE, input_shape=(None, INPUT_VOCAB_SIZE)))
    model.add(Dense(INPUT_VOCAB_SIZE, activation='softmax'))
    print(model)
    return model


model = build_model()
model.summary()
train(model)

input("Network has been trained. Press <Enter> to run program.")
with open(sys.argv[1]) as f:
    for line in f:
        if line.isspace(): continue
        batch = prepare_for_rnn(encode_one_hot('  ' + line + '  '))
        preds = model.predict(batch)
        normal = decode_one_hot(preds)
        print(normal)
