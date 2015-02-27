class DiscreteDish
    attr_accessor :piece, :initial, :decay
    def initialize(piece, initial, decay)
        @piece = piece
        @initial = initial
        @decay = decay
    end

    def to_s
        "D #{piece} #{initial} #{decay}"
    end
end

class ContinuousDish
    attr_accessor :initial, :decay
    def initialize(initial, decay)
        @initial = initial
        @decay = decay
    end

    def to_s
        "C #{initial} #{decay}"
    end
end

def randPieces(sides, max)
    ([0] * sides).map{rand(max / sides)}.inject(:+)
end

def rndDisc(w)
    piece = randPieces(50, w / 100).to_i
    piece = 1 if piece <= 0
    taste = 15000
    taste = randPieces(3, 15000) until taste < 10000
    decay = rand(taste)
    DiscreteDish.new(piece, taste, decay)
end

def rndCon
    taste = 15000
    taste = randPieces(3, 15000) until taste < 10000
    decay = rand(taste)
    ContinuousDish.new(taste, decay)
end

def rndDish(pctCon, w)
    if (rand() < pctCon)
        rndCon
    else
        rndDisc(w)
    end
end

def write_case(casenum, d, w, pctCon)
    f = File.new "../inputs/BuffedBuffet.in#{casenum}", "w"
    f.puts "#{d} #{w}"
    for i in (1..d)
        f.puts rndDish(pctCon, w)
    end
    f.close
end
