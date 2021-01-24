# Letterbox
# Chose to do this homework in Ruby due to its similarities to Python being a dynamically typed object oriented language.

#
# Models the contents of the file
#
class DataStorageManager
  @data = ''

  def dispatch(message)
    init(message[1]) if message[0] == 'init'
    words if message[0] == 'words'
  end

  def init(path_to_file)
    @data = File.read(path_to_file).downcase.gsub(/[^a-z0-9]/, ' ')
  end

  # Returns the list words in storage
  def words
    @data.split(' ')
  end

end

#
# Models the stop word filter
#
class StopWordManager
  def dispatch(message)
    init if message[0] == 'init'
    is_stop_word(message[1]) if message[0] == 'is_stop_word'
  end

  def init
    @stop_words = File.read('stop_words.txt').downcase.split(',')
  end

  def is_stop_word(word)
    @stop_words.include? word or word.length < 2
  end
end

#
# Keeps the word frequency data
#
class WordFrequencyManager

  def dispatch(message)
    increment_count(message[1]) if message[0] == 'increment_count'
    sorted if message[0] == 'sorted'
  end

  def increment_count(word)
    if @word_frequencies.nil?
      @word_frequencies = {}
      @word_frequencies[word] = 1
    elsif @word_frequencies.key? word
      @word_frequencies[word] += 1
    else
      @word_frequencies[word] = 1
    end
  end

  def sorted
    @word_frequencies.sort_by {|_k,v| v}.reverse[0..24]
  end
end

#
# Word frequency controller
#
class WordFrequencyController
  def dispatch(message)
    init(message[1]) if message[0] == 'init'
    run if message[0] == 'run'
  end

  def init(path_to_file)
    @storage_manager = DataStorageManager.new
    @stop_word_manager = StopWordManager.new
    @word_frequency_manager = WordFrequencyManager.new
    @storage_manager.dispatch(['init', path_to_file])
    @stop_word_manager.dispatch(['init'])
  end

  def run
    @storage_manager.dispatch(['words']).each do |word|
      unless @stop_word_manager.dispatch(['is_stop_word', word])
        @word_frequency_manager.dispatch(['increment_count', word])
      end
    end

    word_frequencies = @word_frequency_manager.dispatch(['sorted'])
    word_frequencies.each do |(word, count)|
      puts "#{word} - #{count}"
    end
  end
end

#
# The main function
#
wf_controller = WordFrequencyController.new
wf_controller.dispatch(['init', ARGV[0]])
wf_controller.dispatch(['run'])
